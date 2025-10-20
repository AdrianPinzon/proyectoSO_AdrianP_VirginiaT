/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class SRTNPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos) {
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }
        
        // SRTN: Selecciona el proceso con menor tiempo restante
        PCB[] procesos = colaListos.toArray();
        PCB seleccionado = procesos[0];
        
        for (PCB pcb : procesos) {
            if (pcb.getInstruccionesRestantes() < seleccionado.getInstruccionesRestantes()) {
                seleccionado = pcb;
            }
        }
        
        return seleccionado;
    }
    
    @Override
    public String getNombre() {
        return "Shortest Remaining Time Next (SRTN)";
    }
}
