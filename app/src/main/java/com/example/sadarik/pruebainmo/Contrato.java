package com.example.sadarik.pruebainmo;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sadarik on 26/01/2015.
 */
public class Contrato {

    private Contrato (){
    }

    public static abstract class TablaInmueble implements BaseColumns {
        public static final String TABLA = "inmueble";
        public static final String LOCALIDAD ="localidad";
        public static final String DIRECCION = "direccion";
        public static final String TIPO="tipo";
        public static final String PRECIO = "precio";
        public static final String SUBIDO = "subido";
        public static final String CONTENT_TYPE_INMUEBLE =
                "vnd.android.cursor.item/vnd.inmueble";
        public static final String CONTENT_TYPE_INMUEBLES =
                "vnd.android.cursor.dir/vnd.inmuebles";
        public static final Uri CONTENT_URI = Uri.parse("content://" +
                ProveedorInmueble.AUTORIDAD + "/" + TABLA);
    }

    public static abstract class TablaFotos implements BaseColumns {
        public static final String TABLA = "fotos";
        public static final String IDIMUEBLE ="idinmueble";
        public static final String FOTO = "foto";
    }
}