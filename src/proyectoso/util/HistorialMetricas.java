/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.util;

/*
 * Almacena el historial de las métricas clave de la simulación (Throughput, 
 * Utilización de CPU y Tiempo de Respuesta Promedio)
 * Permite almacenar un número fijo de las últimas mediciones para su 
 * visualización en gráficas de tendencia sin agotar la memoria.
*/

public class HistorialMetricas {
    private double[] throughput;
    private double[] utilizacion;
    private double[] tiempoRespuesta;
    private int tamaño;
    private int contador;
    private int capacidad;
    
    public HistorialMetricas(int capacidad) {
        this.capacidad = capacidad;
        this.throughput = new double[capacidad];
        this.utilizacion = new double[capacidad];
        this.tiempoRespuesta = new double[capacidad];
        this.tamaño = 0;
        this.contador = 0;
    }
    
    public void agregar(double throughput, double utilizacion, double tiempoRespuesta) {
        // Usar array circular
        this.throughput[contador] = throughput;
        this.utilizacion[contador] = utilizacion;
        this.tiempoRespuesta[contador] = tiempoRespuesta;
        
        contador = (contador + 1) % capacidad;
        if (tamaño < capacidad) {
            tamaño++;
        }
    }
    
    public double getThroughput(int index) {
        if (index < 0 || index >= tamaño) return 0;
        int posReal = (contador - tamaño + index + capacidad) % capacidad;
        return throughput[posReal];
    }
    
    public double getUtilizacion(int index) {
        if (index < 0 || index >= tamaño) return 0;
        int posReal = (contador - tamaño + index + capacidad) % capacidad;
        return utilizacion[posReal];
    }
    
    public double getTiempoRespuesta(int index) {
        if (index < 0 || index >= tamaño) return 0;
        int posReal = (contador - tamaño + index + capacidad) % capacidad;
        return tiempoRespuesta[posReal];
    }
    
    public int getTamaño() {
        return tamaño;
    }
    
    public int getCapacidad() {
        return capacidad;
    }
    
    public void limpiar() {
        this.throughput = new double[capacidad];
        this.utilizacion = new double[capacidad];
        this.tiempoRespuesta = new double[capacidad];
        this.tamaño = 0;
        this.contador = 0;
    }
    
    public double getMaxThroughput() {
        return getMaxValor(throughput, tamaño);
    }
    
    public double getMaxUtilizacion() {
        return getMaxValor(utilizacion, tamaño);
    }
    
    public double getMaxTiempoRespuesta() {
        return getMaxValor(tiempoRespuesta, tamaño);
    }
    
    private double getMaxValor(double[] array, int elementos) {
        double max = 0;
        for (int i = 0; i < elementos; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max == 0 ? 1.0 : max;
    }
}
