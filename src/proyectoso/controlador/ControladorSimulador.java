/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.controlador;

import proyectoso.modelo.*;
import proyectoso.hilos.*;
import proyectoso.util.*;
import proyectoso.vista.Vista;
/*
Gestiona la interacción entre el Modelo y la Vista, tiene ejecución concurrente a través de
los hilos de simulación.
*/

public class ControladorSimulador {
    // MODELO
    private GestorColas gestorColas;
    private Configuracion configuracion;
    
    // HILOS
    private HiloSimulador hiloSimulador;
    private HiloReloj hiloReloj; // CORREGIDO: era HiLoReloj
    private ListaHilosExcepcion hilosExcepciones;
    
    // VISTA
    private Vista vista;
    
    // UTILIDADES
    private Logger logger;
    private GestorArchivos gestorArchivos;
    
    // ESTADO
    private volatile boolean simulacionActiva;
    
    public ControladorSimulador() {
        this.logger = new Logger();
        this.hilosExcepciones = new ListaHilosExcepcion();
        this.gestorColas = new GestorColas(this, this.hilosExcepciones );
        this.configuracion = new Configuracion();
        this.hilosExcepciones = new ListaHilosExcepcion();
        this.gestorArchivos = new GestorArchivos();
        this.simulacionActiva = false;
        
        // Cargar configuración guardada si existe
        cargarConfiguracion();
        
        // Inicializar hilos (pero no iniciarlos todavía)
        this.hiloSimulador = new HiloSimulador(gestorColas, this);
        this.hiloReloj = new HiloReloj(this, configuracion.getDuracionCicloMs()); // CORREGIDO
    }
    
    // MÉTODOS DE CONTROL DE SIMULACIÓN
    
    public void iniciarSimulacion() {
        if (simulacionActiva) return;
        
        try {
            simulacionActiva = true;
            
            // Iniciar hilos
            hiloSimulador.start();
            hiloReloj.start(); // CORREGIDO
            
            logger.log("Simulación iniciada - Planificador: " + 
                      gestorColas.getNombrePlanificadorActual());
            
        } catch (Exception e) {
            logger.log("Error al iniciar simulación: " + e.getMessage());
            simulacionActiva = false;
        }
    }
    
    public void pausarSimulacion() {
        if (hiloSimulador != null && hiloSimulador.isEjecutando()) {
            hiloSimulador.pausarSimulacion();
            logger.log("Simulación pausada");
        }
    }
    
    public void reanudarSimulacion() {
        if (hiloSimulador != null && hiloSimulador.isEjecutando()) {
            hiloSimulador.reanudarSimulacion();
            logger.log("Simulación reanudada");
        }
    }
    
    public void detenerSimulacion() {
        simulacionActiva = false;
        
        // Detener hilos
        if (hiloSimulador != null) {
            hiloSimulador.detenerSimulacion();
        }
        if (hiloReloj != null) {
            hiloReloj.detener(); // CORREGIDO
        }
        
        // Detener todos los hilos de excepción
        hilosExcepciones.detenerTodos();
        
        logger.log("Simulación detenida");
    }
    
    public void reiniciarSimulacion() {
        detenerSimulacion();
        
        // Reiniciar gestor de colas
        gestorColas.reiniciar();
        
        // Recrear hilos
        this.hiloSimulador = new HiloSimulador(gestorColas, this);
        this.hiloReloj = new HiloReloj(this, configuracion.getDuracionCicloMs()); // CORREGIDO
        
        logger.log("Simulación reiniciada");
    }
    
    // GESTIÓN DE PROCESOS
    
