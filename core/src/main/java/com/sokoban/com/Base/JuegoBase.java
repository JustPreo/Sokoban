package com.sokoban.com.Base;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.sokoban.com.Base.JuegoBase.cajasEnum.*;
import static com.sokoban.com.Base.JuegoBase.objetivosEnum.*;
import java.util.ArrayList;

public class JuegoBase implements Screen {

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
    private int objetivosRealizados;

    // Grid (mapa estilo Sokoban)
    private final int TILE = 2; // cada celda mide 2 unidades del mundo
    private final int FILAS = 8;
    private final int COLUMNAS = 10;
    private final int cantidadC = 4;

    // 0 = suelo, 1 = pared |2 = objetivo |3 = cajas
    private final int[][] mapa = { // Tomar en cueta que esta inverso el mapa
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 2, 0, 0, 0, 0, 0, 0, 0, 1}, //objetivo0(1,1)
        {1, 0, 3, 0, 0, 0, 0, 0, 0, 1},//caja0(2,2)
        {1, 0, 0, 0, 0, 0, 3, 2, 0, 1},//caja1(6,3) , objetivo1(7,3)
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 3, 0, 0, 0, 0, 0, 0, 1},//caja2(2,5)
        {1, 0, 0, 0, 0, 0, 0, 0, 2, 1},//objetivo2(8,6)
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};
    //Mas que nada es una representacion visual de donde estan las cajas/objetivos asi es mejor entenderlo

    // posiciones en celdas
    private int jugadorX = 2, jugadorY = 4;

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(COLUMNAS * TILE, FILAS * TILE);
        shape = new ShapeRenderer();

        //objetos
        jogador = new Jugador(jugadorX * TILE, jugadorY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight(),TILE);
        spawnear();
        objetivosRealizados = 0;

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
        if (cajita != null) {

            if (!cajita.mover) {
                // La caja no se puede mover, se comporta como pared
                return;
            }

            int nuevoCajaX = (int) (cajita.hitbox.x / TILE) + dx;
            int nuevoCajaY = (int) (cajita.hitbox.y / TILE) + dy;
            Cajita cajitaTemp = new Cajita(nuevoCajaX * TILE, nuevoCajaY * TILE,TILE);
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
            }
        } else {
            jugadorX = nuevoX;
            jugadorY = nuevoY;
        }

        // actualizar pos
        jogador.setPos(jugadorX * TILE, jugadorY * TILE);

    }
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

    private void spawnear() {
        // spawnear N cajas según cantidadC
        for (int i = 0; i < cantidadC && i < cajasEnum.values().length; i++) {
            cajasEnum ce = cajasEnum.values()[i];
            cajas.add(new Cajita(ce.getX() * TILE, ce.getY() * TILE,TILE));
        }

        // spawnear N objetivos según cantidadC
        for (int i = 0; i < cantidadC && i < objetivosEnum.values().length; i++) {
            objetivosEnum oe = objetivosEnum.values()[i];
            objetivos.add(new Objetivo(oe.getX() * TILE, oe.getY() * TILE,TILE));
        }

        // paredes como antes
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                if (mapa[y][x] == 1) {
                    paredes.add(new Pared(x * TILE, y * TILE,TILE));
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
        if (objetivosRealizados == objetivos.size()) {
            System.out.println("Gano");
        }

    }

    @Override
    public void render(float f) {
        render();
    }

    @Override
    public void hide() {
    }

    public enum cajasEnum {
        caja0(2, 2), caja1(6, 3), caja2(2, 5);
        private final int x;
        private final int y;

        cajasEnum(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }

    public enum objetivosEnum {
        objetivo0(1, 1), objetivo1(7, 3), objetivo2(8, 6);
        private final int x;
        private final int y;

        objetivosEnum(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }

    public cajasEnum getCaja(int opcion) {
        switch (opcion) {
            case 1:
                return caja0;
            case 2:
                return caja1;
            case 3:
                return caja2;

        }
        return null;
    }

    public objetivosEnum getObjetivo(int opcion) {
        switch (opcion) {
            case 1:
                return objetivo0;
            case 2:
                return objetivo1;
            case 3:
                return objetivo2;

        }
        return null;
    }

}
