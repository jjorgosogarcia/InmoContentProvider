package com.example.sadarik.pruebainmo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

/**
 * Created by Sadarik on 26/01/2015.
 */
public class AdaptadorFotos extends CursorAdapter {

    public class ViewHolder{
        public ImageView ivfotos;
    }

    public AdaptadorFotos(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.detalle_fotos, parent, false);
        return v;
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        ViewHolder vh = new ViewHolder();
        vh.ivfotos =(ImageView) convertView.findViewById(R.id.ivFoto);
        Foto foto = GestorFoto.getRow(cursor);
        vh.ivfotos.setTag(cursor.getString(2));
        vh.ivfotos.setImageBitmap(BitmapFactory.decodeFile(foto.getFoto()));
        convertView.setTag(vh);
    }
}