/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class GestorColas {
    // COLAS PRINCIPALES (REQUERIDAS)
    private ColaPCB colaListos;
    private ColaPCB colaBloqueados;
    private ColaPCB colaTerminados;
    
    // COLAS SUSPENDIDAS (REQUERIDAS para gestión de memoria)
    private ColaPCB colaListosSuspendidos;
    private ColaPCB colaBloqueadosSuspendidos;
    
    // Cola de nuevos procesos (antes de ser admitidos)
    private ColaPCB colaNuevos;
    
    // Planificador actual
    private Planificador planificador;
    
    // Contadores para métricas
    private int cicloActual;
    private int procesosCompletados;
    
    public GestorColas() {
        this.colaListos = new ColaPCB();
        this.colaBloqueados = new ColaPCB();
        this.colaTerminados = new ColaPCB();
        this.colaListosSuspendidos = new ColaPCB();
        this.colaBloqueadosSuspendidos = new ColaPCB();
        this.colaNuevos = new ColaPCB();
        
        // Planificador por defecto
        this.planificador = new FCFSPlanificador();
        
        this.cicloActual = 0;
        this.procesosCompletados = 0;
    }
    
    // MÉTODOS PRINCIPALES DE GESTIÓN
    
    /**
     * Agrega un nuevo proceso al sistema
     */
    public void agregarProceso(PCB pcb) {
        pcb.setTiempoLlegada(cicloActual);
        pcb.setEstado(Estado.NUEVO);
        colaNuevos.agregar(pcb);
        
        // Admitir a cola de listos (política simple)
        admitirProcesos();
    }
    
    /**
     * Admite procesos de nuevos a listos (política de admisión)
     */
    private void admitirProcesos() {
        while (!colaNuevos.estaVacia()) {
            PCB pcb = colaNuevos.remover();
            pcb.setEstado(Estado.LISTO);
            colaListos.agregar(pcb);
        }
    }
    
    /**
     * Selecciona el siguiente proceso a ejecutar usando el planificador actual
     */
    public PCB seleccionarSiguiente() {
        PCB siguiente = planificador.seleccionarSiguiente(colaListos);
        
        if (siguiente != null) {
            // Lógica específica para múltiples colas
            manejarMultiplesColas(siguiente);
        
            // Remover de cola de listos
            colaListos.removerPCB(siguiente);
            siguiente.setEstado(Estado.EJECUCION);
            if (siguiente.getTiempoInicioEjecucion() == 0) {
                siguiente.setTiempoInicioEjecucion(cicloActual);
            }
        }
        
        return siguiente;
    }
    
    /**
     * Ejecuta un ciclo completo de simulación
     */
    public void ejecutarCiclo() {
        cicloActual++;
        
        // 1. Manejar procesos bloqueados
        manejarProcesosBloqueados();
        
        // 2. Manejar suspensión de procesos (gestión de memoria)
        manejarSuspensionProcesos();
        
        // 3. Re-admitir procesos si es necesario
        admitirProcesos();
    }
    
    /**
     * Maneja los procesos en cola de bloqueados
     */
    private void manejarProcesosBloqueados() {
        ColaPCB temp = new ColaPCB();
        
        while (!colaBloqueados.estaVacia()) {
            PCB pcb = colaBloqueados.remover();
            
            // Ejecutar ciclo (para reducir contadores de E/S)
            pcb.ejecutarCiclo();
            
            // Si sigue bloqueado, volver a la cola
            if (pcb.getEstado() == Estado.BLOQUEADO) {
                temp.agregar(pcb);
            } else if (pcb.getEstado() == Estado.LISTO) {
                // E/S completada, volver a listos
                colaListos.agregar(pcb);
            }
        }
        
        // Restaurar procesos que siguen bloqueados
        while (!temp.estaVacia()) {
            colaBloqueados.agregar(temp.remover());
        }
    }
    
    /**
     * Maneja la suspensión de procesos por memoria (REQUERIDO)
     */
    private void manejarSuspensionProcesos() {
        // SIMULACIÓN: Suspender procesos si hay muchos en memoria
        // En un sistema real, esto dependería de la memoria disponible
        
        // Política simple: si hay más de 5 procesos listos, suspender algunos
        if (colaListos.getTamaño() > 5) {
            PCB aSuspender = colaListos.remover();
            aSuspender.setEstado(Estado.SUSPENDIDO);
            aSuspender.setSuspendido(true);
            colaListosSuspendidos.agregar(aSuspender);
        }
        
        // Re-activar procesos suspendidos si hay pocos en memoria
        if (colaListos.getTamaño() < 3 && !colaListosSuspendidos.estaVacia()) {
            PCB aReactivar = colaListosSuspendidos.remover();
            aReactivar.setEstado(Estado.LISTO);
            aReactivar.setSuspendido(false);
            colaListos.agregar(aReactivar);
        }
    }
    
    /**
    * Maneja la lógica específica para múltiples colas
    */
    private void manejarMultiplesColas(PCB procesoEjecutando) {
        if (planificador instanceof MultiplesColasPlanificador) {
           MultiplesColasPlanificador multiColas = (MultiplesColasPlanificador) planificador;

           // Remover el proceso ejecutado de las colas internas
           if (procesoEjecutando != null) {
               multiColas.removerProceso(procesoEjecutando);
            }

           // Si el proceso terminó o se bloqueó, removerlo completamente
           if (procesoEjecutando != null && 
               (procesoEjecutando.estaTerminado() || procesoEjecutando.getEstado() == Estado.BLOQUEADO)) {
               multiColas.removerProceso(procesoEjecutando);
            }
        }
    }
    
    /**
     * Bloquea un proceso (por E/S)
     */
    public void bloquearProceso(PCB pcb) {
        pcb.setEstado(Estado.BLOQUEADO);
        colaBloqueados.agregar(pcb);
    }
    
    /**
     * Termina un proceso
     */
    public void terminarProceso(PCB pcb) {
        pcb.setEstado(Estado.TERMINADO);
        pcb.setTiempoFinalizacion(cicloActual);
        colaTerminados.agregar(pcb);
        procesosCompletados++;
    }
    
    /**
     * Reanuda un proceso previamente bloqueado
     */
    public void reanudarProceso(PCB pcb) {
        if (colaBloqueados.removerPCB(pcb)) {
            pcb.setEstado(Estado.LISTO);
            colaListos.agregar(pcb);
        }
    }
    
    // MÉTODOS PARA CAMBIO DE PLANIFICADOR (REQUERIDO)
    
    public void setPlanificador(Planificador planificador) {
        this.planificador = planificador;
    }
    
    public Planificador getPlanificador() {
        return planificador;
    }
    
    public String getNombrePlanificadorActual() {
        return planificador.getNombre();
    }
    
    // GETTERS PARA LA INTERFAZ GRÁFICA (REQUERIDOS)
    
    public ColaPCB getColaListos() { return colaListos; }
    public ColaPCB getColaBloqueados() { return colaBloqueados; }
    public ColaPCB getColaTerminados() { return colaTerminados; }
    public ColaPCB getColaListosSuspendidos() { return colaListosSuspendidos; }
    public ColaPCB getColaBloqueadosSuspendidos() { return colaBloqueadosSuspendidos; }
    public ColaPCB getColaNuevos() { return colaNuevos; }
    
    public int getCicloActual() { return cicloActual; }
    public int getProcesosCompletados() { return procesosCompletados; }
    
    // MÉTRICAS DE RENDIMIENTO (REQUERIDAS)
    
    public double getThroughput() {
        if (cicloActual == 0) return 0;
        return (double) procesosCompletados / cicloActual;
    }
    
    public double getUtilizacionCPU(PCB procesoEjecutando) {
        // Simulación: CPU está ocupada si hay proceso ejecutando
        return procesoEjecutando != null ? 100.0 : 0.0;
    }
    
    public double getTiempoRespuestaPromedio() {
        if (colaTerminados.estaVacia()) return 0;
        
        PCB[] terminados = colaTerminados.toArray();
        int suma = 0;
        
        for (PCB pcb : terminados) {
            suma += pcb.getTiempoRespuesta();
        }
        
        return (double) suma / terminados.length;
    }
    
    /**
     * Reinicia el gestor para nueva simulación
     */
    public void reiniciar() {
        colaListos.limpiar();
        colaBloqueados.limpiar();
        colaTerminados.limpiar();
        colaListosSuspendidos.limpiar();
        colaBloqueadosSuspendidos.limpiar();
        colaNuevos.limpiar();
        
        cicloActual = 0;
        procesosCompletados = 0;
    }
    
    @Override
    public String toString() {
        return String.format(
            "GestorColas[ Ciclo: %d, Listos: %d, Bloqueados: %d, Terminados: %d, Planificador: %s ]",
            cicloActual, colaListos.getTamaño(), colaBloqueados.getTamaño(), 
            colaTerminados.getTamaño(), planificador.getNombre()
        );
    }
}