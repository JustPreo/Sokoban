package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.GestorArchivos;
import com.sokoban.com.Juegito;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.Usuario;
import com.sokoban.com.Idiomas;

public class PantallaComparacion implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private boolean puedeInteractuar = true;
    private SistemaUsuarios sistemaUsuarios;
    private GestorArchivos gestorArchivos;
    private ShapeRenderer shapeRenderer;

    // texturas de botones
    private Texture texturaExtra, texturaExtra2, texturaVolver, texturaVolver2;
    private Texture texturaComparar, texturaComparar2;
    private Button.ButtonStyle estiloExtra, estiloVolver, estiloComparar;
    ImageTextButton.ImageTextButtonStyle estiloBtnComparar;

    // datos de comparacion
    private Usuario usuarioActual;
    private Usuario usuarioComparar;
    private String nombreUsuarioComparar;

    // componentes UI
    private SelectBox<String> selectorAmigo;
    private Table tablaComparacion;

    public PantallaComparacion() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
        gestorArchivos = GestorArchivos.getInstancia();
        shapeRenderer = new ShapeRenderer();

        if (sistemaUsuarios.haySesionActiva()) {
            usuarioActual = sistemaUsuarios.getUsuarioActual();
        }
    }

    @Override
    public void show() {
        // setup texturas
        texturaComparar = new Texture(Gdx.files.internal("fondoNormal.png"));
        texturaComparar2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        estiloComparar = new Button.ButtonStyle();
        estiloComparar.up = new TextureRegionDrawable(new TextureRegion(texturaComparar));
        estiloComparar.down = new TextureRegionDrawable(new TextureRegion(texturaComparar2));

        texturaVolver = new Texture(Gdx.files.internal("fondoNormal.png"));
        Drawable fondoVolver = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        texturaVolver2 = new Texture(Gdx.files.internal("fondoNormal.png"));
        Drawable fondoVolver2 = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

        estiloVolver = new Button.ButtonStyle();
        estiloVolver.up = fondoVolver;
        estiloVolver.down = fondoVolver2;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        estiloBtnComparar = new ImageTextButton.ImageTextButtonStyle();
estiloBtnComparar.up = new TextureRegionDrawable(new TextureRegion(texturaComparar));
estiloBtnComparar.down = new TextureRegionDrawable(new TextureRegion(texturaComparar2));
estiloBtnComparar.font = skin.getFont("default-font"); 
        
        bg = new Texture("fondoM.png");

        if (usuarioActual == null) {
            mostrarErrorSesion();
        } else {
            crearInterfaz();
        }
    }

    private void mostrarErrorSesion() {
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label error = new Label(Idiomas.getInstance().obtenerTexto("comparacion.error_sesion"), skin);
        error.setColor(Color.RED);
        tabla.add(error).padBottom(20).row();

        Button btnVolver = new Button(estiloVolver);
        btnVolver.add(new Label(Idiomas.getInstance().obtenerTexto("comparacion.volver"), skin));
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
            }
        });

        tabla.add(btnVolver).size(120, 50);
        stage.addActor(tabla);
    }

    private void crearInterfaz() {
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center().top();
        tablaPrincipal.pad(20);

        // titulo
        Label titulo = new Label(Idiomas.getInstance().obtenerTexto("comparacion.titulo"), skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // selector de usuario para comparar
        Table tablaSelectorUsuario = new Table();

        Label labelSelector = new Label(Idiomas.getInstance().obtenerTexto("comparacion.comparar_con"), skin);
        labelSelector.setColor(Color.WHITE);

        selectorAmigo = new SelectBox<>(skin);
        cargarListaAmigos();

        selectorAmigo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    seleccionarUsuarioComparar();
                }
            }
        });

        ImageTextButton btnComparar = new ImageTextButton(
    Idiomas.getInstance().obtenerTexto("comparacion.boton_comparar"), 
    estiloBtnComparar
);
        btnComparar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    seleccionarUsuarioComparar();
                }
            }
        });

        tablaSelectorUsuario.add(labelSelector).padRight(15);
        tablaSelectorUsuario.add(selectorAmigo).width(200).padRight(15);
        tablaSelectorUsuario.add(btnComparar).size(100, 40);

        tablaPrincipal.add(tablaSelectorUsuario).padBottom(30).row();

        // tabla de comparacion
        tablaComparacion = new Table();
        crearTablaComparacionVacia();

        ScrollPane scrollComparacion = new ScrollPane(tablaComparacion, skin);
        scrollComparacion.setScrollingDisabled(true, false);
        tablaPrincipal.add(scrollComparacion).size(700, 350).padBottom(20).row();

        // botones finales
        Table tablaBotones = new Table();

        Button btnVolver = new Button(estiloVolver);
        btnVolver.add(new Label(Idiomas.getInstance().obtenerTexto("comparacion.volver"), skin));
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                }
            }
        });

        tablaBotones.add(btnVolver).size(120, 50);
        tablaPrincipal.add(tablaBotones).row();

        stage.addActor(tablaPrincipal);
    }

    private void cargarListaAmigos() {
        if (usuarioActual.getListaAmigos().isEmpty()) {
            selectorAmigo.setItems(Idiomas.getInstance().obtenerTexto("comparacion.sin_amigos"));
            return;
        }

        String[] amigos = usuarioActual.getListaAmigos().toArray(new String[0]);
        selectorAmigo.setItems(amigos);
    }

    private void seleccionarUsuarioComparar() {
        String seleccionado = selectorAmigo.getSelected();

        if (seleccionado.equals(Idiomas.getInstance().obtenerTexto("comparacion.sin_amigos"))) {
            mostrarMensaje(Idiomas.getInstance().obtenerTexto("comparacion.msj.agregar_amigos"), Color.YELLOW);
            return;
        }

        nombreUsuarioComparar = seleccionado;
        usuarioComparar = gestorArchivos.cargarUsuario(nombreUsuarioComparar);

        if (usuarioComparar == null) {
            mostrarMensaje(String.format(Idiomas.getInstance().obtenerTexto("comparacion.msj.error_cargar_usuario"), nombreUsuarioComparar), Color.RED);
            return;
        }

        actualizarComparacion();
        mostrarMensaje(String.format(Idiomas.getInstance().obtenerTexto("comparacion.msj.comparando_con"), nombreUsuarioComparar), Color.GREEN);
    }

    private void crearTablaComparacionVacia() {
        tablaComparacion.clear();

        Label mensaje = new Label(Idiomas.getInstance().obtenerTexto("comparacion.seleccionar_amigo"), skin);
        mensaje.setColor(Color.GRAY);
        tablaComparacion.add(mensaje).pad(50);
    }

    private void actualizarComparacion() {
        tablaComparacion.clear();

        // header con nombres
        Table headerTable = new Table();

        Label tuNombre = new Label(String.format(Idiomas.getInstance().obtenerTexto("comparacion.tu"), usuarioActual.getNombreUsuario()), skin);
        tuNombre.setColor(Color.CYAN);

        Label vs = new Label("VS", skin);
        vs.setColor(Color.WHITE);

        Label rivalNombre = new Label(String.format(Idiomas.getInstance().obtenerTexto("comparacion.rival"), usuarioComparar.getNombreUsuario()), skin);
        rivalNombre.setColor(Color.ORANGE);

        headerTable.add(tuNombre).width(250).padRight(50);
        headerTable.add(vs).width(50).padRight(50);
        headerTable.add(rivalNombre).width(250);

        tablaComparacion.add(headerTable).padBottom(30).row();

        // seccion niveles completados
        crearSeccionComparacion(Idiomas.getInstance().obtenerTexto("comparacion.niveles_completados"),
                usuarioActual.getNivelMaximoAlcanzado(), 7,
                usuarioComparar.getNivelMaximoAlcanzado(), 7);

        // seccion puntuacion total
        int maxPuntos = Math.max(usuarioActual.getPuntuacionTotal(), usuarioComparar.getPuntuacionTotal());
        if (maxPuntos == 0) {
            maxPuntos = 1000; // evitar division por zero
        }
        crearSeccionComparacion(Idiomas.getInstance().obtenerTexto("comparacion.puntuacion_total"),
                usuarioActual.getPuntuacionTotal(), maxPuntos,
                usuarioComparar.getPuntuacionTotal(), maxPuntos);

        // seccion tiempo promedio por nivel
        long tiempoPromedioTu = usuarioActual.getPartidasCompletadas() > 0
                ? usuarioActual.getTiempoTotalJugado() / usuarioActual.getPartidasCompletadas() : 0;
        long tiempoPromedioRival = usuarioComparar.getPartidasCompletadas() > 0
                ? usuarioComparar.getTiempoTotalJugado() / usuarioComparar.getPartidasCompletadas() : 0;

        long maxTiempo = Math.max(tiempoPromedioTu, tiempoPromedioRival);
        if (maxTiempo == 0) {
            maxTiempo = 60000; // 1 minuto default
        }
        crearSeccionComparacion(Idiomas.getInstance().obtenerTexto("comparacion.tiempo_promedio"),
                (int) (tiempoPromedioTu / 1000), (int) (maxTiempo / 1000),
                (int) (tiempoPromedioRival / 1000), (int) (maxTiempo / 1000));

        // seccion partidas completadas
        int maxPartidas = Math.max(usuarioActual.getPartidasCompletadas(), usuarioComparar.getPartidasCompletadas());
        if (maxPartidas == 0) {
            maxPartidas = 10;
        }

        crearSeccionComparacion(Idiomas.getInstance().obtenerTexto("comparacion.partidas_completadas"),
                usuarioActual.getPartidasCompletadas(), maxPartidas,
                usuarioComparar.getPartidasCompletadas(), maxPartidas);
    }

    private void crearSeccionComparacion(String titulo, int valorTu, int maxValor, int valorRival, int maxRival) {
        // titulo de seccion
        Label labelTitulo = new Label(titulo, skin);
        labelTitulo.setColor(Color.YELLOW);
        tablaComparacion.add(labelTitulo).padTop(20).padBottom(10).row();

        // crear barras comparativas usando tablas con background
        Table tablaBarras = new Table();

        // barra tuya
        Table contenedorTuBarra = new Table();
        Table barraTu = crearBarraProgreso(valorTu, maxValor, Color.CYAN);
        String textoTu = formatearValor(titulo, valorTu);
        Label labelTu = new Label(String.format(Idiomas.getInstance().obtenerTexto("comparacion.tu"), textoTu), skin);
        labelTu.setColor(Color.WHITE);

        contenedorTuBarra.add(barraTu).width(300).height(20).row();
        contenedorTuBarra.add(labelTu).left().padTop(5);

        // barra rival
        Table contenedorRivalBarra = new Table();
        Table barraRival = crearBarraProgreso(valorRival, maxRival, Color.ORANGE);
        String textoRival = formatearValor(titulo, valorRival);
        Label labelRival = new Label(String.format(Idiomas.getInstance().obtenerTexto("comparacion.rival"), textoRival), skin);
        labelRival.setColor(Color.WHITE);

        contenedorRivalBarra.add(barraRival).width(300).height(20).row();
        contenedorRivalBarra.add(labelRival).left().padTop(5);

        tablaBarras.add(contenedorTuBarra).padRight(50);
        tablaBarras.add(contenedorRivalBarra);

        tablaComparacion.add(tablaBarras).padBottom(15).row();
    }

    private Table crearBarraProgreso(int valor, int maxValor, Color color) {
        Table barra = new Table();

        // calcular porcentaje
        float porcentaje = maxValor > 0 ? Math.min(1.0f, (float) valor / maxValor) : 0f;

        // fondo de la barra
        Table fondo = new Table();
        fondo.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));

        // parte llena de la barra
        Table progreso = new Table();
        progreso.setBackground(skin.newDrawable("default-round", color));

        // contenedor principal
        Table contenedor = new Table();
        contenedor.add(progreso).width(300 * porcentaje).height(20).left();
        contenedor.add().expandX(); // espacio restante

        barra.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        barra.add(contenedor).fill();

        return barra;
    }

    private String formatearValor(String tipoSeccion, int valor) {
        switch (tipoSeccion) {
            case "NIVELES COMPLETADOS":
                return valor + "/7";
            case "PUNTUACION TOTAL":
                return String.format("%,d pts", valor);
            case "TIEMPO PROMEDIO POR NIVEL":
                if (valor == 0) {
                    return "0s";
                }
                int minutos = valor / 60;
                int segundos = valor % 60;
                return minutos > 0 ? String.format("%dm %ds", minutos, segundos) : String.format("%ds", segundos);
            case "PARTIDAS COMPLETADAS":
                return valor + " partidas";
            default:
                return String.valueOf(valor);
        }
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
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
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
