/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class FCFSPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos) {
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }
        // FCFS: Simplemente el primero en la cola
        return colaListos.getPrimero();
    }
    
    @Override
    public String getNombre() {
        return "First-Come, First-Served (FCFS)";
    }
}