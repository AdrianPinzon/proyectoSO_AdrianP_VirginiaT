/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class RoundRobinPlanificador implements Planificador {
    private int quantum;
    private int contadorQuantum;
    
    public RoundRobinPlanificador(int quantum) {
        this.quantum = quantum;
        this.contadorQuantum = 0;
    }
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) {
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }
        
        // Round Robin: selecciona el primero
        PCB seleccionado = colaListos.getPrimero();
        contadorQuantum++;
        
        // Si se acabó el quantum, rotar la cola
        if (contadorQuantum >= quantum) {
            contadorQuantum = 0;
            if (colaListos.getTamaño() > 1) {
                PCB primero = colaListos.remover();
                colaListos.agregar(primero);
            }
        }
        
        return seleccionado;
    }
    
    @Override
    public String getNombre() {
        return "Round Robin (Quantum: " + quantum + ")";
    }
    
    public void setQuantum(int quantum) {
        this.quantum = quantum;
        this.contadorQuantum = 0;
    }
}