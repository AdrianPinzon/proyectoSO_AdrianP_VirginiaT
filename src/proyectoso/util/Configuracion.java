/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.util;

/*
 * Clase estática que almacena y gestiona los parámetros globales de la simulación
 * Permite configurar la velocidad de la simulación, el quantum de tiempo 
 * para Round Robin y el comportamiento por defecto de las excepciones de E/S.
*/

public class Configuracion {
    private int duracionCicloMs;
    private int quantum;
    private int ciclosExcepcion;
    private int ciclosSatisfaccion;
    
    public Configuracion() {
        this.duracionCicloMs = 1000; // 1 segundo por ciclo
        this.quantum = 3; // Quantum de 3 ciclos
        this.ciclosExcepcion = 5; // E/S cada 5 instrucciones
        this.ciclosSatisfaccion = 3; // E/S dura 3 ciclos
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
}
