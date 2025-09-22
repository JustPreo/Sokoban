package com.sokoban.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.com.Base.Jugador;
import com.sokoban.com.Base.Pared;
import com.sokoban.com.Base.PisoHub;
import com.sokoban.com.Base.Thanos;
import com.sokoban.com.SelectorNiveles.Hub;

public class ThanosRoom implements Screen {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private FitViewport viewport;

    private Jugador jugador;
    private Thanos thanos;
    private BitmapFont font;

    private int TILE = 32;
    private int FILAS = 7;
    private int COLUMNAS = 11;

    private Texture puertaTex;
    private Pared[][] paredes;
    private PisoHub[][] pisos;

    // Interacción
    private boolean enRangoThanos = false;
    private boolean dialogoActivo = false;
    private boolean enRangoPuerta = false;
    private String textoThanos = "Gracias a ti pude encontrar todas las gemas!";
    private int DIALOG_HEIGHT = 50;

    // Posición de la puerta en el mapa
    private int puertaFila, puertaCol;

    private int[][] mapa = {
        {1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public ThanosRoom() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(COLUMNAS * TILE, FILAS * TILE);

        // Crear jugador
        int startX = COLUMNAS / 2;
        int startY = 1;
        jugador = new Jugador(startX * TILE, startY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight(), TILE);

        // Crear paredes y pisos
        paredes = new Pared[FILAS][COLUMNAS];
        pisos = new PisoHub[FILAS][COLUMNAS];
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                float x = c * TILE;
                float y = f * TILE;
                if (mapa[f][c] == 1) {
                    paredes[f][c] = new Pared(x, y, TILE);
                } else {
                    pisos[f][c] = new PisoHub(x, y, TILE);
                }

                // Detectar Thanos y puerta
                if (mapa[f][c] == 3) {
                    thanos = new Thanos(x, y, TILE);
                }
                if (mapa[f][c] == 2) {
                    puertaCol = c;
                    puertaFila = f;
                }
            }
        }

        // Puerta
        puertaTex = new Texture("puerta.png");

        // Fuente
        font = new BitmapFont();
        font.getData().setScale(0.8f);
    }

    @Override
    public void render(float delta) {
        // ESC = volver al hub
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            SoundManager.playMusic("lobby", true, 0.5f);
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
        }

        // --- Movimiento jugador (solo si no hay diálogo activo) ---
        if (!dialogoActivo) {
            boolean seMovio = false;
            float newX = jugador.getX();
            float newY = jugador.getY();

            if (jugador.consumeUp()) {
                newY += TILE;
                jugador.setDireccion(1);
                seMovio = true;
            }
            if (jugador.consumeDown()) {
                newY -= TILE;
                jugador.setDireccion(0);
                seMovio = true;
            }
            if (jugador.consumeLeft()) {
                newX -= TILE;
                jugador.setDireccion(3);
                seMovio = true;
            }
            if (jugador.consumeRight()) {
                newX += TILE;
                jugador.setDireccion(2);
                seMovio = true;
            }

            // Revisar colisiones
            int col = (int) newX / TILE;
            int fila = (int) newY / TILE;
            if (fila >= 0 && fila < FILAS && col >= 0 && col < COLUMNAS) {
                int celda = mapa[fila][col];
                if (celda == 0) { // Piso
                    if (seMovio) {
                        jugador.setPos(newX, newY);
                    }
                }
            }
        }

        // --- Interacción con Thanos ---
        float dx = jugador.getX() - thanos.getX();
        float dy = jugador.getY() - thanos.getY();
        enRangoThanos = Math.sqrt(dx * dx + dy * dy) < TILE * 1.5f;

        if (enRangoThanos && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            dialogoActivo = !dialogoActivo;
        }

        // --- Interacción con la puerta ---
        float px = puertaCol * TILE;
        float py = puertaFila * TILE;
        float dxP = jugador.getX() - px;
        float dyP = jugador.getY() - py;
        enRangoPuerta = Math.sqrt(dxP * dxP + dyP * dyP) < TILE * 1.5f;

        if (enRangoPuerta && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            SoundManager.playMusic("lobby", true, 0.5f);
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
        }

        // Actualizar animaciones
        jugador.update();
        thanos.update(delta);

        // --- Render del mapa ---
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                float x = c * TILE;
                float y = f * TILE;
                if (mapa[f][c] == 1 && paredes[f][c] != null) {
                    paredes[f][c].render(batch);
                } else if (pisos[f][c] != null) {
                    pisos[f][c].render(batch);
                }
                if (mapa[f][c] == 2) {
                    batch.draw(puertaTex, x, y);
                }
            }
        }

        // Dibujar Thanos
        thanos.render(batch);

        // Dibujar jugador
        jugador.render(batch);

        // --- Dibujar mensaje ENTER centrado ---
        GlyphLayout layout = new GlyphLayout();
        batch.end();
        batch.begin();

        if (enRangoThanos && !dialogoActivo) {
            layout.setText(font, "ENTER");
            font.draw(batch, "ENTER", thanos.getX() + TILE / 2 - layout.width / 2, thanos.getY() + TILE + 10);
        }
        if (enRangoPuerta) {
            layout.setText(font, "ENTER");
            font.draw(batch, "ENTER", px + TILE / 2 - layout.width / 2, py + TILE + 10);
        }
        batch.end();

        // --- Render overlay de diálogo centrado abajo ---
        if (dialogoActivo) {
            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
            shapeRenderer.rect(0, 0, COLUMNAS * TILE, DIALOG_HEIGHT);
            shapeRenderer.end();

            batch.begin();
            layout.setText(font, textoThanos);
            font.draw(batch, textoThanos, COLUMNAS * TILE / 2 - layout.width / 2, DIALOG_HEIGHT - 15);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
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
        batch.dispose();
        shapeRenderer.dispose();
        puertaTex.dispose();
        jugador.dispose();
        thanos.dispose();
        font.dispose();
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                if (paredes[f][c] != null) {
                    paredes[f][c].dispose();
                }
                if (pisos[f][c] != null) {
                    pisos[f][c].dispose();
                }
            }
        }
    }
}
