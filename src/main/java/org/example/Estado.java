package org.example;

public enum Estado {
    PENDIENTE("Tarea pendiete"),
    ENPROCESO("Tarea en proceso"),
    COMPLETADA("Tarea completada");

    private final String descripcion;

    private Estado(String descripcion) {
        this.descripcion = descripcion;
    }
}
