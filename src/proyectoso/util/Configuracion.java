/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.util;

public class Configuracion {
    private int duracionCicloMs;
    private int quantum;
    private int ciclosExcepcion;
    private int ciclosSatisfaccion;
    private int numProcesadores = 2;
    
    public Configuracion() {
        this.duracionCicloMs = 1000;
        this.quantum = 3;
        this.ciclosExcepcion = 5;
        this.ciclosSatisfaccion = 3;
    }
    
    // Getters y Setters
    public int getDuracionCicloMs() { return duracionCicloMs; }
    public void setDuracionCicloMs(int duracionCicloMs) { this.duracionCicloMs = duracionCicloMs; }
    
    public int getQuantum() { return quantum; }
    public void setQuantum(int quantum) { this.quantum = quantum; }
    
    public int getCiclosExcepcion() { return ciclosExcepcion; }
    public void setCiclosExcepcion(int ciclosExcepcion) { this.ciclosExcepcion = ciclosExcepcion; }
    
    public int getCiclosSatisfaccion() { return ciclosSatisfaccion; }
    public void setCiclosSatisfaccion(int ciclosSatisfaccion) { this.ciclosSatisfaccion = ciclosSatisfaccion; }
    
    public int getNumProcesadores() { return numProcesadores; }
    public void setNumProcesadores(int numProcesadores) { this.numProcesadores = numProcesadores; }
}