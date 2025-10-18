/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

import java.util.List;
import java.util.Random;

public class LoteriaPlanificador implements Planificador {
    private Random random;
    
    public LoteriaPlanificador() {
        this.random = new Random();
    }
    
    @Override
    public Proceso seleccionarSiguiente(List<Proceso> colaListos) {
        if (colaListos == null || colaListos.isEmpty()) {
            return null;
        }
        
        // Lotería: Selección aleatoria
        int indiceAleatorio = random.nextInt(colaListos.size());
        return colaListos.get(indiceAleatorio);
    }
    
    @Override
    public String getNombre() {
        return "Planificador por Lotería";
    }
}
