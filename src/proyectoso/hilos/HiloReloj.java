/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.hilos;

import proyectoso.controlador.ControladorSimulador;

public class HiloReloj extends Thread {
    private ControladorSimulador controlador;
    private volatile boolean ejecutando;
    private int intervaloMs;
    
    public HiloReloj(ControladorSimulador controlador, int intervaloMs) {
        this.controlador = controlador;
        this.ejecutando = false;
        this.intervaloMs = intervaloMs;
    }
    
    @Override
    public void run() {
        ejecutando = true;
        int ciclo = 0;
        
        while (ejecutando) {
            try {
                Thread.sleep(intervaloMs);
                ciclo++;
                
                // Notificar al controlador del tick del reloj
                controlador.onTickReloj(ciclo);
                
            } catch (InterruptedException e) {
                System.out.println("Hilo reloj interrumpido");
                break;
            }
        }
    }
    
    public void detener() {
        ejecutando = false;
        interrupt();
    }
    
    public void setIntervalo(int intervaloMs) {
        this.intervaloMs = intervaloMs;
    }
}
