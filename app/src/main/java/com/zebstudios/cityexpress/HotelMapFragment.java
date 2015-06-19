package com.zebstudios.cityexpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.appsee.Appsee.startScreen;

/**
 * Created by Denumeris Interactive on 23/10/2014.
 */
public class HotelMapFragment extends Fragment
{
	private View _view;
	private Hotel _hotel;
	private GoogleMap _map;
	private MapView _mapView;
	private SupportMapFragment _fragment;

	public HotelMapFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
        startScreen("ViewHotelMap-Smartphone");
		_view = inflater.inflate( R.layout.fragment_hotel_map, container, false );
		_hotel = (Hotel) getArguments().getSerializable( "HOTEL" );

		_mapView = (MapView)_view.findViewById( R.id.mapView );
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
		//_map = ((SupportMapFragment) getFragmentManager().findFragmentById( R.id.map )).getMap();

		LatLng hotelPosition =  new LatLng( _hotel.getLatitude(), _hotel.getLongitude() );
		Marker m = _map.addMarker( new MarkerOptions().position( hotelPosition ).title( _hotel.getNombre() ) );
		//_map.animateCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
		_map.moveCamera( CameraUpdateFactory.newLatLngZoom( hotelPosition, 14 ) );
		m.showInfoWindow();

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppEventTrack( "HOTEL DETAIL ANDROID", "MAP", "HOTEL", _hotel.getNombre(), 1 );

		return _view;
	}
}
