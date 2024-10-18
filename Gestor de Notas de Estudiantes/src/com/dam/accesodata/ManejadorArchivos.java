package com.dam.accesodata;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManejadorArchivos {
    private static final String ARCHIVO = "./notas_estudiantes.txt";

    public void añadirEstudiante(Estudiante estudiante) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            writer.write(estudiante.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error añadiendo estudiante: " + e.getMessage());
        }
    }

    public void mostrarEstudiantes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error mostrando estudiantes: " + e.getMessage());
        }
    }

    public void buscarEstudiante(String nombre) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains(nombre)) {
                    System.out.println("Estudiante encontrado: " + linea);
                    return;
                }
            }
            System.out.println("Estudiante no encontrado.");
        } catch (IOException e) {
            System.out.println("Error buscando estudiante: " + e.getMessage());
        }
    }

    public void calcularMedia() {
        List<Estudiante> estudiantes = leerEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes para calcular la media.");
            return;
        }

        double suma = 0;
        for (Estudiante estudiante : estudiantes) {
            suma += estudiante.getNota();
        }

        double media = suma / estudiantes.size();
        System.out.println("La nota media es: " + media);
    }

    private List<Estudiante> leerEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    estudiantes.add(new Estudiante(partes[0], Double.parseDouble(partes[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo estudiantes: " + e.getMessage());
        }
        return estudiantes;
    }
}