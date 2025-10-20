/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class ColaPCB {
    private NodoPCB primero;
    private NodoPCB ultimo;
    private int tamaño;
    
    // Clase interna para los nodos
    private static class NodoPCB {
        PCB pcb;
        NodoPCB siguiente;
        
        NodoPCB(PCB pcb) {
            this.pcb = pcb;
            this.siguiente = null;
        }
    }
    
    public ColaPCB() {
        this.primero = null;
        this.ultimo = null;
        this.tamaño = 0;
    }
    
    // MÉTODOS BÁSICOS DE COLA
    
    /**
     * Agrega un PCB al final de la cola (enqueue)
     */
    public void agregar(PCB pcb) {
        NodoPCB nuevoNodo = new NodoPCB(pcb);
        
        if (ultimo != null) {
            ultimo.siguiente = nuevoNodo;
        }
        ultimo = nuevoNodo;
        
        if (primero == null) {
            primero = nuevoNodo;
        }
        
        tamaño++;
    }
    
    /**
     * Remueve y retorna el primer PCB de la cola (dequeue)
     */
    public PCB remover() {
        if (primero == null) {
            return null;
        }
        
        PCB pcb = primero.pcb;
        primero = primero.siguiente;
        
        if (primero == null) {
            ultimo = null;
        }
        
        tamaño--;
        return pcb;
    }
    
    /**
     * Retorna el primer PCB sin removerlo (peek)
     */
    public PCB getPrimero() {
        return primero != null ? primero.pcb : null;
    }
    
    /**
     * Verifica si la cola está vacía
     */
    public boolean estaVacia() {
        return primero == null;
    }
    
    /**
     * Retorna el tamaño de la cola
     */
    public int getTamaño() {
        return tamaño;
    }
    
    // MÉTODOS ADICIONALES ÚTILES PARA EL SIMULADOR
    
    /**
     * Busca un PCB por su ID
     */
    public PCB buscarPorId(String id) {
        NodoPCB actual = primero;
        while (actual != null) {
            if (actual.pcb.getId().equals(id)) {
                return actual.pcb;
            }
            actual = actual.siguiente;
        }
        return null;
    }
    
    /**
     * Remueve un PCB específico de la cola por referencia
     */
    public boolean removerPCB(PCB pcb) {
        if (primero == null) return false;
        
        // Caso especial: si es el primero
        if (primero.pcb == pcb) {
            remover();
            return true;
        }
        
        // Buscar el nodo anterior al que contiene el PCB
        NodoPCB actual = primero;
        while (actual.siguiente != null && actual.siguiente.pcb != pcb) {
            actual = actual.siguiente;
        }
        
        if (actual.siguiente != null) {
            // Encontrado, saltar este nodo
            actual.siguiente = actual.siguiente.siguiente;
            
            // Actualizar último si necesario
            if (actual.siguiente == null) {
                ultimo = actual;
            }
            
            tamaño--;
            return true;
        }
        
        return false;
    }
    
    /**
     * Convierte la cola a array para facilitar la iteración en la interfaz
     */
    public PCB[] toArray() {
        PCB[] array = new PCB[tamaño];
        NodoPCB actual = primero;
        int index = 0;
        
        while (actual != null) {
            array[index++] = actual.pcb;
            actual = actual.siguiente;
        }
        
        return array;
    }
    
    /**
     * Limpia toda la cola
     */
    public void limpiar() {
        primero = null;
        ultimo = null;
        tamaño = 0;
    }
    
    /**
     * Verifica si la cola contiene un PCB específico
     */
    public boolean contiene(PCB pcb) {
        NodoPCB actual = primero;
        while (actual != null) {
            if (actual.pcb == pcb) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
    
    @Override
    public String toString() {
        if (estaVacia()) {
            return "ColaPCB[vacía]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("ColaPCB[");
        NodoPCB actual = primero;
        while (actual != null) {
            sb.append(actual.pcb.getNombre());
            if (actual.siguiente != null) {
                sb.append(" -> ");
            }
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}
