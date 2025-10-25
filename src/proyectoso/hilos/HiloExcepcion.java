/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.hilos;

import proyectoso.modelo.PCB;
import proyectoso.modelo.GestorColas;
import proyectoso.modelo.Estado;

/*
Representa un hilo auxiliar encargado de simular el tiempo que un proceso pasa
esperando que se complete una operación de E/S o una interrupción.
Este hilo saca el proceso de la cola de espera y una vez finalizado el tiempo de
simulado, notifica para que el proceso regrese a la cola de listos.
*/

public class HiloExcepcion extends Thread {
    private PCB proceso;
    private int ciclosSatisfaccion;
    private GestorColas gestorColas; // 👈 Referencia al coordinador
    private volatile boolean ejecutando;
    
    public HiloExcepcion(PCB proceso, int ciclosSatisfaccion, GestorColas gestorColas) {
        this.proceso = proceso;
        this.ciclosSatisfaccion = ciclosSatisfaccion;
        this.gestorColas = gestorColas; // 👈 Referencia
        this.ejecutando = true;
    }
    
    @Override
    public void run() {
        try {
            // SIMULACIÓN DE TIEMPO DE E/S
            // 100ms por ciclo de satisfacción como ejemplo.
            long tiempoEsperaMs = (long) ciclosSatisfaccion * 100;
            
            Thread.sleep(tiempoEsperaMs); 

            if (ejecutando) {
                // E/S TERMINADA: Notificar al GestorColas para reanudar.
                // Esta llamada es segura porque reanudarProceso usa ColaPCB (que ahora tiene semáforos).
                gestorColas.reanudarProceso(proceso);
            }
        } catch (InterruptedException e) {
            // El hilo fue interrumpido (ej. al detener la simulación)
            Thread.currentThread().interrupt();
        }
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
