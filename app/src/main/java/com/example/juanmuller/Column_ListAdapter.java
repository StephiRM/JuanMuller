package com.example.juanmuller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Column_ListAdapter extends ArrayAdapter<Broches> {
    //private Context mCtx;
    ArrayList<Broches> brochesList;
    LayoutInflater inflater;
    int mViewResourceId;

    public Column_ListAdapter(Context context, int textViewResourceId, ArrayList<Broches> brochesList) {
        super(context, textViewResourceId, brochesList);
        this.brochesList = brochesList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate( mViewResourceId, null);
        Broches broches = brochesList.get(position);

        if(broches!=null){
            TextView textViewCodigo = (TextView)convertView.findViewById(R.id.textCodigo);
            TextView textViewDesc = (TextView)convertView.findViewById(R.id.textDescripcion);
            TextView textViewPrecio = (TextView)convertView.findViewById(R.id.textPrecio);
            TextView textViewCantidad = (TextView)convertView.findViewById(R.id.textCantidad);

            if(textViewCodigo!=null){
                textViewCodigo.setText(broches.getCodigo());
            }

            if(textViewDesc!=null){
                textViewDesc.setText(broches.getDescripcion());
            }

            if(textViewPrecio!=null){
                textViewPrecio.setText(Double.toString(broches.getPrecio()));
            }

            if(textViewCantidad!=null){
                textViewCantidad.setText(Integer.toString(broches.getCantidad()));
            }
        }

        return convertView;
    }
}
