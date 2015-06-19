package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Denumeris Interactive on 22/10/2014.
 */
public class PromotionsListAdapter extends BaseAdapter
{
	private final Context _context;
	private final ArrayList<Promotion> _items;
	private ImageCache _imageCache;

	public PromotionsListAdapter( Context context, ArrayList<Promotion> items )
	{
		_context = context;
		_items = items;
		_imageCache = new ImageCache( context );
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		View v = convertView;
		ImageView picture;
		TextView name;

		if( v == null )
		{
			LayoutInflater inflater = LayoutInflater.from( _context );
			v = inflater.inflate( R.layout.promos_gridview_item, parent, false );
			v.setTag( R.id.picture, v.findViewById( R.id.picture ) );
			v.setTag( R.id.text, v.findViewById( R.id.text ) );
		}

		picture = (ImageView) v.getTag( R.id.picture );
		name = (TextView) v.getTag( R.id.text );

		Promotion promo = _items.get( position );

		new ImageLoader( picture, _imageCache ).execute( promo.getImageURL() );
		name.setText( promo.getTitle() );

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
