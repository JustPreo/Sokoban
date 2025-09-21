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
        // TEXTOS EN ESPANOL
        Map<String, String> es = new HashMap<>();

        // Menu principal
        es.put("menu.titulo", "Sokoban");
        es.put("menu.jugar", "Jugar");
        es.put("menu.perfil", "Perfil");
        es.put("menu.amigos", "Mis Amigos");
        es.put("menu.iniciar_sesion", "Iniciar Sesion");
        es.put("menu.cerrar_sesion", "Cerrar Sesion");
        es.put("menu.salir", "Salir");
        es.put("menu.bienvenido", "Bienvenido:");
        es.put("menu.sin_sesion", "Sin sesion activa");
        es.put("menu.configuracion", "Configuracion");

        // Login
        es.put("login.titulo_login", "INICIAR SESION");
        es.put("login.titulo_registro", "REGISTRARSE");
        es.put("login.usuario", "Usuario:");
        es.put("login.contrasena", "Contrasena:");
        es.put("login.nombre_completo", "Nombre completo:");
        es.put("login.registrarse", "No tienes cuenta? Registrate");
        es.put("login.ya_cuenta", "Ya tienes cuenta? Inicia sesion");
        es.put("login.volver", "Volver");
        es.put("login.acceso_requerido", "Acceso Requerido");
        es.put("login.necesita_sesion", "Necesitas iniciar sesion\npara acceder a esta funcion");

        // Perfil
        es.put("perfil.titulo", "PERFIL DE USUARIO");
        es.put("perfil.estadisticas", "Estadisticas");
        es.put("perfil.avatares", "Avatares");
        es.put("perfil.ranking", "Ranking");
        es.put("perfil.comparar", "Comparar");
        es.put("perfil.configuracion", "Configuracion");
        es.put("perfil.cerrar", "Cerrar");
        es.put("perfil.usuario", "Usuario:");
        es.put("perfil.nombre", "Nombre:");
        es.put("perfil.nivel_actual", "Nivel actual:");
        es.put("perfil.nivel_maximo", "Nivel maximo:");
        es.put("perfil.partidas_jugadas", "Partidas jugadas:");
        es.put("perfil.puntos_totales", "Puntos totales:");
        es.put("perfil.estadisticas_detalladas", "ESTADISTICAS DETALLADAS");
        es.put("perfil.progreso_niveles", "Progreso por Niveles:");
        es.put("perfil.estadisticas_generales", "Estadisticas Generales:");
        es.put("perfil.nivel", "Nivel");
        es.put("perfil.desbloqueado", "Desbloqueado");
        es.put("perfil.bloqueado", "Bloqueado");
        es.put("perfil.tiempo_total", "Tiempo total jugado:");
        es.put("perfil.minutos", "minutos");
        es.put("perfil.tasa_exito", "Tasa de exito:");

        // Configuracion
        es.put("config.titulo", "CONFIGURACION");
        es.put("config.idioma_actual", "Idioma actual:");
        es.put("config.seleccionar", "Seleccionar idioma:");
        es.put("config.aplicar", "Aplicar");
        es.put("config.idioma_cambiado", "Idioma cambiado correctamente");
        es.put("config.proximamente", "Configuraciones del juego\n(Proximamente mas opciones)");
        es.put("config.crear_backup", "Crear Copia de Seguridad");
        es.put("config.backup_creado", "¡Backup creado!");
        es.put("config.error_backup", "Error creando backup");

        // Settings/Configuracion avanzada
        es.put("settings.titulo", "CONFIGURACION DEL JUEGO");
        es.put("settings.audio", "AUDIO");
        es.put("settings.volumen_musica", "Volumen Musica:");
        es.put("settings.volumen_efectos", "Volumen Efectos:");
        es.put("settings.musica_activada", "Musica Activada");
        es.put("settings.efectos_activados", "Efectos Activados");
        es.put("settings.graficos", "GRAFICOS");
        es.put("settings.pantalla_completa", "Pantalla Completa");
        es.put("settings.vsync", "Sincronizacion Vertical");
        es.put("settings.fps_counter", "Mostrar FPS");
        es.put("settings.juego", "JUEGO");
        es.put("settings.dificultad", "Dificultad:");
        es.put("settings.auto_guardar", "Auto-Guardar");
        es.put("settings.tutorial", "Tutorial");
        es.put("settings.restablecer", "Restablecer");
        es.put("settings.guardar", "Guardar");
        es.put("settings.configuracion_guardada", "Configuracion guardada exitosamente");
        es.put("settings.error_guardar", "Error al guardar configuracion");
        es.put("settings.confirmar_restablecer", "Restablecer configuracion por defecto?");
        es.put("settings.restablecido", "Configuracion restablecida");

        // Dificultades
        es.put("dificultad.facil", "Facil");
        es.put("dificultad.medio", "Medio");
        es.put("dificultad.dificil", "Dificil");

        // General
        es.put("general.si", "Si");
        es.put("general.no", "No");
        es.put("general.cancelar", "Cancelar");
        es.put("general.aceptar", "Aceptar");
        es.put("general.cerrar", "Cerrar");
        es.put("general.volver", "Volver");
        es.put("general.confirmar", "Confirmar");

        textos.put("es", es);

        // TEXTOS EN INGLES
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
        en.put("menu.configuracion", "Settings");
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
        en.put("config.titulo", "LANGUAGE SETTINGS");
        en.put("config.idioma_actual", "Current language:");
        en.put("config.seleccionar", "Select language:");
        en.put("config.aplicar", "Apply");
        en.put("config.idioma_cambiado", "Language changed successfully");
        en.put("config.proximamente", "Game settings\n(More options coming soon)");
        en.put("config.crear_backup", "Create Backup");
        en.put("config.backup_creado", "Backup created!");
        en.put("config.error_backup", "Error creating backup");
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
        en.put("dificultad.facil", "Easy");
        en.put("dificultad.medio", "Medium");
        en.put("dificultad.dificil", "Hard");
        en.put("general.si", "Yes");
        en.put("general.no", "No");
        en.put("general.cancelar", "Cancel");
        en.put("general.aceptar", "Accept");
        en.put("general.cerrar", "Close");
        en.put("general.volver", "Back");
        en.put("general.confirmar", "Confirm");

        textos.put("en", en);

        // TEXTOS EN FRANCES (SIN ACENTOS)
        Map<String, String> fr = new HashMap<>();
        fr.put("menu.titulo", "Sokoban");
        fr.put("menu.jugar", "Jouer");
        fr.put("menu.perfil", "Profil");
        fr.put("menu.amigos", "Mes Amis");
        fr.put("menu.iniciar_sesion", "Se Connecter");
        fr.put("menu.cerrar_sesion", "Se Deconnecter");
        fr.put("menu.salir", "Sortir");
        fr.put("menu.bienvenido", "Bienvenue:");
        fr.put("menu.sin_sesion", "Aucune session active");
        fr.put("menu.configuracion", "Configuration");
        fr.put("login.titulo_login", "SE CONNECTER");
        fr.put("login.titulo_registro", "S'INSCRIRE");
        fr.put("login.usuario", "Nom d'utilisateur:");
        fr.put("login.contrasena", "Mot de passe:");
        fr.put("login.nombre_completo", "Nom complet:");
        fr.put("login.registrarse", "Pas de compte? S'inscrire");
        fr.put("login.ya_cuenta", "Deja un compte? Se connecter");
        fr.put("login.volver", "Retour");
        fr.put("login.acceso_requerido", "Acces Requis");
        fr.put("login.necesita_sesion", "Vous devez vous connecter\npour acceder a cette fonction");
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
        fr.put("perfil.partidas_jugadas", "Parties jouees:");
        fr.put("perfil.puntos_totales", "Points totaux:");
        fr.put("perfil.estadisticas_detalladas", "STATISTIQUES DETAILLEES");
        fr.put("perfil.progreso_niveles", "Progression des Niveaux:");
        fr.put("perfil.estadisticas_generales", "Statistiques Generales:");
        fr.put("perfil.nivel", "Niveau");
        fr.put("perfil.desbloqueado", "Debloque");
        fr.put("perfil.bloqueado", "Verrouille");
        fr.put("perfil.tiempo_total", "Temps total joue:");
        fr.put("perfil.minutos", "minutes");
        fr.put("perfil.tasa_exito", "Taux de succes:");
        fr.put("config.titulo", "PARAMETRES DE LANGUE");
        fr.put("config.idioma_actual", "Langue actuelle:");
        fr.put("config.seleccionar", "Selectionner langue:");
        fr.put("config.aplicar", "Appliquer");
        fr.put("config.idioma_cambiado", "Langue changee avec succes");
        fr.put("config.proximamente", "Parametres de jeu\n(Plus d'options bientot)");
        fr.put("config.crear_backup", "Creer Sauvegarde");
        fr.put("config.backup_creado", "Sauvegarde creee!");
        fr.put("config.error_backup", "Erreur creation sauvegarde");
        fr.put("settings.titulo", "PARAMETRES DU JEU");
        fr.put("settings.audio", "AUDIO");
        fr.put("settings.volumen_musica", "Volume Musique:");
        fr.put("settings.volumen_efectos", "Volume Effets:");
        fr.put("settings.musica_activada", "Musique Activée");
        fr.put("settings.efectos_activados", "Effets Sonores Actives");
        fr.put("settings.graficos", "GRAPHIQUES");
        fr.put("settings.pantalla_completa", "Plein Ecran");
        fr.put("settings.vsync", "Synchronisation Verticale");
        fr.put("settings.fps_counter", "Afficher FPS");
        fr.put("settings.juego", "JEU");
        fr.put("settings.dificultad", "Difficulte:");
        fr.put("settings.auto_guardar", "Sauvegarde Auto");
        fr.put("settings.tutorial", "Tutoriel");
        fr.put("settings.restablecer", "Reinitialiser");
        fr.put("settings.guardar", "Sauvegarder");
        fr.put("settings.configuracion_guardada", "Configuration sauvee avec succes");
        fr.put("settings.error_guardar", "Erreur sauvegarde configuration");
        fr.put("settings.confirmar_restablecer", "Reinitialiser configuration par defaut?");
        fr.put("settings.restablecido", "Configuration reinitialisee");
        fr.put("dificultad.facil", "Facile");
        fr.put("dificultad.medio", "Moyen");
        fr.put("dificultad.dificil", "Difficile");
        fr.put("general.si", "Oui");
        fr.put("general.no", "Non");
        fr.put("general.cancelar", "Annuler");
        fr.put("general.aceptar", "Accepter");
        fr.put("general.cerrar", "Fermer");
        fr.put("general.volver", "Retour");
        fr.put("general.confirmar", "Confirmer");

        textos.put("fr", fr);

        // PAUSA
        es.put("PAUSA", "PAUSA");
        en.put("PAUSA", "PAUSE");
        fr.put("PAUSA", "PAUSE");

        // Botones menu pausa
        es.put("menu.pausa.volver", "Volver");
        en.put("menu.pausa.volver", "Back");
        fr.put("menu.pausa.volver", "Retour");

        es.put("menu.pausa.reiniciar", "Reiniciar");
        en.put("menu.pausa.reiniciar", "Restart");
        fr.put("menu.pausa.reiniciar", "Redemarrer");

        // FIN DE NIVEL
        es.put("NIVEL_COMPLETADO", "NIVEL COMPLETADO!");
        en.put("NIVEL_COMPLETADO", "LEVEL COMPLETED!");
        fr.put("NIVEL_COMPLETADO", "NIVEAU TERMINE !");

        // Botones fin de nivel
        es.put("menu.fin.siguiente", "Siguiente");
        en.put("menu.fin.siguiente", "Next");
        fr.put("menu.fin.siguiente", "Niveau");

        es.put("menu.fin.reiniciar", "Reiniciar");
        en.put("menu.fin.reiniciar", "Restart");
        fr.put("menu.fin.reiniciar", "Redemarrer");

        es.put("menu.fin.menu", "Menu");
        en.put("menu.fin.menu", "Menu");
        fr.put("menu.fin.menu", "Menu");

        // Estadisticas
        es.put("fin.tiempo", "Tiempo");
        en.put("fin.tiempo", "Time");
        fr.put("fin.tiempo", "Temps");

        es.put("fin.movimientos", "Movimientos");
        en.put("fin.movimientos", "Moves");
        fr.put("fin.movimientos", "Mouvements");

        es.put("fin.puntuacion", "Puntuacion");
        en.put("fin.puntuacion", "Score");
        fr.put("fin.puntuacion", "Score");

        // Botones generales / menu
        es.put("boton.iniciar", "Iniciar");
        en.put("boton.iniciar", "Start");
        fr.put("boton.iniciar", "Demarrer");

        es.put("boton.salir", "Salir");
        en.put("boton.salir", "Exit");
        fr.put("boton.salir", "Quitter");

        es.put("boton.continuar", "Continuar");
        en.put("boton.continuar", "Continue");
        fr.put("boton.continuar", "Continuer");

        // Confirmaciones / alertas
        es.put("alerta.salir", "Seguro que quieres salir?");
        en.put("alerta.salir", "Are you sure you want to exit?");
        fr.put("alerta.salir", "Voulez-vous vraiment quitter ?");

        es.put("alerta.reiniciar", "Reiniciar el nivel?");
        en.put("alerta.reiniciar", "Restart the level?");
        fr.put("alerta.reiniciar", "Redemarrer le niveau ?");

        // ESPAÑOL
        es.put("amigos.titulo", "GESTIÓN DE AMIGOS");
        es.put("amigos.buscar.label", "Buscar usuario:");
        es.put("amigos.buscar.placeholder", "Nombre del usuario...");
        es.put("amigos.mis_amigos", "MIS AMIGOS");
        es.put("amigos.sin_sesion", "No hay sesión activa");
        es.put("amigos.sin_amigos", "No tienes amigos agregados aún");
        es.put("amigos.usuario_offline", "Usuario offline");
        es.put("amigos.ver", "Ver");
        es.put("amigos.quitar", "Quitar");
        es.put("amigos.msj.escribir_nombre", "Escribe un nombre de usuario");
        es.put("amigos.msj.usuario_invalido", "Nombre de usuario inválido");
        es.put("amigos.msj.usuario_no_encontrado", "Usuario no encontrado: %s");
        es.put("amigos.msj.ya_amigo", "Ya es tu amigo: %s");
        es.put("amigos.msj.agregarte_ti_mismo", "No puedes agregarte a ti mismo");
        es.put("amigos.agregar.titulo", "AGREGAR AMIGO");
        es.put("amigos.agregar.mensaje", "¿Quieres agregar a %s como tu amigo?");
        es.put("general.si", "Sí");
        es.put("general.cancelar", "Cancelar");
        es.put("amigos.msj.agregado", "Amigo agregado: %s");
        es.put("amigos.msj.error_agregar", "Error al agregar amigo");
        es.put("amigos.quitar.titulo", "QUITAR AMIGO");
        es.put("amigos.quitar.mensaje", "Seguro que quieres quitar a %s de tus amigos?");
        es.put("amigos.quitar.btn", "Quitar");
        es.put("amigos.msj.quitado", "Amigo quitado: %s");
        es.put("amigos.msj.error_quitar", "Error al quitar amigo");
        es.put("amigos.info", "Info de %s (próximamente más detalles)");

