package org.example;
import java.io.*;
import java.util.*;

public class ArchivoManager {
    public static void guardarUsuarios(List<Usuario> usuarios, String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (Usuario u : usuarios) {
                writer.write(u.getNombre() + "," + u.getEmail() + "," + u.getContrasena());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Usuario> cargarUsuarios(String nombreArchivo ) {
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
            e.printStackTrace();
        }
        return usuarios;
    }
}