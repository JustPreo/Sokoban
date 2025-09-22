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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    private boolean isFadingIn = false;
    private boolean isFadingOut = false;
    private float fadeDuration = 1.0f;

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

        // Imagen inicial con opacidad 0
        imageActor = new Image(images[currentIndex]);
        float imageWidth = Gdx.graphics.getWidth() * 0.6f;
        float imageHeight = Gdx.graphics.getHeight() * 0.6f;
        imageActor.setSize(imageWidth, imageHeight);
        imageActor.setPosition(
                (Gdx.graphics.getWidth() - imageWidth) / 2,
                (Gdx.graphics.getHeight() - imageHeight) / 2
        );
        imageActor.getColor().a = 0; // Opacidad inicial

        // Centrar el Label al inicio
        textLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        textLabel.setWrap(true);
        textLabel.setWidth(Gdx.graphics.getWidth() - 100); // Ancho para centrar

        stage.addActor(imageActor);
        stage.addActor(textLabel);

        // Inicia el primer fade in
        imageActor.addAction(Actions.fadeIn(fadeDuration));
        isFadingIn = true;
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

        if (!imageActor.hasActions()) {
            timer += delta;
            if (timer >= displayTime) {
                timer = 0f;
                currentIndex++;

                if (currentIndex < images.length) {
                    if (currentIndex == images.length - 1) {
                        // Última imagen: opacidad 0, mostrar texto
                        imageActor.getColor().a = 0f;
                        textLabel.setText(obtenerTextoUltimoSlide());
                        textLabel.setVisible(true);
                        textLabel.setWidth(Gdx.graphics.getWidth() * 0.8f);
                        textLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
                        textLabel.setWrap(true);
                        textLabel.setPosition(
                                (Gdx.graphics.getWidth() - textLabel.getWidth()) / 2,
                                (Gdx.graphics.getHeight() - textLabel.getHeight()) / 2
                        );
                    } else {
                        // Primeras 3 imágenes: fade out → cambiar → fade in
                        textLabel.setVisible(false);
                        imageActor.addAction(Actions.sequence(
                                Actions.fadeOut(fadeDuration),
                                Actions.run(() -> {
                                    imageActor.setDrawable(new Image(images[currentIndex]).getDrawable());
                                    imageActor.addAction(Actions.fadeIn(fadeDuration));
                                })
                        ));
                    }
                } else {
                    // No hay más imágenes → pasar a Tutorial
                    game.setScreen(new Tutorial());
                    dispose();
                }
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
