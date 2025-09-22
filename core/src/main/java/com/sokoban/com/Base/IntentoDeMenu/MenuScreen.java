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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.EstadisticasNivel;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;
import com.sokoban.com.SelectorNiveles.Hub;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.SlideshowTutorial;
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
    protected BitmapFont font;

    public MenuScreen() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
        idiomas = Idiomas.getInstance();
        // cargar idioma del usuario al entrar al menu
        idiomas.cargarIdiomaUsuario();
    }

    @Override
    public void show() {
        
        Texture texturaFondoNormal = new Texture(Gdx.files.internal("fondoNormal.png"));
        Drawable fondoNormalUp = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal));

        Texture texturaFondoNormal2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoNormalDown = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal2));

        // Crear estilo único para todos los botones
        Button.ButtonStyle estiloBoton = new Button.ButtonStyle();
        estiloBoton.up = fondoNormalUp;
        estiloBoton.down = fondoNormalDown;

        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createPixelFont();

        bg = new Texture("fondoM.png");

        crearInterfaz(estiloBoton);
    }

    private void createPixelFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-vaV7.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16; // Tamaño de fuente para los botones del menú principal
        font = generator.generateFont(parameter);
        skin.add("default-font", font, BitmapFont.class);

        // Generar una fuente más pequeña para submenús
        parameter.size = 12;
        BitmapFont mediumFont = generator.generateFont(parameter);
        skin.add("medium-font", mediumFont, BitmapFont.class);

        // Generar la fuente más pequeña para los submenús y los botones
        parameter.size = 8;
        BitmapFont smallFont = generator.generateFont(parameter);
        skin.add("small-font", smallFont, BitmapFont.class);

        generator.dispose();
    }

    private void crearInterfaz(Button.ButtonStyle estiloBoton) {
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

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = estiloBoton.up;
        textButtonStyle.down = estiloBoton.down;
        textButtonStyle.font = skin.getFont("medium-font"); // Changed font to medium-font
        textButtonStyle.fontColor = Color.WHITE;

        TextButton btnJugar = new TextButton(idiomas.obtenerTexto("menu.jugar"), textButtonStyle);
        TextButton btnPerfil = new TextButton(idiomas.obtenerTexto("menu.perfil"), textButtonStyle);
        TextButton btnAmigos = new TextButton(idiomas.obtenerTexto("menu.amigos"), textButtonStyle);
        TextButton btnIdioma = new TextButton("Idioma / Language", textButtonStyle);
        TextButton btnLogin = new TextButton(
                sistemaUsuarios.haySesionActiva()
                ? idiomas.obtenerTexto("menu.cerrar_sesion")
                : idiomas.obtenerTexto("menu.iniciar_sesion"), textButtonStyle
        );
        TextButton btnSalir = new TextButton(idiomas.obtenerTexto("menu.salir"), textButtonStyle);

        // Listeners para botones
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!puedeInteractuar) {
                    return;
                }

                if (!sistemaUsuarios.haySesionActiva()) {
                    mostrarMensajeLogin();
                    return;
                }

                // Usuario actual
                Usuario u = sistemaUsuarios.getUsuarioActual();

                // Si es la primera vez (sin partidas), mostrar tutorial
                if (u.getPartidasTotales() == 0) {
                    ((Juegito) Gdx.app.getApplicationListener())
                            .setScreen(new SlideshowTutorial((Juegito) Gdx.app.getApplicationListener(), idiomas));
                    SoundManager.playMusic("nivel", true, 0.7f);
                } else {
                    // ir directamente al Hub
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
                    SoundManager.playMusic("nivel", true, 0.7f);
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

        table.add(titulo).padBottom(20).row();
        table.add(labelUsuario).padBottom(30).row();
        table.add(btnJugar).size(250, 50).padBottom(10).row(); // Adjusted size
        table.add(btnPerfil).size(250, 50).padBottom(10).row(); // Adjusted size
        table.add(btnAmigos).size(250, 50).padBottom(10).row(); // Adjusted size
        table.add(btnIdioma).size(250, 50).padBottom(10).row(); // Adjusted size
        table.add(btnLogin).size(250, 50).padBottom(10).row(); // Adjusted size
        table.add(btnSalir).size(250, 50).row(); // Adjusted size

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
        overlay.add(panel).width(350).height(300); // Tamaño del panel reducido

        Label titulo = new Label(idiomas.obtenerTexto("config.titulo"), skin, "small-font", Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        // mostrar idioma actual
        Label labelActual = new Label(idiomas.obtenerTexto("config.idioma_actual"), skin, "small-font", Color.WHITE);
        panel.add(labelActual).left().padBottom(5).row();

        Label idiomaActual = new Label(idiomas.getNombreIdioma(idiomas.getIdiomaActual()), skin, "small-font", Color.YELLOW);
        panel.add(idiomaActual).left().padBottom(15).row();

        // selector de idioma
        Label labelSelector = new Label(idiomas.obtenerTexto("config.seleccionar"), skin, "small-font", Color.WHITE);
        panel.add(labelSelector).left().padBottom(10).row();

        // botones para cada idioma
        Table tablaIdiomas = new Table();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("small-font");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round-down");

        TextButton btnEspanol = new TextButton("Español", textButtonStyle);
        btnEspanol.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarIdioma("es", overlay);
            }
        });

        TextButton btnIngles = new TextButton("English", textButtonStyle);
        btnIngles.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarIdioma("en", overlay);
            }
        });

        TextButton btnFrances = new TextButton("Français", textButtonStyle);
        btnFrances.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarIdioma("fr", overlay);
            }
        });

        tablaIdiomas.add(btnEspanol).size(150, 40).padBottom(8).row();
        tablaIdiomas.add(btnIngles).size(150, 40).padBottom(8).row();
        tablaIdiomas.add(btnFrances).size(150, 40).padBottom(8).row();

        panel.add(tablaIdiomas).row();

        // boton cerrar
        TextButton btnCerrar = new TextButton(idiomas.obtenerTexto("general.cerrar"), textButtonStyle);
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });
        panel.add(btnCerrar).size(150, 40).row();
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
    overlay.add(panel).width(400).height(220); // Panel mas ancho

    Label titulo = new Label(idiomas.obtenerTexto("login.acceso_requerido"), skin, "small-font", Color.YELLOW);
    panel.add(titulo).padBottom(15).row();

    // SOLUCION: Usar wrap para que el texto haga salto de linea
    Label mensaje = new Label(idiomas.obtenerTexto("login.necesita_sesion"), skin, "small-font", Color.WHITE);
    mensaje.setWrap(true); // Activar wrap
    panel.add(mensaje).width(350).padBottom(20).row(); // Definir ancho maximo

    Table botonesTable = new Table();

    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    textButtonStyle.font = skin.getFont("small-font");
    textButtonStyle.fontColor = Color.WHITE;
    textButtonStyle.up = skin.getDrawable("default-round");
    textButtonStyle.down = skin.getDrawable("default-round-down");

    TextButton btnLogin = new TextButton(idiomas.obtenerTexto("menu.iniciar_sesion"), textButtonStyle);
    btnLogin.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            overlay.remove();
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaLogin());
        }
    });

    TextButton btnCancelar = new TextButton(idiomas.obtenerTexto("general.cancelar"), textButtonStyle);
    btnCancelar.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            overlay.remove();
            puedeInteractuar = true;
        }
    });

    botonesTable.add(btnLogin).size(150, 40).padRight(10);
    botonesTable.add(btnCancelar).size(150, 40);
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
        overlay.add(panel).width(400).height(350); // Tamaño del panel reducido

        Label titulo = new Label(idiomas.obtenerTexto("perfil.titulo"), skin, "default-font", Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        // Información del usuario
        Label nombreLabel = new Label(idiomas.obtenerTexto("perfil.usuario") + " " + usuario.getNombreUsuario(), skin, "small-font", Color.WHITE);
        Label nombreCompletoLabel = new Label(idiomas.obtenerTexto("perfil.nombre") + " " + usuario.getNombreCompleto(), skin, "small-font", Color.WHITE);
        Label nivelLabel = new Label(idiomas.obtenerTexto("perfil.nivel_actual") + " " + usuario.getNivelActual(), skin, "small-font", Color.WHITE);
        Label nivelMaxLabel = new Label(idiomas.obtenerTexto("perfil.nivel_maximo") + " " + usuario.getNivelMaximoAlcanzado(), skin, "small-font", Color.WHITE);
        Label partidasLabel = new Label(idiomas.obtenerTexto("perfil.partidas_jugadas") + " " + usuario.getPartidasTotales(), skin, "small-font", Color.WHITE);
        Label puntosLabel = new Label(idiomas.obtenerTexto("perfil.puntos_totales") + " " + usuario.getPuntuacionTotal(), skin, "small-font", Color.WHITE);

        panel.add(nombreLabel).left().padBottom(5).row();
        panel.add(nombreCompletoLabel).left().padBottom(5).row();
        panel.add(nivelLabel).left().padBottom(5).row();
        panel.add(nivelMaxLabel).left().padBottom(5).row();
        panel.add(partidasLabel).left().padBottom(5).row();
        panel.add(puntosLabel).left().padBottom(15).row();

        // Botones
        Table botonesTable = new Table();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("small-font");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round-down");

        TextButton btnEstadisticas = new TextButton(idiomas.obtenerTexto("perfil.estadisticas"), textButtonStyle);
        btnEstadisticas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                mostrarEstadisticas();
            }
        });

        TextButton btnAvatares = new TextButton(idiomas.obtenerTexto("perfil.avatares"), textButtonStyle);
        btnAvatares.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaAvatares());
            }
        });

        TextButton btnRanking = new TextButton(idiomas.obtenerTexto("perfil.ranking"), textButtonStyle);
        btnRanking.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaRanking());
            }
        });

        TextButton btnComparar = new TextButton(idiomas.obtenerTexto("perfil.comparar"), textButtonStyle);
        btnComparar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaComparacion());
            }
        });

        TextButton btnConfiguracion = new TextButton(idiomas.obtenerTexto("perfil.configuracion"), textButtonStyle);
        btnConfiguracion.getLabel().setStyle(new Label.LabelStyle(skin.getFont("small-font"), Color.WHITE));
        btnConfiguracion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                mostrarConfiguracion();
            }
        });

        TextButton btnCerrar = new TextButton(idiomas.obtenerTexto("perfil.cerrar"), textButtonStyle);
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        // organizar botones en 3 filas
        botonesTable.add(btnEstadisticas).size(100, 35).padRight(5);
        botonesTable.add(btnAvatares).size(100, 35).padRight(5);
        botonesTable.add(btnRanking).size(100, 35).row();
        botonesTable.add(btnComparar).size(100, 35).padRight(5).padTop(8);
        botonesTable.add(btnConfiguracion).size(100, 35).padRight(5).padTop(8);
        botonesTable.add(btnCerrar).size(100, 35).padTop(8);

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

    Label titulo = new Label(idiomas.obtenerTexto("perfil.estadisticas_detalladas"), skin, "default-font", Color.CYAN);
    panel.add(titulo).padBottom(15).row();

    // Estadisticas por nivel
    Label subtitulo = new Label(idiomas.obtenerTexto("perfil.progreso_niveles"), skin, "small-font", Color.YELLOW);
    panel.add(subtitulo).left().padBottom(10).row();

    for (int i = 1; i <= 7; i++) {
        String textoNivel = idiomas.obtenerTexto("perfil.nivel") + " " + i + ": ";
        
        if (i <= usuario.getNivelMaximoAlcanzado()) {
            textoNivel += idiomas.obtenerTexto("perfil.desbloqueado");
            
            // Agregar mejor tiempo si existe
            EstadisticasNivel stats = usuario.getEstadisticasNivel(i);
            if (stats.getMejorTiempo() != Long.MAX_VALUE) {
                long segundos = stats.getMejorTiempo() / 1000;
                long minutos = segundos / 60;
                segundos = segundos % 60;
                textoNivel += String.format(" - Mejor: %dm %ds", minutos, segundos);
            }
        } else {
            textoNivel += idiomas.obtenerTexto("perfil.bloqueado");
        }
        
        Label nivelInfo = new Label(textoNivel, skin, "small-font", Color.WHITE);
        if (i <= usuario.getNivelMaximoAlcanzado()) {
            nivelInfo.setColor(Color.GREEN);
        } else {
            nivelInfo.setColor(Color.RED);
        }
        panel.add(nivelInfo).left().padBottom(3).row();
    }

    // Estadisticas generales
    Label subtitulo2 = new Label(idiomas.obtenerTexto("perfil.estadisticas_generales"), skin, "small-font", Color.YELLOW);
    panel.add(subtitulo2).left().padBottom(10).row();

    long tiempoMinutos = usuario.getTiempoTotalJugado() / 60000;
    Label tiempoLabel = new Label(idiomas.obtenerTexto("perfil.tiempo_total") + " " + tiempoMinutos + " " + idiomas.obtenerTexto("perfil.minutos"), skin, "small-font", Color.WHITE);
    Label porcentajeLabel = new Label(idiomas.obtenerTexto("perfil.tasa_exito") + " "
            + (usuario.getPartidasTotales() > 0
            ? String.format("%.1f%%", (double) usuario.getPartidasCompletadas() / usuario.getPartidasTotales() * 100)
            : "0%"), skin, "small-font", Color.WHITE);

    panel.add(tiempoLabel).left().padBottom(5).row();
    panel.add(porcentajeLabel).left().padBottom(15).row();

    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    textButtonStyle.font = skin.getFont("small-font");
    textButtonStyle.fontColor = Color.WHITE;
    textButtonStyle.up = skin.getDrawable("default-round");
    textButtonStyle.down = skin.getDrawable("default-round-down");

    TextButton btnCerrar = new TextButton(idiomas.obtenerTexto("general.cerrar"), textButtonStyle);
    btnCerrar.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            overlay.remove();
            puedeInteractuar = true;
        }
    });
    panel.add(btnCerrar).size(150, 40).row();
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
        overlay.add(panel).width(400).height(300); // Tamaño del panel reducido

        Label titulo = new Label(idiomas.obtenerTexto("perfil.configuracion"), skin, "default-font", Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("small-font");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round-down");

        // Botón para configuración de juego (Settings)
        TextButton btnConfigJuego = new TextButton(idiomas.obtenerTexto("menu.configuracion"), textButtonStyle);
        btnConfigJuego.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaSettings());
            }
        });

        // Botón para configuración de idioma
        TextButton btnIdioma = new TextButton("Idioma / Language", textButtonStyle);
        btnIdioma.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                mostrarConfiguracionIdioma();
            }
        });

        TextButton btnBackup = new TextButton(idiomas.obtenerTexto("config.crear_backup"), textButtonStyle);
        btnBackup.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean exito = sistemaUsuarios.crearBackup();
                mostrarMensaje(exito
                        ? idiomas.obtenerTexto("config.backup_creado")
                        : idiomas.obtenerTexto("config.error_backup"),
                        exito ? Color.GREEN : Color.RED);
            }
        });

        TextButton btnCerrar = new TextButton(idiomas.obtenerTexto("general.cerrar"), textButtonStyle);
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        panel.add(btnConfigJuego).size(200, 40).padBottom(8).row();
        panel.add(btnIdioma).size(200, 40).padBottom(8).row();
        panel.add(btnBackup).size(200, 40).padBottom(8).row();
        panel.add(btnCerrar).size(150, 40).row();

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
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.dispose();
        skin.dispose();
        bg.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        bg.dispose();
    }

}
