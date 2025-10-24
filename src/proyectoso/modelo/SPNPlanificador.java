/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class SPNPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) {
        // ... (verificaciones de cola vacía) ...

        PCB[] procesos = colaListos.toArray();
        PCB seleccionado = procesos[0];

        // Busca el proceso con el menor tiempo TOTAL de servicio
        for (PCB pcb : procesos) {
            if (pcb.getTotalInstrucciones() < seleccionado.getTotalInstrucciones()) { // 👈 CRITERIO CLAVE
                seleccionado = pcb;
            }
        }
        return seleccionado;
    }

    @Override
    public String getNombre() {
        return "Shortest Process Next (SPN)";
    }
}