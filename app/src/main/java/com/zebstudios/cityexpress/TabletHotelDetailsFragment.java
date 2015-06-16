package com.zebstudios.cityexpress;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class TabletHotelDetailsFragment extends Fragment
{
	private View _view;
	private Hotel _hotel;
	private WeatherReport _weatherReport;
	private List<RSSArticle> _articlesRSS;
	private List<RSSArticle> _articlesBlog;
	private String _rssAddress;
	private GoogleMap _map;
	private MapView _mapView;

	public TabletHotelDetailsFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		_view = inflater.inflate( R.layout.fragment_tablet_hotel_detail, container, false );
		_hotel = (Hotel) getArguments().getSerializable( "HOTEL" );

		if( _hotel.getDescripcionMaps() != null && !_hotel.getDescripcionMaps().equalsIgnoreCase( "null" ) )
		{
			TextView lblDescription = (TextView) _view.findViewById( R.id.lblDescription );
			lblDescription.setText( _hotel.getDescripcionMaps() );
		}

		TextView lblAddress = (TextView) _view.findViewById( R.id.lblAddress );
		String address = getAddressString();
		lblAddress.setText( address );

		ServicesListAdapter serviciosListAdapter = new ServicesListAdapter( getActivity(), _hotel.getServicios() );
		NestedGridView gridview = ( NestedGridView )_view.findViewById( R.id.servicesGridView );
		gridview.setFocusable( false );
		gridview.setAdapter( serviciosListAdapter );

		ImageButton btnCall = (ImageButton) _view.findViewById( R.id.btnCall );
		btnCall.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				String uri = "tel:" + _hotel.getTelefono();
				Intent intent = new Intent( Intent.ACTION_CALL );
				intent.setData( Uri.parse( uri ) );
				startActivityForResult( intent, 0 );
			}
		} );

		ImageButton btnEmail = (ImageButton) _view.findViewById( R.id.btnMail );
		btnEmail.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				String to = _hotel.getEmail();
				Intent email = new Intent( Intent.ACTION_SEND );
				email.putExtra( Intent.EXTRA_EMAIL, new String[]{ to } );
				email.setType( "message/rfc822" );
				startActivityForResult( Intent.createChooser( email, "Send Email" ), 1 );
			}
		} );

		ImageButton btnChat = (ImageButton) _view.findViewById( R.id.btnChat );
		btnChat.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v )
			{
				testPresentReservation();
			}
		} );

		/*RadioButton btnNoticias = (RadioButton)_view.findViewById( R.id.btn_noticias );
		btnNoticias.setChecked( true );
		SegmentedGroup segmentedGroup = (SegmentedGroup)_view.findViewById( R.id.segmented );
		segmentedGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged( RadioGroup group, int checkedId )
			{
				handleFeedOptions();
			}
		} );

		_articlesRSS = new ArrayList<RSSArticle>();
		_articlesBlog = new ArrayList<RSSArticle>();
		if( _hotel.getPais().equalsIgnoreCase( "CO" ) )
		{
			_rssAddress = "http://cnnespanol.cnn.com/category/noticias/latinoamerica/colombia/feed/";
		}
		else if( _hotel.getPais().equalsIgnoreCase( "CR" ) )
		{
			_rssAddress = "http://cnnespanol.cnn.com/category/noticias/latinoamerica/costa-rica/feed/";
		}
		else
		{
			_rssAddress = "http://feeds2.feedburner.com/cnnmexico/portada";
		}
		new GetRSS( 1, _rssAddress ).execute();*/

		new GetWeather().execute();

		_mapView = (MapView) _view.findViewById( R.id.mapView );
		_mapView.onCreate( savedInstanceState );
		_mapView.onResume();

		try
		{
			MapsInitializer.initialize( getActivity().getApplicationContext() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		_map = _mapView.getMap();

		if( _map != null )
		{
			LatLng hotelPosition = new LatLng( _hotel.getLatitude(), _hotel.getLongitude() );
			Marker m = _map.addMarker( new MarkerOptions().position( hotelPosition ).title( _hotel.getNombre() ) );
			_map.moveCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
			m.showInfoWindow();
		}

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppEventTrack( "HOTEL DETAIL ANDROID", "DETAIL", "HOTEL", _hotel.getNombre(), 1 );

		return _view;
	}

	private String getAddressString()
	{
		String address = "";
		if( _hotel.getDireccion() != null && !_hotel.getDireccion().equalsIgnoreCase( "null" ) )
		{
			address += _hotel.getDireccion() + "\n";
		}
		if( _hotel.getColonia() != null && !_hotel.getColonia().equalsIgnoreCase( "null" ) )
		{
			address += _hotel.getColonia() + "\n";
		}
		if( _hotel.getCp() != null && !_hotel.getCp().equalsIgnoreCase( "null" ) )
		{
			address += "CP: " + _hotel.getCp() + "\n";
		}
		if( _hotel.getCiudad() != null && !_hotel.getCiudad().equalsIgnoreCase( "null" ) && _hotel.getEstado() != null && !_hotel.getEstado().equalsIgnoreCase( "null" ) )
		{
			address += _hotel.getCiudad() + ". " + _hotel.getEstado();
		}
		else if( _hotel.getCiudad() != null && !_hotel.getCiudad().equalsIgnoreCase( "null" ) )
		{
			address += _hotel.getCiudad();
		}
		else if( _hotel.getEstado() != null && !_hotel.getEstado().equalsIgnoreCase( "null" ) )
		{
			address += _hotel.getEstado();
		}
		if( address.endsWith( "\n" ) )
		{
			address = address.substring( 0, address.length() - 1 );
		}
		return address;
	}

	private void testPresentReservation()
	{
	}

	public void handleFeedOptions()
	{
		RadioButton btnNoticias = (RadioButton)_view.findViewById( R.id.btn_noticias );
		NestedListView list = (NestedListView) _view.findViewById( R.id.list_rss );
		TextView source = (TextView) _view.findViewById( R.id.lblSource );
		if( btnNoticias.isChecked() )
		{
			android.util.Log.d( "TEST", "Noticias" );
			source.setVisibility( View.VISIBLE );
			if( _articlesRSS.size() == 0 )
			{
				list.setAdapter( null );
				new GetRSS( 1, _rssAddress ).execute();
			}
			else
			{
				RSSArticleListAdapter adapter = new RSSArticleListAdapter( getActivity(), _articlesRSS );
				list.setFocusable( false );
				list.setAdapter( adapter );
				list.setOnItemClickListener( new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
					{
						Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( _articlesRSS.get( position ).getUrl() ) );
						startActivity( browserIntent );
					}
				} );
			}
		}
		else
		{
			android.util.Log.d( "TEST", "Blog" );
			source.setVisibility( View.GONE );
			if( _articlesBlog.size() == 0 )
			{
				list.setAdapter( null );
				new GetRSS( 2, "http://blog.cityexpress.com/tag/" + _hotel.getIdEstado() + "/feed/" ).execute();
			}
			else
			{
				BlogArticleListAdapter adapter = new BlogArticleListAdapter( getActivity(), _articlesBlog );
				list.setFocusable( false );
				list.setAdapter( adapter );
				list.setOnItemClickListener( new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
					{
						Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( _articlesBlog.get( position ).getUrl() ) );
						startActivity( browserIntent );
					}
				} );
			}
		}
	}

	public void weatherObtained()
	{
		//LinearLayout panel = (LinearLayout) _view.findViewById( R.id.tblWeather );
		//panel.setVisibility( View.VISIBLE );

		if( _weatherReport.getWeathers().length > 0 )
		{
			ImageView imgWeather = (ImageView) _view.findViewById( R.id.imgWeather );
			imgWeather.setImageResource( GetResourceForWeatherIcon( _weatherReport.getWeathers()[0].getIcon() ) );

			TextView lblDesc = (TextView) _view.findViewById( R.id.lblWDesc );
			lblDesc.setText( _weatherReport.getWeathers()[0].getDescription() );
		}

		TextView lblTemp = (TextView) _view.findViewById( R.id.lblWTemp );
		lblTemp.setText( String.format( "%.0f", _weatherReport.getTemperature() ) + "°C" );
	}

	private void feedObtained( int type, int result, List<RSSArticle> articles )
	{
		if( result == 0 )
		{
			android.util.Log.d( "TEST", "ARTICLES: " + articles.size() );
			if( type == 1 )
			{
				for( int i = articles.size() - 1; i >= 10; i-- )
					articles.remove( i );
				_articlesRSS = articles;
				RSSArticleListAdapter adapter = new RSSArticleListAdapter( getActivity(), _articlesRSS );
				NestedListView list = (NestedListView) _view.findViewById( R.id.list_rss );
				list.setFocusable( false );
				list.setAdapter( adapter );
				list.setOnItemClickListener( new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
					{
						Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( _articlesRSS.get( position ).getUrl() ) );
						startActivity( browserIntent );
					}
				} );
			}
			else if( type == 2 )
			{
				for( int i = articles.size() - 1; i >= 10; i-- )
					articles.remove( i );
				_articlesBlog = articles;
				BlogArticleListAdapter adapter = new BlogArticleListAdapter( getActivity(), _articlesBlog );
				NestedListView list = (NestedListView) _view.findViewById( R.id.list_rss );
				list.setFocusable( false );
				list.setAdapter( adapter );
				list.setOnItemClickListener( new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
					{
						Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( _articlesBlog.get( position ).getUrl() ) );
						startActivity( browserIntent );
					}
				} );
			}
		}
	}

	private class GetWeather extends AsyncTask<Void, Void, Integer>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall( "http://api.openweathermap.org/data/2.5/weather?lat=" + _hotel.getLatitude() + "&lon=" + _hotel.getLongitude() + "&lang=ES&units=metric", ServiceHandler.GET );

			if( jsonStr != null )
			{
				try
				{
					JSONObject json = new JSONObject( jsonStr );
					_weatherReport = new WeatherReport( json );
					if( !_weatherReport.isParsedOk() )
						return -3;

					return 0;
				}
				catch( Exception e )
				{
					android.util.Log.e( "JSONParser", "Cant parse: " + e.getMessage() );
					return -2;
				}
			}
			else android.util.Log.e( "ServiceHandler", "Couldn't get any data" );

			return -1;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );

			if( result == 0 ) weatherObtained();
		}
	}

	private class GetRSS extends AsyncTask<Void, Void, Integer>
	{
		private int _task;
		private String _feed;
		private List<RSSArticle> _articles;

		public GetRSS( int task, String feed )
		{
			_task = task;
			_feed = feed;
			_articles = null;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			try
			{
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				URL url = new URL(_feed);
				RSSHandler handler = new RSSHandler();
				xr.setContentHandler( handler );
				xr.parse( new InputSource(url.openStream()) );
				_articles = handler.getArticleList();
				return 0;
			}
			catch( Exception e )
			{
				android.util.Log.d( "RSSParser", "ERROR: " + e.getMessage() );
				e.printStackTrace();
			}

			return -1;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );

			feedObtained( _task, result, _articles );
		}
	}

	private int GetResourceForWeatherIcon( String icon )
	{
		if( icon.equalsIgnoreCase( "01d" ) ) return R.drawable.w_01d;
		else if( icon.equalsIgnoreCase( "01n" ) ) return R.drawable.w_01n;
		else if( icon.equalsIgnoreCase( "02d" ) ) return R.drawable.w_02d;
		else if( icon.equalsIgnoreCase( "02n" ) ) return R.drawable.w_02n;
		else if( icon.equalsIgnoreCase( "03d" ) ) return R.drawable.w_03d;
		else if( icon.equalsIgnoreCase( "03n" ) ) return R.drawable.w_03n;
		else if( icon.equalsIgnoreCase( "04d" ) ) return R.drawable.w_04d;
		else if( icon.equalsIgnoreCase( "04n" ) ) return R.drawable.w_04n;
		else if( icon.equalsIgnoreCase( "09d" ) ) return R.drawable.w_09d;
		else if( icon.equalsIgnoreCase( "09n" ) ) return R.drawable.w_09n;
		else if( icon.equalsIgnoreCase( "10d" ) ) return R.drawable.w_10d;
		else if( icon.equalsIgnoreCase( "10n" ) ) return R.drawable.w_10n;
		else if( icon.equalsIgnoreCase( "11d" ) ) return R.drawable.w_11d;
		else if( icon.equalsIgnoreCase( "11n" ) ) return R.drawable.w_11n;
		else if( icon.equalsIgnoreCase( "13d" ) ) return R.drawable.w_13d;
		else if( icon.equalsIgnoreCase( "13n" ) ) return R.drawable.w_13n;
		else if( icon.equalsIgnoreCase( "50d" ) ) return R.drawable.w_50d;
		else if( icon.equalsIgnoreCase( "50n" ) ) return R.drawable.w_50n;
		else return R.drawable.w_01d;
	}
}
