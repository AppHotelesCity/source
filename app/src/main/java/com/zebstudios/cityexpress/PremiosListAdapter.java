package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Denumeris Interactive on 4/28/2015.
 */
public class PremiosListAdapter extends BaseAdapter
{
	private final Context _context;
	private final List<PremiosExternoClient.Prze> _items;

	public PremiosListAdapter( Context context, List<PremiosExternoClient.Prze> items )
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
			v = inflater.inflate( R.layout.cat_pri_item, parent, false );
		}
		TextView text = (TextView) v.findViewById( R.id.lblText );

		PremiosExternoClient.Prze prze = _items.get( position );
		text.setText( prze.getPrze_Dscr() );

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
