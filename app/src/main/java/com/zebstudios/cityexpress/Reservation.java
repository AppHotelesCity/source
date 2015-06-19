package com.zebstudios.cityexpress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Denumeris Interactive on 05/11/2014.
 */
public class Reservation implements Serializable
{
	private static final long serialVersionUID = 0L;

	private long _id;
	private String _hotelName;
	private String _hotelAddress;
	private String _hotelNearest;
	private Double _hotelLatitude;
	private Double _hotelLongitude;
	private Date _arrivalDate;
	private Date _departureDate;
	private ArrayList<Room> _rooms;
	private String _hotelEmail;
	private String _hotelPhone;
	private String _moneda;

	public Reservation()
	{
		_rooms = new ArrayList<Room>();
	}

	public long getId()
	{
		return _id;
	}

	public void setId( long id )
	{
		_id = id;
	}

	public String getHotelName()
	{
		return _hotelName;
	}

	public void setHotelName( String hotelName )
	{
		_hotelName = hotelName;
	}

	public String getHotelAddress()
	{
		return _hotelAddress;
	}

	public void setHotelAddress( String hotelAddress )
	{
		_hotelAddress = hotelAddress;
	}

	public String getHotelNearest()
	{
		return _hotelNearest;
	}

	public void setHotelNearest( String hotelNearest )
	{
		_hotelNearest = hotelNearest;
	}

	public Double getHotelLatitude()
	{
		return _hotelLatitude;
	}

	public void setHotelLatitude( Double hotelLatitude )
	{
		_hotelLatitude = hotelLatitude;
	}

	public Double getHotelLongitude()
	{
		return _hotelLongitude;
	}

	public void setHotelLongitude( Double hotelLongitude )
	{
		_hotelLongitude = hotelLongitude;
	}

	public Date getArrivalDate()
	{
		return _arrivalDate;
	}

	public void setArrivalDate( Date arrivalDate )
	{
		_arrivalDate = arrivalDate;
	}

	public Date getDepartureDate()
	{
		return _departureDate;
	}

	public void setDepartureDate( Date departureDate )
	{
		_departureDate = departureDate;
	}

	public ArrayList<Room> getRooms()
	{
		return _rooms;
	}

	public void setRooms( ArrayList<Room> rooms )
	{
		_rooms = rooms;
	}

	public String getHotelEmail()
	{
		return _hotelEmail;
	}

	public void setHotelEmail( String hotelEmail )
	{
		_hotelEmail = hotelEmail;
	}

	public String getHotelPhone()
	{
		return _hotelPhone;
	}

	public void setHotelPhone( String hotelPhone )
	{
		_hotelPhone = hotelPhone;
	}

	public String getMoneda()
	{
		return _moneda;
	}

	public void setMoneda( String moneda )
	{
		_moneda = moneda;
	}

	public static class Room implements Serializable
	{
		private static final long serialVersionUID = 0L;

		private long _id;
		private String _reservationNumber;
		private String _reservationTitular;
		private ArrayList<Double> _nights;
		private String _habDescription;
		private int _adultosExtra;

		public Room()
		{
			_nights = new ArrayList<Double>();
		}

		public long getId()
		{
			return _id;
		}

		public void setId( long id )
		{
			_id = id;
		}

		public String getReservationNumber()
		{
			return _reservationNumber;
		}

		public void setReservationNumber( String reservationNumber )
		{
			_reservationNumber = reservationNumber;
		}

		public String getReservationTitular()
		{
			return _reservationTitular;
		}

		public void setReservationTitular( String reservationTitular )
		{
			_reservationTitular = reservationTitular;
		}

		public ArrayList<Double> getNights()
		{
			return _nights;
		}

		public void setNights( ArrayList<Double> nights )
		{
			_nights = nights;
		}

		public String getHabDescription()
		{
			return _habDescription;
		}

		public void setHabDescription( String habDescription )
		{
			_habDescription = habDescription;
		}

		public int getAdultosExtra()
		{
			return _adultosExtra;
		}

		public void setAdultosExtra( int adultosExtra )
		{
			_adultosExtra = adultosExtra;
		}
	}
}
