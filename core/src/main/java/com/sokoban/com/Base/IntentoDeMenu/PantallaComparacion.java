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
    private Button.ButtonStyle estiloExtra, estiloVolver;

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

        Label error = new Label("Necesitas iniciar sesion para comparar estadisticas", skin);
        error.setColor(Color.RED);
        tabla.add(error).padBottom(20).row();

        Button btnVolver = new Button(estiloVolver);
        btnVolver.add(new Label("Volver", skin));
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
        Label titulo = new Label("COMPARACION DE ESTADISTICAS", skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // selector de usuario para comparar
        Table tablaSelectorUsuario = new Table();
        
        Label labelSelector = new Label("Comparar con:", skin);
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

        Button btnComparar = new Button(estiloExtra);
        btnComparar.add(new Label("Comparar", skin));
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
        btnVolver.add(new Label("Volver", skin));
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
            selectorAmigo.setItems("No tienes amigos agregados");
            return;
        }

        String[] amigos = usuarioActual.getListaAmigos().toArray(new String[0]);
        selectorAmigo.setItems(amigos);
    }

    private void seleccionarUsuarioComparar() {
        String seleccionado = selectorAmigo.getSelected();
        
        if (seleccionado.equals("No tienes amigos agregados")) {
            mostrarMensaje("Primero agrega algunos amigos", Color.YELLOW);
            return;
        }

        nombreUsuarioComparar = seleccionado;
        usuarioComparar = gestorArchivos.cargarUsuario(nombreUsuarioComparar);

        if (usuarioComparar == null) {
            mostrarMensaje("No se pudo cargar datos del usuario: " + nombreUsuarioComparar, Color.RED);
            return;
        }

        actualizarComparacion();
        mostrarMensaje("Comparando con " + nombreUsuarioComparar, Color.GREEN);
    }

    private void crearTablaComparacionVacia() {
        tablaComparacion.clear();
        
        Label mensaje = new Label("Selecciona un amigo para comparar estadisticas", skin);
        mensaje.setColor(Color.GRAY);
        tablaComparacion.add(mensaje).pad(50);
    }

    private void actualizarComparacion() {
        tablaComparacion.clear();

        // header con nombres
        Table headerTable = new Table();
        
        Label tuNombre = new Label("TU: " + usuarioActual.getNombreUsuario(), skin);
        tuNombre.setColor(Color.CYAN);
        
        Label vs = new Label("VS", skin);
        vs.setColor(Color.WHITE);
        
        Label rivalNombre = new Label("RIVAL: " + usuarioComparar.getNombreUsuario(), skin);
        rivalNombre.setColor(Color.ORANGE);

        headerTable.add(tuNombre).width(250).padRight(50);
        headerTable.add(vs).width(50).padRight(50);
        headerTable.add(rivalNombre).width(250);

        tablaComparacion.add(headerTable).padBottom(30).row();

        // seccion niveles completados
        crearSeccionComparacion("NIVELES COMPLETADOS",
            usuarioActual.getNivelMaximoAlcanzado(), 7,
            usuarioComparar.getNivelMaximoAlcanzado(), 7);

        // seccion puntuacion total
        int maxPuntos = Math.max(usuarioActual.getPuntuacionTotal(), usuarioComparar.getPuntuacionTotal());
        if (maxPuntos == 0) maxPuntos = 1000; // evitar division por zero
        
        crearSeccionComparacion("PUNTUACION TOTAL",
            usuarioActual.getPuntuacionTotal(), maxPuntos,
            usuarioComparar.getPuntuacionTotal(), maxPuntos);

        // seccion tiempo promedio por nivel
        long tiempoPromedioTu = usuarioActual.getPartidasCompletadas() > 0 ? 
            usuarioActual.getTiempoTotalJugado() / usuarioActual.getPartidasCompletadas() : 0;
        long tiempoPromedioRival = usuarioComparar.getPartidasCompletadas() > 0 ?
            usuarioComparar.getTiempoTotalJugado() / usuarioComparar.getPartidasCompletadas() : 0;

        long maxTiempo = Math.max(tiempoPromedioTu, tiempoPromedioRival);
        if (maxTiempo == 0) maxTiempo = 60000; // 1 minuto default

        crearSeccionComparacion("TIEMPO PROMEDIO POR NIVEL",
            (int)(tiempoPromedioTu / 1000), (int)(maxTiempo / 1000),
            (int)(tiempoPromedioRival / 1000), (int)(maxTiempo / 1000));

        // seccion partidas completadas
        int maxPartidas = Math.max(usuarioActual.getPartidasCompletadas(), usuarioComparar.getPartidasCompletadas());
        if (maxPartidas == 0) maxPartidas = 10;

        crearSeccionComparacion("PARTIDAS COMPLETADAS",
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
        Label labelTu = new Label("Tu: " + textoTu, skin);
        labelTu.setColor(Color.WHITE);
        
        contenedorTuBarra.add(barraTu).width(300).height(20).row();
        contenedorTuBarra.add(labelTu).left().padTop(5);

        // barra rival
        Table contenedorRivalBarra = new Table();
        Table barraRival = crearBarraProgreso(valorRival, maxRival, Color.ORANGE);
        String textoRival = formatearValor(titulo, valorRival);
        Label labelRival = new Label("Rival: " + textoRival, skin);
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
        float porcentaje = maxValor > 0 ? Math.min(1.0f, (float)valor / maxValor) : 0f;
        
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
                if (valor == 0) return "0s";
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
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (texturaExtra != null) texturaExtra.dispose();
        if (texturaExtra2 != null) texturaExtra2.dispose();
        if (texturaVolver != null) texturaVolver.dispose();
        if (texturaVolver2 != null) texturaVolver2.dispose();
    }
}