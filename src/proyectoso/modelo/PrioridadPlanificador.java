/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class PrioridadPlanificador implements Planificador {
    private boolean prioridadMayorPrimero; // true: mayor prioridad primero
    
    public PrioridadPlanificador(boolean prioridadMayorPrimero) {
        this.prioridadMayorPrimero = prioridadMayorPrimero;
    }
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos) {
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }
        
        // Asignar prioridades (para este ejemplo: IO_BOUND tiene mayor prioridad)
        PCB[] procesos = colaListos.toArray();
        PCB seleccionado = procesos[0];
        
        for (PCB pcb : procesos) {
            int prioridadActual = calcularPrioridad(pcb);
            int prioridadSeleccionado = calcularPrioridad(seleccionado);
            
            if (prioridadMayorPrimero) {
                if (prioridadActual > prioridadSeleccionado) {
                    seleccionado = pcb;
                }
            } else {
                if (prioridadActual < prioridadSeleccionado) {
                    seleccionado = pcb;
                }
            }
        }
        
        return seleccionado;
    }
    
    private int calcularPrioridad(PCB pcb) {
        // Prioridad basada en tipo de proceso y instrucciones restantes
        int prioridad = 0;
        
        // IO_BOUND tiene mayor prioridad que CPU_BOUND
        if (pcb.getTipo() == TipoProceso.IO_BOUND) {
            prioridad += 10;
        }
        
        // Menos instrucciones restantes = mayor prioridad
        prioridad += (100 - pcb.getInstruccionesRestantes()) / 10;
        
        return prioridad;
    }
    
    @Override
    public String getNombre() {
        return "Planificación por Prioridad (" + 
               (prioridadMayorPrimero ? "Mayor Primero" : "Menor Primero") + ")";
    }
}
