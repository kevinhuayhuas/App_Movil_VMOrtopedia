package com.huayhuas.vmortopedia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityPrincipal extends AppCompatActivity {

    FloatingActionButton fab_agregar;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView txtNombre, txtEmail ;
    ImageButton btnCerrarSesion;
    RequestQueue rq;
    ListView listProductos;

    AdapterProducto adapterListaProductos;
    public static ArrayList<Producto> productoArrayList = new ArrayList<>();
    Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        rq = Volley.newRequestQueue(this);
        listProductos = findViewById(R.id.listProductos);
        txtNombre = findViewById(R.id.txtNombre);
        txtEmail = findViewById(R.id.txtEmail);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        adapterListaProductos = new AdapterProducto(this,productoArrayList);
        listProductos.setAdapter(adapterListaProductos);
        fab_agregar = findViewById(R.id.fab_agregar);

        //Traer los datos de Google Nombre y Correo
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!= null){
            String nombre = acct.getDisplayName();
            String email= acct.getEmail();
            txtNombre.setText(nombre);
            txtEmail.setText(email);
        }//fin
        //Click al boton Cerrar Sesion
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        //evento click en lista
        listProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Contruyendo un Alerta Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                CharSequence[] dialogoItem = {"Ver Datos", "Editar datos", "Eliminar Datos"};
                builder.setTitle(productoArrayList.get(position).getNombre());

                builder.setItems(dialogoItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //Mostrar el detalle del registro (OPCION: Ver Datos)
                                    startActivity(new Intent(getApplicationContext(), DetalleProducto.class)
                                            .putExtra("position", position)
                                            .putExtra("id", productoArrayList.get(position).getId())
                                            .putExtra("img", productoArrayList.get(position).getImg())
                                            .putExtra("nombre", productoArrayList.get(position).getNombre())
                                            .putExtra("descripcion", productoArrayList.get(position).getDescripcion())
                                            .putExtra("precio_regular", productoArrayList.get(position).getPrecio_regular())
                                            .putExtra("precio", productoArrayList.get(position).getPrecio())
                                            .putExtra("precio_venta", productoArrayList.get(position).getPrecio_venta())
                                            .putExtra("estado", productoArrayList.get(position).getEstado())
                                    );
                                Toast.makeText(ActivityPrincipal.this, "Mostrando Datos", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                //Editar el registro del Usuario (OPCION: Editar datos)
                               startActivity(new Intent(getApplicationContext(), EditarProducto.class)
                                       .putExtra("position", position)
                                       .putExtra("id", productoArrayList.get(position).getId())
                                       .putExtra("img", productoArrayList.get(position).getImg())
                                       .putExtra("nombre", productoArrayList.get(position).getNombre())
                                       .putExtra("descripcion", productoArrayList.get(position).getDescripcion())
                                       .putExtra("precio_regular", productoArrayList.get(position).getPrecio_regular())
                                       .putExtra("precio", productoArrayList.get(position).getPrecio())
                                       .putExtra("precio_venta", productoArrayList.get(position).getPrecio_venta())
                                       .putExtra("estado", productoArrayList.get(position).getEstado())
                               );
                                Toast.makeText(ActivityPrincipal.this, "Editar Producto", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                //Eliminar el registro (OPCION: Eliminar Datos)
                                EliminarProducto(productoArrayList.get(position).getId());
                                Toast.makeText(ActivityPrincipal.this, "Eliminando", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                //mostrar Alert Dialog
                builder.create().show();
            }
        });
        //Click en nuevo
        fab_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AgregarProducto.class));
            }
        });
        //Llamar al metodo Listar
        listar();
    }
    public void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                finish();
                startActivity(new Intent(ActivityPrincipal.this, MainActivity.class));
            }
        });
    }

    public void listar(){
        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Cargando Datos");
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.show();

        String url="https://vmortopediaperu.com/wp-json/wc/v3/products?page=1&per_page=20";
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void  onResponse (JSONArray response){
                        productoArrayList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject objeto = new JSONObject(response.get(i).toString());
                                //Variables del Producto
                                int id;
                                String img ;
                                String nombre ;
                                String descripcion ;
                                String precio_regular ;
                                String precio ;
                                String precio_venta ;
                                String estado ;
                                //inicializando variables
                                id = objeto.getInt("id");
                                img = objeto.getString("images");
                                nombre = objeto.getString("name");
                                descripcion = objeto.getString("short_description");
                                precio_regular = objeto.getString("regular_price");
                                precio = objeto.getString("price");
                                precio_venta = objeto.getString("sale_price");
                               /* if (objeto.getString("regular_price")==""){
                                    precio_regular = objeto.getDouble("regular_price");
                                }else{
                                    precio_regular = 0.0;
                                }
                                if(objeto.getString("price") == ""){
                                    precio = objeto.getDouble("price");
                                }else{
                                    precio = 0.0;
                                }
                                if(objeto.getString("sale_price") == ""){
                                    precio_venta = objeto.getDouble("sale_price");
                                }else{
                                    precio_venta = 0.0;
                                }*/
                                estado = objeto.getString("status");

                                producto = new Producto(id,img,nombre,descripcion,precio_regular,precio,precio_venta,estado);
                                productoArrayList.add(producto);
                                adapterListaProductos.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }catch (JSONException e){
                                Toast.makeText(ActivityPrincipal.this, "Ups! "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void  onErrorResponse (VolleyError error){
                        Toast.makeText(ActivityPrincipal.this, "Error en consulta: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                };
        rq.add(requerimiento);
    }

    private void EliminarProducto(int id) {
        String url="https://vmortopediaperu.com/wp-json/wc/v3/products/"+id;

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Eliminando Producto");
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.show();

        StringRequest peticion = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Toast.makeText(ActivityPrincipal.this, "Se ha eliminado el Producto ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ActivityPrincipal.class));
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityPrincipal.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

        };
        rq.add(peticion);

    }

}