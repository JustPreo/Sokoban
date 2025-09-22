package com.sokoban.com.Base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Thanos {

    private float x, y;
    private int size;
    private Animation<TextureRegion> animacion;
    private float stateTime;
    private boolean animando;

    public Thanos(float x, float y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.stateTime = 0f;
        this.animando = true;

        // Cargar texturas directamente en la clase
        Texture[] frames = new Texture[3];
        frames[0] = new Texture("thanosGIF1.png");
        frames[1] = new Texture("thanosGIF2.png");
        frames[2] = new Texture("thanosGIF3.png");

        TextureRegion[] texFrames = new TextureRegion[frames.length];
        for (int i = 0; i < frames.length; i++) {
            texFrames[i] = new TextureRegion(frames[i]);
        }

        this.animacion = new Animation<>(0.3f, texFrames);
    }

    public void update(float delta) {
        if (animando) {
            stateTime += delta;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion frame = animacion.getKeyFrame(stateTime, true);
        batch.draw(frame, x, y, size, size);
    }

    public void setPos(float x, float y) { this.x = x; this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }

    public void detener() { animando = false; }
    public void iniciar() { animando = true; }

    public void dispose() {
        for (TextureRegion tr : animacion.getKeyFrames()) {
            tr.getTexture().dispose();
        }
    }
}
