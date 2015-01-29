package com.example.sadarik.pruebainmo;

/**
 * Created by Sadarik on 26/01/2015.
 */

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class FragmentoDos extends Fragment {

    private GestorFoto gfoto;
    private AdaptadorFotos adf;
    private ListView lv;
    private View v;
    private long idItem;

    public FragmentoDos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_fragmento_dos, container, false);
        return v;
    }

    public void rellenarListView(long id) {
        gfoto = new GestorFoto(getActivity().getBaseContext());
        idItem=id;
        gfoto.open();
        Cursor c = gfoto.getCursor(null,null,null,id);
        adf = new AdaptadorFotos(getActivity().getBaseContext(),c);
        lv = (ListView) getActivity().findViewById(R.id.listView2);
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
}
