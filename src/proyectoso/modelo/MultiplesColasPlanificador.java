/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class MultiplesColasPlanificador implements Planificador {
    private ColaPCB colaAltaPrioridad;  // Para IO_BOUND (procesos interactivos)
    private ColaPCB colaBajaPrioridad;  // Para CPU_BOUND (procesos batch)
    private int contadorAltaPrioridad;
    private int quantumAltaPrioridad;
    
    public MultiplesColasPlanificador() {
        this.colaAltaPrioridad = new ColaPCB();
        this.colaBajaPrioridad = new ColaPCB();
        this.contadorAltaPrioridad = 0;
        this.quantumAltaPrioridad = 3; // Quantum para alta prioridad
    }
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListosExterna) {
        // PRIMERO: Distribuir procesos de la cola externa en nuestras colas internas
        distribuirProcesos(colaListosExterna);
        
        // SEGUNDO: Lógica de selección con múltiples colas
        
        // 1. Prioridad a cola de alta prioridad (IO_BOUND)
        if (!colaAltaPrioridad.estaVacia()) {
            contadorAltaPrioridad++;
            PCB seleccionado = colaAltaPrioridad.getPrimero();
            
            // Cada cierto número de procesos de alta prioridad, dar chance a baja prioridad
            if (contadorAltaPrioridad >= quantumAltaPrioridad && !colaBajaPrioridad.estaVacia()) {
                contadorAltaPrioridad = 0;
                return colaBajaPrioridad.getPrimero();
            }
            
            return seleccionado;
        }
        
        // 2. Si no hay en alta prioridad, usar baja prioridad
        if (!colaBajaPrioridad.estaVacia()) {
            return colaBajaPrioridad.getPrimero();
        }
        
        return null;
    }
    
    /**
     * Distribuye los procesos de la cola externa en las colas internas apropiadas
     */
    private void distribuirProcesos(ColaPCB colaListosExterna) {
        if (colaListosExterna == null || colaListosExterna.estaVacia()) {
            return;
        }
        
        // Convertir a array para iterar sin modificar la cola original
        PCB[] procesosExternos = colaListosExterna.toArray();
        
        for (PCB pcb : procesosExternos) {
            // Si el proceso no está en ninguna de nuestras colas, agregarlo
            if (!estaEnNuestrasColas(pcb)) {
                if (pcb.getTipo() == TipoProceso.IO_BOUND) {
                    colaAltaPrioridad.agregar(pcb);
                } else {
                    colaBajaPrioridad.agregar(pcb);
                }
            }
        }
    }
    
    /**
     * Verifica si un proceso ya está en alguna de nuestras colas internas
     */
    private boolean estaEnNuestrasColas(PCB pcb) {
        PCB[] alta = colaAltaPrioridad.toArray();
        PCB[] baja = colaBajaPrioridad.toArray();
        
        for (PCB p : alta) {
            if (p == pcb) return true;
        }
        for (PCB p : baja) {
            if (p == pcb) return true;
        }
        return false;
    }
    
    /**
     * Remueve un proceso de nuestras colas internas (cuando termina o se bloquea)
     */
    public void removerProceso(PCB pcb) {
        colaAltaPrioridad.removerPCB(pcb);
        colaBajaPrioridad.removerPCB(pcb);
    }
    
    @Override
    public String getNombre() {
        return "Múltiples Colas (IO_BOUND → CPU_BOUND)";
    }
    
    // Getters para la interfaz gráfica
    public ColaPCB getColaAltaPrioridad() { 
        return colaAltaPrioridad; 
    }
    
    public ColaPCB getColaBajaPrioridad() { 
        return colaBajaPrioridad; 
    }
    
    public int getContadorAltaPrioridad() {
        return contadorAltaPrioridad;
    }
}