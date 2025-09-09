/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

/**
 *
 * @author user
 */
public class Piso {

    Texture textura;
    Sprite sprite;
    Rectangle hitbox;
    boolean pou;

    public Piso(float x, float y, float TILE) {
        textura = new Texture(textura());
        sprite = new Sprite(textura);
        sprite.setSize(TILE, TILE);
        sprite.setPosition(x, y);
        hitbox = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update() {
        hitbox.setPosition(sprite.getX(), sprite.getY());
    }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }

    public String textura() {
        Random r = new Random();
        int numero = r.nextInt(100) + 1; // 1 - 100
        String SText = "";

        // Probabilidades configurables (suma = 100)
        final int PROB_PISO1 = 40; // 40%
        final int PROB_PISO2 = 30; // 30%
        final int PROB_PISO3 = 25; // 25%
        final int PROB_POU = 5;  // 5%

        if (numero <= PROB_PISO1) {
            
            SText = "piso1.png";
            pou = false;
        } else if (numero <= PROB_PISO1 + PROB_PISO2) {
            
            SText = "piso2.png";
            pou = false;
        } else if (numero <= PROB_PISO1 + PROB_PISO2 + PROB_PISO3) {
            
            SText = "piso3.png";
            pou = false;
        } else {
            
            SText = "pisoPou1.png";
            pou = true;
        }

        return SText;
    }

    public void setPouAplastado() {
        if (textura != null) {
            textura.dispose(); // liberar la textura anterior
        }

        textura = new Texture("pisoPou2.png");
        sprite = new Sprite(textura);
        sprite.setSize(hitbox.getWidth(), hitbox.getHeight());
        sprite.setPosition(hitbox.x, hitbox.y);

    }

    public boolean isPou() {
        return pou;
    }

}
