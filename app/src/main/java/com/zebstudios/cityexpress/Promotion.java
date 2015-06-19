package com.zebstudios.cityexpress;

import org.json.JSONObject;

/**
 * Created by Denumeris Interactive on 22/10/2014.
 */
public class Promotion
{
	private String _title;
	private String _imageURL;
	private boolean _parsedOk;

	public Promotion( JSONObject json )
	{
		try
		{
			_title = json.getString( "Titulo" );
			String temp  = json.getString( "Imagen" );
			if( temp.startsWith( "/" ) )
				_imageURL = APIAddress.HOTELS_WEB_BASE + temp;
			else
				_imageURL = temp;
			_parsedOk = true;
		}
		catch( Exception e )
		{
			_parsedOk = false;
			android.util.Log.e( "Hotel", "Cant parse promo: " + e.getMessage() );
		}
	}

	public String getTitle()
	{
		return _title;
	}

	public void setTitle( String title )
	{
		_title = title;
	}

	public String getImageURL()
	{
		return _imageURL;
	}

	public void setImageURL( String imageURL )
	{
		_imageURL = imageURL;
	}

	public boolean isParsedOk()
	{
		return _parsedOk;
	}

	public void setParsedOk( boolean parsedOk )
	{
		_parsedOk = parsedOk;
	}
}
