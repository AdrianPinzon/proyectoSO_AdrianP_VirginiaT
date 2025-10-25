/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

/*
 * Interfaz de Planificación
 * permite cambiar dinámicamente el algoritmo 
 * de planificación (FCFS, HRRN, Feedback, etc.) sin cambiar su lógica interna.
*/

public interface Planificador {
    // AÑADIR el ciclo actual (necesario para HRRN)
    PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual); // 👈 CAMBIO AQUÍ
    String getNombre();
}
