package com.example.crisol_Jaen.model;

    import java.util.ArrayList;
import java.util.List;

public class User {
    String id, name, lastname, email, telefono, direccion;
    List<String> favoriteBooks;

    public User(String id, String name, String lastname, String email, List<String> favoriteBooks) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.favoriteBooks = favoriteBooks;
    }

    public User(String id, String name, String lastname, String email) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    public User(String id, String name, String lastname, String email, String telefono, String direccion, List<String> favoriteBooks) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.favoriteBooks = favoriteBooks;
    }

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<String> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public void setFavoriteBook(String bookId) {
        if (this.favoriteBooks == null) {
            this.favoriteBooks = new ArrayList<String>();
        }
        this.favoriteBooks.add(bookId);
    }

    public void deleteFavoriteBook(String bookId) {
        if (this.favoriteBooks == null) {
            this.favoriteBooks = new ArrayList<String>();
        }
        if (this.favoriteBooks.contains(bookId)) {
            this.favoriteBooks.remove(bookId);
        }
    }
}