// INGLÉS
        en.put("amigos.titulo", "FRIENDS MANAGEMENT");
        en.put("amigos.buscar.label", "Search user:");
        en.put("amigos.buscar.placeholder", "User name...");
        en.put("amigos.mis_amigos", "MY FRIENDS");
        en.put("amigos.sin_sesion", "No active session");
        en.put("amigos.sin_amigos", "You have no friends added yet");
        en.put("amigos.usuario_offline", "User offline");
        en.put("amigos.ver", "View");
        en.put("amigos.quitar", "Remove");
        en.put("amigos.msj.escribir_nombre", "Enter a username");
        en.put("amigos.msj.usuario_invalido", "Invalid username");
        en.put("amigos.msj.usuario_no_encontrado", "User not found: %s");
        en.put("amigos.msj.ya_amigo", "Already your friend: %s");
        en.put("amigos.msj.agregarte_ti_mismo", "You cannot add yourself");
        en.put("amigos.agregar.titulo", "ADD FRIEND");
        en.put("amigos.agregar.mensaje", "Do you want to add %s as your friend?");
        en.put("general.si", "Yes");
        en.put("general.cancelar", "Cancel");
        en.put("amigos.msj.agregado", "Friend added: %s");
        en.put("amigos.msj.error_agregar", "Error adding friend");
        en.put("amigos.quitar.titulo", "REMOVE FRIEND");
        en.put("amigos.quitar.mensaje", "Are you sure you want to remove %s from your friends?");
        en.put("amigos.quitar.btn", "Remove");
        en.put("amigos.msj.quitado", "Friend removed: %s");
        en.put("amigos.msj.error_quitar", "Error removing friend");
        en.put("amigos.info", "Info of %s (more details coming soon)");

