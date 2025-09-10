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
import com.sokoban.com.IntentoLvl1.Lvl1;
import com.sokoban.com.Juegito;
import com.sokoban.com.Tutorialez.Tutorial;

/**
 *
 * @author user
 */
public class padNiveles {
    
    Texture txt;
    Sprite sprite;
    Rectangle hitbox;
    int nivel;
    
    public padNiveles(float x , float y,float TILE,int nivel)
    {
    txt = new Texture("objetivo.png");
    sprite = new Sprite(txt);
    sprite.setSize(TILE, TILE);
    sprite.setPosition(x, y);
    hitbox = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    this.nivel = nivel;
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
    public void irANivel()
    {
        switch(nivel)
        {
            case 1:
                System.out.println("Nivel 1");
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Tutorial());
                break;
            case 2:
                System.out.println("Nivel 2");
                break;
            case 3:
                System.out.println("Nivel 3");
                break;
            case 4:
                System.out.println("Nivel 4");
                break;
            case 5:
                System.out.println("Nivel 5");
                break;
            case 6:
                System.out.println("Nivel 6");
                break;
            case 7:
                System.out.println("Nivel 7");
                break;
        
        }
    }
    
    public int getNivel()
    {
    return nivel;
    }
    
}
