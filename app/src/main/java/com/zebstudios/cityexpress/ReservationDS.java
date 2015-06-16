package com.zebstudios.cityexpress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rczuart on 05/11/2014.
 */
public class ReservationDS
{
	private final String[] _reservationColumns = { "resId", "resHotelName", "resHotelAddress", "resHotelNearest", "resHotelLatitude", "resHotelLongitude", "resArrivalDate", "resDepartureDate", "resMonedaNacional", "resHotelEmail", "resHotelPhone", "resMoneda" };
	private final String[] _roomColumns = { "resrId", "resrResId", "resrReservationNumber", "resrTitular", "resrHabDesc", "resrAdultosExtra" };
	private CityExpressDBHelper _dbHelper;
	private SQLiteDatabase _database;

	public ReservationDS( Context context )
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

	public void delete( Reservation reservation )
	{
		String where;
		String[] whereArgs;

		for( int i=0; i<reservation.getRooms().size(); i++ )
		{
			where = "resnResrId=?";
			whereArgs = new String[] { "" + reservation.getRooms().get( i ).getId() };
			_database.delete( "ReservationsNights", where, whereArgs );

		}

		where = "resrResId=?";
		whereArgs = new String[] { "" + reservation.getId() };
		_database.delete( "ReservationsRooms", where, whereArgs );

		where = "resId=?";
		whereArgs = new String[] { "" + reservation.getId() };
		_database.delete( "Reservations", where, whereArgs );
	}

	public long insert( Reservation reservation )
	{
		ContentValues dataReservation = new ContentValues();
		dataReservation.put( "resHotelName", reservation.getHotelName() );
		dataReservation.put( "resHotelAddress", reservation.getHotelAddress() );
		dataReservation.put( "resHotelNearest", reservation.getHotelNearest() );
		dataReservation.put( "resHotelLatitude", reservation.getHotelLatitude() );
		dataReservation.put( "resHotelLongitude", reservation.getHotelLongitude() );
		dataReservation.put( "resArrivalDate", reservation.getArrivalDate().getTime() );
		dataReservation.put( "resDepartureDate", reservation.getDepartureDate().getTime() );
		dataReservation.put( "resMonedaNacional", true );
		dataReservation.put( "resHotelEmail", reservation.getHotelEmail() );
		dataReservation.put( "resHotelPhone", reservation.getHotelPhone() );
		dataReservation.put( "resMoneda", reservation.getMoneda() );
		long idReservation = _database.insert( "Reservations", null, dataReservation );

		for( int i = 0; i < reservation.getRooms().size(); i++ )
		{
			ContentValues dataRoom = new ContentValues();
			dataRoom.put( "resrResId", idReservation );
			dataRoom.put( "resrReservationNumber", reservation.getRooms().get( i ).getReservationNumber() );
			dataRoom.put( "resrTitular", reservation.getRooms().get( i ).getReservationTitular() );
			dataRoom.put( "resrHabDesc", reservation.getRooms().get( i ).getHabDescription() );
			dataRoom.put( "resrAdultosExtra", reservation.getRooms().get( i ).getAdultosExtra() );
			long idRoom = _database.insert( "ReservationsRooms", null, dataRoom );

			for( int j = 0; j < reservation.getRooms().get( i ).getNights().size(); j++ )
			{
				ContentValues dataNight = new ContentValues();
				dataNight.put( "resnResrId", idRoom );
				dataNight.put( "resnCost", reservation.getRooms().get( i ).getNights().get( j ) );
				_database.insert( "ReservationsNights", null, dataNight );
			}
		}

		return idReservation;
	}

