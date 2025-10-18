/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.List;

public class FCFSPlanificador implements Planificador {
    
    @Override
    public Proceso seleccionarSiguiente(List<Proceso> colaListos) {
        if (colaListos == null || colaListos.isEmpty()) {
            return null;
        }
        // FCFS: Simplemente el primero en la cola
        return colaListos.get(0);
    }
    
    @Override
    public String getNombre() {
        return "First-Come, First-Served (FCFS)";
    }
}
