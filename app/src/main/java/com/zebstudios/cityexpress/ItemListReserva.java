package com.zebstudios.cityexpress;

/**
 * Created by edwinhernandez on 17/12/15.
 */
public class ItemListReserva {

    String Habitacion, Precio;

    public ItemListReserva(String habitacion, String precio){
        Habitacion = habitacion;
        Precio = precio;
    }

    public String getHabitacion(){
        return Habitacion;
    }

    public void setHabitacion(String habitacion){
        Habitacion = habitacion;
    }

    public String getPrecio(){
        return Precio;
    }

    public void setPrecio(String precio){
        Precio = precio;
    }
}
