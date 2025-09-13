/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.sokoban.com.Juegito;
import com.sokoban.com.SelectorNiveles.Hub;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.SoundManager;
import com.sokoban.com.Usuario;

public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private boolean puedeInteractuar = true;
    private SistemaUsuarios sistemaUsuarios;
    private Label labelUsuario;

    public MenuScreen() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
    }

    @Override
    public void show() {
        // Configurar texturas de botones
        Texture texturaJugar = new Texture(Gdx.files.internal("jugar.png"));
        Drawable fondoJugar = new TextureRegionDrawable(new TextureRegion(texturaJugar));
        Texture texturaJugar2 = new Texture(Gdx.files.internal("jugar2.png"));
        Drawable fondoJugar2 = new TextureRegionDrawable(new TextureRegion(texturaJugar2));
        
        Button.ButtonStyle estiloJugar = new Button.ButtonStyle();
        estiloJugar.up = fondoJugar;
        estiloJugar.down = fondoJugar2;

        Texture texturaExtra = new Texture(Gdx.files.internal("extra.png"));
        Drawable fondoExtra = new TextureRegionDrawable(new TextureRegion(texturaExtra));
        Texture texturaExtra2 = new Texture(Gdx.files.internal("extra2.png"));
        Drawable fondoExtra2 = new TextureRegionDrawable(new TextureRegion(texturaExtra2));
        
        Button.ButtonStyle estiloExtra = new Button.ButtonStyle();
        estiloExtra.up = fondoExtra;
        estiloExtra.down = fondoExtra2;

        Texture texturaSalir = new Texture(Gdx.files.internal("salir.png"));
        Drawable fondoSalir = new TextureRegionDrawable(new TextureRegion(texturaSalir));
        Texture texturaSalir2 = new Texture(Gdx.files.internal("salir2.png"));
        Drawable fondoSalir2 = new TextureRegionDrawable(new TextureRegion(texturaSalir2));
        
        Button.ButtonStyle estiloSalir = new Button.ButtonStyle();
        estiloSalir.up = fondoSalir;
        estiloSalir.down = fondoSalir2;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        bg = new Texture("fondoM.png");

        // Crear elementos UI
        Label titulo = new Label("Sokoban", skin);
        titulo.setColor(Color.CYAN);

        // Información del usuario
        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        if (usuarioActual != null) {
            labelUsuario = new Label("Bienvenido: " + usuarioActual.getNombreCompleto(), skin);
            labelUsuario.setColor(Color.LIGHT_GRAY);
        } else {
            labelUsuario = new Label("Sin sesión activa", skin);
            labelUsuario.setColor(Color.GRAY);
        }

        Button btnJugar = new Button(estiloJugar);
        Button btnPerfil = new Button(estiloExtra);
        Button btnLogin = new Button(estiloExtra);
        Button btnSalir = new Button(estiloSalir);

        // Listeners para botones
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
                        SoundManager.setearMusica(2);
                    } else {
                        mostrarMensajeLogin();
                    }
                }
            }
        });

        btnPerfil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        mostrarPerfil();
                    } else {
                        mostrarMensajeLogin();
                    }
                }
            }
        });

        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        // Cerrar sesión
                        sistemaUsuarios.cerrarSesion();
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                    } else {
                        // Ir a login
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaLogin());
                    }
                }
            }
        });

        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        sistemaUsuarios.cerrarSesion();
                    }
                    System.exit(0);
                }
            }
        });

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(titulo).padBottom(20).row();
        table.add(labelUsuario).padBottom(30).row();
        table.add(btnJugar).size(200, 60).padBottom(15).row();
        table.add(btnPerfil).size(200, 60).padBottom(15).row();
        
        // Cambiar texto del botón según el estado de sesión
        Label labelBotonLogin = new Label(
            sistemaUsuarios.haySesionActiva() ? "Cerrar Sesión" : "Iniciar Sesión", 
            skin
        );
        btnLogin.add(labelBotonLogin);
        
        table.add(btnLogin).size(200, 60).padBottom(15).row();
        table.add(btnSalir).size(200, 60).row();

        stage.addActor(table);
    }

    private void mostrarMensajeLogin() {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel);

        Label titulo = new Label("Acceso Requerido", skin);
        titulo.setColor(Color.YELLOW);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label("Necesitas iniciar sesión\npara acceder a esta función", skin);
        mensaje.setColor(Color.WHITE);
        panel.add(mensaje).padBottom(20).row();

        Table botonesTable = new Table();

        Button btnLogin = new Button(skin);
        btnLogin.add(new Label("Iniciar Sesión", skin));
        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaLogin());
            }
        });

        Button btnCancelar = new Button(skin);
        btnCancelar.add(new Label("Cancelar", skin));
        btnCancelar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        botonesTable.add(btnLogin).size(120, 40).padRight(10);
        botonesTable.add(btnCancelar).size(120, 40);
        panel.add(botonesTable).row();

        stage.addActor(overlay);
    }

    private void mostrarPerfil() {
        puedeInteractuar = false;
        Usuario usuario = sistemaUsuarios.getUsuarioActual();

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel).width(400).height(300);

        Label titulo = new Label("PERFIL DE USUARIO", skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        // Información del usuario
        Label nombreLabel = new Label("Usuario: " + usuario.getNombreUsuario(), skin);
        Label nombreCompletoLabel = new Label("Nombre: " + usuario.getNombreCompleto(), skin);
        Label nivelLabel = new Label("Nivel actual: " + usuario.getNivelActual(), skin);
        Label nivelMaxLabel = new Label("Nivel máximo: " + usuario.getNivelMaximoAlcanzado(), skin);
        Label partidasLabel = new Label("Partidas jugadas: " + usuario.getPartidasTotales(), skin);
        Label puntosLabel = new Label("Puntos totales: " + usuario.getPuntuacionTotal(), skin);

        panel.add(nombreLabel).left().padBottom(5).row();
        panel.add(nombreCompletoLabel).left().padBottom(5).row();
        panel.add(nivelLabel).left().padBottom(5).row();
        panel.add(nivelMaxLabel).left().padBottom(5).row();
        panel.add(partidasLabel).left().padBottom(5).row();
        panel.add(puntosLabel).left().padBottom(15).row();

        // Botones
        Table botonesTable = new Table();

        Button btnEstadisticas = new Button(skin);
        btnEstadisticas.add(new Label("Estadísticas", skin));
        btnEstadisticas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                mostrarEstadisticas();
            }
        });

        Button btnConfiguracion = new Button(skin);
        btnConfiguracion.add(new Label("Configuración", skin));
        btnConfiguracion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                mostrarConfiguracion();
            }
        });

        Button btnCerrar = new Button(skin);
        btnCerrar.add(new Label("Cerrar", skin));
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        botonesTable.add(btnEstadisticas).size(100, 40).padRight(5);
        botonesTable.add(btnConfiguracion).size(100, 40).padRight(5);
        botonesTable.add(btnCerrar).size(100, 40);
        panel.add(botonesTable).row();

        stage.addActor(overlay);
    }

    private void mostrarEstadisticas() {
        puedeInteractuar = false;
        Usuario usuario = sistemaUsuarios.getUsuarioActual();

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel).width(500).height(400);

        Label titulo = new Label("ESTADÍSTICAS DETALLADAS", skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        // Estadísticas por nivel
        Label subtitulo = new Label("Progreso por Niveles:", skin);
        subtitulo.setColor(Color.YELLOW);
        panel.add(subtitulo).left().padBottom(10).row();

        for (int i = 1; i <= 7; i++) {
            String estado = i <= usuario.getNivelMaximoAlcanzado() ? "Desbloqueado" : "Bloqueado";
            String color = i <= usuario.getNivelMaximoAlcanzado() ? "Verde" : "Rojo";
            
            Label nivelInfo = new Label("Nivel " + i + ": " + estado, skin);
            if (i <= usuario.getNivelMaximoAlcanzado()) {
                nivelInfo.setColor(Color.GREEN);
            } else {
                nivelInfo.setColor(Color.RED);
            }
            panel.add(nivelInfo).left().padBottom(3).row();
        }

        // Estadísticas generales
        Label subtitulo2 = new Label("\nEstadísticas Generales:", skin);
        subtitulo2.setColor(Color.YELLOW);
        panel.add(subtitulo2).left().padBottom(10).row();

        long tiempoMinutos = usuario.getTiempoTotalJugado() / 60000;
        Label tiempoLabel = new Label("Tiempo total jugado: " + tiempoMinutos + " minutos", skin);
        Label porcentajeLabel = new Label("Tasa de éxito: " + 
            (usuario.getPartidasTotales() > 0 ? 
                String.format("%.1f%%", (double)usuario.getPartidasCompletadas() / usuario.getPartidasTotales() * 100) : 
                "0%"), skin);

        panel.add(tiempoLabel).left().padBottom(5).row();
        panel.add(porcentajeLabel).left().padBottom(15).row();

        Button btnCerrar = new Button(skin);
        btnCerrar.add(new Label("Cerrar", skin));
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        panel.add(btnCerrar).size(100, 40).row();
        stage.addActor(overlay);
    }

    private void mostrarConfiguracion() {
        puedeInteractuar = false;
        Usuario usuario = sistemaUsuarios.getUsuarioActual();

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel).width(400).height(300);

        Label titulo = new Label("CONFIGURACIÓN", skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label("Configuraciones del juego\n(Próximamente más opciones)", skin);
        panel.add(mensaje).padBottom(20).row();

        // Botón para crear backup
        Button btnBackup = new Button(skin);
        btnBackup.add(new Label("Crear Copia de Seguridad", skin));
        btnBackup.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean exito = sistemaUsuarios.crearBackup();
                Label resultado = new Label(
                    exito ? "¡Backup creado exitosamente!" : "Error creando backup", 
                    skin
                );
                resultado.setColor(exito ? Color.GREEN : Color.RED);
                
                // Mostrar mensaje temporal
                panel.add(resultado).padBottom(10).row();
            }
        });

        Button btnCerrar = new Button(skin);
        btnCerrar.add(new Label("Cerrar", skin));
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        panel.add(btnBackup).size(200, 40).padBottom(10).row();
        panel.add(btnCerrar).size(100, 40).row();

        stage.addActor(overlay);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibujar fondo
        stage.getBatch().begin();
        stage.getBatch().draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getCamera().update(); 
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (bg != null) bg.dispose();
    }
}