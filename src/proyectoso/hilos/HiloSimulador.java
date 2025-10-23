/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package proyectoso.hilos;

import proyectoso.modelo.*;
import proyectoso.controlador.*;
import java.util.concurrent.Semaphore;

public class HiloSimulador extends Thread {
    private GestorColas gestorColas;
    private ControladorSimulador controlador;
    private volatile boolean ejecutando;
    private volatile boolean pausado;
    private int duracionCicloMs; // REQUERIDO: duración configurable del ciclo
    private PCB procesoEjecutando;
    private Semaphore semaforo;
    
    // Contadores para Round Robin
    private int contadorQuantum;
    private int quantumActual;
    
    public HiloSimulador(GestorColas gestorColas, ControladorSimulador controlador) {
        this.gestorColas = gestorColas;
        this.controlador = controlador;
        this.ejecutando = false;
        this.pausado = false;
        this.duracionCicloMs = 1000; // 1 segundo por defecto
        this.procesoEjecutando = null;
        this.semaforo = new Semaphore(1); // REQUERIDO: semáforo para exclusión mutua
        this.contadorQuantum = 0;
        this.quantumActual = 3; // Quantum por defecto para Round Robin
    }
    
    @Override
    public void run() {
        ejecutando = true;
        
        while (ejecutando) {
            try {
                if (!pausado) {
                    // EJECUTAR UN CICLO DE SIMULACIÓN
                    ejecutarCicloSimulacion();
                    
                    // Notificar al controlador para actualizar la interfaz
                    controlador.actualizarVista();
                }
                
                // Esperar según la duración del ciclo configurada
                Thread.sleep(duracionCicloMs);
                
            } catch (InterruptedException e) {
                System.out.println("Hilo de simulación interrumpido");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Ejecuta un ciclo completo de simulación
     */
    private void ejecutarCicloSimulacion() {
        try {
            semaforo.acquire(); // REQUERIDO: exclusión mutua
            
            // 1. Ejecutar ciclo del gestor de colas
            gestorColas.ejecutarCiclo();
            
            // 2. Manejar el proceso actual en ejecución
            manejarProcesoActual();
            
            // 3. Seleccionar nuevo proceso si es necesario
            if (procesoEjecutando == null || procesoEjecutando.getEstado() != Estado.EJECUCION) {
                seleccionarNuevoProceso();
            }
            
            semaforo.release();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Maneja el proceso actualmente en ejecución
     */
    private void manejarProcesoActual() {
        if (procesoEjecutando == null) return;

        // Verificar si el proceso terminó
        if (procesoEjecutando.estaTerminado()) {
            gestorColas.terminarProceso(procesoEjecutando);

            // Remover de colas de feedback si es FB
            if (gestorColas.getPlanificador() instanceof FBPlanificador) {
                ((FBPlanificador) gestorColas.getPlanificador()).removerProceso(procesoEjecutando);
            }

            procesoEjecutando = null;
            contadorQuantum = 0;
            return;
        }

        // Ejecutar un ciclo del proceso
        procesoEjecutando.ejecutarCiclo();

        // Verificar si generó excepción E/S
        if (procesoEjecutando.getEstado() == Estado.BLOQUEADO) {
            gestorColas.bloquearProceso(procesoEjecutando);

            // Remover de colas de feedback si es FB
            if (gestorColas.getPlanificador() instanceof FBPlanificador) {
                ((FBPlanificador) gestorColas.getPlanificador()).removerProceso(procesoEjecutando);
            }

            procesoEjecutando = null;
            contadorQuantum = 0;
            return;
        }

        // Manejar quantum
        contadorQuantum++;
        int quantumMaximo = 0;

        // Obtener quantum según el planificador
        if (gestorColas.getPlanificador() instanceof RoundRobinPlanificador) {
            quantumMaximo = this.quantumActual;
        } else if (gestorColas.getPlanificador() instanceof FBPlanificador) {
            quantumMaximo = ((FBPlanificador) gestorColas.getPlanificador()).getQuantum(procesoEjecutando);
        } else {
            // Para otros planificadores, usar quantum por defecto
            quantumMaximo = 9999; // Muy alto para que no expire
        }

        if (contadorQuantum >= quantumMaximo) {
            // Quantum agotado
            if (procesoEjecutando != null && !procesoEjecutando.estaTerminado()) {
                procesoEjecutando.setEstado(Estado.LISTO);

                // Lógica específica para FB: mover a siguiente cola
                if (gestorColas.getPlanificador() instanceof FBPlanificador) {
                    ((FBPlanificador) gestorColas.getPlanificador()).procesoExpulsado(procesoEjecutando);
                } else {
                    // Para otros planificadores, volver a cola normal
                    gestorColas.getColaListos().agregar(procesoEjecutando);
                }

                procesoEjecutando = null;
            }
            contadorQuantum = 0;
        }
    }
    
    /**
     * Selecciona un nuevo proceso para ejecución
     */
    private void seleccionarNuevoProceso() {
        PCB siguiente = gestorColas.seleccionarSiguiente();
        if (siguiente != null) {
            procesoEjecutando = siguiente;
            procesoEjecutando.setEstado(Estado.EJECUCION);
            
            // Reiniciar contador de quantum
            contadorQuantum = 0;
        }
    }
    
    // MÉTODOS DE CONTROL DE LA SIMULACIÓN
    
    public void pausarSimulacion() {
        pausado = true;
    }
    
    public void reanudarSimulacion() {
        pausado = false;
    }
    
    public void detenerSimulacion() {
        ejecutando = false;
        interrupt();
    }
    
    public boolean isEjecutando() {
        return ejecutando;
    }
    
    public boolean isPausado() {
        return pausado;
    }
    
    // CONFIGURACIÓN DE DURACIÓN DEL CICLO (REQUERIDO)
    
    public void setDuracionCicloMs(int duracionMs) {
        this.duracionCicloMs = duracionMs;
    }
    
    public int getDuracionCicloMs() {
        return duracionCicloMs;
    }
    
    // CONFIGURACIÓN DE QUANTUM
    
    public void setQuantum(int quantum) {
        this.quantumActual = quantum;
        this.contadorQuantum = 0;
    }
    
    public int getQuantum() {
        return quantumActual;
    }
    
    // INFORMACIÓN PARA LA INTERFAZ
    
    public PCB getProcesoEjecutando() {
        return procesoEjecutando;
    }
    
    public int getContadorQuantum() {
        return contadorQuantum;
    }
    
    /**
     * Obtiene información del estado actual para logs
     */
    public String getEstadoActual() {
        if (procesoEjecutando == null) {
            return "CPU: Libre | " + gestorColas.toString();
        } else {
            return String.format(
                "CPU: %s (PC: %d, Inst: %d/%d) | %s",
                procesoEjecutando.getNombre(),
                procesoEjecutando.getProgramCounter(),
                procesoEjecutando.getInstruccionesEjecutadas(),
                procesoEjecutando.getTotalInstrucciones(),
                gestorColas.toString()
            );
        }
    }
}