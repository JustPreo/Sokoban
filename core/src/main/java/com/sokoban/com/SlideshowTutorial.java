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
        imageActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

    // Solo aumenta el temporizador si no hay acciones en curso en el actor.
    // Esto asegura que la lógica no se ejecute durante el fade-in o fade-out.
    if (!imageActor.hasActions()) {
        timer += delta;
        if (timer >= displayTime) {
            timer = 0f;

            // Incrementa el índice para la siguiente imagen
            currentIndex++;

            if (currentIndex < images.length) {
                // Comienza el fade out de la imagen actual
                imageActor.addAction(Actions.sequence(
                    Actions.fadeOut(fadeDuration),
                    Actions.run(() -> {
                        // Cuando el fade out termina, cambia la imagen y empieza el fade in
                        imageActor.setDrawable(new Image(images[currentIndex]).getDrawable());
                        imageActor.addAction(Actions.fadeIn(fadeDuration));
                    })
                ));

                // Maneja la visibilidad y el texto del último slide
                if (currentIndex == images.length - 1) {
                    textLabel.setText(obtenerTextoUltimoSlide());
                    textLabel.setVisible(true);
                    textLabel.setPosition((Gdx.graphics.getWidth() - textLabel.getWidth()) / 2, 50);
                } else {
                    textLabel.setVisible(false);
                }
            } else {
                // Si no hay más imágenes, pasa a la siguiente pantalla
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
