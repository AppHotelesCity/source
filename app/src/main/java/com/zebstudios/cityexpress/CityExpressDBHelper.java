package com.zebstudios.cityexpress;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Denumeris Interactive on 05/11/2014.
 */
public class CityExpressDBHelper extends SQLiteOpenHelper
{
	private final static int DB_VERSION = 6;
	private final static String DB_NAME = "city_express.s3db";

	public CityExpressDBHelper( Context context )
	{
		super( context, DB_NAME, null, DB_VERSION );
	}

	@Override
	public void onCreate( SQLiteDatabase db )
	{
		// Version 1
		db.execSQL( "CREATE TABLE Reservations ( resId INTEGER PRIMARY KEY AUTOINCREMENT, resHotelName TEXT, resHotelAddress TEXT, resHotelNearest TEXT, resHotelLatitude REAL, resHotelLongitude REAL, resArrivalDate INTEGER, resDepartureDate INTEGER, resMonedaNacional BOOLEAN, resHotelEmail TEXT, resHotelPhone TEXT, resMoneda TEXT )" );
		db.execSQL( "CREATE TABLE ReservationsRooms ( resrId INTEGER PRIMARY KEY AUTOINCREMENT, resrResId INTEGER, resrReservationNumber TEXT, resrTitular TEXT, resrHabDesc TEXT, resrAdultosExtra INTEGER )" );
		db.execSQL( "CREATE TABLE ReservationsNights ( resnResrId INTEGER, resnCost REAL )" );
		// Version 2
		db.execSQL( "CREATE TABLE Reservante ( rvntId INTEGER PRIMARY KEY, rvntNombre TEXT, rvntApellido TEXT, rvntCorreo TEXT, rvntSocio TEXT, rvntAfiliate BOOLEAN, rvntTelefono TEXT, rvntViaje INTEGER )" );
		db.execSQL( "CREATE TABLE ClienteReservante (numReservacion INTEGER PRIMARY KEY, nombreUsuario TEXT, apellidoUsuario TEXT, nombreHotel TEXT, siglasHotel TEXT, emailHotel TEXT, fechaLlegada TEXT, fechaSalida TEXT, deschabitacion TEXT, descHotel TEXT, habCosto TEXT, total TEXT, codigoHabitacion TEXT, direccionHotel TEXT, descripcionLugarHotel TEXT, adultos INTEGER, infantes INTEGER, numHabitaciones INTEGER, numNoches INTEGER, longitudHotel REAL, latitudHotel REAL, checkIn BOOLEAN, checkOut BOOLEAN, consultarSaldos BOOLEAN, numHabitacionAsigado BOOLEAN, subtotal TEXT, iva TEXT, cityPremios BOOLEAN)");
		// Version 3
		// ADD COLUMN resHotelEmail
		// ADD COLUMN resHotelPhone
		// Version 4
		// ADD COLUMN resMoneda
		// Version 5
		// ADD COLUMN resrHabDesc
		// ADD COLUMN resrAdultosExtra
		// Version 6
		db.execSQL( "CREATE TABLE PremiosUserLogged ( pulId INTEGER PRIMARY KEY, pulSocio TEXT, pulLogged REAL )" );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
	{
		android.util.Log.d( "DB Helper", "Upgrading BD" );
		if( oldVersion == 1 )
		{
			db.execSQL( "CREATE TABLE Reservante ( rvntId INTEGER PRIMARY KEY, rvntNombre TEXT, rvntApellido TEXT, rvntCorreo TEXT, rvntSocio TEXT, rvntAfiliate BOOLEAN, rvntTelefono TEXT, rvntViaje INTEGER )" );
		}
		if( oldVersion <= 2 )
		{
			db.execSQL( "ALTER TABLE Reservations ADD COLUMN resHotelEmail TEXT" );
			db.execSQL( "ALTER TABLE Reservations ADD COLUMN resHotelPhone TEXT" );
		}
		if( oldVersion <= 3 )
		{
			db.execSQL( "ALTER TABLE Reservations ADD COLUMN resMoneda TEXT" );
		}
		if( oldVersion <= 4 )
		{
			db.execSQL( "ALTER TABLE ReservationsRooms ADD COLUMN resrHabDesc TEXT" );
			db.execSQL( "ALTER TABLE ReservationsRooms ADD COLUMN resrAdultosExtra INTEGER" );
		}
		if( oldVersion <= 5 )
		{
			db.execSQL( "CREATE TABLE PremiosUserLogged ( pulId INTEGER PRIMARY KEY, pulSocio TEXT, pulLogged REAL )" );
		}
	}
}
