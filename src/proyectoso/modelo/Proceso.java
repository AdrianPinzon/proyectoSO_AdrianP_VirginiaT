/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class Proceso {
    private String id;
    private String nombre;
    private int totalInstrucciones;
    private int instruccionesEjecutadas;
    private Estado estado;
    private TipoProceso tipo;
    private int ciclosParaExcepcion; // Solo para IO_BOUND
    private int ciclosParaSatisfacer; // Solo para IO_BOUND
    
    // Constructor
    public Proceso(String id, String nombre, int totalInstrucciones, TipoProceso tipo) {
        this.id = id;
        this.nombre = nombre;
        this.totalInstrucciones = totalInstrucciones;
        this.tipo = tipo;
        this.estado = Estado.NUEVO;
        this.instruccionesEjecutadas = 0;
        this.ciclosParaExcepcion = 0;
        this.ciclosParaSatisfacer = 0;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getTotalInstrucciones() { return totalInstrucciones; }
    public int getInstruccionesEjecutadas() { return instruccionesEjecutadas; }
    public Estado getEstado() { return estado; }
    public TipoProceso getTipo() { return tipo; }
    public int getCiclosParaExcepcion() { return ciclosParaExcepcion; }
    public int getCiclosParaSatisfacer() { return ciclosParaSatisfacer; }
    
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setCiclosParaExcepcion(int ciclos) { this.ciclosParaExcepcion = ciclos; }
    public void setCiclosParaSatisfacer(int ciclos) { this.ciclosParaSatisfacer = ciclos; }
    
    // Métodos de utilidad
    public void incrementarInstrucciones() {
        this.instruccionesEjecutadas++;
    }
    
    public int getInstruccionesRestantes() {
        return totalInstrucciones - instruccionesEjecutadas;
    }
    
    public boolean estaTerminado() {
        return instruccionesEjecutadas >= totalInstrucciones;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + id + ") - " + estado + " - " + instruccionesEjecutadas + "/" + totalInstrucciones;
    }
}
