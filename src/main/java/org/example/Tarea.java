package org.example;

import java.util.Date;

public class Tarea {
    private static int contadorId = 1;
    private int id;
    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private Prioridad prioridad;
    private Estado estado;
    private Date recordatorio;

    public Tarea(String nombre, Date fechaInicio, Date fechaFin, Prioridad prioridad, Date recordatorio) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.descripcion = ""; // Inicializar descripción vacía
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.prioridad = prioridad;
        this.estado = Estado.PENDIENTE;
        this.recordatorio = recordatorio;
    }

    // Constructor adicional con descripción
    public Tarea(String nombre, String descripcion, Date fechaInicio, Date fechaFin, Prioridad prioridad, Date recordatorio) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.prioridad = prioridad;
        this.estado = Estado.PENDIENTE;
        this.recordatorio = recordatorio;
    }

    public void marcarComoHecho() {
        this.estado = Estado.COMPLETADA;
    }

    public void marcarEnProceso() {
        this.estado = Estado.ENPROCESO;
    }

    // Método estático para actualizar el contador de ID (útil al cargar desde archivo)
    public static void actualizarContadorId(int nuevoContador) {
        if (nuevoContador > contadorId) {
            contadorId = nuevoContador;
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public Date getRecordatorio() {
        return recordatorio;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
        // Actualizar el contador si es necesario
        actualizarContadorId(id + 1);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setRecordatorio(Date recordatorio) {
        this.recordatorio = recordatorio;
    }

    @Override
    public String toString() {
        return String.format(
                "📋 ID: %d | Nombre: %s\n" +
                        "   📝 Descripción: %s\n" +
                        "   📅 Inicio: %s | Fin: %s\n" +
                        "   ⚡ Prioridad: %s | Estado: %s\n" +
                        "   🔔 Recordatorio: %s\n",
                id, nombre,
                descripcion.isEmpty() ? "Sin descripción" : descripcion,
                fechaInicio, fechaFin,
                prioridad, estado,
                recordatorio
        );
    }
}