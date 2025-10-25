/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class SPNPlanificador implements Planificador {
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) {
        // 1. Verificar si la cola está vacía
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }

        // 2. Obtener el array de procesos (la operación protegida por semáforo)
        PCB[] procesos = colaListos.toArray();

        // 3. VERIFICACIÓN CRÍTICA CONTRA ARRAY VACÍO
        if (procesos.length == 0) { 
            return null; // Retorna null de forma segura si el array está vacío
        }

        // 4. Inicializar 'seleccionado' (Esta era la línea que causaba el error si length == 0)
        PCB seleccionado = procesos[0]; 

        // 5. Lógica de SPN: Buscar el proceso con el menor tiempo TOTAL de servicio
        for (PCB pcb : procesos) {
            if (pcb.getTotalInstrucciones() < seleccionado.getTotalInstrucciones()) {
                seleccionado = pcb;
            }
        }
        return seleccionado;
    }

    @Override
    public String getNombre() {
        return "Shortest Process Next (SPN)";
    }
}