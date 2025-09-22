package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;
import com.sokoban.com.SistemaUsuarios;

public class PantallaLogin implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private Texture texturaJugar, texturaJugar2, texturaVolver, texturaVolver2;

    private TextField campoUsuario;
    private TextField campoContrasena;
    private TextField campoNombreCompleto;

    private boolean modoRegistro = false;
    private Label labelMensaje;

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
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-vaV7.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        
        // Fuente mas grande para mejor legibilidad
        parameter.size = 14;
        BitmapFont mediumFont = generator.generateFont(parameter);
        
        // Fuente mas pequena para labels
        parameter.size = 10;
        BitmapFont smallFont = generator.generateFont(parameter);
        
        generator.dispose();
        skin.add("medium-font", mediumFont, BitmapFont.class);
        skin.add("small-font", smallFont, BitmapFont.class);

        texturaJugar = new Texture(Gdx.files.internal("fondoNormal.png"));
        texturaJugar2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoJugar = new TextureRegionDrawable(new TextureRegion(texturaJugar));
        Drawable fondoJugar2 = new TextureRegionDrawable(new TextureRegion(texturaJugar2));

        texturaVolver = new Texture(Gdx.files.internal("fondoNormal.png"));
        texturaVolver2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoVolver = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        Drawable fondoVolver2 = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

        Button.ButtonStyle estiloBoton = new Button.ButtonStyle();
        estiloBoton.up = fondoJugar;
        estiloBoton.down = fondoJugar2;

        Button.ButtonStyle estiloBotonVolver = new Button.ButtonStyle();
        estiloBotonVolver.up = fondoVolver;
        estiloBotonVolver.down = fondoVolver2;

        TextButton.TextButtonStyle estiloBotonTexto = new TextButton.TextButtonStyle();
        estiloBotonTexto.up = fondoJugar;
        estiloBotonTexto.down = fondoJugar2;
        estiloBotonTexto.font = skin.getFont("medium-font");
        estiloBotonTexto.fontColor = Color.WHITE;

        Label.LabelStyle estiloLabel = new Label.LabelStyle();
        estiloLabel.font = skin.getFont("small-font");
        
        Label.LabelStyle estiloTitulo = new Label.LabelStyle();
        estiloTitulo.font = skin.getFont("medium-font");

        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();

        // Panel mas espacioso
        Table panelLogin = new Table(skin);
        panelLogin.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.7f)));
        panelLogin.pad(40); // MAS PADDING

        Label titulo = new Label(Idiomas.getInstance().obtenerTexto("login.titulo_login"), estiloTitulo);
        titulo.setColor(Color.CYAN);
        titulo.setName("titulo");
        panelLogin.add(titulo).padBottom(30).row(); // MAS ESPACIO

        Label labelUsuario = new Label(Idiomas.getInstance().obtenerTexto("login.usuario"), estiloLabel);
        labelUsuario.setName("labelUsuario");
        campoUsuario = new TextField("", skin);
        campoUsuario.setMessageText(Idiomas.getInstance().obtenerTexto("login.usuario"));
        panelLogin.add(labelUsuario).left().padBottom(8).row(); // MAS ESPACIO
        panelLogin.add(campoUsuario).width(300).height(40).padBottom(20).row(); // CAMPO MAS GRANDE

        Label labelContrasena = new Label(Idiomas.getInstance().obtenerTexto("login.contrasena"), estiloLabel);
        labelContrasena.setName("labelContrasena");
        campoContrasena = new TextField("", skin);
        campoContrasena.setPasswordMode(true);
        campoContrasena.setPasswordCharacter('*');
        campoContrasena.setMessageText(Idiomas.getInstance().obtenerTexto("login.contrasena"));
        panelLogin.add(labelContrasena).left().padBottom(8).row();
        panelLogin.add(campoContrasena).width(300).height(40).padBottom(20).row(); // CAMPO MAS GRANDE

        Label labelNombreCompleto = new Label(Idiomas.getInstance().obtenerTexto("login.nombre_completo"), estiloLabel);
        labelNombreCompleto.setName("labelNombreCompleto");
        campoNombreCompleto = new TextField("", skin);
        campoNombreCompleto.setMessageText(Idiomas.getInstance().obtenerTexto("login.nombre_completo"));
        campoNombreCompleto.setVisible(false);
        labelNombreCompleto.setVisible(false);
        panelLogin.add(labelNombreCompleto).left().padBottom(8).row();
        panelLogin.add(campoNombreCompleto).width(300).height(40).padBottom(25).row(); // CAMPO MAS GRANDE

        Button btnAccion = new Button(estiloBotonTexto);
        btnAccion.setName("btnAccion");
        Button btnCambiarModo = new Button(estiloBotonTexto);
        btnCambiarModo.setName("btnCambiarModo");
        Button btnVolver = new Button(estiloBotonTexto);
        btnVolver.setName("btnVolver");

        campoContrasena.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if (modoRegistro) {
                        registrarUsuario();
                    } else {
                        iniciarSesion();
                    }
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

        btnAccion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (modoRegistro) {
                    registrarUsuario();
                } else {
                    iniciarSesion();
                }
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

        btnAccion.add(new Label(Idiomas.getInstance().obtenerTexto("login.titulo_login"), estiloLabel));
        btnCambiarModo.add(new Label(Idiomas.getInstance().obtenerTexto("login.registrarse"), estiloLabel));
        btnVolver.add(new Label(Idiomas.getInstance().obtenerTexto("login.volver"), estiloLabel));

        panelLogin.add(btnAccion).size(220, 55).padBottom(15).row(); // BOTONES MAS GRANDES
        panelLogin.add(btnCambiarModo).size(450, 55).padBottom(15).row();
        panelLogin.add(btnVolver).size(220, 55).padBottom(25).row();

        // Mensaje con wrap y mas espacio
        labelMensaje = new Label("", skin);
        labelMensaje.setWrap(true);
        labelMensaje.setColor(Color.WHITE);
        panelLogin.add(labelMensaje).width(420).padBottom(10).row(); // MAS ANCHO

        tablaPrincipal.add(panelLogin);
        stage.addActor(tablaPrincipal);
    }

    private void cambiarModo() {
        Label.LabelStyle estiloLabel = new Label.LabelStyle();
        estiloLabel.font = skin.getFont("small-font");
        modoRegistro = !modoRegistro;

        Label titulo = stage.getRoot().findActor("titulo");
        Label labelNombreCompleto = stage.getRoot().findActor("labelNombreCompleto");
        Button btnCambiarModo = stage.getRoot().findActor("btnCambiarModo");

        if (modoRegistro) {
            if (titulo != null) {
                titulo.setText("REGISTRARSE");
            }
            if (labelNombreCompleto != null) {
                labelNombreCompleto.setVisible(true);
            }
            campoNombreCompleto.setVisible(true);

            btnCambiarModo.clearChildren();
            btnCambiarModo.add(new Label("Ya tienes cuenta? Inicia sesion", estiloLabel));
        } else {
            if (titulo != null) {
                titulo.setText("INICIAR SESION");
            }
            if (labelNombreCompleto != null) {
                labelNombreCompleto.setVisible(false);
            }
            campoNombreCompleto.setVisible(false);

            btnCambiarModo.clearChildren();
            btnCambiarModo.add(new Label("No tienes cuenta? Registrate", estiloLabel));
        }

        limpiarCampos();
        labelMensaje.setText("");
    }

    private boolean validarContrasena(String contrasena) {
        if (contrasena.length() < 8) {
            return false;
        }

        boolean tieneMayuscula = false;
        boolean tieneEspecial = false;
        String caracteresEspeciales = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            }
            if (caracteresEspeciales.indexOf(c) >= 0) {
                tieneEspecial = true;
            }
        }

        return tieneMayuscula && tieneEspecial;
    }

    private void iniciarSesion() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = campoContrasena.getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Completa todos los campos", Color.RED);
            return;
        }

        SistemaUsuarios.ResultadoLogin resultado = sistemaUsuarios.iniciarSesionConInfo(usuario, contrasena);

        if (resultado.exitoso) {
            mostrarMensaje("Bienvenido " + usuario + "!", Color.GREEN);

            if (resultado.esPrimeraSesion) {
                stage.addAction(Actions.sequence(
                        Actions.delay(1f),
                        Actions.run(() -> {
                            System.out.println("Usuario nuevo detectado - deberia ir al tutorial");
                            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                        })
                ));
            } else {
                stage.addAction(Actions.sequence(
                        Actions.delay(1f),
                        Actions.run(() -> ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen()))
                ));
            }
        } else {
            mostrarMensaje(resultado.mensaje, Color.RED);
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

        if (!validarContrasena(contrasena)) {
            mostrarMensaje("Contrasena debe tener: min 8 caracteres, 1 mayuscula y 1 simbolo especial", Color.RED);
            return;
        }

        if (sistemaUsuarios.registrarUsuario(usuario, contrasena, nombreCompleto)) {
            mostrarMensaje("Usuario registrado! Ahora puedes iniciar sesion", Color.GREEN);

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
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (bg != null) bg.dispose();
        if (texturaJugar != null) texturaJugar.dispose();
        if (texturaJugar2 != null) texturaJugar2.dispose();
        if (texturaVolver != null) texturaVolver.dispose();
        if (texturaVolver2 != null) texturaVolver2.dispose();
    }

    public void actualizarTextos() {
        Label titulo = stage.getRoot().findActor("titulo");
        Label labelUsuario = stage.getRoot().findActor("labelUsuario");
        Label labelContrasena = stage.getRoot().findActor("labelContrasena");
        Label labelNombreCompleto = stage.getRoot().findActor("labelNombreCompleto");

        titulo.setText(Idiomas.getInstance().obtenerTexto("login.titulo_login"));
        labelUsuario.setText(Idiomas.getInstance().obtenerTexto("login.usuario"));
        labelContrasena.setText(Idiomas.getInstance().obtenerTexto("login.contrasena"));
        labelNombreCompleto.setText(Idiomas.getInstance().obtenerTexto("login.nombre_completo"));
        campoUsuario.setMessageText(Idiomas.getInstance().obtenerTexto("login.usuario"));
        campoContrasena.setMessageText(Idiomas.getInstance().obtenerTexto("login.contrasena"));
        campoNombreCompleto.setMessageText(Idiomas.getInstance().obtenerTexto("login.nombre_completo"));

        Button btnAccion = stage.getRoot().findActor("btnAccion");
        btnAccion.clearChildren();
        btnAccion.add(new Label(Idiomas.getInstance().obtenerTexto("login.titulo_login"), skin));

        Button btnCambiarModo = stage.getRoot().findActor("btnCambiarModo");
        btnCambiarModo.clearChildren();
        btnCambiarModo.add(new Label(Idiomas.getInstance().obtenerTexto("login.registrarse"), skin));

        Button btnVolver = stage.getRoot().findActor("btnVolver");
        btnVolver.clearChildren();
        btnVolver.add(new Label(Idiomas.getInstance().obtenerTexto("login.volver"), skin));
    }
}