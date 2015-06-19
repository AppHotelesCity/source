package com.zebstudios.cityexpress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Denumeris Interactive on 24/10/2014.
 */
public class RSSArticleListAdapter extends BaseAdapter
{
	private final Context _context;
	private final List<RSSArticle> _items;
	private ImageCache _imageCache;

	public RSSArticleListAdapter( Context context, List<RSSArticle> items )
	{
		_context = context;
		_items = items;
		_imageCache = new ImageCache( context );
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		View v = convertView;

		if( v == null )
		{
			LayoutInflater inflater = LayoutInflater.from( _context );
			v = inflater.inflate( R.layout.feed_item, parent, false );
		}
		TextView title = (TextView) v.findViewById( R.id.lblText );

		RSSArticle article = _items.get( position );
		title.setText( article.getTitle() );

		if( article.getMedia() != null )
		{
			CircularImageView imageView = (CircularImageView) v.findViewById( R.id.imageView );
			new ImageLoader( imageView, _imageCache ).execute( article.getMedia() );
		}

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
