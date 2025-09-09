/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.IntentoLvl1.Lvl1;
import com.sokoban.com.Juegito;
import com.sokoban.com.SelectorNiveles.Hub;
import java.util.ArrayList;

/**
 *
 * @author user
 */

/*
Para estilizarlos botones

Texture textura = new Texture(Gdx.files.internal("fondo_btn.png")); esto es carpeta assets
Drawable fondo = new TextureRegionDrawable(new TextureRegion(textura)); dibuja la textura

TextButton.TextButtonStyle estilo = new TextButton.TextButtonStyle(); se lo pone a textbutton como estilo
estilo.up = fondo; estado normal
estilo.down = fondo;  estado presionado , podemos usar otra textura talvez?
estilo.font = new BitmapFont(); fuente para el texto
estilo.fontColor = Color.WHITE; el color de la font

 */
public class MenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    public static int dificultad = 1;//Inicia en facil
    private boolean puedeInteractuar = true;
    //

    public MenuScreen() {
        //Textura de botones (Ahorita esas imagenes son placeholders)
        //OFF
        Texture texturaJugar = new Texture(Gdx.files.internal("jugar.png"));
        Drawable fondoJugar = new TextureRegionDrawable(new TextureRegion(texturaJugar));
        //ON
        Texture texturaJugar2 = new Texture(Gdx.files.internal("jugar2.png"));
        Drawable fondoJugar2 = new TextureRegionDrawable(new TextureRegion(texturaJugar2));
        
        Button.ButtonStyle estiloJugar = new Button.ButtonStyle();
        estiloJugar.up = fondoJugar;
        estiloJugar.down = fondoJugar2;
        //----------------------------------------------------------------------------------------
        Texture texturaExtra = new Texture(Gdx.files.internal("extra.png"));
        Drawable fondoExtra = new TextureRegionDrawable(new TextureRegion(texturaExtra));
        //
        Texture texturaExtra2 = new Texture(Gdx.files.internal("extra2.png"));
        Drawable fondoExtra2 = new TextureRegionDrawable(new TextureRegion(texturaExtra2));
        
        Button.ButtonStyle estiloDificultades = new Button.ButtonStyle();
        estiloDificultades.up = fondoExtra;
        estiloDificultades.down = fondoExtra2;
        //----------------------------------------------------------------------------------------

        Texture texturaSalir = new Texture(Gdx.files.internal("salir.png"));
        Drawable fondoSalir = new TextureRegionDrawable(new TextureRegion(texturaSalir));
        //
        Texture texturaSalir2 = new Texture(Gdx.files.internal("salir2.png"));
        Drawable fondoSalir2 = new TextureRegionDrawable(new TextureRegion(texturaSalir2));
        
        Button.ButtonStyle estiloSalir = new Button.ButtonStyle();
        estiloSalir.up = fondoSalir;
        estiloSalir.down = fondoSalir2;
        //----------------------------------------------------------------------------------------

        //Resto de cosas
        stage = new Stage(new ScreenViewport());

        // Capturar input para que los botones reciban clics
        Gdx.input.setInputProcessor(stage);

        //Esto de skin es como los fonts y botones (Por defecto , despues se intentan cambiar)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        bg = new Texture("fondoM.png");

        // Crear elementos UI
        Label titulo = new Label("Sokoban", skin);//Skin es como obligatorio para esto
        Button btnJugar = new Button(estiloJugar);
        Button btnSalir = new Button(estiloSalir);

        Button btnExtra = new Button(estiloDificultades);

        // Listeners (acciones al presionar botones)
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click en JUGAR");
                if (puedeInteractuar) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new Hub());
                }
                dispose();

                //Los niveles ahora tendran que ser "Screen" para que funcione bien el code
            }
        });
        btnExtra.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                System.out.println("Dificultad");
                if (puedeInteractuar) {
                    System.out.println("Extra");
                }

            }

        });

        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    //dispose();
                    //Gdx.app.exit();
                    System.exit(0);
                    
                }

            }
        });

        // Layout con Table
        Table table = new Table();
        table.setFillParent(true); // ocupa toda la pantalla
        table.center();            // centrado

        // Agregamos elementos con separaciones
        table.add(titulo).padBottom(40).row();   // texto arriba
        table.add(btnJugar).size(200, 60).padBottom(20).row();
        table.add(btnExtra).size(200, 60).padBottom(20).row();
        table.add(btnSalir).size(200, 60).row();

        //Como el grid layout
        // Agregar a Stage
        stage.addActor(table);//Sinceramente no entiendo bien esto para abajo
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Dibujar fondo
        stage.getBatch().begin();
        stage.getBatch().draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
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

    public void setDificultad(int dif) {
        dificultad = dif;
        //1 = facil , 2 = mediano, 3 = dificil
    }

    /*private void mostrarSelectorDificultad() {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.5f)));
        overlay.center();

        // Panel central
        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel).width(stage.getWidth() * 0.6f).height(stage.getHeight() * 0.5f);

        // Título
        Label titulo = new Label("Selecciona Dificultad", skin);
        panel.add(titulo).padBottom(20).row();

        // Botones de dificultad
        TextButton btnFacil = new TextButton("Facil", skin);
        TextButton btnNormal = new TextButton("Normal", skin);
        TextButton btnDificil = new TextButton("Dificil", skin);

        // Acciones de los botones
        btnFacil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Dificultad: Facil");
                overlay.remove(); // cerrar popup
                setDificultad(1);
                puedeInteractuar = true;
            }
        });

        btnNormal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Dificultad: Normal");
                overlay.remove();
                setDificultad(2);
                puedeInteractuar = true;
            }
        });

        btnDificil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Dificultad: Dificil");
                overlay.remove();
                setDificultad(3);
                puedeInteractuar = true;
            }
        });

        // Agregar botones al panel
        panel.add(btnFacil).size(150, 50).padBottom(10).row();
        panel.add(btnNormal).size(150, 50).padBottom(10).row();
        panel.add(btnDificil).size(150, 50).row();

        // Agregar overlay al stage
        stage.addActor(overlay);
    }*/ 
    //Resulta que no hay que elegir dificultad :(
    //Por si acaso dejo eso ahi por si lo uso despue en algun otro lado

    /*private void disableBotones() {
        for (TextButton but : botones) {
            but.setDisabled(!puedeInteractuar);
            System.out.println(but.getDebug());
        }
    }*/
    //No funciono pero puede funcionar a futuro?
}
