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
    
    // Campos de input
    private TextField campoUsuario;
    private TextField campoContrasena;
    private TextField campoNombreCompleto; // Solo para registro
    
    // Estados
    private boolean modoRegistro = false;
    private Label labelMensaje;
    
    // Referencias al sistema
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
        Texture texturaJugar = new Texture(Gdx.files.internal("jugar.png"));
        Drawable fondoJugar = new TextureRegionDrawable(new TextureRegion(texturaJugar));
        Texture texturaJugar2 = new Texture(Gdx.files.internal("jugar2.png"));
        Drawable fondoJugar2 = new TextureRegionDrawable(new TextureRegion(texturaJugar2));
        
        Button.ButtonStyle estiloBoton = new Button.ButtonStyle();
        estiloBoton.up = fondoJugar;
        estiloBoton.down = fondoJugar2;
        
        // Panel principal
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();
        
        // Panel de login
        Table panelLogin = new Table(skin);
        panelLogin.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.7f)));
        panelLogin.pad(30);
        
        // Título
        Label titulo = new Label("INICIAR SESIÓN", skin);
        titulo.setColor(Color.CYAN);
        panelLogin.add(titulo).padBottom(20).row();
        
        // Campo usuario
        Label labelUsuario = new Label("Usuario:", skin);
        campoUsuario = new TextField("", skin);
        campoUsuario.setMessageText("Ingresa tu usuario");
        panelLogin.add(labelUsuario).left().padBottom(5).row();
        panelLogin.add(campoUsuario).width(250).padBottom(15).row();
        
        // Campo contraseña
        Label labelContrasena = new Label("Contraseña:", skin);
        campoContrasena = new TextField("", skin);
        campoContrasena.setPasswordMode(true);
        campoContrasena.setPasswordCharacter('*');
        campoContrasena.setMessageText("Ingresa tu contraseña");
        panelLogin.add(labelContrasena).left().padBottom(5).row();
        panelLogin.add(campoContrasena).width(250).padBottom(15).row();
        
        // Campo nombre completo (solo para registro)
        Label labelNombreCompleto = new Label("Nombre completo:", skin);
        campoNombreCompleto = new TextField("", skin);
        campoNombreCompleto.setMessageText("Tu nombre completo");
        campoNombreCompleto.setVisible(false);
        labelNombreCompleto.setVisible(false);
        panelLogin.add(labelNombreCompleto).left().padBottom(5).row();
        panelLogin.add(campoNombreCompleto).width(250).padBottom(15).row();
        
        // Botones
        Button btnAccion = new Button(estiloBoton);
        Button btnCambiarModo = new Button(estiloBoton);
        Button btnVolver = new Button(estiloBoton);
        
        // Listeners para campos de texto (Enter para enviar)
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
        
        // Acción del botón principal
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
        
        // Cambiar entre login y registro
        btnCambiarModo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarModo();
            }
        });
        
        // Volver al menú
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
            }
        });
        
        panelLogin.add(btnAccion).size(200, 50).padBottom(10).row();
        panelLogin.add(btnCambiarModo).size(200, 50).padBottom(10).row();
        panelLogin.add(btnVolver).size(200, 50).padBottom(20).row();
        
        // Mensaje de estado
        labelMensaje = new Label("", skin);
        labelMensaje.setColor(Color.WHITE);
        panelLogin.add(labelMensaje).padBottom(10).row();
        
        tablaPrincipal.add(panelLogin);
        stage.addActor(tablaPrincipal);
        
        // Referencias para actualizar UI
        final Label tituloRef = titulo;
        final Label labelNombreCompletoRef = labelNombreCompleto;
        
        // Método para cambiar modo
        Runnable cambiarModoRunnable = () -> {
            modoRegistro = !modoRegistro;
            
            if (modoRegistro) {
                tituloRef.setText("REGISTRARSE");
                labelNombreCompletoRef.setVisible(true);
                campoNombreCompleto.setVisible(true);
            } else {
                tituloRef.setText("INICIAR SESIÓN");
                labelNombreCompletoRef.setVisible(false);
                campoNombreCompleto.setVisible(false);
            }
            
            limpiarCampos();
            labelMensaje.setText("");
        };
        
        // Guardar referencia al método
        btnCambiarModo.setUserObject(cambiarModoRunnable);
    }
    
    private void cambiarModo() {
        Runnable cambiarModoRunnable = (Runnable) stage.getActors().get(0)
                .findActor("btnCambiarModo") != null ? 
                (Runnable) ((Button) stage.getActors().get(0).findActor("btnCambiarModo")).getUserObject() : 
                null;
        
        // Implementación directa del cambio de modo
        modoRegistro = !modoRegistro;
        
        Label titulo = (Label) stage.getActors().get(0).findActor("titulo");
        if (titulo == null) {
            // Buscar en la estructura
            Table tablaPrincipal = (Table) stage.getActors().get(0);
            Table panelLogin = (Table) tablaPrincipal.getChildren().get(0);
            titulo = (Label) panelLogin.getChildren().get(0);
        }
        
        if (modoRegistro) {
            if (titulo != null) titulo.setText("REGISTRARSE");
            campoNombreCompleto.setVisible(true);
        } else {
            if (titulo != null) titulo.setText("INICIAR SESIÓN");
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
            
            // Ir al menú principal después de 1 segundo
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Gdx.app.postRunnable(() -> {
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
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
            
            // Cambiar automáticamente a modo login
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    Gdx.app.postRunnable(() -> {
                        modoRegistro = false;
                        cambiarModo();
                        campoUsuario.setText(usuario);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
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
    }
}