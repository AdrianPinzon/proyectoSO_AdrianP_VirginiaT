/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.hilos;

import proyectoso.modelo.*;
import java.util.concurrent.Semaphore;

public class HiloSimulador extends Thread {
    private final GestorColas gestorColas;
    private final Semaphore semaforoCPU;
    private volatile boolean ejecutando;
    private int duracionCicloMs;
    private int cicloGlobal;
    private Proceso procesoEnEjecucion;
    
    public HiloSimulador(GestorColas gestorColas) {
        this.gestorColas = gestorColas;
        this.semaforoCPU = new Semaphore(1); // Solo 1 proceso en CPU
        this.ejecutando = false;
        this.duracionCicloMs = 1000; // 1 segundo por defecto
        this.cicloGlobal = 0;
        this.procesoEnEjecucion = null;
    }
    
    @Override
    public void run() {
        ejecutando = true;
        System.out.println("🔄 Hilo simulador iniciado");
        
        while (ejecutando) {
            try {
                // Esperar el tiempo del ciclo
                Thread.sleep(duracionCicloMs);
                
                // Ejecutar un ciclo de simulación
                ejecutarCiclo();
                
                cicloGlobal++;
                
            } catch (InterruptedException e) {
                System.out.println("⏸️ Hilo simulador interrumpido");
                ejecutando = false;
            }
        }
        System.out.println("🛑 Hilo simulador terminado");
    }
    
    private void ejecutarCiclo() {
        try {
            semaforoCPU.acquire(); // Adquirir acceso a la CPU
            
            if (procesoEnEjecucion == null && gestorColas.hayProcesosListos()) {
                // Seleccionar siguiente proceso (por ahora FCFS simple)
                procesoEnEjecucion = gestorColas.removerSiguienteListo();
                if (procesoEnEjecucion != null) {
                    procesoEnEjecucion.setEstado(Estado.EJECUCION);
                    System.out.println("🎯 CPU ejecutando: " + procesoEnEjecucion.getNombre());
                }
            }
            
            if (procesoEnEjecucion != null) {
                // Ejecutar instrucción del proceso actual
                ejecutarInstruccion(procesoEnEjecucion);
            }
            
        } catch (InterruptedException e) {
            System.out.println("❌ Error en semáforo: " + e.getMessage());
        } finally {
            semaforoCPU.release(); // Liberar CPU
        }
    }
    
    private void ejecutarInstruccion(Proceso proceso) {
        proceso.incrementarInstrucciones();
        System.out.println("📊 " + proceso.getNombre() + " - Instrucción " + 
                          proceso.getInstruccionesEjecutadas() + "/" + 
                          proceso.getTotalInstrucciones());
        
        // Verificar si el proceso terminó
        if (proceso.estaTerminado()) {
            proceso.setEstado(Estado.TERMINADO);
            gestorColas.terminarProceso(proceso);
            System.out.println("✅ " + proceso.getNombre() + " TERMINADO");
            procesoEnEjecucion = null;
        }
    }
    
    // Getters y Setters
    public void detener() {
        this.ejecutando = false;
        this.interrupt();
    }
    
    public boolean estaEjecutando() {
        return ejecutando;
    }
    
    public void setDuracionCicloMs(int duracion) {
        this.duracionCicloMs = duracion;
    }
    
    public int getCicloGlobal() {
        return cicloGlobal;
    }
    
    public Proceso getProcesoEnEjecucion() {
        return procesoEnEjecucion;
    }
    
    public GestorColas getGestorColas() {
        return gestorColas;
    }
}