// FRANCÉS
        fr.put("amigos.titulo", "GESTION DES AMIS");
        fr.put("amigos.buscar.label", "Chercher utilisateur:");
        fr.put("amigos.buscar.placeholder", "Nom d'utilisateur...");
        fr.put("amigos.mis_amigos", "MES AMIS");
        fr.put("amigos.sin_sesion", "Aucune session active");
        fr.put("amigos.sin_amigos", "Vous n'avez pas encore d'amis");
        fr.put("amigos.usuario_offline", "Utilisateur hors ligne");
        fr.put("amigos.ver", "Voir");
        fr.put("amigos.quitar", "Retirer");
        fr.put("amigos.msj.escribir_nombre", "Entrez un nom d'utilisateur");
        fr.put("amigos.msj.usuario_invalido", "Nom d'utilisateur invalide");
        fr.put("amigos.msj.usuario_no_encontrado", "Utilisateur introuvable : %s");
        fr.put("amigos.msj.ya_amigo", "Déjà votre ami : %s");
        fr.put("amigos.msj.agregarte_ti_mismo", "Vous ne pouvez pas vous ajouter vous-même");
        fr.put("amigos.agregar.titulo", "AJOUTER UN AMI");
        fr.put("amigos.agregar.mensaje", "Voulez-vous ajouter %s comme votre ami ?");
        fr.put("general.si", "Oui");
        fr.put("general.cancelar", "Annuler");
        fr.put("amigos.msj.agregado", "Ami ajouté : %s");
        fr.put("amigos.msj.error_agregar", "Erreur lors de l'ajout de l'ami");
        fr.put("amigos.quitar.titulo", "SUPPRIMER UN AMI");
        fr.put("amigos.quitar.mensaje", "Êtes-vous sûr de vouloir supprimer %s de vos amis ?");
        fr.put("amigos.quitar.btn", "Supprimer");
        fr.put("amigos.msj.quitado", "Ami supprimé : %s");
        fr.put("amigos.msj.error_quitar", "Erreur lors de la suppression de l'ami");
        fr.put("amigos.info", "Infos de %s (plus de détails bientôt)");

        // Español
        es.put("comparacion.boton_comparar", "Comparar");
        es.put("comparacion.titulo", "Comparación de Jugadores");
        es.put("comparacion.comparar_con", "Comparar con:");
        es.put("comparacion.volver", "Volver");
        es.put("comparacion.error_sesion", "Debes iniciar sesión para comparar jugadores.");
        es.put("comparacion.sin_amigos", "No tienes amigos para comparar");
        es.put("comparacion.msj.agregar_amigos", "Agrega amigos primero");
        es.put("comparacion.msj.error_cargar_usuario", "Error al cargar el usuario: %s");
        es.put("comparacion.msj.comparando_con", "Comparando con %s");
        es.put("comparacion.seleccionar_amigo", "Selecciona un amigo para comparar");
        es.put("comparacion.tu", "Tu: %s");
        es.put("comparacion.rival", "Rival: %s");
        es.put("comparacion.niveles_completados", "NIVELES COMPLETADOS");
        es.put("comparacion.puntuacion_total", "PUNTUACION TOTAL");
        es.put("comparacion.tiempo_promedio", "TIEMPO PROMEDIO POR NIVEL");
        es.put("comparacion.partidas_completadas", "PARTIDAS COMPLETADAS");

