package com.zebstudios.cityexpress;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import static com.appsee.Appsee.startScreen;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class MainTabletFragment extends Fragment
{
	private View _view;
	private ArrayList<ImageOption> _options;
	private int _currentSelectedOption;
	private State _state;
	private PromoCode _promocode;
	private ProgressDialogFragment _progress;
	private ArrayList<Hotel> _results;
	private Hotel _tempHotel;

	private TabletHotelsResultListAdapter _hotelsResultListAdapter;

	private static final int FRAGMENT_LIST_STATES = 100;
	private static final int RESULTS_ACTIVITY = 1000;
	private static final int DETAILS_ACTIVITY = 1001;

	public MainTabletFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		startScreen("ViewBuscarHotel-Tablet");
		_view = inflater.inflate( R.layout.fragment_main_tablet, container, false );

		_currentSelectedOption = -1;
		_options = new ArrayList<ImageOption>();
		ImageButton btnOption1 = (ImageButton) _view.findViewById( R.id.btnOption1 );
		ImageButton btnOption2 = (ImageButton) _view.findViewById( R.id.btnOption2 );
		ImageButton btnOption3 = (ImageButton) _view.findViewById( R.id.btnOption3 );
		ImageButton btnOption4 = (ImageButton) _view.findViewById( R.id.btnOption4 );
		_options.add( new ImageOption( btnOption1, R.drawable.bar_marca_1_on, R.drawable.bar_marca_1_off, "express" ) );
		_options.add( new ImageOption( btnOption2, R.drawable.bar_marca_2_on, R.drawable.bar_marca_2_off, "plus" ) );
		_options.add( new ImageOption( btnOption3, R.drawable.bar_marca_3_on, R.drawable.bar_marca_3_off, "suites" ) );
		_options.add( new ImageOption( btnOption4, R.drawable.bar_marca_4_on, R.drawable.bar_marca_4_off, "junior" ) );
		prepareOptions();

		Button btnCity = (Button) _view.findViewById( R.id.btnCity );
		btnCity.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view )
			{
				getStates();
			}
		} );

		Button btnSeach = (Button) _view.findViewById( R.id.btnSearch );
		btnSeach.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view )
			{
				Search();
			}
		} );

		return _view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity)getActivity();
		activity.getSupportActionBar().setTitle( "Principal" );
	}

	private void Search()
	{
		EditText txtWord = (EditText)_view.findViewById( R.id.txtWord );
		String text = txtWord.getText().toString().trim();

		if( _currentSelectedOption == -1 && _state == null && text.length() == 0 )
		{
			AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
			alert.setTitle( "Atención" );
			alert.setMessage( "Por favor ingrese una palabra y/o seleccione un estado y/o una marca." );
			alert.setIcon( R.drawable.notification_warning_small );
			alert.setCancelable( false );
			alert.setButton( "OK", new DialogInterface.OnClickListener()
			{
				public void onClick( DialogInterface dialog, int which )
				{
					// Nada
				}
			} );
			alert.show();
		}
		else
		{
			new DoSearch().execute();
			Analytics analytics = (Analytics)getActivity().getApplication();
			if( _state != null )
			{
				analytics.sendAppEventTrack( "MAIN ANDROID", "SEARCH", "ESTADO", _state.getNombre(), 1 );
			}
			if( _currentSelectedOption != -1 )
			{
				analytics.sendAppEventTrack( "MAIN ANDROID", "SEARCH", "MARCA", _options.get( _currentSelectedOption ).getOption(), 1 );
			}
		}
	}

	private void Searched()
	{
		android.util.Log.d( "TEST", "RESULTS: " + _results.size() );
		if( _results.size() == 0 )
		{
			AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
			alert.setTitle( "Atención" );

			EditText txtWord = (EditText)_view.findViewById( R.id.txtWord );
			String text = txtWord.getText().toString().trim();

			if( text.length() > 0 && _currentSelectedOption != -1 )
				alert.setMessage( "No se encontraron hoteles de esa marca con la palabra que ingresó." );
			else if( text.length() > 0 )
				alert.setMessage( "No se encontraron hoteles con la palabra que ingresó." );
			else if( _state != null && _currentSelectedOption != -1 )
				alert.setMessage( "No se encontraron hoteles de esa marca en el estado que seleccionó." );
			else if( _state != null )
				alert.setMessage( "No se encontraron hoteles en el estado que seleccionó." );
			else
				alert.setMessage( "No se encontraron hoteles de esa marca." );
			alert.setIcon( R.drawable.notification_warning_small );
			alert.setCancelable( false );
			alert.setButton( "OK", new DialogInterface.OnClickListener()
			{
				public void onClick( DialogInterface dialog, int which )
				{
				}
			} );
			alert.show();
		}
		else if( _results.size() > 1 )
		{
			Collections.sort( _results, new Hotel.HotelComparator() );

			_hotelsResultListAdapter = new TabletHotelsResultListAdapter( getActivity(), _results );
			ListView list = (ListView) _view.findViewById( R.id.result_list );
			list.setAdapter( _hotelsResultListAdapter );
			list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
				{
					ItemSelected( i );
				}
			} );
		}
		else
		{
			_tempHotel = _results.get( 0 );
			new GetHotelData( _tempHotel.getId() ).execute();
		}
	}

	private void hotelObtained( int res, Hotel hotel )
	{
		Intent dialog = new Intent( getActivity(), TabletHotelDetailsActivity.class );
		if( res == 0 )
			dialog.putExtra( "HOTEL", hotel );
		else
			dialog.putExtra( "HOTEL", _tempHotel );
		startActivityForResult( dialog, DETAILS_ACTIVITY );
	}

	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		if( requestCode == DETAILS_ACTIVITY && resultCode == Activity.RESULT_OK)
		{
			long reservationId = data.getLongExtra( "RESERVATION_ID", 0 );
			MainActivity mainActivity = (MainActivity)getActivity();
			mainActivity.presentReservation( reservationId );
		}
	}

	public void ItemSelected( int position )
	{
		_tempHotel = _results.get( position );
		new GetHotelData( _tempHotel.getId() ).execute();
	}

	private void getStates()
	{
		StatesFragment fragment = new StatesFragment();
		fragment.setTargetFragment( this, FRAGMENT_LIST_STATES );
		fragment.show(getFragmentManager(), "dialog");
	}

	public  void setSelectedPromocode(PromoCode promocode){
		EditText lblpromocode = (EditText)_view.findViewById(R.id.txtPromocode);
		_promocode = promocode;

		lblpromocode.setText(promocode.getnumpromocode());

	}

	public void setSelectedState( State state )
	{
		Button btnState = (Button) _view.findViewById( R.id.btnCity );
		_state = state;
		if( _state.getId() == -1 )
		{
			_state = null;
			btnState.setText( "Buscar por estado" );
		}
		else
		{
			btnState.setText( _state.getNombre() );
		}
	}

	private void prepareOptions()
	{
		for( int i = 0; i < _options.size(); i++ )
		{
			ImageOption op = _options.get( i );
			final int index = i;
			op.getImageButton().setOnClickListener( new View.OnClickListener()
			{
				@Override
				public void onClick( View view )
				{
					handleOption( index );
				}
			} );
		}
	}

	private void handleOption( int option )
	{
		if( _currentSelectedOption == option )
		{
			_options.get( option ).SetOff();
			_currentSelectedOption = -1;
		}
		else
		{
			if( _currentSelectedOption != -1 )
				_options.get( _currentSelectedOption ).SetOff();
			_options.get( option ).SetOn();
			_currentSelectedOption = option;
		}
	}

	private class DoSearch extends AsyncTask<Void, Void, Integer>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			_progress = ProgressDialogFragment.newInstance();
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			_results = new ArrayList<Hotel>();

			EditText txtWord = (EditText)_view.findViewById( R.id.txtWord );
			String text = txtWord.getText().toString().trim();

			String url = "";
			if( text.length() > 0 && _currentSelectedOption != -1 )
				url = APIAddress.HOTELS_API_MOBILE + "/SearchHotels?term=" + urlEncode( text ) + "&marca=" + _options.get( _currentSelectedOption ).getOption();
			else if( text.trim().length() > 0 )
				url = APIAddress.HOTELS_API_MOBILE + "/SearchHotels?term=" + urlEncode( text ) + "&marca=";
			else if( _state != null && _currentSelectedOption != -1 )
				url = APIAddress.HOTELS_API_MOBILE + "/GetHotelsByStateAndBrand?stateId=" + _state.getId() + "&brandName=" + _options.get( _currentSelectedOption ).getOption();
			else if( _state != null )
				url = APIAddress.HOTELS_API_MOBILE + "/GetHotelsByState/" + _state.getId();
			else if( _currentSelectedOption != -1 )
				url = APIAddress.HOTELS_API_MOBILE + "/GetHotelsByBrand?brandName=" + _options.get( _currentSelectedOption ).getOption();

			android.util.Log.d( "", "URL: " + url );
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall( url, ServiceHandler.GET );

			if( jsonStr != null )
			{
				try
				{
					JSONArray results = new JSONArray( jsonStr );
					for( int i = 0; i < results.length(); i++ )
					{
						JSONObject h = results.getJSONObject( i );
						Hotel hotel = new Hotel( h );
						if( !hotel.isParsedOk() )
						{
							// Error
							return -3;
						}
						_results.add( hotel );
					}

					return 0;
				}
				catch( Exception e )
				{
					android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
					return -2;
				}
			}
			else
				android.util.Log.e( "ServiceHandler", "Couldn't get any data" );

			return -1;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );
			// Dismiss the progress dialog
			_progress.dismiss();

			if( result != 0 )
			{
				AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
				alert.setTitle( "Atención" );
				alert.setMessage( "No se ha podido realizar su búsqueda en este momento. Por favor intente nuevamente más tarde." );
				alert.setIcon( R.drawable.notification_warning_small );
				alert.setCancelable( false );
				alert.setButton( DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick( DialogInterface dialog, int which )
					{
					}
				} );
				alert.show();
			}
			else
			{
				Searched();
			}
		}

		private String urlEncode( String text )
		{
			String encoded = "";

			try
			{
				encoded = URLEncoder.encode( text, "UTF-8" );
			}
			catch( Exception e )
			{
				android.util.Log.w( "ENCODER", "Cant encode text: " + e.getMessage() );
			}

			return encoded;
		}
	}

	private class GetHotelData extends AsyncTask<Void, Void, Integer>
	{
		private int _id;
		private Hotel _hotel;

		public GetHotelData( int id )
		{
			_id = id;
			_hotel = null;
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
		protected Integer doInBackground( Void... arg0 )
		{
			String url = APIAddress.HOTELS_API_MOBILE + "/GetHotel/" + _id;

			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall( url, ServiceHandler.GET );

			if( jsonStr != null )
			{
				try
				{
					JSONObject raw = new JSONObject( jsonStr );
					JSONObject h = raw.getJSONObject( "Hotele" );
					Hotel hotel = new Hotel( h );
					if( !hotel.isParsedOk() )
					{
						// Error
						return -3;
					}

					_hotel = hotel;
					return 0;
				}
				catch( Exception e )
				{
					android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
					return -2;
				}
			}
			else
				android.util.Log.e( "ServiceHandler", "Couldn't get any data" );

			return -1;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );
			_progress.dismiss();

			hotelObtained( result, _hotel );
		}
	}

	private class ImageOption
	{
		private ImageButton _imageButton;
		private int _onResource;
		private int _offResource;
		private String _option;

		public ImageOption( ImageButton imageButton, int onResource, int offResource, String option )
		{
			_imageButton = imageButton;
			_onResource = onResource;
			_offResource = offResource;
			_option = option;
		}

		public void SetOn()
		{
			_imageButton.setBackgroundResource( R.drawable.buttonmain_background );
			_imageButton.setImageResource( _onResource );
		}

		public void SetOff()
		{
			_imageButton.setBackgroundResource( R.drawable.buttonmain_background );
			_imageButton.setImageResource( _offResource );
		}

		public ImageButton getImageButton()
		{
			return _imageButton;
		}

		public int getOnResource()
		{
			return _onResource;
		}

		public int getOffResource()
		{
			return _offResource;
		}

		public String getOption()
		{
			return _option;
		}
	}
}
