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
    private Texture texturaJugar, texturaJugar2,texturaVolver,texturaVolver2;

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
    // === TEXTURAS LOGIN ===
    Texture texLogin = new Texture(Gdx.files.internal("login.png"));
    Texture texLoginPressed = new Texture(Gdx.files.internal("login2.png"));
    Button.ButtonStyle estiloLogin = new Button.ButtonStyle();
    estiloLogin.up = new TextureRegionDrawable(new TextureRegion(texLogin));
    estiloLogin.down = new TextureRegionDrawable(new TextureRegion(texLoginPressed));

    // === TEXTURAS REGISTER ===
    Texture texRegister = new Texture(Gdx.files.internal("register.png"));
    Texture texRegisterPressed = new Texture(Gdx.files.internal("register2.png"));
    Button.ButtonStyle estiloRegister = new Button.ButtonStyle();
    estiloRegister.up = new TextureRegionDrawable(new TextureRegion(texRegister));
    estiloRegister.down = new TextureRegionDrawable(new TextureRegion(texRegisterPressed));

    // === TEXTURAS CAMBIAR MODO ===
    Texture texModo = new Texture(Gdx.files.internal("modo.png"));
    Texture texModoPressed = new Texture(Gdx.files.internal("modo2.png"));
    Button.ButtonStyle estiloModo = new Button.ButtonStyle();
    estiloModo.up = new TextureRegionDrawable(new TextureRegion(texModo));
    estiloModo.down = new TextureRegionDrawable(new TextureRegion(texModoPressed));

    // === TEXTURAS VOLVER ===
    Texture texVolver = new Texture(Gdx.files.internal("volver.png"));
    Texture texVolverPressed = new Texture(Gdx.files.internal("volver2.png"));
    Button.ButtonStyle estiloVolver = new Button.ButtonStyle();
    estiloVolver.up = new TextureRegionDrawable(new TextureRegion(texVolver));
    estiloVolver.down = new TextureRegionDrawable(new TextureRegion(texVolverPressed));

    // === TABLA PRINCIPAL ===
    Table tablaPrincipal = new Table();
    tablaPrincipal.setFillParent(true);
    tablaPrincipal.center();

    // === PANEL LOGIN/REGISTRO ===
    Table panelLogin = new Table(skin);
    panelLogin.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.7f)));
    panelLogin.pad(30);

    // Título dinámico
    Label titulo = new Label("INICIAR SESION", skin);
    titulo.setColor(Color.CYAN);
    titulo.setName("titulo");
    panelLogin.add(titulo).padBottom(20).row();

    // Usuario
    Label labelUsuario = new Label("Usuario:", skin);
    campoUsuario = new TextField("", skin);
    campoUsuario.setMessageText("Ingresa tu usuario");
    panelLogin.add(labelUsuario).left().padBottom(5).row();
    panelLogin.add(campoUsuario).width(250).padBottom(15).row();

    // Contraseña
    Label labelContrasena = new Label("Contrasena:", skin);
    campoContrasena = new TextField("", skin);
    campoContrasena.setPasswordMode(true);
    campoContrasena.setPasswordCharacter('*');
    campoContrasena.setMessageText("Ingresa tu contrasena");
    panelLogin.add(labelContrasena).left().padBottom(5).row();
    panelLogin.add(campoContrasena).width(250).padBottom(15).row();

    // Nombre completo (solo visible en registro)
    Label labelNombreCompleto = new Label("Nombre completo:", skin);
    labelNombreCompleto.setName("labelNombreCompleto");
    campoNombreCompleto = new TextField("", skin);
    campoNombreCompleto.setMessageText("Tu nombre completo");
    campoNombreCompleto.setVisible(false);
    labelNombreCompleto.setVisible(false);
    panelLogin.add(labelNombreCompleto).left().padBottom(5).row();
    panelLogin.add(campoNombreCompleto).width(250).padBottom(15).row();

    // === BOTONES ===
    Button btnAccion = new Button(estiloLogin); // Por defecto Login
    Button btnCambiarModo = new Button(estiloModo);
    btnCambiarModo.setName("btnCambiarModo");
    Button btnVolver = new Button(estiloVolver);

    // Listener: Acción principal
    btnAccion.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (modoRegistro) registrarUsuario();
            else iniciarSesion();
        }
    });

    // Listener: Cambiar entre Login/Registro
    btnCambiarModo.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            cambiarModo();
            if (modoRegistro) {
                btnAccion.setStyle(estiloRegister);
            } else {
                btnAccion.setStyle(estiloLogin);
            }
        }
    });

    // Listener: Volver al menú
    btnVolver.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
        }
    });

    // === AGREGAR BOTONES Y MENSAJE ===
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
            if (titulo != null) titulo.setText("INICIAR SESION");
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
            mostrarMensaje("Usuario o contrasena incorrectos", Color.RED);
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
            mostrarMensaje("Usuario invalido (3-20 chars, solo letras/numeros)", Color.RED);
            return;
        }

        if (contrasena.length() < 4) {
            mostrarMensaje("La contrasena debe tener al menos 4 caracteres", Color.RED);
            return;
        }

        if (sistemaUsuarios.registrarUsuario(usuario, contrasena, nombreCompleto)) {
            mostrarMensaje("¡Usuario registrado! Ahora puedes iniciar sesion", Color.GREEN);

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
        stage.dispose();
        skin.dispose();
        bg.dispose();
        texturaJugar.dispose();
        texturaJugar2.dispose();
    }
}
