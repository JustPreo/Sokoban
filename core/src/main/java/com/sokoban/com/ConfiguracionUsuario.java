package com.sokoban.com;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Configuraciones de audio
    private float volumenMusica;
    private float volumenEfectos;
    private boolean sonidoActivado;
    
    // Configuraciones visuales
    private String idioma;
    private String temaVisual;
    private boolean animacionesActivadas;
    private boolean efectosParticulas;
    
    // Configuraciones de juego
    private boolean mostrarAyuda;
    private boolean confirmarMovimientos;
    private int velocidadAnimacion; // 1-5 (lenta a rápida)
    private boolean modoDesafio;
    
    // Configuraciones de control
    private String esquemaControles;
    private boolean invertirEjes;
    
    public ConfiguracionUsuario() {
        // Valores por defecto
        this.volumenMusica = 0.7f;
        this.volumenEfectos = 0.8f;
        this.sonidoActivado = true;
        
        this.idioma = "es";
        this.temaVisual = "clasico";
        this.animacionesActivadas = true;
        this.efectosParticulas = true;
        
        this.mostrarAyuda = true;
        this.confirmarMovimientos = false;
        this.velocidadAnimacion = 3;
        this.modoDesafio = false;
        
        this.esquemaControles = "wasd";
        this.invertirEjes = false;
    }
    
    /**
     * Aplica configuración de audio
     */
    public void aplicarConfiguracionAudio() {
        if (!sonidoActivado) {
            // Silenciar todo
            System.out.println("Audio desactivado");
        } else {
            System.out.println("Música: " + (volumenMusica * 100) + "%, Efectos: " + (volumenEfectos * 100) + "%");
        }
    }
    
    /**
     * Obtiene descripción de la configuración actual
     */
    public String getDescripcionConfig() {
        return String.format("""
             CONFIGURACIÓN ACTUAL
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                Audio:
              • Música: %.0f%%
              • Efectos: %.0f%%
              • Sonido: %s
            
               Visual:
              • Idioma: %s
              • Tema: %s
              • Animaciones: %s
              • Partículas: %s
            
               Juego:
              • Ayuda: %s
              • Velocidad: %d/5
              • Modo desafío: %s
            
               Controles:
              • Esquema: %s
              • Ejes invertidos: %s
            """,
            volumenMusica * 100, volumenEfectos * 100, 
            sonidoActivado ? "Activado" : "Desactivado",
            idioma.toUpperCase(), temaVisual, 
            animacionesActivadas ? "Sí" : "No",
            efectosParticulas ? "Sí" : "No",
            mostrarAyuda ? "Sí" : "No", velocidadAnimacion,
            modoDesafio ? "Sí" : "No",
            esquemaControles.toUpperCase(),
            invertirEjes ? "Sí" : "No"
        );
    }
    
    // Getters y Setters
    public float getVolumenMusica() { return volumenMusica; }
    public void setVolumenMusica(float volumenMusica) { 
        this.volumenMusica = Math.max(0f, Math.min(1f, volumenMusica)); 
    }
    
    public float getVolumenEfectos() { return volumenEfectos; }
    public void setVolumenEfectos(float volumenEfectos) { 
        this.volumenEfectos = Math.max(0f, Math.min(1f, volumenEfectos)); 
    }
    
    public boolean isSonidoActivado() { return sonidoActivado; }
    public void setSonidoActivado(boolean sonidoActivado) { this.sonidoActivado = sonidoActivado; }
    
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    
    public String getTemaVisual() { return temaVisual; }
    public void setTemaVisual(String temaVisual) { this.temaVisual = temaVisual; }
    
    public boolean isAnimacionesActivadas() { return animacionesActivadas; }
    public void setAnimacionesActivadas(boolean animacionesActivadas) { 
        this.animacionesActivadas = animacionesActivadas; 
    }
    
    public boolean isEfectosParticulas() { return efectosParticulas; }
    public void setEfectosParticulas(boolean efectosParticulas) { 
        this.efectosParticulas = efectosParticulas; 
    }
    
    public boolean isMostrarAyuda() { return mostrarAyuda; }
    public void setMostrarAyuda(boolean mostrarAyuda) { this.mostrarAyuda = mostrarAyuda; }
    
    public boolean isConfirmarMovimientos() { return confirmarMovimientos; }
    public void setConfirmarMovimientos(boolean confirmarMovimientos) { 
        this.confirmarMovimientos = confirmarMovimientos; 
    }
    
    public int getVelocidadAnimacion() { return velocidadAnimacion; }
    public void setVelocidadAnimacion(int velocidadAnimacion) { 
        this.velocidadAnimacion = Math.max(1, Math.min(5, velocidadAnimacion)); 
    }
    
    public boolean isModoDesafio() { return modoDesafio; }
    public void setModoDesafio(boolean modoDesafio) { this.modoDesafio = modoDesafio; }
    
    public String getEsquemaControles() { return esquemaControles; }
    public void setEsquemaControles(String esquemaControles) { 
        this.esquemaControles = esquemaControles; 
    }
    
    public boolean isInvertirEjes() { return invertirEjes; }
    public void setInvertirEjes(boolean invertirEjes) { this.invertirEjes = invertirEjes; }
}
