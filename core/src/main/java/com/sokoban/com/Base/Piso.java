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
public class Piso {
    Texture textura;
    Sprite sprite;
    Rectangle hitbox;
    public Piso(float x , float y,float TILE)
    {
    textura = new Texture("piso.png");
    sprite = new Sprite(textura);
    sprite.setSize(TILE, TILE);
    sprite.setPosition(x, y);
    hitbox = new Rectangle(x,y,sprite.getWidth(),sprite.getHeight());
    }
    
    public Rectangle getHitbox()
    {
    return hitbox;
    }
    
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
    
    public void update()
    {
    hitbox.setPosition(sprite.getX(),sprite.getY());
    }
    public void dispose() {
        if (textura != null) textura.dispose();
    }
    
}
