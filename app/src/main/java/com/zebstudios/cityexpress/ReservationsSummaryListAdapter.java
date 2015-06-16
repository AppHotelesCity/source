package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rczuart on 05/11/2014.
 */
public class ReservationsSummaryListAdapter extends BaseAdapter
{
	private final Context _context;
	private final ArrayList<Reservation.Room> _items;

	public ReservationsSummaryListAdapter( Context context, ArrayList<Reservation.Room> items )
	{
		_context = context;
		_items = items;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		View v = convertView;

		if( v == null )
		{
			LayoutInflater inflater = LayoutInflater.from( _context );
			v = inflater.inflate( R.layout.reservation_item, parent, false );
		}

		Reservation.Room item = _items.get( position );

		TextView textView1 = (TextView) v.findViewById( R.id.lblReservaNumber );
		textView1.setText( item.getReservationNumber() );

		TextView textView2 = (TextView) v.findViewById( R.id.lblTitular );
		textView2.setText( item.getReservationTitular() );

		return v;
	}

	public int getCount()
	{
		return _items.size();
	}

	public Object getItem( int position )
	{
		return null;
	}

	public long getItemId( int position )
	{
		return 0;
	}
}
