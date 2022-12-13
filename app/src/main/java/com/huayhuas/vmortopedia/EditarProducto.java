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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditarProducto extends AppCompatActivity {

    RequestQueue rq;

    EditText txtId,  txtNombre,txtDescripcion, txtPrecioRegular, txtPrecio, txtPrecioVenta,txtEstado;
    int position;
    int id;
    String nombre, description,estado;
    String img;
    String precio_regular, precio, precio_venta;
    Button btnActualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        rq = Volley.newRequestQueue(this);

        txtId= findViewById(R.id.txtId);
        txtNombre= findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecioRegular = findViewById(R.id.txtPrecioRegular);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtPrecioVenta = findViewById(R.id.txtPrecioVenta);
        txtEstado = findViewById(R.id.txtEstado);
        btnActualizar = findViewById(R.id.btnActualizar);

        Intent intent= getIntent();
        position = intent.getExtras().getInt("position");
        id = intent.getExtras().getInt("id");
        img = intent.getExtras().getString("img");
        nombre = intent.getExtras().getString("nombre");
        description = intent.getExtras().getString("descripcion");
        precio_regular = intent.getExtras().getString("precio_regular");
        precio = intent.getExtras().getString("precio");
        precio_venta = intent.getExtras().getString("precio_venta");
        estado = intent.getExtras().getString("estado");

        try {
            txtId.setText("" + id);
            txtNombre.setText(nombre);
            txtDescripcion.setText(description);
            txtPrecioRegular.setText(precio_regular);
            txtPrecio.setText(precio);
            txtPrecioVenta.setText( precio_venta);
            txtEstado.setText(estado);
        }catch (Throwable t) {
            Toast.makeText(this, "Un Problema" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarProducto();
            }
        });
    }

    private void actualizarProducto(){
        try {
             int id =Integer.parseInt(txtId.getText().toString().trim()) ;

            String url="https://vmortopediaperu.com/wp-json/wc/v3/products/"+id;

            String nombre =txtNombre.getText().toString().trim() ;
            String descripcion =txtDescripcion.getText().toString().trim();
            String precio_regular =txtPrecioRegular.getText().toString().trim() ;
            String precio =txtPrecio.getText().toString().trim() ;
            String precioventa =txtPrecioVenta.getText().toString().trim();
            String estado =txtEstado.getText().toString().trim();

             ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("Actualizando");
            progressDialog.setMessage("Espere porfavor...");
            progressDialog.show();

            StringRequest requerimiento = new StringRequest(Request.Method.PUT,
                    url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Toast.makeText(EditarProducto.this, "Se ha actualizado ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ActivityPrincipal.class));
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditarProducto.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    params.put("price", ""+precio);
                    params.put("regular_price", ""+precio_regular);
                    params.put("sale_price", ""+precioventa);
                    params.put("status", estado);
                    return params;
                }
            };
            rq.add(requerimiento);

        }catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

}