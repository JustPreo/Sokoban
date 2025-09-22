package com.sokoban.com.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Base.IntentoDeMenu.MenuScreen;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;
import com.sokoban.com.Niveles.Lvl1;
import com.sokoban.com.Niveles.Lvl2;
import com.sokoban.com.Niveles.Lvl3;
import com.sokoban.com.Niveles.Lvl4;
import com.sokoban.com.Niveles.Lvl5;
import com.sokoban.com.Niveles.Lvl6;
import com.sokoban.com.RegistroPartida;
import com.sokoban.com.SelectorNiveles.Hub;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.SoundManager;
import java.util.ArrayList;

public abstract class JuegoBase implements Screen {

    // Funcionalidades
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private ShapeRenderer shape;
    protected BitmapFont fontStats;
    protected BitmapFont fontSmall;
    protected BitmapFont fontTitulo;
    protected Stage stage;
    protected Skin skin;
    private Table overlayPausa;
    private float winCheckTimer = 0f;

    protected SistemaUsuarios sistemaUsuarios;
    protected RegistroPartida partidaActual;

    // Objetos
    protected Jugador jogador;
    protected ArrayList<Cajita> cajas = new ArrayList<>();
    protected ArrayList<Pared> paredes = new ArrayList<>();
    protected ArrayList<Piso> pisos = new ArrayList<>();
    protected ArrayList<Objetivo> objetivos = new ArrayList<>();
    protected boolean moverCaja = true;
    protected int objetivosRealizados;
    protected boolean gano = false;
    protected boolean pausa = false;

    // Grid 
    protected int FILAS = 8;
    protected int COLUMNAS = 10;
    protected int cantidadC = 0;
    protected int pasos = 0;
    protected int segundosT = 0;
    protected int TILE = 800 / COLUMNAS;
    protected int vecesEmpujado = 0;
    protected int cantidadPous = 0;
    protected int cantidadPousPermitidas = 1;

    protected Idiomas idiomas;

