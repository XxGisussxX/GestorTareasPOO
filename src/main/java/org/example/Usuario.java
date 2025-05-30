package org.example;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private static int contadorId = 1;
    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private List<Calendario> calendarios;

    public Usuario(String nombre, String email, String contrasena) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.calendarios = new ArrayList<>();
        // Crear un calendario por defecto
        this.calendarios.add(new Calendario());
    }

    public Calendario obtenerCalendario() {
        if (calendarios.isEmpty()) {
            calendarios.add(new Calendario());
        }
        return calendarios.get(0);
    }

    public void agregarCalendario(Calendario calendario) {
        calendarios.add(calendario);
    }

    public boolean autenticar(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public List<Calendario> getCalendarios() {
        return calendarios;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return String.format("👤 Usuario: %s | Email: %s | Calendarios: %d",
                nombre, email, calendarios.size());
    }
}