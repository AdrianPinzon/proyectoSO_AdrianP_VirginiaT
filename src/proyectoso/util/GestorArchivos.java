/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.util;

import proyectoso.modelo.ColaPCB;
import proyectoso.modelo.PCB;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class GestorArchivos {
    private static final String ARCHIVO_CONFIG = "configuracion.csv";
    
    public void guardarConfiguracion(Configuracion config) {
        try (FileWriter fw = new FileWriter(ARCHIVO_CONFIG);
             PrintWriter pw = new PrintWriter(fw)) {
            
            // Escribir encabezados
            pw.println("parametro,valor");
            
            // Escribir configuración
            pw.println("duracionCicloMs," + config.getDuracionCicloMs());
            pw.println("quantum," + config.getQuantum());
            pw.println("ciclosExcepcion," + config.getCiclosExcepcion());
            pw.println("ciclosSatisfaccion," + config.getCiclosSatisfaccion());
            pw.println("numProcesadores," + config.getNumProcesadores());
            
        } catch (Exception e) {
            throw new RuntimeException("Error guardando configuración: " + e.getMessage());
        }
    }
    
    public Configuracion cargarConfiguracion() {
        try (FileReader fr = new FileReader(ARCHIVO_CONFIG);
             BufferedReader br = new BufferedReader(fr)) {
            
            Configuracion config = new Configuracion();
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar encabezado
                }
                
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String parametro = partes[0].trim();
                    String valor = partes[1].trim();
                    
                    switch (parametro) {
                        case "duracionCicloMs":
                            config.setDuracionCicloMs(Integer.parseInt(valor));
                            break;
                        case "quantum":
                            config.setQuantum(Integer.parseInt(valor));
                            break;
                        case "ciclosExcepcion":
                            config.setCiclosExcepcion(Integer.parseInt(valor));
                            break;
                        case "ciclosSatisfaccion":
                            config.setCiclosSatisfaccion(Integer.parseInt(valor));
                            break;
                        case "numProcesadores":
                            config.setNumProcesadores(Integer.parseInt(valor));
                            break;    
                    }
                }
            }
            
            return config;
            
        } catch (Exception e) {
            // Si no existe el archivo, retornar null para usar valores por defecto
            return null;
        }
    }
    
    public void guardarMetricas(Metricas metricas, String nombreArchivo) {
        try (FileWriter fw = new FileWriter(nombreArchivo);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println("metrica,valor");
            pw.println("throughput," + metricas.getThroughput());
            pw.println("utilizacionCPU," + metricas.getUtilizacionCPU());
            pw.println("tiempoRespuestaPromedio," + metricas.getTiempoRespuestaPromedio());
            pw.println("procesosCompletados," + metricas.getProcesosCompletados());
            pw.println("ciclosTotales," + metricas.getCiclosTotales());
            
        } catch (Exception e) {
            throw new RuntimeException("Error guardando métricas: " + e.getMessage());
        }
    }
    
    public void guardarEstadoColas(ColaPCB[] colas, String[] nombres, String nombreArchivo) {
        try (FileWriter fw = new FileWriter(nombreArchivo);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println("cola,procesos");
            
            for (int i = 0; i < colas.length; i++) {
                StringBuilder procesos = new StringBuilder();
                if (colas[i] != null && !colas[i].estaVacia()) {
                    PCB[] arrayProcesos = colas[i].toArray();
                    for (int j = 0; j < arrayProcesos.length; j++) {
                        procesos.append(arrayProcesos[j].getNombre());
                        if (j < arrayProcesos.length - 1) {
                            procesos.append(";");
                        }
                    }
                } else {
                    procesos.append("vacía");
                }
                
                pw.println(nombres[i] + "," + procesos.toString());
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error guardando estado de colas: " + e.getMessage());
        }
    }
    
    public void exportarDatosSimulacion(Configuracion config, Metricas metricas, ColaPCB[] colas, String[] nombresColas) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        guardarConfiguracion(config);
        guardarMetricas(metricas, "metricas_" + timestamp + ".csv");
        guardarEstadoColas(colas, nombresColas, "estado_colas_" + timestamp + ".csv");
    }
}