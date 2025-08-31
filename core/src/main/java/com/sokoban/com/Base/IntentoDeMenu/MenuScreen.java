/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.Juegito;

/**
 *
 * @author user
 */
public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;

    public MenuScreen() {
        stage = new Stage(new ScreenViewport());

        // Capturar input para que los botones reciban clics
        Gdx.input.setInputProcessor(stage);

        //Esto de skin es como los fonts y botones (Por defecto , despues se intentan cambiar)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Crear elementos UI
        Label titulo = new Label("Sokoban", skin);//Skin es como obligatorio para esto
        TextButton btnJugar = new TextButton("Jugar", skin);
        TextButton btnSalir = new TextButton("Salir", skin);

        // Listeners (acciones al presionar botones)
        btnJugar.addListener(e -> {
            if (btnJugar.isPressed()) {
                System.out.println("Click en JUGAR");
                 ((Juegito) Gdx.app.getApplicationListener()).setScreen(new JuegoBase());
                 //Los niveles ahora tendran que ser "Screen" para que funcione bien el code
            }
            return true;
        });

        btnSalir.addListener(e -> {
            if (btnSalir.isPressed()) {
                Gdx.app.exit();
            }
            return true;
        });

        // Layout con Table
        Table table = new Table();
        table.setFillParent(true); // ocupa toda la pantalla
        table.center();            // centrado

        // Agregamos elementos con separaciones
        table.add(titulo).padBottom(40).row();   // texto arriba
        table.add(btnJugar).size(200, 60).padBottom(20).row();
        table.add(btnSalir).size(200, 60).row();
        //Como el grid layout

        // Agregar a Stage
        stage.addActor(table);//Sinceramente no entiendo bien esto para abajo
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    public void show() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
