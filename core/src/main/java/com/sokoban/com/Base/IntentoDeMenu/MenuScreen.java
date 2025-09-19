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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Idiomas;
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
    private Idiomas idiomas;
    private Label labelUsuario;

    public MenuScreen() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
        idiomas = Idiomas.getInstance();
        // cargar idioma del usuario al entrar al menu
        idiomas.cargarIdiomaUsuario();
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

        crearInterfaz(estiloJugar, estiloExtra, estiloSalir);
    }

    private void crearInterfaz(Button.ButtonStyle estiloJugar, Button.ButtonStyle estiloExtra, Button.ButtonStyle estiloSalir) {
        // Crear elementos UI con textos traducidos
        Label titulo = new Label(idiomas.obtenerTexto("menu.titulo"), skin);
        titulo.setColor(Color.CYAN);

        // Información del usuario con texto traducido
        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        if (usuarioActual != null) {
            labelUsuario = new Label(idiomas.obtenerTexto("menu.bienvenido") + " " + usuarioActual.getNombreCompleto(), skin);
            labelUsuario.setColor(Color.LIGHT_GRAY);
        } else {
            labelUsuario = new Label(idiomas.obtenerTexto("menu.sin_sesion"), skin);
            labelUsuario.setColor(Color.GRAY);
        }

        Button btnJugar = new Button(estiloJugar);
        Button btnPerfil = new Button(estiloExtra);
        Button btnAmigos = new Button(estiloExtra);
        Button btnIdioma = new Button(estiloExtra);
        Button btnLogin = new Button(estiloExtra);
        Button btnSalir = new Button(estiloSalir);

        // Listeners para botones
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
                        SoundManager.playMusic("nivel", true, 0.7f);
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

        btnAmigos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaAmigos());
                    } else {
                        mostrarMensajeLogin();
                    }
                }
            }
        });

        btnIdioma.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    mostrarConfiguracionIdioma();
                }
            }
        });

        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        sistemaUsuarios.cerrarSesion();
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                    } else {
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

        // Layout con textos traducidos
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // agregar labels traducidos a botones
        btnJugar.add(new Label(idiomas.obtenerTexto("menu.jugar"), skin));
        btnPerfil.add(new Label(idiomas.obtenerTexto("menu.perfil"), skin));
        btnAmigos.add(new Label(idiomas.obtenerTexto("menu.amigos"), skin));
        btnIdioma.add(new Label("Idioma / Language", skin));
        
        Label labelBotonLogin = new Label(
            sistemaUsuarios.haySesionActiva() ? 
                idiomas.obtenerTexto("menu.cerrar_sesion") : 
                idiomas.obtenerTexto("menu.iniciar_sesion"), 
            skin
        );
        btnLogin.add(labelBotonLogin);
        
        btnSalir.add(new Label(idiomas.obtenerTexto("menu.salir"), skin));

        table.add(titulo).padBottom(20).row();
        table.add(labelUsuario).padBottom(30).row();
        table.add(btnJugar).size(200, 60).padBottom(15).row();
        table.add(btnPerfil).size(200, 60).padBottom(15).row();
        table.add(btnAmigos).size(200, 60).padBottom(15).row();
        table.add(btnIdioma).size(200, 60).padBottom(15).row();
        table.add(btnLogin).size(200, 60).padBottom(15).row();
        table.add(btnSalir).size(200, 60).row();

        stage.addActor(table);
    }

    private void mostrarConfiguracionIdioma() {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel).width(350).height(300);

        Label titulo = new Label(idiomas.obtenerTexto("config.titulo"), skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(20).row();

        // mostrar idioma actual
        Label labelActual = new Label(idiomas.obtenerTexto("config.idioma_actual"), skin);
        labelActual.setColor(Color.WHITE);
        panel.add(labelActual).left().padBottom(5).row();

        Label idiomaActual = new Label(idiomas.getNombreIdioma(idiomas.getIdiomaActual()), skin);
        idiomaActual.setColor(Color.YELLOW);
        panel.add(idiomaActual).left().padBottom(20).row();

        // selector de idioma
        Label labelSelector = new Label(idiomas.obtenerTexto("config.seleccionar"), skin);
        labelSelector.setColor(Color.WHITE);
        panel.add(labelSelector).left().padBottom(10).row();

        // botones para cada idioma
        Table tablaIdiomas = new Table();

        Button btnEspanol = new Button(skin);
        btnEspanol.add(new Label("Español", skin));
        btnEspanol.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarIdioma("es", overlay);
            }
        });

        Button btnIngles = new Button(skin);
        btnIngles.add(new Label("English", skin));
        btnIngles.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarIdioma("en", overlay);
            }
        });

        Button btnFrances = new Button(skin);
        btnFrances.add(new Label("Français", skin));
        btnFrances.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarIdioma("fr", overlay);
            }
        });

        tablaIdiomas.add(btnEspanol).size(80, 35).padBottom(5).row();
        tablaIdiomas.add(btnIngles).size(80, 35).padBottom(5).row();
        tablaIdiomas.add(btnFrances).size(80, 35).padBottom(15).row();

        panel.add(tablaIdiomas).row();

        // boton cerrar
        Button btnCerrar = new Button(skin);
        btnCerrar.add(new Label(idiomas.obtenerTexto("general.cerrar"), skin));
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

    private void cambiarIdioma(String nuevoIdioma, Table overlay) {
        idiomas.cambiarIdioma(nuevoIdioma);
        
        // guardar en perfil del usuario si hay sesion activa
        if (sistemaUsuarios.haySesionActiva()) {
            idiomas.guardarIdiomaUsuario(nuevoIdioma);
        }
        
        overlay.remove();
        mostrarMensaje(idiomas.obtenerTexto("config.idioma_cambiado"), Color.GREEN);
        
        // recargar la pantalla con el nuevo idioma después de un delay
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                Gdx.app.postRunnable(() -> {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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

        Label titulo = new Label(idiomas.obtenerTexto("login.acceso_requerido"), skin);
        titulo.setColor(Color.YELLOW);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label(idiomas.obtenerTexto("login.necesita_sesion"), skin);
        mensaje.setColor(Color.WHITE);
        panel.add(mensaje).padBottom(20).row();

        Table botonesTable = new Table();

        Button btnLogin = new Button(skin);
        btnLogin.add(new Label(idiomas.obtenerTexto("menu.iniciar_sesion"), skin));
        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaLogin());
            }
        });

        Button btnCancelar = new Button(skin);
        btnCancelar.add(new Label(idiomas.obtenerTexto("general.cancelar"), skin));
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
        overlay.add(panel).width(400).height(370);

        Label titulo = new Label(idiomas.obtenerTexto("perfil.titulo"), skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        // Información del usuario
        Label nombreLabel = new Label(idiomas.obtenerTexto("perfil.usuario") + " " + usuario.getNombreUsuario(), skin);
        Label nombreCompletoLabel = new Label(idiomas.obtenerTexto("perfil.nombre") + " " + usuario.getNombreCompleto(), skin);
        Label nivelLabel = new Label(idiomas.obtenerTexto("perfil.nivel_actual") + " " + usuario.getNivelActual(), skin);
        Label nivelMaxLabel = new Label(idiomas.obtenerTexto("perfil.nivel_maximo") + " " + usuario.getNivelMaximoAlcanzado(), skin);
        Label partidasLabel = new Label(idiomas.obtenerTexto("perfil.partidas_jugadas") + " " + usuario.getPartidasTotales(), skin);
        Label puntosLabel = new Label(idiomas.obtenerTexto("perfil.puntos_totales") + " " + usuario.getPuntuacionTotal(), skin);

        panel.add(nombreLabel).left().padBottom(5).row();
        panel.add(nombreCompletoLabel).left().padBottom(5).row();
        panel.add(nivelLabel).left().padBottom(5).row();
        panel.add(nivelMaxLabel).left().padBottom(5).row();
        panel.add(partidasLabel).left().padBottom(5).row();
        panel.add(puntosLabel).left().padBottom(15).row();

        // Botones
        Table botonesTable = new Table();

        Button btnEstadisticas = new Button(skin);
        btnEstadisticas.add(new Label(idiomas.obtenerTexto("perfil.estadisticas"), skin));
        btnEstadisticas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                mostrarEstadisticas();
            }
        });

        Button btnAvatares = new Button(skin);
        btnAvatares.add(new Label(idiomas.obtenerTexto("perfil.avatares"), skin));
        btnAvatares.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaAvatares());
            }
        });

        Button btnRanking = new Button(skin);
        btnRanking.add(new Label(idiomas.obtenerTexto("perfil.ranking"), skin));
        btnRanking.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaRanking());
            }
        });

        Button btnComparar = new Button(skin);
        btnComparar.add(new Label(idiomas.obtenerTexto("perfil.comparar"), skin));
        btnComparar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaComparacion());
            }
        });

        Button btnConfiguracion = new Button(skin);
        btnConfiguracion.add(new Label(idiomas.obtenerTexto("perfil.configuracion"), skin));
        btnConfiguracion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                mostrarConfiguracion();
            }
        });

        Button btnCerrar = new Button(skin);
        btnCerrar.add(new Label(idiomas.obtenerTexto("perfil.cerrar"), skin));
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        // organizar botones en 3 filas
        botonesTable.add(btnEstadisticas).size(80, 30).padRight(3);
        botonesTable.add(btnAvatares).size(80, 30).padRight(3);
        botonesTable.add(btnRanking).size(80, 30).row();
        botonesTable.add(btnComparar).size(80, 30).padRight(3).padTop(3);
        botonesTable.add(btnConfiguracion).size(80, 30).padRight(3).padTop(3);
        botonesTable.add(btnCerrar).size(80, 30).padTop(3);
        
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

        Label titulo = new Label(idiomas.obtenerTexto("perfil.estadisticas_detalladas"), skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        // Estadísticas por nivel
        Label subtitulo = new Label(idiomas.obtenerTexto("perfil.progreso_niveles"), skin);
        subtitulo.setColor(Color.YELLOW);
        panel.add(subtitulo).left().padBottom(10).row();

        for (int i = 1; i <= 7; i++) {
            String estado = i <= usuario.getNivelMaximoAlcanzado() ? 
                idiomas.obtenerTexto("perfil.desbloqueado") : 
                idiomas.obtenerTexto("perfil.bloqueado");
            
            Label nivelInfo = new Label(idiomas.obtenerTexto("perfil.nivel") + " " + i + ": " + estado, skin);
            if (i <= usuario.getNivelMaximoAlcanzado()) {
                nivelInfo.setColor(Color.GREEN);
            } else {
                nivelInfo.setColor(Color.RED);
            }
            panel.add(nivelInfo).left().padBottom(3).row();
        }

        // Estadísticas generales
        Label subtitulo2 = new Label(idiomas.obtenerTexto("perfil.estadisticas_generales"), skin);
        subtitulo2.setColor(Color.YELLOW);
        panel.add(subtitulo2).left().padBottom(10).row();

        long tiempoMinutos = usuario.getTiempoTotalJugado() / 60000;
        Label tiempoLabel = new Label(idiomas.obtenerTexto("perfil.tiempo_total") + " " + tiempoMinutos + " " + idiomas.obtenerTexto("perfil.minutos"), skin);
        Label porcentajeLabel = new Label(idiomas.obtenerTexto("perfil.tasa_exito") + " " + 
            (usuario.getPartidasTotales() > 0 ? 
                String.format("%.1f%%", (double)usuario.getPartidasCompletadas() / usuario.getPartidasTotales() * 100) : 
                "0%"), skin);

        panel.add(tiempoLabel).left().padBottom(5).row();
        panel.add(porcentajeLabel).left().padBottom(15).row();

        Button btnCerrar = new Button(skin);
        btnCerrar.add(new Label(idiomas.obtenerTexto("general.cerrar"), skin));
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

    Table overlay = new Table();
    overlay.setFillParent(true);
    overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
    overlay.center();

    Table panel = new Table(skin);
    panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
    panel.pad(20);
    overlay.add(panel).width(400).height(350);

    Label titulo = new Label(idiomas.obtenerTexto("perfil.configuracion"), skin);
    titulo.setColor(Color.CYAN);
    panel.add(titulo).padBottom(15).row();

    // Botón para configuración de juego (Settings)
    Button btnConfigJuego = new Button(skin);
    Label labelConfigJuego = new Label(idiomas.obtenerTexto("menu.configuracion"), skin);
    labelConfigJuego.setFontScale(0.8f);
    btnConfigJuego.add(labelConfigJuego);
    btnConfigJuego.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            overlay.remove();
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaSettings());
        }
    });

    // Botón para configuración de idioma
    Button btnIdioma = new Button(skin);
    Label labelIdioma = new Label("Idioma / Language", skin);
    labelIdioma.setFontScale(0.8f);
    btnIdioma.add(labelIdioma);
    btnIdioma.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            overlay.remove();
            mostrarConfiguracionIdioma();
        }
    });

    Button btnBackup = new Button(skin);
    Label labelBackup = new Label(idiomas.obtenerTexto("config.crear_backup"), skin);
    labelBackup.setFontScale(0.8f);
    btnBackup.add(labelBackup);
    btnBackup.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            boolean exito = sistemaUsuarios.crearBackup();
            mostrarMensaje(exito ? 
                idiomas.obtenerTexto("config.backup_creado") : 
                idiomas.obtenerTexto("config.error_backup"), 
                exito ? Color.GREEN : Color.RED);
        }
    });

    Button btnCerrar = new Button(skin);
    Label labelCerrar = new Label(idiomas.obtenerTexto("general.cerrar"), skin);
    labelCerrar.setFontScale(0.8f);
    btnCerrar.add(labelCerrar);
    btnCerrar.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            overlay.remove();
            puedeInteractuar = true;
        }
    });

    panel.add(btnConfigJuego).size(200, 40).padBottom(10).row();
    panel.add(btnIdioma).size(200, 40).padBottom(10).row();
    panel.add(btnBackup).size(200, 40).padBottom(10).row();
    panel.add(btnCerrar).size(100, 40).row();

    stage.addActor(overlay);
}

    private void mostrarMensaje(String texto, Color color) {
        Label mensajeTemporal = new Label(texto, skin);
        mensajeTemporal.setColor(color);
        mensajeTemporal.setPosition(10, Gdx.graphics.getHeight() - 50);
        stage.addActor(mensajeTemporal);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Gdx.app.postRunnable(() -> mensajeTemporal.remove());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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