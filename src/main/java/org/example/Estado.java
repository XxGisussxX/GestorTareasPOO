package org.example;

public enum Estado {
    PENDIENTE("Tarea pendiente"),
    ENPROCESO("Tarea en proceso"),
    COMPLETADA("Tarea completada");

    private final String descripcion;

    private Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}