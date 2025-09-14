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
    private Texture texturaExtra, texturaExtra2, texturaVolver, texturaVolver2;
    private Button.ButtonStyle estiloExtra, estiloVolver;

    // avatares disponibles usando texturas existentes
    private String[] avatares = {
        "personaje.png",    // avatar por defecto
        "caja.png",         // caja como avatar,xq why not 
        "objetivo.png",     // objetivo como avatar, amo los avatars randoms, con tematica del mismo juego ok.
        "tp1.png",          // portales como avatares, tambien los portales por aja
        "tp2.png",
        "tp3.png",
        "tp4.png",
        "tp5.png",
        "tp6.png",
        "tp7.png"
    };

    private String[] nombresAvatares = {
        "Personaje Clasico", //podes cambiar eso si queres, 0 creatvidad la mia actually
        "Caja Misteriosa", 
        "Portal Objetivo",
        "Portal Nivel 1",
        "Portal Nivel 2", 
        "Portal Nivel 3",
        "Portal Nivel 4",
        "Portal Nivel 5",
        "Portal Nivel 6",
        "Portal Nivel 7"
    };

    private String avatarSeleccionado;
    private Image imagenPrevia;

    public PantallaAvatares() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
        // cargar avatar actual del usuario
        Usuario usuario = sistemaUsuarios.getUsuarioActual();
        avatarSeleccionado = usuario != null ? usuario.getRutaAvatar() : avatares[0];
        
        // si el avatar actual no esta en la lista, usar el default
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
        // setup de texturas igual que otras pantallas
        texturaExtra = new Texture(Gdx.files.internal("extra.png"));
        Drawable fondoExtra = new TextureRegionDrawable(new TextureRegion(texturaExtra));
        texturaExtra2 = new Texture(Gdx.files.internal("extra2.png"));
        Drawable fondoExtra2 = new TextureRegionDrawable(new TextureRegion(texturaExtra2));

        estiloExtra = new Button.ButtonStyle();
        estiloExtra.up = fondoExtra;
        estiloExtra.down = fondoExtra2;

        texturaVolver = new Texture(Gdx.files.internal("volver.png"));
        Drawable fondoVolver = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        texturaVolver2 = new Texture(Gdx.files.internal("volver2.png"));
        Drawable fondoVolver2 = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

        estiloVolver = new Button.ButtonStyle();
        estiloVolver.up = fondoVolver;
        estiloVolver.down = fondoVolver2;

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
        Label titulo = new Label("SELECCIONAR AVATAR", skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // seccion de preview y seleccion
        Table tablaContenido = new Table();

        // panel izquierdo - preview
        Table panelPreview = new Table(skin);
        panelPreview.setBackground(skin.newDrawable("default-round", new Color(0.2f, 0.2f, 0.2f, 0.8f)));
        panelPreview.pad(20);

        Label labelPreview = new Label("VISTA PREVIA", skin);
        labelPreview.setColor(Color.YELLOW);
        panelPreview.add(labelPreview).padBottom(15).row();

        // imagen de preview
        Texture texturaAvatar = new Texture(Gdx.files.internal(avatarSeleccionado));
        imagenPrevia = new Image(texturaAvatar);
        imagenPrevia.setSize(80, 80);
        panelPreview.add(imagenPrevia).size(80, 80).padBottom(15).row();

        // info del usuario
        Usuario usuario = sistemaUsuarios.getUsuarioActual();
        Label labelUsuario = new Label("Usuario: " + (usuario != null ? usuario.getNombreUsuario() : "N/A"), skin);
        labelUsuario.setColor(Color.WHITE);
        panelPreview.add(labelUsuario).padBottom(5).row();

        Label labelAvatarActual = new Label("Avatar: " + obtenerNombreAvatar(avatarSeleccionado), skin);
        labelAvatarActual.setColor(Color.LIGHT_GRAY);
        panelPreview.add(labelAvatarActual).row();

        tablaContenido.add(panelPreview).width(200).height(250).padRight(30);

        // panel derecho - grilla de avatares
        Table panelAvatares = new Table(skin);
        panelAvatares.setBackground(skin.newDrawable("default-round", new Color(0.2f, 0.2f, 0.2f, 0.8f)));
        panelAvatares.pad(20);

        Label labelAvatares = new Label("AVATARES DISPONIBLES", skin);
        labelAvatares.setColor(Color.YELLOW);
        panelAvatares.add(labelAvatares).padBottom(15).colspan(3).row();

        // crear grilla de avatares 3x4 aprox
        int columnas = 3;
        for (int i = 0; i < avatares.length; i++) {
            String avatar = avatares[i];
            
            try {
                Texture texturaBtn = new Texture(Gdx.files.internal(avatar));
                Button btnAvatar = new Button(new TextureRegionDrawable(new TextureRegion(texturaBtn)));
                
                // resaltar el avatar seleccionado
                if (avatar.equals(avatarSeleccionado)) {
                    // crear un borde o cambiar el tint
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
                
                // nueva fila cada 3 elementos
                if ((i + 1) % columnas == 0) {
                    panelAvatares.row();
                }
                
            } catch (Exception e) {
                System.out.println("No se pudo cargar avatar: " + avatar);
                // continuar con el siguiente avatar
            }
        }

        tablaContenido.add(panelAvatares).width(250).height(250);
        tablaPrincipal.add(tablaContenido).padBottom(30).row();

        // botones de accion
        Table tablaBotones = new Table();

        Button btnGuardar = new Button(estiloExtra);
        btnGuardar.add(new Label("Guardar", skin));
        btnGuardar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    guardarAvatar();
                }
            }
        });

        Button btnCancelar = new Button(estiloVolver);
        btnCancelar.add(new Label("Cancelar", skin));
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
        
        // actualizar preview
        try {
            Texture nuevaTextura = new Texture(Gdx.files.internal(nuevoAvatar));
            imagenPrevia.setDrawable(new TextureRegionDrawable(new TextureRegion(nuevaTextura)));
        } catch (Exception e) {
            System.out.println("Error actualizando preview: " + e.getMessage());
        }

        // actualizar colores de los botones
        recrearGrilla();
        mostrarMensaje("Avatar seleccionado: " + obtenerNombreAvatar(nuevoAvatar), Color.GREEN);
    }

    private void recrearGrilla() {
        // esta es una version simplificada, en una implementacion real
        // seria mejor mantener referencias a los botones
        stage.clear();
        crearInterfaz();
    }

    private void guardarAvatar() {
        if (!sistemaUsuarios.haySesionActiva()) {
            mostrarMensaje("No hay sesion activa", Color.RED);
            return;
        }

        Usuario usuario = sistemaUsuarios.getUsuarioActual();
        String avatarAnterior = usuario.getRutaAvatar();
        
        usuario.setRutaAvatar(avatarSeleccionado);
        boolean exito = sistemaUsuarios.guardarProgreso();

        if (exito) {
            mostrarMensaje("Avatar guardado correctamente!", Color.GREEN);
            
            // delay y volver al menu
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    Gdx.app.postRunnable(() -> 
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen())
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
        } else {
            mostrarMensaje("Error al guardar avatar", Color.RED);
            usuario.setRutaAvatar(avatarAnterior); // revertir cambio
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
        if (texturaExtra != null) texturaExtra.dispose();
        if (texturaExtra2 != null) texturaExtra2.dispose();
        if (texturaVolver != null) texturaVolver.dispose();
        if (texturaVolver2 != null) texturaVolver2.dispose();
    }
}