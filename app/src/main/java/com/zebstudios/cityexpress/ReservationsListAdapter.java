package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.appsee.Appsee.startScreen;

/**
 * Created by Denumeris Interactive on 06/11/2014.
 */
public class ReservationsListAdapter extends BaseAdapter
{
	private final Context _context;
	private final ArrayList<ReservacionBD> _items;

	public ReservationsListAdapter( Context context, ArrayList<ReservacionBD> items )
	{
		_context = context;
		_items = items;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		startScreen("ViewReservations");

		View v = convertView;

		if( v == null )
		{
			LayoutInflater inflater = LayoutInflater.from( _context );
			v = inflater.inflate( R.layout.reservation_list_item, parent, false );
		}

		ReservacionBD item = _items.get( position );

		SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
		TextView txtNumber = (TextView) v.findViewById( R.id.txtNumber );
		txtNumber.setText( ""+ item.getNumReservacion() );
		TextView txtName = (TextView) v.findViewById( R.id.txtName );
		txtName.setText( item.getNombreHotel());
		TextView txtDates = (TextView) v.findViewById( R.id.txtDates );
		txtDates.setText(sdf.format(item.getFechaSalida()) + " / " + sdf.format(item.getFechaLlegada()));

		return v;
	}

	@Override
	public int getCount()
	{
		return _items.size();
	}

	@Override
	public Object getItem( int position )
	{
		return null;
	}

	@Override
	public long getItemId( int position )
	{
		return 0;
	}
}
