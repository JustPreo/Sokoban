/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author user
 */
public class Objetivo {
    Texture txt;
    Sprite sprite;
    Rectangle hitbox;
    
    public Objetivo(float x , float y,float TILE)
    {
    txt = new Texture("objetivo.png");
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
        if (txt != null) txt.dispose();
    }
    
}
