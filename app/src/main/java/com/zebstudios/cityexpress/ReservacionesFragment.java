package com.zebstudios.cityexpress;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ReservacionesFragment extends Fragment
{
	View _view;
	ArrayList<Reservation> _reservations;
	Button btnreservacion;

	public ReservacionesFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		_view = inflater.inflate( R.layout.fragment_reservaciones, container, false );

		btnreservacion = (Button) _view.findViewById(R.id.btnreservacion);

		btnreservacion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MiReservacionDetailActivity.class);
				startActivity(intent);
			}
		});
		ReservationDS ds = new ReservationDS( getActivity() );
		ds.open();
		_reservations = ds.getReservations();
		ds.close();

		Bundle args = getArguments();
		if( args != null )
		{
			long reservationId = args.getLong( "RESERVATION_ID", 0 );
			if( reservationId != 0 )
			{
				int reservationIndex = getReservationIndex( reservationId );
				if( reservationIndex != -1 )
				{
					reservationSelected( reservationIndex );
				}
			}
		}

		ReservationsListAdapter reservationsListAdapter = new ReservationsListAdapter( getActivity(), _reservations );
		ListView list = (ListView)_view.findViewById( R.id.list_reservations );
		list.setAdapter( reservationsListAdapter );
		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id )
			{
				reservationSelected( position );
			}
		} );

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppScreenTrack( "RESERVATIONS ANDROID" );

		return _view;
	}

	private int getReservationIndex( long reservationId )
	{
		for( int i=0; i<_reservations.size(); i++ )
		{
			if( _reservations.get( i ).getId() == reservationId )
				return i;
		}

		return -1;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity)getActivity();
		activity.getSupportActionBar().setTitle( "Reservaciones" );
	}

	private void reservationSelected( int index )
	{

		Bundle parameters = new Bundle();
		parameters.putSerializable( "RESERVATION", _reservations.get( index ) );

		Log.d("Reservacionesfragment", "Index :3:3:· --> " + _reservations.get(index).getId() );
		HotelReservaResultFragment fragment = new HotelReservaResultFragment();
		fragment.setArguments( parameters );
		getFragmentManager().beginTransaction().add( R.id.fragment_container, fragment ).addToBackStack( "ReservationDetail" ).commit();
	}
}
