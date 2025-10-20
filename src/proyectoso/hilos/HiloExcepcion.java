/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.hilos;

import proyectoso.modelo.PCB;
import proyectoso.modelo.Estado;
import java.util.concurrent.Semaphore;

public class HiloExcepcion extends Thread {
    private PCB proceso;
    private int ciclosSatisfaccion;
    private Semaphore semaforo;
    private volatile boolean ejecutando;
    
    public HiloExcepcion(PCB proceso, int ciclosSatisfaccion, Semaphore semaforo) {
        this.proceso = proceso;
        this.ciclosSatisfaccion = ciclosSatisfaccion;
        this.semaforo = semaforo;
        this.ejecutando = true;
    }
    
    @Override
    public void run() {
        // ... (código anterior se mantiene igual) ...
    }
    
    public void cancelar() {
        ejecutando = false;
        interrupt();
    }
    
    // GETTER para acceder al proceso desde ListaHilosExcepcion
    public PCB getProceso() {
        return proceso;
    }
}