// Inglés
        en.put("comparacion.boton_comparar", "Compare");
        en.put("comparacion.titulo", "Player Comparison");
        en.put("comparacion.comparar_con", "Compare with:");
        en.put("comparacion.volver", "Back");
        en.put("comparacion.error_sesion", "You must be logged in to compare players.");
        en.put("comparacion.sin_amigos", "No friends to compare");
        en.put("comparacion.msj.agregar_amigos", "Add friends first");
        en.put("comparacion.msj.error_cargar_usuario", "Error loading user: %s");
        en.put("comparacion.msj.comparando_con", "Comparing with %s");
        en.put("comparacion.seleccionar_amigo", "Select a friend to compare");
        en.put("comparacion.tu", "You: %s");
        en.put("comparacion.rival", "Rival: %s");
        en.put("comparacion.niveles_completados", "LEVELS COMPLETED");
        en.put("comparacion.puntuacion_total", "TOTAL SCORE");
        en.put("comparacion.tiempo_promedio", "AVERAGE TIME PER LEVEL");
        en.put("comparacion.partidas_completadas", "COMPLETED GAMES");

// Francés
        fr.put("comparacion.boton_comparar", "Comparer");
        fr.put("comparacion.titulo", "Comparaison des Joueurs");
        fr.put("comparacion.comparar_con", "Comparer avec :");
        fr.put("comparacion.volver", "Retour");
        fr.put("comparacion.error_sesion", "Vous devez vous connecter pour comparer les joueurs.");
        fr.put("comparacion.sin_amigos", "Pas d'amis à comparer");
        fr.put("comparacion.msj.agregar_amigos", "Ajoutez d'abord des amis");
        fr.put("comparacion.msj.error_cargar_usuario", "Erreur lors du chargement de l'utilisateur : %s");
        fr.put("comparacion.msj.comparando_con", "Comparaison avec %s");
        fr.put("comparacion.seleccionar_amigo", "Sélectionnez un ami à comparer");
        fr.put("comparacion.tu", "Vous : %s");
        fr.put("comparacion.rival", "Rival : %s");
        fr.put("comparacion.niveles_completados", "NIVEAUX TERMINÉS");
        fr.put("comparacion.puntuacion_total", "SCORE TOTAL");
        fr.put("comparacion.tiempo_promedio", "TEMPS MOYEN PAR NIVEAU");
        fr.put("comparacion.partidas_completadas", "PARTIES TERMINÉES");

        es.put("login.titulo_login", "INICIAR SESION");
        es.put("login.titulo_registro", "REGISTRARSE");
        es.put("login.usuario", "Usuario:");
        es.put("login.contrasena", "Contrasena:");
        es.put("login.nombre_completo", "Nombre completo:");
        es.put("login.registrarse", "¿No tienes cuenta? Regístrate");
        es.put("login.ya_cuenta", "¿Ya tienes cuenta? Inicia sesión");
        es.put("login.volver", "Volver");
        es.put("login.acceso_requerido", "Acceso Requerido");
        es.put("login.necesita_sesion", "Necesitas iniciar sesión para acceder a esta función");
        textos.put("es", es);
        en.put("login.titulo_login", "LOG IN");
        en.put("login.titulo_registro", "REGISTER");
        en.put("login.usuario", "Username:");
        en.put("login.contrasena", "Password:");
        en.put("login.nombre_completo", "Full name:");
        en.put("login.registrarse", "Don't have an account? Register");
        en.put("login.ya_cuenta", "Already have an account? Log in");
        en.put("login.volver", "Back");
        en.put("login.acceso_requerido", "Access Required");
        en.put("login.necesita_sesion", "You need to log in to access this function");
        fr.put("login.titulo_login", "SE CONNECTER");
        fr.put("login.titulo_registro", "S'INSCRIRE");
        fr.put("login.usuario", "Nom d'utilisateur:");
        fr.put("login.contrasena", "Mot de passe:");
        fr.put("login.nombre_completo", "Nom complet:");
        fr.put("login.registrarse", "Pas de compte? S'inscrire");
        fr.put("login.ya_cuenta", "Déjà un compte? Se connecter");
        fr.put("login.volver", "Retour");
        fr.put("login.acceso_requerido", "Accès Requis");
        fr.put("login.necesita_sesion", "Vous devez vous connecter pour accéder à cette fonction");
        es.put("amigos.buscar", "Buscar");
        es.put("amigos.actualizar", "Actualizar");
        es.put("amigos.volver", "Volver");

