package com.zebstudios.cityexpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.appsee.Appsee.startScreen;

public class CercaFragment extends Fragment
{
	private View _view;
	private LocationManager _locationManager;
	private LocationListener _locationListener;
	private WorkingDialogFragment _progress;
	private boolean _isLocationObtained;

	private static final int RESULTS_ACTIVITY = 1000;
	private static final int DETAILS_ACTIVITY = 1001;

	public CercaFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		startScreen("ViewCercaDeTi");
		_view = inflater.inflate( R.layout.fragment_cerca, container, false );

		_isLocationObtained = false;

		return _view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity)getActivity();
		activity.getSupportActionBar().setTitle( "Cerca de tí" );
		activity.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.blue_button));

		if( !_isLocationObtained )
		{
			_locationManager = (LocationManager)getActivity().getSystemService( Context.LOCATION_SERVICE );
			_locationListener = new LocalLocationListener();
			_locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, _locationListener );

			_progress = new WorkingDialogFragment();
			_progress.setLabel( "Obteniendo tu ubicación" );
			_progress.setIcon( R.drawable.pin_big );
			_progress.setCancelable( false );
			_progress.show( getFragmentManager(), "Dialog" );
		}
	}

	private void locationObtained( Location location )
	{
		if( _isLocationObtained )
		{
			return;
		}
		_isLocationObtained = true;
		_locationManager.removeUpdates( _locationListener );
		_progress.ChageLabel( "Obteniendo hotel más cercano" );
		new SearchNearest( location.getLatitude(), location.getLongitude() ).execute();
	}

	private void nearestObtained( int res, ArrayList<Integer> hotels )
	{
		if( res == 0 )
		{
			_progress.ChageLabel( "Obteniendo datos del hotel" );
			new GetHotelData( hotels ).execute();
		}
		else
		{
			alert( "No se ha podido realizar la búsqueda. Por favor intenta nuevamente más tarde." );
			_progress.dismiss();
		}
	}

	private void hotelsObtained( int res, ArrayList<Hotel> hotels )
	{
		if( res == 0 )
		{
			for( int i=0; i<hotels.size(); i++ )
			{
				android.util.Log.d( "TEST", "HOTEL: " + hotels.get( i ).getNombre() );
			}
			_progress.dismiss();

			if( CompatibilityUtil.isTablet( getActivity() ) )
			{
				Bundle bundle = new Bundle();
				bundle.putSerializable( "RESULTS", hotels );

				SearchResultsDialogFragment dialog = new SearchResultsDialogFragment();
				dialog.setTargetFragment( this, 0 );
				dialog.setArguments( bundle );
				dialog.setCancelable( false );
				dialog.show( getFragmentManager(), "dialog" );
			}
			else
			{
				Intent dialog = new Intent( getActivity(), SearchResultsActivity.class );
				dialog.putExtra( "STATE", "Cerca de ti" );
				dialog.putExtra( "MARCA", "" );
				dialog.putExtra( "WORD", "" );
				dialog.putExtra( "RESULTS", hotels );
				startActivityForResult( dialog, RESULTS_ACTIVITY );
			}
		}
		else
		{
			alert( "No se ha podido realizar la búsqueda. Por favor intenta nuevamente más tarde." );
			_progress.dismiss();
		}
	}

	public void hotelSelected( Hotel hotel )
	{
		Intent dialog = new Intent( getActivity(), TabletHotelDetailsActivity.class );
		dialog.putExtra( "HOTEL", hotel );
		startActivityForResult( dialog, DETAILS_ACTIVITY );
	}

	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		if( ( requestCode == RESULTS_ACTIVITY || requestCode == DETAILS_ACTIVITY ) && resultCode == Activity.RESULT_OK)
		{
			long reservationId = data.getLongExtra( "RESERVATION_ID", 0 );
			MainActivity mainActivity = (MainActivity)getActivity();
			mainActivity.presentReservation( reservationId );
		}
	}

	private class LocalLocationListener implements LocationListener
	{
		public void onLocationChanged( Location location )
		{
			if( !_isLocationObtained )
			{
				locationObtained( location );
			}
		}

		public void onProviderDisabled( String provider )
		{
		}

		public void onProviderEnabled( String provider )
		{
		}

		public void onStatusChanged( String provider, int status, Bundle extras )
		{
		}
	}

	private class SearchNearest extends AsyncTask<Void, Void, Integer>
	{
		private double _latitude;
		private double _longitude;
		private ArrayList<Integer> _hotels;

		public SearchNearest( double lat, double lon )
		{
			_latitude = lat;
			_longitude = lon;
			_hotels = new ArrayList<Integer>();
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			String url = APIAddress.HOTELS_API_MOBILE + "/GetHotelsNearest/?Latitud="+ _latitude +"&Longitud=" + _longitude;

			android.util.Log.d( "", "URL: " + url );
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall( url, ServiceHandler.GET );

			if( jsonStr != null )
			{
				try
				{
					JSONArray nearest = new JSONArray( jsonStr );
					for( int i=0; i<nearest.length(); i++ )
					{
						_hotels.add( nearest.getInt( i ) );
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

			nearestObtained( result, _hotels );
		}
	}

	private class GetHotelData extends AsyncTask<Void, Void, Integer>
	{
		private ArrayList<Integer> _ids;
		private ArrayList<Hotel> _hotels;

		public GetHotelData( ArrayList<Integer> ids )
		{
			_ids = ids;
			_hotels = new ArrayList<Hotel>();
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			int errorNumber = 0;
			for( int i=0; i<_ids.size(); i++ )
			{
				String url = APIAddress.HOTELS_API_MOBILE + "/GetHotel/" + _ids.get( i );

				android.util.Log.d( "", "URL: " + url );
				ServiceHandler sh = new ServiceHandler();
				String jsonStr = sh.makeServiceCall( url, ServiceHandler.GET );

				if( jsonStr != null )
				{
					try
					{
						JSONObject raw = new JSONObject( jsonStr );
						JSONObject h = raw.getJSONObject( "Hotele" );
						Hotel hotel = new Hotel( h, 1 );
						if( !hotel.isParsedOk() )
						{
							// Error
							errorNumber = -3;
						}

						_hotels.add( hotel );
					}
					catch( Exception e )
					{
						android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
						errorNumber = -2;
						break;
					}
				}
				else
				{
					android.util.Log.e( "ServiceHandler", "Couldn't get any data" );
					errorNumber = -1;
					break;
				}
			}

			return errorNumber;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );

			hotelsObtained( result, _hotels );
		}
	}

	private void alert( String message )
	{
		AlertDialog alert = new AlertDialog.Builder( getActivity() ).create();
		alert.setTitle( "Atención" );
		alert.setMessage( message );
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
}
