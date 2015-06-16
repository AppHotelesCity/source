package com.zebstudios.cityexpress;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by rczuart on 23/10/2014.
 */
public class Hotel implements Serializable
{
	private static final long serialVersionUID = 0L;

	private int _id;
	private String _siglas;
	private String _nombre;
	private String _direccion;
	private String _colonia;
	private String _zona;
	private String _region;
	private String _ciudad;
	private String _estado;
	private String _keywords5;
	private String _cp;
	private String _pais;
	private String _telefono;
	private String _fax;
	private String _gerente;
	private String _websiteES;
	private String _websiteEN;
	private String _descripcionMaps;
	private String _keywords;
	private double _latitude;
	private double _longitude;
	private Date _apertura;
	private String _lugaresCercanos;
	private String _googlePlus;
	private String _email;
	private String _imagenPrincipal;
	private String[] _imagenesExtra;
	private int[] _servicios;
	private int _idMarca;
	private String _imagenFondoApp;
	private boolean _parsedOk;
	private int _idEstado;
	private String _merchantUserName;
	private String _merchantPassword;
	private String _signature;
	private boolean _blockCC;

	private  int _source;

	public Hotel( JSONObject json )
	{
		this( json, 0 );
	}

	public Hotel( JSONObject json, int source )
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
		try
		{
			_id = json.getInt( "ID" );
			_siglas = json.getString( "Siglas" );
			_nombre = json.getString( "Nombre" );
			_direccion = json.getString( "Direccion" );
			_colonia = json.getString( "Colonia" );
			_zona = json.getString( "Zona" );
			_region = json.getString( "Region" );
			_ciudad = json.getString( "Ciudad" );
			_estado = json.getString( "Estado" );
			_keywords5 = json.getString( "Keywords_5" );
			_cp = json.getString( "CP" );
			_pais = json.getString( "Pais" );
			_telefono = json.getString( "Telefono" );
			_fax = json.getString( "Fax" );
			_gerente = json.getString( "Gerente" );
			_websiteES = json.getString( "Website_ES" );
			_websiteEN = json.getString( "Website_EN" );
			_descripcionMaps = json.getString( "Descripcion_Maps" );
			_keywords = json.getString( "Keywords" );
			if( json.getString( "Latitud" ).length() > 0 && json.getString( "Longitud" ).length() > 0 )
			{
				if( !json.getString( "Latitud" ).equalsIgnoreCase( "NULL" ) && !json.getString( "Longitud" ).equalsIgnoreCase( "NULL" ) )
				{
					_latitude = Double.parseDouble( json.getString( "Latitud" ) );
					_longitude = Double.parseDouble( json.getString( "Longitud" ) );
				}
			}
			String tempDate = json.getString( "Fecha_Apertura" ).trim();
			//if( tempDate.length() > 0 && !tempDate.equalsIgnoreCase( "NULL" ) )
			//	_apertura = dateFormat.parse( json.getString( "Fecha_Apertura" ) );
			_lugaresCercanos = json.getString( "Lugares_Cercanos" );
			_googlePlus = json.getString( "Google_Plus" );
			_email = json.getString( "Email" );
			_imagenPrincipal = json.getString( "Imagen_Principal" ).trim();
			if( _imagenPrincipal.length() > 0 )
			{
				if( _imagenPrincipal.startsWith( "/media" ) )
					_imagenPrincipal = APIAddress.HOTELS_WEB_BASE + _imagenPrincipal;
			}
			_imagenesExtra = json.getString( "Imagenes_Extra" ).replace( "\r\n", "" ).split( "," );
			_idMarca = json.getInt( "IdMarca" );
			_imagenFondoApp = json.getString( "Imagen_Fondo_Hotel_App" );

			ArrayList<String> imagesOk = new ArrayList<String>();
			for( int i=0; i<_imagenesExtra.length; i++ )
			{
				if( _imagenesExtra[i] != null )
				{
					if( _imagenesExtra[i].trim().length() > 0 )
					{
						if( _imagenesExtra[i].trim().startsWith( "http" ) )
							imagesOk.add( _imagenesExtra[i].trim() );
						else
							android.util.Log.d( "VERIFY", _imagenesExtra[i].trim() );
					}
				}
			}

			_imagenesExtra = new String[ imagesOk.size() ];
			for( int i=0; i<imagesOk.size(); i++ )
				_imagenesExtra[i] = imagesOk.get( i );

			String servs = json.getString( "Servicios" );
			if( servs.length() > 0 )
			{
				String[] temp = servs.split( "," );
				_servicios = new int[temp.length];
				for( int i = 0; i < temp.length; i++ )
					_servicios[i] = Integer.parseInt( temp[i] );
			}
			else
				_servicios = new int[0];

			_source = source;
			_idEstado = json.getInt( "IdEstado" );

			_merchantUserName = json.getString( "MerchantUserName" );
			_merchantPassword = json.getString( "MerchantPassword" );
			_signature = json.getString( "Signature" );
			_blockCC = json.getString( "BloqueoTC" ) == "true" ? true : false;

			_parsedOk = true;
		}
		catch( Exception e )
		{
			_parsedOk = false;
			android.util.Log.e( "Hotel", "Cant parse hotel: " + e.getMessage() );
		}
	}

	public int getId()
	{
		return _id;
	}

	public String getSiglas()
	{
		return _siglas;
	}

	public String getNombre()
	{
		return _nombre;
	}

	public String getDireccion()
	{
		return _direccion;
	}

	public String getColonia()
	{
		return _colonia;
	}

	public String getZona()
	{
		return _zona;
	}

	public String getRegion()
	{
		return _region;
	}

	public String getCiudad()
	{
		return _ciudad;
	}

	public String getEstado()
	{
		return _estado;
	}

	public String getKeywords5()
	{
		return _keywords5;
	}

	public String getCp()
	{
		return _cp;
	}

	public String getPais()
	{
		return _pais;
	}

	public String getTelefono()
	{
		return _telefono;
	}

	public String getFax()
	{
		return _fax;
	}

	public String getGerente()
	{
		return _gerente;
	}

	public String getWebsiteES()
	{
		return _websiteES;
	}

	public String getWebsiteEN()
	{
		return _websiteEN;
	}

	public String getDescripcionMaps()
	{
		return _descripcionMaps;
	}

	public String getKeywords()
	{
		return _keywords;
	}

	public double getLatitude()
	{
		return _latitude;
	}

	public double getLongitude()
	{
		return _longitude;
	}

	public Date getApertura()
	{
		return _apertura;
	}

	public String getLugaresCercanos()
	{
		return _lugaresCercanos;
	}

	public String getGooglePlus()
	{
		return _googlePlus;
	}

	public String getEmail()
	{
		return _email;
	}

	public String getImagenPrincipal()
	{
		return _imagenPrincipal;
	}

	public String[] getImagenesExtra()
	{
		return _imagenesExtra;
	}

	public int[] getServicios()
	{
		return _servicios;
	}

	public int getIdMarca()
	{
		return _idMarca;
	}

	public String getImagenFondoApp()
	{
		return _imagenFondoApp;
	}

	public boolean isParsedOk()
	{
		return _parsedOk;
	}

	public int getSource()
	{
		return _source;
	}

	public void setSource( int source )
	{
		_source = source;
	}

	public int getIdEstado()
	{
		return _idEstado;
	}

	public void setIdEstado( int idEstado )
	{
		_idEstado = idEstado;
	}

	public boolean isBlockCC()
	{
		return _blockCC;
	}

	public void setBlockCC( boolean blockCC )
	{
		_blockCC = blockCC;
	}

	public String getSignature()
	{
		return _signature;
	}

	public void setSignature( String signature )
	{
		_signature = signature;
	}

	public String getMerchantPassword()
	{
		return _merchantPassword;
	}

	public void setMerchantPassword( String merchantPassword )
	{
		_merchantPassword = merchantPassword;
	}

	public String getMerchantUserName()
	{
		return _merchantUserName;
	}

	public void setMerchantUserName( String merchantUserName )
	{
		_merchantUserName = merchantUserName;
	}

	public static class HotelComparator implements Comparator<Hotel>
	{
		public int compare( Hotel c1, Hotel c2 )
		{
			return c1.getNombre().compareToIgnoreCase( c2.getNombre() );
		}
	}
}
