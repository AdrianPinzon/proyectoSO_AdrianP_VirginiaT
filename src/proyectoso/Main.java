/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoso;

import proyectoso.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Simulador de Planificación de Procesos");
        
        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Mostrar la ventana principal
        java.awt.EventQueue.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
            System.out.println("✅ Interfaz gráfica iniciada");
        });
    }
}
