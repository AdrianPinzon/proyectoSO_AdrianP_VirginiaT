/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoso;

import proyectoso.controlador.ControladorSimulador;
import proyectoso.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Versión simplificada sin LookAndFeel
        try {
            System.out.println("Iniciando Simulador de Planificación de Procesos...");
            
            // Crear controlador
            ControladorSimulador controlador = new ControladorSimulador();
            
            // Crear y mostrar ventana principal
            new VentanaPrincipal(controlador);
            
            System.out.println("Simulador iniciado correctamente");
            System.out.println("Universidad Metropolitana - Sistemas Operativos - Proyecto 1  - Adrian Pinzon y Virginia Torrealba");
            System.out.println("Trimestre: 2526-1");
            
        } catch (Exception e) {
            System.err.println("Error crítico iniciando la aplicación: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                null, 
                "Error iniciando la aplicación: " + e.getMessage(), 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
}