package com.zebstudios.cityexpress;

import org.json.JSONObject;

/**
 * Created by Denumeris Interactive on 28/10/2014.
 */
public class RoomAvailableExtra
{
	private String _codigoHabitacion;
	private String _codigoHotel;
	private String _ubicacion;
	private String _descripcion;
	private String _descipcionLarga;
	private int _numPersonas;
	private int _id;
	private String _imgHabitacion;
	private String _imgHabitacionWeb;
	private String _imagen;
	private String _imagenApp;
	private boolean _activo;
	private boolean _parsedOk;

	public RoomAvailableExtra( JSONObject json )
	{
		try
		{
			_codigoHabitacion = json.getString( "Codigo_Habitacion" );
			_codigoHotel = json.getString( "Codigo_Hotel" );
			_ubicacion = json.getString( "Ubicacion" );
			_descripcion = json.getString( "Descripcion" );
			_descipcionLarga = json.getString( "Descripcion_Larga" );
			_numPersonas = json.getInt( "Num_de_Personas" );
			_id = json.getInt( "ID" );
			_imgHabitacion = json.getString( "ImgHabitacion" );
			_imgHabitacionWeb = json.getString( "ImgHabitacionWeb" );
			_imagen = json.getString( "Imagen" );
			_imagenApp = json.getString( "Imagen_App" ).replace( "http://23.253.241.31", APIAddress.HOTELS_WEB_BASE );
			_activo = json.getBoolean( "Activo" );
			_parsedOk = true;
		}
		catch( Exception e )
		{
			_parsedOk = false;
			android.util.Log.e( "Hotel", "Cant parse RoomAvailableExtra: " + e.getMessage() );
		}
	}

	public String getCodigoHabitacion()
	{
		return _codigoHabitacion;
	}

	public String getCodigoHotel()
	{
		return _codigoHotel;
	}

	public String getUbicacion()
	{
		return _ubicacion;
	}

	public String getDescripcion()
	{
		return _descripcion;
	}

	public String getDescipcionLarga()
	{
		return _descipcionLarga;
	}

	public int getNumPersonas()
	{
		return _numPersonas;
	}

	public int getId()
	{
		return _id;
	}

	public String getImgHabitacion()
	{
		return _imgHabitacion;
	}

	public String getImgHabitacionWeb()
	{
		return _imgHabitacionWeb;
	}

	public String getImagen()
	{
		return _imagen;
	}

	public boolean isActivo()
	{
		return _activo;
	}

	public boolean isParsedOk()
	{
		return _parsedOk;
	}

	public String getImagenApp()
	{
		return _imagenApp;
	}

	public void setImagenApp( String imagenApp )
	{
		_imagenApp = imagenApp;
	}
}
