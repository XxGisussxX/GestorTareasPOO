package org.example;

public enum Prioridad {
    INMEDIATO("Inmediato"),
    IMPORTANTE("Importante"),
    CONTIEMPO("Contiempo");

    private final String descripcion;

    private Prioridad(String descripcion) {
        this.descripcion = descripcion;
    }
}
