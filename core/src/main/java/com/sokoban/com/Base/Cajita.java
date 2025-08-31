/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author user
 */
public class Cajita {

    Texture text;
    Texture text2;
    Sprite caja;
    Rectangle hitbox;
    float timer;
    boolean mover = true;

    public Cajita(float x, float y,float TILE) {
        text = new Texture("caja.png");
        text2 = new Texture("cajaInmovible.png");
        caja = new Sprite(text);
        caja.setSize(TILE, TILE);
        caja.setPosition(x, y);
        hitbox = new Rectangle(x, y, caja.getWidth(), caja.getHeight());
    }

    public void update() {
        
        if (mover){hitbox.setPosition(caja.getX(), caja.getY());}
        if (!mover)
        {
        caja.setTexture(text2);
        }
    }

    public void render(SpriteBatch batch) {
        caja.draw(batch);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setPos(float x, float y) {
        caja.setPosition(x, y);
        hitbox.setPosition(x, y);
    }
    public void dispose() {
        if (text != null) text.dispose();
    }

}
