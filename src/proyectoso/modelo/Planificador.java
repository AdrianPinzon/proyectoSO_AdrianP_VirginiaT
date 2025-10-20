/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public interface Planificador {
    // CAMBIO CRÍTICO: Usa ColaPCB en lugar de List<PCB>
    PCB seleccionarSiguiente(ColaPCB colaListos);
    String getNombre();
}
