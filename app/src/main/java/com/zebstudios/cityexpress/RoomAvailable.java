package com.zebstudios.cityexpress;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by rczuart on 28/10/2014.
 */
public class RoomAvailable implements Serializable
{
	private static final long serialVersionUID = 0L;

	private String _code;
	private String _title;
	private String _description;
	private String _imagen;
	private double _tarifaProm;
	private double _total;
	private int _maxAdultos;
	private ArrayList<Double> _nightsCost;
	private String _tarifaCode;
	private String _promoCode;
	private String _moneda;

	public RoomAvailable clone()
	{
		RoomAvailable r = new RoomAvailable();

		r.setCode( this.getCode() );
		r.setTitle( this.getTitle() );
		r.setDescription( this.getDescription() );
		r.setImagen( this.getImagen() );
		r.setTarifaProm( this.getTarifaProm() );
		r.setTotal( this.getTotal() );
		r.setMaxAdultos( this.getMaxAdultos() );
		for( int i=0; i<this.getNightsCost().size(); i++ )
			r.getNightsCost().add( this.getNightsCost().get( i ) );
		r.setTarifaCode( this.getTarifaCode() );
		r.setPromoCode( this.getPromoCode() );
		r.setMoneda( this.getMoneda() );

		return r;
	}

	public RoomAvailable()
	{
		_nightsCost = new ArrayList<Double>();
	}

	public RoomAvailable( ReservationEngineClient.HabBase habit, String country )
	{
		double total = 0;

		_nightsCost = new ArrayList<Double>();
		try
		{
			for( int k = 0; k < habit.getNoches().size(); k++ )
			{
				double d = NumberFormat.getNumberInstance( java.util.Locale.US ).parse( habit.getNoches().get( k ).getCosto() ).doubleValue();
				total += d;
				_nightsCost.add( d );
			}
		}
		catch( Exception e )
		{
			total = 0;
			android.util.Log.d( "RoomAvailable", "Cant parse cost: " +e.getMessage() );
		}

		this.setCode( habit.getCodBase() );
		this.setDescription( habit.getDescBase() );
		this.setImagen( "" );
		this.setTarifaProm( total / habit.getNoches().size() );
		this.setTotal( total );
		this.setMaxAdultos( 0 );
		if( country.equalsIgnoreCase( "MX" ) )
		{
			this.setMoneda( "MXN" );
		}
		else if( country.equalsIgnoreCase( "CO" ) )
		{
			this.setMoneda( "COP" );
		}
		else
		{
			this.setMoneda( "USD" );
		}
	}

	public RoomAvailable( ReservationEngineClient.HabPromo habit, String promoCode, String country )
	{
		double total = 0;
		int totalNoches = 1;
		_nightsCost = new ArrayList<Double>();
		try
		{
			for( int i = 0; i<habit.getPromotions().size(); i++ )
			{
				ReservationEngineClient.Promotion p = habit.getPromotions().get( i );

				if( p.getCodPromo().equalsIgnoreCase( promoCode ) )
				{
					for( int j=0; j<p.getNoches().size(); j++ )
					{
						double d = NumberFormat.getNumberInstance( java.util.Locale.US ).parse( p.getNoches().get( j ).getCosto() ).doubleValue();
						total += d;
						_nightsCost.add( d );
					}
					totalNoches = p.getNoches().size();
					break;
				}
			}
		}
		catch( Exception e )
		{
			total = 0;
			android.util.Log.d( "RoomAvailable", "Cant parse cost: " +e.getMessage() );
		}

		this.setCode( habit.getCodRoom() );
		this.setDescription( habit.getDescRoom() );
		this.setImagen( "" );
		this.setTarifaProm( total / totalNoches );
		this.setTotal( total );
		this.setMaxAdultos( 0 );
		if( country.equalsIgnoreCase( "MX" ) )
		{
			this.setMoneda( "MXN" );
		}
		else if( country.equalsIgnoreCase( "CO" ) )
		{
			this.setMoneda( "COP" );
		}
		else
		{
			this.setMoneda( "USD" );
		}
	}

	public int getMaxAdultosFromTable()
	{
		if( this.getCode().equalsIgnoreCase( "HCP" ) )
			return 2;
		else if( this.getCode().equalsIgnoreCase( "NSD" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "NSQ" ) )
			return 2;
		else if( this.getCode().equalsIgnoreCase( "SUN" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "SDN" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "SKN" ) )
			return 2;
		else if( this.getCode().equalsIgnoreCase( "SKP" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "JKN" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "MDA" ) )
			return 6;
		else if( this.getCode().equalsIgnoreCase( "NDS" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "NSK" ) )
			return 2;
		else if( this.getCode().equalsIgnoreCase( "MKN" ) )
			return 6;
		else if( this.getCode().equalsIgnoreCase( "JSN" ) )
			return 6;
		else if( this.getCode().equalsIgnoreCase( "MSN" ) )
			return 8;
		else if( this.getCode().equalsIgnoreCase( "MSH" ) )
			return 8;
		else if( this.getCode().equalsIgnoreCase( "SQN" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "NST" ) )
			return 2;
		else if( this.getCode().equalsIgnoreCase( "NTT" ) )
			return 3;
		else if( this.getCode().equalsIgnoreCase( "SUT" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "STD" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "STE" ) )
			return 4;
		else if( this.getCode().equalsIgnoreCase( "STK" ) )
			return 2;
		else if( this.getCode().equalsIgnoreCase( "SDS" ) )
			return 6;
		else
			return 0;
	}

	public String getCode()
	{
		return _code;
	}

	public void setCode( String code )
	{
		_code = code;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription( String description )
	{
		_description = description;
	}

	public String getImagen()
	{
		return _imagen;
	}

	public void setImagen( String imagen )
	{
		_imagen = imagen;
	}

	public double getTarifaProm()
	{
		return _tarifaProm;
	}

	public void setTarifaProm( double tarifaProm )
	{
		_tarifaProm = tarifaProm;
	}

	public double getTotal()
	{
		return _total;
	}

	public void setTotal( double total )
	{
		_total = total;
	}

	public int getMaxAdultos()
	{
		return _maxAdultos;
	}

	public void setMaxAdultos( int maxAdultos )
	{
		_maxAdultos = maxAdultos;
	}

	public String getTitle()
	{
		return _title;
	}

	public void setTitle( String title )
	{
		_title = title;
	}

	public ArrayList<Double> getNightsCost()
	{
		return _nightsCost;
	}

	public void setNightsCost( ArrayList<Double> nightsCost )
	{
		_nightsCost = nightsCost;
	}

	public String getTarifaCode()
	{
		return _tarifaCode;
	}

	public void setTarifaCode( String tarifaCode )
	{
		_tarifaCode = tarifaCode;
	}

	public String getPromoCode()
	{
		return _promoCode;
	}

	public void setPromoCode( String promoCode )
	{
		_promoCode = promoCode;
	}

	public String getMoneda()
	{
		return _moneda;
	}

	public void setMoneda( String moneda )
	{
		_moneda = moneda;
	}
}
