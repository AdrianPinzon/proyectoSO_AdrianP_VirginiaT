/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class SRTPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) {
        
        if (colaListos == null || colaListos.estaVacia()) { 
        return null;
        }
        
        // ... (verificaciones de cola vacía) ...
        PCB[] procesos = colaListos.toArray();
        // 👈 VERIFICACIÓN CRÍTICA ADICIONAL
        if (procesos.length == 0) { 
            return null;
        }
        PCB seleccionado = procesos[0];

        // Busca el proceso con el menor tiempo RESTANTE de servicio
        for (PCB pcb : procesos) {
            if (pcb.getInstruccionesRestantes() < seleccionado.getInstruccionesRestantes()) { // 👈 CRITERIO CLAVE
                seleccionado = pcb;
            }
        }
        return seleccionado;
    }
    

    @Override
    public String getNombre() {
        return "Shortest Remaining Time (SRT)";
    }
}
