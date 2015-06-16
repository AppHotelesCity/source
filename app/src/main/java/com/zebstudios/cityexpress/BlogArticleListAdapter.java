package com.zebstudios.cityexpress;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rczuart on 02/12/2014.
 */
public class BlogArticleListAdapter extends BaseAdapter
{
	private final Context _context;
	private final List<RSSArticle> _items;
	private ImageCache _imageCache;

	public BlogArticleListAdapter( Context context, List<RSSArticle> items )
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
			v = inflater.inflate( R.layout.blog_item, parent, false );
		}
		TextView title = (TextView) v.findViewById( R.id.lblTitle );
		TextView text = (TextView) v.findViewById( R.id.lblText );

		RSSArticle article = _items.get( position );
		title.setText( article.getTitle() );
		text.setText( stripHtml( article.getDescription() ) );

		return v;
	}

	public String stripHtml(String html)
	{
		return Html.fromHtml( html ).toString();
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
