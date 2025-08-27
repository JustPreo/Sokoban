package com.sokoban.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private Sprite personaje;
    private FitViewport viewport;
    private Texture personajeT;

    @Override
    public void create() {
        
        spriteBatch = new SpriteBatch();
    viewport = new FitViewport(8, 5);//No se que hace aun
    personajeT = new Texture("player.png");
    }

    @Override
    public void render() {
        spriteBatch.begin();
        
        
        
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {

        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) {
            return;
        }

        viewport.update(width, height, true); // true centers the camera
        // Resize your application here. The parameters represent the new window size.
    }

    
}