// INGLÉS
        en.put("amigos.buscar", "Search");
        en.put("amigos.actualizar", "Refresh");
        en.put("amigos.volver", "Back");

// FRANCÉS
        fr.put("amigos.buscar", "Chercher");
        fr.put("amigos.actualizar", "Actualiser");
        fr.put("amigos.volver", "Retour");
        es.put("avatares.titulo", "SELECCIONAR AVATAR");
        es.put("avatares.preview", "VISTA PREVIA");
        es.put("avatares.disponibles", "AVATARES DISPONIBLES");
        es.put("avatares.usuario", "Usuario:");
        es.put("avatares.actual", "Avatar:");
        es.put("menu.guardar", "GUARDAR");
        es.put("menu.volver", "VOLVER");
        en.put("avatares.titulo", "SELECT AVATAR");
        en.put("avatares.preview", "PREVIEW");
        en.put("avatares.disponibles", "AVAILABLE AVATARS");
        en.put("avatares.usuario", "User:");
        en.put("avatares.actual", "Avatar:");
        en.put("menu.guardar", "SAVE");
        en.put("menu.volver", "BACK");
        fr.put("avatares.titulo", "SÉLECTIONNER UN AVATAR");
        fr.put("avatares.preview", "APERÇU");
        fr.put("avatares.disponibles", "AVATARS DISPONIBLES");
        fr.put("avatares.usuario", "Utilisateur:");
        fr.put("avatares.actual", "Avatar:");
        fr.put("menu.guardar", "ENREGISTRER");
        fr.put("menu.volver", "RETOUR");

        es.put("ranking.titulo", "Ranking");
        es.put("ranking.filtro_tipo", "Filtrar por:");
        es.put("ranking.tipo_puntuacion", "Puntuacion Total");
        es.put("ranking.tipo_nivel", "Nivel Maximo");
        es.put("ranking.tipo_partidas", "Partidas Completadas");
        es.put("ranking.tipo_tiempo", "Tiempo Jugado");
        es.put("ranking.filtro_periodo", "Periodo:");
        es.put("ranking.periodo_historico", "Historico");
        es.put("ranking.periodo_mes", "Este Mes");
        es.put("ranking.periodo_semana", "Esta Semana");
        es.put("ranking.header_pos", "Pos");
        es.put("ranking.header_jugador", "Jugador");
        es.put("ranking.header_nivel", "Nivel");
        es.put("ranking.header_puntos", "Puntos");
        es.put("ranking.header_completadas", "Completadas");
        es.put("ranking.header_tiempo", "Tiempo");
        es.put("ranking.puntos_abreviatura", "pts");
        es.put("ranking.nivel", "Nivel");
        es.put("ranking.partidas", "partidas");
        es.put("ranking.sin_sesion", "Inicia Sesion para ver tu posicion");
        es.put("ranking.posicion_actual", "Tu posición actual: #%d de %d jugadores");
        es.put("ranking.no_encontrado", "No se encontro tu posicion en el ranking");
        es.put("ranking.actualizado", "Ranking actualizado");
        es.put("ranking.no_sesion", "Debes iniciar sesion para ver tu perfil");

