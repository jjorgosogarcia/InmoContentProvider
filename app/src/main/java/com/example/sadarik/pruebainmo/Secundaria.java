package com.example.sadarik.pruebainmo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Sadarik on 26/01/2015.
 */
public class Secundaria extends Activity {

    protected static final int CAMERA_REQUEST = 1;
    protected static final int GALLERY_PICTURE = 2;
    private Intent intentImagen = null;
    private GestorFoto gf;
    private AdaptadorFotos adf;
    private long id;
    private ListView lv;

    /*****************************************************/
    /*                 metodos on                        */
    /*****************************************************/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_borrar) {
            Cursor cursor = (Cursor) lv.getItemAtPosition(index);
            Foto foto = GestorFoto.getRow(cursor);
            File file = new File(foto.getFoto());
            if(file.delete()){
                gf.delete(foto);
                adf.changeCursor(gf.getCursor());
                initComponents();
            }else{

            }

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_secundaria);
        gf = new GestorFoto(this);
        Intent i=getIntent();
        id=i.getLongExtra("id",0);
        FragmentoDos fdos = (FragmentoDos) getFragmentManager().findFragmentById(R.id.fragment2);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.longmenufotos, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gf.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secundaria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_aniadir) {
            abrirDialogo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        Bitmap imagen=null;
        String currentDateTimeString;
        File archivo=null;
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_PICTURE:
                    gf.open();
                    try {
                        stream = getContentResolver().openInputStream(data.getData());
                        imagen = BitmapFactory.decodeStream(stream);
                        stream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    currentDateTimeString=currentDateTimeString.replace(" ","_");
                    currentDateTimeString=currentDateTimeString.replace("/","_");
                    archivo = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),"inmueble_"+id+"_"+currentDateTimeString+".jpg");
                    try {
                        FileOutputStream out = new FileOutputStream(archivo);
                        imagen.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Foto foto= new Foto();
                    foto.setIdInmueble(id);
                    foto.setFoto(archivo.getPath());
                    gf.insert(foto);
                    break;
            }
        }
    }


    /*****************************************************/
    /*                     auxiliares                    */
    /*****************************************************/

    public void initComponents(){
        gf.open();
        Cursor c = gf.getCursor(null,null,null,id);
        adf = new AdaptadorFotos(this,c);
        lv = (ListView) findViewById(R.id.listView2);
        lv.setAdapter(adf);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = view.getTag();
                AdaptadorFotos.ViewHolder vh;
                vh = (AdaptadorFotos.ViewHolder)o;
                String s=(String)vh.ivfotos.getTag();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + s), "image/jpeg");
                startActivity(intent);
            }
        });
        registerForContextMenu(lv);
    }

    private void abrirDialogo() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle(R.string.aadfoto);
        myAlertDialog.setMessage(R.string.seleccionar);

        myAlertDialog.setPositiveButton(R.string.galeria, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                intentImagen = new Intent(Intent.ACTION_GET_CONTENT, null);
                intentImagen.setType("image/*");
                intentImagen.putExtra("return-data", true);
                startActivityForResult(intentImagen, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton(R.string.camara,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                intentImagen = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (intentImagen.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        gf.open();
                        Foto foto= new Foto();
                        foto.setIdInmueble(id);
                        foto.setFoto(photoFile.getPath());
                        gf.insert(foto);
                    } catch (IOException ex) {

                    }
                    if (photoFile != null) {
                        intentImagen.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                        startActivityForResult(intentImagen, CAMERA_REQUEST);
                    }
                }
            }
        });
        myAlertDialog.show();
    }

    private File createImageFile() throws IOException {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        currentDateTimeString=currentDateTimeString.replace(" ","_");
        currentDateTimeString=currentDateTimeString.replace("/","_");
        String imageFileName="inmueble_"+id+"_"+currentDateTimeString+"";
        File storageDir =  getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir  );
        return image;
    }
}