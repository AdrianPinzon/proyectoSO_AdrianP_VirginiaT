/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
Proporciona funcionalidad centralizada para el registro de eventos (logging)
 * de la simulación.
 * Cada entrada de log incluye una marca de tiempo (timestamp) y es escrita 
 * tanto en la consola (stdout) como en un archivo persistente (simulacion.log).
*/

public class Logger {
    private static final String ARCHIVO_LOG = "simulacion.log";
    private SimpleDateFormat dateFormat;
    
    public Logger() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public void log(String mensaje) {
        String timestamp = dateFormat.format(new Date());
        String logEntry = "[" + timestamp + "] " + mensaje;
        
        // Imprimir en consola
        System.out.println(logEntry);
        
        // Escribir en archivo
        escribirEnArchivo(logEntry);
    }
    
    public void logEventoScheduler(String mensaje) {
        log("[SCHEDULER] " + mensaje);
    }
    
    public void logCambioEstado(String proceso, String estadoAnterior, String estadoNuevo) {
        log("[ESTADO] " + proceso + ": " + estadoAnterior + " → " + estadoNuevo);
    }
    
    public void logExcepcionES(String proceso, String tipoEvento) {
        log("[E/S] " + proceso + " - " + tipoEvento);
    }
    
    private void escribirEnArchivo(String logEntry) {
        try (FileWriter fw = new FileWriter(ARCHIVO_LOG, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(logEntry);
        } catch (Exception e) {
            System.err.println("Error escribiendo en log: " + e.getMessage());
        }
    }
    
    public void limpiarLog() {
        try (FileWriter fw = new FileWriter(ARCHIVO_LOG);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.print(""); // Limpiar archivo
        } catch (Exception e) {
            System.err.println("Error limpiando log: " + e.getMessage());
        }
    }
}
