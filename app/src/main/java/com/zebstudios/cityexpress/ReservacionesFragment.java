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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ReservacionesFragment extends Fragment
{
	View _view;
    ReservationsListAdapter reservationsListAdapter;
    ArrayList<ReservacionBD> _reservations;
	ReservacionBD reservacionesBD;
    ReservacionBDD ds;
    ListView list;
    Date hoy;
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

		ds = new ReservacionBDD( getActivity() );
		ds.open();
		_reservations = ds.getReservations();

        reservationsListAdapter = new ReservationsListAdapter( getActivity(), _reservations );
		list = (ListView)_view.findViewById( R.id.list_reservations );
		list.setAdapter( reservationsListAdapter );


        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR); //obtenemos el año
        int mes = c.get(Calendar.MONTH); //obtenemos el mes
        String mesT = Integer.toString(mes);
        int dia = c.get(Calendar.DAY_OF_MONTH); // obtemos el día.
        switch (mesT){
            case "0":
                mesT = "ene";
                break;
            case "1":
                mesT = "feb";
                break;
            case "2":
                mesT = "mar";
                break;
            case "3":
                mesT = "abr";
                break;
            case "4":
                mesT = "may";
                break;
            case "5":
                mesT = "jun";
                break;
            case "6":
                mesT = "jul";
                break;
            case "7":
                mesT = "ago";
                break;
            case "8":
                mesT = "sep";
                break;
            case "9":
                mesT = "oct";
                break;
            case "10":
                mesT = "nov";
                break;
            case "11":
                mesT = "dic";
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
        hoy = c.getTime();
        hoy.getTime();
        System.out.println("Hoy->"+ hoy.getTime());

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
                                    System.out.println("->" + reservationsListAdapter.getItem(position) + "NUmoeri de reservacion"+_reservations.get(position).getNumReservacion());
                                    if(hoy.before(_reservations.get(position).getFechaLlegada())) {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Hoteles City")
                                                .setMessage("Deseas eliminar esta reservación?")
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        System.out.println("NUMERORESERVACION" + _reservations.get(position).getNumReservacion());
                                                        ds.delete(_reservations.get(position).getNumReservacion());
                                                        _reservations.remove(reservationsListAdapter.getItem(position));
                                                        reservationsListAdapter = new ReservationsListAdapter(getActivity(), _reservations);
                                                        list.setAdapter(reservationsListAdapter);
                                                    }

                                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create().show();
                                    }
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
