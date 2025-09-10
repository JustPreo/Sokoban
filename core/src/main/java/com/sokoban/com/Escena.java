package com.sokoban.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Base.IntentoDeMenu.MenuScreen;
import java.util.Timer;
import java.util.TimerTask;

public class Escena implements Screen {

    private Stage stage;
    private Juegito game;
    private BitmapFont font;
    private Label current;
    private String[] storyLines;
    private int currentIndex = 0;
    private Label[] labels;
    private LabelStyle style;
    private Texture textura;
    private Sprite sprite;

    public Escena(Juegito game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        stage.clear();
        createPixelFont(64);
        //textura = new Texture("caja.png");
        //sprite = new Sprite(textura);
        //sprite.setSize(256, 256);
        //sprite.setPosition(150, 300);

        style = new LabelStyle(font, Color.WHITE);

        storyLines = new String[]{
            "En un futuro no muy lejano...",
            "Thanos esta en la busqueda \nde todas las gemas del infinito...",
            "Pero necesitara un ayudante \npara organizarlas, y ese eres tu.",
            "Ayúdalo y tal vez recibas una recompensa... \n(Una buena nota)"
        };
        labels = new Label[storyLines.length];
        for (int i = 0; i < storyLines.length; i++) {
            labels[i] = new Label(storyLines[i], style);
            labels[i].setPosition(50, 300);//x , y
        }
        Timer timer = new Timer();
        

        // Schedule a task to run after 2 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                inicio();
                timer.cancel(); // Stops the timer after execution
            }
        }, 2000); // Delay in milliseconds
    }
    

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // fondo negro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            currentIndex++;
            if (currentIndex < storyLines.length) {
                labels[currentIndex - 1].remove();
                stage.addActor(labels[currentIndex]);
                
            } else {
                game.setScreen(new MenuScreen());
                dispose();
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    public void inicio()
    {
     stage.addActor(labels[0]);
        Label lblEnter = new Label("Enter para continuar...",style);
        lblEnter.setPosition(200, 20);
        stage.addActor(lblEnter);
        
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    private void createPixelFont(int TILE) {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                    Gdx.files.internal("fonts/PressStart2P-vaV7.ttf")
            );
            FreeTypeFontGenerator.FreeTypeFontParameter par = new FreeTypeFontGenerator.FreeTypeFontParameter();

            par.size = Math.max(12, TILE / 4);
            par.color = Color.WHITE;
            par.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
            par.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
            par.borderWidth = 1;
            par.borderColor = Color.BLACK;

            font = generator.generateFont(par);
            generator.dispose();
        } catch (Exception e) {
            System.err.println("Error cargando fuente :" + e.getMessage());
            font = new BitmapFont();
            font.getRegion().getTexture().setFilter(
                    com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest,
                    com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest
            );
            font.setColor(Color.WHITE);
            font.getData().setScale(TILE * 0.025f);
        }
    }
}
