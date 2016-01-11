package com.zebstudios.cityexpress;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Denumeris Interactive on 23/10/2014.
 */
public class SearchResultsActivity extends ActionBarActivity
{
	private String _marca;
	private ArrayList<Hotel> _results;
	private HotelsResultListAdapter _hotelsResultListAdapter;
	private ProgressDialogFragment _progress;
	private Hotel _tempHotel;

	private static final int DETAILS_ACTIVITY = 1001;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		Intent intent = getIntent();
		_marca = intent.getStringExtra( "MARCA" );
		String state = intent.getStringExtra( "STATE" );
		String word = intent.getStringExtra( "WORD" );
		_results = (ArrayList<Hotel>) intent.getSerializableExtra( "RESULTS" );

		String addTitle = "";
		if( _marca.equalsIgnoreCase( "express" ) )
		{
			addTitle = "Hoteles";
			setTheme( R.style.MainTheme );
		}
		else if( _marca.equalsIgnoreCase( "plus" ) )
		{
			addTitle = "Plus";
			setTheme( R.style.PlusTheme );
		}
		else if( _marca.equalsIgnoreCase( "suites" ) )
		{
			addTitle = "Suites";
			setTheme( R.style.SuitesTheme );
		}
		else if( _marca.equalsIgnoreCase( "junior" ) )
		{
			addTitle = "Junior";
			setTheme( R.style.JuniorTheme );
		}
		else
		{
			addTitle = "";
			setTheme( R.style.MainTheme );
		}

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_searchresults );

		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setIcon( android.R.color.transparent );

		if( word.length() > 0 && addTitle.length() > 0 )
			getSupportActionBar().setTitle( addTitle );
		else if( word.length() > 0 )
			getSupportActionBar().setTitle( "Resultados" );
		else if( state.length() > 0 && addTitle.length() > 0 )
			getSupportActionBar().setTitle( state + " - " + addTitle );
		else if( state.length() > 0 )
			getSupportActionBar().setTitle( state );
		else
			getSupportActionBar().setTitle( addTitle );

		_hotelsResultListAdapter = new HotelsResultListAdapter( this, _results );
		ListView list = (ListView)findViewById( R.id.result_list );
		list.setAdapter( _hotelsResultListAdapter );
		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )
			{
				ItemSelected( i );
			}
		} );
	}

	public void ItemSelected( int position )
	{
		_tempHotel = _results.get( position );
		new GetHotelData( _tempHotel.getId() ).execute();
		Log.e("SearchResult", "id de hotel :3 --> " + _tempHotel.getId() );
	}

	private void hotelObtained( int res, Hotel hotel )
	{
		Intent dialog = new Intent( this, DetalleHotelCercaActivity.class );
		if( res == 0 )
			dialog.putExtra( "HOTEL", hotel );
		else
			dialog.putExtra( "HOTEL", _tempHotel );
		startActivityForResult( dialog, DETAILS_ACTIVITY );
	}

	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		if( requestCode == DETAILS_ACTIVITY )
		{
			android.util.Log.d( "TEST", "RETURN FROM DETAILS: " + resultCode );
			if( resultCode == RESULT_OK )
			{
				long reservationId = data.getLongExtra( "RESERVATION_ID", 0 );

				Intent intent = getIntent();
				intent.putExtra( "RESERVATION_ID", reservationId );
				setResult( RESULT_OK, intent );
				finish();
			}
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
			_progress.show( getSupportFragmentManager(), "Dialog" );
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			String url = APIAddress.HOTELS_API_MOBILE + "/GetHotel/" + _id;

			android.util.Log.d( "", "URL: " + url );
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall( url, ServiceHandler.GET );

			if( jsonStr != null )
			{
				try
				{
					Log.e("search", "GETHOTELID -- " + jsonStr );
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

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		super.onOptionsItemSelected( item );
		switch( item.getItemId() )
		{
			case android.R.id.home:
				SearchResultsActivity.this.onBackPressed();
				break;
		}
		return true;
	}
}
