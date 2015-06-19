package com.zebstudios.cityexpress;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Denumeris Interactive on 23/10/2014.
 */
public class WeatherReport
{
	private double _latitude;
	private double _longitude;
	private String _country;
	private long _sunrise;
	private long _sunset;
	private Weather[] _weathers;
	private double _temperature;
	private double _humidity;
	private double _pressure;
	private double _tempMin;
	private double _tempMax;
	private double _windSpeed;
	private double _windDegree;
	private double _rain3h;
	private double _clouds;
	private long _dateTime;
	private String _name;
	private int _code;
	private boolean _parsedOk;

	public WeatherReport( JSONObject json )
	{
		try
		{
			_parsedOk = false;

			JSONObject o = json.getJSONObject( "coord" );
			_latitude = o.getDouble( "lat" );
			_longitude = o.getDouble( "lon" );

			o = json.getJSONObject( "sys" );
			_country = o.getString( "country" );
			_sunrise = o.getLong( "sunrise" );
			_sunset = o.getLong( "sunset" );

			if( json.has( "weather" ) )
			{
				JSONArray a = json.getJSONArray( "weather" );
				_weathers = new Weather[a.length()];
				for( int i = 0; i < a.length(); i++ )
				{
					o = a.getJSONObject( i );
					Weather w = new Weather( o );
					if( w.isParsedOk() )
						_weathers[i] = w;
					else
						return;
				}
			}

			o = json.getJSONObject( "main" );
			_temperature = o.getDouble( "temp" );
			_humidity = o.getDouble( "humidity" );
			_pressure = o.getDouble( "pressure" );
			if( o.has( "temp_min" ) )
				_tempMin = o.getDouble( "temp_min" );
			if( o.has( "temp_max" ) )
				_tempMax = o.getDouble( "temp_max" );

			o = json.getJSONObject( "wind" );
			_windSpeed = o.getDouble( "speed" );
			_windDegree = o.getDouble( "deg" );

			if( json.has( "rain" ) )
			{
				o = json.getJSONObject( "rain" );
				if( o.has( "3h" ) )
					_rain3h = o.getDouble( "3h" );
			}

			o = json.getJSONObject( "clouds" );
			_clouds = o.getDouble( "all" );

			_dateTime = json.getLong( "dt" );
			_name = json.getString( "name" );
			_code = json.getInt( "cod" );

			_parsedOk = true;
		}
		catch( Exception e )
		{
			android.util.Log.e( "WEATHER REPORT", "Cant parse weather report: " + e.getMessage() );
		}
	}

	public double getLatitude()
	{
		return _latitude;
	}

	public double getLongitude()
	{
		return _longitude;
	}

	public String getCountry()
	{
		return _country;
	}

	public long getSunrise()
	{
		return _sunrise;
	}

	public long getSunset()
	{
		return _sunset;
	}

	public Weather[] getWeathers()
	{
		return _weathers;
	}

	public double getTemperature()
	{
		return _temperature;
	}

	public double getHumidity()
	{
		return _humidity;
	}

	public double getPressure()
	{
		return _pressure;
	}

	public double getTempMin()
	{
		return _tempMin;
	}

	public double getTempMax()
	{
		return _tempMax;
	}

	public double getWindSpeed()
	{
		return _windSpeed;
	}

	public double getWindDegree()
	{
		return _windDegree;
	}

	public double getRain3h()
	{
		return _rain3h;
	}

	public double getClouds()
	{
		return _clouds;
	}

	public long getDateTime()
	{
		return _dateTime;
	}

	public String getName()
	{
		return _name;
	}

	public int getCode()
	{
		return _code;
	}

	public boolean isParsedOk()
	{
		return _parsedOk;
	}

	public class Weather
	{
		private int _id;
		private String _main;
		private String _description;
		private String _icon;
		private boolean _parsedOk;

		public Weather( JSONObject json )
		{
			try
			{
				_parsedOk = false;

				_id = json.getInt( "id" );
				_main = json.getString( "main" );
				_description = json.getString( "description" );
				_icon = json.getString( "icon" );

				_parsedOk = true;
			}
			catch( Exception e )
			{
				android.util.Log.e( "WEATHER", "Cant parse weather: " + e.getMessage() );
			}
		}

		public int getId()
		{
			return _id;
		}

		public String getMain()
		{
			return _main;
		}

		public String getDescription()
		{
			return _description;
		}

		public String getIcon()
		{
			return _icon;
		}

		public boolean isParsedOk()
		{
			return _parsedOk;
		}
	}
}
