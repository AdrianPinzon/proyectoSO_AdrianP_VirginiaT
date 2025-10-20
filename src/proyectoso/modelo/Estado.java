/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public enum Estado {
    NUEVO,
    LISTO, 
    EJECUCION,
    BLOQUEADO,
    TERMINADO,
    SUSPENDIDO,       // REQUERIDO: para gestión de memoria
    LISTO_SUSPENDIDO, // Opcional: para mayor claridad en colas
    BLOQUEADO_SUSPENDIDO // Opcional: para mayor claridad
}