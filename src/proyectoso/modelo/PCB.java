/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;

public class PCB {
    private String id;
    private Estado estado;
    private String nombreProceso;
    private int programCounter;
    private int memoryAddressRegister;
    private int instruccionesEjecutadas;
    private int totalInstrucciones;
    
    public PCB(Proceso proceso) {
        this.id = proceso.getId();
        this.nombreProceso = proceso.getNombre();
        this.estado = Estado.NUEVO;
        this.programCounter = 0;
        this.memoryAddressRegister = 0;
        this.instruccionesEjecutadas = 0;
        this.totalInstrucciones = proceso.getTotalInstrucciones();
    }
    
    // Getters
    public String getId() { return id; }
    public Estado getEstado() { return estado; }
    public String getNombreProceso() { return nombreProceso; }
    public int getProgramCounter() { return programCounter; }
    public int getMemoryAddressRegister() { return memoryAddressRegister; }
    public int getInstruccionesEjecutadas() { return instruccionesEjecutadas; }
    public int getTotalInstrucciones() { return totalInstrucciones; }
    
    // Setters
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setProgramCounter(int pc) { this.programCounter = pc; }
    public void setMemoryAddressRegister(int mar) { this.memoryAddressRegister = mar; }
    
    // Métodos para simulación
    public void incrementarPC() {
        this.programCounter++;
    }
    
    public void incrementarMAR() {
        this.memoryAddressRegister++;
    }
    
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
        return "PCB[" + id + "] - PC: " + programCounter + ", MAR: " + memoryAddressRegister + 
               ", Inst: " + instruccionesEjecutadas + "/" + totalInstrucciones + " - " + estado;
    }
}
