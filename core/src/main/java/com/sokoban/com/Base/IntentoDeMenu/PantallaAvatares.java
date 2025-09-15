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

public class PantallaAvatares implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private boolean puedeInteractuar = true;
    private SistemaUsuarios sistemaUsuarios;

    // texturas de botones
    private Texture texturaGuardar, texturaGuardar2, texturaVolver, texturaVolver2;
    private Button.ButtonStyle estiloGuardar, estiloVolver;

    // avatares disponibles
    private String[] avatares = {
        "personaje.png", "caja.png", "objetivo.png","srek.png"
    };
    //Pongaos solo 9 avatares , pero que sean "unicos"
    //Talvez hacer un avatar del munequito ese
    private String[] nombresAvatares = {
        "Personaje Clasico", "Caja Misteriosa", "Portal Objetivo",
         "Mike Sullivan"
    };

    private String avatarSeleccionado;
    private Image imagenPrevia;

    public PantallaAvatares() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
        Usuario usuario = sistemaUsuarios.getUsuarioActual();
        avatarSeleccionado = usuario != null ? usuario.getRutaAvatar() : avatares[0];

        boolean encontrado = false;
        for (String avatar : avatares) {
            if (avatarSeleccionado.equals(avatar) || avatarSeleccionado.contains(avatar)) {
                avatarSeleccionado = avatar;
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            avatarSeleccionado = avatares[0];
        }
    }

    @Override
    public void show() {
        // cargar texturas de botones
        texturaGuardar = new Texture(Gdx.files.internal("guardar.png"));
        texturaGuardar2 = new Texture(Gdx.files.internal("guardar2.png"));
        estiloGuardar = new Button.ButtonStyle();
        estiloGuardar.up = new TextureRegionDrawable(new TextureRegion(texturaGuardar));
        estiloGuardar.down = new TextureRegionDrawable(new TextureRegion(texturaGuardar2));

        texturaVolver = new Texture(Gdx.files.internal("volver.png"));
        texturaVolver2 = new Texture(Gdx.files.internal("volver2.png"));
        estiloVolver = new Button.ButtonStyle();
        estiloVolver.up = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        estiloVolver.down = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

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

        Label titulo = new Label("SELECCIONAR AVATAR", skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        Table tablaContenido = new Table();

        Table panelPreview = new Table(skin);
        panelPreview.setBackground(skin.newDrawable("default-round", new Color(0.2f, 0.2f, 0.2f, 0.8f)));
        panelPreview.pad(20);

        Label labelPreview = new Label("VISTA PREVIA", skin);
        labelPreview.setColor(Color.YELLOW);
        panelPreview.add(labelPreview).padBottom(15).row();

        Texture texturaAvatar = new Texture(Gdx.files.internal(avatarSeleccionado));
        imagenPrevia = new Image(texturaAvatar);
        imagenPrevia.setSize(80, 80);
        panelPreview.add(imagenPrevia).size(80, 80).padBottom(15).row();

        Usuario usuario = sistemaUsuarios.getUsuarioActual();
        Label labelUsuario = new Label("Usuario: " + (usuario != null ? usuario.getNombreUsuario() : "N/A"), skin);
        labelUsuario.setColor(Color.WHITE);
        panelPreview.add(labelUsuario).padBottom(5).row();

        Label labelAvatarActual = new Label("Avatar: " + obtenerNombreAvatar(avatarSeleccionado), skin);
        labelAvatarActual.setColor(Color.LIGHT_GRAY);
        panelPreview.add(labelAvatarActual).row();

        tablaContenido.add(panelPreview).width(200).height(250).padRight(30);

        Table panelAvatares = new Table(skin);
        panelAvatares.setBackground(skin.newDrawable("default-round", new Color(0.2f, 0.2f, 0.2f, 0.8f)));
        panelAvatares.pad(20);

        Label labelAvatares = new Label("AVATARES DISPONIBLES", skin);
        labelAvatares.setColor(Color.YELLOW);
        panelAvatares.add(labelAvatares).padBottom(15).colspan(3).row();

        int columnas = 3;
        for (int i = 0; i < avatares.length; i++) {
            String avatar = avatares[i];
            try {
                Texture texturaBtn = new Texture(Gdx.files.internal(avatar));
                Button btnAvatar = new Button(new TextureRegionDrawable(new TextureRegion(texturaBtn)));

                if (avatar.equals(avatarSeleccionado)) {
                    btnAvatar.setColor(Color.CYAN);
                } else {
                    btnAvatar.setColor(Color.WHITE);
                }

                btnAvatar.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (puedeInteractuar) {
                            seleccionarAvatar(avatar);
                        }
                    }
                });

                panelAvatares.add(btnAvatar).size(50, 50).pad(5);
                if ((i + 1) % columnas == 0) {
                    panelAvatares.row();
                }

            } catch (Exception e) {
                System.out.println("No se pudo cargar avatar: " + avatar);
            }
        }

        tablaContenido.add(panelAvatares).width(250).height(250);
        tablaPrincipal.add(tablaContenido).padBottom(30).row();

        // botones de accion con texturas
        Table tablaBotones = new Table();

        Button btnGuardar = new Button(estiloGuardar);
        btnGuardar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    guardarAvatar();
                }
            }
        });

        Button btnCancelar = new Button(estiloVolver);
        btnCancelar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                }
            }
        });

        tablaBotones.add(btnGuardar).size(120, 50).padRight(20);
        tablaBotones.add(btnCancelar).size(120, 50);
        tablaPrincipal.add(tablaBotones).row();

        stage.addActor(tablaPrincipal);
    }

    private void seleccionarAvatar(String nuevoAvatar) {
        avatarSeleccionado = nuevoAvatar;
        try {
            Texture nuevaTextura = new Texture(Gdx.files.internal(nuevoAvatar));
            imagenPrevia.setDrawable(new TextureRegionDrawable(new TextureRegion(nuevaTextura)));
        } catch (Exception e) {
            System.out.println("Error actualizando preview: " + e.getMessage());
        }
        recrearGrilla();
    }

    private void recrearGrilla() {
        stage.clear();
        crearInterfaz();
    }

    private void guardarAvatar() {
        if (!sistemaUsuarios.haySesionActiva()) {
            return;
        }

        Usuario usuario = sistemaUsuarios.getUsuarioActual();
        String avatarAnterior = usuario.getRutaAvatar();
        usuario.setRutaAvatar(avatarSeleccionado);
        boolean exito = sistemaUsuarios.guardarProgreso();

        if (!exito) {
            usuario.setRutaAvatar(avatarAnterior);
        } else {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    Gdx.app.postRunnable(() -> ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private String obtenerNombreAvatar(String rutaAvatar) {
        for (int i = 0; i < avatares.length; i++) {
            if (avatares[i].equals(rutaAvatar)) {
                return nombresAvatares[i];
            }
        }
        return "Avatar Personalizado";
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
        if (texturaGuardar != null) {
            texturaGuardar.dispose();
        }
        if (texturaGuardar2 != null) {
            texturaGuardar2.dispose();
        }
        if (texturaVolver != null) {
            texturaVolver.dispose();
        }
        if (texturaVolver2 != null) {
            texturaVolver2.dispose();
        }
    }
}
