package com.zebstudios.cityexpress;

/**
 * Created by DanyCarreto on 06/12/15.
 */
public class Pais {
    int id;
    String code;
    String descripcion;

    public Pais(int id, String code, String descripcion) {
        this.id = id;
        this.code = code;
        this.descripcion = descripcion;
    }

    public Pais(String code, String descripcion) {
        this.code = code;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
