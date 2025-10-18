/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.vista;

import proyectoso.hilos.HiloSimulador;
import proyectoso.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private HiloSimulador hiloSimulador;
    private GestorColas gestorColas;
    
    // Componentes de la interfaz
    private JLabel lblProcesoEjecucion;
    private JLabel lblCicloGlobal;
    private JTextArea txtColaListos;
    private JTextArea txtColaBloqueados;
    private JButton btnIniciar;
    private JButton btnPausar;
    private JButton btnAgregarProceso;
    
    public VentanaPrincipal() {
        inicializarComponentes();
        configurarVentana();
        inicializarSistema();
    }
    
    private void inicializarSistema() {
        this.gestorColas = new GestorColas();
        this.hiloSimulador = new HiloSimulador(gestorColas);
        
        // Agregar procesos de prueba
        agregarProcesosPrueba();
    }
    
    private void agregarProcesosPrueba() {
        Proceso p1 = new Proceso("P1", "Navegador", 50, TipoProceso.CPU_BOUND);
        Proceso p2 = new Proceso("P2", "Editor", 30, TipoProceso.CPU_BOUND);
        Proceso p3 = new Proceso("P3", "Descarga", 20, TipoProceso.IO_BOUND);
        p3.setCiclosParaExcepcion(5);
        p3.setCiclosParaSatisfacer(3);
        
        gestorColas.agregarProceso(p1);
        gestorColas.agregarProceso(p2);
        gestorColas.agregarProceso(p3);
        
        actualizarInterfaz();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        // Panel superior - Información general
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        lblProcesoEjecucion = new JLabel("CPU: Libre");
        lblCicloGlobal = new JLabel("Ciclo: 0");
        panelSuperior.add(lblProcesoEjecucion);
        panelSuperior.add(lblCicloGlobal);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central - Colas de procesos
        JPanel panelCentral = new JPanel(new GridLayout(1, 2));
        
        txtColaListos = new JTextArea(10, 20);
        txtColaListos.setEditable(false);
        txtColaListos.setText("COLA DE LISTOS:\n");
        panelCentral.add(new JScrollPane(txtColaListos));
        
        txtColaBloqueados = new JTextArea(10, 20);
        txtColaBloqueados.setEditable(false);
        txtColaBloqueados.setText("COLA DE BLOQUEADOS:\n");
        panelCentral.add(new JScrollPane(txtColaBloqueados));
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior - Controles
        JPanel panelInferior = new JPanel();
        btnIniciar = new JButton("Iniciar Simulación");
        btnPausar = new JButton("Pausar");
        btnAgregarProceso = new JButton("Agregar Proceso");
        
        btnIniciar.addActionListener(e -> iniciarSimulacion());
        btnPausar.addActionListener(e -> pausarSimulacion());
        btnAgregarProceso.addActionListener(e -> agregarProceso());
        
        panelInferior.add(btnIniciar);
        panelInferior.add(btnPausar);
        panelInferior.add(btnAgregarProceso);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void configurarVentana() {
        setTitle("Simulador de Planificación - Sistemas Operativos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centrar ventana
    }
    
    private void iniciarSimulacion() {
        if (!hiloSimulador.estaEjecutando()) {
            hiloSimulador.start();
            btnIniciar.setEnabled(false);
            btnPausar.setEnabled(true);
            
            // Hilo para actualizar interfaz en tiempo real
            new Thread(() -> {
                while (hiloSimulador.estaEjecutando()) {
                    actualizarInterfaz();
                    try {
                        Thread.sleep(500); // Actualizar cada 500ms
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
        }
    }
    
    private void pausarSimulacion() {
        if (hiloSimulador.estaEjecutando()) {
            hiloSimulador.detener();
            btnIniciar.setEnabled(true);
            btnPausar.setEnabled(false);
        }
    }
    
    private void agregarProceso() {
        // Por ahora agregamos un proceso de prueba
        Proceso nuevo = new Proceso("P" + (gestorColas.getTotalProcesos() + 1), 
                                   "Proceso Nuevo", 25, TipoProceso.CPU_BOUND);
        gestorColas.agregarProceso(nuevo);
        actualizarInterfaz();
    }
    
    public void actualizarInterfaz() {
        // Actualizar proceso en ejecución
        Proceso enEjecucion = hiloSimulador.getProcesoEnEjecucion();
        if (enEjecucion != null) {
            lblProcesoEjecucion.setText("CPU: " + enEjecucion.getNombre() + 
                                      " (" + enEjecucion.getInstruccionesEjecutadas() + 
                                      "/" + enEjecucion.getTotalInstrucciones() + ")");
        } else {
            lblProcesoEjecucion.setText("CPU: Libre");
        }
        
        // Actualizar ciclo global
        lblCicloGlobal.setText("Ciclo: " + hiloSimulador.getCicloGlobal());
        
        // Actualizar cola de listos
        List<Proceso> listos = gestorColas.getColaListos();
        StringBuilder sbListos = new StringBuilder("COLA DE LISTOS:\n");
        for (Proceso p : listos) {
            sbListos.append("• ").append(p.toString()).append("\n");
        }
        txtColaListos.setText(sbListos.toString());
        
        // Actualizar cola de bloqueados
        List<Proceso> bloqueados = gestorColas.getColaBloqueados();
        StringBuilder sbBloqueados = new StringBuilder("COLA DE BLOQUEADOS:\n");
        for (Proceso p : bloqueados) {
            sbBloqueados.append("• ").append(p.toString()).append("\n");
        }
        txtColaBloqueados.setText(sbBloqueados.toString());
    }
}
