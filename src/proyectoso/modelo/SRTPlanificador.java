/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

/*
Planificación SRT, Preexpropiable.
 * Selecciona el proceso que tiene el menor tiempo de ejecución restante.
 * Un proceso en ejecución puede ser desalojado por un proceso recién llegado
 * si este último tiene un tiempo restante más corto.
*/

public class SRTPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) {
        // ... (verificaciones de cola vacía) ...

        PCB[] procesos = colaListos.toArray();
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
