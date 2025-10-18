/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.List;
import java.util.ArrayList;

public class MultiplesColasPlanificador implements Planificador {
    private List<List<Proceso>> colas;
    private int[] quantums;
    
    public MultiplesColasPlanificador() {
        this.colas = new ArrayList<>();
        this.colas.add(new ArrayList<>()); // Cola 0: Alta prioridad
        this.colas.add(new ArrayList<>()); // Cola 1: Media prioridad  
        this.colas.add(new ArrayList<>()); // Cola 2: Baja prioridad
        
        this.quantums = new int[]{2, 4, 8}; // Quantums por cola
    }
    
    @Override
    public Proceso seleccionarSiguiente(List<Proceso> colaListos) {
        // Clasificar procesos en colas según su tipo
        clasificarProcesos(colaListos);
        
        // Buscar desde cola de mayor prioridad
        for (int i = 0; i < colas.size(); i++) {
            if (!colas.get(i).isEmpty()) {
                return colas.get(i).get(0);
            }
        }
        
        return null;
    }
    
    private void clasificarProcesos(List<Proceso> colaListos) {
        // Reiniciar colas
        for (List<Proceso> cola : colas) {
            cola.clear();
        }
        
        // Clasificar según tipo de proceso
        for (Proceso proceso : colaListos) {
            if (proceso.getTipo() == TipoProceso.IO_BOUND) {
                colas.get(0).add(proceso); // Alta prioridad para I/O
            } else if (proceso.getInstruccionesRestantes() <= 10) {
                colas.get(1).add(proceso); // Media prioridad para trabajos cortos
            } else {
                colas.get(2).add(proceso); // Baja prioridad para trabajos largos
            }
        }
    }
    
    @Override
    public String getNombre() {
        return "Planificador Múltiples Colas";
    }
}