// Inglés
        en.put("ranking.titulo", "Ranking");
        en.put("ranking.filtro_tipo", "Filter by:");
        en.put("ranking.tipo_puntuacion", "Total Score");
        en.put("ranking.tipo_nivel", "Max Level");
        en.put("ranking.tipo_partidas", "Completed Games");
        en.put("ranking.tipo_tiempo", "Time Played");
        en.put("ranking.filtro_periodo", "Period:");
        en.put("ranking.periodo_historico", "Historic");
        en.put("ranking.periodo_mes", "This Month");
        en.put("ranking.periodo_semana", "This Week");
        en.put("ranking.header_pos", "Pos");
        en.put("ranking.header_jugador", "Player");
        en.put("ranking.header_nivel", "Level");
        en.put("ranking.header_puntos", "Points");
        en.put("ranking.header_completadas", "Completed");
        en.put("ranking.header_tiempo", "Time");
        en.put("ranking.puntos_abreviatura", "pts");
        en.put("ranking.nivel", "Level");
        en.put("ranking.partidas", "games");
        en.put("ranking.sin_sesion", "Log in to see your position");
        en.put("ranking.posicion_actual", "Your current position: #%d of %d players");
        en.put("ranking.no_encontrado", "Your position was not found");
        en.put("ranking.actualizado", "Ranking updated");
        en.put("ranking.no_sesion", "You must log in to view your profile");

