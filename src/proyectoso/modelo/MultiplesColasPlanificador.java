/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class MultiplesColasPlanificador implements Planificador {
    private ColaPCB colaAltaPrioridad;  // Para procesos interactivos (IO_BOUND)
    private ColaPCB colaBajaPrioridad;  // Para procesos batch (CPU_BOUND)
    private int contadorAltaPrioridad;
    
    public MultiplesColasPlanificador() {
        this.colaAltaPrioridad = new ColaPCB();
        this.colaBajaPrioridad = new ColaPCB();
        this.contadorAltaPrioridad = 0;
    }
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos) {
        // En múltiples colas, ignoramos la cola de entrada y usamos nuestras propias colas
        // Primero verificar cola de alta prioridad
        if (!colaAltaPrioridad.estaVacia()) {
            contadorAltaPrioridad++;
            PCB seleccionado = colaAltaPrioridad.getPrimero();
            
            // Cada 3 procesos de alta prioridad, dar chance a baja prioridad
            if (contadorAltaPrioridad >= 3 && !colaBajaPrioridad.estaVacia()) {
                contadorAltaPrioridad = 0;
                return colaBajaPrioridad.getPrimero();
            }
            
            return seleccionado;
        }
        
        // Si no hay en alta prioridad, usar baja prioridad
        if (!colaBajaPrioridad.estaVacia()) {
            return colaBajaPrioridad.getPrimero();
        }
        
        return null;
    }
    
    // Método para agregar procesos a las colas apropiadas
    public void agregarProceso(PCB pcb) {
        if (pcb.getTipo() == TipoProceso.IO_BOUND) {
            colaAltaPrioridad.agregar(pcb);
        } else {
            colaBajaPrioridad.agregar(pcb);
        }
    }
    
    // Método para remover proceso de cualquier cola
    public void removerProceso(PCB pcb) {
        if (colaAltaPrioridad.contiene(pcb)) {
            colaAltaPrioridad.removerPCB(pcb);
        } else if (colaBajaPrioridad.contiene(pcb)) {
            colaBajaPrioridad.removerPCB(pcb);
        }
    }
    
    @Override
    public String getNombre() {
        return "Múltiples Colas (IO_BOUND → CPU_BOUND)";
    }
    
    // Getters para la interfaz (mostrar ambas colas)
    public ColaPCB getColaAltaPrioridad() { return colaAltaPrioridad; }
    public ColaPCB getColaBajaPrioridad() { return colaBajaPrioridad; }
}