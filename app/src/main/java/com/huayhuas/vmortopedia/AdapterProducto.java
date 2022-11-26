package com.huayhuas.vmortopedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterProducto extends ArrayAdapter<Producto> {
    Context context;
    List<Producto> arrayProdutos;

    public AdapterProducto(@NonNull Context context, List<Producto> arrayProdutos) {
        super(context,  R.layout.list_productos, arrayProdutos);
        this.context = context;
        this.arrayProdutos = arrayProdutos;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_productos, null, true);
        TextView txtId     = view.findViewById(R.id.txtId);
        TextView txtName = view.findViewById(R.id.txtName);
        txtId.setText(arrayProdutos.get(position).getId()+"");
        txtName.setText(arrayProdutos.get(position).getNombre());
        return view;
    }


}
