/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.List;

public interface Planificador {
    Proceso seleccionarSiguiente(List<Proceso> colaListos);
    String getNombre();
}
