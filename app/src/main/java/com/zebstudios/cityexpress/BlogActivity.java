package com.zebstudios.cityexpress;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.appsee.Appsee.startScreen;


/**
 * Created by Denumeris Interactive on 3/3/2015.
 */
public class BlogActivity extends ActionBarActivity
{
	private List<RSSArticle> _articlesBlog;
	private ProgressDialogFragment _progress;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		startScreen("ViewBlog");
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_blog );

		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setIcon( android.R.color.transparent );

		if( CompatibilityUtil.isTablet( this ) )
		{
			ListView list = (ListView) findViewById( R.id.list_rss );
			list.setPadding( 140, 60, 140, 60 );
		}

		_articlesBlog = new ArrayList<RSSArticle>();
		new GetRSS( 2, "https://www.cityexpress.com/blog/feed/" ).execute(); //http://blog.cityexpress.com/tag/14/feed/

		Analytics analytics = (Analytics)getApplication();
		analytics.sendAppScreenTrack( "BLOG ANDROID" );
		startScreen("ViewBlog");
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		super.onOptionsItemSelected( item );
		switch( item.getItemId() )
		{
			case android.R.id.home:
				BlogActivity.this.onBackPressed();
				break;
		}
		return true;
	}

	private void feedObtained( int type, int result, List<RSSArticle> articles )
	{
		if( result == 0 )
		{
			android.util.Log.d( "TEST", "ARTICLES: " + articles.size() );
			for( int i = articles.size() - 1; i >= 10; i-- )
				articles.remove( i );
			_articlesBlog = articles;
			BlogArticleListAdapter adapter = new BlogArticleListAdapter( this, _articlesBlog );
			ListView list = (ListView) findViewById( R.id.list_rss );
			list.setFocusable( false );
			list.setAdapter( adapter );
			list.setOnItemClickListener( new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick( AdapterView<?> parent, View view, int position, long id )
				{
					Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( _articlesBlog.get( position ).getUrl() ) );
					startActivity( browserIntent );
				}
			} );
		}
	}

	private class GetRSS extends AsyncTask<Void, Void, Integer>
	{
		private int _task;
		private String _feed;
		private List<RSSArticle> _articles;

		public GetRSS( int task, String feed )
		{
			_task = task;
			_feed = feed;
			_articles = null;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			_progress = ProgressDialogFragment.newInstance();
			_progress.setCancelable( false );
			_progress.show( getSupportFragmentManager(), "Dialog" );
		}

		@Override
		protected Integer doInBackground( Void... arg0 )
		{
			try
			{
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				URL url = new URL(_feed);
				RSSHandler handler = new RSSHandler();
				xr.setContentHandler( handler );
				xr.parse( new InputSource(url.openStream()) );
				_articles = handler.getArticleList();
				return 0;
			}
			catch( Exception e )
			{
				android.util.Log.d( "RSSParser", "ERROR: " + e.getMessage() );
				e.printStackTrace();
			}

			return -1;
		}

		@Override
		protected void onPostExecute( Integer result )
		{
			super.onPostExecute( result );
			_progress.dismiss();

			feedObtained( _task, result, _articles );
		}
	}
}
