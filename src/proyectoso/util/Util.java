/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.util;

public class Util {
    
    /**
     * Genera un ID único para procesos
     */
    public static String generarIdProceso(int secuencia) {
        return "P" + String.format("%03d", secuencia);
    }
    
    /**
     * Valida que un número esté en un rango específico
     */
    public static boolean validarRango(int valor, int min, int max) {
        return valor >= min && valor <= max;
    }
    
    /**
     * Formatea tiempo en milisegundos a string legible
     */
    public static String formatTiempo(long milisegundos) {
        if (milisegundos < 1000) {
            return milisegundos + "ms";
        } else if (milisegundos < 60000) {
            return (milisegundos / 1000) + "s";
        } else {
            return (milisegundos / 60000) + "m " + ((milisegundos % 60000) / 1000) + "s";
        }
    }
    
    /**
     * Calcula el porcentaje de un valor
     */
    public static double calcularPorcentaje(double parte, double total) {
        if (total == 0) return 0;
        return (parte / total) * 100;
    }
    
    /**
     * Limita un valor entre un mínimo y máximo
     */
    public static int clamp(int valor, int min, int max) {
        return Math.min(Math.max(valor, min), max);
    }
}
