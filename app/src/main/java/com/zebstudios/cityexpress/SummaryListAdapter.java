package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rczuart on 29/10/2014.
 */
public class SummaryListAdapter extends BaseAdapter
{
	private final Context _context;
	private ArrayList<SummaryEntry> _items;

	public SummaryListAdapter( Context context, ArrayList<SummaryEntry> items )
	{
		_context = context;
		_items = items;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		View v = convertView;

		SummaryEntry item = _items.get( position );

		if( v == null )
		{
			LayoutInflater inflater = LayoutInflater.from( _context );
			if( item.getType() == 0 )
				v = inflater.inflate( R.layout.summary_header_item, parent, false );
			else
				v = inflater.inflate( R.layout.summary_detail_item, parent, false );
		}

		TextView textView = (TextView) v.findViewById( R.id.lblText );
		textView.setText( item.getText() );

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
