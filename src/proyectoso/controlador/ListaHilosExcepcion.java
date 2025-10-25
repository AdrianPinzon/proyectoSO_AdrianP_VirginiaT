/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.controlador;

import proyectoso.hilos.HiloExcepcion;
import java.util.concurrent.Semaphore;

/*
Representa una lista enlazada simple y segura para hilos diseñada para almacenar y gestionar instancias.
Esta lista se utiliza para controlar hilos que representan operaciones asíncronas (como solicitudes de I/O)
y permite su manipulación segura en un entorno simulación concurrente gracias al uso de un semáforo.
*/

public class ListaHilosExcepcion {
    private NodoHilo primero;
    private NodoHilo ultimo;
    private int tamaño;
    private Semaphore semaforo;
    
    private static class NodoHilo {
        HiloExcepcion hilo;
        NodoHilo siguiente;
        
        NodoHilo(HiloExcepcion hilo) {
            this.hilo = hilo;
            this.siguiente = null;
        }
    }
    
    public ListaHilosExcepcion() {
        this.primero = null;
        this.ultimo = null;
        this.tamaño = 0;
        this.semaforo = new Semaphore(1);
    }
    
    public void agregar(HiloExcepcion hilo) {
        try {
            semaforo.acquire();
            
            NodoHilo nuevoNodo = new NodoHilo(hilo);
            
            if (ultimo != null) {
                ultimo.siguiente = nuevoNodo;
            }
            ultimo = nuevoNodo;
            
            if (primero == null) {
                primero = nuevoNodo;
            }
            
            tamaño++;
            semaforo.release();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void detenerTodos() {
        try {
            semaforo.acquire();
            
            NodoHilo actual = primero;
            while (actual != null) {
                actual.hilo.cancelar();
                actual = actual.siguiente;
            }
            
            // Limpiar la lista
            primero = null;
            ultimo = null;
            tamaño = 0;
            
            semaforo.release();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public int getTamaño() {
        return tamaño;
    }
    
    public boolean estaVacia() {
        return primero == null;
    }
    
    /**
     * Remueve y retorna el primer hilo de la lista
     */
    public HiloExcepcion removerPrimero() {
        try {
            semaforo.acquire();
            
            if (primero == null) {
                semaforo.release();
                return null;
            }
            
            HiloExcepcion hilo = primero.hilo;
            primero = primero.siguiente;
            
            if (primero == null) {
                ultimo = null;
            }
            
            tamaño--;
            semaforo.release();
            return hilo;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    /**
     * Busca un hilo por el nombre del proceso
     */
    public HiloExcepcion buscarPorProceso(String nombreProceso) {
        try {
            semaforo.acquire();
            
            NodoHilo actual = primero;
            while (actual != null) {
                if (actual.hilo != null && actual.hilo.getProceso() != null &&
                    actual.hilo.getProceso().getNombre().equals(nombreProceso)) {
                    semaforo.release();
                    return actual.hilo;
                }
                actual = actual.siguiente;
            }
            
            semaforo.release();
            return null;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
