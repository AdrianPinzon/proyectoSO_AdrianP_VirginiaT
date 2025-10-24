/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

/*
 * Planificación FCFS
 * El proceso que llega primero a la cola de listos 
 * es el primero en ser seleccionado para su ejecución.
 * No Preexpropiable . Una vez que un proceso 
 * inicia su ejecución, solo puede ser desalojado si termina o si se bloquea 
 * por una operación de E/S.
*/

public class FCFSPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) { // 👈 AGREGAR int cicloActual
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }
        // Retorna el proceso que llegó primero sin removerlo
        return colaListos.getPrimero();
    }
    @Override
    public String getNombre() {
    return "First-Come, First-Served (FCFS)";
}
}
