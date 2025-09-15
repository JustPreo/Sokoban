package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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

    // texturas de botones para mantener consistencia
    private Texture texturaBuscar, texturaBuscar2;
    private Texture texturaActualizar, texturaActualizar2;
    private Texture texturaExtra, texturaExtra2, texturaVolver, texturaVolver2;
    private Button.ButtonStyle estiloBuscar, estiloActualizar, estiloVolver;

    // campos de busqueda
    private TextField campoBusqueda;
    private ScrollPane scrollAmigos;
    private Table tablaAmigos;

    public PantallaAmigos() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
    }

    @Override
    public void show() {
        // === BOTÓN BUSCAR ===
        texturaBuscar = new Texture(Gdx.files.internal("buscar.png"));
        Drawable fondoBuscar = new TextureRegionDrawable(new TextureRegion(texturaBuscar));
        texturaBuscar2 = new Texture(Gdx.files.internal("buscar2.png"));
        Drawable fondoBuscar2 = new TextureRegionDrawable(new TextureRegion(texturaBuscar2));

        estiloBuscar = new Button.ButtonStyle();
        estiloBuscar.up = fondoBuscar;
        estiloBuscar.down = fondoBuscar2;

        // === BOTÓN ACTUALIZAR ===
        texturaActualizar = new Texture(Gdx.files.internal("actualizar.png"));
        Drawable fondoActualizar = new TextureRegionDrawable(new TextureRegion(texturaActualizar));
        texturaActualizar2 = new Texture(Gdx.files.internal("actualizar2.png"));
        Drawable fondoActualizar2 = new TextureRegionDrawable(new TextureRegion(texturaActualizar2));

        estiloActualizar = new Button.ButtonStyle();
        estiloActualizar.up = fondoActualizar;
        estiloActualizar.down = fondoActualizar2;

        // === BOTÓN VOLVER ===
        texturaVolver = new Texture(Gdx.files.internal("volver.png"));
        Drawable fondoVolver = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        texturaVolver2 = new Texture(Gdx.files.internal("volver2.png"));
        Drawable fondoVolver2 = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

        estiloVolver = new Button.ButtonStyle();
        estiloVolver.up = fondoVolver;
        estiloVolver.down = fondoVolver2;

        // === STAGE Y SKIN ===
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        bg = new Texture("fondoM.png");

        crearInterfaz();
    }

    private void crearInterfaz() {
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();

        // titulo
        Label titulo = new Label("GESTION DE AMIGOS", skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // campo de busqueda
        Table tablaBusqueda = new Table();
        Label labelBuscar = new Label("Buscar usuario:", skin);
        labelBuscar.setColor(Color.WHITE);
        campoBusqueda = new TextField("", skin);
        campoBusqueda.setMessageText("Nombre del usuario...");

        Button btnBuscar = new Button(estiloBuscar);
        btnBuscar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    buscarUsuario();
                }
            }
        });

        tablaBusqueda.add(labelBuscar).padRight(10);
        tablaBusqueda.add(campoBusqueda).width(200).padRight(10);
        tablaBusqueda.add(btnBuscar).size(120, 50);
        tablaPrincipal.add(tablaBusqueda).padBottom(20).row();

        // seccion de amigos
        Label labelAmigos = new Label("MIS AMIGOS", skin);
        labelAmigos.setColor(Color.YELLOW);
        tablaPrincipal.add(labelAmigos).left().padBottom(10).row();

        tablaAmigos = new Table();
        actualizarListaAmigos();

        scrollAmigos = new ScrollPane(tablaAmigos, skin);
        scrollAmigos.setSize(600, 200);
        scrollAmigos.setScrollingDisabled(true, false);
        tablaPrincipal.add(scrollAmigos).size(600, 200).padBottom(20).row();

        // botones de navegación
        Table tablaBotones = new Table();

        Button btnActualizar = new Button(estiloActualizar);
        btnActualizar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    actualizarListaAmigos();
                }
            }
        });

        Button btnVolver = new Button(estiloVolver);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                }
            }
        });

        tablaBotones.add(btnActualizar).size(120, 50).padRight(20);
        tablaBotones.add(btnVolver).size(120, 50);
        tablaPrincipal.add(tablaBotones).row();

        stage.addActor(tablaPrincipal);
    }

    private void actualizarListaAmigos() {
        tablaAmigos.clear();

        if (!sistemaUsuarios.haySesionActiva()) {
            Label sinSesion = new Label("No hay sesion activa", skin);
            sinSesion.setColor(Color.RED);
            tablaAmigos.add(sinSesion).row();
            return;
        }

        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        List<String> amigos = usuarioActual.getListaAmigos();

        if (amigos.isEmpty()) {
            Label sinAmigos = new Label("No tienes amigos agregados aun", skin);
            sinAmigos.setColor(Color.GRAY);
            tablaAmigos.add(sinAmigos).row();
            return;
        }

        for (String nombreAmigo : amigos) {
            crearFilaAmigo(nombreAmigo);
        }
    }

    private void crearFilaAmigo(String nombreAmigo) {
        Table filaAmigo = new Table(skin);
        filaAmigo.setBackground(skin.newDrawable("default-round", new Color(0.2f, 0.2f, 0.2f, 0.8f)));
        filaAmigo.pad(10);

        Label labelNombre = new Label(nombreAmigo, skin);
        labelNombre.setColor(Color.WHITE);
        filaAmigo.add(labelNombre).width(150).left();

        String infoExtra = "Usuario offline";
        Label labelInfo = new Label(infoExtra, skin);
        labelInfo.setColor(Color.LIGHT_GRAY);
        filaAmigo.add(labelInfo).width(200).left().padLeft(20);

        Button btnVer = new Button(skin);
        btnVer.add(new Label("Ver", skin));
        btnVer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    mostrarInfoAmigo(nombreAmigo);
                }
            }
        });

        Button btnQuitar = new Button(skin);
        btnQuitar.add(new Label("Quitar", skin));
        btnQuitar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    confirmarQuitarAmigo(nombreAmigo);
                }
            }
        });

        filaAmigo.add(btnVer).size(60, 30).padLeft(10);
        filaAmigo.add(btnQuitar).size(60, 30).padLeft(5);

        tablaAmigos.add(filaAmigo).width(580).padBottom(5).row();
    }

    private void buscarUsuario() {
        String nombreBuscar = campoBusqueda.getText().trim();

        if (nombreBuscar.isEmpty()) {
            mostrarMensaje("Escribe un nombre de usuario", Color.YELLOW);
            return;
        }

        if (!SistemaUsuarios.validarNombreUsuario(nombreBuscar)) {
            mostrarMensaje("Nombre de usuario invalido", Color.RED);
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
            mostrarMensaje("Usuario no encontrado: " + nombreBuscar, Color.RED);
            return;
        }

        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        if (usuarioActual.getListaAmigos().contains(nombreBuscar)) {
            mostrarMensaje("Ya es tu amigo: " + nombreBuscar, Color.YELLOW);
            return;
        }

        if (nombreBuscar.equals(usuarioActual.getNombreUsuario())) {
            mostrarMensaje("No puedes agregarte a ti mismo", Color.YELLOW);
            return;
        }

        confirmarAgregarAmigo(nombreBuscar);
    }

    private void confirmarAgregarAmigo(String nombreAmigo) {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);

        Label titulo = new Label("AGREGAR AMIGO", skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label("Quieres agregar a " + nombreAmigo + "\ncomo tu amigo?", skin);
        mensaje.setColor(Color.WHITE);
        panel.add(mensaje).padBottom(20).row();

        Table tablaBotones = new Table();

        Button btnSi = new Button(skin);
        btnSi.add(new Label("Si", skin));
        btnSi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                agregarAmigo(nombreAmigo);
                puedeInteractuar = true;
            }
        });

        Button btnNo = new Button(skin);
        btnNo.add(new Label("Cancelar", skin));
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

    private void agregarAmigo(String nombreAmigo) {
        boolean exito = sistemaUsuarios.agregarAmigo(nombreAmigo);

        if (exito) {
            mostrarMensaje("Amigo agregado: " + nombreAmigo, Color.GREEN);
            actualizarListaAmigos();
            campoBusqueda.setText("");
        } else {
            mostrarMensaje("Error al agregar amigo", Color.RED);
        }
    }

    private void confirmarQuitarAmigo(String nombreAmigo) {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);

        Label titulo = new Label("QUITAR AMIGO", skin);
        titulo.setColor(Color.CYAN);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label("Seguro que quieres quitar a\n" + nombreAmigo + " de tus amigos?", skin);
        mensaje.setColor(Color.WHITE);
        panel.add(mensaje).padBottom(20).row();

        Table tablaBotones = new Table();

        Button btnSi = new Button(skin);
        btnSi.add(new Label("Quitar", skin));
        btnSi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                quitarAmigo(nombreAmigo);
                puedeInteractuar = true;
            }
        });

        Button btnNo = new Button(skin);
        btnNo.add(new Label("Cancelar", skin));
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

    private void quitarAmigo(String nombreAmigo) {
        Usuario usuarioActual = sistemaUsuarios.getUsuarioActual();
        boolean exito = usuarioActual.getListaAmigos().remove(nombreAmigo);

        if (exito) {
            sistemaUsuarios.guardarProgreso();
            mostrarMensaje("Amigo quitado: " + nombreAmigo, Color.GREEN);
            actualizarListaAmigos();
        } else {
            mostrarMensaje("Error al quitar amigo", Color.RED);
        }
    }

    private void mostrarInfoAmigo(String nombreAmigo) {
        // por ahora mostrar info basica, despues se puede expandir
        mostrarMensaje("Info de " + nombreAmigo + " (proximamente mas detalles)", Color.CYAN);
    }

    private void mostrarMensaje(String texto, Color color) {
        // crear mensaje temporal que se muestre por unos segundos
        Label mensajeTemporal = new Label(texto, skin);
        mensajeTemporal.setColor(color);
        mensajeTemporal.setPosition(10, Gdx.graphics.getHeight() - 50);
        stage.addActor(mensajeTemporal);

        // quitar el mensaje despues de 3 segundos
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

        // dibujar fondo igual que MenuScreen
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
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
        if (texturaExtra != null) {
            texturaExtra.dispose();
        }
        if (texturaExtra2 != null) {
            texturaExtra2.dispose();
        }
        if (texturaVolver != null) {
            texturaVolver.dispose();
        }
        if (texturaVolver2 != null) {
            texturaVolver2.dispose();
        }
    }
}
