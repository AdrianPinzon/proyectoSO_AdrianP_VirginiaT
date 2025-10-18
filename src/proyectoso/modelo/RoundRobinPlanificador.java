/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.List;

public class RoundRobinPlanificador implements Planificador {
    private int quantum;
    private int contadorQuantum;
    
    public RoundRobinPlanificador() {
        this.quantum = 4; // Quantum por defecto: 4 ciclos
        this.contadorQuantum = 0;
    }
    
    public RoundRobinPlanificador(int quantum) {
        this.quantum = quantum;
        this.contadorQuantum = 0;
    }
    
    @Override
    public Proceso seleccionarSiguiente(List<Proceso> colaListos) {
        if (colaListos == null || colaListos.isEmpty()) {
            return null;
        }
        
        // Round Robin: Rotación circular con quantum
        if (contadorQuantum >= quantum || contadorQuantum == 0) {
            contadorQuantum = 0;
            // Mover el primer proceso al final (rotación)
            if (colaListos.size() > 1) {
                Proceso primero = colaListos.remove(0);
                colaListos.add(primero);
            }
        }
        
        contadorQuantum++;
        return colaListos.get(0);
    }
    
    @Override
    public String getNombre() {
        return "Round Robin (Quantum: " + quantum + ")";
    }
    
    public void setQuantum(int quantum) {
        this.quantum = quantum;
        this.contadorQuantum = 0;
    }
    
    public int getQuantum() {
        return quantum;
    }
}