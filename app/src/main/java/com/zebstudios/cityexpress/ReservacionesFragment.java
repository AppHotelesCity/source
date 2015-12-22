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

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ReservacionesFragment extends Fragment
{
	View _view;
	ArrayList<ReservacionBD> _reservations;
	RealmResults<ReservacionBD> reservacionesBD;

	public ReservacionesFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		_view = inflater.inflate( R.layout.fragment_reservaciones, container, false );

        _reservations = new ArrayList<>();

		/*ReservationDS ds = new ReservationDS( getActivity() );
		ds.open();
		_reservations = ds.getReservations();
		ds.close();*/

		Realm realm = Realm.getInstance(getActivity());

		reservacionesBD = realm.where(ReservacionBD.class).findAll();
        reservacionesBD.sort("numReservacion");

		for (int i = 0; i < reservacionesBD.size(); i++) {
			System.out.println("->" + reservacionesBD.get(i).getNombreUsuario() + reservacionesBD.get(i).getNombreHotel() + "RESERVACIONES->" + reservacionesBD.get(i).getNumReservacion());
            _reservations.add(
                    new ReservacionBD(reservacionesBD.get(i).getNumReservacion(),
                            reservacionesBD.get(i).getNombreUsuario(),
                            reservacionesBD.get(i).getNombreHotel(),
                            reservacionesBD.get(i).getFechaLlegada(),
                            reservacionesBD.get(i).getFechaSalida(),
                            reservacionesBD.get(i).getDeschabitacion(),
                            reservacionesBD.get(i).getDescHotel(),
                            reservacionesBD.get(i).getHabCosto(),
                            reservacionesBD.get(i).getTotal(),
                            reservacionesBD.get(i).getDireccionHotel(),
                            reservacionesBD.get(i).getDescripcionLugarHotel(),
                            reservacionesBD.get(i).getAdultos(),
                            reservacionesBD.get(i).getInfantes(),
                            reservacionesBD.get(i).getNumHabitaciones(),
                            reservacionesBD.get(i).getNumNoches(),
                            reservacionesBD.get(i).getLongitudHotel(),
                            reservacionesBD.get(i).getLatitudHotel()
            ));
		}

		/*Bundle args = getArguments();
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
		}*/

		ReservationsListAdapter reservationsListAdapter = new ReservationsListAdapter( getActivity(), _reservations );
		ListView list = (ListView)_view.findViewById( R.id.list_reservations );
		list.setAdapter( reservationsListAdapter );
		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id )
			{
				reservationSelected( position );
                System.out.println("POSICION"+position);
            }
		} );

		Analytics analytics = (Analytics)getActivity().getApplication();
		analytics.sendAppScreenTrack( "RESERVATIONS ANDROID" );

		return _view;
	}

	/*private int getReservationIndex( long reservationId )
	{
		for( int i=0; i<_reservations.size(); i++ )
		{
			/*if( _reservations.get( i ).getId() == reservationId )
				return i;
		}

		return -1;
	}*/

	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity)getActivity();
		activity.getSupportActionBar().setTitle( "Reservaciones" );
	}

	private void reservationSelected( int index )
	{
        Intent intent = new Intent(getActivity(), MiReservacionDetailActivity.class);
        _reservations.get(index);
        startActivity(intent);
		/*Bundle parameters = new Bundle();
		parameters.putSerializable( "RESERVATION", .get( index ) );

		Log.d("Reservacionesfragment", "Index :3:3:· --> " + _reservations.get(index).getId() );
		HotelReservaResultFragment fragment = new HotelReservaResultFragment();
		fragment.setArguments( parameters );
		getFragmentManager().beginTransaction().add( R.id.fragment_container, fragment ).addToBackStack( "ReservationDetail" ).commit();*/
	}
}
