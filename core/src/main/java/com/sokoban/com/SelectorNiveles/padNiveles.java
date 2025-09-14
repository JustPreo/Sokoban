/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.SelectorNiveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sokoban.com.Niveles.Lvl1;
import com.sokoban.com.Juegito;
import com.sokoban.com.Niveles.Lvl2;
import com.sokoban.com.Niveles.Lvl3;

/**
 *
 * @author user
 */
public class padNiveles {

    Texture txt;
    Sprite sprite;
    Rectangle hitbox;
    int nivel;

    public padNiveles(float x, float y, float TILE, int nivel) {
        this.nivel = nivel;
        txt = switch (nivel) {
            case 1 ->
                new Texture("portales/tp1.png");
            case 2 ->
                new Texture("portales/tp2.png");
            case 3 ->
                new Texture("portales/tp3.png");
            case 4 ->
                new Texture("portales/tp4.png");
            case 5 ->
                new Texture("portales/tp5.png");
            case 6 ->
                new Texture("portales/tp6.png");
            case 7 ->
                new Texture("portales/tp7.png");
            default ->
                new Texture("tp.png");
        }; // por si acaso
        sprite = new Sprite(txt);
        sprite.setSize(TILE, TILE);
        sprite.setPosition(x, y);
        hitbox = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());

    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void dispose() {
        if (txt != null) {
            txt.dispose();
        }
    }

    public void irANivel() {
        switch (nivel) {
            case 1 -> {
                System.out.println("Nivel 1");
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Lvl1());
            }
            case 2 -> {
                System.out.println("Nivel 2");
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Lvl2());
            }
            case 3 -> {
                System.out.println("Nivel 3");
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Lvl3());
            }
            case 4 ->
                System.out.println("Nivel 4");
            case 5 ->
                System.out.println("Nivel 5");
            case 6 ->
                System.out.println("Nivel 6");
            case 7 ->
                System.out.println("Nivel 7");

        }
    }

    public int getNivel() {
        return nivel;
    }

}
