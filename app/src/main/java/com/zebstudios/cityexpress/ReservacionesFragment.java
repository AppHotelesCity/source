package com.zebstudios.cityexpress;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;



/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ReservacionesFragment extends Fragment
{
	View _view;
	ArrayList<ReservacionBD> _reservations;
	ReservacionBD reservacionesBD;
	//RealmResults<ReservacionBD> reservacionesBD;

	public ReservacionesFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		_view = inflater.inflate( R.layout.fragment_reservaciones, container, false );

        _reservations = new ArrayList<>();

		ReservacionBDD ds = new ReservacionBDD( getActivity() );
		ds.open();
		_reservations = ds.getReservations();
		ds.close();

		final ReservationsListAdapter reservationsListAdapter = new ReservationsListAdapter( getActivity(), _reservations );
		ListView list = (ListView)_view.findViewById( R.id.list_reservations );
		list.setAdapter( reservationsListAdapter );

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        list,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    System.out.println("->" + reservationsListAdapter.getItem(position));
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Hoteles City")
                                            .setMessage("Deseas eliminar esta reservación?")
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    _reservations.remove(position);
                                                }

                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create().show();

                                }
                                reservationsListAdapter.notifyDataSetChanged();
                            }
                        });
        list.setOnTouchListener(touchListener);
        list.setOnScrollListener(touchListener.makeScrollListener());



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


	@Override
	public void onResume()
	{
		super.onResume();
		ActionBarActivity activity = (ActionBarActivity)getActivity();
		activity.getSupportActionBar().setTitle( "Reservaciones" );
		activity.getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.blue_button));
	}

	private void reservationSelected( int index )
	{
        Intent intent = new Intent(getActivity(), MiReservacionDetailActivity.class);
        intent.putExtra("numReservacion",_reservations.get(index).getNumReservacion());
        startActivity(intent);
	}
}
