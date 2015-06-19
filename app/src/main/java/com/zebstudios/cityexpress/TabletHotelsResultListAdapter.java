package com.zebstudios.cityexpress;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.appsee.Appsee.startScreen;

/**
 * Created by Denumeris Interactive on 24/11/2014.
 */
public class TabletHotelsResultListAdapter extends ArrayAdapter<Hotel>
{
	private final Activity _context;
	private final ArrayList<Hotel> _items;
	private ImageCache _imageCache;

	public TabletHotelsResultListAdapter( Activity context, ArrayList<Hotel> items )
	{
		super( context, R.layout.hotelresult_tablet_item, items );
		_context = context;
		_items = items;

		_imageCache = new ImageCache( context );
		startScreen("ViewHotelReservaResult-Tablet");
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		View rowView = convertView;
		if( rowView == null )
		{
			LayoutInflater inflater = _context.getLayoutInflater();
			rowView = inflater.inflate( R.layout.hotelresult_tablet_item, null );
		}

		Hotel item = _items.get( position );
		if( item != null )
		{
			TextView tt = (TextView)rowView.findViewById( R.id.lblText );
			if( tt != null )
				tt.setText( item.getNombre() );
		}

		return rowView;
	}
}
