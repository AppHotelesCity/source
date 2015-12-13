package com.zebstudios.cityexpress;

/**
 * Created by user on 11/12/15.
 */
public class HabitacionBase {
    String disponibilidad;
    String codigoBase;
    String descBase;
    String costo;
    String fecha;

    public HabitacionBase(String disponibilidad, String codigoBase, String descBase, String costo, String fecha) {
        this.disponibilidad = disponibilidad;
        this.codigoBase = codigoBase;
        this.descBase = descBase;
        this.costo = costo;
        this.fecha = fecha;
    }

    public HabitacionBase() {
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getCodigoBase() {
        return codigoBase;
    }

    public void setCodigoBase(String codigoBase) {
        this.codigoBase = codigoBase;
    }

    public String getDescBase() {
        return descBase;
    }

    public void setDescBase(String descBase) {
        this.descBase = descBase;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
