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
        // textos en español
        Map<String, String> es = new HashMap<>();
        es.put("menu.titulo", "Sokoban");
        es.put("menu.jugar", "Jugar");
        es.put("menu.perfil", "Perfil");
        es.put("menu.amigos", "Mis Amigos");
        es.put("menu.iniciar_sesion", "Iniciar Sesión");
        es.put("menu.cerrar_sesion", "Cerrar Sesión");
        es.put("menu.salir", "Salir");
        es.put("menu.bienvenido", "Bienvenido:");
        es.put("menu.sin_sesion", "Sin sesión activa");
        
        es.put("login.titulo_login", "INICIAR SESIÓN");
        es.put("login.titulo_registro", "REGISTRARSE");
        es.put("login.usuario", "Usuario:");
        es.put("login.contrasena", "Contraseña:");
        es.put("login.nombre_completo", "Nombre completo:");
        es.put("login.registrarse", "¿No tienes cuenta? Regístrate");
        es.put("login.ya_cuenta", "¿Ya tienes cuenta? Inicia sesión");
        es.put("login.volver", "Volver");
        
        es.put("perfil.titulo", "PERFIL DE USUARIO");
        es.put("perfil.estadisticas", "Estadísticas");
        es.put("perfil.avatares", "Avatares");
        es.put("perfil.ranking", "Ranking");
        es.put("perfil.comparar", "Comparar");
        es.put("perfil.configuracion", "Configuración");
        es.put("perfil.cerrar", "Cerrar");
        
        es.put("config.titulo", "CONFIGURACIÓN DE IDIOMA");
        es.put("config.idioma_actual", "Idioma actual:");
        es.put("config.seleccionar", "Seleccionar idioma:");
        es.put("config.aplicar", "Aplicar");
        es.put("config.idioma_cambiado", "Idioma cambiado correctamente");
        
        es.put("general.si", "Sí");
        es.put("general.no", "No");
        es.put("general.cancelar", "Cancelar");
        es.put("general.aceptar", "Aceptar");
        es.put("general.cerrar", "Cerrar");
        es.put("general.volver", "Volver");
        
        textos.put("es", es);
        
        // textos en inglés, we gotta slay the proyect
        Map<String, String> en = new HashMap<>();
        en.put("menu.titulo", "Sokoban");
        en.put("menu.jugar", "Play");
        en.put("menu.perfil", "Profile");
        en.put("menu.amigos", "My Friends");
        en.put("menu.iniciar_sesion", "Log In");
        en.put("menu.cerrar_sesion", "Log Out");
        en.put("menu.salir", "Exit");
        en.put("menu.bienvenido", "Welcome:");
        en.put("menu.sin_sesion", "No active session");
        
        en.put("login.titulo_login", "LOG IN");
        en.put("login.titulo_registro", "REGISTER");
        en.put("login.usuario", "Username:");
        en.put("login.contrasena", "Password:");
        en.put("login.nombre_completo", "Full name:");
        en.put("login.registrarse", "Don't have an account? Register");
        en.put("login.ya_cuenta", "Already have an account? Log in");
        en.put("login.volver", "Back");
        
        en.put("perfil.titulo", "USER PROFILE");
        en.put("perfil.estadisticas", "Statistics");
        en.put("perfil.avatares", "Avatars");
        en.put("perfil.ranking", "Ranking");
        en.put("perfil.comparar", "Compare");
        en.put("perfil.configuracion", "Settings");
        en.put("perfil.cerrar", "Close");
        
        en.put("config.titulo", "LANGUAGE SETTINGS");
        en.put("config.idioma_actual", "Current language:");
        en.put("config.seleccionar", "Select language:");
        en.put("config.aplicar", "Apply");
        en.put("config.idioma_cambiado", "Language changed successfully");
        
        en.put("general.si", "Yes");
        en.put("general.no", "No");
        en.put("general.cancelar", "Cancel");
        en.put("general.aceptar", "Accept");
        en.put("general.cerrar", "Close");
        en.put("general.volver", "Back");
        
        textos.put("en", en);
        
        // textos en francés, tres idiomas para que mire que hubo esfuerzo
        Map<String, String> fr = new HashMap<>();
        fr.put("menu.titulo", "Sokoban");
        fr.put("menu.jugar", "Jouer");
        fr.put("menu.perfil", "Profil");
        fr.put("menu.amigos", "Mes Amis");
        fr.put("menu.iniciar_sesion", "Se Connecter");
        fr.put("menu.cerrar_sesion", "Se Déconnecter");
        fr.put("menu.salir", "Sortir");
        fr.put("menu.bienvenido", "Bienvenue:");
        fr.put("menu.sin_sesion", "Aucune session active");
        
        fr.put("login.titulo_login", "SE CONNECTER");
        fr.put("login.titulo_registro", "S'INSCRIRE");
        fr.put("login.usuario", "Nom d'utilisateur:");
        fr.put("login.contrasena", "Mot de passe:");
        fr.put("login.nombre_completo", "Nom complet:");
        fr.put("login.registrarse", "Pas de compte? S'inscrire");
        fr.put("login.ya_cuenta", "Déjà un compte? Se connecter");
        fr.put("login.volver", "Retour");
        
        fr.put("perfil.titulo", "PROFIL UTILISATEUR");
        fr.put("perfil.estadisticas", "Statistiques");
        fr.put("perfil.avatares", "Avatars");
        fr.put("perfil.ranking", "Classement");
        fr.put("perfil.comparar", "Comparer");
        fr.put("perfil.configuracion", "Configuration");
        fr.put("perfil.cerrar", "Fermer");
        
        fr.put("config.titulo", "PARAMÈTRES DE LANGUE");
        fr.put("config.idioma_actual", "Langue actuelle:");
        fr.put("config.seleccionar", "Sélectionner langue:");
        fr.put("config.aplicar", "Appliquer");
        fr.put("config.idioma_cambiado", "Langue changée avec succès");
        
        fr.put("general.si", "Oui");
        fr.put("general.no", "Non");
        fr.put("general.cancelar", "Annuler");
        fr.put("general.aceptar", "Accepter");
        fr.put("general.cerrar", "Fermer");
        fr.put("general.volver", "Retour");
        
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
/*Lol a ver si funkaa */
