package com.zebstudios.cityexpress;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.InputFilter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.zebstudios.calendar.CalendarFragment;
import com.zebstudios.calendar.CalendarListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.appsee.Appsee.addEvent;
import static com.appsee.Appsee.startScreen;

public class HotelReservaFragment extends Fragment
{
	private View _view;
	private Hotel _hotel;

	private Date _arrivalDate;
	private Button _btnArrival;
	private CalendarFragment _arrivalCalendarFragment;
	private CalendarListener _arrivalCalendarListener;
	private Date _departureDate;
	private Button _btnDeparture;
	private CalendarFragment _departureCalendarFragment;
	private CalendarListener _departureCalendarListener;

	private ProgressDialogFragment _progress;
	private String _lastTarifaFetched;
	private String _lastPromoCode;
	private ArrayList<RoomAvailable> _rooms;
	private int _totalHabitaciones;
	private int _currentRoom;

	private ArrayList<PromoCode> _promocode;

	public HotelReservaFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		startScreen("ViewHotelReserva-Smartphone");
		_view = inflater.inflate( R.layout.fragment_hotel_reserva, container, false );
		_hotel = (Hotel) getArguments().getSerializable( "HOTEL" );

		PrepareArrivalCalendar();
		PrepareDepartureCalendar();

		Button btnAvailability = (Button) _view.findViewById( R.id.btnAvailability );
		btnAvailability.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAvailability();
				addEvent("HotelCheckAvailability-Smartphone");
			}
		});



		CirclePageIndicator indicator = (CirclePageIndicator) _view.findViewById( R.id.indicator );

		final float density = getResources().getDisplayMetrics().density;
		indicator.setRadius( 2 * density );
		indicator.setPageColor( 0xFF264b89 );
		indicator.setFillColor( 0xFF264b89 );
		indicator.setStrokeColor( 0xFF9daeca );
		indicator.setStrokeWidth( 1 * density );

		indicator.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected( int i )
			{
				setRoomSelectedIndex( i );
			}
		} );

		// http://stackoverflow.com/questions/8184597/how-do-i-make-a-portion-of-a-checkboxs-text-clickable
		CheckBox cbAgree1 = (CheckBox) _view.findViewById( R.id.cbAgree1 );
		TextView textAgree1 = (TextView) _view.findViewById( R.id.txtAgree1 );
		cbAgree1.setText( "" );
		textAgree1.setText( Html.fromHtml( "He leído y estoy de acuerdo con las <a href='https://www.cityexpress.com/es/politicas-de-cambios-y-cancelacion-de-reservaciones/'>políticas de cambios y cancelación de reservaciones</a>." ) );
		textAgree1.setClickable( true );
		textAgree1.setMovementMethod( LinkMovementMethod.getInstance() );

		CheckBox cbAgree2 = (CheckBox) _view.findViewById( R.id.cbAgree2 );
		TextView textAgree2 = (TextView) _view.findViewById( R.id.txtAgree2 );
		cbAgree2.setText( "" );
		textAgree2.setText( Html.fromHtml( "Acepto que mis datos personales sean tratados conforme a los términos de <a href='https://www.cityexpress.com/es/politica-y-aviso-de-privacidad'>aviso de privacidad</a> y <a href='https://www.cityexpress.com/es/terminos-y-condiciones-de-uso/'>términos y condiciones de uso</a>." ) );
		textAgree2.setClickable( true );
		textAgree2.setMovementMethod( LinkMovementMethod.getInstance() );

		Spinner spinHabitaciones = (Spinner) _view.findViewById( R.id.spinHabitaciones );
		SpinnerAdapter adap = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, new String[]{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" } );
		spinHabitaciones.setAdapter( adap );
		spinHabitaciones.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
			{
				changeHabitaciones( position + 1 );
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent )
			{
				// FATAL: No debería ocurrir
			}
		} );

		Button btnContinue = (Button) _view.findViewById( R.id.btnContinue );
		btnContinue.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				nextStep();
				addEvent("HotelContinueReserva-Smartphone");
			}
		} );

		_totalHabitaciones = 1;

		resetCapture();

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppEventTrack( "HOTEL DETAIL ANDROID", "RESERVA 1", "HOTEL", _hotel.getNombre(), 1 );
		_promocode = null;

		EditText txtPromoCode = (EditText) _view.findViewById( R.id.txtPromocode );
		txtPromoCode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

		return _view;
	}

	private void checkAvailability()
	{
		if( _arrivalDate == null || _departureDate == null )
		{
			alert( "Selecciona las fechas para tu estancia en este hotel." );
			return;
		}


		checkAvailability( null );
	}

	private void resetCapture()
	{
		ImageView imgElige = (ImageView) _view.findViewById( R.id.imgElige );
		LinearLayout pnlRooms = (LinearLayout) _view.findViewById( R.id.pnlRooms );
		LinearLayout pnlContinue = (LinearLayout) _view.findViewById( R.id.pnlContinue );
		imgElige.setVisibility( View.VISIBLE );
		pnlRooms.setVisibility( View.GONE );
		pnlContinue.setVisibility( View.GONE );
		_currentRoom = 0;
		Spinner spinHabitaciones = (Spinner) _view.findViewById( R.id.spinHabitaciones );
		spinHabitaciones.setSelection( 0 );

		CheckBox cbAgree1 = (CheckBox) _view.findViewById( R.id.cbAgree1 );
		CheckBox cbAgree2 = (CheckBox) _view.findViewById( R.id.cbAgree2 );
		cbAgree1.setChecked( false );
		cbAgree2.setChecked( false );

		ViewPager viewPager = (ViewPager) _view.findViewById( R.id.view_pager );
		viewPager.setCurrentItem( 0 );
		viewPager.setAdapter( null );
	}


	private void checkAvailability( String tarifa )
	{

		// TODO: Validar promoCode?
		EditText txtPromoCode = (EditText) _view.findViewById( R.id.txtPromocode );



		long timeOne = _arrivalDate.getTime();
		long timeTwo = _departureDate.getTime();
		long oneDay = 1000 * 60 * 60 * 24;
		long delta = ( timeTwo - timeOne ) / oneDay;

		ReservationEngineClient.PromoRequestModel promo = new ReservationEngineClient.PromoRequestModel();
		promo.setHotel(_hotel.getSiglas());
		promo.setNumeroDeNoches((int) delta);
		promo.setNumeroAdultos(1);
		promo.setNumeroHabitaciones(1);
		promo.setFechaInicial(_arrivalDate);
		if( tarifa != null
				&& ( tarifa.equalsIgnoreCase( "1111" )
				|| tarifa.equalsIgnoreCase( "1211" )
				|| tarifa.equalsIgnoreCase( "1311" ) ) )
		{
			_lastTarifaFetched = tarifa;
			promo.setCodigoPromocion( "" );
		}
		else if( txtPromoCode.getText().toString().trim().length() > 0 )
		{
			if( _hotel.getPais().equalsIgnoreCase( "MX" ) )
			{
				_lastTarifaFetched = "1168";
			}
			else if( _hotel.getPais().equalsIgnoreCase( "CO" ) )
			{
				_lastTarifaFetched = "1368";
			}
			else
			{
				_lastTarifaFetched = "1268";
			}
			_lastPromoCode = txtPromoCode.getText().toString().trim();
			promo.setCodigoPromocion( txtPromoCode.getText().toString().trim() );
		}
		else
		{
			if( _hotel.getPais().equalsIgnoreCase( "MX" ) )
			{
				_lastTarifaFetched = "1114";
			}
			else if( _hotel.getPais().equalsIgnoreCase( "CO" ) )
			{
				_lastTarifaFetched = "1314";
			}
			else
			{
				_lastTarifaFetched = "1214";
			}
			promo.setCodigoPromocion( "" );
		}
		promo.setCodigoTarifa(_lastTarifaFetched);

		if (_promocode != null) {
			for (PromoCode temp : _promocode) {
				Log.e("HotelReservafragment", "Interacion  -- " + temp.getnumpromocode() + " - " + txtPromoCode.getText());

				String t1 = txtPromoCode.getText().toString();
				String t2 = temp.getnumpromocode();

				if (t1.equals(t2)) {
					promo.setCodigoTarifa(temp.getCodigoTarifa().replaceAll("xx", "11"));
					Log.e("hotelReservaFragment", "Si es igual _3:3:·:3:··:");
					break;
				}
			}
			Log.e("HotelReservafragment", "Tarifa--> " + promo.getCodigoTarifa());
		}

		Log.e("TEST", "GET: " + promo.getHotel());
		new ServiceCaller( SERVICE_GETROOMSAVAILABLEPROMO, promo ).execute();
	}

	private void roomsAvailablesObtained( ReservationEngineClient.GetRoomsAvailablePromoResult result )
	{
		android.util.Log.d( "TEST", "RESPONSE: " + result.getResponse() );
		if( result.getResponse() == 0 )
		{
			android.util.Log.d( "TEST", "TOTAL: " + result.getAvailables().size() );
			if( result.getAvailables().size() > 0 )
			{
				android.util.Log.d( "TEST", result.getAvailables().get( 0 ).getDescError() );
				if( result.getAvailables().get( 0 ).getCodigoError().length() == 0 )
				{
					ArrayList<RoomAvailable> rooms = new ArrayList<RoomAvailable>();
					String habitaciones = "";

					ArrayList<ReservationEngineClient.Available> availables = result.getAvailables();
					for( int i = 0; i < availables.size(); i++ )
					{
						ReservationEngineClient.Available a = availables.get( i );
						if( a.getCodigoTarifa().equalsIgnoreCase( _lastTarifaFetched ) )
						{
							if( _lastTarifaFetched.equalsIgnoreCase( "1168" ) || _lastTarifaFetched.equalsIgnoreCase( "1268" ) || _lastTarifaFetched.equalsIgnoreCase( "1368" ) )
							{
								// TODO: Verificar que la lógica de las promociones es correcta.
								ArrayList<ReservationEngineClient.HabPromo> habPromos = a.getPromotions();
								for( int j = 0; j < habPromos.size(); j++ )
								{
									ReservationEngineClient.HabPromo habPromo = habPromos.get( j );
									ArrayList<ReservationEngineClient.Promotion> promotions = habPromo.getPromotions();
									for( int k = 0; k < promotions.size(); k++ )
									{
										ReservationEngineClient.Promotion promo = promotions.get( k );
										if( promo.getCodPromo().equalsIgnoreCase( _lastPromoCode ) )
										{
											RoomAvailable r = new RoomAvailable( habPromo, _lastPromoCode, _hotel.getPais() );
											r.setTarifaCode( _lastTarifaFetched );
											r.setPromoCode( _lastPromoCode );
											rooms.add( r );
											habitaciones += r.getCode() + ",";
										}
									}
								}
							}
							else
							{
								ArrayList<ReservationEngineClient.HabBase> habs = a.getTarifaBase();
								for( int j = 0; j < habs.size(); j++ )
								{
									ReservationEngineClient.HabBase habit = habs.get( j );
									if( habit.getNoches().size() > 0 )
									{
										RoomAvailable r = new RoomAvailable( habit, _hotel.getPais() );
										r.setTarifaCode( _lastTarifaFetched );
										r.setPromoCode( "" );
										rooms.add( r );
										habitaciones += r.getCode() + ",";
									}
								}
							}

							break;
						}
					}

					for( int i=0; i<rooms.size(); i++ )
					{
						if( rooms.get( i ).getCode().equalsIgnoreCase( "HCP" ) )
						{
							rooms.remove( i );
							break;
						}
					}

					if( rooms.size() > 0 )
					{
						habitaciones = habitaciones.substring( 0, habitaciones.length() - 1 );
						_rooms = rooms;
						new CompleteRoomsInfo( habitaciones ).execute();
					}
					else
					{
						// No hay habitaciones disponibles con la tarifa que se buscó.
						// si la tarifa es 1114, volvemos a buscar pero ahora con la
						// tarifa 1111
						if( _lastTarifaFetched.equalsIgnoreCase( "1114" ) )
						{
							checkAvailability( "1111" );
						}
						else if( _lastTarifaFetched.equalsIgnoreCase( "1314" ) )
						{
							checkAvailability( "1311" );
						}
						else if( _lastTarifaFetched.equalsIgnoreCase( "1214" ) )
						{
							checkAvailability( "1211" );
						}
						else if( _lastTarifaFetched.equalsIgnoreCase( "1168" ) || _lastTarifaFetched.equalsIgnoreCase( "1268" ) || _lastTarifaFetched.equalsIgnoreCase( "1368" ) )
						{
							alert( "No se encontraron habitaciones disponibles con el Promocode que proporcionaste en las fechas que seleccionaste." );
						}
						else
						{
							alert( "No se econtraron habitaciones disponibles en las fechas que seleccionaste." );
						}
					}
				}
				else
				{
					android.util.Log.w( "TEST", result.getAvailables().get( 0 ).getCodigoError() );
					alert( "No se ha podido comprobar la disponibilidad. Por favor intente más tarde." );
				}
			}
			else
			{
				alert( "No se ha podido comprobar la disponibilidad. Por favor intente más tarde." );
			}
		}
		else
		{
			alert( "No se ha podido comprobar la disponibilidad. Por favor intente más tarde." );
		}
	}

	private void ExtrasObtained()
	{
		ImageView imgElige = (ImageView) _view.findViewById( R.id.imgElige );
		LinearLayout pnlRooms = (LinearLayout) _view.findViewById( R.id.pnlRooms );
		LinearLayout pnlContinue = (LinearLayout) _view.findViewById( R.id.pnlContinue );
		imgElige.setVisibility( View.GONE );
		pnlRooms.setVisibility( View.VISIBLE );
		pnlContinue.setVisibility( View.VISIBLE );

		ViewPager viewPager = (ViewPager) _view.findViewById( R.id.view_pager );
		RoomAvailablesPagerAdapter adapter = new RoomAvailablesPagerAdapter( getActivity(), _rooms );
		viewPager.setAdapter( adapter );

		CirclePageIndicator indicator = (CirclePageIndicator) _view.findViewById( R.id.indicator );
		indicator.setViewPager( viewPager );

		setRoomSelectedIndex(0);
	}

	private void setRoomSelectedIndex( int page )
	{
		_currentRoom = page;
		RoomAvailable room = _rooms.get( page );
		TextView lblTotal = (TextView) _view.findViewById( R.id.lblTotal );
		TextView lblProm = (TextView) _view.findViewById( R.id.lblProm );
		lblTotal.setText(String.format("Total: $%,.2f ", room.getTotal() * _totalHabitaciones) + room.getMoneda());
		lblProm.setText(String.format("Tarifa base para 2 adultos (impuestos incluídos) $%,.2f ", room.getTarifaProm()) + room.getMoneda());
	}

	private void changeHabitaciones( int total )
	{
		_totalHabitaciones = total;
		setRoomSelectedIndex(_currentRoom);
	}

	private void nextStep()
	{
		CheckBox cbAgree1 = (CheckBox) _view.findViewById( R.id.cbAgree1 );
		CheckBox cbAgree2 = (CheckBox) _view.findViewById( R.id.cbAgree2 );

		if( !cbAgree1.isChecked() )
		{
			alert( "Debes aceptar las políticas de cambios y cancelaciones de reservaciones para continuar" );
			return;
		}

		if( !cbAgree2.isChecked() )
		{
			alert( "Debes aceptar el aviso de privacidad y términos y condiciones de uso" );
			return;
		}

		Bundle parameters = new Bundle();
		parameters.putSerializable( "HOTEL", _hotel );
		parameters.putSerializable( "ROOM", _rooms.get( _currentRoom ) );
		parameters.putSerializable( "ARRIVAL", _arrivalDate );
		parameters.putSerializable( "DEPARTURE", _departureDate );
		parameters.putInt( "TOTAL_ROOMS", _totalHabitaciones );

		HotelReserva2Fragment fragment = new HotelReserva2Fragment();
		fragment.setArguments( parameters );
		getFragmentManager().beginTransaction().add( R.id.fragment_container, fragment ).commit();
	}

	//PromoCode
	void AbrirPormo(){

		Log.i("HotelReservaFragment", "Abrir");
		PromoCodeFragment fragment = new PromoCodeFragment();
		fragment.setTargetFragment(this, 101 );
		getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();

		/*
		StatesFragment fragment = new StatesFragment();
		fragment.setTargetFragment(this, FRAGMENT_LIST_STATES);
		getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();
		 */
	}

	//Escribir en edittext
	void escribirPromocode(PromoCode promocode ){
		EditText txtPromoCode = (EditText) _view.findViewById( R.id.txtPromocode );
		txtPromoCode.setText(promocode.getnumpromocode());
	}

	//Arreglo de Promocodes para validar
	void setPromoCodeArray(ArrayList<PromoCode> promocode){
		_promocode = promocode;
		Log.e("HotelReservaFragment", "Arreglo de promocode :3");
	}

	private void PrepareArrivalCalendar()
	{
		_arrivalDate = null;
		_btnArrival = (Button) _view.findViewById( R.id.btnArrival );

		_arrivalCalendarListener = new CalendarListener()
		{
			@Override
			public void onSelectDate( Date date, View view )
			{
				_arrivalDate = date;
				if( _departureDate != null && _departureDate.compareTo( _arrivalDate ) <= 0 )
				{
					_btnDeparture.setText( "Elegir" );
					_departureDate = null;
				}
				SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
				_btnArrival.setText( sdf.format( date ) );
				_arrivalCalendarFragment.dismiss();

				if( _departureDate == null )
				{
					Calendar c = Calendar.getInstance();
					c.setTime( date );
					c.add( Calendar.DATE, 1 );

					_departureDate = c.getTime();
					_btnDeparture.setText( sdf.format( _departureDate ) );
				}

				resetCapture();
			}
		};

		Bundle bundle = new Bundle();
		bundle.putString( CalendarFragment.DIALOG_TITLE, "Llegada" );
		_arrivalCalendarFragment = new CalendarFragment();
		_arrivalCalendarFragment.setCalendarListener( _arrivalCalendarListener );
		_arrivalCalendarFragment.setArguments( bundle );

		_btnArrival.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{

				Calendar cal = Calendar.getInstance();
				Date d = cal.getTime();
				if( _arrivalDate != null )
				{
					_arrivalCalendarFragment.setSelectedDates( _arrivalDate, _arrivalDate );
				}
				_arrivalCalendarFragment.setMinDate( d );
				_arrivalCalendarFragment.show( getFragmentManager(), "FROM_CALENDAR_FRAGMENT" );
			}
		} );
	}

	private void PrepareDepartureCalendar()
	{
		_departureDate = null;
		_btnDeparture = (Button) _view.findViewById( R.id.btnDeparture );

		_departureCalendarListener = new CalendarListener()
		{
			@Override
			public void onSelectDate( Date date, View view )
			{
				_departureDate = date;
				if( _arrivalDate != null && _arrivalDate.compareTo( _departureDate ) >= 0 )
				{
					_btnArrival.setText( "Elegir" );
					_arrivalDate = null;
				}
				SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
				_btnDeparture.setText( sdf.format( date ) );
				_departureCalendarFragment.dismiss();
				resetCapture();
			}
		};

		Bundle bundle = new Bundle();
		bundle.putString( CalendarFragment.DIALOG_TITLE, "Salida" );
		_departureCalendarFragment = new CalendarFragment();
		_departureCalendarFragment.setCalendarListener( _departureCalendarListener );
		_departureCalendarFragment.setArguments( bundle );

		_btnDeparture.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				Calendar cal = Calendar.getInstance();
				//if( _arrivalDate != null )
				//	cal.setTime( _arrivalDate );
				cal.add( Calendar.DATE, 1 );
				Date d = cal.getTime();
				if( _departureDate != null )
				{
					_departureCalendarFragment.setSelectedDates( _departureDate, _departureDate );
				}
				_departureCalendarFragment.setMinDate( d );
				_departureCalendarFragment.show( getFragmentManager(), "TO_CALENDAR_FRAGMENT" );
			}
		} );
	}

	private static final int SERVICE_GETROOMSAVAILABLEPROMO = 100;

	private class ServiceCaller extends AsyncTask<Void, Void, Void>
	{
		private int _task;
		private Object _param;
		private Object _response;

		public ServiceCaller( int task, Object param )
		{
			_task = task;
			_param = param;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			_progress = ProgressDialogFragment.newInstance();
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		@Override
		protected Void doInBackground( Void... arg0 )
		{
			ReservationEngineClient client = new ReservationEngineClient();

			if( _task == SERVICE_GETROOMSAVAILABLEPROMO )
			{
				ReservationEngineClient.PromoRequestModel promo = (ReservationEngineClient.PromoRequestModel) _param;
				_response = client.GetRoomsAvailablePromo( promo );
			}

			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			_progress.dismiss();

			if( _task == SERVICE_GETROOMSAVAILABLEPROMO )
			{
				ReservationEngineClient.GetRoomsAvailablePromoResult result = (ReservationEngineClient.GetRoomsAvailablePromoResult) _response;
				roomsAvailablesObtained( result );
			}
		}
	}

	private void AppRatealert(){

	AppRater appRater = new AppRater(getActivity());
	//Dias en los que se volvera a lanzar la alerta
	appRater.setDaysBeforePrompt(5);
	//Numero de veces que se tenga que realizar la accion
	appRater.setLaunchesBeforePrompt(3);
	appRater.setPhrases("Califica nuestra APP", "En City Express nos gusta escuchar tu opinión. Califica nuestra APP, te tomará 1 minuto. ¡Gracias por tu tiempo!", "Calificar City Express", "Recordar mas tarde", "No, gracias");
	appRater.setTargetUri("https://play.google.com/store/apps/details?id=%1$s");
	appRater.show();
	//appRater.demo();

	}

	private class CompleteRoomsInfo extends AsyncTask<Void, Void, Void>
	{
		String _codes;
		ArrayList<RoomAvailableExtra> _extras;

		public CompleteRoomsInfo( String codes )
		{
			_codes = codes;
			_extras = new ArrayList<RoomAvailableExtra>();
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			_progress = ProgressDialogFragment.newInstance();
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		@Override
		protected Void doInBackground( Void... arg0 )
		{
			String url = APIAddress.HOTELS_API_MOBILE + "/GetHabitaciones?HotelCode=" + _hotel.getSiglas() + "&RoomCodes=" + _codes;
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall( url, ServiceHandler.GET );
			Log.e("TEST", "URL: " + url + " -  json -- " + jsonStr);

			if( jsonStr != null )
			{
				try
				{
					Log.e("HotelReservaFragment", "JSONSTR --> " + jsonStr);


					JSONArray results = new JSONArray( jsonStr );
					for( int i = 0; i < results.length(); i++ )
					{
						JSONObject h = results.getJSONObject( i );
						RoomAvailableExtra r = new RoomAvailableExtra( h );
						if( !r.isParsedOk() )
						{
							return null;
						}
						_extras.add( r );
					}




					return null;
				}
				catch( Exception e )
				{
					android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
					return null;
				}
			}
			else
			{
				android.util.Log.e( "ServiceHandler", "Couldn't get any data" );
			}
			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			AppRatealert();


			for( int i = 0; i < _rooms.size(); i++ )
			{
				RoomAvailableExtra x = GetExtra( _rooms.get( i ).getCode() );
				if( x != null )
				{
					_rooms.get( i ).setMaxAdultos( x.getNumPersonas() );
					_rooms.get( i ).setImagen( x.getImagenApp() );
					_rooms.get( i ).setDescription( x.getDescipcionLarga() );
					_rooms.get( i ).setTitle( x.getDescripcion() );

					if( _rooms.get( i ).getMaxAdultos() != _rooms.get( i ).getMaxAdultosFromTable() )
					{
						// TODO: Usar el valor de Max Adultos de la info extra o de la tabla del email?
						android.util.Log.w( "TEST", "INCOMPATIBILIDAD NUMERO ADULTOS" );
					}
				}
			}

			_progress.dismiss();
			ExtrasObtained();
		}

		private RoomAvailableExtra GetExtra( String code )
		{
			for( int i = 0; i < _extras.size(); i++ )
			{
				if( _extras.get( i ).getCodigoHabitacion().equalsIgnoreCase( code ) )
				{
					return _extras.get( i );
				}
			}

			return null;
		}
	}

	private void alert( String message )
	{
		AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
		alert.setTitle( "Atención" );
		alert.setMessage( message );
		alert.setIcon( R.drawable.notification_warning_small );
		alert.setCancelable( false );
		alert.setButton( DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
			}
		} );
		alert.show();
	}
}
