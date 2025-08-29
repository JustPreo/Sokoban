package com.sokoban.com;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main implements ApplicationListener {

    //Funcionalidades
    SpriteBatch spriteBatch;
    FitViewport viewport;
    ShapeRenderer shape;

    //Objetos
    Cajita caja;
    Jugador jogador;
    Pared wall;

    //Timer
    float timerCaminar;

    //Booleans
    Pared[] paredes;

    @Override
    public void create() {
        // Prepare your application here.
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10);
        shape = new ShapeRenderer();

        //Objetos
        caja = new Cajita(2, 2);
        jogador = new Jugador(4, 2, viewport.getWorldWidth(), viewport.getWorldHeight());//Creo el jugador (La referencia)
        paredes = spawnearParedes(5);

    }

    @Override
    public void resize(int width, int height) {
        if (width > 0 && height > 0) {
            viewport.update(width, height, true);
        }
    }

    @Override
    public void render() {//Esto se repite siempre
        // Draw your application here.
        ScreenUtils.clear(Color.DARK_GRAY);//Actualizar siempre la pantalla
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        //Obligatorios para que funcione bien todo
        float dy = 0, dx = 0;//Mover player
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy = 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy = -2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx = -2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = 2;
        }

        //Calcular la pos del jugador
        Rectangle hitboxRevisar = new Rectangle(
                jogador.getHitbox().x + dx,
                jogador.getHitbox().y + dy,
                jogador.getHitbox().width,
                jogador.getHitbox().height
        );
        boolean puedeMoverse = true;

        for (Pared par : paredes) {
            if (hitboxRevisar.overlaps(par.getHitbox())) {
                puedeMoverse = false;
                //System.out.println("Jogador " + puedeMoverse);
                break;
            }
        }

        caja.update();
        jogador.update();

        if (!hitboxRevisar.overlaps(caja.getHitbox()) && puedeMoverse) {
            jogador.mover(dx, dy);
            if (!puedeMoverse)System.out.println("Entro");//
        } else {
            Rectangle nextCaja = new Rectangle(
                    caja.getHitbox().x + dx,
                    caja.getHitbox().y + dy,
                    caja.getHitbox().width,
                    caja.getHitbox().height
            );

            boolean cajaPuedeMoverse = true;

            for (Pared par : paredes) {
                if (nextCaja.overlaps(par.getHitbox())) {
                    cajaPuedeMoverse = false;//Entra en bucle infinito no se porque
                    System.out.println("Cajita " + cajaPuedeMoverse);
                    break;
                }

            }
            if (cajaPuedeMoverse) {
                if (jogador.mover(dx, dy))
                    
                caja.mover(dx, dy);
                caja.update();
                jogador.update();
            }

        }

        jogador.update();//Revisa el input individual del jugador

        spriteBatch.begin();
        if (paredes != null) {
            for (Pared par : paredes) {
                par.render(spriteBatch);
            }
        }

        //Adentro la logica para los sprites
        jogador.render(spriteBatch);
        caja.render(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }

    public Pared[] spawnearParedes(int cantidad) {
        Pared[] paredes = new Pared[cantidad];
        for (int i = 0; i < cantidad; i++) {
            paredes[i] = new Pared((i * 2), (0));
        }

        return paredes;

    }
}
