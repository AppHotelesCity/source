package com.zebstudios.cityexpress;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

/**
 * *Created by Denumeris Interactive on 23/10/2014.
 */
public class State implements Serializable
{
	private static final long serialVersionUID = 0L;

	private int _id;
	private String _nombre;
	private boolean _parsedOk;

	public State( JSONObject json )
	{
		try
		{
			_id = json.getInt( "ID" );
			_nombre = json.getString( "Nombre" );

			_parsedOk = true;
		}
		catch( Exception e )
		{
			_parsedOk = false;
			android.util.Log.e( "Hotel", "Cant parse estado: " + e.getMessage() );
		}
	}

	public State( int id, String nombre )
	{
		_id = id;
		_nombre = nombre;
	}

	public int getId()
	{
		return _id;
	}

	public String getNombre()
	{
		return _nombre;
	}

	public boolean isParsedOk()
	{
		return _parsedOk;
	}

	public static class StateComparator implements Comparator<State>
	{
		public int compare( State c1, State c2 )
		{
			return c1.getNombre().compareToIgnoreCase( c2.getNombre() );
		}
	}
}