// Francés
        fr.put("ranking.titulo", "Classement");
        fr.put("ranking.filtro_tipo", "Filtrer par :");
        fr.put("ranking.tipo_puntuacion", "Score Total");
        fr.put("ranking.tipo_nivel", "Niveau Max");
        fr.put("ranking.tipo_partidas", "Parties terminées");
        fr.put("ranking.tipo_tiempo", "Temps Joué");
        fr.put("ranking.filtro_periodo", "Période :");
        fr.put("ranking.periodo_historico", "Historique");
        fr.put("ranking.periodo_mes", "Ce Mois");
        fr.put("ranking.periodo_semana", "Cette Semaine");
        fr.put("ranking.header_pos", "Pos");
        fr.put("ranking.header_jugador", "Joueur");
        fr.put("ranking.header_nivel", "Niveau");
        fr.put("ranking.header_puntos", "Points");
        fr.put("ranking.header_completadas", "Terminées");
        fr.put("ranking.header_tiempo", "Temps");
        fr.put("ranking.puntos_abreviatura", "pts");
        fr.put("ranking.nivel", "Niveau");
        fr.put("ranking.partidas", "parties");
        fr.put("ranking.sin_sesion", "Connectez-vous pour voir votre position");
        fr.put("ranking.posicion_actual", "Votre position actuelle : #%d sur %d joueurs");
        fr.put("ranking.no_encontrado", "Votre position n'a pas été trouvée");
        fr.put("ranking.actualizado", "Classement mis à jour");
        fr.put("ranking.no_sesion", "Vous devez vous connecter pour voir votre profil");

        // Español
        es.put("ranking.boton_actualizar", "Actualizar");
        es.put("ranking.boton_perfil", "Mi Perfil");
        es.put("ranking.boton_volver", "Volver");

