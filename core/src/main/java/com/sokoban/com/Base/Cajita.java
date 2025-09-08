package com.sokoban.com.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

public class Cajita {
    
    Texture text;
    Texture text2;
    Sprite caja;
    Rectangle hitbox;
    
    // Variables para animación simple
    private float posInicialX, posInicialY;
    private float posObjetivoX, posObjetivoY;
    private boolean animando;
    private float tiempoAnimacion;
    private float duracionAnimacion = 0.2f; // Un poco más lenta que el jugador
    
    public Cajita(float x, float y, float TILE) {
        text = new Texture("caja.png");
        text2 = new Texture("cajaInmovible.png");
        caja = new Sprite(text);
        caja.setSize(TILE, TILE);
        
        // Inicializar posiciones
        posInicialX = posObjetivoX = x;
        posInicialY = posObjetivoY = y;
        caja.setPosition(x, y);
        
        hitbox = new Rectangle(x, y, caja.getWidth(), caja.getHeight());
    }
    
    public void update() {
        // Solo animar si está en movimiento
        if (animando) {
            tiempoAnimacion += Gdx.graphics.getDeltaTime();
            float progreso = Math.min(tiempoAnimacion / duracionAnimacion, 1.0f);
            
            // Interpolación suave
            float factor = Interpolation.smooth.apply(progreso);
            
            // Calcular posición actual
            float x = posInicialX + (posObjetivoX - posInicialX) * factor;
            float y = posInicialY + (posObjetivoY - posInicialY) * factor;
            
            caja.setPosition(x, y);
            
            // Terminar animación
            if (progreso >= 1.0f) {
                animando = false;
                caja.setPosition(posObjetivoX, posObjetivoY);
            }
        }
        
        // Actualizar hitbox
        hitbox.setPosition(caja.getX(), caja.getY());
    }
    
    public void render(SpriteBatch batch) {
        caja.draw(batch);
    }
    
    public Rectangle getHitbox() {
        return hitbox;
    }
    
    public void setPos(float x, float y) {
        if (!animando) {
            posInicialX = caja.getX();
            posInicialY = caja.getY();
            posObjetivoX = x;
            posObjetivoY = y;
            animando = true;
            tiempoAnimacion = 0f;
        }
    }
    
    public boolean estaAnimando() {
        return animando;
    }
    
    public void dispose() {
        if (text != null) text.dispose();
        if (text2 != null) text2.dispose();
    }
}