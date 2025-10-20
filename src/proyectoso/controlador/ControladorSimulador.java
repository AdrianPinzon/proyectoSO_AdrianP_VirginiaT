/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.controlador;

import proyectoso.modelo.*;
import proyectoso.hilos.*;
import proyectoso.util.*;

public class ControladorSimulador {
    // MODELO
    private GestorColas gestorColas;
    private Configuracion configuracion;
    
    // HILOS
    private HiloSimulador hiloSimulador;
    private HiloReloj hiloReloj;
    private ListaHilosExcepcion hilosExcepciones;
    
    // VISTA
    private VentanaPrincipal ventana;
    
    // UTILIDADES
    private Logger logger;
    private GestorArchivos gestorArchivos;
    
    // ESTADO
    private volatile boolean simulacionActiva;
    
    public ControladorSimulador() {
        this.gestorColas = new GestorColas();
        this.configuracion = new Configuracion();
        this.hilosExcepciones = new ListaHilosExcepcion();
        this.logger = new Logger();
        this.gestorArchivos = new GestorArchivos();
        this.simulacionActiva = false;
        
        // Cargar configuración guardada si existe
        cargarConfiguracion();
        
        // Inicializar hilos (pero no iniciarlos todavía)
        this.hiloSimulador = new HiloSimulador(gestorColas, this);
        this.hiloReloj = new HiloReloj(this, configuracion.getDuracionCicloMs());
    }
    
    // ... (los demás métodos se mantienen igual hasta detenerSimulacion) ...
    
    public void detenerSimulacion() {
        simulacionActiva = false;
        
        // Detener hilos
        if (hiloSimulador != null) {
            hiloSimulador.detenerSimulacion();
        }
        if (hiloReloj != null) {
            hiloReloj.detener();
        }
        
        // Detener todos los hilos de excepción
        hilosExcepciones.detenerTodos();
        
        logger.log("Simulación detenida");
    }
    
    // ... (el resto de métodos se mantienen igual) ...
}