/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.modelo;


public class PCB {
    // Atributos de identificación
    private String id;
    private String nombre;
    
    // Atributos de ejecución (REQUERIDOS para la interfaz)
    private Estado estado;
    private int programCounter;
    private int memoryAddressRegister;
    private int instruccionesEjecutadas;
    private int totalInstrucciones;
    
    // Atributos de tipo de proceso
    private TipoProceso tipo;
    private int ciclosParaExcepcion;
    private int ciclosParaSatisfacer;
    private int ciclosEsperaES;
    
    // Atributos para métricas y suspensión (REQUERIDOS)
    private boolean suspendido;
    private int tiempoLlegada;
    private int tiempoInicioEjecucion;
    private int tiempoFinalizacion;
    private int memoriaRequerida;
    
    // Constructor
    public PCB(String id, String nombre, int totalInstrucciones, TipoProceso tipo) {
        this.id = id;
        this.nombre = nombre;
        this.totalInstrucciones = totalInstrucciones;
        this.tipo = tipo;
        this.estado = Estado.NUEVO;
        this.programCounter = 0;
        this.memoryAddressRegister = 0;
        this.instruccionesEjecutadas = 0;
        this.suspendido = false;
        this.memoriaRequerida = (int) (Math.random() * 100) + 10; // 10-110 KB
        
        // Configuración para I/O bound
        if (tipo == TipoProceso.IO_BOUND) {
            this.ciclosParaExcepcion = 5;  // Cada 5 ciclos genera E/S
            this.ciclosParaSatisfacer = 3; // 3 ciclos para satisfacer E/S
        } else {
            this.ciclosParaExcepcion = 0;
            this.ciclosParaSatisfacer = 0;
        }
        this.ciclosEsperaES = 0;
    }
    
    // GETTERS Y SETTERS
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Estado getEstado() { return estado; }
    public int getProgramCounter() { return programCounter; }
    public int getMemoryAddressRegister() { return memoryAddressRegister; }
    public int getInstruccionesEjecutadas() { return instruccionesEjecutadas; }
    public int getTotalInstrucciones() { return totalInstrucciones; }
    public TipoProceso getTipo() { return tipo; }
    public int getCiclosParaExcepcion() { return ciclosParaExcepcion; }
    public int getCiclosParaSatisfacer() { return ciclosParaSatisfacer; }
    public boolean isSuspendido() { return suspendido; }
    public int getTiempoLlegada() { return tiempoLlegada; }
    public int getMemoriaRequerida() { return memoriaRequerida; }
    public int getCiclosEsperaES() { return ciclosEsperaES; }
    public int getTiempoInicioEjecucion() { return tiempoInicioEjecucion; }
    public int getTiempoFinalizacion() { return tiempoFinalizacion; }
    
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setProgramCounter(int pc) { this.programCounter = pc; }
    public void setMemoryAddressRegister(int mar) { this.memoryAddressRegister = mar; }
    public void setTiempoLlegada(int tiempo) { this.tiempoLlegada = tiempo; }
    public void setTiempoInicioEjecucion(int tiempo) { this.tiempoInicioEjecucion = tiempo; }
    public void setTiempoFinalizacion(int tiempo) { this.tiempoFinalizacion = tiempo; }
    public void setCiclosParaExcepcion(int ciclos) { this.ciclosParaExcepcion = ciclos; }
    public void setCiclosParaSatisfacer(int ciclos) { this.ciclosParaSatisfacer = ciclos; }
    public void setSuspendido(boolean suspendido) { this.suspendido = suspendido; }
    
    // MÉTODOS DE EJECUCIÓN (ESENCIALES)
    public void ejecutarCiclo() {
        if (estaTerminado() || suspendido || estado == Estado.BLOQUEADO) {
            return;
        }
        
        // Ejecutar instrucción normal (REQUERIDO: ejecución lineal)
        instruccionesEjecutadas++;
        programCounter++;
        memoryAddressRegister++;
        
        // Verificar si genera excepción E/S (solo para I/O bound)
        if (tipo == TipoProceso.IO_BOUND && 
            ciclosParaExcepcion > 0 &&
            instruccionesEjecutadas % ciclosParaExcepcion == 0 &&
            instruccionesEjecutadas > 0) {
            generarExcepcionES();
        }
    }
    
    private void generarExcepcionES() {
        this.estado = Estado.BLOQUEADO;
    }
    
    // MÉTODOS DE UTILIDAD
    public void incrementarPC() { this.programCounter++; }
    public void incrementarMAR() { this.memoryAddressRegister++; }
    
    public int getInstruccionesRestantes() {
        return totalInstrucciones - instruccionesEjecutadas;
    }
    
    public boolean estaTerminado() {
        return instruccionesEjecutadas >= totalInstrucciones;
    }
    
    public boolean esCPU_Bound() {
        return tipo == TipoProceso.CPU_BOUND;
    }
    
    public boolean esIO_Bound() {
        return tipo == TipoProceso.IO_BOUND;
    }
    
    // Métricas de rendimiento (REQUERIDAS para el proyecto)
    public int getTiempoRespuesta() {
        return tiempoFinalizacion - tiempoLlegada;
    }
    
    public int getTiempoEspera() {
        if (tiempoInicioEjecucion == 0) return 0;
        return (tiempoFinalizacion - tiempoLlegada) - getTiempoServicio();
    }
    
    public int getTiempoServicio() {
        return totalInstrucciones; // Cada instrucción = 1 ciclo
    }
    
    @Override
    public String toString() {
        // 👈 CRÍTICO: Debe mostrar el PC, MAR, Instrucciones y el Estado completo
        return nombre + " (" + id + ")" + 
               " - PC: " + programCounter + 
               " - MAR: " + memoryAddressRegister + // 👈 MAR Requerido
               " - Inst: " + instruccionesEjecutadas + "/" + totalInstrucciones + 
               " - " + estado + 
               // 👈 CRÍTICO: Muestra si está SUSPENDIDO
               (suspendido ? " [SUSPENDIDO]" : "") +
               " - " + tipo;
    }
}