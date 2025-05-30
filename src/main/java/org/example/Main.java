package org.example;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Main {
    private static final String ARCHIVO_USUARIOS = "usuariospredefinidos.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🗓️  === GESTOR DE TAREAS === 🗓️");
        System.out.println();

        // Cargar usuarios existentes
        List<Usuario> usuarios = ArchivoManager.cargarUsuarios(ARCHIVO_USUARIOS);

        Usuario usuarioActual = null;

        // Si no hay usuarios registrados, crear uno nuevo
        if (usuarios.isEmpty()) {
            System.out.println("👋 ¡Bienvenido! Parece que es tu primera vez aquí.");
            usuarioActual = registrarNuevoUsuario(scanner, usuarios);
        } else {
            // Permitir login o registro
            System.out.println("¿Qué deseas hacer?");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar nuevo usuario");
            System.out.print("Opción: ");

            int opcion = leerEntero(scanner);

            if (opcion == 1) {
                usuarioActual = iniciarSesion(scanner, usuarios);
            } else {
                usuarioActual = registrarNuevoUsuario(scanner, usuarios);
            }
        }

        if (usuarioActual == null) {
            System.out.println("❌ No se pudo iniciar la aplicación.");
            return;
        }

        System.out.println("✅ ¡Bienvenido, " + usuarioActual.getNombre() + "!");

        // Guardar usuarios (por si se registró uno nuevo)
        ArchivoManager.guardarUsuarios(usuarios, ARCHIVO_USUARIOS);

        // Menú principal
        mostrarMenuPrincipal(usuarioActual, scanner);
    }

    private static Usuario registrarNuevoUsuario(Scanner scanner, List<Usuario> usuarios) {
        System.out.println("\n📝 === REGISTRO DE USUARIO ===");

        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine().trim();

        String email;
        do {
            System.out.print("Ingrese su correo electrónico: ");
            email = scanner.nextLine().trim();
            if (!esEmailValido(email)) {
                System.out.println("❌ Email inválido. Intente nuevamente.");
            }
        } while (!esEmailValido(email));

        // Verificar si el email ya existe
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                System.out.println("❌ Este email ya está registrado.");
                return null;
            }
        }

        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine().trim();

        Usuario nuevoUsuario = new Usuario(nombre, email, contrasena);
        usuarios.add(nuevoUsuario);

        System.out.println("✅ Usuario registrado correctamente.");
        return nuevoUsuario;
    }

    private static Usuario iniciarSesion(Scanner scanner, List<Usuario> usuarios) {
        System.out.println("\n🔐 === INICIAR SESIÓN ===");

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine().trim();

        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.autenticar(contrasena)) {
                return usuario;
            }
        }

        System.out.println("❌ Credenciales incorrectas.");
        return null;
    }

    private static boolean esEmailValido(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }

    private static void mostrarMenuPrincipal(Usuario usuario, Scanner scanner) {
        Calendario calendario = usuario.obtenerCalendario();
        boolean ejecutando = true;

        while (ejecutando) {
            System.out.println("\n📋 === MENÚ PRINCIPAL ===");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Buscar tarea por nombre o ID");
            System.out.println("3. Modificar tarea");
            System.out.println("4. Eliminar tarea");
            System.out.println("5. Mostrar todas las tareas");
            System.out.println("6. Marcar tarea como completada");
            System.out.println("7. Ver estadísticas");
            System.out.println("8. Salir");
            System.out.print("Opción: ");

            int opcion = leerEntero(scanner);

            switch (opcion) {
                case 1 -> agregarTarea(calendario, scanner);
                case 2 -> buscarTareas(calendario, scanner);
                case 3 -> modificarTarea(calendario, scanner);
                case 4 -> eliminarTarea(calendario, scanner);
                case 5 -> mostrarTareas(calendario);
                case 6 -> marcarTareaCompletada(calendario, scanner);
                case 7 -> mostrarEstadisticas(calendario);
                case 8 -> {
                    System.out.println("👋 ¡Hasta luego!");
                    ejecutando = false;
                }
                default -> System.out.println("⚠️ Opción no válida.");
            }
        }
    }

    private static void agregarTarea(Calendario calendario, Scanner scanner) {
        try {
            System.out.println("\n➕ === AGREGAR NUEVA TAREA ===");

            System.out.print("Nombre de la tarea: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Descripción (opcional): ");
            String descripcion = scanner.nextLine().trim();

            System.out.print("Fecha de inicio (YYYY-MM-DD): ");
            String fechaInicioStr = scanner.nextLine().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(fechaInicioStr);

            System.out.print("Fecha de fin (YYYY-MM-DD): ");
            String fechaFinStr = scanner.nextLine().trim();
            Date fechaFin = sdf.parse(fechaFinStr);

            System.out.println("Prioridades disponibles:");
            System.out.println("- INMEDIATO");
            System.out.println("- IMPORTANTE");
            System.out.println("- CONTIEMPO");
            System.out.print("Seleccione prioridad: ");
            String prioridadStr = scanner.nextLine().trim().toUpperCase();
            Prioridad prioridad = Prioridad.valueOf(prioridadStr);

            System.out.print("Fecha del recordatorio (YYYY-MM-DD): ");
            String recordatorioStr = scanner.nextLine().trim();
            Date recordatorio = sdf.parse(recordatorioStr);

            Tarea nuevaTarea;
            if (descripcion.isEmpty()) {
                nuevaTarea = new Tarea(nombre, fechaInicio, fechaFin, prioridad, recordatorio);
            } else {
                nuevaTarea = new Tarea(nombre, descripcion, fechaInicio, fechaFin, prioridad, recordatorio);
            }

            calendario.agregarTarea(nuevaTarea);
            System.out.println("✅ Tarea agregada exitosamente!");
            System.out.println("📋 ID: " + nuevaTarea.getId() + " | Nombre: " + nuevaTarea.getNombre());

        } catch (ParseException e) {
            System.out.println("❌ Error: Formato de fecha incorrecto. Use YYYY-MM-DD");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: Valor de prioridad inválido. Use INMEDIATO, IMPORTANTE o CONTIEMPO");
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    private static List<Tarea> buscarTareas(Calendario calendario, Scanner scanner) {
        System.out.println("\n🔍 === BUSCAR TAREAS ===");
        System.out.print("Digite el nombre o el ID de la tarea: ");
        String busqueda = scanner.nextLine().trim();
        int id = -1;

        try {
            id = Integer.parseInt(busqueda);
        } catch (NumberFormatException e) {
            // No es un número, buscar por nombre
        }

        List<Tarea> resultados = calendario.buscarTarea(busqueda, id);

        if (resultados.isEmpty()) {
            System.out.println("⚠️ No se encontraron tareas.");
        } else {
            System.out.println("🔍 Tareas encontradas:");
            for (Tarea tarea : resultados) {
                System.out.println("➡️ ID: " + tarea.getId() + " | Nombre: " + tarea.getNombre() + " | Estado: " + tarea.getEstado());
            }
        }
        return resultados;
    }

    private static void modificarTarea(Calendario calendario, Scanner scanner) {
        List<Tarea> resultados = buscarTareas(calendario, scanner);
        if (resultados.isEmpty()) return;

        System.out.print("Ingrese el ID de la tarea a modificar: ");
        int id = leerEntero(scanner);

        System.out.print("Nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();
        System.out.print("Nueva descripción: ");
        String nuevaDescripcion = scanner.nextLine().trim();

        boolean exito = calendario.modificarTarea(id, nuevoNombre, nuevaDescripcion);
        if (exito) {
            System.out.println("✅ Tarea modificada exitosamente.");
        } else {
            System.out.println("⚠️ No se encontró una tarea con ese ID.");
        }
    }

    private static void eliminarTarea(Calendario calendario, Scanner scanner) {
        List<Tarea> resultados = buscarTareas(calendario, scanner);
        if (resultados.isEmpty()) return;

        System.out.print("Ingrese el ID de la tarea a eliminar: ");
        int id = leerEntero(scanner);

        System.out.print("⚠️ ¿Confirma la eliminación permanente? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();

        if (confirmacion.equals("S") || confirmacion.equals("SI")) {
            boolean exito = calendario.eliminarTarea(id);
            if (exito) {
                System.out.println("✅ Tarea eliminada exitosamente.");
            } else {
                System.out.println("⚠️ No se encontró una tarea con ese ID.");
            }
        } else {
            System.out.println("❌ Eliminación cancelada.");
        }
    }

    private static void marcarTareaCompletada(Calendario calendario, Scanner scanner) {
        List<Tarea> resultados = buscarTareas(calendario, scanner);
        if (resultados.isEmpty()) return;

        System.out.print("Ingrese el ID de la tarea a marcar como completada: ");
        int id = leerEntero(scanner);

        boolean exito = calendario.marcarTareaComoCompletada(id);
        if (exito) {
            System.out.println("✅ Tarea marcada como completada.");
        } else {
            System.out.println("⚠️ No se encontró una tarea con ese ID.");
        }
    }

    private static void mostrarTareas(Calendario calendario) {
        calendario.mostrarTareas();
    }

    private static void mostrarEstadisticas(Calendario calendario) {
        System.out.println("\n📊 === ESTADÍSTICAS ===");
        System.out.println("📝 Total de tareas: " + calendario.contarTareas());
        System.out.println("⏳ Pendientes: " + calendario.contarTareasPorEstado(Estado.PENDIENTE));
        System.out.println("🔄 En proceso: " + calendario.contarTareasPorEstado(Estado.ENPROCESO));
        System.out.println("✅ Completadas: " + calendario.contarTareasPorEstado(Estado.COMPLETADA));
    }

    private static int leerEntero(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.print("❌ Entrada vacía. Intente nuevamente: ");
                continue;
            }
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("❌ Debe ingresar un número válido. Intente nuevamente: ");
            }
        }
    }
}