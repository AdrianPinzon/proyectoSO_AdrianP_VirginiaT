/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

/*
 Planificación HRRN
 * prioriza el proceso que tiene la razón (ratio) de respuesta más alta. 
 * Esta razón combina el tiempo de servicio (S) con el tiempo que el proceso ha estado
 * esperando (W), mitigando así la inanición de los procesos largos
*/

public class HRRNPlanificador implements Planificador {
    private boolean prioridadMayorPrimero; // true: mayor prioridad primero
    
    public HRRNPlanificador(boolean prioridadMayorPrimero) {
        this.prioridadMayorPrimero = prioridadMayorPrimero;
    }
    
    @Override
    public PCB seleccionarSiguiente(ColaPCB colaListos, int cicloActual) { // 👈 RECIBE cicloActual
        if (colaListos == null || colaListos.estaVacia()) {
            return null;
        }

        PCB[] procesos = colaListos.toArray();
        double ratioMasAlto = -1.0;
        PCB seleccionado = null;

        for (PCB pcb : procesos) {
            double tiempoServicio = (double) pcb.getTotalInstrucciones(); // S

            // Tiempo de Espera (W) = Ciclo Actual - Tiempo de Llegada
            double tiempoEspera = (double) (cicloActual - pcb.getTiempoLlegada()); // W

            // Fórmula HRRN: (W + S) / S
            double ratio = (tiempoEspera + tiempoServicio) / tiempoServicio;

            if (ratio > ratioMasAlto) {
                ratioMasAlto = ratio;
                seleccionado = pcb;
            }
        }

        return seleccionado;
    }

    @Override
    public String getNombre() {
        return "Highest Response Ratio Next (HRRN)";
    }
}
