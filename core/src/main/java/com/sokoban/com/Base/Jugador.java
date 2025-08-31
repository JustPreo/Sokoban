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
public class Jugador {

    Texture per;
    Sprite personaje;
    Rectangle hitbox;
    final float velocidad = 4f;
    float maxW, maxH;
    float timerCaminar;

    public Jugador(float x, float y, float width, float height,float TILE) {
        per = new Texture("personaje.png");
        personaje = new Sprite(per);
        personaje.setSize(TILE, TILE);//ANCHO ALTO , unidades mundo
        personaje.setPosition(x, y);//Ponerle la posicion en x y y que da 
        
        maxW = width - personaje.getWidth();
        maxH = height - personaje.getHeight();
        //No me acuerdo para que usaba esto la verdad

        //Igualar la hitbox con la del sprite
        hitbox = new Rectangle(x, y, personaje.getWidth(), personaje.getHeight());
    }

    //Creo un metodo update para recibir inputs
    public void update() {
        //Actualizar la hitbox con cada update
        hitbox.setPosition(personaje.getX(), personaje.getY());
        //Se actualiza la x y la y
    }
    
    public void setPos(float x, float y) {
    personaje.setPosition(x, y);
    hitbox.setPosition(x, y);
}


    public void render(SpriteBatch batch) {
        personaje.draw(batch);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
    public void dispose() {
        if (per!= null) per.dispose();
    }

}
