package com.sokoban.com.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

public class Jugador {

    Texture per;
    Sprite personaje;
    Rectangle hitbox;
    float maxW, maxH;
    private volatile boolean up, down, left, right;
    
    // animation
    private float posInicialX, posInicialY;
    private float posObjetivoX, posObjetivoY;
    private boolean animando;
    private float tiempoAnimacion;
    private float duracionAnimacion = 0.15f; // 150ms

    public Jugador(float x, float y, float width, float height, float TILE) {
        per = new Texture("personaje.png");
        personaje = new Sprite(per);
        personaje.setSize(TILE, TILE);
        
        // Inicializar posiciones
        posInicialX = posObjetivoX = x;
        posInicialY = posObjetivoY = y;
        personaje.setPosition(x, y);
        
        maxW = width - personaje.getWidth();
        maxH = height - personaje.getHeight();
        hitbox = new Rectangle(x, y, personaje.getWidth(), personaje.getHeight());
        
        Thread inputThread = new Thread(() -> {
            while (true) {
                up = Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
                down = Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
                left = Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
                right = Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
    }

    public void update() {
        // si animando entonces
        if (animando) {
            tiempoAnimacion += Gdx.graphics.getDeltaTime();
            float progreso = Math.min(tiempoAnimacion / duracionAnimacion, 1.0f);
            
            // Interpolación suave
            float factor = Interpolation.smooth.apply(progreso);
            
            // Calcular pos actual
            float x = posInicialX + (posObjetivoX - posInicialX) * factor;
            float y = posInicialY + (posObjetivoY - posInicialY) * factor;
            
            personaje.setPosition(x, y);
            
            // Terminar anim
            if (progreso >= 1.0f) {
                animando = false;
                personaje.setPosition(posObjetivoX, posObjetivoY);
            }
        }
        
        // Actualizar hitbox
        hitbox.setPosition(personaje.getX(), personaje.getY());
    }
    
    public void setPos(float x, float y) {
        if (!animando) {
            posInicialX = personaje.getX();
            posInicialY = personaje.getY();
            posObjetivoX = x;
            posObjetivoY = y;
            animando = true;
            tiempoAnimacion = 0f;
        }
    }
    
    public boolean estaAnimando() {
        return animando;
    }

    public void render(SpriteBatch batch) {
        personaje.draw(batch);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
    
    public void dispose() {
        if (per != null) per.dispose();
    }
    
    public float getX() {
        return personaje.getX();
    }
    
    public float getY() {
        return personaje.getY();
    }

    //(solo si no esta animando)
    public boolean consumeUp() {
        if (up && !animando) { 
            up = false; 
            return true; 
        }
        if (up) up = false; // Consumir input aunque esté animando
        return false;
    }

    public boolean consumeDown() {
        if (down && !animando) { 
            down = false; 
            return true; 
        }
        if (down) down = false;
        return false;
    }

    public boolean consumeLeft() {
        if (left && !animando) { 
            left = false; 
            return true; 
        }
        if (left) left = false;
        return false;
    }

    public boolean consumeRight() {
        if (right && !animando) { 
            right = false; 
            return true; 
        }
        if (right) right = false;
        return false;
    }
}