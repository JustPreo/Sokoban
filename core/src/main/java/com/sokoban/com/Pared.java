/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author user
 */
public class Pared {
    Texture textura;
    Sprite sprite;
    Rectangle hitbox;
    public Pared(float x , float y)
    {
    textura = new Texture("cajita.png");
    sprite = new Sprite(textura);
    sprite.setSize(2, 2);
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
    
    
}
