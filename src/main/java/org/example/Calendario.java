package org.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calendario {
    private List<Tarea> tareas;
    private String archivoTareas; // Archivo donde se guardan las tareas

    public Calendario() {
        this.tareas = new ArrayList<>();
        this.archivoTareas = null; // Se asignará cuando se asocie con un usuario
    }

    public Calendario(String archivoTareas) {
        this.tareas = new ArrayList<>();
        this.archivoTareas = archivoTareas;
        cargarTareasDesdeArchivo();
    }

    // Métodos de persistencia
    public void setArchivoTareas(String archivoTareas) {
        this.archivoTareas = archivoTareas;
        cargarTareasDesdeArchivo();
    }

    public void cargarTareasDesdeArchivo() {
        if (archivoTareas != null) {
            List<Tarea> tareasCargadas = ArchivoManager.cargarTareas(archivoTareas);
            this.tareas.clear();
            this.tareas.addAll(tareasCargadas);
        }
    }

    public void guardarTareasEnArchivo() {
        if (archivoTareas != null) {
            ArchivoManager.guardarTareas(tareas, archivoTareas);
        }
    }

    public void crearBackup() {
        if (archivoTareas != null) {
            ArchivoManager.crearBackupTareas(archivoTareas);
        }
    }

    // Métodos existentes con persistencia automática
    public List<Tarea> getTareas() {
        return tareas;
    }

    public void agregarTarea(Tarea tarea) {
        tareas.add(tarea);
        guardarTareasEnArchivo(); // Guardar automáticamente
        System.out.println("💾 Tarea guardada automáticamente.");
    }

    public List<Tarea> buscarTarea(String nombre, int id) {
        List<Tarea> tareasEncontradas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (tarea.getNombre().toLowerCase().contains(nombre.toLowerCase()) || tarea.getId() == id) {
                tareasEncontradas.add(tarea);
            }
        }
        return tareasEncontradas;
    }

    public boolean modificarTarea(int id, String nuevoNombre, String nuevaDescripcion) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                tarea.setNombre(nuevoNombre);
                tarea.setDescripcion(nuevaDescripcion);
                guardarTareasEnArchivo(); // Guardar cambios
                System.out.println("💾 Cambios guardados automáticamente.");
                return true; // Tarea modificada exitosamente
            }
        }
        return false; // No se encontró la tarea buscada
    }

    public boolean eliminarTarea(int id) {
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getId() == id) {
                tareas.remove(i);
                guardarTareasEnArchivo(); // Guardar cambios
                System.out.println("💾 Eliminación guardada automáticamente.");
                return true; // Tarea eliminada exitosamente
            }
        }
        return false; // No se pudo eliminar tarea
    }

    public List<Tarea> obtenerTareasPorFecha(Date fecha) {
        List<Tarea> tareasEncontradas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (tarea.getFechaFin().equals(fecha)) {
                tareasEncontradas.add(tarea);
            }
        }
        return tareasEncontradas;
    }

    public List<Tarea> obtenerTareasPorEstado(Estado estado) {
        List<Tarea> tareasEncontradas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (tarea.getEstado() == estado) {
                tareasEncontradas.add(tarea);
            }
        }
        return tareasEncontradas;
    }

    public List<Tarea> obtenerTareasPorPrioridad(Prioridad prioridad) {
        List<Tarea> tareasEncontradas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (tarea.getPrioridad() == prioridad) {
                tareasEncontradas.add(tarea);
            }
        }
        return tareasEncontradas;
    }

    public boolean marcarTareaComoCompletada(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                tarea.marcarComoHecho();
                guardarTareasEnArchivo(); // Guardar cambios
                System.out.println("💾 Estado actualizado y guardado automáticamente.");
                return true;
            }
        }
        return false;
    }

    public boolean marcarTareaEnProceso(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                tarea.marcarEnProceso();
                guardarTareasEnArchivo(); // Guardar cambios
                System.out.println("💾 Estado actualizado y guardado automáticamente.");
                return true;
            }
        }
        return false;
    }

    public void mostrarTareas() {
        if (tareas.isEmpty()) {
            System.out.println("📝 No hay tareas registradas.");
            return;
        }

        System.out.println("\n📋 === LISTADO DE TAREAS ===");
        System.out.println("📄 Archivo: " + (archivoTareas != null ? archivoTareas : "Sin archivo"));
        for (Tarea tarea : tareas) {
            System.out.println(tarea);
            System.out.println("─".repeat(50));
        }
    }

    public int contarTareas() {
        return tareas.size();
    }

    public int contarTareasPorEstado(Estado estado) {
        int contador = 0;
        for (Tarea tarea : tareas) {
            if (tarea.getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }

    // Métodos adicionales para gestión avanzada
    public void sincronizarConArchivo() {
        System.out.println("🔄 Sincronizando con archivo...");
        cargarTareasDesdeArchivo();
        System.out.println("✅ Sincronización completada.");
    }

    public void mostrarInformacionArchivo() {
        if (archivoTareas != null) {
            System.out.println("📄 Archivo de tareas: " + archivoTareas);
            ArchivoManager.mostrarEstadisticasArchivo(archivoTareas);
        } else {
            System.out.println("⚠️ No hay archivo de tareas asignado.");
        }
    }

    public String getArchivoTareas() {
        return archivoTareas;
    }
}