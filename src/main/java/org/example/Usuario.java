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
        // Crear un calendario por defecto con persistencia
        inicializarCalendarioPrincipal();
    }

    private void inicializarCalendarioPrincipal() {
        String archivoTareas = ArchivoManager.generarNombreArchivoTareas(this.email);
        Calendario calendarioPrincipal = new Calendario(archivoTareas);
        this.calendarios.add(calendarioPrincipal);
        System.out.println("📂 Calendario inicializado para " + this.nombre);
        System.out.println("💾 Archivo de tareas: " + archivoTareas);
    }

    public Calendario obtenerCalendario() {
        if (calendarios.isEmpty()) {
            inicializarCalendarioPrincipal();
        }
        return calendarios.get(0);
    }

    public void agregarCalendario(Calendario calendario) {
        calendarios.add(calendario);
    }

    // Método para crear un nuevo calendario con persistencia
    public Calendario crearNuevoCalendario(String sufijo) {
        String nombreArchivo = ArchivoManager.generarNombreArchivoTareas(this.email + "_" + sufijo);
        Calendario nuevoCalendario = new Calendario(nombreArchivo);
        this.calendarios.add(nuevoCalendario);
        System.out.println("📂 Nuevo calendario creado: " + nombreArchivo);
        return nuevoCalendario;
    }

    public boolean autenticar(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    // Método para sincronizar todos los calendarios
    public void sincronizarTodosLosCalendarios() {
        System.out.println("🔄 Sincronizando todos los calendarios de " + this.nombre + "...");
        for (Calendario calendario : calendarios) {
            calendario.sincronizarConArchivo();
        }
        System.out.println("✅ Sincronización completada para todos los calendarios.");
    }

    // Método para crear backup de todas las tareas
    public void crearBackupCompleto() {
        System.out.println("💾 Creando backup completo para " + this.nombre + "...");
        for (Calendario calendario : calendarios) {
            calendario.crearBackup();
        }
        System.out.println("✅ Backup completo creado.");
    }

    // Método para mostrar información de archivos
    public void mostrarInformacionArchivos() {
        System.out.println("\n📊 === INFORMACIÓN DE ARCHIVOS ===");
        System.out.println("👤 Usuario: " + this.nombre);
        System.out.println("📧 Email: " + this.email);
        System.out.println("📂 Calendarios: " + calendarios.size());

        for (int i = 0; i < calendarios.size(); i++) {
            System.out.println("\n📋 Calendario " + (i + 1) + ":");
            calendarios.get(i).mostrarInformacionArchivo();
            System.out.println("   📝 Tareas: " + calendarios.get(i).contarTareas());
        }
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
        // Reconfigurar archivos de calendarios con el nuevo email
        reconfigurarArchivosCalendarios();
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    private void reconfigurarArchivosCalendarios() {
        for (int i = 0; i < calendarios.size(); i++) {
            String nuevoArchivo;
            if (i == 0) {
                nuevoArchivo = ArchivoManager.generarNombreArchivoTareas(this.email);
            } else {
                nuevoArchivo = ArchivoManager.generarNombreArchivoTareas(this.email + "_" + i);
            }
            calendarios.get(i).setArchivoTareas(nuevoArchivo);
        }
        System.out.println("🔄 Archivos de calendarios reconfigurados para nuevo email.");
    }

    @Override
    public String toString() {
        return String.format("👤 Usuario: %s | Email: %s | Calendarios: %d",
                nombre, email, calendarios.size());
    }
}