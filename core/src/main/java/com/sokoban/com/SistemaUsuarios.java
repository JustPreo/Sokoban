package com.sokoban.com;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class SistemaUsuarios {
    private SistemaUsuarios instancia;
    private Usuario usuarioActual;
    private Map<String, Usuario> usuariosCargados;
    private final ReentrantLock lock = new ReentrantLock();
    
    private SistemaUsuarios() {
        usuariosCargados = new HashMap<>();
    }
    
    public static synchronized SistemaUsuarios getInstance() {
        if (instancia == null) {
            instancia = new SistemaUsuarios();
        }
        return instancia;
    }
    
    /**
     * Registra un nuevo usuario
     */
    public boolean registrarUsuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        lock.lock();
        try {
            // Verificar si ya existe
            if (GestorArchivos.existeUsuario(nombreUsuario)) {
                System.out.println("Usuario ya existe: " + nombreUsuario);
                return false;
            }
            
            // Validaciones básicas
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
            
            // Crear usuario
            Usuario nuevoUsuario = new Usuario(
                nombreUsuario.trim(),
                contrasena,
                nombreCompleto.trim()
            );
            
            // Guardar en archivo
            boolean guardado = GestorArchivos.guardarUsuario(nuevoUsuario);
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
    
    /**
     * Inicia sesión con un usuario
     */
    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        lock.lock();
        try {
            // Verificar si existe
            if (!GestorArchivos.existeUsuario(nombreUsuario)) {
                System.out.println("Usuario no encontrado: " + nombreUsuario);
                return false;
            }
            
            // Cargar usuario si no está en memoria
            Usuario usuario = usuariosCargados.get(nombreUsuario);
            if (usuario == null) {
                usuario = GestorArchivos.cargarUsuario(nombreUsuario);
                if (usuario == null) {
                    System.out.println("Error cargando usuario: " + nombreUsuario);
                    return false;
                }
                usuariosCargados.put(nombreUsuario, usuario);
            }
            
            // Verificar contraseña
            if (!usuario.verificarContrasena(contrasena)) {
                System.out.println("Contraseña incorrecta para: " + nombreUsuario);
                return false;
            }
            
            // Iniciar sesión
            usuario.iniciarSesion();
            usuarioActual = usuario;
            
            // Guardar cambios (actualizar última sesión)
            GestorArchivos.guardarUsuario(usuario);
            
            System.out.println("Sesión iniciada exitosamente para: " + nombreUsuario);
            return true;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    public  void cerrarSesion() {
        lock.lock();
        try {
            if (usuarioActual != null) {
                // Guardar datos antes de cerrar
                GestorArchivos.guardarUsuario(usuarioActual);
                System.out.println("Sesión cerrada para: " + usuarioActual.getNombreUsuario());
                usuarioActual = null;
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Obtiene el usuario actual
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Verifica si hay una sesión activa
     */
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    /**
     * Guarda el progreso del usuario actual
     */
    public boolean guardarProgreso() {
        lock.lock();
        try {
            if (usuarioActual == null) {
                System.out.println("No hay usuario activo para guardar");
                return false;
            }
            
            return GestorArchivos.guardarUsuario(usuarioActual);
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Registra una nueva partida para el usuario actual
     */
    public void registrarPartida(RegistroPartida partida) {
        lock.lock();
        try {
            if (usuarioActual != null) {
                usuarioActual.registrarPartida(partida);
                
                // Si completó el nivel, desbloquear siguiente
                if (partida.isCompletada()) {
                    int nivelCompletado = partida.getNivel();
                    int nivelMaximo = usuarioActual.getNivelMaximoAlcanzado();
                    
                    if (nivelCompletado >= nivelMaximo && nivelCompletado < 7) {
                        usuarioActual.setNivelMaximoAlcanzado(nivelCompletado + 1);
                        System.out.println("¡Nivel " + (nivelCompletado + 1) + " desbloqueado!");
                    }
                }
                
                // Guardar automáticamente
                guardarProgreso();
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Obtiene las estadísticas de un nivel específico
     */
    public EstadisticasNivel getEstadisticasNivel(int nivel) {
        if (usuarioActual == null) {
            return new EstadisticasNivel(nivel);
        }
        return usuarioActual.getEstadisticasNivel(nivel);
    }
    
    /**
     * Verifica si un nivel está desbloqueado
     */
    public boolean nivelDesbloqueado(int nivel) {
        if (usuarioActual == null) {
            return nivel == 1; // Solo nivel 1 disponible sin usuario
        }
        return nivel <= usuarioActual.getNivelMaximoAlcanzado();
    }
    
    /**
     * Lista todos los usuarios registrados
     */
    public String[] listarUsuarios() {
        return GestorArchivos.listarUsuarios();
    }
    
    /**
     * Actualiza la configuración del usuario actual
     */
    public boolean actualizarConfiguracion(ConfiguracionUsuario nuevaConfig) {
        lock.lock();
        try {
            if (usuarioActual == null) {
                return false;
            }
            
            usuarioActual.setPreferencias(nuevaConfig);
            return guardarProgreso();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Agrega un amigo al usuario actual
     */
    public boolean agregarAmigo(String nombreAmigo) {
        lock.lock();
        try {
            if (usuarioActual == null) {
                return false;
            }
            
            // Verificar que el amigo existe
            if (!GestorArchivos.existeUsuario(nombreAmigo)) {
                System.out.println("Usuario no encontrado: " + nombreAmigo);
                return false;
            }
            
            boolean agregado = usuarioActual.agregarAmigo(nombreAmigo);
            if (agregado) {
                guardarProgreso();
            }
            return agregado;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Obtiene el ranking general de usuarios
     */
    public String[] getRankingGlobal() {
        String[] usuarios = listarUsuarios();
        // Aquí podrías implementar lógica para ordenar por puntuación
        // Por simplicidad, retornamos la lista de usuarios
        return usuarios;
    }
    
    /**
     * Crea una copia de seguridad del usuario actual
     */
    public boolean crearBackup() {
        if (usuarioActual == null) {
            return false;
        }
        return GestorArchivos.crearBackup(usuarioActual.getNombreUsuario());
    }
    
    /**
     * Valida el formato del nombre de usuario
     */
    public static boolean validarNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return false;
        }
        
        String nombre = nombreUsuario.trim();
        
        // Longitud entre 3 y 20 caracteres
        if (nombre.length() < 3 || nombre.length() > 20) {
            return false;
        }
        
        // Solo letras, números y guiones bajos
        return nombre.matches("^[a-zA-Z0-9_]+$");
    }
    
    /**
     * Obtiene información resumida del usuario actual
     */
    public String getResumenUsuarioActual() {
        if (usuarioActual == null) {
            return "No hay usuario activo";
        }
        return usuarioActual.getResumenUsuario();
    }
}