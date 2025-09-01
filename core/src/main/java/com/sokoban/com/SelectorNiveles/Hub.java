/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.SelectorNiveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.Base.Jugador;
import com.sokoban.com.Base.Pared;
import com.sokoban.com.IntentoLvl1.Lvl1;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Hub implements Screen {

    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    protected Jugador jogador;

    protected int FILAS = 8; //Elegir las filas manuales
    protected int COLUMNAS = 14;//Elegir las columnas manuales
    protected int TILE = 800 / COLUMNAS;
    protected int vecesEmpujado = 0;
    protected ArrayList<padNiveles> pads = new ArrayList<>();
    ArrayList<Pared> paredes = new ArrayList<>();

    protected int jugadorX = 2, jugadorY = 4;
    

    private int[][] mapa = { // Tomar en cueta que esta inverso el mapa
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, //0
        {1, 2, 0, 1, 3, 0, 1, 4, 0, 1, 0, 5, 0, 1}, //1
        {1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1}, //2
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, //3
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, //4
        {1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1}, //5
        {1, 6, 0, 1, 7, 0, 1, 8, 0, 1, 0, 0, 0, 1}, //6
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};//7
    //0  1  2  3  4  5  6  7  8  9  10 11  12  13 
    //2 = lvl 1 | 3 = lvl 2 | 4 = lvl 3 | 5 = lvl 4 | 6 = lvl 5 | 7 = lvl 6 | 8 = lvl 7 |
    
    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(COLUMNAS * TILE, FILAS * TILE);

        jogador = new Jugador(jugadorX * TILE, jugadorY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight(), TILE);
        
        //niveles.add(new Lvl1());
        spawnearNiveles();
    }

    @Override
    public void render(float f) {
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
        
        //Dibujar sprites
        spriteBatch.begin();
        for (padNiveles pad : pads)
        {
        pad.render(spriteBatch);
        }
        jogador.render(spriteBatch);
        
        for (Pared pared : paredes)
        {
        pared.render(spriteBatch);
        }
        spriteBatch.end();
        
    }

    @Override
    public void resize(int width, int height) {
        if (width > 0 && height > 0) {
            viewport.update(width, height, true);
        }
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
        spriteBatch.dispose();
        jogador.dispose();
    }

    private void moverJugador(int dx, int dy) {
        int nuevoX = jugadorX + dx;
        int nuevoY = jugadorY + dy;

        // pared?
        if (mapa[nuevoY][nuevoX] == 1) {
            return;
        }
        
        
        //Despues hacer la logica de colision con nivel 
        jugadorX = nuevoX;
        jugadorY = nuevoY;

        // actualizar pos
        jogador.setPos(jugadorX * TILE, jugadorY * TILE);
        for (padNiveles pad : pads)
        {
        if (jogador.getHitbox().overlaps(pad.getHitbox()))
        {
            System.out.println(pad.getNivel());
            pad.irANivel();
        }
        }
        
    }
    
    private void spawnearNiveles()
    {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                if (mapa[f][c] == 1)
                {
                    paredes.add(new Pared(c * TILE,f * TILE,TILE));
                }
                if (mapa[f][c] != 1 && mapa[f][c] != 0)
                {
                pads.add(new padNiveles(c * TILE,f * TILE,TILE,(mapa[f][c]-1)));
                }
                
            }
        }
    
    }

}


