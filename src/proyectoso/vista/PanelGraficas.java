/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoso.vista;

import proyectoso.util.Metricas;
import proyectoso.util.HistorialMetricas;
import javax.swing.*;
import java.awt.*;

/*
 * Muestra el Throughput, la Utilización de CPU y el Tiempo de Respuesta Promedio
 * como gráficos de línea utilizando un historial de métricas con buffer circular.
 *
 */


public class PanelGraficas extends JPanel {
    private HistorialMetricas historial;
    
    public PanelGraficas() {
        this.historial = new HistorialMetricas(50); // 50 puntos máximo
        setPreferredSize(new Dimension(600, 400));
        setBorder(BorderFactory.createTitledBorder("Gráficas de Rendimiento en Tiempo Real"));
    }
    
    public void agregarMetricas(Metricas metricas) {
        historial.agregar(
            metricas.getThroughput(),
            metricas.getUtilizacionCPU(),
            metricas.getTiempoRespuestaPromedio()
        );
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int graphWidth = width - 2 * padding;
        int graphHeight = height - 2 * padding;
        
        // Dibujar ejes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, padding, padding, height - padding); // Eje Y
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // Eje X
        
        if (historial.getTamaño() == 0) return;
        
        // Encontrar valores máximos para escalar
        double maxThroughput = historial.getMaxThroughput();
        double maxUtilizacion = historial.getMaxUtilizacion();
        double maxTiempoRespuesta = historial.getMaxTiempoRespuesta();
        
        double globalMax = Math.max(maxThroughput, Math.max(maxUtilizacion, maxTiempoRespuesta));
        
        // Dibujar líneas de las métricas
        dibujarLineaMetrica(g2d, historial, Color.BLUE, "Throughput", globalMax, padding, graphWidth, graphHeight, 0);
        dibujarLineaMetrica(g2d, historial, Color.RED, "Utilización CPU", globalMax, padding, graphWidth, graphHeight, 1);
        dibujarLineaMetrica(g2d, historial, Color.GREEN, "Tiempo Respuesta", globalMax, padding, graphWidth, graphHeight, 2);
        
        // Leyenda
        dibujarLeyenda(g2d, width, padding);
        
        // Títulos de ejes
        g2d.setColor(Color.BLACK);
        g2d.drawString("Tiempo (ciclos)", width / 2 - 30, height - 10);
        g2d.drawString("Valor", 10, height / 2);
    }
    
    private void dibujarLineaMetrica(Graphics2D g2d, HistorialMetricas historial, Color color, 
                                   String nombre, double maxValor, int padding, 
                                   int graphWidth, int graphHeight, int tipoMetrica) {
        int pointCount = historial.getTamaño();
        if (pointCount == 0) return;
        
        g2d.setColor(color);
        
        int[] xPoints = new int[pointCount];
        int[] yPoints = new int[pointCount];
        
        for (int i = 0; i < pointCount; i++) {
            xPoints[i] = padding + (i * graphWidth) / Math.max(1, pointCount - 1);
            
            double valor = 0;
            switch (tipoMetrica) {
                case 0: valor = historial.getThroughput(i); break;
                case 1: valor = historial.getUtilizacion(i); break;
                case 2: valor = historial.getTiempoRespuesta(i); break;
            }
            
            int y = (int) (padding + graphHeight - (valor / maxValor) * graphHeight);
            yPoints[i] = y;
            
            // Dibujar punto
            g2d.fillOval(xPoints[i] - 2, y - 2, 4, 4);
        }
        
        // Dibujar línea conectando puntos
        for (int i = 0; i < pointCount - 1; i++) {
            g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }
    }
    
    private void dibujarLeyenda(Graphics2D g2d, int width, int padding) {
        int legendX = width - 150;
        int legendY = padding + 20;
        
        String[] leyendas = {"Throughput", "Utilización CPU", "Tiempo Respuesta"};
        Color[] colores = {Color.BLUE, Color.RED, Color.GREEN};
        
        for (int i = 0; i < leyendas.length; i++) {
            g2d.setColor(colores[i]);
            g2d.fillRect(legendX, legendY + i * 20, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(leyendas[i], legendX + 15, legendY + i * 20 + 10);
        }
    }
    
    public void limpiarHistorial() {
        historial.limpiar();
        repaint();
    }
}
