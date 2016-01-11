package com.zebstudios.cityexpress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by edwinhernandez on 30/12/15.
 */
public class ReservacionBDD {

    private final String[] _reservanteColumns = { "numReservacion", "nombreUsuario", "apellidoUsuario", "nombreHotel", "siglasHotel", "emailHotel", "fechaLlegada", "fechaSalida", "deschabitacion", "descHotel", "habCosto", "total", "codigoHabitacion", "direccionHotel", "descripcionLugarHotel", "adultos", "infantes", "numHabitaciones", "numNoches", "longitudHotel", "latitudHotel", "checkIn", "checkOut", "consultarSaldos", "numHabitacionAsigado", "subtotal", "iva" };
    private CityExpressDBHelper _dbHelper;
    private SQLiteDatabase _database;

    public ReservacionBDD(Context context)
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
        _database.delete("Reservations", where, whereArgs);
    }

    public void insert( ReservacionBD reservante )
    {
        ContentValues data = new ContentValues();
        data.put( "numReservacion", reservante.getNumReservacion());
        data.put( "nombreUsuario", reservante.getNombreUsuario() );
        data.put( "apellidoUsuario", reservante.getApellidoUsuario() );
        data.put( "nombreHotel", reservante.getNombreHotel() );
        data.put( "siglasHotel", reservante.getSiglasHotel() );
        data.put( "emailHotel", reservante.getEmailHotel() );
        data.put( "fechaLlegada", reservante.getFechaLlegada().getTime() );
        data.put( "fechaSalida", reservante.getFechaSalida().getTime()) ;
        data.put( "deschabitacion", reservante.getDeschabitacion() );
        data.put( "descHotel", reservante.getDescHotel() );
        data.put( "habCosto", reservante.getHabCosto() );
        data.put( "total", reservante.getTotal() );
        data.put( "codigoHabitacion", reservante.getCodigoHabitacion() );
        data.put( "direccionHotel", reservante.getDireccionHotel() );
        data.put( "descripcionLugarHotel", reservante.getDescripcionLugarHotel() );
        data.put( "adultos", reservante.getAdultos() );
        data.put( "infantes", reservante.getInfantes() );
        data.put( "numHabitaciones", reservante.getNumHabitaciones() );
        data.put( "numNoches", reservante.getNumNoches() );
        data.put( "longitudHotel", reservante.getLongitudHotel() );
        data.put( "latitudHotel", reservante.getLatitudHotel() );
        data.put( "checkIn", reservante.isCheckIn() );
        data.put( "checkOut", reservante.isCheckOut() );
        data.put( "consultarSaldos", reservante.isConsultarSaldos() );
        data.put( "numHabitacionAsigado", reservante.getNumHabitacionAsigado() );
        data.put( "subtotal", reservante.getSubtotal() );
        data.put( "iva", reservante.getIva() );

        _database.insert("ClienteReservante", null, data);
        System.out.println("BDNUMERORESERVACION->"+reservante.getNumReservacion());
    }

    public void update( ReservacionBD reservante, String numReservacion)
    {
        ContentValues data = new ContentValues();
        data.put( "numReservacion", reservante.getNumReservacion() );
        data.put( "nombreUsuario", reservante.getNombreUsuario() );
        data.put( "apellidoUsuario", reservante.getApellidoUsuario() );
        data.put( "nombreHotel", reservante.getNombreHotel() );
        data.put( "siglasHotel", reservante.getSiglasHotel() );
        data.put( "emailHotel", reservante.getEmailHotel() );
        data.put( "fechaLlegada", reservante.getFechaLlegada() .getTime());
        data.put( "fechaSalida", reservante.getFechaSalida().getTime()) ;
        data.put( "deschabitacion", reservante.getDeschabitacion() );
        data.put( "descHotel", reservante.getDescHotel() );
        data.put( "habCosto", reservante.getHabCosto() );
        data.put( "total", reservante.getTotal() );
        data.put( "codigoHabitacion", reservante.getCodigoHabitacion() );
        data.put( "direccionHotel", reservante.getDireccionHotel() );
        data.put( "descripcionLugarHotel", reservante.getDescripcionLugarHotel() );
        data.put( "adultos", reservante.getAdultos() );
        data.put( "infantes", reservante.getInfantes() );
        data.put( "numHabitaciones", reservante.getNumHabitaciones() );
        data.put( "numNoches", reservante.getNumNoches() );
        data.put( "longitudHotel", reservante.getLongitudHotel() );
        data.put("latitudHotel", reservante.getLatitudHotel() );
        data.put( "checkIn", reservante.isCheckIn() );
        data.put( "checkOut", reservante.isCheckOut() );
        data.put( "consultarSaldos", reservante.isConsultarSaldos() );
        data.put( "numHabitacionAsigado", reservante.getNumHabitacionAsigado() );
        data.put( "subtotal", reservante.getSubtotal() );
        data.put( "iva", reservante.getIva() );

        String where = "numReservacion=?";
        String[] whereArgs = { "" + numReservacion };
        _database.update( "ClienteReservante", data, where, whereArgs );
    }

    public ReservacionBD getReservante(int numReservacion)
    {
        ReservacionBD reservante = null;

        Cursor cursor = _database.rawQuery("select * from ClienteReservante where numReservacion=" + numReservacion, null);
        cursor.moveToFirst();
        if( !cursor.isAfterLast() )
        {
            reservante = getReservanteFromCursor( cursor );
            System.out.println("BASESQLITE->"+reservante.getNombreHotel()+"HOLA"+reservante.getNombreUsuario());
        }
        cursor.close();

        return reservante;
    }

    // { "rvntId", "rvntNombre", "rvntApellido", "rvntCorreo", "rvntSocio", "rvntAfiliate", "rvntTelefono", "rvntViaje" };

    public ArrayList<ReservacionBD> getReservations()
    {
        ArrayList<ReservacionBD> results = new ArrayList<ReservacionBD>();

        Cursor cursor = _database.query( "ClienteReservante", _reservanteColumns, null, null, null, null, "numReservacion DESC" );
        cursor.moveToFirst();
        while( !cursor.isAfterLast() )
        {
            ReservacionBD reservation = getReservanteFromCursor(cursor);
            results.add( reservation );
            cursor.moveToNext();
        }
        cursor.close();


        return results;
    }


    private ReservacionBD getReservanteFromCursor( Cursor cursor )
    {
        ReservacionBD reservante = new ReservacionBD();

        reservante.setNumReservacion( cursor.getInt( 0 ) );
        reservante.setNombreUsuario( cursor.getString( 1 ) );
        reservante.setApellidoUsuario( cursor.getString( 2 ) );
        reservante.setNombreHotel( cursor.getString( 3 ) );
        reservante.setSiglasHotel( cursor.getString(4) );
        reservante.setEmailHotel( cursor.getString( 5 ) );
        reservante.setFechaLlegada(  new Date( cursor.getLong( 6 )));
        reservante.setFechaSalida(  new Date( cursor.getLong( 7 )));
        reservante.setDeschabitacion( cursor.getString( 8 ) );
        reservante.setDescHotel( cursor.getString( 9 ) );
        reservante.setHabCosto( cursor.getString(10 ) );
        reservante.setTotal( cursor.getString( 11 ) );
        reservante.setCodigoHabitacion( cursor.getString( 12 ) );
        reservante.setDireccionHotel( cursor.getString( 13 ) );
        reservante.setDescripcionLugarHotel( cursor.getString( 14 ) );
        reservante.setAdultos( cursor.getInt( 15 ) );
        reservante.setInfantes( cursor.getInt( 16 ) );
        reservante.setNumHabitaciones( cursor.getInt( 17 ) );
        reservante.setNumNoches( cursor.getInt( 18 ) );
        reservante.setLongitudHotel( cursor.getDouble( 19 ) );
        reservante.setLatitudHotel( cursor.getDouble( 20 ) );
        reservante.setCheckIn( cursor.getInt( 21 ) > 0 ? Boolean.TRUE : Boolean.FALSE);
        reservante.setCheckOut( cursor.getInt(22 ) > 0 ? Boolean.TRUE : Boolean.FALSE );
        reservante.setConsultarSaldos( cursor.getInt( 23 ) > 0 ? Boolean.TRUE : Boolean.FALSE );
        reservante.setNumHabitacionAsigado( cursor.getString( 24 ) );
        reservante.setSubtotal(cursor.getString(25));
        reservante.setIva(cursor.getString(26));


        return reservante;
    }

}
