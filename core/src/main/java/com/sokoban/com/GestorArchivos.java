package com.sokoban.com;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class GestorArchivos {
    private static final String DIRECTORIO_BASE = "usuarios";
    private static final String ARCHIVO_USUARIO = "perfil.dat";
    private static final String ARCHIVO_CONFIGURACION = "config.dat";
    private static final String ARCHIVO_ESTADISTICAS = "stats.dat";
    private static final String DIRECTORIO_PARTIDAS = "partidas";
    
    // Singleton para manejar archivos de forma centralizada
    private static GestorArchivos instancia;
    
    private GestorArchivos() {
        crearDirectorioBase();
    }
    
    public static GestorArchivos getInstancia() {
        if (instancia == null) {
            instancia = new GestorArchivos();
        }
        return instancia;
    }
    
    private void crearDirectorioBase() {
        try {
            Path directorio = Paths.get(DIRECTORIO_BASE);
            if (!Files.exists(directorio)) {
                Files.createDirectories(directorio);
                System.out.println("Directorio base creado: " + directorio.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al crear directorio base: " + e.getMessage());
        }
    }
    
    public boolean crearCarpetaUsuario(String nombreUsuario) {
        try {
            Path carpetaUsuario = Paths.get(DIRECTORIO_BASE, nombreUsuario);
            Path carpetaPartidas = carpetaUsuario.resolve(DIRECTORIO_PARTIDAS);
            
            Files.createDirectories(carpetaUsuario);
            Files.createDirectories(carpetaPartidas);
            
            System.out.println("Carpeta creada para usuario: " + nombreUsuario);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear carpeta para usuario " + nombreUsuario + ": " + e.getMessage());
            return false;
        }
    }
    
    public boolean guardarUsuario(Usuario usuario) {
        try {
             crearCarpetaUsuario(usuario.getNombreUsuario());
            Path archivoUsuario = Paths.get(DIRECTORIO_BASE, usuario.getNombreUsuario(), ARCHIVO_USUARIO);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoUsuario.toFile()))) {
                oos.writeObject(usuario);
                System.out.println("Usuario guardado: " + usuario.getNombreUsuario());
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public Usuario cargarUsuario(String nombreUsuario) {
        try {
            Path archivoUsuario = Paths.get(DIRECTORIO_BASE, nombreUsuario, ARCHIVO_USUARIO);
            
            if (!Files.exists(archivoUsuario)) {
                System.out.println("Archivo de usuario no existe: " + nombreUsuario);
                return null;
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoUsuario.toFile()))) {
                Usuario usuario = (Usuario) ois.readObject();
                System.out.println("Usuario cargado: " + nombreUsuario);
                return usuario;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuario: " + e.getMessage());
            return null;
        }
    }
    
    public boolean guardarConfiguracion(String nombreUsuario, ConfiguracionUsuario config) {
        try {
            Path archivoConfig = Paths.get(DIRECTORIO_BASE, nombreUsuario, ARCHIVO_CONFIGURACION);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoConfig.toFile()))) {
                oos.writeObject(config);
                System.out.println("Configuración guardada para: " + nombreUsuario);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error al guardar configuración: " + e.getMessage());
            return false;
        }
    }
    
    public ConfiguracionUsuario cargarConfiguracion(String nombreUsuario) {
        try {
            Path archivoConfig = Paths.get(DIRECTORIO_BASE, nombreUsuario, ARCHIVO_CONFIGURACION);
            
            if (!Files.exists(archivoConfig)) {
                // Retorna configuración por defecto si no existe archivo
                return new ConfiguracionUsuario();
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoConfig.toFile()))) {
                return (ConfiguracionUsuario) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar configuración: " + e.getMessage());
            return new ConfiguracionUsuario(); // Retorna configuración por defecto en caso de error
        }
    }
    
    public boolean guardarPartida(String nombreUsuario, RegistroPartida partida) {
        try {
            String nombreArchivo = String.format("partida_%d_%d.dat", 
                partida.getNivel(), System.currentTimeMillis());
            Path archivoPartida = Paths.get(DIRECTORIO_BASE, nombreUsuario, DIRECTORIO_PARTIDAS, nombreArchivo);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoPartida.toFile()))) {
                oos.writeObject(partida);
                System.out.println("Partida guardada: " + nombreArchivo);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error al guardar partida: " + e.getMessage());
            return false;
        }
    }
    
    public List<RegistroPartida> cargarHistorialPartidas(String nombreUsuario) {
        List<RegistroPartida> historial = new ArrayList<>();
        
        try {
            Path carpetaPartidas = Paths.get(DIRECTORIO_BASE, nombreUsuario, DIRECTORIO_PARTIDAS);
            
            if (!Files.exists(carpetaPartidas)) {
                return historial; // Retorna lista vacía si no existe la carpeta
            }
            
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpetaPartidas, "*.dat")) {
                for (Path archivo : stream) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo.toFile()))) {
                        RegistroPartida partida = (RegistroPartida) ois.readObject();
                        historial.add(partida);
                    } catch (ClassNotFoundException e) {
                        System.err.println("Error al leer partida: " + archivo.getFileName());
                    }
                }
            }
            
            System.out.println("Cargadas " + historial.size() + " partidas para: " + nombreUsuario);
        } catch (IOException e) {
            System.err.println("Error al cargar historial de partidas: " + e.getMessage());
        }
        
        return historial;
    }
    
    public List<String> listarUsuarios() {
        List<String> usuarios = new ArrayList<>();
        
        try {
            Path directorioBase = Paths.get(DIRECTORIO_BASE);
            
            if (!Files.exists(directorioBase)) {
                return usuarios; // Retorna lista vacía si no existe el directorio
            }
            
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directorioBase)) {
                for (Path carpeta : stream) {
                    if (Files.isDirectory(carpeta)) {
                        Path archivoUsuario = carpeta.resolve(ARCHIVO_USUARIO);
                        if (Files.exists(archivoUsuario)) {
                            usuarios.add(carpeta.getFileName().toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    public boolean existeUsuario(String nombreUsuario) {
        Path archivoUsuario = Paths.get(DIRECTORIO_BASE, nombreUsuario, ARCHIVO_USUARIO);
        return Files.exists(archivoUsuario);
    }
    
    public boolean eliminarUsuario(String nombreUsuario) {
        try {
            Path carpetaUsuario = Paths.get(DIRECTORIO_BASE, nombreUsuario);
            
            if (!Files.exists(carpetaUsuario)) {
                return false;
            }
            
            // Eliminar recursivamente toda la carpeta del usuario
            eliminarDirectorioRecursivo(carpetaUsuario);
            System.out.println("Usuario eliminado: " + nombreUsuario);
            return true;
        } catch (IOException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    private void eliminarDirectorioRecursivo(Path directorio) throws IOException {
        if (Files.isDirectory(directorio)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directorio)) {
                for (Path archivo : stream) {
                    eliminarDirectorioRecursivo(archivo);
                }
            }
        }
        Files.delete(directorio);
    }
    
    public boolean guardarEstadisticasNivel(String nombreUsuario, int nivel, EstadisticasNivel estadisticas) {
        try {
            String nombreArchivo = String.format("estadisticas_nivel_%d.dat", nivel);
            Path archivoStats = Paths.get(DIRECTORIO_BASE, nombreUsuario, nombreArchivo);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoStats.toFile()))) {
                oos.writeObject(estadisticas);
                System.out.println("Estadísticas guardadas para nivel " + nivel + " usuario: " + nombreUsuario);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
            return false;
        }
    }
    
    public EstadisticasNivel cargarEstadisticasNivel(String nombreUsuario, int nivel) {
        try {
            String nombreArchivo = String.format("estadisticas_nivel_%d.dat", nivel);
            Path archivoStats = Paths.get(DIRECTORIO_BASE, nombreUsuario, nombreArchivo);
            
            if (!Files.exists(archivoStats)) {
                return new EstadisticasNivel(nivel); // Retorna estadísticas vacías si no existe
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoStats.toFile()))) {
                return (EstadisticasNivel) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar estadísticas: " + e.getMessage());
            return new EstadisticasNivel(nivel); // Retorna estadísticas vacías en caso de error
        }
    }
    
    public long getTamañoCarpetaUsuario(String nombreUsuario) {
        try {
            Path carpetaUsuario = Paths.get(DIRECTORIO_BASE, nombreUsuario);
            if (!Files.exists(carpetaUsuario)) {
                return 0;
            }
            
            return Files.walk(carpetaUsuario)
                    .filter(Files::isRegularFile)
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
        } catch (IOException e) {
            System.err.println("Error al calcular tamaño de carpeta: " + e.getMessage());
            return 0;
        }
    }
    public boolean crearBackup(String nombreUsuario) {
    Path carpetaUsuario = Paths.get(DIRECTORIO_BASE, nombreUsuario);
    if (!Files.exists(carpetaUsuario)) return false;

    Path carpetaBackup = Paths.get(DIRECTORIO_BASE, "backups", nombreUsuario + "_" + System.currentTimeMillis());

    try {
        Files.createDirectories(carpetaBackup.getParent());
        Files.walk(carpetaUsuario).forEach(origen -> {
            try {
                Path destino = carpetaBackup.resolve(carpetaUsuario.relativize(origen));
                if (Files.isDirectory(origen)) {
                    Files.createDirectories(destino);
                } else {
                    Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Backup creado para usuario: " + nombreUsuario);
        return true;
    } catch (IOException e) {
        System.err.println("Error creando backup: " + e.getMessage());
        return false;
    }
}

}