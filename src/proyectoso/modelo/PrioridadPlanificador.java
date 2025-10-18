/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.List;

public class PrioridadPlanificador implements Planificador {
    // Clase interna para manejar prioridades
    public static class ProcesoConPrioridad {
        public Proceso proceso;
        public int prioridad;
        
        public ProcesoConPrioridad(Proceso proceso, int prioridad) {
            this.proceso = proceso;
            this.prioridad = prioridad;
        }
    }
    
    @Override
    public Proceso seleccionarSiguiente(List<Proceso> colaListos) {
        if (colaListos == null || colaListos.isEmpty()) {
            return null;
        }
        
        // Prioridad: Seleccionar el proceso con mayor prioridad (número menor = mayor prioridad)
        // Por simplicidad, usamos el ID como prioridad (P1 > P2 > P3)
        Proceso mayorPrioridad = colaListos.get(0);
        
        for (Proceso proceso : colaListos) {
            if (esMayorPrioridad(proceso, mayorPrioridad)) {
                mayorPrioridad = proceso;
            }
        }
        
        return mayorPrioridad;
    }
    
    private boolean esMayorPrioridad(Proceso p1, Proceso p2) {
        // Comparar por ID (P1 tiene mayor prioridad que P2)
        return p1.getId().compareTo(p2.getId()) < 0;
    }
    
    @Override
    public String getNombre() {
        return "Planificador por Prioridad";
    }
}