    public void agregarProceso(String nombre, int totalInstrucciones, TipoProceso tipo) {
        try {
            // Generar ID único
            String id = "P" + (gestorColas.getColaTerminados().getTamaño() + 
                              gestorColas.getColaListos().getTamaño() + 1);
            
            // Crear PCB
            PCB nuevoProceso = new PCB(id, nombre, totalInstrucciones, tipo);
            
            // Configurar parámetros específicos si es I/O bound
            if (tipo == TipoProceso.IO_BOUND) {
                nuevoProceso.setCiclosParaExcepcion(configuracion.getCiclosExcepcion());
                nuevoProceso.setCiclosParaSatisfacer(configuracion.getCiclosSatisfaccion());
            }
            
            // Agregar al gestor
            gestorColas.agregarProceso(nuevoProceso);
            
            // 👈 LÓGICA DE DISTRIBUCIÓN FB MEJORADA
            // La distribución debe ocurrir si la simulación NO está activa O si está pausada.
            if (gestorColas.getPlanificador() instanceof FBPlanificador) {

                boolean simulaciónPuedeDistribuir = 
                    !this.simulacionActiva || // Si está Detenida
                    (this.simulacionActiva && hiloSimulador.isPausado()); // O si está Pausada

                if (simulaciónPuedeDistribuir) {
                    FBPlanificador fb = (FBPlanificador) gestorColas.getPlanificador();
                    // Llamar al método de distribución
                    fb.distribuirProcesosNuevos(gestorColas.getColaListos()); 
                }
            }

            logger.log("Nuevo proceso creado: " + nuevoProceso.toString());
            actualizarVista();

        } catch (Exception e) {
            logger.log("Error al crear proceso: " + e.getMessage());
        }
    }
    
    // GESTIÓN DE PLANIFICADORES
    
    public void cambiarPlanificador(String tipoPlanificador) {
        try {
            Planificador nuevoPlanificador = crearPlanificador(tipoPlanificador);
            gestorColas.setPlanificador(nuevoPlanificador);

            // 👈 VERIFICACIÓN DE NULIDAD CRÍTICA
            if (nuevoPlanificador == null) {
                logger.log("Error: El planificador '" + tipoPlanificador + "' no pudo ser creado.");
                actualizarVista();
                return; // Sale del método si es nulo.
            }

            // 1. CONFIGURACIÓN DEL MODELO: Asignar el nuevo planificador
            gestorColas.setPlanificador(nuevoPlanificador);

            // 2. LÓGICA CONDICIONAL: Ya es segura porque nuevoPlanificador no es null
            if (nuevoPlanificador instanceof RoundRobinPlanificador roundRobinPlanificador) { 
                 roundRobinPlanificador.setQuantum(configuracion.getQuantum());
                 hiloSimulador.setQuantum(configuracion.getQuantum());
            }

            logger.log("Planificador cambiado a: " + nuevoPlanificador.getNombre());

            // 👈 LÍNEA CRÍTICA: La actualización debe ejecutarse SIEMPRE
            actualizarVista(); 

        } catch (Exception e) {
            logger.log("Error al cambiar planificador: " + e.getMessage());
        }
    }
    
    private Planificador crearPlanificador(String tipo) {
        switch (tipo.toUpperCase()) {
            case "FCFS":
                return new FCFSPlanificador();
            case "SPN":
                return new SPNPlanificador();
            case "SRT":
                return new SRTPlanificador();
            case "ROUNDROBIN":
                RoundRobinPlanificador rr = new RoundRobinPlanificador(configuracion.getQuantum());
                hiloSimulador.setQuantum(configuracion.getQuantum());
                return rr;
            case "HRRN":
                return new HRRNPlanificador(true);
            case "FB":  
            return new FBPlanificador(this);
        default:
            return new FCFSPlanificador();
        }
    }
    
    // CONFIGURACIÓN
    
    public void configurarDuracionCiclo(int duracionMs) {
        configuracion.setDuracionCicloMs(duracionMs);
        hiloSimulador.setDuracionCicloMs(duracionMs);
        if (hiloReloj != null) {
            hiloReloj.setIntervalo(duracionMs); // CORREGIDO
        }
        
        logger.log("Duración del ciclo cambiada a: " + duracionMs + "ms");
        
        // Guardar configuración
        guardarConfiguracion();
    }
    
