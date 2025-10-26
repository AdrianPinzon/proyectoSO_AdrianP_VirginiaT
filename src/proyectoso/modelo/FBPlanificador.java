package proyectoso.modelo;
import proyectoso.controlador.ControladorSimulador;

public class FBPlanificador implements Planificador { 
    private ColaPCB[] colas;
    private int[] quantums;
    private int numeroColas;
    private ControladorSimulador controlador;
    
    public FBPlanificador(ControladorSimulador controlador) {
        this.controlador = controlador;
        this.numeroColas = 3; // 3 niveles de feedback
        this.colas = new ColaPCB[numeroColas];
        this.quantums = new int[numeroColas];
        
        // Inicializar colas
        for (int i = 0; i < numeroColas; i++) {
            colas[i] = new ColaPCB();
        }
        
        // Configurar quantums: cada nivel tiene el doble de quantum que el anterior
        quantums[0] = 2;  // Cola 0: quantum 2
        quantums[1] = 4;  // Cola 1: quantum 4  
        quantums[2] = 8;  // Cola 2: quantum 8 (sin expulsión)
    }
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) {
        // Distribuir procesos nuevos en la cola de mayor prioridad (0)
        distribuirProcesosNuevos(colaListos);
        
        // Buscar desde la cola de mayor prioridad (0) hasta la de menor (2)
        for (int i = 0; i < numeroColas; i++) {
            if (!colas[i].estaVacia()) {
                return colas[i].getPrimero();
            }
        }
        
        return null;
    }
    
    /**
     * Distribuye procesos nuevos en la cola de mayor prioridad
     */
    public void distribuirProcesosNuevos(ColaPCB colaListosExterna) {
    if (colaListosExterna == null || colaListosExterna.estaVacia()) {
        return;
    }

    // Iterar y remover procesos de la cola externa
    ColaPCB temp = new ColaPCB();
        PCB pcb;

        // 1. Mover todos los procesos de la cola externa a una cola temporal
        while ((pcb = colaListosExterna.remover()) != null) {
            temp.agregar(pcb);
        }

        // 2. Distribuir desde la cola temporal a la Cola 0 interna
        while ((pcb = temp.remover()) != null) {

            // Asumiendo que estaEnColasFeedback(pcb) siempre será falso para nuevos procesos
            if (!estaEnColasFeedback(pcb)) {

                // 👈 AJUSTE CRÍTICO: Establecer el índice de cola en el PCB
                pcb.setColaFB(0); 

                colas[0].agregar(pcb); // Añadir a la cola de mayor prioridad
            }
        }
    }

    /**
     * Verifica si un proceso está en alguna cola de feedback
     */
    private boolean estaEnColasFeedback(PCB pcb) {
        for (int i = 0; i < numeroColas; i++) {
            PCB[] procesos = colas[i].toArray();
            for (PCB p : procesos) {
                if (p == pcb) return true;
            }
        }
        return false;
    }
    
    /**
     * Método llamado cuando un proceso agota su quantum
     * Lo mueve a la siguiente cola de menor prioridad
     */
    public void procesoExpulsado(PCB proceso) {
        // Buscar en qu é cola está actualmente
        int colaActual = proceso.getColaFB();
        for (int i = 0; i < numeroColas; i++) {
            if (colas[i].contiene(proceso)) {
                colaActual = i;
                break;
            }
        }
        
        if (colaActual != -1 && colaActual < numeroColas){
    
            // 1. Remover de cola actual (e.g., Cola 0)
            boolean removido = colas[colaActual].removerPCB(proceso); // 👈 Guardar resultado

            if (removido) {

                // Mover a siguiente cola (si no es la última)
                if (colaActual < numeroColas - 1) {
                    colas[colaActual + 1].agregar(proceso);
                    proceso.setColaFB(colaActual + 1);
                    proceso.setEstado(Estado.LISTO);

                    // 👈 REGISTRO DE TRACE: Verificar si se movió a Cola 1
                    controlador.getLogger().log("FB Degradación: Proceso " + proceso.getNombre() + 
                                               " movido con éxito a Cola " + (colaActual + 1));
                } else {
                    // Última cola
                    colas[colaActual].agregar(proceso);
                    proceso.setEstado(Estado.LISTO);
                    // 👈 REGISTRO DE TRACE: Verificar reencolamiento
                    controlador.getLogger().log("FB Degradación: Proceso " + proceso.getNombre() + 
                                               " reencolado en Cola " + colaActual);
                }

            } else {
                // 👈 REGISTRO DE ERROR: Si removerPCB falló (P1 desapareció sin moverse)
                controlador.getLogger().log("FB ERROR: Falló remoción de P1 de Cola " + colaActual + ". El proceso se ha perdido.");
            }
        }
    }
    
    /**
     * Obtiene el quantum para un proceso específico
     */
    public int getQuantum(PCB proceso) {
        for (int i = 0; i < numeroColas; i++) {
            if (colas[i].contiene(proceso)) {
                return quantums[i];
            }
        }
        return quantums[0]; // Default: quantum de la cola 0
    }
    
    /**
     * Obtiene la cola actual de un proceso
     */
    public int getColaActual(PCB proceso) {
        for (int i = 0; i < numeroColas; i++) {
            if (colas[i].contiene(proceso)) {
                return i;
            }
        }
        return -1; // No encontrado
    }
    
    /**
     * Remueve un proceso de todas las colas de feedback
     */
    public void removerProceso(PCB pcb) {
        for (int i = 0; i < numeroColas; i++) {
            colas[i].removerPCB(pcb);
        }
    }
    
    @Override
    public String getNombre() {
        return "Feedback";
    }
    
    // Getters para la interfaz gráfica
    public ColaPCB getCola(int nivel) {
        if (nivel >= 0 && nivel < numeroColas) {
            return colas[nivel];
        }
        return null;
    }
    
    public int getQuantum(int nivel) {
        if (nivel >= 0 && nivel < numeroColas) {
            return quantums[nivel];
        }
        return 0;
    }
    
    public int getNumeroColas() {
        return numeroColas;
    }
}
