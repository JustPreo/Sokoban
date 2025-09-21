package com.sokoban.com;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfiguracionJuego {
    private static ConfiguracionJuego instancia;
    
    // Configuraciones de audio
    private float volumenMusica = SoundManager.getMusicVolume();
    private float volumenEfectos = SoundManager.getSoundVolume();
    private boolean musicaActivada = true;
    private boolean efectosActivados = true;
    
    // Configuraciones de gráficos
    private boolean pantallaCompleta = false;
    private boolean vsync = true;
    private boolean mostrarFPS = false;
    
    // Configuraciones de juego
    private String dificultad = "medio"; // facil, medio, dificil
    private boolean autoGuardar = true;
    private boolean mostrarTutorial = true;
    //Quite esto porque no es necesario
    private Map<String, Integer> controles;  // NUEVO
    
    private ConfiguracionJuego() {
        controles = new HashMap<>();
         // Controles por defecto
        controles.put("arriba", com.badlogic.gdx.Input.Keys.W);
        controles.put("abajo", com.badlogic.gdx.Input.Keys.S);
        controles.put("izquierda", com.badlogic.gdx.Input.Keys.A);
        controles.put("derecha", com.badlogic.gdx.Input.Keys.D);

        controles.put("arribaAlt", com.badlogic.gdx.Input.Keys.UP);
        controles.put("abajoAlt", com.badlogic.gdx.Input.Keys.DOWN);
        controles.put("izquierdaAlt", com.badlogic.gdx.Input.Keys.LEFT);
        controles.put("derechaAlt", com.badlogic.gdx.Input.Keys.RIGHT);
        // configuracion por defecto
    }
    public void setControles(Map<String, Integer> controles) { 
        this.controles = new HashMap<>(controles); 
    }

    public void setControl(String accion, int keycode) {
        controles.put(accion, keycode);
    }

    public int getControl(String accion) {
        return controles.getOrDefault(accion, -1);
    }
    public Map<String, Integer> getControles()
    {
    return controles;
    }
    
    public static ConfiguracionJuego getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionJuego();
        }
        return instancia;
    }
    
    // Getters y Setters para Audio
    public float getVolumenMusica() { return volumenMusica; }
    public void setVolumenMusica(float volumen) { 
        this.volumenMusica = Math.max(0.0f, Math.min(1.0f, volumen)); 
        SoundManager.setMusicVolume(volumenMusica);
    }
    
    public float getVolumenEfectos() { return volumenEfectos; }
    public void setVolumenEfectos(float volumen) { 
        this.volumenEfectos = Math.max(0.0f, Math.min(1.0f, volumen));
        SoundManager.setSoundVolume(volumenEfectos);
        
    }
    
    public boolean isMusicaActivada() { return musicaActivada; }
    public void setMusicaActivada(boolean activada) { this.musicaActivada = activada; }
    
    public boolean isEfectosActivados() { return efectosActivados; }
    public void setEfectosActivados(boolean activados) { this.efectosActivados = activados; }
    
    // Getters y Setters para Gráficos
    public boolean isPantallaCompleta() { return pantallaCompleta; }
    public void setPantallaCompleta(boolean completa) { this.pantallaCompleta = completa; }
    
    public boolean isVsync() { return vsync; }
    public void setVsync(boolean vsync) { this.vsync = vsync; }
    
    public boolean isMostrarFPS() { return mostrarFPS; }
    public void setMostrarFPS(boolean mostrar) { this.mostrarFPS = mostrar; }
    
    // Getters y Setters para Juego
    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    
    public boolean isAutoGuardar() { return autoGuardar; }
    public void setAutoGuardar(boolean auto) { this.autoGuardar = auto; }
    
    public boolean isMostrarTutorial() { return mostrarTutorial; }
    public void setMostrarTutorial(boolean mostrar) { this.mostrarTutorial = mostrar; }
    
    // Métodos de utilidad
    public void restablecerPorDefecto() {
        volumenMusica = SoundManager.getMusicVolume();
        volumenEfectos = SoundManager.getSoundVolume();
        musicaActivada = true;
        efectosActivados = true;
        pantallaCompleta = false;
        vsync = true;
        mostrarFPS = false;
        dificultad = "medio";
        autoGuardar = true;
        mostrarTutorial = true;
    }
    
    public void guardarConfiguracion() {
        // Aquí podrías guardar en archivo o en el perfil del usuario
        SistemaUsuarios sistema = SistemaUsuarios.getInstance();
        if (sistema.haySesionActiva()) {
            // Guardar en el perfil del usuario
            sistema.guardarProgreso();
        }
    }
}