    public void configurarQuantum(int quantum) {
        configuracion.setQuantum(quantum);
        
        // Si el planificador actual es Round Robin, actualizarlo
        if (gestorColas.getPlanificador() instanceof RoundRobinPlanificador) {
            ((RoundRobinPlanificador) gestorColas.getPlanificador()).setQuantum(quantum);
            hiloSimulador.setQuantum(quantum);
        }
        
        logger.log("Quantum configurado a: " + quantum);
        guardarConfiguracion();
    }
    
    public void configurarCiclosExcepcion(int ciclos) {
        configuracion.setCiclosExcepcion(ciclos); // Asume que Configuracion tiene el setter
        logger.log("Ciclos de excepción configurados a: " + ciclos);
        guardarConfiguracion(); // Guarda el cambio
    }

    public void configurarCiclosSatisfaccion(int ciclos) {
        configuracion.setCiclosSatisfaccion(ciclos); // Asume que Configuracion tiene el setter
        logger.log("Ciclos de satisfacción configurados a: " + ciclos);
        guardarConfiguracion(); // Guarda el cambio
    }
    
    public void configurarNumProcesadores(int numProcesadores) {
        // 1. Validar el límite requerido (aunque el spinner lo controle)
        if (numProcesadores < 1 || numProcesadores > 4) {
            logger.log("Advertencia: Número de procesadores fuera de rango.");
            return;
        }

        // 2. Guardar en el objeto de configuración
        configuracion.setNumProcesadores(numProcesadores); 
        logger.log("Número de procesadores configurado a: " + numProcesadores);

        // 3. Forzar el guardado en el archivo (TXT/CSV/JSON) [cite: 15]
        guardarConfiguracion(); 
    }
    
    // MÉTRICAS Y ESTADÍSTICAS
    
    public Metricas getMetricas() {
        Metricas metricas = new Metricas();
        metricas.setThroughput(gestorColas.getThroughput());
        metricas.setUtilizacionCPU(gestorColas.getUtilizacionCPU(hiloSimulador.getProcesoEjecutando()));
        metricas.setTiempoRespuestaPromedio(gestorColas.getTiempoRespuestaPromedio());
        metricas.setProcesosCompletados(gestorColas.getProcesosCompletados());
        metricas.setCiclosTotales(gestorColas.getCicloActual());
        return metricas;
    }
    
    // ACTUALIZACIÓN DE VISTA
    
    public void actualizarVista() {
        if (vista != null) {
            vista.actualizarVista();
        }
    }
    
    public void onTickReloj(int ciclo) {
        if (vista != null) {
        vista.actualizarMetricas();
        }
    }
    
    
    // PERSISTENCIA
    
    private void cargarConfiguracion() {
        try {
            Configuracion configCargada = gestorArchivos.cargarConfiguracion();
            if (configCargada != null) {
                this.configuracion = configCargada;
                logger.log("Configuración cargada desde archivo");
            }
        } catch (Exception e) {
            logger.log("No se pudo cargar configuración, usando valores por defecto");
        }
    }
    
    private void guardarConfiguracion() {
        try {
            gestorArchivos.guardarConfiguracion(configuracion);
            logger.log("Configuración guardada en archivo");
        } catch (Exception e) {
            logger.log("Error al guardar configuración: " + e.getMessage());
        }
    }
    
    // GETTERS 
    
    public GestorColas getGestorColas() { return gestorColas; }
    public HiloSimulador getHiloSimulador() { return hiloSimulador; }
    public Configuracion getConfiguracion() { return configuracion; }
    public Logger getLogger() { return logger; }
    public boolean isSimulacionActiva() { return simulacionActiva; }
    
     // SETTER 
    public void setVista(Vista vista) {
        this.vista = vista;
    }
}
