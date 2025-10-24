/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;
import java.util.concurrent.Semaphore;

/*
 * Representa una implementación de una Cola (FIFO) de Bloques de Control de Proceso (PCB).
 * Garantizar la exclusión mutua durante todas las operaciones de modificación 
 * y acceso a la estructura de datos interna. 
*/

public class ColaPCB {
    private NodoPCB primero;
    private NodoPCB ultimo;
    private int tamaño;
    
    private final Semaphore semaforoCola = new Semaphore(1);

    /**
    * Verifica si la cola está vacía
    */
   public boolean estaVacia() {
       boolean vacia = false; // Valor por defecto en caso de fallo
       try {
           semaforoCola.acquire(); 
           // 👈 Lógica corregida: La cola está vacía si el puntero 'primero' es nulo
           vacia = (primero == null); 
       } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
       } finally {
           semaforoCola.release(); 
       }
       return vacia;
   }
    
    // Clase interna para los nodos
    private static class NodoPCB {
        PCB pcb;
        NodoPCB siguiente;
        
        NodoPCB(PCB pcb) {
            this.pcb = pcb;
            this.siguiente = null;
        }
    }
    
    public ColaPCB() {
        this.primero = null;
        this.ultimo = null;
        this.tamaño = 0;
    }
    
    // MÉTODOS BÁSICOS DE COLA
    
    /**
     * Agrega un PCB al final de la cola (enqueue)
     */
    public void agregar(PCB pcb) {
        try {
            semaforoCola.acquire(); // 👈 1. BLOQUEA (Si otro hilo está dentro, espera aquí)

            // 2. LÓGICA CRÍTICA DE LA COLA 
            NodoPCB nuevoNodo = new NodoPCB(pcb);

            if (ultimo != null) {
                ultimo.siguiente = nuevoNodo;
            }
            ultimo = nuevoNodo;

            if (primero == null) {
                primero = nuevoNodo;
            }

            tamaño++;

        } catch (InterruptedException e) {
            // Manejo si el hilo es interrumpido mientras espera el semáforo
            Thread.currentThread().interrupt();
        } finally {
            // 3. LIBERA (Se ejecuta siempre, incluso si hay una excepción)
            semaforoCola.release(); 
        }
    }
    /**
     * Remueve y retorna el primer PCB de la cola (dequeue)
     */
    public PCB remover() {
        PCB pcb = null;
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA el acceso

            // --- LÓGICA CRÍTICA ORIGINAL (Inicio) ---
            if (primero == null) {
                // Si está vacía, liberamos y retornamos nulo
                return null; 
            }

            pcb = primero.pcb;
            primero = primero.siguiente;

            if (primero == null) {
                ultimo = null;
            }

            tamaño--;
            // --- LÓGICA CRÍTICA ORIGINAL (Fin) ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforoCola.release(); // 👈 LIBERA el acceso
        }
        return pcb;
    }    
    /**
    * Retorna el primer PCB sin removerlo (peek)
    */
    public PCB getPrimero() {
        PCB pcb = null;
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA el acceso

            // --- LÓGICA CRÍTICA ORIGINAL ---
            pcb = (primero != null ? primero.pcb : null);
            // --- LÓGICA CRÍTICA ORIGINAL ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
           semaforoCola.release(); // 👈 LIBERA el acceso
        }
        return pcb;
    }
    
    /**
    * Retorna el tamaño de la cola
    */
    public int getTamaño() {
        int size = 0;
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA
            size = tamaño;           // Lectura crítica
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforoCola.release(); // 👈 LIBERA
        }
        return size;
    }
    
    // MÉTODOS ADICIONALES ÚTILES PARA EL SIMULADOR
    
    /**
    * Busca un PCB por su ID
    */
    public PCB buscarPorId(String id) {
        PCB encontrado = null;
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA

            // --- LÓGICA CRÍTICA ORIGINAL (Inicio) ---
            NodoPCB actual = primero;
            while (actual != null) {
                if (actual.pcb.getId().equals(id)) {
                    encontrado = actual.pcb;
                    break;
                }
                actual = actual.siguiente;
            }
            // --- LÓGICA CRÍTICA ORIGINAL (Fin) ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforoCola.release(); // 👈 LIBERA
        }
        return encontrado;
    }
    
    /**
     * Remueve un PCB específico de la cola por referencia
     */
    /**
    * Remueve un PCB específico de la cola por referencia
    */
    public boolean removerPCB(PCB pcb) {
        boolean removido = false;
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA el acceso

            // --- LÓGICA CRÍTICA ORIGINAL (Inicio) ---
            if (primero == null) return false;

            // Caso especial: si es el primero
            if (primero.pcb == pcb) {
                // NO llamas a remover() aquí, ya que remover() haría un doble acquire/release.
                // Implementa la lógica directamente:
                primero = primero.siguiente;
                if (primero == null) {
                    ultimo = null;
                }
                tamaño--;
                removido = true;
                return removido;
            }

            // Buscar el nodo anterior al que contiene el PCB
            NodoPCB actual = primero;
            while (actual.siguiente != null && actual.siguiente.pcb != pcb) {
                actual = actual.siguiente;
            }
        
            if (actual.siguiente != null) {
                // Encontrado, saltar este nodo
                actual.siguiente = actual.siguiente.siguiente;

                // Actualizar último si necesario
                if (actual.siguiente == null) {
                    ultimo = actual;
                }

                tamaño--;
                removido = true;
            }
            // --- LÓGICA CRÍTICA ORIGINAL (Fin) ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Asegurarse de liberar el semáforo en todos los casos.
            semaforoCola.release(); 
        }
        return removido;
    }
    
    /**
     * Convierte la cola a array para facilitar la iteración en la interfaz
     */
    public PCB[] toArray() {
        PCB[] array = new PCB[tamaño]; // El tamaño debe ser protegido
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA: Garantiza que la lectura es consistente

            // --- LÓGICA CRÍTICA ORIGINAL (Inicio) ---
            NodoPCB actual = primero;
            int index = 0;

            while (actual != null) {
                array[index++] = actual.pcb;
                actual = actual.siguiente;
            }
            // --- LÓGICA CRÍTICA ORIGINAL (Fin) ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Nota: Si se interrumpe, el array podría estar incompleto
        } finally {
            semaforoCola.release(); // 👈 LIBERA
        }
        return array;
    }    
    /**
    * Limpia toda la cola
    */
    public void limpiar() {
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA

            // --- LÓGICA CRÍTICA ORIGINAL (Inicio) ---
            primero = null;
            ultimo = null;
            tamaño = 0;
            // --- LÓGICA CRÍTICA ORIGINAL (Fin) ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforoCola.release(); // 👈 LIBERA
        }
    }
    
    /**
    * Verifica si la cola contiene un PCB específico
    */
    public boolean contiene(PCB pcb) {
        boolean found = false;
        try {
            semaforoCola.acquire(); // 👈 BLOQUEA

            // --- LÓGICA CRÍTICA ORIGINAL (Inicio) ---
            NodoPCB actual = primero;
            while (actual != null) {
                if (actual.pcb == pcb) {
                    found = true;
                    break;
                }
                actual = actual.siguiente;
            }
            // --- LÓGICA CRÍTICA ORIGINAL (Fin) ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforoCola.release(); // 👈 LIBERA
        }
        return found;
    }
    
    @Override
    public String toString() {
        if (estaVacia()) {
            return "ColaPCB[vacía]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("ColaPCB[");
        NodoPCB actual = primero;
        while (actual != null) {
            sb.append(actual.pcb.getNombre());
            if (actual.siguiente != null) {
                sb.append(" -> ");
            }
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}
