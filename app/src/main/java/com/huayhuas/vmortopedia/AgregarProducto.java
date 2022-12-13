package com.huayhuas.vmortopedia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AgregarProducto extends AppCompatActivity {
    RequestQueue rq;
    Button btnRegistrar;
    EditText  txtNombre,txtDescripcion, txtPrecioRegular, txtPrecio, txtPrecioVenta,txtEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        rq = Volley.newRequestQueue(this);

        txtNombre= findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecioRegular = findViewById(R.id.txtPrecioRegular);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtPrecioVenta = findViewById(R.id.txtPrecioVenta);
        txtEstado = findViewById(R.id.txtEstado);
        btnRegistrar = findViewById(R.id.btnActualizar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarProducto();
            }
        });
    }

    private void registrarProducto(){
        try {
            String url="https://vmortopediaperu.com/wp-json/wc/v3/products";

            String nombre =txtNombre.getText().toString().trim() ;
            String descripcion =txtDescripcion.getText().toString().trim();
            String precio_regular =txtPrecioRegular.getText().toString().trim() ;
            String precio =txtPrecio.getText().toString().trim() ;
            String precioventa =txtPrecioVenta.getText().toString().trim();
            String estado =txtEstado.getText().toString().trim();

            if(nombre.isEmpty()){
                Toast.makeText(this, "No ha ingresado su nombre", Toast.LENGTH_SHORT).show();
                return;
            }
            if(descripcion.isEmpty()){
                Toast.makeText(this, "No ha ingresado su nombre", Toast.LENGTH_SHORT).show();
                return;
            }
            if(precio_regular.isEmpty()){
                Toast.makeText(this, "No ha ingresado su nombre", Toast.LENGTH_SHORT).show();
                return;
            }
            if(precio.isEmpty()){
                Toast.makeText(this, "No ha ingresado su nombre", Toast.LENGTH_SHORT).show();
                return;
            }
            if(precioventa.isEmpty()){
                Toast.makeText(this, "No ha ingresado su nombre", Toast.LENGTH_SHORT).show();
                return;
            }


            ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("Registrando");
            progressDialog.setMessage("Espere porfavor...");
            progressDialog.show();

            StringRequest peticion = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Toast.makeText(AgregarProducto.this, "Se ha actualizado ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ActivityPrincipal.class));
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AgregarProducto.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                //Enlace Autenticacion Basica Volley https://itecnote.com/tecnote/android-how-does-one-use-basic-authentication-with-volley-on-android/
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    GlobalClass vglobal= new GlobalClass();
                    String creds = String.format("%s:%s",vglobal.user,vglobal.pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                    params.put("Authorization", auth);
                    return params;
                }
                @Nullable
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("name", nombre);
                    params.put("short_description", descripcion);
                    params.put("price", precio);
                    params.put("regular_price", precio_regular);
                    params.put("sale_price", precioventa);
                    params.put("status", estado);
                    return params;
                }
            };
            rq.add(peticion);
        }catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            txtEstado.setText(e.toString());
        }

    }

}