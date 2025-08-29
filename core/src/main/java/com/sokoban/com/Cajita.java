/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com;

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
    Sprite caja;
    Rectangle hitbox;
    float timer;
    
    public Cajita(float x , float y)
    {
    text = new Texture("cajita.png");
    caja = new Sprite(text);
    caja.setSize(2, 2);
    caja.setPosition(x, y);
    hitbox = new Rectangle(x,y,caja.getWidth(),caja.getHeight());
    }
    
    
    public void update()
    {
    hitbox.setPosition(caja.getX(), caja.getY()); 
    }
    public void render(SpriteBatch batch) {
        caja.draw(batch);
    }
    
    public Rectangle getHitbox()
    {
    return hitbox;
    }
    
    public void mover(float x , float y)
    {
       
        caja.setPosition(caja.getX() + x, caja.getY() + y);
        
        
    
    }
    
    
}
