package com.huayhuas.vmortopedia;

public class Producto {

    int id;
    String  img, nombre, descripcion;
    String precio_regular, precio, precio_venta;
    String estado;

    public Producto(){

    }

    public Producto(int id, String img, String nombre, String descripcion, String precio_regular, String precio , String precio_venta, String estado) {
        this.id = id;
        this.img = img;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio_regular = precio_regular;
        this.precio = precio;
        this.precio_venta = precio_venta;
        this.estado = estado;
    }


    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio_regular() {
        return precio_regular;
    }

    public String getPrecio() {
        return precio;
    }

    public String getPrecio_venta() {
        return precio_venta;
    }

    public String getEstado() {
        return estado;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio_regular(String precio_regular) {
        this.precio_regular = precio_regular;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setPrecio_venta(String precio_venta) {
        this.precio_venta = precio_venta;
    }

    public void setStado(String estado) {
        this.estado = estado;
    }
}
