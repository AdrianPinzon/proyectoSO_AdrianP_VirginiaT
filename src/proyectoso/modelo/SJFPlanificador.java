/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class SJFPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos) {
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }
        
        // SJF: Selecciona el proceso con menos instrucciones totales
        PCB[] procesos = colaListos.toArray();
        PCB seleccionado = procesos[0];
        
        for (PCB pcb : procesos) {
            if (pcb.getTotalInstrucciones() < seleccionado.getTotalInstrucciones()) {
                seleccionado = pcb;
            }
        }
        
        return seleccionado;
    }
    
    @Override
    public String getNombre() {
        return "Shortest Job First (SJF)";
    }
}