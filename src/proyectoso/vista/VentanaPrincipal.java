/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.vista;

import proyectoso.controlador.ControladorSimulador;
import proyectoso.modelo.*;
import proyectoso.util.Metricas;
import proyectoso.util.Configuracion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame implements Vista {
    private ControladorSimulador controlador;
    
    // PANELES PRINCIPALES
    private JPanel panelPrincipal;
    private JPanel panelControl;
    private JPanel panelColas;
    private JPanel panelCPU;
    private JPanel panelMetricas;
    private JPanel panelConfiguracion;
    
    // COMPONENTES DE CONTROL
    private JButton btnIniciar, btnPausar, btnReanudar, btnDetener, btnReiniciar;
    private JButton btnAgregarProceso;
    private JComboBox<String> comboPlanificadores;
    private JLabel lblEstadoSimulacion;
    private JLabel lblPlanificadorActual;
    
    // COMPONENTES DE COLAS
    private JScrollPane scrollListos, scrollBloqueados, scrollTerminados;
    private JScrollPane scrollListosSuspendidos, scrollBloqueadosSuspendidos;

    private JTextArea areaListos, areaBloqueados, areaTerminados;
    private JTextArea areaListosSuspendidos, areaBloqueadosSuspendidos;
    
    // COMPONENTES DE CPU
    private JLabel lblProcesoEjecutando, lblPC, lblMAR, lblInstrucciones;
    private JLabel lblQuantum, lblCicloActual;
    
    // COMPONENTES DE CONFIGURACIÓN
    private JSpinner spinnerDuracionCiclo, spinnerQuantum;
    private JSpinner spinnerCiclosExcepcion, spinnerCiclosSatisfaccion;
    private JSpinner spinnerNumProcesadores;
    
    // COMPONENTES DE MÉTRICAS
    private JLabel lblThroughput, lblUtilizacionCPU, lblTiempoRespuesta;
    private JLabel lblProcesosCompletados, lblCiclosTotales;
    
    // GRÁFICA
    private PanelGraficas panelGraficas;
    private JTabbedPane tabbedPane;
    
    public VentanaPrincipal(ControladorSimulador controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);
        
        // INICIALIZAR COMPONENTES PRIMERO
        inicializarComponentes();
        configurarVentana();
        
        // ACTUALIZAR VISTA DESPUÉS DE QUE TODO ESTÉ CREADO
        SwingUtilities.invokeLater(() -> {
            actualizarVista();
        });
    }
    
    private void inicializarComponentes() {
        // CONFIGURACIÓN BÁSICA DE LA VENTANA
        setTitle("Simulador de Planificación de Procesos - Sistemas Operativos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // INICIALIZAR PANELES PRIMERO
        panelPrincipal = new JPanel(new BorderLayout());
        panelControl = new JPanel(new FlowLayout());
        panelColas = new JPanel(new GridLayout(2, 3, 5, 5));
        panelCPU = new JPanel(new GridLayout(6, 1, 5, 5));
        panelMetricas = new JPanel(new GridLayout(5, 1, 10, 10));
        panelConfiguracion = new JPanel(new GridLayout(4, 2, 10, 10));
        
        // CREAR COMPONENTES
        crearComponentesControl();
        crearComponentesColas();
        crearComponentesCPU();
        crearComponentesMetricas();
        crearComponentesConfiguracion();
        
        // ORGANIZAR PANELES
        organizarPaneles();
    }
    
    private void crearComponentesControl() {
        panelControl.setBorder(BorderFactory.createTitledBorder("Control de Simulación"));
        
        // BOTONES DE CONTROL
        btnIniciar = new JButton("Iniciar");
        btnPausar = new JButton("Pausar");
        btnReanudar = new JButton("Reanudar");
        btnDetener = new JButton("Detener");
        btnReiniciar = new JButton("Reiniciar");
        btnAgregarProceso = new JButton("Agregar Proceso");
        
        // COMBO BOX DE PLANIFICADORES
        comboPlanificadores = new JComboBox<>(new String[]{
            "FCFS", "SPN", "SRT", "ROUNDROBIN", "HRRN", "FB"
        });
        
        // ETIQUETAS DE ESTADO
        lblEstadoSimulacion = new JLabel("Estado: Detenido");
        lblPlanificadorActual = new JLabel("Planificador: FCFS");
        
        // AGREGAR COMPONENTES
        panelControl.add(btnIniciar);
        panelControl.add(btnPausar);
        panelControl.add(btnReanudar);
        panelControl.add(btnDetener);
        panelControl.add(btnReiniciar);
        panelControl.add(new JLabel("Planificador:"));
        panelControl.add(comboPlanificadores);
        panelControl.add(btnAgregarProceso);
        panelControl.add(lblEstadoSimulacion);
        panelControl.add(lblPlanificadorActual);
        
        // CONFIGURAR EVENTOS
        configurarEventos();
    }
    
    private void crearComponentesColas() {
    panelColas.setBorder(BorderFactory.createTitledBorder("Colas de Procesos"));
    
    // CREAR ÁREAS DE TEXTO Y SCROLLPANES
    scrollListos = crearAreaTextoConTitulo("Listos");
    scrollBloqueados = crearAreaTextoConTitulo("Bloqueados");
    scrollTerminados = crearAreaTextoConTitulo("Terminados");
    scrollListosSuspendidos = crearAreaTextoConTitulo("Listos Suspendidos");
    scrollBloqueadosSuspendidos = crearAreaTextoConTitulo("Bloqueados Suspendidos");
    
    // OBTENER LAS ÁREAS DE TEXTO DE LOS SCROLLPANES
    areaListos = (JTextArea) scrollListos.getViewport().getView();
    areaBloqueados = (JTextArea) scrollBloqueados.getViewport().getView();
    areaTerminados = (JTextArea) scrollTerminados.getViewport().getView();
    areaListosSuspendidos = (JTextArea) scrollListosSuspendidos.getViewport().getView();
    areaBloqueadosSuspendidos = (JTextArea) scrollBloqueadosSuspendidos.getViewport().getView();
    
    // AGREGAR LOS SCROLLPANES AL PANEL
    panelColas.add(scrollListos);
    panelColas.add(scrollBloqueados);
    panelColas.add(scrollTerminados);
    panelColas.add(scrollListosSuspendidos);
    panelColas.add(scrollBloqueadosSuspendidos);
    }
    
    private JScrollPane crearAreaTextoConTitulo(String titulo) {
        JTextArea area = new JTextArea(10, 20);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createTitledBorder(titulo));
        return scrollPane;
    }
    
    private void crearComponentesCPU() {
        panelCPU.setBorder(BorderFactory.createTitledBorder("Estado del CPU"));
        
        // ETIQUETAS PARA INFORMACIÓN DEL CPU
        lblProcesoEjecutando = crearEtiquetaCPU("Proceso ejecutando: Ninguno");
        lblPC = crearEtiquetaCPU("Program Counter: 0");
        lblMAR = crearEtiquetaCPU("Memory Address Register: 0");
        lblInstrucciones = crearEtiquetaCPU("Instrucciones: 0/0");
        lblQuantum = crearEtiquetaCPU("Quantum actual: 0/0");
        lblCicloActual = crearEtiquetaCPU("Ciclo actual: 0");
        
        panelCPU.add(lblProcesoEjecutando);
        panelCPU.add(lblPC);
        panelCPU.add(lblMAR);
        panelCPU.add(lblInstrucciones);
        panelCPU.add(lblQuantum);
        panelCPU.add(lblCicloActual);
    }
    
    private JLabel crearEtiquetaCPU(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }
    
    private void crearComponentesMetricas() {
        panelMetricas.setBorder(BorderFactory.createTitledBorder("Métricas de Rendimiento"));
        
        lblThroughput = crearEtiquetaMetrica("Throughput: 0.00 procesos/ciclo");
        lblUtilizacionCPU = crearEtiquetaMetrica("Utilización CPU: 0.00%");
        lblTiempoRespuesta = crearEtiquetaMetrica("Tiempo respuesta promedio: 0.00 ciclos");
        lblProcesosCompletados = crearEtiquetaMetrica("Procesos completados: 0");
        lblCiclosTotales = crearEtiquetaMetrica("Ciclos totales: 0");
        
        panelMetricas.add(lblThroughput);
        panelMetricas.add(lblUtilizacionCPU);
        panelMetricas.add(lblTiempoRespuesta);
        panelMetricas.add(lblProcesosCompletados);
        panelMetricas.add(lblCiclosTotales);
    }
    
    private JLabel crearEtiquetaMetrica(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return label;
    }
    
    private void crearComponentesConfiguracion() {
        panelConfiguracion.setBorder(BorderFactory.createTitledBorder("Configuración del Sistema"));
        Configuracion config = controlador.getConfiguracion();
        
        // DURACIÓN DEL CICLO
        spinnerDuracionCiclo = new JSpinner(new SpinnerNumberModel((int) config.getDuracionCicloMs(), 100, 5000, 100));        
        panelConfiguracion.add(new JLabel("Duración del ciclo (ms):"));
        panelConfiguracion.add(spinnerDuracionCiclo);
        
        // QUANTUM
        spinnerQuantum = new JSpinner(new SpinnerNumberModel((int) config.getQuantum(), 1, 10, 1));
        panelConfiguracion.add(new JLabel("Quantum (Round Robin):"));
        panelConfiguracion.add(spinnerQuantum);
        
        // CICLOS PARA EXCEPCIÓN
        spinnerCiclosExcepcion = new JSpinner(new SpinnerNumberModel((int) config.getCiclosExcepcion(), 1, 20, 1));
        panelConfiguracion.add(new JLabel("Ciclos para excepción E/S:"));
        panelConfiguracion.add(spinnerCiclosExcepcion);
        
        
        // CICLOS PARA SATISFACER
        spinnerCiclosSatisfaccion = new JSpinner(new SpinnerNumberModel((int) config.getCiclosSatisfaccion(), 1, 100, 1));
        panelConfiguracion.add(new JLabel("Ciclos para satisfacer E/S:"));
        panelConfiguracion.add(spinnerCiclosSatisfaccion);
        
        // CREACIÓN DEL SPINNER (Límites requeridos: entre 2 y 3) 
        spinnerNumProcesadores = new JSpinner(new SpinnerNumberModel(config.getNumProcesadores(), 1, 4, 1) );// Rango de 1 a 4, aunque el requisito pide entre 2 y 3
        panelConfiguracion.add(new JLabel("Número de Procesadores:"));
        panelConfiguracion.add(spinnerNumProcesadores);
        
        // BOTÓN PARA APLICAR CONFIGURACIÓN
        JButton btnAplicarConfig = new JButton("Aplicar Configuración");
        btnAplicarConfig.addActionListener(e -> aplicarConfiguracion());
        panelConfiguracion.add(btnAplicarConfig);
    }
    
    private void organizarPaneles() {
        // Crear panel de gráficas
        panelGraficas = new PanelGraficas();

        // ORGANIZAR PANELES EN PESTAÑAS
        tabbedPane = new JTabbedPane();

        // PANEL DE SIMULACIÓN (CPU + COLAS)
        JPanel panelSimulacion = new JPanel(new GridLayout(1, 2));
        panelSimulacion.add(panelCPU);
        panelSimulacion.add(panelColas);

        tabbedPane.addTab("Simulación", panelSimulacion);
        tabbedPane.addTab("Configuración", panelConfiguracion);
        tabbedPane.addTab("Métricas", panelMetricas);
        tabbedPane.addTab("Gráficas", panelGraficas); // NUEVA PESTAÑA

        panelPrincipal.add(panelControl, BorderLayout.NORTH);
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);

        add(panelPrincipal);
    }
    
    private void configurarVentana() {
        // TIMER PARA ACTUALIZACIÓN AUTOMÁTICA
        Timer timerActualizacion = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.isSimulacionActiva()) {
                    actualizarVista();
                }
            }
        });
        timerActualizacion.start();
        
        setVisible(true);
    }
    
    private void configurarEventos() {
        btnIniciar.addActionListener(e -> controlador.iniciarSimulacion());
        btnPausar.addActionListener(e -> controlador.pausarSimulacion());
        btnReanudar.addActionListener(e -> controlador.reanudarSimulacion());
        btnDetener.addActionListener(e -> controlador.detenerSimulacion());
        btnReiniciar.addActionListener(e -> controlador.reiniciarSimulacion());
        
        btnAgregarProceso.addActionListener(e -> mostrarDialogoAgregarProceso());
        
        comboPlanificadores.addActionListener(e -> {
            String planificador = (String) comboPlanificadores.getSelectedItem();
            controlador.cambiarPlanificador(planificador);
            actualizarPanelControl();
        });
    }
    
    private void aplicarConfiguracion() {
        int duracionCiclo = (Integer) spinnerDuracionCiclo.getValue();
        int quantum = (Integer) spinnerQuantum.getValue();
        int ciclosExcepcion = (Integer) spinnerCiclosExcepcion.getValue();
        int ciclosSatisfaccion = (Integer) spinnerCiclosSatisfaccion.getValue();
        int numProcesadores = (Integer) spinnerNumProcesadores.getValue();
        
        controlador.configurarDuracionCiclo(duracionCiclo);
        controlador.configurarQuantum(quantum);
        controlador.configurarCiclosExcepcion(ciclosExcepcion);
        controlador.configurarCiclosSatisfaccion(ciclosSatisfaccion);
        controlador.configurarNumProcesadores(numProcesadores);
    }
    
    private void mostrarDialogoAgregarProceso() {
    // Crear panel para el diálogo de agregar proceso
    JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
    
    // Campos del formulario
    JTextField txtNombre = new JTextField("Proceso" + (System.currentTimeMillis() % 1000));
    JTextField txtInstrucciones = new JTextField("30");
    JComboBox<TipoProceso> comboTipo = new JComboBox<>(TipoProceso.values());
    
    // Agregar componentes al panel
    panel.add(new JLabel("Nombre:"));
    panel.add(txtNombre);
    panel.add(new JLabel("Total Instrucciones:"));
    panel.add(txtInstrucciones);
    panel.add(new JLabel("Tipo:"));
    panel.add(comboTipo);
    
    // Mostrar diálogo
    int result = JOptionPane.showConfirmDialog(
        this, 
        panel, 
        "Agregar Nuevo Proceso", 
        JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.PLAIN_MESSAGE
    );
    
    // Si el usuario hizo clic en OK
    if (result == JOptionPane.OK_OPTION) {
        try {
            String nombre = txtNombre.getText().trim();
            int totalInstrucciones = Integer.parseInt(txtInstrucciones.getText().trim());
            TipoProceso tipo = (TipoProceso) comboTipo.getSelectedItem();
            
            // Validaciones
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (totalInstrucciones <= 0) {
                JOptionPane.showMessageDialog(this, "El total de instrucciones debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Agregar el proceso al controlador
            controlador.agregarProceso(nombre, totalInstrucciones, tipo);
            
            // Actualizar la vista para mostrar el nuevo proceso en la cola
            actualizarVista();
            
            JOptionPane.showMessageDialog(this, 
                "Proceso '" + nombre + "' agregado exitosamente!\n" +
                "Instrucciones: " + totalInstrucciones + "\n" +
                "Tipo: " + tipo, 
                "Proceso Agregado", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingresa un número válido para las instrucciones", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al agregar proceso: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    }
    
    // MÉTODOS DE LA INTERFAZ VISTA
    @Override
    public void actualizarVista() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> actualizarVista());
            return;
        }
        
        actualizarPanelControl();
        actualizarPanelColas();
        actualizarPanelCPU();
    }
    
    @Override
    public void actualizarMetricas() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> actualizarMetricas());
            return;
        }

        Metricas metricas = controlador.getMetricas();

        if (lblThroughput != null) {
            lblThroughput.setText(String.format("Throughput: %.4f procesos/ciclo", metricas.getThroughput()));
            lblUtilizacionCPU.setText(String.format("Utilización CPU: %.2f%%", metricas.getUtilizacionCPU()));
            lblTiempoRespuesta.setText(String.format("Tiempo respuesta promedio: %.2f ciclos", metricas.getTiempoRespuestaPromedio()));
            lblProcesosCompletados.setText(String.format("Procesos completados: %d", metricas.getProcesosCompletados()));
            lblCiclosTotales.setText(String.format("Ciclos totales: %d", metricas.getCiclosTotales()));

            // ACTUALIZAR GRÁFICAS
            if (panelGraficas != null) {
                panelGraficas.agregarMetricas(metricas);
            }
        }
    }
    
    private void actualizarPanelControl() {
        if (btnIniciar == null) return;
        
        boolean simulacionActiva = controlador.isSimulacionActiva();
        
        btnIniciar.setEnabled(!simulacionActiva);
        btnPausar.setEnabled(simulacionActiva && controlador.getHiloSimulador() != null && !controlador.getHiloSimulador().isPausado());
        btnReanudar.setEnabled(simulacionActiva && controlador.getHiloSimulador() != null && controlador.getHiloSimulador().isPausado());
        btnDetener.setEnabled(simulacionActiva);
        btnReiniciar.setEnabled(true);
        btnAgregarProceso.setEnabled(true);
        
        String estado = simulacionActiva ? 
            (controlador.getHiloSimulador() != null && controlador.getHiloSimulador().isPausado() ? "Pausada" : "Ejecutándose") : 
            "Detenida";
        lblEstadoSimulacion.setText("Estado: " + estado);
        lblPlanificadorActual.setText("Planificador: " + controlador.getGestorColas().getNombrePlanificadorActual());
    }
    
    private void actualizarPanelColas() {
        if (areaListos == null) return;

        // LIMPIAR TODAS LAS ÁREAS PRIMERO
        areaListos.setText("");
        areaBloqueados.setText("");
        areaTerminados.setText("");
        areaListosSuspendidos.setText("");
        areaBloqueadosSuspendidos.setText("");

        // LÓGICA ESPECIAL PARA FEEDBACK (FB)
        if (controlador.getGestorColas().getPlanificador() instanceof FBPlanificador) {
            FBPlanificador fb = (FBPlanificador) controlador.getGestorColas().getPlanificador();

            for (int i = 0; i < fb.getNumeroColas(); i++) {
                areaListos.append("=== COLA " + i + " (Quantum: " + fb.getQuantum(i) + ") ===\n");
                ColaPCB cola = fb.getCola(i);
                if (fb.getCola(i) != null && !fb.getCola(i).estaVacia()) {
                    PCB[] procesos = fb.getCola(i).toArray();
                    for (PCB pcb : procesos) {
                        areaListos.append(pcb.toString() + "\n");
                    }
                } else {
                    areaListos.append("Vacía\n");
                }
                areaListos.append("\n");
            }

        } else {
            // COMPORTAMIENTO NORMAL PARA OTROS PLANIFICADORES
            actualizarAreaCola(areaListos, controlador.getGestorColas().getColaListos());
        }

        // LAS OTRAS COLAS SIEMPRE SE MUESTRAN NORMAL
        actualizarAreaCola(areaBloqueados, controlador.getGestorColas().getColaBloqueados());
        actualizarAreaCola(areaTerminados, controlador.getGestorColas().getColaTerminados());
        actualizarAreaCola(areaListosSuspendidos, controlador.getGestorColas().getColaListosSuspendidos());
        actualizarAreaCola(areaBloqueadosSuspendidos, controlador.getGestorColas().getColaBloqueadosSuspendidos());
    }
    
    private void actualizarAreaCola(JTextArea area, ColaPCB cola) {
        if (area == null) return;
        
        area.setText("");
        if (cola != null && !cola.estaVacia()) {
            PCB[] procesos = cola.toArray();
            for (PCB pcb : procesos) {
                area.append(pcb.toString() + "\n");
            }
        } else {
            area.setText("Vacía");
        }
    }
    
    private void actualizarPanelCPU() {
        if (lblProcesoEjecutando == null) return;
        
        PCB procesoEjecutando = controlador.getHiloSimulador() != null ? 
            controlador.getHiloSimulador().getProcesoEjecutando() : null;
        GestorColas gestor = controlador.getGestorColas();
        
        if (procesoEjecutando != null) {
            lblProcesoEjecutando.setText("Proceso ejecutando: " + procesoEjecutando.getNombre());
            lblPC.setText("Program Counter: " + procesoEjecutando.getProgramCounter());
            lblMAR.setText("Memory Address Register: " + procesoEjecutando.getMemoryAddressRegister());
            lblInstrucciones.setText("Instrucciones: " + 
                procesoEjecutando.getInstruccionesEjecutadas() + "/" + 
                procesoEjecutando.getTotalInstrucciones());
            
            if (controlador.getGestorColas().getPlanificador() instanceof RoundRobinPlanificador) {
                lblQuantum.setText("Quantum: " + controlador.getHiloSimulador().getContadorQuantum() + 
                                 "/" + controlador.getHiloSimulador().getQuantum());
            } else {
                lblQuantum.setText("Quantum: No aplica");
            }
        } else {
            lblProcesoEjecutando.setText("Proceso ejecutando: Ninguno");
            lblPC.setText("Program Counter: 0");
            lblMAR.setText("Memory Address Register: 0");
            lblInstrucciones.setText("Instrucciones: 0/0");
            lblQuantum.setText("Quantum: 0/0");
        }
        
        lblCicloActual.setText("Ciclo actual: " + gestor.getCicloActual());
    }
    
    
    
}