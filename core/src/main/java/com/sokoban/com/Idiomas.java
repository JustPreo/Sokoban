package com.sokoban.com;

import java.util.HashMap;
import java.util.Map;

public class Idiomas {
    private static Idiomas instancia;
    private String idiomaActual = "es"; // español por defecto
    private Map<String, Map<String, String>> textos;
    
    private Idiomas() {
        textos = new HashMap<>();
        cargarTextos();
    }
    
    public static Idiomas getInstance() {
        if (instancia == null) {
            instancia = new Idiomas();
        }
        return instancia;
    }
    
    private void cargarTextos() {
        // TEXTOS EN ESPAÑOL
        Map<String, String> es = new HashMap<>();
        
        // Menu principal
        es.put("menu.titulo", "Sokoban");
        es.put("menu.jugar", "Jugar");
        es.put("menu.perfil", "Perfil");
        es.put("menu.amigos", "Mis Amigos");
        es.put("menu.iniciar_sesion", "Iniciar Sesión");
        es.put("menu.cerrar_sesion", "Cerrar Sesión");
        es.put("menu.salir", "Salir");
        es.put("menu.bienvenido", "Bienvenido:");
        es.put("menu.sin_sesion", "Sin sesión activa");
        es.put("menu.configuracion", "Configuración");
        
        // Login
        es.put("login.titulo_login", "INICIAR SESIÓN");
        es.put("login.titulo_registro", "REGISTRARSE");
        es.put("login.usuario", "Usuario:");
        es.put("login.contrasena", "Contraseña:");
        es.put("login.nombre_completo", "Nombre completo:");
        es.put("login.registrarse", "¿No tienes cuenta? Regístrate");
        es.put("login.ya_cuenta", "¿Ya tienes cuenta? Inicia sesión");
        es.put("login.volver", "Volver");
        es.put("login.acceso_requerido", "Acceso Requerido");
        es.put("login.necesita_sesion", "Necesitas iniciar sesión\npara acceder a esta función");
        
        // Perfil
        es.put("perfil.titulo", "PERFIL DE USUARIO");
        es.put("perfil.estadisticas", "Estadísticas");
        es.put("perfil.avatares", "Avatares");
        es.put("perfil.ranking", "Ranking");
        es.put("perfil.comparar", "Comparar");
        es.put("perfil.configuracion", "Configuración");
        es.put("perfil.cerrar", "Cerrar");
        es.put("perfil.usuario", "Usuario:");
        es.put("perfil.nombre", "Nombre:");
        es.put("perfil.nivel_actual", "Nivel actual:");
        es.put("perfil.nivel_maximo", "Nivel máximo:");
        es.put("perfil.partidas_jugadas", "Partidas jugadas:");
        es.put("perfil.puntos_totales", "Puntos totales:");
        es.put("perfil.estadisticas_detalladas", "ESTADÍSTICAS DETALLADAS");
        es.put("perfil.progreso_niveles", "Progreso por Niveles:");
        es.put("perfil.estadisticas_generales", "Estadísticas Generales:");
        es.put("perfil.nivel", "Nivel");
        es.put("perfil.desbloqueado", "Desbloqueado");
        es.put("perfil.bloqueado", "Bloqueado");
        es.put("perfil.tiempo_total", "Tiempo total jugado:");
        es.put("perfil.minutos", "minutos");
        es.put("perfil.tasa_exito", "Tasa de éxito:");
        
        // Configuración
        es.put("config.titulo", "CONFIGURACIÓN");
        es.put("config.idioma_actual", "Idioma actual:");
        es.put("config.seleccionar", "Seleccionar idioma:");
        es.put("config.aplicar", "Aplicar");
        es.put("config.idioma_cambiado", "Idioma cambiado correctamente");
        es.put("config.proximamente", "Configuraciones del juego\n(Próximamente más opciones)");
        es.put("config.crear_backup", "Crear Copia de Seguridad");
        es.put("config.backup_creado", "¡Backup creado!");
        es.put("config.error_backup", "Error creando backup");
        
        // Settings/Configuración avanzada
        es.put("settings.titulo", "CONFIGURACIÓN DEL JUEGO");
        es.put("settings.audio", "AUDIO");
        es.put("settings.volumen_musica", "Volumen Música:");
        es.put("settings.volumen_efectos", "Volumen Efectos:");
        es.put("settings.musica_activada", "Música Activada");
        es.put("settings.efectos_activados", "Efectos Activados");
        es.put("settings.graficos", "GRÁFICOS");
        es.put("settings.pantalla_completa", "Pantalla Completa");
        es.put("settings.vsync", "Sincronización Vertical");
        es.put("settings.fps_counter", "Mostrar FPS");
        es.put("settings.juego", "JUEGO");
        es.put("settings.dificultad", "Dificultad:");
        es.put("settings.auto_guardar", "Auto-Guardar");
        es.put("settings.tutorial", "Tutorial");
        es.put("settings.restablecer", "Restablecer");
        es.put("settings.guardar", "Guardar");
        es.put("settings.configuracion_guardada", "Configuración guardada exitosamente");
        es.put("settings.error_guardar", "Error al guardar configuración");
        es.put("settings.confirmar_restablecer", "¿Restablecer configuración por defecto?");
        es.put("settings.restablecido", "Configuración restablecida");
        
        // Dificultades
        es.put("dificultad.facil", "Fácil");
        es.put("dificultad.medio", "Medio");
        es.put("dificultad.dificil", "Difícil");
        
        // General
        es.put("general.si", "Sí");
        es.put("general.no", "No");
        es.put("general.cancelar", "Cancelar");
        es.put("general.aceptar", "Aceptar");
        es.put("general.cerrar", "Cerrar");
        es.put("general.volver", "Volver");
        es.put("general.confirmar", "Confirmar");
        
        textos.put("es", es);
        
        // TEXTOS EN INGLÉS
        Map<String, String> en = new HashMap<>();
        
        // Menu principal
        en.put("menu.titulo", "Sokoban");
        en.put("menu.jugar", "Play");
        en.put("menu.perfil", "Profile");
        en.put("menu.amigos", "My Friends");
        en.put("menu.iniciar_sesion", "Log In");
        en.put("menu.cerrar_sesion", "Log Out");
        en.put("menu.salir", "Exit");
        en.put("menu.bienvenido", "Welcome:");
        en.put("menu.sin_sesion", "No active session");
        en.put("menu.configuracion", "Settings");
        
        // Login
        en.put("login.titulo_login", "LOG IN");
        en.put("login.titulo_registro", "REGISTER");
        en.put("login.usuario", "Username:");
        en.put("login.contrasena", "Password:");
        en.put("login.nombre_completo", "Full name:");
        en.put("login.registrarse", "Don't have an account? Register");
        en.put("login.ya_cuenta", "Already have an account? Log in");
        en.put("login.volver", "Back");
        en.put("login.acceso_requerido", "Access Required");
        en.put("login.necesita_sesion", "You need to log in\nto access this function");
        
        // Perfil
        en.put("perfil.titulo", "USER PROFILE");
        en.put("perfil.estadisticas", "Statistics");
        en.put("perfil.avatares", "Avatars");
        en.put("perfil.ranking", "Ranking");
        en.put("perfil.comparar", "Compare");
        en.put("perfil.configuracion", "Settings");
        en.put("perfil.cerrar", "Close");
        en.put("perfil.usuario", "Username:");
        en.put("perfil.nombre", "Name:");
        en.put("perfil.nivel_actual", "Current level:");
        en.put("perfil.nivel_maximo", "Max level:");
        en.put("perfil.partidas_jugadas", "Games played:");
        en.put("perfil.puntos_totales", "Total points:");
        en.put("perfil.estadisticas_detalladas", "DETAILED STATISTICS");
        en.put("perfil.progreso_niveles", "Level Progress:");
        en.put("perfil.estadisticas_generales", "General Statistics:");
        en.put("perfil.nivel", "Level");
        en.put("perfil.desbloqueado", "Unlocked");
        en.put("perfil.bloqueado", "Locked");
        en.put("perfil.tiempo_total", "Total time played:");
        en.put("perfil.minutos", "minutes");
        en.put("perfil.tasa_exito", "Success rate:");
        
        // Configuración
        en.put("config.titulo", "LANGUAGE SETTINGS");
        en.put("config.idioma_actual", "Current language:");
        en.put("config.seleccionar", "Select language:");
        en.put("config.aplicar", "Apply");
        en.put("config.idioma_cambiado", "Language changed successfully");
        en.put("config.proximamente", "Game settings\n(More options coming soon)");
        en.put("config.crear_backup", "Create Backup");
        en.put("config.backup_creado", "Backup created!");
        en.put("config.error_backup", "Error creating backup");
        
        // Settings
        en.put("settings.titulo", "GAME SETTINGS");
        en.put("settings.audio", "AUDIO");
        en.put("settings.volumen_musica", "Music Volume:");
        en.put("settings.volumen_efectos", "Effects Volume:");
        en.put("settings.musica_activada", "Music Enabled");
        en.put("settings.efectos_activados", "Sound Effects Enabled");
        en.put("settings.graficos", "GRAPHICS");
        en.put("settings.pantalla_completa", "Full Screen");
        en.put("settings.vsync", "Vertical Sync");
        en.put("settings.fps_counter", "Show FPS");
        en.put("settings.juego", "GAME");
        en.put("settings.dificultad", "Difficulty:");
        en.put("settings.auto_guardar", "Auto-Save");
        en.put("settings.tutorial", "Tutorial");
        en.put("settings.restablecer", "Reset");
        en.put("settings.guardar", "Save");
        en.put("settings.configuracion_guardada", "Settings saved successfully");
        en.put("settings.error_guardar", "Error saving settings");
        en.put("settings.confirmar_restablecer", "Reset to default settings?");
        en.put("settings.restablecido", "Settings reset");
        
        // Dificultades
        en.put("dificultad.facil", "Easy");
        en.put("dificultad.medio", "Medium");
        en.put("dificultad.dificil", "Hard");
        
        // General
        en.put("general.si", "Yes");
        en.put("general.no", "No");
        en.put("general.cancelar", "Cancel");
        en.put("general.aceptar", "Accept");
        en.put("general.cerrar", "Close");
        en.put("general.volver", "Back");
        en.put("general.confirmar", "Confirm");
        
        textos.put("en", en);
        
        // TEXTOS EN FRANCÉS
        Map<String, String> fr = new HashMap<>();
        
        // Menu principal
        fr.put("menu.titulo", "Sokoban");
        fr.put("menu.jugar", "Jouer");
        fr.put("menu.perfil", "Profil");
        fr.put("menu.amigos", "Mes Amis");
        fr.put("menu.iniciar_sesion", "Se Connecter");
        fr.put("menu.cerrar_sesion", "Se Déconnecter");
        fr.put("menu.salir", "Sortir");
        fr.put("menu.bienvenido", "Bienvenue:");
        fr.put("menu.sin_sesion", "Aucune session active");
        fr.put("menu.configuracion", "Configuration");
        
        // Login
        fr.put("login.titulo_login", "SE CONNECTER");
        fr.put("login.titulo_registro", "S'INSCRIRE");
        fr.put("login.usuario", "Nom d'utilisateur:");
        fr.put("login.contrasena", "Mot de passe:");
        fr.put("login.nombre_completo", "Nom complet:");
        fr.put("login.registrarse", "Pas de compte? S'inscrire");
        fr.put("login.ya_cuenta", "Déjà un compte? Se connecter");
        fr.put("login.volver", "Retour");
        fr.put("login.acceso_requerido", "Accès Requis");
        fr.put("login.necesita_sesion", "Vous devez vous connecter\npour accéder à cette fonction");
        
        // Perfil
        fr.put("perfil.titulo", "PROFIL UTILISATEUR");
        fr.put("perfil.estadisticas", "Statistiques");
        fr.put("perfil.avatares", "Avatars");
        fr.put("perfil.ranking", "Classement");
        fr.put("perfil.comparar", "Comparer");
        fr.put("perfil.configuracion", "Configuration");
        fr.put("perfil.cerrar", "Fermer");
        fr.put("perfil.usuario", "Utilisateur:");
        fr.put("perfil.nombre", "Nom:");
        fr.put("perfil.nivel_actual", "Niveau actuel:");
        fr.put("perfil.nivel_maximo", "Niveau max:");
        fr.put("perfil.partidas_jugadas", "Parties jouées:");
        fr.put("perfil.puntos_totales", "Points totaux:");
        fr.put("perfil.estadisticas_detalladas", "STATISTIQUES DÉTAILLÉES");
        fr.put("perfil.progreso_niveles", "Progression des Niveaux:");
        fr.put("perfil.estadisticas_generales", "Statistiques Générales:");
        fr.put("perfil.nivel", "Niveau");
        fr.put("perfil.desbloqueado", "Débloqué");
        fr.put("perfil.bloqueado", "Verrouillé");
        fr.put("perfil.tiempo_total", "Temps total joué:");
        fr.put("perfil.minutos", "minutes");
        fr.put("perfil.tasa_exito", "Taux de succès:");
        
        // Configuración
        fr.put("config.titulo", "PARAMÈTRES DE LANGUE");
        fr.put("config.idioma_actual", "Langue actuelle:");
        fr.put("config.seleccionar", "Sélectionner langue:");
        fr.put("config.aplicar", "Appliquer");
        fr.put("config.idioma_cambiado", "Langue changée avec succès");
        fr.put("config.proximamente", "Paramètres de jeu\n(Plus d'options bientôt)");
        fr.put("config.crear_backup", "Créer Sauvegarde");
        fr.put("config.backup_creado", "Sauvegarde créée!");
        fr.put("config.error_backup", "Erreur création sauvegarde");
        
        // Settings
        fr.put("settings.titulo", "PARAMÈTRES DU JEU");
        fr.put("settings.audio", "AUDIO");
        fr.put("settings.volumen_musica", "Volume Musique:");
        fr.put("settings.volumen_efectos", "Volume Effets:");
        fr.put("settings.musica_activada", "Musique Activée");
        fr.put("settings.efectos_activados", "Effets Sonores Activés");
        fr.put("settings.graficos", "GRAPHIQUES");
        fr.put("settings.pantalla_completa", "Plein Écran");
        fr.put("settings.vsync", "Synchronisation Verticale");
        fr.put("settings.fps_counter", "Afficher FPS");
        fr.put("settings.juego", "JEU");
        fr.put("settings.dificultad", "Difficulté:");
        fr.put("settings.auto_guardar", "Sauvegarde Auto");
        fr.put("settings.tutorial", "Tutoriel");
        fr.put("settings.restablecer", "Réinitialiser");
        fr.put("settings.guardar", "Sauvegarder");
        fr.put("settings.configuracion_guardada", "Configuration sauvée avec succès");
        fr.put("settings.error_guardar", "Erreur sauvegarde configuration");
        fr.put("settings.confirmar_restablecer", "Réinitialiser configuration par défaut?");
        fr.put("settings.restablecido", "Configuration réinitialisée");
        
        // Dificultades
        fr.put("dificultad.facil", "Facile");
        fr.put("dificultad.medio", "Moyen");
        fr.put("dificultad.dificil", "Difficile");
        
        // General
        fr.put("general.si", "Oui");
        fr.put("general.no", "Non");
        fr.put("general.cancelar", "Annuler");
        fr.put("general.aceptar", "Accepter");
        fr.put("general.cerrar", "Fermer");
        fr.put("general.volver", "Retour");
        fr.put("general.confirmar", "Confirmer");
        
        textos.put("fr", fr);
    }
    
