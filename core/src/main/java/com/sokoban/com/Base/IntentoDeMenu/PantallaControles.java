/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.ConfiguracionJuego;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;

import java.util.HashMap;
import java.util.Map;

public class PantallaControles implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private ConfiguracionJuego config;
    private Idiomas idiomas;

    private Map<String, Label> labelsControles; // Labels que muestran la tecla actual
    private String accionActual = null; // Acción que se está reasignando
    private Map<Button, String> textosOriginales; // 

    public PantallaControles() {
        config = ConfiguracionJuego.getInstance();
        idiomas = Idiomas.getInstance();
        labelsControles = new HashMap<>();
        textosOriginales = new HashMap<>();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        bg = new Texture("fondoM.png");

        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.top().padTop(20);

        Label titulo = new Label(idiomas.obtenerTexto("controles.titulo"), skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        Table tablaControles = new Table();
        tablaControles.top();

        // Crear filas para cada control
        for (String accion : config.getControles().keySet()) {
            if (accion.equalsIgnoreCase("arribaAlt") || accion.equalsIgnoreCase("abajoAlt") || accion.equalsIgnoreCase("izquierdaAlt") || accion.equalsIgnoreCase("derechaAlt")) {
                continue; // No mostrar en la UI
            }
            Table fila = new Table();
            Label labelAccion = new Label(accion, skin);
            labelAccion.setFontScale(0.8f);

            Label labelTecla = new Label(Input.Keys.toString(config.getControl(accion)), skin);
            labelTecla.setFontScale(0.8f);
            labelsControles.put(accion, labelTecla);

            Button btnCambiar = new Button(skin);
            Label labelBtn = new Label(idiomas.obtenerTexto("controles.cambiar"), skin);
            btnCambiar.add(labelBtn);
            textosOriginales.put(btnCambiar, labelBtn.getText().toString());
            btnCambiar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    accionActual = accion;
                    labelBtn.setText("Presiona tecla...");
                }
            });

            fila.add(labelAccion).width(150).left();
            fila.add(labelTecla).width(100).padLeft(10);
            fila.add(btnCambiar).width(120).padLeft(10);
            tablaControles.add(fila).padBottom(5).row();
        }

        // Botón para volver
        Button btnVolver = new Button(skin);
        Label labelVolver = new Label(idiomas.obtenerTexto("controles.volver"), skin);
        btnVolver.add(labelVolver);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new PantallaSettings());
            }
        });

        tablaPrincipal.add(tablaControles).padBottom(20).row();
        tablaPrincipal.add(btnVolver).size(120, 45).padTop(10);
        stage.addActor(tablaPrincipal);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // Detectar input para reasignar controles
        if (accionActual != null) {
            for (int i = 0; i < 256; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    config.setControl(accionActual, i);
                    labelsControles.get(accionActual).setText(Input.Keys.toString(i));
                    Button btn = (Button) labelsControles.get(accionActual).getParent().getChildren().get(2);
                    // Restaurar texto original del botón
                    Table fila = (Table) labelsControles.get(accionActual).getParent();
                    Button btnCambiar = (Button) fila.getChildren().get(2); // tercer actor de la fila
                    Label labelBtn = (Label) btnCambiar.getChild(0);
                    labelBtn.setText(textosOriginales.get(btnCambiar));
                    accionActual = null;
                    break;
                }
            }
        }
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
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (bg != null) {
            bg.dispose();
        }
    }
}
