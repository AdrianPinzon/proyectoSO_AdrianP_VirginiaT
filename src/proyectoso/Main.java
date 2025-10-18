/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoso;

import proyectoso.modelo.*;
import proyectoso.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Simulador de Planificación de Procesos");
        
        // Mostrar la ventana principal
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
        
        // Prueba básica del sistema
        probarSistemaBasico();
    }
    
    private static void probarSistemaBasico() {
        System.out.println("🧪 Probando sistema básico...");
        
        // Crear gestor de colas
        GestorColas gestor = new GestorColas();
        
        // Crear procesos de prueba
        Proceso proceso1 = new Proceso("P1", "Proceso CPU", 100, TipoProceso.CPU_BOUND);
        Proceso proceso2 = new Proceso("P2", "Proceso I/O", 150, TipoProceso.IO_BOUND);
        proceso2.setCiclosParaExcepcion(10);
        proceso2.setCiclosParaSatisfacer(5);
        
        // Agregar procesos al gestor
        gestor.agregarProceso(proceso1);
        gestor.agregarProceso(proceso2);
        
        // Crear PCBs
        PCB pcb1 = new PCB(proceso1);
        PCB pcb2 = new PCB(proceso2);
        
        // Mostrar estado inicial
        System.out.println("✅ Sistema básico funcionando correctamente");
        System.out.println("Gestor: " + gestor);
        System.out.println("PCB1: " + pcb1);
        System.out.println("PCB2: " + pcb2);
    }
}
