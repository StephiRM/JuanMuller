package com.example.juanmuller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class ListadoActivity extends AppCompatActivity {
    private static final String URL_broches = "http://192.168.0.131/WS/listarSeleccionados.php";
    private static final String URL_eliminar = "http://192.168.0.131/WS/eliminar.php";
    private static final String URL_eliminarTodo = "http://192.168.0.131/WS/eliminarTodo.php";
    ListView listView;
    ArrayList<Broches> lista;
    Column_ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        listView = findViewById(R.id.listView2);
        lista = new ArrayList<>();
        loadSeleccionados();

        Button button = (Button) findViewById(R.id.btnVolver2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(ListadoActivity.this, InfoActivity.class);
                i.putExtra("broches", getIntent().getStringExtra("broches"));
                startActivity(i);*/
                //startActivity((new Intent(ListadoActivity.this, InfoActivity.class).putExtra("broches", lista.get(position))));
                onBackPressed();
            }
        });

        Button button2 = (Button) findViewById(R.id.btnVaciar);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ListadoActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Eliminar todo")
                        .setMessage("¿Desea eliminar todo el listado?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarTodo();
                                recreate();
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                                //Toast.makeText(getApplicationContext(), "Listado vaciado con éxito", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.cabecera, listView, false);
        listView.addHeaderView(headerView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ListadoActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Eliminar producto")
                        .setMessage("¿Desea eliminar el producto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Broches broches =  lista.get(position-1);
                                //Broches broches = (Broches) listView.getItemAtPosition(position-1);

                                String codigo = broches.getCodigo();
                                eliminarProducto(codigo);
                                recreate();
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    private void loadSeleccionados(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_broches, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Broches bro = null;
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject broche = array.getJSONObject(i);
                        bro = new Broches();
                        bro.setCodigo(broche.getString("codigo"));
                        bro.setDescripcion(broche.getString("descripcion"));
                        bro.setPrecio(broche.getDouble("precio"));
                        bro.setCantidad(broche.getInt("cantidad"));
                        lista.add(bro);
                    }

                    adapter = new Column_ListAdapter(ListadoActivity.this, R.layout.list_adapter_view, lista);
                    listView.setAdapter(adapter);
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

    private void eliminarProducto(final String codigo){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_eliminar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                //Log.d("XYZ1", String.valueOf(params));
                params.put("codigo",codigo);
                Log.d("XYZ2", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void eliminarTodo(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_eliminarTodo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Listado vaciado con éxito", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String,String> params = new HashMap<String,String>();
                //final String codigo;
                //params.put("codigo",codigo);
                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
