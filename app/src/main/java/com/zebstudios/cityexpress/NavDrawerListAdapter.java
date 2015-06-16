package com.zebstudios.cityexpress;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rczuart on 22/10/2014.
 */
public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerListAdapter.ListItem>
{
	private final Activity _context;
	private final ArrayList<ListItem> _items;

	public NavDrawerListAdapter( Activity context, ArrayList<ListItem> items )
	{
		super( context, R.layout.navdrawer_cell, items );
		_context = context;
		_items = items;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		View rowView = convertView;
		if( rowView == null )
		{
			LayoutInflater inflater = _context.getLayoutInflater();
			rowView = inflater.inflate( R.layout.navdrawer_cell, null );
		}

		ListItem item = _items.get( position );
		if( item != null )
		{
			TextView tt = (TextView)rowView.findViewById( R.id.lblText );
			if( tt != null )
			{
				Drawable img = _context.getResources().getDrawable( item.getDrawable() );
				tt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );
				tt.setText( item.getName() );
			}

		}

		return rowView;
	}

	public static class ListItem
	{
		private String _name;
		private int _drawable;

		public ListItem( String name, int drawable )
		{
			_name = name;
			_drawable = drawable;
		}

		public String getName()
		{
			return _name;
		}

		public void setName( String name )
		{
			_name = name;
		}

		public int getDrawable()
		{
			return _drawable;
		}

		public void setDrawable( int drawable )
		{
			_drawable = drawable;
		}
	}
}
