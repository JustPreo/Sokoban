package com.sokoban.com;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;

public class Main implements ApplicationListener {

    // Funcionalidades
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private ShapeRenderer shape;

    // Objetos
    private Jugador jogador;
    private ArrayList<Cajita> cajas = new ArrayList<>();
    private ArrayList<Pared> paredes = new ArrayList<>();
    private ArrayList<Objetivo> objetivos = new ArrayList<>();
    boolean moverCaja = true;

    // Grid (mapa estilo Sokoban)
    private final int TILE = 2; // cada celda mide 2 unidades del mundo
    private final int FILAS = 8;
    private final int COLUMNAS = 10;

    // 0 = suelo, 1 = pared |2 = objetivo |3 = cajas
    private int[][] mapa = { // Tomar en cueta que esta inverso el mapa
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 3, 0, 2, 0, 0, 0, 1},
        {1, 0, 0, 0, 2, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 3, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};

    // posiciones en celdas
    private int jugadorX = 2, jugadorY = 2;
    private int cajaX = 4, cajaY = 2;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(COLUMNAS * TILE, FILAS * TILE);
        shape = new ShapeRenderer();

        //objetos
        jogador = new Jugador(jugadorX * TILE, jugadorY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight());
        spawnear();

    }

    private void moverJugador(int dx, int dy) {
        int nuevoX = jugadorX + dx;
        int nuevoY = jugadorY + dy;

        // pared?
        if (mapa[nuevoY][nuevoX] == 1) {
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
        if (cajita != null && cajita.mover) {

            int nuevoCajaX = (int) (cajita.hitbox.x / TILE) + dx;
            int nuevoCajaY = (int) (cajita.hitbox.y / TILE) + dy;
            for (Cajita c : cajas) {
                if (cajita.hitbox.overlaps(c.hitbox) && c != cajita) {
                    return;
                }
            }

            if (mapa[nuevoCajaY][nuevoCajaX] != 1 && moverCaja) {
                //Osea si no hay una pared/obstaculo y se puede mover la caja todo joya
                jugadorX = nuevoX;
                jugadorY = nuevoY;
                cajita.setPos(nuevoCajaX * TILE, nuevoCajaY * TILE);
            }
        } else {
            jugadorX = nuevoX;
            jugadorY = nuevoY;
        }

        // actualizar pos
        jogador.setPos(jugadorX * TILE, jugadorY * TILE);

    }

    @Override
    public void render() {
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
        jogador.render(spriteBatch);

        for (Cajita caj : cajas) {
            caj.render(spriteBatch);
        }

        for (Objetivo obj : objetivos) {
            obj.render(spriteBatch);
        }

        for (Pared pared : paredes) {
            pared.render(spriteBatch);
        }
        spriteBatch.end();

        // Debug de hitboxes 
        shape.setProjectionMatrix(viewport.getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Line);

        // paredes
        shape.setColor(Color.GREEN);
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                if (mapa[y][x] == 1) {
                    shape.rect(x * TILE, y * TILE, TILE, TILE);
                }
            }
        }

        // jugador
        shape.setColor(Color.BLACK);
        shape.rect(jogador.getHitbox().x, jogador.getHitbox().y,
                jogador.getHitbox().width, jogador.getHitbox().height);

        // caja
        shape.end();
        revisarWin();
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
    }

    @Override
    public void resume() {
    }

    public void spawnear() {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                switch (mapa[f][c]) {
                    case 1:
                        Pared pared = new Pared(c * TILE, f * TILE);
                        paredes.add(pared);
                        //f = y
                        //c = x
                        //Me habia olvidado de eso XD
                        break;
                    case 2:

                        Objetivo objetivo = new Objetivo(c * TILE, f * TILE);
                        objetivos.add(objetivo);

                        break;
                    case 3:
                        Cajita caja = new Cajita(c * TILE, f * TILE);
                        cajas.add(caja);
                        break;
                }
            }

        }
    }

    public void revisarWin() {
        for (Cajita caja : cajas) {
            for (Objetivo obj : objetivos) {
                if (caja.hitbox.overlaps(obj.hitbox)) {
                    System.out.println("Gano");
                    caja.mover = false;

                }
            }

        }

    }

}
