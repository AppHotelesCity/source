package com.zebstudios.cityexpress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Denumeris Interactive on 12/11/2014.
 */
public class ReservanteDS
{
	private final String[] _reservanteColumns = { "rvntId", "rvntNombre", "rvntApellido", "rvntCorreo", "rvntSocio", "rvntAfiliate", "rvntTelefono", "rvntViaje" };
	private CityExpressDBHelper _dbHelper;
	private SQLiteDatabase _database;

	public ReservanteDS( Context context )
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

	public void insert( Reservante reservante )
	{
		ContentValues data = new ContentValues();
		data.put( "rvntId", 1 );
		data.put( "rvntNombre", reservante.getNombre() );
		data.put( "rvntApellido", reservante.getApellido() );
		data.put( "rvntCorreo", reservante.getCorreo() );
		data.put( "rvntSocio", reservante.getSocio() );
		data.put( "rvntAfiliate", reservante.isAfiliate() );
		data.put( "rvntTelefono", reservante.getTelefono() );
		data.put( "rvntViaje", reservante.getViaje() );
		_database.insert( "Reservante", null, data );
	}

	public void update( Reservante reservante )
	{
		ContentValues data = new ContentValues();
		data.put( "rvntNombre", reservante.getNombre() );
		data.put( "rvntApellido", reservante.getApellido() );
		data.put( "rvntCorreo", reservante.getCorreo() );
		data.put( "rvntSocio", reservante.getSocio() );
		data.put( "rvntAfiliate", reservante.isAfiliate() );
		data.put( "rvntTelefono", reservante.getTelefono() );
		data.put( "rvntViaje", reservante.getViaje() );

		String where = "rvntId=?";
		String[] whereArgs = { "1" };
		_database.update( "Reservante", data, where, whereArgs );
	}

	public Reservante getReservante()
	{
		Reservante reservante = null;

		Cursor cursor = _database.query( "Reservante", _reservanteColumns, null, null, null, null, null );
		cursor.moveToFirst();
		if( !cursor.isAfterLast() )
		{
			reservante = getReservanteFromCursor( cursor );
		}
		cursor.close();

		return reservante;
	}

	// { "rvntId", "rvntNombre", "rvntApellido", "rvntCorreo", "rvntSocio", "rvntAfiliate", "rvntTelefono", "rvntViaje" };
	private Reservante getReservanteFromCursor( Cursor cursor )
	{
		Reservante reservante = new Reservante();

		reservante.setNombre( cursor.getString( 1 ) );
		reservante.setApellido( cursor.getString( 2 ) );
		reservante.setCorreo( cursor.getString( 3 ) );
		reservante.setSocio( cursor.getString( 4 ) );
		reservante.setAfiliate( cursor.getInt( 5 ) > 0 ? Boolean.TRUE : Boolean.FALSE );
		reservante.setTelefono( cursor.getString( 6 ) );
		reservante.setViaje( cursor.getInt( 7 ) );

		return reservante;
	}

	public static class Reservante
	{
		private String _nombre;
		private String _apellido;
		private String _correo;
		private String _socio;
		private boolean _afiliate;
		private String _telefono;
		private int _viaje;

		public Reservante()
		{
		}

		public String getNombre()
		{
			return _nombre;
		}

		public void setNombre( String nombre )
		{
			_nombre = nombre;
		}

		public String getApellido()
		{
			return _apellido;
		}

		public void setApellido( String apellido )
		{
			_apellido = apellido;
		}

		public String getCorreo()
		{
			return _correo;
		}

		public void setCorreo( String correo )
		{
			_correo = correo;
		}

		public String getSocio()
		{
			return _socio;
		}

		public void setSocio( String socio )
		{
			_socio = socio;
		}

		public boolean isAfiliate()
		{
			return _afiliate;
		}

		public void setAfiliate( boolean afiliate )
		{
			_afiliate = afiliate;
		}

		public String getTelefono()
		{
			return _telefono;
		}

		public void setTelefono( String telefono )
		{
			_telefono = telefono;
		}

		public int getViaje()
		{
			return _viaje;
		}

		public void setViaje( int viaje )
		{
			_viaje = viaje;
		}
	}
}
