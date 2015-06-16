package com.zebstudios.cityexpress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by rczuart on 23/10/2014.
 */
public class HotelServicesFragment extends Fragment
{
	private View _view;
	private Hotel _hotel;
	private ServicesListAdapter _serviciosListAdapter;

	public HotelServicesFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		// Inflate the layout for this fragment
		_view = inflater.inflate( R.layout.fragment_hotel_services, container, false );
		_hotel = (Hotel) getArguments().getSerializable( "HOTEL" );

		_serviciosListAdapter = new ServicesListAdapter( getActivity(), _hotel.getServicios() );
		ListView listView = (ListView)_view.findViewById( R.id.serviciosList );
		listView.setAdapter( _serviciosListAdapter );

		listView.setDivider( null );
		listView.setDividerHeight( 0 );

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppEventTrack( "HOTEL DETAIL ANDROID", "SERVICES", "HOTEL", _hotel.getNombre(), 1 );

		return _view;
	}

}
