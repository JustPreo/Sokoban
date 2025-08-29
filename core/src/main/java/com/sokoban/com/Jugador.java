/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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

    public Jugador(float x, float y, float width, float height) {
        per = new Texture("personaje.png");
        personaje = new Sprite(per);
        personaje.setSize(2, 2);//ANCHO ALTO , unidades mundo
        personaje.setPosition(x, y);//Ponerle la posicion en x y y que da 
        maxW = width - personaje.getWidth();
        maxH = height;

        //Igualar la hitbox con la del sprite
        hitbox = new Rectangle(x, y, personaje.getWidth(), personaje.getHeight());
    }

    //Creo un metodo update para recibir inputs
    public void update() {
        //Actualizar la hitbox con cada update
        hitbox.setPosition(personaje.getX(), personaje.getY());
        //System.out.println("X:" + personaje.getX() + "\nY:" + personaje.getY());
        //Se actualiza la x y la y
    }
    
    public boolean mover(float dx, float dy) {
        timerCaminar += Gdx.graphics.getDeltaTime();
        if (timerCaminar > .15f) { // revisar si paso mas de 1s 
                timerCaminar = 0; // Resetear timer 
                personaje.setPosition(personaje.getX() + dx, personaje.getY() + dy);
                return true;
            }
        return false;
    
    }

    public void render(SpriteBatch batch) {
        personaje.draw(batch);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

}
