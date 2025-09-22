package com.sokoban.com.Base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.sokoban.com.Juegito;
import com.sokoban.com.ThanosRoom;

public class Puerta {

    private float x, y;
    private float size;
    private Texture textura;
    private Rectangle hitbox;
    private boolean esThanosRoom; // true si es la puerta especial a ThanosRoom

    public Puerta(float x, float y, float size, boolean esThanosRoom) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.esThanosRoom = esThanosRoom;
        this.textura = new Texture(Gdx.files.internal("puerta.png"));
        this.hitbox = new Rectangle(x, y, size, size);
    }

    public void render(SpriteBatch batch) {
        batch.draw(textura, x, y, size, size);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean interactuarConJugador(Rectangle jugadorHitbox) {
        if (hitbox.overlaps(jugadorHitbox)) {
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)
                    || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
                if (esThanosRoom) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new ThanosRoom());
                }
                return true;
            }
        }
        return false;
    }

    public void dispose() {
        textura.dispose();
    }
    public boolean isThanos(){
            return esThanosRoom;}
}