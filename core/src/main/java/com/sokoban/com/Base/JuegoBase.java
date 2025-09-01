package com.sokoban.com.Base;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Base.IntentoDeMenu.MenuScreen;

import com.sokoban.com.Juegito;
import java.util.ArrayList;

public abstract class JuegoBase implements Screen {

    // Funcionalidades
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private ShapeRenderer shape;
    protected BitmapFont font;
    protected Stage stage;
    protected Skin skin;
    private Table overlayPausa;

    // Objetos
    protected Jugador jogador;
    protected ArrayList<Cajita> cajas = new ArrayList<>();
    protected ArrayList<Pared> paredes = new ArrayList<>();
    protected ArrayList<Objetivo> objetivos = new ArrayList<>();
    protected boolean moverCaja = true;
    protected int objetivosRealizados;
    protected boolean gano = false;
    protected boolean pausa = false;

    // Grid (mapa estilo Sokoban)
    protected int FILAS = 8; //Elegir las filas manuales
    protected int COLUMNAS = 10; //Elegir las columnas manuales
    protected int cantidadC = 0; //Esto se define despues con conseguir cantCajas por nivel
    protected int segundosT = 0;
    protected int TILE = 800 / COLUMNAS; //
    protected int vecesEmpujado = 0;

    // 0 = suelo, 1 = pared |2 = objetivo |3 = cajas
    private int[][] mapa = { // Tomar en cueta que esta inverso el mapa
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 2, 0, 0, 0, 0, 0, 0, 0, 1}, //objetivo0(1,1)
        {1, 0, 3, 0, 0, 0, 0, 0, 0, 1},//caja0(2,2)
        {1, 0, 0, 0, 0, 0, 3, 2, 0, 1},//caja1(6,3) , objetivo1(7,3)
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 3, 0, 0, 0, 0, 0, 0, 1},//caja2(2,5)
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 1},//objetivo2(8,6)
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};
    //Mas que nada es una representacion visual de donde estan las cajas/objetivos asi es mejor entenderlo

    // Posiciones iniciales de cajas
    protected int[][] cajasPos = new int[][]{{2, 2}, {6, 3}, {2, 5}};
    // Posiciones de objetivos
    protected int[][] objetivosPos = new int[][]{{1, 1}, {7, 3}, {8, 6}};

    // posiciones en celdas
    protected int jugadorX = 2, jugadorY = 4;
    protected float timer = 0f;

    public abstract void conseguirCantCajas();
    //La idea de como deberia de ser   

    /*switch (MenuScreen.dificultad) {
            case 1:
                cantidadC = 1;
                break;
            case 2:
                cantidadC = 2;
                break;
            case 3:
                cantidadC = 3;
                break;
        }*/

    protected abstract void configurarNivel();

    @Override
    public void show() {
        configurarNivel();
        conseguirCantCajas();

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(COLUMNAS * TILE, FILAS * TILE);

        shape = new ShapeRenderer();//Para las hitbox

        // Crear fuente
        font = new BitmapFont(); // por defecto carga font interno
        font.setColor(Color.WHITE);

        // Escala proporcional al tamaño del nivel
        float escalaBase = 0.05f; //
        float escala = escalaBase * TILE;
        font.getData().setScale(escala); //objetos

        jogador = new Jugador(jugadorX * TILE, jugadorY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight(), TILE);

        spawnear();
        objetivosRealizados = 0;

        // Stage y Skin que son obligatorios para poder usar cosas como botones (Problema de UI)
        //Imaginate no poder usar swing ( pipipi , extrano swing:( )
        stage = new Stage(new ScreenViewport());
        //stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

    }

    private void moverJugador(int dx, int dy) {
        int nuevoX = jugadorX + dx;
        int nuevoY = jugadorY + dy;

        // pared?
        if (mapa[nuevoY][nuevoX] == 1) {
            return;
        }
        if (gano || pausa) {
            return;
        }

        Cajita cajita = null;
        for (Cajita caja : cajas) {
            if ((int) (caja.hitbox.x / TILE) == nuevoX && (int) (caja.hitbox.y / TILE) == nuevoY) {
                cajita = caja;
                break;
            }
        }
        // caja?
        if (cajita != null) {

            if (!cajita.mover) {
                // La caja no se puede mover, se comporta como pared
                return;
            }

            int nuevoCajaX = (int) (cajita.hitbox.x / TILE) + dx;
            int nuevoCajaY = (int) (cajita.hitbox.y / TILE) + dy;
            Cajita cajitaTemp = new Cajita(nuevoCajaX * TILE, nuevoCajaY * TILE, TILE);
            for (Cajita c : cajas) {
                if (c != cajita && cajitaTemp.hitbox.overlaps(c.hitbox)) {
                    return; // otra caja bloquea el movimiento
                }
            }

            if (mapa[nuevoCajaY][nuevoCajaX] != 1 && cajita.mover) {
                //Osea si no hay una pared/obstaculo y se puede mover la caja todo joya
                jugadorX = nuevoX;
                jugadorY = nuevoY;
                cajita.setPos(nuevoCajaX * TILE, nuevoCajaY * TILE);
                vecesEmpujado++;
            }
        } else {
            jugadorX = nuevoX;
            jugadorY = nuevoY;
        }

        // actualizar pos
        jogador.setPos(jugadorX * TILE, jugadorY * TILE);

    }

    public void render() {
        if (!gano && !pausa) {
            timer += Gdx.graphics.getDeltaTime();
        } // aumenta segundos

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !gano) {
            if (!pausa) {
                pause();
            }
            else if (pausa)
            {
            resume();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            moverJugador(0, 1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            moverJugador(0, -1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            moverJugador(-1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            moverJugador(1, 0);
        }

        ScreenUtils.clear(Color.DARK_GRAY);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        // Dibujar sprites
        spriteBatch.begin();

        for (Objetivo obj : objetivos) {//Aqui se pone primero el obj
            obj.render(spriteBatch);
        }
        jogador.render(spriteBatch);
        for (Cajita caj : cajas) {//Despues las cajas (por prioridad)
            caj.render(spriteBatch);
            caj.update();
        }

        for (Pared pared : paredes) {//De ultimo las paredes
            pared.render(spriteBatch);
            pared.update();
        }

        // Mostrar timer
        segundosT = (int) timer;
        int minutos = (int) (timer / 60);
        int segundos = (int) (timer % 60);
        float margen = TILE * 0.2f; // relativo a TILE, ajustable
        font.draw(spriteBatch, String.format("%02d:%02d   Veces empujado %d", minutos, segundos, vecesEmpujado), margen, FILAS * TILE - margen);
        //%d → número normal (vecesEmpujado)

        spriteBatch.end();

        // Debug de hitboxes 
        shape.setProjectionMatrix(viewport.getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Line);

        // jugador
        shape.setColor(Color.BLACK);
        shape.rect(jogador.getHitbox().x, jogador.getHitbox().y,
                jogador.getHitbox().width, jogador.getHitbox().height);
        shape.end();
        revisarWin();
        if (gano || pausa) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.getViewport().apply();
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width > 0 && height > 0) {
            viewport.update(width, height, true);
        }
    }

    @Override
    public void dispose() {
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

        shape.dispose();
    }

    @Override
    public void pause() {
        pausa = true;
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
        for (int i = 0; i < cantidadC; i++) {//Hacerlo basado en cantidadC
            int[] pos = cajasPos[i];
            cajas.add(new Cajita(pos[0] * TILE, pos[1] * TILE, TILE));
        }

        for (int i = 0; i < cantidadC; i++) {
            int[] pos = objetivosPos[i];
            objetivos.add(new Objetivo(pos[0] * TILE, pos[1] * TILE, TILE));
        }

        // paredes como antes
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                if (mapa[y][x] == 1) {
                    paredes.add(new Pared(x * TILE, y * TILE, TILE));
                }
            }
        }
    }

    public void revisarWin() {
        for (Cajita caja : cajas) {
            for (Objetivo obj : objetivos) {
                if (caja.hitbox.overlaps(obj.hitbox) && caja.mover != false) {//Para evitar multiples setFalse
                    caja.mover = false;
                    objetivosRealizados++;

                }
            }

        }
        if (objetivosRealizados == objetivos.size() && !gano) {
            System.out.println("Gano");
            gano = true;
            guardarSegundos(segundosT);
            finNivel();
        }

    }

    @Override
    public void render(float f) {
        render();
    }

    @Override
    public void hide() {
    }

    public void cambiarMapa(int[][] mapa) {
        this.mapa = mapa;

    }

    public void guardarSegundos(int segundos) {
        //Logica para guardar segundos en archivos
    }

    private void menuPausa() {
        
        overlayPausa = new Table();
        overlayPausa.setFillParent(true);
        overlayPausa.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlayPausa.center();
        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlayPausa.add(panel);
        Label titulo = new Label("Nivel Completado!11!!", skin);
        panel.add(titulo).padBottom(20).row();
        TextButton btnVolver = new TextButton("Volver a menu", skin);
        TextButton btnReiniciar = new TextButton("Reiniciar", skin);

        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Volver");
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                // remover overlay para limpiar
                overlayPausa.remove();

            }
        });

        btnReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Reiniciar");
                // Quitar overlay
                overlayPausa.remove();
                // Reiniciar variables del nivel
                cajas.clear();
                paredes.clear();
                objetivos.clear();
                timer = 0f;
                vecesEmpujado = 0;
                objetivosRealizados = 0;
                gano = false;
                pausa = false;
                configurarNivel();
                spawnear();
                jogador.setPos(jugadorX * TILE, jugadorY * TILE);
            }
        });

        panel.add(btnVolver).size(150, 50).padBottom(10).row();
        panel.add(btnReiniciar).size(150, 50).padBottom(10).row();

        stage.addActor(overlayPausa); 

    }

    //Logica para pantalla de volver atras y tiempo?
    private void finNivel() {

        // Fondo semi-transparente para bloquear input detrás
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        // Panel central
        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel);

        // Título
        Label titulo = new Label("Nivel Completado!11!!", skin);
        panel.add(titulo).padBottom(20).row();

        // Botones de dificultad
        TextButton btnVolver = new TextButton("Volver a menu", skin);
        TextButton btnReiniciar = new TextButton("Reiniciar", skin);
        TextButton btnExtra = new TextButton("Extra", skin);

        // Acciones de los botones
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Volver");
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                // remover overlay para limpiar
                overlay.remove();

            }
        });

        btnReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Reiniciar");
                // Quitar overlay
                overlay.remove();
                // Reiniciar variables del nivel
                cajas.clear();
                paredes.clear();
                objetivos.clear();
                timer = 0f;
                vecesEmpujado = 0;
                objetivosRealizados = 0;
                gano = false;
                pausa = false;
                configurarNivel();
                spawnear();
                jogador.setPos(jugadorX * TILE, jugadorY * TILE);
            }
        });

        btnExtra.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Extra");

            }
        });

        // Agregar botones al panel
        panel.add(btnVolver).size(150, 50).padBottom(10).row();
        panel.add(btnReiniciar).size(150, 50).padBottom(10).row();
        panel.add(btnExtra).size(150, 50).row();

        // Agregar overlay al stage
        stage.addActor(overlay);
    }

}