	public ArrayList<Reservation> getReservations()
	{
		ArrayList<Reservation> results = new ArrayList<Reservation>();

		Cursor cursor = _database.query( "Reservations", _reservationColumns, null, null, null, null, "resArrivalDate DESC" );
		cursor.moveToFirst();
		while( !cursor.isAfterLast() )
		{
			Reservation reservation = getReservation( cursor );
			results.add( reservation );
			cursor.moveToNext();
		}
		cursor.close();

		android.util.Log.d( "TEST", "TOTAL: " + results.size() );
		for( int i = 0; i < results.size(); i++ )
		{
			String[] whereArgs = { "" + results.get( i ).getId() };
			cursor = _database.query( "ReservationsRooms", _roomColumns, "resrResId=?", whereArgs, null, null, null );
			cursor.moveToFirst();
			while( !cursor.isAfterLast() )
			{
				android.util.Log.d( "TEST", "ROOM" );
				Reservation.Room room = getRoom( cursor );
				results.get( i ).getRooms().add( room );
				cursor.moveToNext();
			}
			cursor.close();
		}

		String[] nightsColumns = { "resnCost" };
		for( int i = 0; i < results.size(); i++ )
		{
			for( int j = 0; j < results.get( i ).getRooms().size(); j++ )
			{
				String[] whereArgs = { "" + results.get( i ).getRooms().get( j ).getId() };
				cursor = _database.query( "ReservationsNights", nightsColumns, "resnResrId=?", whereArgs, null, null, null );
				cursor.moveToFirst();
				while( !cursor.isAfterLast() )
				{
					results.get( i ).getRooms().get( j ).getNights().add( cursor.getDouble( 0 ) );
					cursor.moveToNext();
				}
				cursor.close();
			}
		}

		return results;
	}

	//  { "resId", "resHotelName", "resHotelAddress", "resHotelNearest", "resHotelLatitude", "resHotelLongitude", "resArrivalDate", "resDepartureDate", "resMonedaNacional", "resHotelEmail", "resHotelPhone", "resMoneda" };
	private Reservation getReservation( Cursor cursor )
	{
		Reservation reservation = new Reservation();

		reservation.setId( cursor.getLong( 0 ) );
		reservation.setHotelName( cursor.getString( 1 ) );
		reservation.setHotelAddress( cursor.getString( 2 ) );
		reservation.setHotelNearest( cursor.getString( 3 ) );
		reservation.setHotelLatitude( cursor.getDouble( 4 ) );
		reservation.setHotelLongitude( cursor.getDouble( 5 ) );
		reservation.setArrivalDate( new Date( cursor.getLong( 6 ) ) );
		reservation.setDepartureDate( new Date( cursor.getLong( 7 ) ) );
		//reservation.setMonedaNacional( cursor.getInt( 8 ) > 0 ? Boolean.TRUE : Boolean.FALSE ); <-- ya no se usa
		if( !cursor.isNull( 9 ) )
		{
			reservation.setHotelEmail( cursor.getString( 9 ) );
		}
		if( !cursor.isNull( 10 ) )
		{
			reservation.setHotelPhone( cursor.getString( 10 ) );
		}
		if( !cursor.isNull( 11 ) )
		{
			reservation.setMoneda( cursor.getString( 11 ) );
		}
		else
		{
			if( cursor.getInt( 8 ) > 0 )
				reservation.setMoneda( "MXN" );
			else
				reservation.setMoneda( "USD" );
		}

		return reservation;
	}

	//{ "resrId", "resrResId", "resrReservationNumber", "resrTitular", "resrHabDesc", "resrAdultosExtra" };
	private Reservation.Room getRoom( Cursor cursor )
	{
		Reservation.Room room = new Reservation.Room();

		room.setId( cursor.getLong( 0 ) );
		room.setReservationNumber( cursor.getString( 2 ) );
		room.setReservationTitular( cursor.getString( 3 ) );
		if( !cursor.isNull( 4 ) )
		{
			room.setHabDescription( cursor.getString( 4 ) );
		}
		if( !cursor.isNull( 5 ) )
		{
			room.setAdultosExtra( cursor.getInt( 5 ) );
		}

		return room;
	}
}
