package com.zebstudios.cityexpress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by Denumeris Interactive on 4/27/2015.
 */
public class PremiosUserLoggedDS
{
	private final String[] _pulColumns = { "pulId", "pulSocio", "pulLogged" };
	private CityExpressDBHelper _dbHelper;
	private SQLiteDatabase _database;

	public PremiosUserLoggedDS( Context context )
	{
		_dbHelper = new CityExpressDBHelper( context );
	}

	public void open()
	{
		_database = _dbHelper.getWritableDatabase();
	}

	public void close()
	{
		_dbHelper.close();
	}

	public void insert( PremiosUserLogged user )
	{
		ContentValues data = new ContentValues();
		data.put( "pulId", 1 );
		data.put( "pulSocio", user.getSocio() );
		data.put( "pulLogged", user.getLogged().getTime() );
		_database.insert( "PremiosUserLogged", null, data );
	}

	public void update( PremiosUserLogged user )
	{
		ContentValues data = new ContentValues();
		data.put( "pulSocio", user.getSocio() );
		data.put( "pulLogged", user.getLogged().getTime() );

		String where = "pulId=?";
		String[] whereArgs = { "1" };
		_database.update( "PremiosUserLogged", data, where, whereArgs );
	}

	public void clearUser()
	{
		String where = "pulId=?";
		String[] whereArgs = { "1" };
		_database.delete( "PremiosUserLogged", where, whereArgs );
	}

	public PremiosUserLogged getUserLogged()
	{
		PremiosUserLogged user = null;

		Cursor cursor = _database.query( "PremiosUserLogged", _pulColumns, null, null, null, null, null );
		cursor.moveToFirst();
		if( !cursor.isAfterLast() )
		{
			user = getUserLoggedFromCursor( cursor );
		}
		cursor.close();

		return user;
	}

	private PremiosUserLogged getUserLoggedFromCursor( Cursor cursor )
	{
		PremiosUserLogged user = new PremiosUserLogged();

		user.setSocio( cursor.getString( 1 ) );
		user.setLogged( new Date( cursor.getLong( 2 ) ) );

		return user;
	}

	public static class PremiosUserLogged
	{
		private String _socio;
		private Date _logged;

		public String getSocio()
		{
			return _socio;
		}

		public void setSocio( String socio )
		{
			_socio = socio;
		}

		public Date getLogged()
		{
			return _logged;
		}

		public void setLogged( Date logged )
		{
			_logged = logged;
		}
	}
}
