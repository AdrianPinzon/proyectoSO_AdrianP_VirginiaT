/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorColas {
    private List<Proceso> colaListos;
    private List<Proceso> colaBloqueados;
    private List<Proceso> colaSuspendidos;
    private List<Proceso> procesosTerminados;
    
    public GestorColas() {
        this.colaListos = new ArrayList<>();
        this.colaBloqueados = new ArrayList<>();
        this.colaSuspendidos = new ArrayList<>();
        this.procesosTerminados = new ArrayList<>();
    }
    
    // Métodos para agregar procesos
    public void agregarProceso(Proceso proceso) {
        proceso.setEstado(Estado.LISTO);
        colaListos.add(proceso);
    }
    
    public void bloquearProceso(Proceso proceso) {
        proceso.setEstado(Estado.BLOQUEADO);
        colaListos.remove(proceso);
        colaBloqueados.add(proceso);
    }
    
    public void suspenderProceso(Proceso proceso) {
        proceso.setEstado(Estado.SUSPENDIDO);
        // Remover de donde esté actualmente
        colaListos.remove(proceso);
        colaBloqueados.remove(proceso);
        colaSuspendidos.add(proceso);
    }
    
    public void terminarProceso(Proceso proceso) {
        proceso.setEstado(Estado.TERMINADO);
        procesosTerminados.add(proceso);
    }
    
    // Métodos para obtener procesos
    public Proceso obtenerSiguienteListo() {
        if (colaListos.isEmpty()) {
            return null;
        }
        return colaListos.get(0);
    }
    
    public Proceso removerSiguienteListo() {
        if (colaListos.isEmpty()) {
            return null;
        }
        return colaListos.remove(0);
    }
    
    // Getters para las colas
    public List<Proceso> getColaListos() { return new ArrayList<>(colaListos); }
    public List<Proceso> getColaBloqueados() { return new ArrayList<>(colaBloqueados); }
    public List<Proceso> getColaSuspendidos() { return new ArrayList<>(colaSuspendidos); }
    public List<Proceso> getProcesosTerminados() { return new ArrayList<>(procesosTerminados); }
    
    // Métodos de información
    public int getTotalProcesos() {
        return colaListos.size() + colaBloqueados.size() + colaSuspendidos.size() + procesosTerminados.size();
    }
    
    public boolean hayProcesosListos() {
        return !colaListos.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Colas - Listos: " + colaListos.size() + 
               ", Bloqueados: " + colaBloqueados.size() + 
               ", Suspendidos: " + colaSuspendidos.size() +
               ", Terminados: " + procesosTerminados.size();
    }
}
