package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by rczuart on 06/11/2014.
 */
public class ReservationsListAdapter extends BaseAdapter
{
	private final Context _context;
	private final ArrayList<Reservation> _items;

	public ReservationsListAdapter( Context context, ArrayList<Reservation> items )
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
			v = inflater.inflate( R.layout.reservation_list_item, parent, false );
		}

		Reservation item = _items.get( position );

		SimpleDateFormat sdf = new SimpleDateFormat( "d MMM yyyy" );
		TextView txtNumber = (TextView) v.findViewById( R.id.txtNumber );
		txtNumber.setText( item.getRooms().get( 0 ).getReservationNumber() );
		TextView txtName = (TextView) v.findViewById( R.id.txtName );
		txtName.setText( item.getHotelName() );
		TextView txtDates = (TextView) v.findViewById( R.id.txtDates );
		txtDates.setText( sdf.format( item.getArrivalDate() ) + " / " + sdf.format( item.getDepartureDate() ) );

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
