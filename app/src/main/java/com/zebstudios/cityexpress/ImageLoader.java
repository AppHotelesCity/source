package com.zebstudios.cityexpress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rczuart on 23/10/2014.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap>
{
	private final WeakReference<ImageView> _imageReference;
	private final WeakReference<ImageCache> _cacheReference;

	public ImageLoader( ImageView view, ImageCache cache )
	{
		_imageReference = new WeakReference<ImageView>( view );
		_cacheReference = new WeakReference<ImageCache>( cache );
	}

	protected Bitmap doInBackground( String... urls )
	{
		String urldisplay = urls[0];
		Bitmap bitmap = null;

		//android.util.Log.w( "IMG LOADER", "URL: " + urldisplay );
		//android.util.Log.w( "IMG LOADER", "KEY: " + GetKeyForCache( urldisplay ) );



		try
		{
			ImageCache cache = _cacheReference.get();
			if( cache != null )
			{
				bitmap = cache.getBitmapFromCache( GetKeyForCache( urldisplay ) );
				if( bitmap != null )
					return bitmap;
			}

			InputStream data = getDataFromURL( urldisplay );
			if( data != null )
			{
				bitmap = BitmapFactory.decodeStream( data );
				if( bitmap != null )
				{
					cache.addBitmapToCache( GetKeyForCache( urldisplay ), bitmap );
				}
				else
					android.util.Log.w( "IMG LOADER", "IMAGE IS NULL" );
			}
		}
		catch( Exception e )
		{
			android.util.Log.e( "IMG LOADER", e.getMessage() );
			e.printStackTrace();
		}

		return bitmap;
	}

	protected void onPostExecute( Bitmap result )
	{
		ImageView imageView = _imageReference.get();
		if( imageView != null )
			imageView.setImageBitmap( result );
	}

	private InputStream getDataFromURL( String url )
	{
		try
		{
			URL obj = new URL( url );
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

			boolean redirect = false;

			// normally, 3xx is redirect
			int status = conn.getResponseCode();
			if( status != HttpURLConnection.HTTP_OK )
			{
				if( status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER )
					redirect = true;
			}

			if( redirect )
			{
				// get redirect url from "location" header field
				String newUrl = conn.getHeaderField( "Location" );

				// open the new connnection again
				conn = (HttpURLConnection) new URL( newUrl ).openConnection();
			}

			return conn.getInputStream();

		}
		catch( Exception e )
		{
			android.util.Log.e( "IMG LOADER", url );
			android.util.Log.e( "IMG LOADER", e.getMessage() );
			e.printStackTrace();
		}

		return null;
	}

	private String GetKeyForCache( String url )
	{
		String name = url.toLowerCase().replace( "http://", "" ).replace( "https://", "" ).replace( ".", "" ).replace( "/", "-" ).replace( "?", "-" ).replace( "=", "-" ).replace( "%2C", "-" ).replace( "%2c", "-" );
		if( name.length() > 120 )
			name = name.substring( 0, 60 );
		return name;
	}
}