    // Mapa de ejemplo
    private int[][] mapa = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 2, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 3, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 3, 2, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 3, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};

    // Animacion para fondo
    private Texture[] fondoFrames;
    private int frameActual = 0;
    private float timerFondo = 0f;
    private float tiempoPorFrame = 0.5f;

    protected int[][] cajasPos = new int[][]{{2, 2}, {6, 3}, {2, 5}};
    protected int[][] objetivosPos = new int[][]{{1, 1}, {7, 3}, {8, 6}};
    protected int jugadorXInicial, jugadorYInicial;
    protected int jugadorX = 2, jugadorY = 4;
    protected float timer = 0f;

    public abstract void conseguirCantCajas();

    protected abstract void configurarNivel();

    protected abstract void xyInicial(int x, int y);

    protected abstract int obtenerNumeroNivel();

    @Override
    public void show() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
        if (sistemaUsuarios.haySesionActiva()) {
            partidaActual = new RegistroPartida(obtenerNumeroNivel());
        }
        
        idiomas = Idiomas.getInstance();
        idiomas.cargarIdiomaUsuario();
        
        configurarNivel();
        conseguirCantCajas();

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(COLUMNAS * TILE, FILAS * TILE);
        shape = new ShapeRenderer();

        createPixelFont();

        jogador = new Jugador(jugadorX * TILE, jugadorY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight(), TILE);

        spawnear();
        objetivosRealizados = 0;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Cargar fondo animado
        fondoFrames = new Texture[7];
        for (int i = 0; i < 7; i++) {
            fondoFrames[i] = new Texture(Gdx.files.internal("Fondo/Espacio" + (i + 1) + ".png"));
        }
    }

    private void createPixelFont() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                    Gdx.files.internal("fonts/PressStart2P-vaV7.ttf")
            );

            FreeTypeFontGenerator.FreeTypeFontParameter par = new FreeTypeFontGenerator.FreeTypeFontParameter();
            par.color = Color.WHITE;
            par.magFilter = Texture.TextureFilter.Nearest;
            par.minFilter = Texture.TextureFilter.Nearest;
            par.borderWidth = 1;
            par.borderColor = Color.BLACK;

            // Font pequena (small)
            par.size = Math.max(12, TILE / 6);
            fontSmall = generator.generateFont(par);

            // Font stats
            par.size = Math.max(12, TILE / 5);
            fontStats = generator.generateFont(par);

            // Font titulo
            par.size = Math.max(12, TILE / 4);
            fontTitulo = generator.generateFont(par);

            generator.dispose();
        } catch (Exception e) {
            System.err.println("Error cargando fuentes: " + e.getMessage());
            fontSmall = fontStats = fontTitulo = new BitmapFont();
            fontSmall.getData().setScale(TILE * 0.025f);
            fontStats.getData().setScale(TILE * 0.03f);
            fontTitulo.getData().setScale(TILE * 0.04f);
        }
    }

    private void moverJugador(int dx, int dy) {
        int nuevoX = jugadorX + dx;
        int nuevoY = jugadorY + dy;

        // Verificar paredes
        if (mapa[nuevoY][nuevoX] == 1) {
            return;
        }

        if (gano || pausa) {
            return;
        }

        // Buscar cajita en la nueva pos
        Cajita cajita = null;
        for (Cajita caja : cajas) {
            if ((int) (caja.getHitbox().x / TILE) == nuevoX && (int) (caja.getHitbox().y / TILE) == nuevoY) {
                cajita = caja;
                break;
            }
        }

        // Si hay una cajita, verificar si se puede mover
        if (cajita != null) {
            int nuevoCajaX = (int) (cajita.getHitbox().x / TILE) + dx;
            int nuevoCajaY = (int) (cajita.getHitbox().y / TILE) + dy;

            Cajita cajitaTemp = new Cajita(nuevoCajaX * TILE, nuevoCajaY * TILE, TILE);
            boolean colision = false;

            for (Cajita c : cajas) {
                if (c != cajita && cajitaTemp.getHitbox().overlaps(c.getHitbox())) {
                    colision = true;
                    break;
                }
            }
            cajitaTemp.dispose();

            if (mapa[nuevoCajaY][nuevoCajaX] != 1 && !colision) {
                jugadorX = nuevoX;
                jugadorY = nuevoY;
                cajita.setPos(nuevoCajaX * TILE, nuevoCajaY * TILE);
                jogador.setPos(jugadorX * TILE, jugadorY * TILE);
                vecesEmpujado++;

                if (partidaActual != null) {
                    partidaActual.incrementarMovimientos();
                }

                for (Piso pis : pisos) {
                    if (pis.isPou() && cajita.hitbox.overlaps(pis.hitbox)) {
                        pis.setPouAplastado();
                    }
                }
            }
        } else {
            jugadorX = nuevoX;
            jugadorY = nuevoY;
            for (Piso pis : pisos) {
                if (pis.isPou() && jogador.hitbox.overlaps(pis.hitbox)) {
                    pis.setPouAplastado();
                }
            }
            jogador.setPos(jugadorX * TILE, jugadorY * TILE);
            pasos++;

            if (partidaActual != null) {
                partidaActual.incrementarMovimientos();
            }
        }
    }

    @Override
    public void render(float delta) {
        SoundManager.update();

        if (!gano && !pausa) {
            timer += Gdx.graphics.getDeltaTime();
            winCheckTimer += Gdx.graphics.getDeltaTime();

            if (winCheckTimer >= 0.5f) {
                revisarWin();
                winCheckTimer = 0f;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !gano) {
            if (!pausa) {
                SoundManager.pauseMusic();
                pause();
            } else {
                SoundManager.resumeMusic();
                resume();
            }
        }

        boolean puedeMoverse = !jogador.estaAnimando();
        for (Cajita caja : cajas) {
            if (caja.estaAnimando()) {
                puedeMoverse = false;
                break;
            }
        }

        if (puedeMoverse) {
            if (jogador.consumeUp()) {
                moverJugador(0, 1);
            } else if (jogador.consumeDown()) {
                moverJugador(0, -1);
            } else if (jogador.consumeLeft()) {
                moverJugador(-1, 0);
            } else if (jogador.consumeRight()) {
                moverJugador(1, 0);
            }
        } else {
            jogador.consumeUp();
            jogador.consumeDown();
            jogador.consumeLeft();
            jogador.consumeRight();
        }

        jogador.update();
        for (Cajita caja : cajas) {
            caja.update();
        }

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        timerFondo += Gdx.graphics.getDeltaTime();
        if (timerFondo >= tiempoPorFrame) {
            frameActual = (frameActual + 1) % fondoFrames.length;
            timerFondo = 0f;
        }

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(fondoFrames[frameActual], 0, 0, COLUMNAS * TILE, FILAS * TILE);
        
        for (Piso piso : pisos) {
            piso.render(spriteBatch);
            piso.update();
        }

        for (Objetivo obj : objetivos) {
            obj.render(spriteBatch);
        }

        jogador.render(spriteBatch);

        for (Cajita caj : cajas) {
            caj.render(spriteBatch);
        }

        for (Pared pared : paredes) {
            pared.render(spriteBatch);
            pared.update();
        }

        segundosT = (int) timer;
        int minutos = (int) (timer / 60);
        int segundos = (int) (timer % 60);
        float margen = TILE * 0.2f;
        fontTitulo.draw(spriteBatch, String.format("%02d:%02d   Empujones: %d", minutos, segundos, vecesEmpujado), margen, FILAS * TILE - margen);

        spriteBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F12)) {
            gano = true;
            debugMostrarFinNivel();
        }

        if (gano || pausa) {
            stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width > 0 && height > 0) {
            viewport.update(width, height, true);
            stage.getCamera().update();
        }
    }

    @Override
    public void dispose() {
        if (fondoFrames != null) {
            for (Texture t : fondoFrames) {
                t.dispose();
            }
        }
        spriteBatch.dispose();
        jogador.dispose();
        for (Pared pared : paredes) {
            pared.dispose();
        }
        for (Cajita caj : cajas) {
            caj.dispose();
        }
        for (Objetivo obj : objetivos) {
            obj.dispose();
        }
        for (Piso piso : pisos) {
            piso.dispose();
        }
        shape.dispose();
        fontSmall.dispose();
        fontTitulo.dispose();
        fontStats.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void pause() {
        pausa = true;
        if (partidaActual != null && !gano) {
            partidaActual.finalizarPartida(false, "pausado");
            sistemaUsuarios.registrarPartida(partidaActual);
        }
        menuPausa();
    }

    @Override
    public void resume() {
        pausa = false;
        if (overlayPausa != null) {
            overlayPausa.remove();
            overlayPausa = null;
        }
    }

    private void spawnear() {
        for (int i = 0; i < cantidadC; i++) {
            int[] pos = cajasPos[i];
            cajas.add(new Cajita(pos[0] * TILE, pos[1] * TILE, TILE));
        }

        for (int i = 0; i < cantidadC; i++) {
            int[] pos = objetivosPos[i];
            objetivos.add(new Objetivo(pos[0] * TILE, pos[1] * TILE, TILE));
        }

        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                if (mapa[y][x] == 1) {
                    paredes.add(new Pared(x * TILE, y * TILE, TILE));
                }
                if (mapa[y][x] == 0 || mapa[y][x] == 3 || mapa[y][x] == 2 && (y != (FILAS - 1) && x != (COLUMNAS - 1)) || mapa[y][x] == 4) {
                    pisos.add(crearPiso(x, y));
                }
            }
        }
    }

    public Piso crearPiso(int x, int y) {
        Piso pis = new Piso(x * TILE, y * TILE, TILE);
        if (pis.isPou()) {
            if (cantidadPous < cantidadPousPermitidas) {
                cantidadPous++;
                return pis;
            } else {
                while (pis.isPou()) {
                    pis = new Piso(x * TILE, y * TILE, TILE);
                }
            }
        }
        return pis;
    }

    public void revisarWin() {
        objetivosRealizados = 0;

        for (Cajita caja : cajas) {
            for (Objetivo obj : objetivos) {
                if (caja.getHitbox().overlaps(obj.getHitbox())) {
                    objetivosRealizados++;
                    break;
                }
            }
        }

        if (objetivosRealizados == objetivos.size() && !gano) {
            gano = true;

            if (partidaActual != null && sistemaUsuarios.haySesionActiva()) {
                partidaActual.setMovimientos(vecesEmpujado);
                partidaActual.finalizarPartida(true, "completado");
                sistemaUsuarios.registrarPartida(partidaActual);
            }

            finNivel();
        }
    }

    @Override
    public void hide() {
    }

    public void cambiarMapa(int[][] mapa) {
        this.mapa = mapa;
    }

    public void guardarSegundos(int segundos) {
        // logica para guardar segundos
    }

    private void menuPausa() {
        // OBTENER TEXTOS ACTUALES - SE ACTUALIZA CADA VEZ QUE SE ABRE EL MENU
        String textoPausa = Idiomas.getInstance().obtenerTexto("PAUSA");
        String textoVolver = Idiomas.getInstance().obtenerTexto("menu.pausa.volver");
        String textoReiniciar = Idiomas.getInstance().obtenerTexto("menu.pausa.reiniciar");
        
        overlayPausa = new Table();
        overlayPausa.setFillParent(true);
        overlayPausa.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlayPausa.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlayPausa.add(panel);

        Label titulo = new Label(textoPausa, new Label.LabelStyle(fontTitulo, Color.WHITE));
        panel.add(titulo).padBottom(20).row();

        Texture texturaFondoNormal = new Texture(Gdx.files.internal("fondoNormal.png"));
        Texture texturaFondoNormal2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoNormalUp = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal));
        Drawable fondoNormalDown = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal2));

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.up = fondoNormalUp;
        estiloBoton.down = fondoNormalDown;
        estiloBoton.font = fontSmall;
        estiloBoton.fontColor = Color.WHITE;

        TextButton btnVolver = new TextButton(textoVolver, estiloBoton);
        TextButton btnReiniciar = new TextButton(textoReiniciar, estiloBoton);

        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
                overlayPausa.remove();
                SoundManager.resumeMusic();
            }
        });

        btnReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlayPausa.remove();
                reiniciarNivel();
            }
        });

        panel.add(btnVolver).size(150, 50).padBottom(10).row();
        panel.add(btnReiniciar).size(150, 50).row();

        stage.addActor(overlayPausa);
    }

    private void finNivel() {
        SoundManager.pauseMusic();
        SoundManager.playVictorySound();
        
        // OBTENER TEXTOS ACTUALES - SE ACTUALIZA CADA VEZ QUE SE COMPLETA EL NIVEL
        String textoTitulo = Idiomas.getInstance().obtenerTexto("NIVEL_COMPLETADO");
        String textoTiempo = Idiomas.getInstance().obtenerTexto("fin.tiempo");
        String textoMovimientos = Idiomas.getInstance().obtenerTexto("fin.movimientos");
        String textoPuntuacion = Idiomas.getInstance().obtenerTexto("fin.puntuacion");
        String textoSiguiente = Idiomas.getInstance().obtenerTexto("menu.fin.siguiente");
        String textoReiniciar = Idiomas.getInstance().obtenerTexto("menu.fin.reiniciar");
        String textoMenu = Idiomas.getInstance().obtenerTexto("menu.fin.menu");
        
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel);

        Label titulo = new Label(textoTitulo, new Label.LabelStyle(fontTitulo, Color.GREEN));
        titulo.setColor(Color.GREEN);
        panel.add(titulo).padBottom(20).row();

        if (partidaActual != null) {
            Label tiempoLabel = new Label(textoTiempo + ": " + String.format("%02d:%02d", (int) (timer / 60), (int) (timer % 60)), skin);
            Label movimientosLabel = new Label(textoMovimientos + ": " + partidaActual.getMovimientos(), skin);
            Label puntuacionLabel = new Label(textoPuntuacion + ": " + partidaActual.getPuntuacion(), skin);

            panel.add(tiempoLabel).padBottom(5).row();
            panel.add(movimientosLabel).padBottom(5).row();
            panel.add(puntuacionLabel).padBottom(15).row();
        }

        Texture texturaFondoNormal = new Texture(Gdx.files.internal("fondoNormal.png"));
        Texture texturaFondoNormal2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoNormalUp = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal));
        Drawable fondoNormalDown = new TextureRegionDrawable(new TextureRegion(texturaFondoNormal2));

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.up = fondoNormalUp;
        estiloBoton.down = fondoNormalDown;
        estiloBoton.font = fontSmall;
        estiloBoton.fontColor = Color.WHITE;

        TextButton btnSiguiente = new TextButton(textoSiguiente, estiloBoton);
        TextButton btnReiniciar = new TextButton(textoReiniciar, estiloBoton);
        TextButton btnMenu = new TextButton(textoMenu, estiloBoton);

        btnSiguiente.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                SoundManager.resumeMusic();
                irSiguienteNivel();
            }
        });

        btnReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                SoundManager.resumeMusic();
                reiniciarNivel();
            }
        });

        btnMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                SoundManager.playMusic("lobby", true, 0.5f);
            }
        });

        Table botonesTable = new Table();
        botonesTable.add(btnSiguiente).size(180, 50).padRight(10);
        botonesTable.add(btnReiniciar).size(200, 50).padRight(10);
        botonesTable.add(btnMenu).size(150, 50);

        panel.add(botonesTable).row();
        stage.addActor(overlay);
    }

    private void reiniciarNivel() {
        cajas.clear();
        paredes.clear();
        objetivos.clear();
        pisos.clear();

        timer = 0f;
        vecesEmpujado = 0;
        pasos = 0;
        objetivosRealizados = 0;
        gano = false;
        pausa = false;
        cantidadPous = 0;
        SoundManager.resumeMusic();

        configurarNivel();
        spawnear();

        jugadorX = jugadorXInicial;
        jugadorY = jugadorYInicial;
        jogador.setPos(jugadorX * TILE, jugadorY * TILE);
        
        if (sistemaUsuarios.haySesionActiva()) {
            if (partidaActual != null) {
                partidaActual.finalizarPartida(false, "reiniciado");
                sistemaUsuarios.registrarPartida(partidaActual);
            }
            partidaActual = new RegistroPartida(obtenerNumeroNivel());
        }
    }

    protected void irSiguienteNivel() {
        int nivelActual = obtenerNumeroNivel();
        if (sistemaUsuarios.haySesionActiva()
                && sistemaUsuarios.nivelDesbloqueado(nivelActual + 1)
                && nivelActual < 7) {

            Juegito juego = (Juegito) Gdx.app.getApplicationListener();
            int siguienteNivel = nivelActual + 1;

            switch (siguienteNivel) {
                case 1:
                    juego.setScreen(new Lvl1());
                    break;
                case 2:
                    juego.setScreen(new Lvl2());
                    break;
                case 3:
                    juego.setScreen(new Lvl3());
                    break;
                case 4:
                    juego.setScreen(new Lvl4());
                    break;
                case 5:
                    juego.setScreen(new Lvl5());
                    break;
                case 6:
                    juego.setScreen(new Lvl6());
                    break;
                default:
                    juego.setScreen(new Hub());
                    break;
            }
        } else {
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
        }
    }

    public void debugMostrarFinNivel() {
        finNivel();
    }
}