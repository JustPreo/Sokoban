package com.sokoban.com;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroPartida implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int nivel;
    private int movimientos;
    private long duracion; 
    private boolean completada;
    private int puntuacion;
    private String tipoFinalizacion; 
    
    public RegistroPartida(int nivel) {
        this.nivel = nivel;
        this.fechaInicio = LocalDateTime.now();
        this.movimientos = 0;
        this.completada = false;
        this.puntuacion = 0;
        this.tipoFinalizacion = "en_progreso";
    }
    
  
    public void finalizarPartida(boolean completada, String tipoFinalizacion) {
        this.fechaFin = LocalDateTime.now();
        this.duracion = java.time.Duration.between(fechaInicio, fechaFin).toMillis();
        this.completada = completada;
        this.tipoFinalizacion = tipoFinalizacion;
        
        if (completada) {
            calcularPuntuacion();
        }
    }
    
    
    private void calcularPuntuacion() {
       
        int baseScore = nivel * 1000;
        int movimientosPenalizacion = Math.max(0, movimientos - (nivel * 20)); 
        long segundos = duracion / 1000;
        int tiempoPenalizacion = (int) Math.max(0, segundos - (nivel * 60)); 
        
        this.puntuacion = Math.max(100, baseScore - (movimientosPenalizacion * 10) - (tiempoPenalizacion * 2));
    }
    
    
    public void incrementarMovimientos() {
        this.movimientos++;
    }
    
   
    public String getDescripcionPartida() {
        String duracionStr = formatearDuracion(duracion);
        String estado = completada ? "✅ COMPLETADA" : "❌ " + tipoFinalizacion.toUpperCase();
        
        return String.format("""
            PARTIDA NIVEL %d
            Estado: %s
            Movimientos: %d
            Duración: %s
            Puntuación: %d
            Fecha: %s
            """, 
            nivel, estado, movimientos, duracionStr, puntuacion,
            fechaInicio.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }
    
    private String formatearDuracion(long milisegundos) {
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;
        
        if (minutos > 0) {
            return String.format("%dm %ds", minutos, segundos);
        } else {
            return String.format("%ds", segundos);
        }
    }
    
    // Getters y Setters
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public int getNivel() { return nivel; }
    public int getMovimientos() { return movimientos; }
    public void setMovimientos(int movimientos) { this.movimientos = movimientos; }
    public long getDuracion() { return duracion; }
    public boolean isCompletada() { return completada; }
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public String getTipoFinalizacion() { return tipoFinalizacion; }
}