// Inglés
        en.put("ranking.boton_actualizar", "Update");
        en.put("ranking.boton_perfil", "My Profile");
        en.put("ranking.boton_volver", "Back");

// Francés
        fr.put("ranking.boton_actualizar", "Mettre à jour");
        fr.put("ranking.boton_perfil", "Mon Profil");
        fr.put("ranking.boton_volver", "Retour");

        // Español
        es.put("hub.titulo", "SELECCIONAR NIVEL");
        es.put("hub.instruccion1", "Usa WASD o flechas para moverte");
        es.put("hub.instruccion2", "Camina sobre un nivel para seleccionarlo");
        es.put("hub.instruccion3", "ENTER/ESPACIO para entrar al nivel");
        es.put("hub.instruccion4", "ESC para volver al menú principal");
        es.put("hub.nivel", "Nivel");
        es.put("hub.dificultad", "Dificultad");
        es.put("hub.estado", "Estado");
        es.put("hub.accion", "ENTER/ESPACIO: Jugar");

// Inglés
        en.put("hub.titulo", "SELECT LEVEL");
        en.put("hub.instruccion1", "Use WASD or arrows to move");
        en.put("hub.instruccion2", "Step on a level to select it");
        en.put("hub.instruccion3", "ENTER/SPACE to enter level");
        en.put("hub.instruccion4", "ESC to return to main menu");
        en.put("hub.nivel", "Level");
        en.put("hub.dificultad", "Difficulty");
        en.put("hub.estado", "State");
        en.put("hub.accion", "ENTER/SPACE: Play");

// Francés
        fr.put("hub.titulo", "SÉLECTIONNER NIVEAU");
        fr.put("hub.instruccion1", "Utilisez WASD ou flèches pour vous déplacer");
        fr.put("hub.instruccion2", "Marchez sur un niveau pour le sélectionner");
        fr.put("hub.instruccion3", "ENTRÉE/ESPACE pour entrer dans le niveau");
        fr.put("hub.instruccion4", "ÉCHAP pour revenir au menu principal");
        fr.put("hub.nivel", "Niveau");
        fr.put("hub.dificultad", "Difficulté");
        fr.put("hub.estado", "État");
        fr.put("hub.accion", "ENTRÉE/ESPACE: Jouer");

        // Español
        es.put("tutorial.movimiento", "Usa W A S D para moverte");
        es.put("tutorial.empujar", "Empuja las cajas hacia los objetivos");
        es.put("tutorial.evitar", "Evita quedar atrapado");
        es.put("tutorial.resolver", "Resuelve todos los niveles para ganar");

// Inglés
        en.put("tutorial.movimiento", "Use W A S D to move");
        en.put("tutorial.empujar", "Push the boxes to the targets");
        en.put("tutorial.evitar", "Avoid getting stuck");
        en.put("tutorial.resolver", "Complete all levels to win");

// Francés
        fr.put("tutorial.movimiento", "Utilisez Z Q S D pour vous déplacer");
        fr.put("tutorial.empujar", "Poussez les caisses vers les cibles");
        fr.put("tutorial.evitar", "Évitez de rester coincé");
        fr.put("tutorial.resolver", "Terminez tous les niveaux pour gagner");

        // Español
        es.put("controles.titulo", "Configuracion de Controles");
        es.put("controles.cambiar", "Cambiar");
        es.put("controles.volver", "Volver");

// Inglés
        en.put("controles.titulo", "Controls Settings");
        en.put("controles.cambiar", "Change");
        en.put("controles.volver", "Back");

// Francés
        fr.put("controles.titulo", "Configuration des Contrôles");
        fr.put("controles.cambiar", "Changer");
        fr.put("controles.volver", "Retour");
        
        // Español
es.put("settings.cambiar_controles", "Controles");

// Inglés
en.put("settings.cambiar_controles", "Controls");

// Francés
fr.put("settings.cambiar_controles", "Les contrôles");
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
            case "es":
                return "Espanol";
            case "en":
                return "English";
            case "fr":
                return "Francais";
            default:
                return codigo;
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
