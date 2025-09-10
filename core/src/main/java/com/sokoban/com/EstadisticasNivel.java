package com.sokoban.com;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasNivel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int numeroNivel;
    private int vecesJugado;
    private int vecesCompletado;
    private long mejorTiempo;           // en milisegundos
    private int menosmovimientos;       // menor cantidad de movimientos
    private int mejorPuntuacion;
    private long tiempoPromedioCompletar;
    private double promedioMovimientos;
    private LocalDateTime primeraCompletado;
    private LocalDateTime ultimaJugado;
    private List<RegistroPartida> partidasDeEstLevel;
    private boolean desbloqueado;
    
    public EstadisticasNivel(int numeroNivel) {
        this.numeroNivel = numeroNivel;
        this.vecesJugado = 0;
        this.vecesCompletado = 0;
        this.mejorTiempo = Long.MAX_VALUE;
        this.menosmovimientos = Integer.MAX_VALUE;
        this.mejorPuntuacion = 0;
        this.tiempoPromedioCompletar = 0;
        this.promedioMovimientos = 0.0;
        this.partidasDeEstLevel = new ArrayList<>();
        this.desbloqueado = (numeroNivel == 1); // Solo el nivel 1 está desbloqueado inicialmente
    }
    
    public void agregarPartida(RegistroPartida partida) {
        if (partida.getNivel() != this.numeroNivel) {
            return; // No es del nivel correcto
        }
        
        this.vecesJugado++;
        this.ultimaJugado = LocalDateTime.now();
        this.partidasDeEstLevel.add(partida);
        
        if (partida.isCompletada()) {
            this.vecesCompletado++;
            
            // Primera vez completado
            if (primeraCompletado == null) {
                primeraCompletado = LocalDateTime.now();
            }
            
            // Actualizar récords
            if (partida.getDuracion() < mejorTiempo) {
                mejorTiempo = partida.getDuracion();
            }
            
            if (partida.getMovimientos() < menosmovimientos) {
                menosmovimientos = partida.getMovimientos();
            }
            
            if (partida.getPuntuacion() > mejorPuntuacion) {
                mejorPuntuacion = partida.getPuntuacion();
            }
            
            // Recalcular promedios
            recalcularPromedios();
        }
    }
    
    private void recalcularPromedios() {
        if (vecesCompletado == 0) {
            tiempoPromedioCompletar = 0;
            promedioMovimientos = 0.0;
            return;
        }
        
        long tiempoTotal = 0;
        int movimientosTotal = 0;
        
        for (RegistroPartida partida : partidasDeEstLevel) {
            if (partida.isCompletada()) {
                tiempoTotal += partida.getDuracion();
                movimientosTotal += partida.getMovimientos();
            }
        }
        
        tiempoPromedioCompletar = tiempoTotal / vecesCompletado;
        promedioMovimientos = (double) movimientosTotal / vecesCompletado;
    }
    
    public double getPorcentajeCompletado() {
        if (vecesJugado == 0) return 0.0;
        return ((double) vecesCompletado / vecesJugado) * 100.0;
    }
    
    public String getCalificacionRendimiento() {
        double porcentaje = getPorcentajeCompletado();
        
        if (porcentaje >= 80) return "Excelente";
        if (porcentaje >= 60) return "Bueno";
        if (porcentaje >= 40) return "Regular";
        if (porcentaje >= 20) return "Necesita práctica";
        return "Principiante";
    }
    
    public String getResumenEstadisticas() {
        String tiempoRecord = (mejorTiempo == Long.MAX_VALUE) ? "N/A" : formatearTiempo(mejorTiempo);
        String movimientosRecord = (menosmovimientos == Integer.MAX_VALUE) ? "N/A" : String.valueOf(menosmovimientos);
        String promedioTiempo = formatearTiempo(tiempoPromedioCompletar);
        
        return String.format("""
            ESTADÍSTICAS NIVEL %d
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            Estado: %s
            
            JUGADAS
            • Veces jugado: %d
            • Veces completado: %d
            • Éxito: %.1f%%
            • Rendimiento: %s
            
            RÉCORDS
            • Mejor tiempo: %s
            • Menos movimientos: %s
            • Mejor puntuación: %d
            
            PROMEDIOS
            • Tiempo promedio: %s
            • Movimientos promedio: %.1f
            
            FECHAS
            • Primer completado: %s
            • Última jugada: %s
            """,
            numeroNivel,
            desbloqueado ? "Desbloqueado" : "Bloqueado",
            vecesJugado, vecesCompletado,
            getPorcentajeCompletado(),
            getCalificacionRendimiento(),
            tiempoRecord, movimientosRecord, mejorPuntuacion,
            promedioTiempo, promedioMovimientos,
            primeraCompletado != null ? primeraCompletado.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A",
            ultimaJugado != null ? ultimaJugado.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A"
        );
    }
    
    private String formatearTiempo(long milisegundos) {
        if (milisegundos == 0) return "0s";
        
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
    public int getNumeroNivel() { return numeroNivel; }
    public int getVecesJugado() { return vecesJugado; }
    public int getVecesCompletado() { return vecesCompletado; }
    public long getMejorTiempo() { return mejorTiempo; }
    public int getMenosmovimientos() { return menosmovimientos; }
    public int getMejorPuntuacion() { return mejorPuntuacion; }
    public long getTiempoPromedioCompletar() { return tiempoPromedioCompletar; }
    public double getPromedioMovimientos() { return promedioMovimientos; }
    public LocalDateTime getPrimeraCompletado() { return primeraCompletado; }
    public LocalDateTime getUltimaJugado() { return ultimaJugado; }
    public boolean isDesbloqueado() { return desbloqueado; }
    public void setDesbloqueado(boolean desbloqueado) { this.desbloqueado = desbloqueado; }
    public List<RegistroPartida> getPartidasDeEstLevel() { return new ArrayList<>(partidasDeEstLevel); }
}