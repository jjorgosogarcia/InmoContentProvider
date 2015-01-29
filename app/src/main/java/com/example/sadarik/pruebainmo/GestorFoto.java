package com.example.sadarik.pruebainmo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sadarik on 26/01/2015.
 */

public class GestorFoto {

    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorFoto(Context c) {
        abd = new Ayudante(c);
    }

    public void open() {
        bd = abd.getWritableDatabase();
    }

    public void openRead() {
        bd = abd.getReadableDatabase();
    }

    public void close() {
        abd.close();
    }

    public long insert(Foto objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaFotos.IDIMUEBLE, objeto.getIdInmueble());
        valores.put(Contrato.TablaFotos.FOTO,objeto.getFoto());
        long id = bd.insert(Contrato.TablaFotos.TABLA, null, valores);
        return id;
    }

    public int delete(Foto objeto) {
        String condicion = Contrato.TablaFotos._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.delete(Contrato.TablaFotos.TABLA, condicion , argumentos);
        return cuenta;
    }

    public int update(Foto objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaFotos.IDIMUEBLE, objeto.getIdInmueble());
        valores.put(Contrato.TablaFotos.FOTO, objeto.getFoto());
        String condicion = Contrato.TablaFotos._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.update(Contrato.TablaFotos.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Foto> select () {
        return select(null,null,null);
    }

    public List <Foto> select (String condicion, String [] parametros, String orden) {
        List <Foto> alj = new ArrayList<Foto>();
        Cursor cursor = bd.query(Contrato.TablaFotos.TABLA, null, condicion, parametros, null, null, orden);
        cursor.moveToFirst();
        Foto foto;
        while (!cursor.isAfterLast()) {
            foto = getRow(cursor);
            alj.add(foto);
            cursor.moveToNext();
        }
        cursor.close();
        return alj;
    }

    public static Foto getRow(Cursor c) {
        Foto foto = new Foto();
        foto.setId(c.getLong(0));
        foto.setIdInmueble(c.getLong(1));
        foto.setFoto(c.getString(2));
        return foto;
    }

    public Cursor getCursor(String condicion,String[] parametros,String orden,long posicion) {
        condicion = Contrato.TablaFotos.IDIMUEBLE + " = ?";
        parametros = new String[]{posicion + ""};
        Cursor cursor = bd.query(Contrato.TablaFotos.TABLA, null, condicion, parametros, null, null, orden);
        return cursor;
    }

    public Cursor getCursor() {
        return getCursor(null,null,null,0);
    }
}
