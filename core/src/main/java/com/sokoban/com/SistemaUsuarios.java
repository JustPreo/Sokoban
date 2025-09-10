package com.sokoban.com;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class SistemaUsuarios {
    private static SistemaUsuarios instancia;
    private Usuario usuarioActual;
    private Map<String, Usuario> usuariosCargados;
    private final ReentrantLock lock = new ReentrantLock();
    
    // Singleton de GestorArchivos
    private final GestorArchivos gestorArchivos;

    private SistemaUsuarios() {
        usuariosCargados = new HashMap<>();
        gestorArchivos = GestorArchivos.getInstancia();
    }
    
    public static synchronized SistemaUsuarios getInstance() {
        if (instancia == null) {
            instancia = new SistemaUsuarios();
        }
        return instancia;
    }
    
    /** Registra un nuevo usuario */
    public boolean registrarUsuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        lock.lock();
        try {
            if (gestorArchivos.existeUsuario(nombreUsuario)) {
                System.out.println("Usuario ya existe: " + nombreUsuario);
                return false;
            }
            
            if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
                System.out.println("Nombre de usuario inválido");
                return false;
            }
            
            if (contrasena == null || contrasena.length() < 4) {
                System.out.println("Contraseña debe tener al menos 4 caracteres");
                return false;
            }
            
            if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
                System.out.println("Nombre completo inválido");
                return false;
            }
            
            Usuario nuevoUsuario = new Usuario(nombreUsuario.trim(), contrasena, nombreCompleto.trim());
            
            boolean guardado = gestorArchivos.guardarUsuario(nuevoUsuario);
            if (guardado) {
                usuariosCargados.put(nombreUsuario, nuevoUsuario);
                System.out.println("Usuario registrado exitosamente: " + nombreUsuario);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
    
    /** Inicia sesión con un usuario */
    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        lock.lock();
        try {
            if (!gestorArchivos.existeUsuario(nombreUsuario)) {
                System.out.println("Usuario no encontrado: " + nombreUsuario);
                return false;
            }
            
            Usuario usuario = usuariosCargados.get(nombreUsuario);
            if (usuario == null) {
                usuario = gestorArchivos.cargarUsuario(nombreUsuario);
                if (usuario == null) {
                    System.out.println("Error cargando usuario: " + nombreUsuario);
                    return false;
                }
                usuariosCargados.put(nombreUsuario, usuario);
            }
            
            if (!usuario.verificarContrasena(contrasena)) {
                System.out.println("Contraseña incorrecta para: " + nombreUsuario);
                return false;
            }
            
            usuario.iniciarSesion();
            usuarioActual = usuario;
            gestorArchivos.guardarUsuario(usuario); // Actualizar última sesión
            
            System.out.println("Sesión iniciada exitosamente para: " + nombreUsuario);
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    /** Cierra la sesión actual */
    public void cerrarSesion() {
        lock.lock();
        try {
            if (usuarioActual != null) {
                gestorArchivos.guardarUsuario(usuarioActual);
                System.out.println("Sesión cerrada para: " + usuarioActual.getNombreUsuario());
                usuarioActual = null;
            }
        } finally {
            lock.unlock();
        }
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    /** Guarda el progreso del usuario actual */
    public boolean guardarProgreso() {
        lock.lock();
        try {
            if (usuarioActual == null) {
                System.out.println("No hay usuario activo para guardar");
                return false;
            }
            return gestorArchivos.guardarUsuario(usuarioActual);
        } finally {
            lock.unlock();
        }
    }
    
    /** Registra una nueva partida */
    public void registrarPartida(RegistroPartida partida) {
        lock.lock();
        try {
            if (usuarioActual != null) {
                usuarioActual.registrarPartida(partida);
                
                if (partida.isCompletada()) {
                    int nivelCompletado = partida.getNivel();
                    int nivelMaximo = usuarioActual.getNivelMaximoAlcanzado();
                    
                    if (nivelCompletado >= nivelMaximo && nivelCompletado < 7) {
                        usuarioActual.setNivelMaximoAlcanzado(nivelCompletado + 1);
                        System.out.println("¡Nivel " + (nivelCompletado + 1) + " desbloqueado!");
                    }
                }
                
                guardarProgreso();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public EstadisticasNivel getEstadisticasNivel(int nivel) {
        if (usuarioActual == null) return new EstadisticasNivel(nivel);
        return usuarioActual.getEstadisticasNivel(nivel);
    }
    
    public boolean nivelDesbloqueado(int nivel) {
        if (usuarioActual == null) return nivel == 1;
        return nivel <= usuarioActual.getNivelMaximoAlcanzado();
    }
    
    public String[] listarUsuarios() {
        return gestorArchivos.listarUsuarios().toArray(new String[0]);
    }
    
    public boolean actualizarConfiguracion(ConfiguracionUsuario nuevaConfig) {
        lock.lock();
        try {
            if (usuarioActual == null) return false;
            usuarioActual.setPreferencias(nuevaConfig);
            return guardarProgreso();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean agregarAmigo(String nombreAmigo) {
        lock.lock();
        try {
            if (usuarioActual == null) return false;
            
            if (!gestorArchivos.existeUsuario(nombreAmigo)) {
                System.out.println("Usuario no encontrado: " + nombreAmigo);
                return false;
            }
            
            boolean agregado = usuarioActual.agregarAmigo(nombreAmigo);
            if (agregado) guardarProgreso();
            return agregado;
        } finally {
            lock.unlock();
        }
    }
    
    public String[] getRankingGlobal() {
        return listarUsuarios(); // Temporal, se puede ordenar por puntuación
    }
    
    public boolean crearBackup() {
        if (usuarioActual == null) return false;
        return gestorArchivos.crearBackup(usuarioActual.getNombreUsuario());
    }
    
    public static boolean validarNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) return false;
        String nombre = nombreUsuario.trim();
        return nombre.length() >= 3 && nombre.length() <= 20 && nombre.matches("^[a-zA-Z0-9_]+$");
    }
    
    public String getResumenUsuarioActual() {
        if (usuarioActual == null) return "No hay usuario activo";
        return usuarioActual.getResumenUsuario();
    }
}
