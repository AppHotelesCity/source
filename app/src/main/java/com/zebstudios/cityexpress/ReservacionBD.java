package com.zebstudios.cityexpress;

import java.util.Date;


/**
 * Created by DanyCarreto on 21/12/15.
 */
public class ReservacionBD {

    private int numReservacion;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String nombreHotel;
    private String siglasHotel;
    private String emailHotel;
    private String emailUsuario;
    private Date fechaLlegada;
    private Date fechaSalida;
    private String deschabitacion;
    private String descHotel;
    private String habCosto;
    private String subtotal;
    private String iva;
    private String total;
    private String codigoHabitacion;
    private String direccionHotel;
    private String descripcionLugarHotel;
    private int adultos;
    private int infantes;
    private int numHabitaciones;
    private int numNoches;
    private double longitudHotel;
    private double latitudHotel;
    private boolean checkIn;
    private boolean checkOut;
    private boolean consultarSaldos;
    private String numHabitacionAsigado;
    private boolean cityPremios;

    public ReservacionBD() {
    }

    public ReservacionBD(int numReservacion, String nombreUsuario, String apellidoUsuario, String nombreHotel, Date fechaLlegada, Date fechaSalida, String deschabitacion, String descHotel, String habCosto, String total, String direccionHotel, String descripcionLugarHotel, int adultos, int infantes, int numHabitaciones, int numNoches, double longitudHotel, double latitudHotel) {
        this.numReservacion = numReservacion;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.nombreHotel = nombreHotel;
        this.fechaLlegada = fechaLlegada;
        this.fechaSalida = fechaSalida;
        this.deschabitacion = deschabitacion;
        this.descHotel = descHotel;
        this.habCosto = habCosto;
        this.total = total;
        this.direccionHotel = direccionHotel;
        this.descripcionLugarHotel = descripcionLugarHotel;
        this.adultos = adultos;
        this.infantes = infantes;
        this.numHabitaciones = numHabitaciones;
        this.numNoches = numNoches;
        this.longitudHotel = longitudHotel;
        this.latitudHotel = latitudHotel;
    }

    public int getNumReservacion() {
        return numReservacion;
    }

    public void setNumReservacion(int numReservacion) {
        this.numReservacion = numReservacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getNombreHotel() {
        return nombreHotel;
    }

    public void setNombreHotel(String nombreHotel) {
        this.nombreHotel = nombreHotel;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getDeschabitacion() {
        return deschabitacion;
    }

    public void setDeschabitacion(String deschabitacion) {
        this.deschabitacion = deschabitacion;
    }

    public String getDescHotel() {
        return descHotel;
    }

    public void setDescHotel(String descHotel) {
        this.descHotel = descHotel;
    }

    public String getHabCosto() {
        return habCosto;
    }

    public void setHabCosto(String habCosto) {
        this.habCosto = habCosto;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDireccionHotel() {
        return direccionHotel;
    }

    public void setDireccionHotel(String direccionHotel) {
        this.direccionHotel = direccionHotel;
    }

    public String getDescripcionLugarHotel() {
        return descripcionLugarHotel;
    }

    public void setDescripcionLugarHotel(String descripcionLugarHotel) {
        this.descripcionLugarHotel = descripcionLugarHotel;
    }

    public int getAdultos() {
        return adultos;
    }

    public void setAdultos(int adultos) {
        this.adultos = adultos;
    }

    public int getInfantes() {
        return infantes;
    }

    public void setInfantes(int infantes) {
        this.infantes = infantes;
    }

    public int getNumHabitaciones() {
        return numHabitaciones;
    }

    public void setNumHabitaciones(int numHabitaciones) {
        this.numHabitaciones = numHabitaciones;
    }

    public int getNumNoches() {
        return numNoches;
    }

    public void setNumNoches(int numNoches) {
        this.numNoches = numNoches;
    }

    public double getLongitudHotel() {
        return longitudHotel;
    }

    public void setLongitudHotel(double longitudHotel) {
        this.longitudHotel = longitudHotel;
    }

    public double getLatitudHotel() {
        return latitudHotel;
    }

    public void setLatitudHotel(double latitudHotel) {
        this.latitudHotel = latitudHotel;
    }

    public String getSiglasHotel() {
        return siglasHotel;
    }

    public void setSiglasHotel(String siglasHotel) {
        this.siglasHotel = siglasHotel;
    }

    public String getCodigoHabitacion() {
        return codigoHabitacion;
    }

    public void setCodigoHabitacion(String codigoHabitacion) {
        this.codigoHabitacion = codigoHabitacion;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    public boolean isCheckOut() {
        return checkOut;
    }

    public void setCheckOut(boolean checkOut) {
        this.checkOut = checkOut;
    }

    public boolean isConsultarSaldos() {
        return consultarSaldos;
    }

    public void setConsultarSaldos(boolean consultarSaldos) {
        this.consultarSaldos = consultarSaldos;
    }

    public String getEmailHotel() {
        return emailHotel;
    }

    public void setEmailHotel(String emailHotel) {
        this.emailHotel = emailHotel;
    }

    public String getNumHabitacionAsigado() {
        return numHabitacionAsigado;
    }

    public void setNumHabitacionAsigado(String numHabitacionAsigado) {
        this.numHabitacionAsigado = numHabitacionAsigado;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public boolean isCityPremios() {
        return cityPremios;
    }

    public void setCityPremios(boolean cityPremios) {
        this.cityPremios = cityPremios;
    }
}
