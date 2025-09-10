package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Juegito;
import com.sokoban.com.SistemaUsuarios;

public class PantallaLogin implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private Texture texturaJugar, texturaJugar2;

    // Campos de input
    private TextField campoUsuario;
    private TextField campoContrasena;
    private TextField campoNombreCompleto;

    // Estados
    private boolean modoRegistro = false;
    private Label labelMensaje;

    // Sistema de usuarios
    private SistemaUsuarios sistemaUsuarios;

    public PantallaLogin() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        bg = new Texture("fondoM.png");

        crearUI();
    }

    private void crearUI() {
        // Texturas para botones
        texturaJugar = new Texture(Gdx.files.internal("jugar.png"));
        texturaJugar2 = new Texture(Gdx.files.internal("jugar2.png"));
        Drawable fondoJugar = new TextureRegionDrawable(new TextureRegion(texturaJugar));
        Drawable fondoJugar2 = new TextureRegionDrawable(new TextureRegion(texturaJugar2));

        Button.ButtonStyle estiloBoton = new Button.ButtonStyle();
        estiloBoton.up = fondoJugar;
        estiloBoton.down = fondoJugar2;

        // Tabla principal
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();

        // Panel login
        Table panelLogin = new Table(skin);
        panelLogin.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.7f)));
        panelLogin.pad(30);

        // Título
        Label titulo = new Label("INICIAR SESIÓN", skin);
        titulo.setColor(Color.CYAN);
        titulo.setName("titulo");
        panelLogin.add(titulo).padBottom(20).row();

        // Campos de texto
        Label labelUsuario = new Label("Usuario:", skin);
        campoUsuario = new TextField("", skin);
        campoUsuario.setMessageText("Ingresa tu usuario");
        panelLogin.add(labelUsuario).left().padBottom(5).row();
        panelLogin.add(campoUsuario).width(250).padBottom(15).row();

        Label labelContrasena = new Label("Contraseña:", skin);
        campoContrasena = new TextField("", skin);
        campoContrasena.setPasswordMode(true);
        campoContrasena.setPasswordCharacter('*');
        campoContrasena.setMessageText("Ingresa tu contraseña");
        panelLogin.add(labelContrasena).left().padBottom(5).row();
        panelLogin.add(campoContrasena).width(250).padBottom(15).row();

        Label labelNombreCompleto = new Label("Nombre completo:", skin);
        labelNombreCompleto.setName("labelNombreCompleto");
        campoNombreCompleto = new TextField("", skin);
        campoNombreCompleto.setMessageText("Tu nombre completo");
        campoNombreCompleto.setVisible(false);
        labelNombreCompleto.setVisible(false);
        panelLogin.add(labelNombreCompleto).left().padBottom(5).row();
        panelLogin.add(campoNombreCompleto).width(250).padBottom(15).row();

        // Botones
        Button btnAccion = new Button(estiloBoton);
        Button btnCambiarModo = new Button(estiloBoton);
        btnCambiarModo.setName("btnCambiarModo");
        Button btnVolver = new Button(estiloBoton);

        // Listeners ENTER
        campoContrasena.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if (modoRegistro) registrarUsuario();
                    else iniciarSesion();
                    return true;
                }
                return false;
            }
        });

        campoNombreCompleto.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER && modoRegistro) {
                    registrarUsuario();
                    return true;
                }
                return false;
            }
        });

        // Botones click
        btnAccion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (modoRegistro) registrarUsuario();
                else iniciarSesion();
            }
        });

        btnCambiarModo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarModo();
            }
        });

        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
            }
        });

        // Agregar botones y labelMensaje
        panelLogin.add(btnAccion).size(200, 50).padBottom(10).row();
        panelLogin.add(btnCambiarModo).size(200, 50).padBottom(10).row();
        panelLogin.add(btnVolver).size(200, 50).padBottom(20).row();

        labelMensaje = new Label("", skin);
        labelMensaje.setColor(Color.WHITE);
        panelLogin.add(labelMensaje).padBottom(10).row();

        tablaPrincipal.add(panelLogin);
        stage.addActor(tablaPrincipal);
    }

    private void cambiarModo() {
        modoRegistro = !modoRegistro;

        Label titulo = stage.getRoot().findActor("titulo");
        Label labelNombreCompleto = stage.getRoot().findActor("labelNombreCompleto");

        if (modoRegistro) {
            if (titulo != null) titulo.setText("REGISTRARSE");
            if (labelNombreCompleto != null) labelNombreCompleto.setVisible(true);
            campoNombreCompleto.setVisible(true);
        } else {
            if (titulo != null) titulo.setText("INICIAR SESIÓN");
            if (labelNombreCompleto != null) labelNombreCompleto.setVisible(false);
            campoNombreCompleto.setVisible(false);
        }

        limpiarCampos();
        labelMensaje.setText("");
    }

    private void iniciarSesion() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = campoContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Completa todos los campos", Color.RED);
            return;
        }

        if (sistemaUsuarios.iniciarSesion(usuario, contrasena)) {
            mostrarMensaje("¡Bienvenido " + usuario + "!", Color.GREEN);

            // Delay usando Actions
            stage.addAction(Actions.sequence(
                    Actions.delay(1f),
                    Actions.run(() -> ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen()))
            ));
        } else {
            mostrarMensaje("Usuario o contraseña incorrectos", Color.RED);
        }
    }

    private void registrarUsuario() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = campoContrasena.getText();
        String nombreCompleto = campoNombreCompleto.getText().trim();

        if (usuario.isEmpty() || contrasena.isEmpty() || nombreCompleto.isEmpty()) {
            mostrarMensaje("Completa todos los campos", Color.RED);
            return;
        }

        if (!SistemaUsuarios.validarNombreUsuario(usuario)) {
            mostrarMensaje("Usuario inválido (3-20 chars, solo letras/números)", Color.RED);
            return;
        }

        if (contrasena.length() < 4) {
            mostrarMensaje("La contraseña debe tener al menos 4 caracteres", Color.RED);
            return;
        }

        if (sistemaUsuarios.registrarUsuario(usuario, contrasena, nombreCompleto)) {
            mostrarMensaje("¡Usuario registrado! Ahora puedes iniciar sesión", Color.GREEN);

            // Cambiar a login después de 2 segundos
            stage.addAction(Actions.sequence(
                    Actions.delay(2f),
                    Actions.run(() -> {
                        modoRegistro = false;
                        cambiarModo();
                        campoUsuario.setText(usuario);
                    })
            ));
        } else {
            mostrarMensaje("Error: El usuario ya existe", Color.RED);
        }
    }

    private void mostrarMensaje(String mensaje, Color color) {
        labelMensaje.setText(mensaje);
        labelMensaje.setColor(color);
    }

    private void limpiarCampos() {
        campoUsuario.setText("");
        campoContrasena.setText("");
        campoNombreCompleto.setText("");
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
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        bg.dispose();
        texturaJugar.dispose();
        texturaJugar2.dispose();
    }
}
