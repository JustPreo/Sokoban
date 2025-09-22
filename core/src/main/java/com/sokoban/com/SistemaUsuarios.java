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

    // clase para resultado de login con mas informacion
    public static class ResultadoLogin {
        public final boolean exitoso;
        public final boolean esPrimeraSesion;
        public final String mensaje;
        
        public ResultadoLogin(boolean exitoso, boolean esPrimeraSesion, String mensaje) {
            this.exitoso = exitoso;
            this.esPrimeraSesion = esPrimeraSesion;
            this.mensaje = mensaje;
        }
    }

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
    
    /** Inicia sesion con informacion adicional sobre primera vez */
    public ResultadoLogin iniciarSesionConInfo(String nombreUsuario, String contrasena) {
        lock.lock();
        try {
            if (!gestorArchivos.existeUsuario(nombreUsuario)) {
                System.out.println("Usuario no encontrado: " + nombreUsuario);
                return new ResultadoLogin(false, false, "Usuario no encontrado");
            }
            
            Usuario usuario = usuariosCargados.get(nombreUsuario);
            if (usuario == null) {
                usuario = gestorArchivos.cargarUsuario(nombreUsuario);
                if (usuario == null) {
                    System.out.println("Error cargando usuario: " + nombreUsuario);
                    return new ResultadoLogin(false, false, "Error cargando usuario");
                }
                usuariosCargados.put(nombreUsuario, usuario);
            }
            
            if (!usuario.verificarContrasena(contrasena)) {
                System.out.println("Contraseña incorrecta para: " + nombreUsuario);
                return new ResultadoLogin(false, false, "Contraseña incorrecta");
            }
            
            // revisar si es primera sesion
            boolean esPrimera = usuario.esPrimeraSesion();
            
            usuario.iniciarSesion();
            usuarioActual = usuario;
            
            // marcar que ya no es primera sesion y guardar
            if (esPrimera) {
                usuario.setPrimeraSesion(false);
            }
            
            gestorArchivos.guardarUsuario(usuario);
            
            System.out.println("Sesión iniciada exitosamente para: " + nombreUsuario + 
                              (esPrimera ? " (Primera sesión)" : ""));
            
            return new ResultadoLogin(true, esPrimera, "Sesión iniciada exitosamente");
            
        } finally {
            lock.unlock();
        }
    }
    
    /** Inicia sesión con un usuario*/
    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        return iniciarSesionConInfo(nombreUsuario, contrasena).exitoso;
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
    
    public boolean enviarSolicitudAmistad(String nombreDestino) {
    lock.lock();
    try {
        if (usuarioActual == null || !gestorArchivos.existeUsuario(nombreDestino)) {
            return false;
        }
        
        if (nombreDestino.equals(usuarioActual.getNombreUsuario())) {
            return false;
        }
        
        if (usuarioActual.getListaAmigos().contains(nombreDestino)) {
            return false;
        }
        
        if (usuarioActual.getSolicitudesEnviadas().contains(nombreDestino)) {
            return false;
        }
        
        Usuario destino = cargarDatosUsuario(nombreDestino);
        if (destino == null) return false;
        
        usuarioActual.getSolicitudesEnviadas().add(nombreDestino);
        destino.getSolicitudesPendientes().add(usuarioActual.getNombreUsuario());
        
        guardarProgreso();
        gestorArchivos.guardarUsuario(destino);
        return true;
    } finally {
        lock.unlock();
    }
}

public boolean aceptarSolicitud(String nombreRemitente) {
    lock.lock();
    try {
        if (usuarioActual == null) return false;
        
        if (!usuarioActual.getSolicitudesPendientes().contains(nombreRemitente)) {
            return false;
        }
        
        usuarioActual.getSolicitudesPendientes().remove(nombreRemitente);
        usuarioActual.agregarAmigo(nombreRemitente);
        
        Usuario remitente = cargarDatosUsuario(nombreRemitente);
        if (remitente != null) {
            remitente.getSolicitudesEnviadas().remove(usuarioActual.getNombreUsuario());
            remitente.agregarAmigo(usuarioActual.getNombreUsuario());
            gestorArchivos.guardarUsuario(remitente);
        }
        
        return guardarProgreso();
    } finally {
        lock.unlock();
    }
}

public boolean rechazarSolicitud(String nombreRemitente) {
    lock.lock();
    try {
        if (usuarioActual == null) return false;
        
        if (!usuarioActual.getSolicitudesPendientes().contains(nombreRemitente)) {
            return false;
        }
        
        usuarioActual.getSolicitudesPendientes().remove(nombreRemitente);
        
        Usuario remitente = cargarDatosUsuario(nombreRemitente);
        if (remitente != null) {
            remitente.getSolicitudesEnviadas().remove(usuarioActual.getNombreUsuario());
            gestorArchivos.guardarUsuario(remitente);
        }
        
        return guardarProgreso();
    } finally {
        lock.unlock();
    }
}
    
    /** Cargar datos de un usuario especifico (para comparaciones, etc) */
    public Usuario cargarDatosUsuario(String nombreUsuario) {
        if (!gestorArchivos.existeUsuario(nombreUsuario)) {
            return null;
        }
        
        // primero revisar si ya esta cargado
        Usuario usuario = usuariosCargados.get(nombreUsuario);
        if (usuario != null) {
            return usuario;
        }
        
        // cargar desde archivo
        usuario = gestorArchivos.cargarUsuario(nombreUsuario);
        if (usuario != null) {
            usuariosCargados.put(nombreUsuario, usuario);
        }
        
        return usuario;
    }
    
    public String[] getRankingGlobal() {
        return listarUsuarios(); // se puede mejorar ordenando por puntuacion
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
    
    /** Metodo helper para verificar si un usuario es nuevo */
    public boolean esUsuarioNuevo(String nombreUsuario) {
        Usuario usuario = cargarDatosUsuario(nombreUsuario);
        return usuario != null && usuario.getPartidasTotales() == 0;
    }
    
    /** Obtener cantidad total de usuarios registrados */
    public int getCantidadUsuarios() {
        return gestorArchivos.listarUsuarios().size();
    }
    
}