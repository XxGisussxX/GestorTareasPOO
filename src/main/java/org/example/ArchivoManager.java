package org.example;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class ArchivoManager {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Métodos existentes para usuarios
    public static void guardarUsuarios(List<Usuario> usuarios, String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (Usuario u : usuarios) {
                writer.write(u.getNombre() + "," + u.getEmail() + "," + u.getContrasena());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("❌ Error al guardar usuarios: " + e.getMessage());
        }
    }

    public static List<Usuario> cargarUsuarios(String nombreArchivo) {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    Usuario u = new Usuario(partes[0], partes[1], partes[2]);
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, no es un error crítico
            System.out.println("📄 Archivo de usuarios no encontrado. Se creará uno nuevo.");
        }
        return usuarios;
    }

    // Nuevos métodos para tareas
    public static void guardarTareas(List<Tarea> tareas, String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (Tarea tarea : tareas) {
                // Formato: id,nombre,descripcion,fechaInicio,fechaFin,prioridad,estado,recordatorio
                String linea = String.format("%d|%s|%s|%s|%s|%s|%s|%s",
                        tarea.getId(),
                        escaparTexto(tarea.getNombre()),
                        escaparTexto(tarea.getDescripcion()),
                        sdf.format(tarea.getFechaInicio()),
                        sdf.format(tarea.getFechaFin()),
                        tarea.getPrioridad().name(),
                        tarea.getEstado().name(),
                        sdf.format(tarea.getRecordatorio())
                );
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("💾 Tareas guardadas correctamente en " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar tareas: " + e.getMessage());
        }
    }

    public static List<Tarea> cargarTareas(String nombreArchivo) {
        List<Tarea> tareas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    Tarea tarea = parsearTarea(linea);
                    if (tarea != null) {
                        tareas.add(tarea);
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Error al procesar línea: " + linea + " - " + e.getMessage());
                }
            }
            System.out.println("📂 Se cargaron " + tareas.size() + " tareas desde " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("📄 Archivo de tareas no encontrado. Se creará uno nuevo cuando agregues tareas.");
        }
        return tareas;
    }

    private static Tarea parsearTarea(String linea) {
        try {
            String[] partes = linea.split("\\|");
            if (partes.length != 8) {
                throw new IllegalArgumentException("Formato de línea incorrecto");
            }

            int id = Integer.parseInt(partes[0]);
            String nombre = desescaparTexto(partes[1]);
            String descripcion = desescaparTexto(partes[2]);
            Date fechaInicio = sdf.parse(partes[3]);
            Date fechaFin = sdf.parse(partes[4]);
            Prioridad prioridad = Prioridad.valueOf(partes[5]);
            Estado estado = Estado.valueOf(partes[6]);
            Date recordatorio = sdf.parse(partes[7]);

            // Crear la tarea usando el constructor apropiado
            Tarea tarea;
            if (descripcion.isEmpty()) {
                tarea = new Tarea(nombre, fechaInicio, fechaFin, prioridad, recordatorio);
            } else {
                tarea = new Tarea(nombre, descripcion, fechaInicio, fechaFin, prioridad, recordatorio);
            }

            // Restaurar el ID original y el estado
            tarea.setId(id);
            tarea.setEstado(estado);

            return tarea;

        } catch (ParseException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println("❌ Error al parsear tarea: " + e.getMessage());
            return null;
        }
    }

    // Método para generar nombre de archivo único por usuario
    public static String generarNombreArchivoTareas(String emailUsuario) {
        // Reemplazar caracteres especiales para crear un nombre válido de archivo
        String nombreLimpio = emailUsuario.replaceAll("[^a-zA-Z0-9]", "_");
        return "tareas_" + nombreLimpio + ".txt";
    }

    // Métodos auxiliares para manejar texto con caracteres especiales
    private static String escaparTexto(String texto) {
        if (texto == null) return "";
        return texto.replace("|", "\\|").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String desescaparTexto(String texto) {
        if (texto == null) return "";
        return texto.replace("\\|", "|").replace("\\n", "\n").replace("\\r", "\r");
    }

    // Método para crear backup de tareas
    public static void crearBackupTareas(String archivoOriginal) {
        String archivoBackup = archivoOriginal.replace(".txt", "_backup.txt");
        try {
            List<String> lineas = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    lineas.add(linea);
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoBackup))) {
                for (String linea : lineas) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
            System.out.println("💾 Backup creado: " + archivoBackup);
        } catch (IOException e) {
            System.err.println("⚠️ No se pudo crear backup: " + e.getMessage());
        }
    }

    // Método para obtener estadísticas del archivo
    public static void mostrarEstadisticasArchivo(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            int totalLineas = 0;
            while (reader.readLine() != null) {
                totalLineas++;
            }
            System.out.println("📊 Archivo: " + nombreArchivo + " - Total de registros: " + totalLineas);
        } catch (IOException e) {
            System.out.println("❌ No se pudo leer el archivo: " + nombreArchivo);
        }
    }
}