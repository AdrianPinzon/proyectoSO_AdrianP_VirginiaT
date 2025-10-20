/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.util;

public class Metricas {
    private double throughput;
    private double utilizacionCPU;
    private double tiempoRespuestaPromedio;
    private int procesosCompletados;
    private int ciclosTotales;
    
    // Getters y Setters
    public double getThroughput() { return throughput; }
    public void setThroughput(double throughput) { this.throughput = throughput; }
    
    public double getUtilizacionCPU() { return utilizacionCPU; }
    public void setUtilizacionCPU(double utilizacionCPU) { this.utilizacionCPU = utilizacionCPU; }
    
    public double getTiempoRespuestaPromedio() { return tiempoRespuestaPromedio; }
    public void setTiempoRespuestaPromedio(double tiempoRespuestaPromedio) { this.tiempoRespuestaPromedio = tiempoRespuestaPromedio; }
    
    public int getProcesosCompletados() { return procesosCompletados; }
    public void setProcesosCompletados(int procesosCompletados) { this.procesosCompletados = procesosCompletados; }
    
    public int getCiclosTotales() { return ciclosTotales; }
    public void setCiclosTotales(int ciclosTotales) { this.ciclosTotales = ciclosTotales; }
}
