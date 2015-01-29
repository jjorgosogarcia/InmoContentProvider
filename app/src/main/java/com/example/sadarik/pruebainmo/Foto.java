package com.example.sadarik.pruebainmo;

import java.io.Serializable;

/**
 * Created by Sadarik on 26/01/2015.
 */

public class Foto implements Serializable, Comparable<Foto> {

    private long id;
    private long idInmueble;
    private String foto;

    public Foto() {
    }

    public Foto(long id, long idInmueble, String foto) {
        this.id = id;
        this.idInmueble = idInmueble;
        this.foto = foto;
    }

    public Foto(long idInmueble, String foto) {
        this.id = 0;
        this.idInmueble = idInmueble;
        this.foto = foto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Foto.class != o.getClass()) return false;

        Foto foto = (Foto) o;

        if (id != foto.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public int compareTo(Foto another) {
        return 0;
    }
}
