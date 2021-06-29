package com.example.juanmuller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    private static final String URL_broches = "http://192.168.0.131/WS/listar.php";
    ArrayList<Broches> brochesList;
    GridView gridView;
    EditText editText;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView)findViewById(R.id.gridview);
        editText = (EditText) findViewById(R.id.inputSearch);
        brochesList = new ArrayList<>();
        editText.addTextChangedListener(this);
        loadBroches();
    }

    private void loadBroches(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_broches, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Pruebo1: ", response);
                Broches bro = null;
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject broche = array.getJSONObject(i);
                        bro = new Broches();
                        bro.setDescripcion(broche.getString("progennom"));
                        bro.setCodigo(broche.getString("progencod"));
                        bro.setPrecio(broche.getDouble("PreProPre1"));
                        bro.setImage(broche.getString("ImaAsoIma"));
                        //bro.setDato(broche.getString("ImaAsoIma"));
                        brochesList.add(bro);
                    }
                    adapter = new Adapter(brochesList, MainActivity.this);
                    gridView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.adapter.getFilter().filter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public class Adapter extends BaseAdapter implements Filterable {
        private List<Broches> brochesList;
        private List<Broches> brochestemp;
        private Context context;

        public Adapter(List<Broches> brochesList, Context context) {
            this.brochesList = brochesList;
            this.brochestemp = brochesList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return brochesList.size();
        }

        @Override
        public Object getItem(int position) {
            return brochesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Broches broches = brochesList.get(position);
            TextView textViewDesc;
            ImageView imageView;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.list_layout, parent, false);
            textViewDesc = (TextView) itemView.findViewById(R.id.textNames);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            if (brochesList.get(position).getImage() != null) {
                Glide.with(context).load(broches.getImage()).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.noimage);
            }
            textViewDesc.setText(broches.getDescripcion());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity((new Intent(MainActivity.this, InfoActivity.class).putExtra("broches", brochesList.get(position))));
                }
            });
            return itemView;
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null || constraint.length() > 0) {
                    constraint = constraint.toString().toUpperCase();
                    ArrayList<Broches> filters = new ArrayList<>();
                    for (int i = 0; i < brochestemp.size(); i++) {
                        if (brochestemp.get(i).getDescripcion().toUpperCase().contains(constraint)) {
                            //Broches bro = new Broches(brochestemp.get(i).getDescripcion(), brochestemp.get(i).getCodigo(), brochestemp.get(i).getCodExterno(), brochestemp.get(i).getPrecio(), brochestemp.get(i).getIdproducto(), brochestemp.get(i).getImagen());
                            Broches bro = new Broches(brochestemp.get(i).getDescripcion(), brochestemp.get(i).getCodigo(), brochestemp.get(i).getPrecio(), brochestemp.get(i).getImage());
                            filters.add(bro);
                        }
                    }

                    filterResults.count = filters.size();
                    filterResults.values = filters;

                } else {
                    filterResults.count = brochestemp.size();
                    filterResults.values = brochestemp;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                brochesList = (List<Broches>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
