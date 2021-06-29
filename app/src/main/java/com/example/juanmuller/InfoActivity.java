package com.example.juanmuller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {

    TextView tvCodigo,tvDescripcion, tvPrecio;
    EditText etCantidad;
    ImageView img;
    RequestQueue requestQueue;
    String codExterno, idproducto, dato;
    Bitmap imagen;
    String image;
    Boolean estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        img = (ImageView) findViewById(R.id.imageview);
        tvCodigo = (TextView)findViewById(R.id.tvCodigo2);
        tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        tvPrecio = (TextView)findViewById(R.id.tvPrecio);
        etCantidad = (EditText) findViewById(R.id.etCantidad);

        Intent intent = getIntent();
        final Broches broches = intent.getParcelableExtra("broches");

        if(broches!=null) {
            //imagen = broches.getImagen();
            String codigo = broches.getCodigo();
            String descripcion = broches.getDescripcion();
            Double precio = broches.getPrecio();

            //codExterno = broches.getCodExterno();
            //idproducto = broches.getIdproducto();
            image = broches.getImage();
            estado = broches.isEstado();

            if (broches.getImage() != null) {
               Glide.with(this).load(broches.getImage()).into(img);
            } else {
                img.setImageResource(R.drawable.noimage);
            }

            tvCodigo.setText(codigo);
            tvDescripcion.setText(descripcion);
            tvPrecio.setText(Double.toString(precio));
        }


        Button button = (Button) findViewById(R.id.btnAgregar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCantidad.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Debe ingresar una cantidad", Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(etCantidad.getText().toString())==0){
                    Toast.makeText(getApplicationContext(), "La cantidad no puede ser cero. Vuelva a ingresar", Toast.LENGTH_SHORT).show();
                }else{
                    ejecutarServicio("http://192.168.0.131/WS/insertar.php");
                }
            }
        });

        Button button2 = (Button) findViewById(R.id.btnVerListado);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoActivity.this, ListadoActivity.class);
                //broches2 = new Broches(tvDescripcion.getText().toString(), tvCodigo.getText().toString(), codExterno, Double.parseDouble(tvPrecio.getText().toString()), idproducto, Integer.parseInt(etCantidad.getText().toString()), dato, imagen);
                //broches2 = new Broches(tvCodigo.getText().toString(),tvDescripcion.getText().toString(),Double.parseDouble(tvPrecio.getText().toString()),Integer.parseInt(etCantidad.getText().toString()));
                i.putExtra("broches", getIntent().getStringExtra("broches"));
                //i.putExtra("broches",broches2);
                startActivity(i);
            }
        });

        Button button3 = (Button) findViewById(R.id.btnVolver);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(InfoActivity.this, MainActivity.class);
                //startActivity(i);
                onBackPressed();
            }
        });
    }

    private void ejecutarServicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();

                parametros.put("codigo",tvCodigo.getText().toString());
                parametros.put("descripcion",tvDescripcion.getText().toString());
                parametros.put("precio",tvPrecio.getText().toString());
                parametros.put("cantidad",etCantidad.getText().toString());
                parametros.put("estado",estado.toString());
                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
