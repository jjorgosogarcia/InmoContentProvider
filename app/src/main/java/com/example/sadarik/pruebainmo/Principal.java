package com.example.sadarik.pruebainmo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Sadarik on 26/01/2015.
 */

public class Principal extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Adaptador ad;
    private final int ACTIVIDADAGREGAR=1;
    private final int ACTIVIDADEDITAR=2;
    private final int ACTIVIDADFRAGMENTODOS=3;

    private boolean horizontal;
    private ListView lv;
    private FragmentoDos fdos;
    private AlertDialog alerta;
    private Cursor cursor;


    /*****************************************************/
    /*                 metodos on                        */
    /*****************************************************/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        int idInmueble = cursor.getInt(0);
        if (id == R.id.action_borrar) {
            Cursor cursor = (Cursor) lv.getItemAtPosition(index);
            int borrar = cursor.getInt(0);
            Uri uri = Contrato.TablaInmueble.CONTENT_URI;
            String where = Contrato.TablaInmueble._ID +" = ? ";
            String args[] = new String[]{borrar+""};
            getContentResolver().delete(uri,where,args);
        } else {
            if (id == R.id.action_editar) {
                Intent i = new Intent(Principal.this,Agregar.class);
                i.setType("editar");
                Cursor cursor = (Cursor) lv.getItemAtPosition(index);
                Inmueble inm = ProveedorInmueble.getRow(cursor);
                i.putExtra("inmueble",inm);
                startActivityForResult(i,ACTIVIDADEDITAR);
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        getLoaderManager().initLoader(0, null, this);
        this.setTitle(getString(R.string.app_name) + " " + loadSharedPreferences());
        cargarCursor();
        fdos = (FragmentoDos) getFragmentManager().findFragmentById(R.id.fragmento2land);
        horizontal = fdos!=null && fdos.isInLayout();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.longmenu, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_aniadir) {
            Intent i = new Intent(Principal.this,Agregar.class);
            i.setType("agregar");
            startActivityForResult(i,ACTIVIDADAGREGAR);
            return true;
        }else
            if(id == R.id.action_usuario ){
                return entrarUsuario();
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Inmueble inm;
        long id;
        ContentValues valores;
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVIDADAGREGAR:
                    inm=(Inmueble)data.getExtras().getSerializable("inmueble");
                    //Añadida
                    valores = conseguirValores(inm);
                    getContentResolver().insert(uri, valores);
                    Toast.makeText(this, R.string.insertado, Toast.LENGTH_LONG).show();
                    break;
                case ACTIVIDADEDITAR:
                    inm=(Inmueble)data.getExtras().getSerializable("inmueble");
                    //Modificada
                    valores = conseguirValores(inm);
                    String where = Contrato.TablaInmueble._ID + " = ?";
                    String [] args = new String[]{inm.getId()+""};
                    getContentResolver().update(uri, valores, where, args);
                    Toast.makeText(this, R.string.editado, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    /*****************************************************/
    /*                     auxiliares                    */
    /*****************************************************/

    public void initComponents(){
        this.setTitle(getString(R.string.app_name) + " " + loadSharedPreferences());
        ad = new Adaptador(this,cursor);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(horizontal){
                    fdos.rellenarListView(id);
                }else{
                    Intent intent = new Intent(Principal.this,Secundaria.class);
                    intent.putExtra("id",id);
                    startActivityForResult(intent, ACTIVIDADFRAGMENTODOS);
                }
            }
        });
        registerForContextMenu(lv);
    }
    private ContentValues conseguirValores(Inmueble inm){
        ContentValues valores = new ContentValues();
        String localidad = inm.getLocalidad();
        String tipo = inm.getTipo();
        int precio = inm.getPrecio();
        String direccion = inm.getDireccion();
        //   int subido = inm.getSubido();

        valores.put(Contrato.TablaInmueble.LOCALIDAD, localidad);
        valores.put(Contrato.TablaInmueble.TIPO, tipo);
        valores.put(Contrato.TablaInmueble.PRECIO, precio);
        valores.put(Contrato.TablaInmueble.DIRECCION, direccion);
        //    valores.put(Contrato.TablaInmueble.SUBIDO, subido);
        return valores;
    }

    private void saveSharedPreferences(String valor) {
        SharedPreferences pc;
        SharedPreferences.Editor spe;
        pc = getSharedPreferences("preferencias", MODE_PRIVATE);
        spe = pc.edit();
        spe.putString("usuario", valor);
        spe.apply();
    }

    private String loadSharedPreferences() {
        SharedPreferences pc;
        pc = getSharedPreferences("preferencias", MODE_PRIVATE);
        return pc.getString("usuario", "");
    }

    private boolean entrarUsuario(){
        String usuario = loadSharedPreferences();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.addusuario));
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_usuario, null);
        alert.setView(vista);

        final EditText etUsuario = (EditText)vista.findViewById(R.id.etUsuario);
        etUsuario.setText(usuario);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                if(!etUsuario.getText().toString().equals("")){
                    saveSharedPreferences(etUsuario.getText().toString());
                    Principal.this.setTitle(getString(R.string.app_name) + etUsuario.getText().toString());
                }
            }
        });
        alerta = alert.create();
        alerta.show();
        return true;
    }

    private void cargarCursor(){
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        String[] projection = new String[]{
                Contrato.TablaInmueble._ID,
                Contrato.TablaInmueble.LOCALIDAD,
                Contrato.TablaInmueble.DIRECCION,
                Contrato.TablaInmueble.TIPO,
                Contrato.TablaInmueble.PRECIO,
              //  Contrato.TablaInmueble.SUBIDO,
               };
        cursor = getContentResolver().query(uri, projection, null, null, null);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        return new CursorLoader(
                this, uri, null, null, null,
                Contrato.TablaInmueble._ID +" collate localized asc");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ad.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ad.swapCursor(data);
    }
}