package com.example.sadarik.pruebainmo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sadarik on 26/01/2015.
 */
public class Adaptador extends CursorAdapter {


    public class ViewHolder{
        public TextView tvloc,tvdir,tvtipo,tvprecio, tvsubido;
    }

    public Adaptador(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.detalle, parent, false);
        return v;
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        ViewHolder vh = new ViewHolder();

        vh.tvloc=(TextView) convertView.findViewById(R.id.tvLocalidad);
        vh.tvdir=(TextView) convertView.findViewById(R.id.tvDireccion);
        vh.tvtipo=(TextView) convertView.findViewById(R.id.tvTipo);
        vh.tvprecio=(TextView) convertView.findViewById(R.id.tvPrecio);
        vh.tvsubido=(TextView) convertView.findViewById(R.id.tvSubido);

        Inmueble inm = ProveedorInmueble.getRow(cursor);

       if (inm.getSubido()==0){
            vh.tvsubido.setText("Subido: No");
        }else{
           vh.tvsubido.setText("Subido: Si");
        }
        vh.tvloc.setText(inm.getLocalidad());
        vh.tvdir.setText(inm.getDireccion());
        vh.tvtipo.setText(inm.getTipo());
        vh.tvprecio.setText(inm.getPrecio()+" €");
    }
}
