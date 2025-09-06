/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
   private volatile boolean up, down, left, right;  //variables compartidas entre múltiples hilos.
    

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
        
        Thread inputThread = new Thread(() -> {
            while (true) {
                // El "consume" en el otro hilo 
                up = Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
                down = Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
                left = Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
                right = Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

                /*try {
                    Thread.sleep(10); // Talvez funciona asi?
                } catch (InterruptedException ignored) {}*/
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
    }
    
    public void revisarInput()
    {
    if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            //(0, 1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)|| Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            //(0, -1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)|| Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            //(-1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)|| Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            //(1, 0);
        }
    
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
    
    public float getX()
    {
    return personaje.getX();
    }
    
    public float getY()
    {
    return personaje.getY();
    }
    
    // Métodos para "consumir" los inputs
    public boolean consumeUp() {
        if (up) { 
            up = false; 
            return true; 
        }
        return false;
    }

    public boolean consumeDown() {
        if (down) { 
            down = false; 
            return true; 
        }
        return false;
    }

    public boolean consumeLeft() {
        if (left) { 
            left = false; 
            return true; 
        }
        return false;
    }

    public boolean consumeRight() {
        if (right) { 
            right = false; 
            return true; 
        }
        return false;
    }


}
