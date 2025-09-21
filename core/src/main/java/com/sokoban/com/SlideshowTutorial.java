/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Tutorialez.Tutorial;

public class SlideshowTutorial implements Screen {

    private Stage stage;
    private Juegito game;
    private Texture[] images;
    private Image imageActor;
    private Label textLabel;
    private BitmapFont font;
    private int currentIndex = 0;
    private float displayTime = 3f;
    private float timer = 0f;
    private Idiomas idiomas;

    public SlideshowTutorial(Juegito game, Idiomas idiomas1) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        idiomas = Idiomas.getInstance();
        idiomas.cargarIdiomaUsuario(); // cargar idioma actual

        images = new Texture[]{
            new Texture("tutorial1.png"),
            new Texture("tutorial2.png"),
            new Texture("tutorial3.png"),
            new Texture("tutorial4.png")
        };

        // Fuente y label para el texto
        font = new BitmapFont();
        font.getData().setScale(2f);
        textLabel = new Label("", new LabelStyle(font, Color.WHITE));
        textLabel.setPosition(50, 50);

        // Imagen inicial
        imageActor = new Image(images[currentIndex]);
        imageActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(imageActor);
        stage.addActor(textLabel);
    }

    private String obtenerTextoUltimoSlide() {
        String[] keys = new String[]{
            "tutorial.movimiento", // "Usa W A S D para moverte"
            "tutorial.empujar", // "Empuja las cajas hacia los objetivos"
            "tutorial.evitar", // "Evita quedar atrapado"
            "tutorial.resolver" // "Resuelve todos los niveles para ganar"
        };

        // Última slide: mostrar todo junto
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(idiomas.obtenerTexto(key)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        timer += delta;
        if (timer >= displayTime) {
            timer = 0f;
            currentIndex++;

            if (currentIndex < images.length - 1) {
                // Mostrar solo la imagen actual
                imageActor.setDrawable(new Image(images[currentIndex]).getDrawable());
                textLabel.setVisible(false);
            } else if (currentIndex == images.length - 1) {
                // Última slide: mostrar todo el texto
                imageActor.setDrawable(new Image(images[currentIndex]).getDrawable());
                textLabel.setText(obtenerTextoUltimoSlide());
                textLabel.setVisible(true);
            } else {
                // Terminó slideshow, ir al tutorial
                game.setScreen(new Tutorial());
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
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
        font.dispose();
        for (Texture tex : images) {
            tex.dispose();
        }
    }
}
