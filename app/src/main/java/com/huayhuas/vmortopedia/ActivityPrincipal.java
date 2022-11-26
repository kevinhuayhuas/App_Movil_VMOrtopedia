package com.huayhuas.vmortopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityPrincipal extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView txtNombre, txtEmail;
    Button btnCerrarSesion;
    RequestQueue rq;
    ListView listProductos;

    AdapterProducto adapterListaProductos;
    ArrayList<Producto> productoArrayList;
    Producto usuario;

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

        productoArrayList = new ArrayList<>();
        adapterListaProductos = new AdapterProducto(this,productoArrayList);
        listProductos.setAdapter(adapterListaProductos);

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
                                int id           = objeto.getInt("id");
                                String name       = objeto.getString("name");
                                usuario = new Producto(id, name, "");
                                productoArrayList.add(usuario);
                                adapterListaProductos.notifyDataSetChanged();
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

}