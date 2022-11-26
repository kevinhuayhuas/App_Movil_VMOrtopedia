package com.huayhuas.vmortopedia;

public class Producto {
    int id;
    String  nombre, descripcion;
public Producto(){

}
public Producto(int id, String nombre, String descripcion){
    this.id=id;
    this.nombre=nombre;
    this.descripcion=descripcion;
}
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
