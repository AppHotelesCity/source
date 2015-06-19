package com.zebstudios.cityexpress;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Denumeris Interactive on 28/10/2014.
 */
public class RoomAvailablesPagerAdapter extends PagerAdapter
{
	private ArrayList<RoomAvailable> _items;
	private ImageCache _imageCache;
	private Context _context;

	public RoomAvailablesPagerAdapter( Context context, ArrayList<RoomAvailable> items )
	{
		_items = items;
		_context = context;
		_imageCache = new ImageCache( context );
	}

	@Override
	public int getCount()
	{
		return _items.size();
	}

	@Override
	public boolean isViewFromObject( View view, Object o )
	{
		return view == ( (View) o );
	}

	@Override
	public Object instantiateItem( ViewGroup container, int position )
	{
		LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View view = inflater.inflate( R.layout.room_available_item, null );

		RoomAvailable room = _items.get( position );

		ImageView imageView = (ImageView) view.findViewById( R.id.imgRoom );
		if( room.getImagen() != null && room.getImagen().length() > 0 )
			new ImageLoader( imageView, _imageCache ).execute( room.getImagen() );

		TextView lblRoom = (TextView) view.findViewById( R.id.lblRoom );
		lblRoom.setText( room.getTitle() );

		TextView lblTarifa = (TextView) view.findViewById( R.id.lblRoomTarifa );
		lblTarifa.setText( String.format( "Tarifa $%,.2f ", room.getTarifaProm() ) + room.getMoneda() );

		TextView lblRoomDesc = (TextView) view.findViewById( R.id.lblRoomDesc );
		lblRoomDesc.setText( room.getDescription() );

		( (ViewPager) container ).addView( view, 0 );
		return view;
	}

	@Override
	public void destroyItem( ViewGroup container, int position, Object object )
	{
		( (ViewPager) container ).removeView( (View) object );
	}
}
