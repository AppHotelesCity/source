package com.zebstudios.cityexpress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by rczuart on 22/10/2014.
 */
public class ImageCache
{
	// http://stackoverflow.com/questions/10185898/using-disklrucache-in-android-4-0-does-not-provide-for-opencache-method
	private LruCache<String, Bitmap> _memoryCache;

	private DiskLruCache _diskLruCache;
	private final Object _diskCacheLock = new Object();
	private boolean _diskCacheStarting = true;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";

	private Context _context;

	public ImageCache( Context context )
	{
		_context = context;

		final int maxMemory = (int) ( Runtime.getRuntime().maxMemory() / 1024 );
		final int cacheSize = maxMemory / 4;

		_memoryCache = new LruCache<String, Bitmap>( cacheSize )
		{
			@Override
			protected int sizeOf( String key, Bitmap bitmap )
			{
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};

		File cacheDir = getDiskCacheDir( _context, DISK_CACHE_SUBDIR );
		new InitDiskCacheTask().execute( cacheDir );
	}

	private Bitmap getBitmapFromMemCache( String key )
	{
		return _memoryCache.get( key );
	}

	public void addBitmapToCache( String key, Bitmap bitmap )
	{
		// Add to memory cache as before
		if( getBitmapFromMemCache( key ) == null )
			_memoryCache.put( key, bitmap );

		// Also add to disk cache
		synchronized( _diskCacheLock )
		{
			try
			{
				if( _diskLruCache != null && _diskLruCache.get( key ) == null )
					put( key, bitmap );
			}
			catch( Exception e )
			{
				android.util.Log.e( "ImageCache", "Cant add to cache: " + e.getMessage() );
			}
		}
	}

	public Bitmap getBitmapFromCache( String key )
	{
		Bitmap bmp = getBitmapFromMemCache( key );
		if( bmp != null )
		{
			return bmp;
		}

		if( diskContainsKey( key ) )
		{
			return getBitmapFromDiskCache( key );
		}
		else return null;
	}

	private boolean diskContainsKey( String key )
	{

		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try
		{
			snapshot = _diskLruCache.get( key );
			contained = snapshot != null;
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( snapshot != null )
			{
				snapshot.close();
			}
		}

		return contained;

	}

	public Bitmap getBitmapFromDiskCache( String key )
	{
		synchronized( _diskCacheLock )
		{
			// Wait while disk cache is started from background thread
			while( _diskCacheStarting )
			{
				try
				{
					_diskCacheLock.wait();
				}
				catch( InterruptedException e ) {}
			}
			if( _diskLruCache != null )
				return getBitmapFromDisk( key );
		}
		return null;
	}

	public Bitmap getBitmapFromDisk( String key )
	{
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try
		{

			snapshot = _diskLruCache.get( key );
			if( snapshot == null )
				return null;
			final InputStream in = snapshot.getInputStream( 0 );
			if( in != null )
			{
				final BufferedInputStream buffIn = new BufferedInputStream( in, ImageCacheUtil.IO_BUFFER_SIZE );
				bitmap = BitmapFactory.decodeStream( buffIn );
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( snapshot != null )
				snapshot.close();
		}

		return bitmap;

	}


	private boolean writeBitmapToFile( Bitmap bitmap, DiskLruCache.Editor editor ) throws IOException, FileNotFoundException
	{
		OutputStream out = null;
		try
		{
			out = new BufferedOutputStream( editor.newOutputStream( 0 ), ImageCacheUtil.IO_BUFFER_SIZE );
			return bitmap.compress( Bitmap.CompressFormat.PNG, 100, out );
		}
		finally
		{
			if( out != null )
			{
				out.close();
			}
		}
	}


	private void put( String key, Bitmap data )
	{
		DiskLruCache.Editor editor = null;
		try
		{
			editor = _diskLruCache.edit( key );
			if( editor == null )
				return;

			if( writeBitmapToFile( data, editor ) )
			{
				_diskLruCache.flush();
				editor.commit();
			}
			else
			{
				editor.abort();
				android.util.Log.d( "ImageCache", "ERROR on: image put on disk cache " + key );
			}
		}
		catch( IOException e )
		{
			android.util.Log.d( "ImageCache", "ERROR on: image put on disk cache " + key );
			try
			{
				if( editor != null )
					editor.abort();
			}
			catch( IOException ignored )
			{
			}
		}

	}

	class InitDiskCacheTask extends AsyncTask<File, Void, Void>
	{
		@Override
		protected Void doInBackground( File... params )
		{
			synchronized( _diskCacheLock )
			{
				try
				{
					File cacheDir = params[0];
					_diskLruCache = DiskLruCache.open( cacheDir, 1, 1, DISK_CACHE_SIZE );
					_diskCacheStarting = false; // Finished initialization
					_diskCacheLock.notifyAll(); // Wake any waiting threads
				}
				catch( Exception e )
				{
					android.util.Log.e( "ImageCache", "Cant open disk cache: " + e.getMessage() );
				}
			}
			return null;
		}
	}


	// Creates a unique subdirectory of the designated app cache directory. Tries to use external
	// but if not mounted, falls back on internal storage.
	public static File getDiskCacheDir( Context context, String uniqueName )
	{
		// Check if media is mounted or storage is built-in, if so, try and use external cache dir
		// otherwise use internal cache dir
		final String cachePath =
				Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState() ) ||
						!ImageCacheUtil.isExternalStorageRemovable() ? ImageCacheUtil.getExternalCacheDir( context ).getPath() :
						context.getCacheDir().getPath();

		return new File( cachePath + File.separator + uniqueName );
	}

	public void ClearCache()
	{
		try
		{
			_diskLruCache.delete();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
