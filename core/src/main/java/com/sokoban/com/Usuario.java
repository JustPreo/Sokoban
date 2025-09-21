package com.sokoban.com;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Datos básicos del usuario
    private String nombreUsuario;
    private String contrasenaHash;
    private String nombreCompleto;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimaSesion;
    private boolean esPrimeraSesion;

    // Progreso del juego
    private int nivelActual;
    private int nivelMaximoAlcanzado;
    private long tiempoTotalJugado;
    private int partidasTotales;
    private int partidasCompletadas;

    // Estadísticas 
    private Map<Integer, EstadisticasNivel> estadisticasPorNivel;
    private List<RegistroPartida> historialPartidas;

    // Personalización
    private ConfiguracionUsuario preferencias;
    private String rutaAvatar;

    // Amigos y ranking
    private int puntuacionTotal;
    private List<String> listaAmigos;
    private List<String> listaRivales;

    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = hashContrasena(contrasena);
        this.nombreCompleto = nombreCompleto;
        this.fechaRegistro = LocalDateTime.now();
        this.ultimaSesion = LocalDateTime.now();
        this.esPrimeraSesion = true; // nueva cuenta = primera sesion

        this.nivelActual = 1;
        this.nivelMaximoAlcanzado = 1;
        this.tiempoTotalJugado = 0;
        this.partidasTotales = 0;
        this.partidasCompletadas = 0;
        this.puntuacionTotal = 0;

        this.estadisticasPorNivel = new HashMap<>();
        this.historialPartidas = new ArrayList<>();
        this.listaAmigos = new ArrayList<>();
        this.listaRivales = new ArrayList<>();

        this.preferencias = new ConfiguracionUsuario();
        this.rutaAvatar = "avatars/default.png";

        System.out.println("Usuario creado: " + nombreUsuario);
    }

    public boolean verificarContrasena(String contrasena) {
        return this.contrasenaHash.equals(hashContrasena(contrasena));
    }

    public void iniciarSesion() {
        this.ultimaSesion = LocalDateTime.now();
        System.out.println("Sesión iniciada para: " + nombreUsuario);
    }

    public void registrarPartida(RegistroPartida partida) {
        this.historialPartidas.add(partida);
        this.partidasTotales++;
        this.tiempoTotalJugado += partida.getDuracion();

        if (partida.isCompletada()) {
            this.partidasCompletadas++;
            this.puntuacionTotal += partida.getPuntuacion();

            if (partida.getNivel() > this.nivelMaximoAlcanzado) {
                this.nivelMaximoAlcanzado = partida.getNivel();
            }
        }

        actualizarEstadisticasNivel(partida);
    }

    private void actualizarEstadisticasNivel(RegistroPartida partida) {
        int nivel = partida.getNivel();
        EstadisticasNivel stats = estadisticasPorNivel.get(nivel);

        if (stats == null) {
            stats = new EstadisticasNivel(nivel);
            estadisticasPorNivel.put(nivel, stats);
        }

        stats.agregarPartida(partida);
    }

    public long getTiempoPromedioNivel() {
        if (partidasCompletadas == 0) {
            return 0;
        }
        return tiempoTotalJugado / partidasCompletadas;
    }

    public EstadisticasNivel getEstadisticasNivel(int nivel) {
        return estadisticasPorNivel.getOrDefault(nivel, new EstadisticasNivel(nivel));
    }

    public boolean desbloquearSiguienteNivel() {
        if (nivelActual < nivelMaximoAlcanzado) {
            nivelActual++;
            System.out.println("Nivel " + nivelActual + " desbloqueado!");
            return true;
        }
        return false;
    }

    public boolean agregarAmigo(String nombreAmigo) {
        if (!listaAmigos.contains(nombreAmigo) && !nombreAmigo.equals(this.nombreUsuario)) {
            listaAmigos.add(nombreAmigo);
            System.out.println("Amigo:" + nombreAmigo + " agregado como amigo");
            return true;
        }
        return false;
    }

    public boolean agregarRival(String nombreRival) {
        if (!listaRivales.contains(nombreRival) && !nombreRival.equals(this.nombreUsuario)) {
            listaRivales.add(nombreRival);
            System.out.println("Rival: " + nombreRival + " agregado como rival");
            return true;
        }
        return false;
    }

    public String getResumenUsuario() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return String.format("""
            PERFIL DE USUARIO
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            Nombre: %s (%s)
            Registro: %s
            Última sesión: %s
            
            PROGRESO
            Nivel actual: %d
            Nivel máximo: %d
            Partidas jugadas: %d
            Partidas completadas: %d
            Tiempo total: %s
            
            PUNTUACIÓN
            Puntos totales: %d
            Promedio por nivel: %d min
            
            SOCIAL
            Amigos: %d
            Rivales: %d
            """,
                nombreCompleto, nombreUsuario,
                fechaRegistro.format(formatter),
                ultimaSesion.format(formatter),
                nivelActual, nivelMaximoAlcanzado,
                partidasTotales, partidasCompletadas,
                formatearTiempo(tiempoTotalJugado),
                puntuacionTotal, getTiempoPromedioNivel() / 60000,
                listaAmigos.size(), listaRivales.size()
        );
    }

    private String hashContrasena(String contrasena) {
        int hash = contrasena.hashCode();
        return String.valueOf(Math.abs(hash));
    }

    private String formatearTiempo(long milisegundos) {
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;

        if (horas > 0) {
            return String.format("%dh %dm", horas, minutos % 60);
        } else if (minutos > 0) {
            return String.format("%dm %ds", minutos, segundos % 60);
        } else {
            return String.format("%ds", segundos);
        }
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getUltimaSesion() {
        return ultimaSesion;
    }

    public void setUltimaSesion(LocalDateTime ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(int nivelActual) {
        this.nivelActual = nivelActual;
    }

    public int getNivelMaximoAlcanzado() {
        return nivelMaximoAlcanzado;
    }

    public void setNivelMaximoAlcanzado(int nivelMaximoAlcanzado) {
        this.nivelMaximoAlcanzado = nivelMaximoAlcanzado;
    }

    public long getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }

    public void setTiempoTotalJugado(long tiempoTotalJugado) {
        this.tiempoTotalJugado = tiempoTotalJugado;
    }

    public int getPartidasTotales() {
        return partidasTotales;
    }

    public int getPartidasCompletadas() {
        return partidasCompletadas;
    }

    public ConfiguracionUsuario getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(ConfiguracionUsuario preferencias) {
        this.preferencias = preferencias;
    }

    public String getRutaAvatar() {
        return rutaAvatar;
    }

    public void setRutaAvatar(String rutaAvatar) {
        this.rutaAvatar = rutaAvatar;
    }

    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionTotal(int puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }

    public List<String> getListaAmigos() {
        return new ArrayList<>(listaAmigos);
    }

    public List<String> getListaRivales() {
        return new ArrayList<>(listaRivales);
    }

    public List<RegistroPartida> getHistorialPartidas() {
        return new ArrayList<>(historialPartidas);
    }

    public Map<Integer, EstadisticasNivel> getEstadisticasPorNivel() {
        return new HashMap<>(estadisticasPorNivel);
    }

    public boolean esPrimeraSesion() {
        return esPrimeraSesion;
    }

    public void setPrimeraSesion(boolean esPrimeraSesion) {
        this.esPrimeraSesion = esPrimeraSesion;
    }

    public void aplicarConfiguracionJuego() {
        ConfiguracionJuego.getInstance().setControles(preferencias.getControles());
    }


    public void actualizarConfiguracionDesdeJuego() {
        preferencias.setControles(ConfiguracionJuego.getInstance().getControles());
    }
}
