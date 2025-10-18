/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SJFPlanificador implements Planificador {
    
    @Override
    public Proceso seleccionarSiguiente(List<Proceso> colaListos) {
        if (colaListos == null || colaListos.isEmpty()) {
            return null;
        }
        
        // SJF: Seleccionar el proceso con menor tiempo restante
        Proceso menorProceso = null;
        int menorInstrucciones = Integer.MAX_VALUE;
        
        for (Proceso proceso : colaListos) {
            int instruccionesRestantes = proceso.getInstruccionesRestantes();
            if (instruccionesRestantes < menorInstrucciones) {
                menorInstrucciones = instruccionesRestantes;
                menorProceso = proceso;
            }
        }
        
        return menorProceso;
    }
    
    @Override
    public String getNombre() {
        return "Shortest Job First (SJF)";
    }
}
