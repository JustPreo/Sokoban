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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.Usuario;
import java.util.List;

public class PantallaAmigos implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private boolean puedeInteractuar = true;
    private SistemaUsuarios sistemaUsuarios;

    // texturas de botones
    private Texture texturaBuscar, texturaBuscar2;
    private Texture texturaActualizar, texturaActualizar2;
    private Texture texturaVolver, texturaVolver2;
    private Button.ButtonStyle estiloBuscar, estiloActualizar, estiloVolver;
    private Button.ButtonStyle estiloBoton;
    TextButton.TextButtonStyle estiloBotonTexto;

    // campos de busqueda
    private TextField campoBusqueda;
    private ScrollPane scrollAmigos;
    private Table tablaAmigos;

    public PantallaAmigos() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
    }

    private void createPixelFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-vaV7.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 16;
        BitmapFont defaultFont = generator.generateFont(parameter);
        skin.add("default-font", defaultFont, BitmapFont.class);

        parameter.size = 12;
        BitmapFont mediumFont = generator.generateFont(parameter);
        skin.add("medium-font", mediumFont, BitmapFont.class);

        parameter.size = 8;
        BitmapFont smallFont = generator.generateFont(parameter);
        skin.add("small-font", smallFont, BitmapFont.class);

        generator.dispose();
    }

    @Override
    public void show() {
        Idiomas idiomas = Idiomas.getInstance();

        // === TEXTURAS GENERALES PARA BOTONES ===
        Texture texturaFondoNormal = new Texture(Gdx.files.internal("fondoNormal.png"));
        Drawable fondoNormalUp = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal));

        Texture texturaFondoNormal2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoNormalDown = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal2));

        estiloBoton = new Button.ButtonStyle();
        estiloBoton.up = fondoNormalUp;
        estiloBoton.down = fondoNormalDown;

        // === STAGE Y SKIN ===
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createPixelFont();
        bg = new Texture("fondoM.png");
        estiloBotonTexto = new TextButton.TextButtonStyle();
        estiloBotonTexto.up = fondoNormalUp;
        estiloBotonTexto.down = fondoNormalDown;
        estiloBotonTexto.font = skin.getFont("medium-font"); 
        crearInterfaz(idiomas);
    }

    private void crearInterfaz(Idiomas idiomas) {
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();

        // titulo
        Label titulo = new Label(idiomas.obtenerTexto("amigos.titulo"), skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // campo de busqueda
        Table tablaBusqueda = new Table();
        Label labelBuscar = new Label(idiomas.obtenerTexto("amigos.buscar.label"), skin);
        labelBuscar.setColor(Color.WHITE);
        campoBusqueda = new TextField("", skin);
        campoBusqueda.setMessageText(idiomas.obtenerTexto("amigos.buscar.placeholder"));

        TextButton btnBuscar = new TextButton(idiomas.obtenerTexto("amigos.buscar"), estiloBotonTexto);
        btnBuscar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    buscarUsuario(idiomas);
                }
            }
        });

        tablaBusqueda.add(labelBuscar).padRight(10);
        tablaBusqueda.add(campoBusqueda).width(200).padRight(10);
        tablaBusqueda.add(btnBuscar).size(150, 50);
        tablaPrincipal.add(tablaBusqueda).padBottom(20).row();

        // seccion amigos
        Label labelAmigos = new Label(idiomas.obtenerTexto("amigos.mis_amigos"), skin);
        labelAmigos.setColor(Color.YELLOW);
        tablaPrincipal.add(labelAmigos).left().padBottom(10).row();

        tablaAmigos = new Table();
        actualizarListaAmigos(idiomas);

        scrollAmigos = new ScrollPane(tablaAmigos, skin);
        scrollAmigos.setSize(600, 200);
        scrollAmigos.setScrollingDisabled(true, false);
        tablaPrincipal.add(scrollAmigos).size(600, 200).padBottom(20).row();

        // botones de navegacion
        Table tablaBotones = new Table();

        TextButton btnActualizar = new TextButton(idiomas.obtenerTexto("amigos.actualizar"), estiloBotonTexto);
        btnActualizar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    actualizarListaAmigos(idiomas);
                }
            }
        });

        TextButton btnVolver = new TextButton(idiomas.obtenerTexto("amigos.volver"), estiloBotonTexto);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                }
            }
        });

        tablaBotones.add(btnActualizar).size(150, 50).padRight(20);
        tablaBotones.add(btnVolver).size(150, 50);
        tablaPrincipal.add(tablaBotones).row();

        stage.addActor(tablaPrincipal);
    }

    private void actualizarListaAmigos(Idiomas idiomas) {
        tablaAmigos.clear();

        if (!sistemaUsuarios.haySesionActiva()) {
            Label sinSesion = new Label(idiomas.obtenerTexto("amigos.sin_sesion"), skin);
            sinSesion.setColor(Color.RED);
            tablaAmigos.add(sinSesion).row();
            return;
        }

        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        List<String> amigos = usuarioActual.getListaAmigos();

        if (amigos.isEmpty()) {
            Label sinAmigos = new Label(idiomas.obtenerTexto("amigos.sin_amigos"), skin);
            sinAmigos.setColor(Color.GRAY);
            tablaAmigos.add(sinAmigos).row();
            return;
        }

        for (String nombreAmigo : amigos) {
            crearFilaAmigo(nombreAmigo, idiomas);
        }
    }

    private void crearFilaAmigo(String nombreAmigo, Idiomas idiomas) {
        Table filaAmigo = new Table(skin);
        filaAmigo.setBackground(skin.newDrawable("default-round", new Color(0.2f, 0.2f, 0.2f, 0.8f)));
        filaAmigo.pad(10);

        Label labelNombre = new Label(nombreAmigo, skin);
        labelNombre.setColor(Color.WHITE);
        filaAmigo.add(labelNombre).width(150).left();

        Label labelInfo = new Label(idiomas.obtenerTexto("amigos.usuario_offline"), skin);
        labelInfo.setColor(Color.LIGHT_GRAY);
        filaAmigo.add(labelInfo).width(200).left().padLeft(20);

        Button btnVer = new Button(skin);
        btnVer.add(new Label(idiomas.obtenerTexto("amigos.ver"), skin));
        btnVer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    mostrarInfoAmigo(nombreAmigo, idiomas);
                }
            }
        });

        Button btnQuitar = new Button(skin);
        btnQuitar.add(new Label(idiomas.obtenerTexto("amigos.quitar"), skin));
        btnQuitar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    confirmarQuitarAmigo(nombreAmigo, idiomas);
                }
            }
        });

        filaAmigo.add(btnVer).size(60, 30).padLeft(10);
        filaAmigo.add(btnQuitar).size(60, 30).padLeft(5);

        tablaAmigos.add(filaAmigo).width(580).padBottom(5).row();
    }

    private void buscarUsuario(Idiomas idiomas) {
        String nombreBuscar = campoBusqueda.getText().trim();

        if (nombreBuscar.isEmpty()) {
            mostrarMensaje(idiomas.obtenerTexto("amigos.msj.escribir_nombre"), Color.YELLOW);
            return;
        }

        if (!SistemaUsuarios.validarNombreUsuario(nombreBuscar)) {
            mostrarMensaje(idiomas.obtenerTexto("amigos.msj.usuario_invalido"), Color.RED);
            return;
        }

        String[] todosLosUsuarios = sistemaUsuarios.listarUsuarios();
        boolean usuarioExiste = false;
        for (String usuario : todosLosUsuarios) {
            if (usuario.equals(nombreBuscar)) {
                usuarioExiste = true;
                break;
            }
        }

        if (!usuarioExiste) {
            mostrarMensaje(String.format(idiomas.obtenerTexto("amigos.msj.usuario_no_encontrado"), nombreBuscar), Color.RED);
            return;
        }

        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        if (usuarioActual.getListaAmigos().contains(nombreBuscar)) {
            mostrarMensaje(String.format(idiomas.obtenerTexto("amigos.msj.ya_amigo"), nombreBuscar), Color.YELLOW);
            return;
        }

        if (nombreBuscar.equals(usuarioActual.getNombreUsuario())) {
            mostrarMensaje(idiomas.obtenerTexto("amigos.msj.agregarte_ti_mismo"), Color.YELLOW);
            return;
        }

        confirmarAgregarAmigo(nombreBuscar, idiomas);
    }

    private void confirmarAgregarAmigo(String nombreAmigo, Idiomas idiomas) {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);

        Label titulo = new Label(idiomas.obtenerTexto("amigos.agregar.titulo"), skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label(String.format(idiomas.obtenerTexto("amigos.agregar.mensaje"), nombreAmigo), skin);
        mensaje.setColor(Color.WHITE);
        panel.add(mensaje).padBottom(20).row();

        Table tablaBotones = new Table();

        Button btnSi = new Button(skin);
        btnSi.add(new Label(idiomas.obtenerTexto("general.si"), skin));
        btnSi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                agregarAmigo(nombreAmigo, idiomas);
                puedeInteractuar = true;
            }
        });

        Button btnNo = new Button(skin);
        btnNo.add(new Label(idiomas.obtenerTexto("general.cancelar"), skin));
        btnNo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        tablaBotones.add(btnSi).size(80, 40).padRight(10);
        tablaBotones.add(btnNo).size(80, 40);
        panel.add(tablaBotones).row();
        overlay.add(panel);

        stage.addActor(overlay);
    }

    private void agregarAmigo(String nombreAmigo, Idiomas idiomas) {
        boolean exito = sistemaUsuarios.agregarAmigo(nombreAmigo);

        if (exito) {
            mostrarMensaje(String.format(idiomas.obtenerTexto("amigos.msj.agregado"), nombreAmigo), Color.GREEN);
            actualizarListaAmigos(idiomas);
            campoBusqueda.setText("");
        } else {
            mostrarMensaje(idiomas.obtenerTexto("amigos.msj.error_agregar"), Color.RED);
        }
    }

    private void confirmarQuitarAmigo(String nombreAmigo, Idiomas idiomas) {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);

        Label titulo = new Label(idiomas.obtenerTexto("amigos.quitar.titulo"), skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label(String.format(idiomas.obtenerTexto("amigos.quitar.mensaje"), nombreAmigo), skin);
        mensaje.setColor(Color.WHITE);
        panel.add(mensaje).padBottom(20).row();

        Table tablaBotones = new Table();

        Button btnSi = new Button(skin);
        btnSi.add(new Label(idiomas.obtenerTexto("amigos.quitar.btn"), skin));
        btnSi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                quitarAmigo(nombreAmigo, idiomas);
                puedeInteractuar = true;
            }
        });

        Button btnNo = new Button(skin);
        btnNo.add(new Label(idiomas.obtenerTexto("general.cancelar"), skin));
        btnNo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        tablaBotones.add(btnSi).size(80, 40).padRight(10);
        tablaBotones.add(btnNo).size(80, 40);
        panel.add(tablaBotones).row();
        overlay.add(panel);

        stage.addActor(overlay);
    }

    private void quitarAmigo(String nombreAmigo, Idiomas idiomas) {
        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        boolean exito = usuarioActual.getListaAmigos().remove(nombreAmigo);

        if (exito) {
            sistemaUsuarios.guardarProgreso();
            mostrarMensaje(String.format(idiomas.obtenerTexto("amigos.msj.quitado"), nombreAmigo), Color.GREEN);
            actualizarListaAmigos(idiomas);
        } else {
            mostrarMensaje(idiomas.obtenerTexto("amigos.msj.error_quitar"), Color.RED);
        }
    }

    private void mostrarInfoAmigo(String nombreAmigo, Idiomas idiomas) {
        mostrarMensaje(String.format(idiomas.obtenerTexto("amigos.info"), nombreAmigo), Color.CYAN);
    }

    private void mostrarMensaje(String texto, Color color) {
        Label mensajeTemporal = new Label(texto, skin);
        mensajeTemporal.setColor(color);
        mensajeTemporal.setPosition(10, Gdx.graphics.getHeight() - 50);
        stage.addActor(mensajeTemporal);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Gdx.app.postRunnable(mensajeTemporal::remove);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getCamera().update();
    }

    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (bg != null) {
            bg.dispose();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
