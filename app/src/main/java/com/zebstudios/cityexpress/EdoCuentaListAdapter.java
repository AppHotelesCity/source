package com.zebstudios.cityexpress;

import android.content.Context;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Denumeris Interactive on 4/28/2015.
 */
public class EdoCuentaListAdapter extends BaseAdapter
{
	private final Context _context;
	private final List<PremiosExternoClient.EdoCtaResponse.Record> _items;

	public EdoCuentaListAdapter( Context context, List<PremiosExternoClient.EdoCtaResponse.Record> items )
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
			v = inflater.inflate( R.layout.cuenta_item, parent, false );
		}
		SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yy" );

		PremiosExternoClient.EdoCtaResponse.Record rec = _items.get( position );

		TextView lblDate = (TextView) v.findViewById( R.id.lblDate );
		TextView lblDescription = (TextView) v.findViewById( R.id.lblDescription );
		TextView lblTotal = (TextView) v.findViewById( R.id.lblTotal );

		if( rec.getDate() != null )
			lblDate.setText( sdf.format( rec.getDate() ) );
		else
			lblDate.setText( "" );
		lblDescription.setText( rec.getDescription() );
		lblTotal.setText( String.format( "%,d", rec.getPoints() ) );

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