    public String obtenerTexto(String clave) {
        Map<String, String> idiomaMap = textos.get(idiomaActual);
        if (idiomaMap != null && idiomaMap.containsKey(clave)) {
            return idiomaMap.get(clave);
        }
        // fallback a español si no encuentra la clave
        Map<String, String> espanol = textos.get("es");
        if (espanol != null && espanol.containsKey(clave)) {
            return espanol.get(clave);
        }
        return clave; // devolver la clave si no se encuentra nada
    }
    
    public void cambiarIdioma(String nuevoIdioma) {
        if (textos.containsKey(nuevoIdioma)) {
            idiomaActual = nuevoIdioma;
            System.out.println("Idioma cambiado a: " + nuevoIdioma);
        }
    }
    
    public String getIdiomaActual() {
        return idiomaActual;
    }
    
    public String[] getIdiomasDisponibles() {
        return new String[]{"es", "en", "fr"};
    }
    
    public String getNombreIdioma(String codigo) {
        switch (codigo) {
            case "es": return "Español";
            case "en": return "English";
            case "fr": return "Français";
            default: return codigo;
        }
    }
    
    // cargar idioma desde configuracion del usuario
    public void cargarIdiomaUsuario() {
        SistemaUsuarios sistema = SistemaUsuarios.getInstance();
        if (sistema.haySesionActiva()) {
            String idiomaGuardado = sistema.getUsuarioActual().getPreferencias().getIdioma();
            if (idiomaGuardado != null && textos.containsKey(idiomaGuardado)) {
                idiomaActual = idiomaGuardado;
            }
        }
    }
    
    // guardar idioma en configuracion del usuario
    public void guardarIdiomaUsuario(String idioma) {
        SistemaUsuarios sistema = SistemaUsuarios.getInstance();
        if (sistema.haySesionActiva()) {
            sistema.getUsuarioActual().getPreferencias().setIdioma(idioma);
            sistema.guardarProgreso();
        }
    }
}