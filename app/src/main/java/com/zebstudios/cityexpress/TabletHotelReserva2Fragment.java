package com.zebstudios.cityexpress;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.appsee.Appsee.startScreen;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class TabletHotelReserva2Fragment extends Fragment implements PayPalCaller.PayPalCallerInterface
{
	private Hotel _hotel;
	private RoomAvailable _room;
	private Date _arrivalDate;
	private Date _departureDate;
	private int _totalRooms;
	private ArrayList<RoomAvailable> _rooms;
	private ArrayList<GuestData> _guests;
	private int _lastGuestIndex;

	private View _view;
	private ProgressDialogFragment _progress;
	private long _idToPresent;

	private boolean _isAutomaticAdultChange = true; // Para prevenir el ir actualizar el precio de la habitación al cambiar el número de adultos.

	private static final int PAYMENT_METHOD_CARD = 100;
	private static final int PAYMENT_METHOD_PAYPAL = 101;
	private int _paymentMethod;
	private boolean _isShowingWebView = false;

	public TabletHotelReserva2Fragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		Bundle args = getArguments();
		_hotel = (Hotel) args.getSerializable( "HOTEL" );
		_room = (RoomAvailable) args.getSerializable( "ROOM" );
		_arrivalDate = (Date) args.getSerializable( "ARRIVAL" );
		_departureDate = (Date) args.getSerializable( "DEPARTURE" );
		_totalRooms = args.getInt( "TOTAL_ROOMS" );

		startScreen("ViewHotelReserva2-Tablet");
		_view = inflater.inflate(R.layout.fragment_tablet_hotel_reserva2, container, false);

		_lastGuestIndex = 0;

		_rooms = new ArrayList<RoomAvailable>();
		for( int i = 0; i < _totalRooms; i++ )
		{
			_rooms.add( _room.clone() );
		}

		TextView lblBack = (TextView) _view.findViewById( R.id.header_text2 );
		lblBack.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				// TODO: Preguntar antes de regresar al primer paso de la reserva.
				// TODO: Preguntar antes de regresar de los detalles del hotel si está en medio de la reservación.
				getFragmentManager().beginTransaction().remove( TabletHotelReserva2Fragment.this ).commit();
			}
		} );

		SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
		TextView lblHotelName = (TextView) _view.findViewById( R.id.lblHotelName );
		TextView lblArrivalDate = (TextView) _view.findViewById( R.id.dates_arrival_text );
		TextView lblDepartureDate = (TextView) _view.findViewById( R.id.dates_departure_text );
		TextView lblTotal = (TextView) _view.findViewById( R.id.lblTotal );

		lblHotelName.setText( _hotel.getNombre() );
		lblArrivalDate.setText( sdf.format( _arrivalDate ) );
		lblDepartureDate.setText( sdf.format( _departureDate ) );

		lblTotal.setText( String.format( "Total: $%,.2f ", getTotalCost() ) + _room.getMoneda() );

		_guests = new ArrayList<GuestData>();
		ArrayList<String> huespedes = new ArrayList<String>();
		ArrayList<SummaryEntry> sumary = new ArrayList<SummaryEntry>();
		for( int i = 0; i < _rooms.size(); i++ )
		{
			huespedes.add( "Huesped titular - habitación " + ( i + 1 ) );
			sumary.add( new SummaryEntry( 0, "Habitación " + ( i + 1 ) ) );
			for( int j = 0; j < _rooms.get( i ).getNightsCost().size(); j++ )
			{
				sumary.add( new SummaryEntry( 1, "Noche " + ( j + 1 ) + String.format( " $%,.2f " + _rooms.get( i ).getMoneda(), _rooms.get( i ).getNightsCost().get( j ) ) ) );
			}

			_guests.add( new GuestData() );
		}

		NestedListView list = (NestedListView) _view.findViewById( R.id.list_summary );
		SummaryListAdapter adapter = new SummaryListAdapter( getActivity(), sumary );
		list.setAdapter( adapter );

		SpinnerAdapter adapterHuespedes = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, huespedes );
		Spinner spinHuespedes = (Spinner) _view.findViewById( R.id.spinHuespedes );
		spinHuespedes.setAdapter( adapterHuespedes );
		spinHuespedes.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
			{
				changeHuesped( position );
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent )
			{
				// FATAL: No debería ocurrir
			}
		} );

		RadioButton btnMisma = (RadioButton) _view.findViewById( R.id.btn_rad_misma );
		RadioButton btnOtras = (RadioButton) _view.findViewById( R.id.btn_rad_otros );
		btnMisma.setChecked( true );
		btnMisma.setEnabled( false );
		btnOtras.setEnabled( false );
		SegmentedGroup segmentedGroup = (SegmentedGroup) _view.findViewById( R.id.segmented );
		segmentedGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged( RadioGroup group, int checkedId )
			{
				handleCaptureOptionHandle();
			}
		} );

		SegmentedGroup segmentedPayment = (SegmentedGroup) _view.findViewById( R.id.segmentedPaymentMethod );
		segmentedPayment.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged( RadioGroup group, int checkedId )
			{
				handleChangePaymentMethod();
			}
		} );

		LinearLayout pnlPaymentMethod = (LinearLayout) _view.findViewById( R.id.pnlCCPayment );
		Button btnReserva2 = (Button) _view.findViewById( R.id.btnReserva );
		RadioButton btnCardMethod = (RadioButton) _view.findViewById( R.id.btn_method_card );
		RadioButton btnPaypalMethod = (RadioButton) _view.findViewById( R.id.btn_method_paypal );
		if( _hotel.isBlockCC() && !_room.getMoneda().equalsIgnoreCase( "MXN" ) && !_room.getMoneda().equalsIgnoreCase( "USD" ) )
		{
			btnPaypalMethod.setEnabled( false );
			btnCardMethod.setEnabled( false );
			btnReserva2.setVisibility( View.GONE );
			pnlPaymentMethod.setVisibility( View.GONE );
		}
		else if( _hotel.isBlockCC() )
		{
			_paymentMethod = PAYMENT_METHOD_PAYPAL;
			btnPaypalMethod.setChecked( true );
			btnCardMethod.setEnabled( false );
			btnReserva2.setText( "Ingresar a PayPal" );
			pnlPaymentMethod.setVisibility( View.GONE );

		}
		else
		{
			_paymentMethod = PAYMENT_METHOD_CARD;
			btnCardMethod.setChecked( true );
			btnReserva2.setText( "Reserva" );
			pnlPaymentMethod.setVisibility( View.VISIBLE );

			if( !_room.getMoneda().equalsIgnoreCase( "MXN" ) && !_room.getMoneda().equalsIgnoreCase( "USD" ) )
			{
				btnPaypalMethod.setEnabled( false );
			}
		}

		ArrayList<String> viajes = new ArrayList<String>();
		viajes.add( "Viaje de negocios" ); // 001
		viajes.add( "Viaje de placer" ); // 015
		SpinnerAdapter adapterViaje = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, viajes );
		Spinner spinViaje = (Spinner) _view.findViewById( R.id.spinViaje );
		spinViaje.setAdapter( adapterViaje );

		Log.d( "TEST", "MAX: " + _room.getMaxAdultos() );

		ArrayList<String> adultos = new ArrayList<String>();
		ArrayList<String> ninos = new ArrayList<String>();
		for( int i = 0; i <= _room.getMaxAdultos() - 1; i++ )
		{
			adultos.add( "" + i );
		}
		for( int i = 0; i <= _room.getMaxAdultos() - 1; i++ )
		{
			ninos.add( "" + i );
		}

		Spinner spinAdultos = (Spinner) _view.findViewById( R.id.spinAdultos );
		SpinnerAdapter adapterAdultos = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, adultos );
		spinAdultos.setAdapter( adapterAdultos );
		spinAdultos.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
			{
				changeAdultos();
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent )
			{
			}
		} );

		Spinner spinNinos = (Spinner) _view.findViewById( R.id.spinNinos );
		SpinnerAdapter adapterNinos = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, ninos );
		spinNinos.setAdapter( adapterNinos );

		ArrayList<String> months = new ArrayList<String>();
		months.add( "Mes" );
		for( int i = 1; i < 13; i++ )
		{
			if( i < 10 )
			{
				months.add( "0" + i );
			}
			else
			{
				months.add( "" + i );
			}
		}
		SpinnerAdapter adapterMonths = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, months );
		Spinner spinMonths = (Spinner) _view.findViewById( R.id.spinExpMonth );
		spinMonths.setAdapter( adapterMonths );

		// TODO: Cuántos años en el select de años de la tarjeta?
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get( Calendar.YEAR );
		ArrayList<String> years = new ArrayList<String>();
		years.add( "Año" );
		for( int i = 0; i < 15; i++ )
		{
			years.add( "" + ( year + i ) );
		}
		SpinnerAdapter adapterYears = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, years );
		Spinner spinYears = (Spinner) _view.findViewById( R.id.spinExpYear );
		spinYears.setAdapter( adapterYears );

		Button btnReserva = (Button) _view.findViewById( R.id.btnReserva );
		btnReserva.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				doReserva();
			}
		} );


		ReservanteDS db = new ReservanteDS( getActivity() );
		db.open();
		ReservanteDS.Reservante reservante = db.getReservante();
		if( reservante != null )
		{
			EditText txtName = (EditText) _view.findViewById( R.id.txtName );
			EditText txtLast = (EditText) _view.findViewById( R.id.txtLast );
			EditText txtEmail = (EditText) _view.findViewById( R.id.txtEmail );
			EditText txtPhone = (EditText) _view.findViewById( R.id.txtPhone );
			CheckBox cbAfiliate = (CheckBox) _view.findViewById( R.id.cbAfiliate );
			EditText txtSocio = (EditText) _view.findViewById( R.id.txtSocio );

			txtName.setText( reservante.getNombre() );
			txtLast.setText( reservante.getApellido() );
			txtEmail.setText( reservante.getCorreo() );
			txtSocio.setText( reservante.getSocio() );
			cbAfiliate.setChecked( reservante.isAfiliate() );
			txtPhone.setText( reservante.getTelefono() );
			spinViaje.setSelection( reservante.getViaje() );
		}
		db.close();

		WebView webView = (WebView) _view.findViewById( R.id.webView );
		webView.setWebViewClient( new PayPalWebViewClient() );

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppEventTrack( "HOTEL DETAIL ANDROID", "RESERVA 2", "HOTEL", _hotel.getNombre(), 1 );

		return _view;
	}

	private boolean isLessThan48hoursToArrival()
	{
		// TODO: Utilizar una hora que no se pueda modificar por el usuario.
		Calendar today = Calendar.getInstance();
		today.setTimeInMillis( System.currentTimeMillis() );
		today.set( Calendar.HOUR_OF_DAY, 0 );
		today.set( Calendar.MINUTE, 0 );
		today.set( Calendar.SECOND, 0 );
		today.set( Calendar.MILLISECOND, 0 );

		Calendar cal = Calendar.getInstance();
		cal.setTime( _arrivalDate );
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );
		long toArrival = cal.getTimeInMillis() - today.getTimeInMillis();

		//android.util.Log.d( "TEST", "Arrival " + cal.getTimeInMillis() );
		//android.util.Log.d( "TEST", "Today " + today.getTimeInMillis() );
		//android.util.Log.d( "TEST", "diff " + toArrival );

		if( toArrival < 48 * 60 * 60 * 1000 )
		{
			//return true;
			return false;
		}
		else
		{
			return false;
		}
	}

	private void prepareNinosSpinner( int totalAdultos )
	{
		// Actualizamos el combo de niños, porque no pueden haber más personas
		// que el máximo de adultos.
		Spinner spinNinos = (Spinner) _view.findViewById( R.id.spinNinos );
		int maxNinos = _room.getMaxAdultos() - totalAdultos;

		ArrayList<String> ninos = new ArrayList<String>();
		for( int i = 0; i <= maxNinos; i++ )
		{
			ninos.add( "" + i );
		}

		SpinnerAdapter adapterNinos = new ArrayAdapter<String>( getActivity(), R.layout.habitaciones_item, R.id.txtOption, ninos );
		spinNinos.setAdapter( adapterNinos );
	}

	private void changeAdultos()
	{
		if( _isAutomaticAdultChange )
		{
			_isAutomaticAdultChange = false;
			return;
		}

		Spinner spinAdultos = (Spinner) _view.findViewById( R.id.spinAdultos );
		Spinner spinNinos = (Spinner) _view.findViewById( R.id.spinNinos );

		Log.d( "TEST", "ADULTOS AT: " + spinAdultos.getSelectedItemPosition() );
		Log.d( "TEST", "NINOS AT: " + spinNinos.getSelectedItemPosition() );

		// Actualizamos el combo de niños, porque no pueden haber más personas
		// que el máximo de adultos.
		int totalAdultos = spinAdultos.getSelectedItemPosition() + 1; // Acompañantes + titular
		int totalNinos = spinNinos.getSelectedItemPosition();
		int maxNinos = _room.getMaxAdultos() - totalAdultos;

		prepareNinosSpinner( totalAdultos );

		if( totalNinos <= maxNinos )
		{
			spinNinos.setSelection( totalNinos );
		}
		else
		{
			spinNinos.setSelection( 0 );
		}

		long timeOne = _arrivalDate.getTime();
		long timeTwo = _departureDate.getTime();
		long oneDay = 1000 * 60 * 60 * 24;
		long delta = ( timeTwo - timeOne ) / oneDay;

		// Volvemos a pedir el costo para esta habitación
		// porque ya tenemos un número de adultos distinto.
		ReservationEngineClient.PromoRequestModel promo = new ReservationEngineClient.PromoRequestModel();
		promo.setHotel( _hotel.getSiglas() );
		promo.setNumeroDeNoches( (int) delta );
		promo.setNumeroAdultos( totalAdultos );
		promo.setNumeroHabitaciones( 1 );
		promo.setFechaInicial( _arrivalDate );
		promo.setCodigoPromocion( _room.getPromoCode() );
		promo.setCodigoTarifa( _room.getTarifaCode() );
		promo.setTipoHabitacion( _room.getCode() );
		new ServiceCaller( SERVICE_GETROOMSAVAILABLEPROMO, promo ).execute();
	}

	private void roomDataObtained( ReservationEngineClient.GetRoomsAvailablePromoResult result )
	{
		// TODO: Regresar a la página de disponibilidad cuando no se pueda comprobar el precio de la habitación con un número de adultos distinto?
		RoomAvailable room = _rooms.get( _lastGuestIndex );
		double lastCost = room.getTotal();

		Log.d( "TEST", "RESPONSE: " + result.getResponse() );
		if( result.getResponse() == 0 )
		{
			Log.d( "TEST", "TOTAL: " + result.getAvailables().size() );
			if( result.getAvailables().size() > 0 )
			{
				Log.d( "TEST", result.getAvailables().get( 0 ).getDescError() );
				if( result.getAvailables().get( 0 ).getCodigoError().length() == 0 )
				{
					ArrayList<ReservationEngineClient.Available> availables = result.getAvailables();
					for( ReservationEngineClient.Available a : availables )
					{
						if( a.getCodigoTarifa().equalsIgnoreCase( room.getTarifaCode() ) )
						{
							if( room.getTarifaCode().equalsIgnoreCase( "1168" ) || room.getTarifaCode().equalsIgnoreCase( "1268" ) || room.getTarifaCode().equalsIgnoreCase( "1368" ) )
							{
								ArrayList<ReservationEngineClient.HabPromo> habPromos = a.getPromotions();
								for( ReservationEngineClient.HabPromo habPromo : habPromos )
								{
									if( habPromo.getCodRoom().equalsIgnoreCase( room.getCode() ) ) // Solo habitación similar a la que buscamos
									{
										ArrayList<ReservationEngineClient.Promotion> promotions = habPromo.getPromotions();
										for( ReservationEngineClient.Promotion promo : promotions )
										{
											if( promo.getCodPromo().equalsIgnoreCase( room.getPromoCode() ) ) // Solo con el mismo promoCode
											{
												ArrayList<Double> costo = new ArrayList<Double>();
												double total = 0;
												try
												{
													for( int l = 0; l < promo.getNoches().size(); l++ )
													{
														double d = NumberFormat.getNumberInstance( Locale.US ).parse( promo.getNoches().get( l ).getCosto() ).doubleValue();
														total += d;
														costo.add( d );
													}
												}
												catch( Exception e )
												{
													Log.d( "UpdateCost", "Cant parse cost: " + e.getMessage() );
												}

												room.setTotal( total );
												room.setNightsCost( costo );
												updateCostList();
												if( lastCost != total )
												{
													alert( "El precio de la Habitación " + ( _lastGuestIndex + 1 ) + " cambio debido al número de acompañantes." );
												}
												return;
											}
										}
									}
								}
							}
							else
							{
								ArrayList<ReservationEngineClient.HabBase> habs = a.getTarifaBase();
								for( ReservationEngineClient.HabBase habit : habs )
								{
									if( habit.getCodBase().equalsIgnoreCase( room.getCode() ) && habit.getNoches().size() > 0 )
									{
										ArrayList<Double> costo = new ArrayList<Double>();
										double total = 0;
										try
										{
											for( int l = 0; l < habit.getNoches().size(); l++ )
											{
												double d = NumberFormat.getNumberInstance( Locale.US ).parse( habit.getNoches().get( l ).getCosto() ).doubleValue();
												total += d;
												costo.add( d );
											}
										}
										catch( Exception e )
										{
											Log.d( "UpdateCost", "Cant parse cost: " + e.getMessage() );
										}

										room.setTotal( total );
										room.setNightsCost( costo );
										updateCostList();
										if( lastCost != total )
										{
											alert( "El precio de la Habitación " + ( _lastGuestIndex + 1 ) + " cambio debido al número de acompañantes." );
										}
										return;
									}
								}
							}

							break;
						}
					}
				}
				else
				{
					alert( "No se ha podido comprobar el nuevo costo de esta habitación. Por favor intente más tarde." );
				}
			}
			else
			{
				alert( "No se ha podido comprobar el nuevo costo de esta habitación. Por favor intente más tarde." );
			}
		}
		else
		{
			alert( "No se ha podido comprobar el nuevo costo de esta habitación. Por favor intente más tarde." );
		}
	}

	private void updateCostList()
	{
		ArrayList<SummaryEntry> sumary = new ArrayList<SummaryEntry>();
		for( int i = 0; i < _rooms.size(); i++ )
		{
			sumary.add( new SummaryEntry( 0, "Habitación " + ( i + 1 ) ) );
			for( int j = 0; j < _rooms.get( i ).getNightsCost().size(); j++ )
			{
				sumary.add( new SummaryEntry( 1, "Noche " + ( j + 1 ) + String.format( " $%,.2f ", _rooms.get( i ).getNightsCost().get( j ) ) + _rooms.get( i ).getMoneda() ) );
			}
		}

		NestedListView list = (NestedListView) _view.findViewById( R.id.list_summary );
		SummaryListAdapter adapter = new SummaryListAdapter( getActivity(), sumary );
		list.setAdapter( adapter );

		TextView lblTotal = (TextView) _view.findViewById( R.id.lblTotal );

		lblTotal.setText( String.format( "Total: $%,.2f ", getTotalCost() ) + _room.getMoneda() );
	}

	private void clearCaptureFocus()
	{
		RadioButton btnMisma = (RadioButton) _view.findViewById( R.id.btn_rad_misma );
		RadioButton btnOtras = (RadioButton) _view.findViewById( R.id.btn_rad_otros );
		EditText txtName = (EditText) _view.findViewById( R.id.txtName );
		EditText txtLast = (EditText) _view.findViewById( R.id.txtLast );
		EditText txtEmail = (EditText) _view.findViewById( R.id.txtEmail );
		EditText txtSocio = (EditText) _view.findViewById( R.id.txtSocio );
		CheckBox cbAfiliate = (CheckBox) _view.findViewById( R.id.cbAfiliate );
		EditText txtPhone = (EditText) _view.findViewById( R.id.txtPhone );
		Spinner spinViaje = (Spinner) _view.findViewById( R.id.spinViaje );
		Spinner spinAdultos = (Spinner) _view.findViewById( R.id.spinAdultos );
		Spinner spinNinos = (Spinner) _view.findViewById( R.id.spinNinos );

		btnMisma.clearFocus();
		btnOtras.clearFocus();
		txtName.clearFocus();
		txtLast.clearFocus();
		txtEmail.clearFocus();
		txtSocio.clearFocus();
		cbAfiliate.clearFocus();
		txtPhone.clearFocus();
		spinViaje.clearFocus();
		spinAdultos.clearFocus();
		spinNinos.clearFocus();
	}

	private void changeHuesped( int index )
	{
		clearCaptureFocus();
		saveCurrentGuest();
		_lastGuestIndex = index;
		setGuestData();

		if( index == 0 )
		{
			setEnableSegmentedGroup( false );
		}
		else
		{
			setEnableSegmentedGroup( true );
		}
	}

	private void saveCurrentGuest()
	{
		RadioButton btnMisma = (RadioButton) _view.findViewById( R.id.btn_rad_misma );
		EditText txtName = (EditText) _view.findViewById( R.id.txtName );
		EditText txtLast = (EditText) _view.findViewById( R.id.txtLast );
		EditText txtEmail = (EditText) _view.findViewById( R.id.txtEmail );
		EditText txtSocio = (EditText) _view.findViewById( R.id.txtSocio );
		CheckBox cbAfiliate = (CheckBox) _view.findViewById( R.id.cbAfiliate );
		EditText txtPhone = (EditText) _view.findViewById( R.id.txtPhone );
		Spinner spinViaje = (Spinner) _view.findViewById( R.id.spinViaje );
		Spinner spinAdultos = (Spinner) _view.findViewById( R.id.spinAdultos );
		Spinner spinNinos = (Spinner) _view.findViewById( R.id.spinNinos );

		GuestData d = _guests.get( _lastGuestIndex );
		d.setDataOption( btnMisma.isChecked() ? 0 : 1 );
		if( _lastGuestIndex == 0 || ( _lastGuestIndex != 0 && !btnMisma.isChecked() ) )
		{
			d.setName( txtName.getText().toString() );
			d.setLastName( txtLast.getText().toString() );
			d.setEmail( txtEmail.getText().toString() );
			d.setSocio( txtSocio.getText().toString() );
			d.setAfiliate( cbAfiliate.isChecked() );
			d.setPhone( txtPhone.getText().toString() );
			d.setViaje( spinViaje.getSelectedItemPosition() );
		}
		d.setAdultos( spinAdultos.getSelectedItemPosition() );
		d.setNinos( spinNinos.getSelectedItemPosition() );
	}

	private void setGuestData()
	{
		Log.d( "TEST", "Setting Guest Data" );
		RadioButton btnMisma = (RadioButton) _view.findViewById( R.id.btn_rad_misma );
		RadioButton btnOtras = (RadioButton) _view.findViewById( R.id.btn_rad_otros );
		EditText txtName = (EditText) _view.findViewById( R.id.txtName );
		EditText txtLast = (EditText) _view.findViewById( R.id.txtLast );
		EditText txtEmail = (EditText) _view.findViewById( R.id.txtEmail );
		EditText txtSocio = (EditText) _view.findViewById( R.id.txtSocio );
		CheckBox cbAfiliate = (CheckBox) _view.findViewById( R.id.cbAfiliate );
		EditText txtPhone = (EditText) _view.findViewById( R.id.txtPhone );
		Spinner spinViaje = (Spinner) _view.findViewById( R.id.spinViaje );
		Spinner spinAdultos = (Spinner) _view.findViewById( R.id.spinAdultos );
		Spinner spinNinos = (Spinner) _view.findViewById( R.id.spinNinos );

		GuestData d;

		d = _guests.get( _lastGuestIndex );

		_isAutomaticAdultChange = true;
		prepareNinosSpinner( d.getAdultos() + 1 ); // Acompañantes + Titular
		spinAdultos.setSelection( d.getAdultos() );
		spinNinos.setSelection( d.getNinos() );

		if( _lastGuestIndex == 0 )
		{
			d = _guests.get( 0 );
			txtName.setEnabled( true );
			txtLast.setEnabled( true );
			txtEmail.setEnabled( true );
			txtSocio.setEnabled( true );
			cbAfiliate.setEnabled( true );
			txtPhone.setEnabled( true );
			spinViaje.setEnabled( true );
		}
		else
		{
			d = _guests.get( _lastGuestIndex );
			if( d.getDataOption() == 0 )
			{
				btnMisma.setChecked( true );
			}
			else
			{
				btnOtras.setChecked( true );
			}

			if( d.getDataOption() == 0 )
			{
				d = _guests.get( 0 );
				txtName.setEnabled( false );
				txtLast.setEnabled( false );
				txtEmail.setEnabled( false );
				txtSocio.setEnabled( false );
				cbAfiliate.setEnabled( false );
				txtPhone.setEnabled( false );
				spinViaje.setEnabled( false );
			}
			else
			{
				txtName.setEnabled( true );
				txtLast.setEnabled( true );
				txtEmail.setEnabled( true );
				txtSocio.setEnabled( true );
				cbAfiliate.setEnabled( true );
				txtPhone.setEnabled( true );
				spinViaje.setEnabled( true );
			}
		}

		txtName.setText( d.getName() );
		txtLast.setText( d.getLastName() );
		txtEmail.setText( d.getEmail() );
		txtSocio.setText( d.getSocio() );
		cbAfiliate.setChecked( d.isAfiliate() );
		txtPhone.setText( d.getPhone() );
		spinViaje.setSelection( d.getViaje() );
	}

	private void handleCaptureOptionHandle()
	{
		if( _lastGuestIndex != 0 )
		{
			RadioButton btnMisma = (RadioButton) _view.findViewById( R.id.btn_rad_misma );
			GuestData d = _guests.get( _lastGuestIndex );
			d.setDataOption( btnMisma.isChecked() ? 0 : 1 );
			setGuestData();
		}
	}

	private void setEnableSegmentedGroup( boolean enabled )
	{
		RadioButton btnMisma = (RadioButton) _view.findViewById( R.id.btn_rad_misma );
		RadioButton btnOtras = (RadioButton) _view.findViewById( R.id.btn_rad_otros );
		btnMisma.setEnabled( enabled );
		btnOtras.setEnabled( enabled );
	}

	private boolean validateGuest( int index )
	{
		GuestData g = _guests.get( index );
		if( index != 0 && g.getDataOption() == 0 )
		{
			return true;
		}

		String title = "Huesped titular - habitación " + ( index + 1 );

		if( g.getName().trim().length() == 0 )
		{
			alert( "Por favor ingresa el nombre del " + title + "." );
			return false;
		}
		if( g.getLastName().trim().length() == 0 )
		{
			alert( "Por favor ingresa el apellido del " + title + "." );
			return false;
		}
		if( g.getEmail().trim().length() == 0 )
		{
			alert( "Por favor ingresa el correo electrónico del " + title + "." );
			return false;
		}
		if( !isEmailValid( g.getEmail() ) )
		{
			alert( "El correo electrónico del " + title + " es incorrecto." );
			return false;
		}
		if( g.getSocio().trim().length() != 0 && ( g.getSocio().trim().length() != 10 || !isAlphaNumeric( g.getSocio().trim() ) ) )
		{
			alert( "El número de socio City Premios del " + title + " es incorrecto." );
			return false;
		}
		if( g.getPhone().trim().length() == 0 )
		{
			// TODO: Validar más adecuadamente el teléfono
			alert( "Por favor ingresa el teléfono del " + title + "." );
			return false;
		}


		return true;
	}

	private boolean validateCapture()
	{
		for( int i = 0; i < _guests.size(); i++ )
		{
			if( !validateGuest( i ) )
			{
				return false;
			}
		}

		if( _paymentMethod == PAYMENT_METHOD_CARD )
		{
			if( isLessThan48hoursToArrival() )
			{
				alert( "Su reservación con Tarjeta de Crédito no puede ser realizada con menos de 48 horas de anticipación." );
				return false;
			}

			EditText txtCardName = (EditText) _view.findViewById( R.id.txtCardName );
			if( txtCardName.getText().toString().trim().length() == 0 )
			{
				alert( "Por favor ingresa el nombre del titular de la tarjeta de crédito." );
				return false;
			}

			EditText txtCardNumber = (EditText) _view.findViewById( R.id.txtCardNumber );
			if( !CCUtils.validCC( txtCardNumber.getText().toString() ) )
			{
				alert( "El número de tarjeta es inválido." );
				return false;
			}

			Spinner spinExpMonth = (Spinner) _view.findViewById( R.id.spinExpMonth );
			if( spinExpMonth.getSelectedItemPosition() == 0 )
			{
				alert( "Selecciona el mes de expiración de la tarjeta de crédito." );
				return false;
			}

			Spinner spinExpYear = (Spinner) _view.findViewById( R.id.spinExpYear );
			if( spinExpYear.getSelectedItemPosition() == 0 )
			{
				alert( "Selecciona el año de expiración de la tarjeta de crédito." );
				return false;
			}

			EditText txtCardCode = (EditText) _view.findViewById( R.id.txtCardCode );
			if( txtCardCode.getText().toString().trim().length() != 3 || !isNumeric( txtCardCode.getText().toString().trim() ) )
			{
				alert( "El código de validación de la tarjeta es incorrecto. Ingresa los últimos tres dígitos del número que se encuentra en la parte posterior de la tarjeta." );
				return false;
			}
		}

		return true;
	}

	private void saveReservante()
	{
		GuestData o = _guests.get( 0 );
		ReservanteDS.Reservante reservante = new ReservanteDS.Reservante();
		reservante.setNombre( o.getName() );
		reservante.setApellido( o.getLastName() );
		reservante.setCorreo( o.getEmail() );
		reservante.setSocio( o.getSocio() );
		reservante.setAfiliate( o.isAfiliate() );
		reservante.setTelefono( o.getPhone() );
		reservante.setViaje( o.getViaje() );

		ReservanteDS db = new ReservanteDS( getActivity() );
		db.open();
		ReservanteDS.Reservante saved = db.getReservante();
		if( saved == null )
		{
			db.insert( reservante );
		}
		else
		{
			db.update( reservante );
		}
		db.close();
	}

	private void handleChangePaymentMethod()
	{
		RadioButton btnCardMethod = (RadioButton) _view.findViewById( R.id.btn_method_card );
		LinearLayout pnlPaymentMethod = (LinearLayout) _view.findViewById( R.id.pnlCCPayment );
		Button btnReserva = (Button) _view.findViewById( R.id.btnReserva );
		if( btnCardMethod.isChecked() )
		{
			pnlPaymentMethod.setVisibility( View.VISIBLE );
			btnReserva.setText( "Reserva" );
			_paymentMethod = PAYMENT_METHOD_CARD;
		}
		else
		{
			pnlPaymentMethod.setVisibility( View.GONE );
			btnReserva.setText( "Ingresar a PayPal" );
			_paymentMethod = PAYMENT_METHOD_PAYPAL;
		}
	}

	private void doReserva()
	{
		saveCurrentGuest();

		if( _paymentMethod == PAYMENT_METHOD_CARD )
		{
			if( validateCapture() )
			{
				saveReservante();

				PaymentData payment = getPaymentData();

				long timeOne = _arrivalDate.getTime();
				long timeTwo = _departureDate.getTime();
				long oneDay = 1000 * 60 * 60 * 24;
				long delta = ( timeTwo - timeOne ) / oneDay;

				ArrayList<ReservationEngineClient.ReservationModelv3_01> reservations = new ArrayList<ReservationEngineClient.ReservationModelv3_01>();
				for( int i = 0; i < _rooms.size(); i++ )
				{
					RoomAvailable room = _rooms.get( i );
					GuestData d = _guests.get( i );
					GuestData o = _guests.get( i );
					if( i != 0 && d.getDataOption() == 0 )
					{
						o = _guests.get( 0 );
					}

					ReservationEngineClient.ReservationModelv3_01 reserva = new ReservationEngineClient.ReservationModelv3_01();
					reserva.getEmpresa().setRfcEmisor( "EMISAPP" );

					reserva.getEstancia().setCodigoOperador( "APP_Movil" );
					reserva.getEstancia().setCodigoOrigen( "007" );
					reserva.getEstancia().setFechaEntrada( _arrivalDate );
					reserva.getEstancia().setHotel( _hotel.getSiglas() );
					reserva.getEstancia().setNumeroDeNoches( (int) delta );
					reserva.getEstancia().setTipoReservacion( "TCRED" );
					reserva.getEstancia().setFormaDePago( payment.getCardType() );
					reserva.getEstancia().setCodigoSegmento( o.getViaje() == 0 ? "001" : "015" );

					reserva.getHabitacion().setCodigoHabitacion( room.getCode() );
					reserva.getHabitacion().setCodigoPromocion( room.getPromoCode() );
					reserva.getHabitacion().setCodigoTarifa( room.getTarifaCode() );
					reserva.getHabitacion().setNumeroHabitaciones( 1 );

					reserva.getHabitacion().getHuespedTitular().setNombre( o.getName() );
					reserva.getHabitacion().getHuespedTitular().setApellidos( o.getLastName() );
					reserva.getHabitacion().getHuespedTitular().setCorreoElectronico( o.getEmail() );
					reserva.getHabitacion().getHuespedTitular().setTelefono( o.getPhone() );
					reserva.getHabitacion().getHuespedTitular().setRwdNumber( o.getSocio() );
					reserva.getHabitacion().getHuespedTitular().setTotAcompAdult( d.getAdultos() );
					reserva.getHabitacion().getHuespedTitular().setTotAcompMenor( d.getNinos() );
					reserva.getHabitacion().getHuespedTitular().setCodigoPais( "MEX" );
					// TODO: Cuál es el código de país que debe llevar el huesped titular

					// TODO: Es necesario agregar los acompañantes niños y adultos al arreglo de personas del huesped titular?

					reserva.getTarjetaCredito().setNombreTarjeta( payment.getTitularName() );
					reserva.getTarjetaCredito().setNumeroTarjeta( payment.getCardNumber() );
					reserva.getTarjetaCredito().setMesVencimiento( payment.getExpirationMonth() );
					reserva.getTarjetaCredito().setAnoVencimiento( payment.getExpirationYear() );
					reserva.getTarjetaCredito().setCodigoSeguridad( payment.getValidationCode() );

					// TODO: En dónde se manda la bandera para afiliarse a City Premios?

					reservations.add( reserva );
				}

				new ServiceCaller( SERVICE_RESERVATION, reservations ).execute();
			}
		}
		else if( _paymentMethod == PAYMENT_METHOD_PAYPAL )
		{
			if( validateCapture() )
			{
				saveReservante();

				// Iniciar PayPal Payment
				_payPalPayment = getPayPalPayment();
				new PayPalCaller( this, PayPalCaller.INITIATE_PAYMENT, _payPalPayment, getFragmentManager(), _progress ).execute();
			}
		}
	}

	PayPalCaller.PayPalPayment _payPalPayment;
	PayPalCaller.PayPalSECResponse _payPalSECResponse;
	PayPalCaller.PayPalReturnResponse _payPalReturnResponse;
	PayPalCaller.PayPalDECParameters _payPalDECParameters;
	PayPalCaller.PayPalDECResponse _payPalDECResponse;
	PayPalCaller.PayPalRTParameters _payPalRTParameters;

	private PayPalCaller.PayPalPayment getPayPalPayment()
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );

		double total = getTotalCost();

		PayPalCaller.PayPalPayment payment = new PayPalCaller.PayPalPayment();
		payment.setUser( _hotel.getMerchantUserName() );
		payment.setPassword( _hotel.getMerchantPassword() );
		payment.setSignature( _hotel.getSignature() );
		payment.setReturnURL( "http://www.zebstudiospayment-return.com" );
		payment.setCancelURL( "http://www.zebstudiospayment-cancel.com" );
		payment.setItemName( "Reservante: " + _guests.get( 0 ).getName() + " " + _guests.get( 0 ).getLastName() );
		payment.setItemSKU( sdf.format( _arrivalDate ) + " a " + sdf.format( _departureDate ) );
		payment.setItemDesc( _rooms.size() + " habitaciones en: " + _room.getDescription() );
		payment.setItemAmount( String.format( Locale.US, "%.2f", total ) );
		payment.setAmount( String.format( Locale.US, "%.2f", total ) );
		payment.setCurrencyCode( _room.getMoneda() );

		if( _hotel.getIdMarca() == 4 )
		{
			payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress-plus.png" );
			payment.setCartBorderColor( "221c35" );
		}
		else if( _hotel.getIdMarca() == 3 )
		{
			payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress-suites.png" );
			payment.setCartBorderColor( "8b2131" );
		}
		else if( _hotel.getIdMarca() == 2 )
		{
			payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress-junior.png" );
			payment.setCartBorderColor( "004d41" );
		}
		else
		{
			payment.setLogoImg( "https://www.cityexpress.com/images/logo-cityexpress.png" );
			payment.setCartBorderColor( "071689" );
		}

		return payment;
	}

	private class PayPalWebViewClient extends WebViewClient
	{
		@Override
		public void onPageStarted( WebView view, String url, Bitmap favicon )
		{
			if( _progress != null && !_progress.isVisible() )
			{
				_progress.setCancelable( false );
				_progress.show( getFragmentManager(), "DIALOG" );
			}
		}

		@Override
		public void onPageFinished( WebView view, String url )
		{
			if( _progress != null && _progress.isVisible() )
			{
				_progress.dismiss();
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading( WebView view, String url )
		{
			android.util.Log.d( "WEBVIEW", "CALLING: " + url );
			if( url.startsWith( "http://www.zebstudiospayment-return.com" ) )
			{
				if( _progress != null && _progress.isVisible() )
				{
					_progress.dismiss();
				}
				android.util.Log.d( "WEBVIEW", "ACEPTED" );
				onPayPalPaymentReturned( url.replace( "http://www.zebstudiospayment-return.com/?", "" ) );
				return true;
			}
			else if( url.startsWith( "http://www.zebstudiospayment-cancel.com" ) )
			{
				if( _progress != null && _progress.isVisible() )
				{
					_progress.dismiss();
				}
				android.util.Log.d( "WEBVIEW", "CANCELLED" );
				onPayPalPaymentCancelled();
				return true;
			}
			else
				return false;
		}
	}

	public void onPayPalPaymentInitiated( PayPalCaller.PayPalSECResponse response )
	{
		_payPalSECResponse = response;
		android.util.Log.d( "TEST", "PAYMENT INITIATED" );
		if( _payPalSECResponse.getACK().equalsIgnoreCase( "SUCCESS" ) )
		{
			android.util.Log.d( "TEST", "TOKEN: " + _payPalSECResponse.getToken() );

			_isShowingWebView = true;

			_progress = ProgressDialogFragment.newInstance();
			_progress.setCancelable( false );
			LinearLayout pnlCapture = (LinearLayout) _view.findViewById( R.id.pnlCapture );
			WebView webView = (WebView) _view.findViewById( R.id.webView );
			pnlCapture.setVisibility( View.GONE );
			webView.clearView();
			//TODO: Utilizar liga de acuerdo a si es producción o sandbox
			webView.loadUrl( APIAddress.PAYPAL_MERCHANT_WEB + "?cmd=_express-checkout&token=" + _payPalSECResponse.getToken() );
			webView.setVisibility( View.VISIBLE );
		}
		else
		{
			android.util.Log.d( "TEST", "ERROR: " + _payPalSECResponse.getLongMessage() );
			alert( "PayPal no disponible. Por favor intente nuevamente más tarde." );
		}
	}

	private void onPayPalPaymentCancelled()
	{
		LinearLayout pnlCapture = (LinearLayout) _view.findViewById( R.id.pnlCapture );
		WebView webView = (WebView) _view.findViewById( R.id.webView );
		pnlCapture.setVisibility( View.VISIBLE );
		webView.setVisibility( View.GONE );
	}

	private void onPayPalPaymentReturned( String queryString )
	{
		android.util.Log.d( "TEST", "PAYPAL RETURN: " + queryString );

		LinearLayout pnlCapture = (LinearLayout) _view.findViewById( R.id.pnlCapture );
		WebView webView = (WebView) _view.findViewById( R.id.webView );
		pnlCapture.setVisibility( View.VISIBLE );
		webView.setVisibility( View.GONE );
		_isShowingWebView = false;

		_payPalReturnResponse = new PayPalCaller.PayPalReturnResponse( queryString );
		if( _payPalReturnResponse.getToken() != null && _payPalReturnResponse.getPayerId() != null )
		{
			if( _payPalReturnResponse.getToken().equalsIgnoreCase( _payPalSECResponse.getToken() ) )
			{
				_payPalDECParameters = new PayPalCaller.PayPalDECParameters();
				_payPalDECParameters.setUser( _hotel.getMerchantUserName() );
				_payPalDECParameters.setPassword( _hotel.getMerchantPassword() );
				_payPalDECParameters.setSignature( _hotel.getSignature() );
				_payPalDECParameters.setToken( _payPalReturnResponse.getToken() );
				_payPalDECParameters.setPayerId( _payPalReturnResponse.getPayerId() );
				_payPalDECParameters.setAmount( _payPalPayment.getAmount() );
				_payPalDECParameters.setCurrency( _payPalPayment.getCurrencyCode() );

				new PayPalCaller( this, PayPalCaller.DO_EXPRESS_CHECKOUT, _payPalDECParameters, getFragmentManager(), _progress ).execute();
			}
			else
			{
				alert( "Datos de PayPal corruptos. Por favor intente nuevamente más tarde." );
			}
		}
		else
		{
			alert( "Error de comunicación con PayPal. Por favor intente nuevamente más tarde." );
		}
	}

	public void onPayPalDoExpressCheckOut( PayPalCaller.PayPalDECResponse response )
	{
		android.util.Log.d( "TEST", "EXPRESS CHECKOUT DONE" );
		_payPalDECResponse = response;
		if( _payPalDECResponse.getACK().equalsIgnoreCase( "Success" ) )
		{
			if( _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Pending" ) && isLessThan48hoursToArrival() )
			{
				android.util.Log.d( "TEST", "PENDING LESS THAN 48" );
				_payPalRTParameters = new PayPalCaller.PayPalRTParameters();
				_payPalRTParameters.setUser( _hotel.getMerchantUserName() );
				_payPalRTParameters.setPassword( _hotel.getMerchantPassword() );
				_payPalRTParameters.setSignature( _hotel.getSignature() );
				_payPalRTParameters.setPayerId( _payPalReturnResponse.getPayerId() );
				_payPalRTParameters.setCurrency( _payPalDECResponse.getCourrencyCode() );
				_payPalRTParameters.setTransactionId( _payPalDECResponse.getTransactionId() );

				new PayPalCaller( this, PayPalCaller.REFUND_TRANSACTION, _payPalRTParameters, getFragmentManager(), _progress ).execute();
			}
			else if( _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Completed" ) || _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Processed" ) || _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "Pending" ) )
			{
				android.util.Log.d( "TEST", "OK: " + _payPalDECResponse.getPaymentStatus() );
				initiatePayPalReservation();
			}
			else
			{
				android.util.Log.d( "TEST", "ELSE: " + _payPalDECResponse.getPaymentStatus() );
				alert( "No se ha podido realizar su pago con PayPal. Por favor intente nuevamente más tarde." );
			}
		}
		else
		{
			alert( "Error de comunicación con PayPal. Por favor intente nuevamente más tarde." );
			android.util.Log.d( "PAYPAL", "ERROR: " + _payPalDECResponse.getLongMessage() );
		}
	}

	public void onPayPalRefundTransaction( PayPalCaller.PayPalRTResponse response )
	{
		android.util.Log.d( "TEST", "REFUND TRANSACTION DONE" );
		if( response.getACK().equalsIgnoreCase( "Success" ) )
		{
			alert( "No se ha podido realizar su pago con PayPal. Fecha de reservación menor a 48 horas." );
		}
		else
		{
			//TODO: Dar un mensaje más descriptivo de este error
			alert( "No se ha podido realizar su pago con PayPal. Fecha de reservación menor a 48 horas." );
		}
	}

	private void initiatePayPalReservation()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		long timeOne = _arrivalDate.getTime();
		long timeTwo = _departureDate.getTime();
		long oneDay = 1000 * 60 * 60 * 24;
		long delta = ( timeTwo - timeOne ) / oneDay;

		ArrayList<ReservationEngineClient.ReservationModelv3_01> reservations = new ArrayList<ReservationEngineClient.ReservationModelv3_01>();
		for( int i = 0; i < _rooms.size(); i++ )
		{
			RoomAvailable room = _rooms.get( i );
			GuestData d = _guests.get( i );
			GuestData o = _guests.get( i );
			if( i != 0 && d.getDataOption() == 0 )
			{
				o = _guests.get( 0 );
			}

			ReservationEngineClient.ReservationModelv3_01 reserva = new ReservationEngineClient.ReservationModelv3_01();
			reserva.getEmpresa().setRfcEmisor( "EMISAPP" );

			reserva.getEstancia().setCodigoOperador( "APP_Movil" );
			reserva.getEstancia().setCodigoOrigen( "007" );
			reserva.getEstancia().setFechaEntrada( _arrivalDate );
			reserva.getEstancia().setHotel( _hotel.getSiglas() );
			reserva.getEstancia().setNumeroDeNoches( (int) delta );
			reserva.getEstancia().setTipoReservacion( "DEPO" );
			reserva.getEstancia().setFormaDePago( "PAYPAL" );
			reserva.getEstancia().setCodigoSegmento( o.getViaje() == 0 ? "001" : "015" );

			reserva.getHabitacion().setCodigoHabitacion( room.getCode() );
			reserva.getHabitacion().setCodigoPromocion( room.getPromoCode() );
			reserva.getHabitacion().setCodigoTarifa( room.getTarifaCode() );
			reserva.getHabitacion().setNumeroHabitaciones( 1 );

			reserva.getHabitacion().getHuespedTitular().setNombre( o.getName() );
			reserva.getHabitacion().getHuespedTitular().setApellidos( o.getLastName() );
			reserva.getHabitacion().getHuespedTitular().setCorreoElectronico( o.getEmail() );
			reserva.getHabitacion().getHuespedTitular().setTelefono( o.getPhone() );
			reserva.getHabitacion().getHuespedTitular().setRwdNumber( o.getSocio() );
			reserva.getHabitacion().getHuespedTitular().setTotAcompAdult( d.getAdultos() );
			reserva.getHabitacion().getHuespedTitular().setTotAcompMenor( d.getNinos() );
			reserva.getHabitacion().getHuespedTitular().setCodigoPais( "MEX" );
			// TODO: Cuál es el código de país que debe llevar el huesped titular

			reserva.getDeposito().setComprobante( _payPalDECResponse.getTransactionId() );
			reserva.getDeposito().setFecha( sdf.format( cal.getTime() ) );
			reserva.getDeposito().setMonto( Float.parseFloat( _payPalDECResponse.getAmount() ) );
			reserva.getDeposito().setTipoMoneda( _payPalDECResponse.getCourrencyCode() );
			reserva.getDeposito().setNotas( "PAGO PAYPAL. DEPOSITO POR ESTANCIA COMPLETA" );
			if( _payPalDECResponse.getPaymentStatus().equalsIgnoreCase( "PENDING" ) )
			{
				reserva.getDeposito().setNotas2( "PENDING" );
			}
			else
			{
				reserva.getDeposito().setNotas2( "OK" );
			}

			reservations.add( reserva );
		}

		new ServiceCaller( SERVICE_RESERVATION, reservations ).execute();
	}

	private void reservationComplete( ArrayList<ReservationEngineClient.ReservationResult> results )
	{
		boolean thereIsAnError = false;
		String errorMessage = "";
		for( int i = 0; i < results.size(); i++ )
		{
			Log.d( "TEST", "RESPONSE: " + results.get( i ).getResponse().getResponse() );
			Log.d( "TEST", "ERROR: " + results.get( i ).getResponse().getCodigoError() );
			Log.d( "TEST", "ERROR: " + results.get( i ).getResponse().getDescError() );
			Log.d( "TEST", "MESSAGE: " + results.get( i ).getResponse().getErrorMessage() );
			Log.d( "TEST", "RESERVA: " + results.get( i ).getResponse().getNumReservacion() );
			Log.d( "TEST", "TOTAL: " + results.get( i ).getResponse().getTotalRate() );

			if( results.get( i ).getResponse().getResponse() != 0 )
			{
				thereIsAnError = true;
				break;
			}

			if( !results.get( i ).getResponse().getCodigoError().equalsIgnoreCase( "0" ) )
			{
				thereIsAnError = true;
				errorMessage = "\nERROR: " + results.get( i ).getResponse().getDescError() + ": " + results.get( i ).getResponse().getErrorMessage();
				break;
			}
		}

		if( thereIsAnError )
		{
			alert( "No se ha podido realizar su reservación. Por favor verifique sus datos y vuelva a intentarlo más tarde." + errorMessage );
			return;
		}

		Reservation reservation = new Reservation();
		reservation.setHotelName( _hotel.getNombre() );
		reservation.setHotelAddress( _hotel.getDireccion() + "\n" + _hotel.getColonia() + "\nCP: " + _hotel.getCp() + "\n" + _hotel.getCiudad() + ". " + _hotel.getEstado() );
		reservation.setHotelNearest( _hotel.getLugaresCercanos() );
		reservation.setHotelLatitude( _hotel.getLatitude() );
		reservation.setHotelLongitude( _hotel.getLongitude() );
		reservation.setArrivalDate( _arrivalDate );
		reservation.setDepartureDate( _departureDate );
		reservation.setMoneda( _room.getMoneda() );
		reservation.setHotelEmail( _hotel.getEmail() );
		reservation.setHotelPhone( _hotel.getTelefono() );
		for( int i = 0; i < results.size(); i++ )
		{
			ReservationEngineClient.ResponseBooking response = results.get( i ).getResponse();
			ReservationEngineClient.Huesped titular = results.get( i ).getReservation().getHabitacion().getHuespedTitular();

			Reservation.Room room = new Reservation.Room();
			room.setReservationNumber( response.getNumReservacion() );
			room.setReservationTitular( titular.getNombre() + " " + titular.getApellidos() );
			room.setHabDescription( _room.getTitle() );
			room.setAdultosExtra( titular.getTotAcompAdult() );

			for( int j = 0; j < _rooms.get( i ).getNightsCost().size(); j++ )
			{
				room.getNights().add( _rooms.get( i ).getNightsCost().get( j ) );
			}

			reservation.getRooms().add( room );
		}

		ReservationDS ds = new ReservationDS( getActivity() );
		ds.open();
		long id = ds.insert( reservation );
		ds.close();

		_idToPresent = id;

		new EmailSender( results ).execute();
	}

	private void emailSent()
	{
		TabletHotelDetailsActivity hotelDetailsActivity = (TabletHotelDetailsActivity) getActivity();
		hotelDetailsActivity.presentReservation( _idToPresent );
	}

	private PaymentData getPaymentData()
	{
		EditText txtCardName = (EditText) _view.findViewById( R.id.txtCardName );
		EditText txtCardNumber = (EditText) _view.findViewById( R.id.txtCardNumber );
		Spinner spinExpMonth = (Spinner) _view.findViewById( R.id.spinExpMonth );
		Spinner spinExpYear = (Spinner) _view.findViewById( R.id.spinExpYear );
		EditText txtCardCode = (EditText) _view.findViewById( R.id.txtCardCode );

		PaymentData data = new PaymentData();

		data.setTitularName( txtCardName.getText().toString().trim() );
		data.setCardNumber( txtCardNumber.getText().toString().trim() );
		data.setExpirationMonth( spinExpMonth.getSelectedItem().toString() );
		data.setExpirationYear( spinExpYear.getSelectedItem().toString() );
		data.setCardType( CCUtils.getCardName( data.getCardNumber() ) );
		try
		{
			data.setValidationCode( Integer.parseInt( txtCardCode.getText().toString().trim() ) );
		}
		catch( Exception e )
		{
			Log.e( "PAYMENT", "Cant parse validation code. ERROR: " + e.getMessage() );
		}

		return data;
	}

	private static final int SERVICE_GETROOMSAVAILABLEPROMO = 100;
	private static final int SERVICE_RESERVATION = 101;

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

			if( _task == SERVICE_GETROOMSAVAILABLEPROMO )
			{
				_progress = ProgressDialogFragment.newInstance( "Actualizando costo..." );
			}
			else if( _task == SERVICE_RESERVATION )
			{
				_progress = ProgressDialogFragment.newInstance( "Enviando reservación..." );
			}
			else
			{
				_progress = ProgressDialogFragment.newInstance();
			}
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
			else if( _task == SERVICE_RESERVATION )
			{
				ArrayList<ReservationEngineClient.ReservationModelv3_01> reservations = (ArrayList<ReservationEngineClient.ReservationModelv3_01>) _param;
				ArrayList<ReservationEngineClient.ReservationResult> results = new ArrayList<ReservationEngineClient.ReservationResult>();
				for( int i = 0; i < reservations.size(); i++ )
				{
					ReservationEngineClient.ResponseBooking r = client.InsertBookingv3_01( reservations.get( i ) );
					results.add( new ReservationEngineClient.ReservationResult( reservations.get( i ), r ) );
				}
				_response = results;
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
				roomDataObtained( result );
			}
			else if( _task == SERVICE_RESERVATION )
			{
				ArrayList<ReservationEngineClient.ReservationResult> results = (ArrayList<ReservationEngineClient.ReservationResult>) _response;
				reservationComplete( results );
			}
		}
	}

	private class EmailSender extends AsyncTask<Void, Void, Void>
	{
		private ArrayList<ReservationEngineClient.ReservationResult> _results;

		public EmailSender( ArrayList<ReservationEngineClient.ReservationResult> results )
		{
			_results = results;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			_progress = ProgressDialogFragment.newInstance( "Enviando respuesta..." );
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		@Override
		protected Void doInBackground( Void... arg0 )
		{
			///////////////////////
			String para = "";
			ArrayList<String> ccs = new ArrayList<String>();
			ArrayList<String> afiliates = new ArrayList<String>();
			String codigos = "";
			String reservantes = "";
			double total = 0;
			for( int i = 0; i < _results.size(); i++ )
			{
				ReservationEngineClient.Huesped huesped = _results.get( i ).getReservation().getHabitacion().getHuespedTitular();
				String correo = huesped.getCorreoElectronico();
				String reservante = "[" + huesped.getNombre() + " " + huesped.getApellidos() + "^" + _room.getTitle() + "^" + huesped.getTotAcompAdult() + "^" + _results.get( i ).getReservation().getEstancia().getNumeroDeNoches() + "^" + String.format( Locale.US, "%.2f", _results.get( i ).getResponse().getTotalRate() ) + "]";
				reservante = reservante.replaceAll( ",", " " );

				total += _results.get( i ).getResponse().getTotalRate();
				if( i == 0 )
				{
					para = correo;
					codigos += _results.get( i ).getResponse().getNumReservacion();
					reservantes += reservante;
				}
				else
				{
					if( !para.equalsIgnoreCase( correo ) && !isInList( correo, ccs ) )
					{
						ccs.add( correo );
					}
					codigos += "," + _results.get( i ).getResponse().getNumReservacion();
					reservantes += "," + reservante;
				}
			}

			for( int i = 0; i < _guests.size(); i++ )
			{
				GuestData g = _guests.get( i );
				if( i == 0 )
				{
					if( g.isAfiliate() )
					{
						afiliates.add( g.getEmail() );
					}
				}
				else if( g.isAfiliate() && g.getDataOption() != 0 )
				{
					if( !isInList( g.getEmail(), afiliates ) )
						afiliates.add( g.getEmail() );
				}
			}

			String cctext = "";
			for( int i = 0; i < ccs.size(); i++ )
			{
				if( i == 0 )
				{
					cctext = ccs.get( i );
				}
				else
				{
					cctext += "," + ccs.get( i );
				}
			}

			String afiliatestext = "";
			for( int i = 0; i < afiliates.size(); i++ )
			{
				if( i == 0 )
				{
					afiliatestext = afiliates.get( i );
				}
				else
				{
					afiliatestext += "," + afiliates.get( i );
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add( new BasicNameValuePair( "Para", para ) );
			params.add( new BasicNameValuePair( "CC", cctext ) );
			params.add( new BasicNameValuePair( "CodigosReservacion", codigos ) );
			params.add( new BasicNameValuePair( "CodigoHotel", _hotel.getSiglas() ) );
			params.add( new BasicNameValuePair( "FInicioFfinal", sdf.format( _arrivalDate ) + "," + sdf.format( _departureDate ) ) );
			params.add( new BasicNameValuePair( "NumHabitaciones", _results.size() + "" ) );
			params.add( new BasicNameValuePair( "idioma", "es" ) );
			params.add( new BasicNameValuePair( "DatosReservantes", reservantes ) );
			params.add( new BasicNameValuePair( "CostoTotal", String.format( Locale.US, "%.2f", total ) ) );
			params.add( new BasicNameValuePair( "cuentasAfiliacion", afiliatestext ) );

			ServiceHandler handler = new ServiceHandler();
			String response = handler.makeServiceCall( APIAddress.HOTELS_API_MOBILE + "/EnvioCorreos", ServiceHandler.GET, params );
			Log.d( "TEST", "EMAIL: " + response );

			return null;
		}

		@Override
		protected void onPostExecute( Void arg0 )
		{
			super.onPostExecute( arg0 );
			_progress.dismiss();

			emailSent();
		}

		private boolean isInList( String text, ArrayList<String> list )
		{
			for( int i = 0; i < list.size(); i++ )
			{
				if( list.get( i ).equalsIgnoreCase( text ) )
				{
					return true;
				}
			}

			return false;
		}
	}

	private boolean isAlphaNumeric( String s )
	{
		String pattern = "^[a-zA-Z0-9]*$";
		return s.matches(pattern);
	}

	private boolean isNumeric( String s )
	{
		String pattern = "^[0-9]*$";
		return s.matches(pattern);
	}

	private boolean isEmailValid( CharSequence email )
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher( email ).matches();
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

	private double getTotalCost()
	{
		double total = 0;
		for( int i = 0; i < _rooms.size(); i++ )
		{
			total += _rooms.get( i ).getTotal();
		}

		return total;
	}

	private class GuestData
	{
		private int _dataOption;
		private String _name;
		private String _lastName;
		private String _email;
		private String _socio;
		private boolean _afiliate;
		private String _phone;
		private int _viaje;
		private int _adultos;
		private int _ninos;

		public GuestData()
		{
		}

		public int getDataOption()
		{
			return _dataOption;
		}

		public void setDataOption( int dataOption )
		{
			_dataOption = dataOption;
		}

		public String getName()
		{
			return _name;
		}

		public void setName( String name )
		{
			_name = name;
		}

		public String getLastName()
		{
			return _lastName;
		}

		public void setLastName( String lastName )
		{
			_lastName = lastName;
		}

		public String getEmail()
		{
			return _email;
		}

		public void setEmail( String email )
		{
			_email = email;
		}

		public String getSocio()
		{
			return _socio;
		}

		public void setSocio( String socio )
		{
			_socio = socio;
		}

		public boolean isAfiliate()
		{
			return _afiliate;
		}

		public void setAfiliate( boolean afiliate )
		{
			_afiliate = afiliate;
		}

		public String getPhone()
		{
			return _phone;
		}

		public void setPhone( String phone )
		{
			_phone = phone;
		}

		public int getViaje()
		{
			return _viaje;
		}

		public void setViaje( int viaje )
		{
			_viaje = viaje;
		}

		public int getAdultos()
		{
			return _adultos;
		}

		public void setAdultos( int adultos )
		{
			_adultos = adultos;
		}

		public int getNinos()
		{
			return _ninos;
		}

		public void setNinos( int ninos )
		{
			_ninos = ninos;
		}
	}

	private static class PaymentData
	{
		private String _titularName;
		private String _cardType;
		private String _cardNumber;
		private String _expirationMonth;
		private String _expirationYear;
		private int _validationCode;

		public PaymentData()
		{
		}

		public String getTitularName()
		{
			return _titularName;
		}

		public void setTitularName( String titularName )
		{
			_titularName = titularName;
		}

		public String getCardType()
		{
			return _cardType;
		}

		public void setCardType( String cardType )
		{
			_cardType = cardType;
		}

		public String getCardNumber()
		{
			return _cardNumber;
		}

		public void setCardNumber( String cardNumber )
		{
			_cardNumber = cardNumber;
		}

		public String getExpirationMonth()
		{
			return _expirationMonth;
		}

		public void setExpirationMonth( String expirationMonth )
		{
			_expirationMonth = expirationMonth;
		}

		public String getExpirationYear()
		{
			return _expirationYear;
		}

		public void setExpirationYear( String expirationYear )
		{
			_expirationYear = expirationYear;
		}

		public int getValidationCode()
		{
			return _validationCode;
		}

		public void setValidationCode( int validationCode )
		{
			_validationCode = validationCode;
		}
	}
}
