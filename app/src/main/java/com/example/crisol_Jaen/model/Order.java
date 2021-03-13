package com.example.crisol_Jaen.model;

public class Order {
    Book book;
    String id, direccionEntrega, idUser, telefono, token, estado, productos;
    double costoPedido, costoTotal;

    public Order(Book book, String direccionEntrega, String telefono){
        this.book = book;
        this.direccionEntrega = direccionEntrega;
        this.telefono = telefono;
    }

    public Order(String id, String direccionEntrega, String idUser, String telefono, String token, String estado, double costoPedido, double costoTotal, String productos) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.idUser = idUser;
        this.telefono = telefono;
        this.token = token;
        this.estado = estado;
        this.costoPedido = costoPedido;
        this.costoTotal = costoTotal;
        this.productos = productos;
    }

    public Order(){ }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getCostoPedido() {
        return costoPedido;
    }

    public void setCostoPedido(double costoPedido) {
        this.costoPedido = costoPedido;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getProductos() { return productos; }

    public void setProductos(String productos) { this.productos = productos; }

//    public void addProducto(Book book){
//        if(this.productos==null){
//            this.productos = new ArrayList<Book>();
//        }
//        this.productos.add(book);
//    }
//
//    public void deleteProducto(Book book){
//        if(this.productos==null){
//            this.productos = new ArrayList<Book>();
//        }
//        if(this.productos.contains(book)) {
//            this.productos.remove(book);
//        }
//    }
}
