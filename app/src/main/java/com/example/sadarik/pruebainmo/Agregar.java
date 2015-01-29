package com.example.sadarik.pruebainmo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sadarik on 26/01/2015.
 */

public class Agregar extends Activity {

    private EditText etDirecc,etLoca,etTipo,etPrecio;
    private Button btAgregar;
    private Inmueble inm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_agregar);
        initComponents();
        Intent i = getIntent();
        if(i.getType().equals("editar")){
            inm=(Inmueble)i.getExtras().getSerializable("inmueble");
            etLoca.setText(inm.getLocalidad());
            etDirecc.setText(inm.getDireccion());
            etTipo.setText(inm.getTipo());
            etPrecio.setText(inm.getPrecio()+"");
        }else{
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void aniadir(View view){
        Intent i=getIntent();
        String localidad,direccion,tipo,precio;
        localidad = etLoca.getText().toString();
        direccion = etDirecc.getText().toString();
        tipo = etTipo.getText().toString();
        precio = etPrecio.getText().toString();
        if(i.getType().equals("agregar")){
            Inmueble inm = new Inmueble(localidad,direccion,tipo,Integer.valueOf(precio),0);
            i.putExtra("inmueble",inm);
        }
        if(i.getType().equals("editar")){
            inm.setLocalidad(localidad);
            inm.setDireccion(direccion);
            inm.setTipo(tipo);
            inm.setPrecio(Integer.valueOf(precio));
            i.putExtra("inmueble",inm);
        }
        setResult(RESULT_OK,i);
        finish();
    }

public void initComponents(){
    btAgregar = (Button)findViewById(R.id.btAgregar);
    etLoca = (EditText) findViewById(R.id.etLocalidad);
    etDirecc = (EditText) findViewById(R.id.etDireccion);
    etTipo = (EditText) findViewById(R.id.etTipo);
    etPrecio = (EditText) findViewById(R.id.etPrecio);
}
}
