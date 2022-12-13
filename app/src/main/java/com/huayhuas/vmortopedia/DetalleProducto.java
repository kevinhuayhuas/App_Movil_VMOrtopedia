package com.huayhuas.vmortopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetalleProducto extends AppCompatActivity {
    ImageView imgProducto;
    TextView txtIdProducto, txtNombreProducto, txtTitulo,txtDescripcion, txtPrecioRegular, txtPrecio, txtPrecioVenta,txtEstado;
    int position;
    int id;
    String nombre, description, estado;
    String img;
    String precio_regular, precio, precio_venta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        imgProducto = findViewById(R.id.imgProducto);
        txtIdProducto= findViewById(R.id.txtIdProducto);
        txtNombreProducto= findViewById(R.id.txtNombreProducto);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecioRegular = findViewById(R.id.txtPrecioRegular);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtPrecioVenta = findViewById(R.id.txtPrecioVenta);
        txtEstado = findViewById(R.id.txtEstado);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        id = intent.getExtras().getInt("id");
        img = intent.getExtras().getString("img");
        nombre = intent.getExtras().getString("nombre");
        description = intent.getExtras().getString("descripcion");
        precio_regular = intent.getExtras().getString("precio_regular");
        precio = intent.getExtras().getString("precio");
        precio_venta = intent.getExtras().getString("precio_venta");
        estado = intent.getExtras().getString("estado");
        //Para convertir el array de la variable images en JSon
        JSONArray jsonArray ;

        try {
            jsonArray = new JSONArray(img);
            txtTitulo.setText(nombre);
            txtIdProducto.setText(""+id);
            txtNombreProducto.setText(nombre);
            txtDescripcion.setText(description);
            txtPrecioRegular.setText("S/"+precio_regular);
            txtPrecio.setText("S/"+precio);
            txtPrecioVenta.setText("S/"+precio_venta);
            txtEstado.setText(estado);

            //Obtener la primera imagen
            String imagenUrl= jsonArray.getJSONObject(0).getString("src");
            Picasso.get().load(imagenUrl).into(imgProducto);
        }catch (Throwable t) {
            Toast.makeText(this, "Un Problema" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }


    }
}