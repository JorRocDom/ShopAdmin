package com.example.testfx;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorComponentes {
    private static final String FILE_NAME = "items.txt";

    // Leer componentes desde el archivo
    public static ArrayList<Component> leerComponentes() {
        ArrayList<Component> componentes = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                Component componente = convertirAComponente(linea);
                if (componente != null) {
                    componentes.add(componente);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("⚠ Archivo no encontrado, creando uno nuevo...");
        }
        return componentes;
    }

    // Guardar un componente en el archivo
    public static void guardarComponente(Component c) {
        ArrayList<Component> componentes = leerComponentes();

        // Obtener la ID más alta y sumarle 1
        int nuevaId = componentes.stream()
                .mapToInt(Component::getId)
                .max()
                .orElse(0) + 1;

        c.setId(nuevaId); // Asignar la nueva ID única

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            if (new File(FILE_NAME).length() > 0) {
                writer.newLine();  // Solo agrega nueva línea si el archivo no está vacío
            }
            writer.write(convertirAString(c));
            System.out.println("✅ Componente guardado con ID: " + nuevaId);
        } catch (IOException e) {
            System.out.println("❌ Error al guardar el componente.");
        }
    }

    // Nueva función para eliminar un componente
    public static void eliminarComponente(Component c) {
        ArrayList<Component> componentes = leerComponentes();
        int sizeAntes = componentes.size();

        // Filtrar componentes sin modificar directamente la lista
        List<Component> nuevaLista = new ArrayList<>();
        boolean eliminado = false;
        for (Component comp : componentes) {
            if (comp.getId() == c.getId() && !eliminado) {
                eliminado = true; // Marcar el componente como eliminado
            } else {
                nuevaLista.add(comp);
            }
        }

        if (!eliminado) {
            System.out.println("⚠ No se encontró el componente a eliminar.");
            return;
        }

        // Verificar si solo se eliminó uno
        int sizeDespues = nuevaLista.size();
        if (sizeAntes - sizeDespues != 1) {
            System.out.println("⚠ Se eliminó más de un componente, revisa la lectura de datos.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (Component comp : nuevaLista) {
                writer.write(convertirAString(comp));
                writer.newLine();
            }
            System.out.println("✅ Componente eliminado correctamente.");
        } catch (IOException e) {
            System.out.println("❌ Error al escribir el archivo.");
            e.printStackTrace();
        }
    }




    // Convertir objeto a String para guardarlo en el archivo
    private static String convertirAString(Component c) {
        if (c instanceof Processador p) {
            return String.format("Processador;%d;%s;%s;%.2f;%d;%.1f;%d;%s",
                    p.getId(), p.getNom(), p.getMarca(), p.getPreu(), p.getStock(),
                    p.getFrequencia(), p.getNuclis(), p.getSocket());
        } else if (c instanceof TargetaGrafica t) {
            return String.format("TargetaGrafica;%d;%s;%s;%.2f;%d;%d;%.1f;%d",
                    t.getId(), t.getNom(), t.getMarca(), t.getPreu(), t.getStock(),
                    t.getMemoriaVRAM(), t.getFrequencia(), t.getConsum());
        } else if (c instanceof DiscDur d) {
            return String.format("DiscDur;%d;%s;%s;%.2f;%d;%d;%s",
                    d.getId(), d.getNom(), d.getMarca(), d.getPreu(), d.getStock(),
                    d.getCapacitat(), d.getTipus());
        } else if (c instanceof FontAlimentacio f) {
            return String.format("FontAlimentacio;%d;%s;%s;%.2f;%d;%d;%s;%b",
                    f.getId(), f.getNom(), f.getMarca(), f.getPreu(), f.getStock(),
                    f.getPotencia(), f.getCertificacion(), f.isModular());
        }
        return "";
    }

    // Convertir una línea de texto en un objeto Component
    private static Component convertirAComponente(String linea) {
        String[] datos = linea.split(";");
        System.out.println("🔍 Procesando: " + linea); // Debug

        if (datos.length < 6) {
            System.out.println("❌ Datos insuficientes en línea: " + linea);
            return null;
        }

        try {
            String tipo = datos[0];
            int id = Integer.parseInt(datos[1]);
            String nombre = datos[2];
            String marca = datos[3];
            double precio = Double.parseDouble(datos[4].replace(",", "."));  // REEMPLAZAR COMA POR PUNTO
            int stock = Integer.parseInt(datos[5]);

            return switch (tipo) {
                case "Processador" -> new Processador(id, nombre, marca, precio, stock,
                        Double.parseDouble(datos[6].replace(",", ".")),
                        Integer.parseInt(datos[7]),
                        datos[8]);

                case "TargetaGrafica" -> new TargetaGrafica(id, nombre, marca, precio, stock,
                        Integer.parseInt(datos[6]),
                        Double.parseDouble(datos[7].replace(",", ".")),
                        Integer.parseInt(datos[8]));

                case "DiscDur" -> new DiscDur(id, nombre, marca, precio, stock,
                        Integer.parseInt(datos[6]),
                        datos[7]);
                case "FontAlimentacio" -> new FontAlimentacio(id, nombre, marca, precio, stock,
                        Integer.parseInt(datos[6]),  // Potencia (W)
                        datos[7],                   // Certificación (ej. 80 Plus Gold)
                        Boolean.parseBoolean(datos[8])); // Si es modular o no

                default -> {
                    System.out.println("⚠ Tipo desconocido: " + tipo);
                    yield null;
                }
            };
        } catch (Exception e) {
            System.out.println("❌ Error al leer componente: " + linea);
            e.printStackTrace();
            return null;
        }
    }

}
