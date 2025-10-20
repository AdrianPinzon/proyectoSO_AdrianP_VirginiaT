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
        try {
            // Simular el tiempo de satisfacción de la E/S
            for (int i = 0; i < ciclosSatisfaccion && ejecutando; i++) {
                Thread.sleep(1000); // 1 segundo por ciclo de E/S
                
                // Actualizar contador de ciclos restantes
                proceso.setCiclosEsperaES(ciclosSatisfaccion - i - 1);
            }
            
            if (ejecutando) {
                // E/S completada, restaurar proceso a listo
                semaforo.acquire();
                if (proceso.getEstado() == Estado.BLOQUEADO) {
                    proceso.setEstado(Estado.LISTO);
                }
                semaforo.release();
            }
            
        } catch (InterruptedException e) {
            System.out.println("Hilo de excepción interrumpido para: " + proceso.getNombre());
        }
    }
    
    public void cancelar() {
        ejecutando = false;
        interrupt();
    }
}