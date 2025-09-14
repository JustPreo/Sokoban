package com.sokoban.com.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.sokoban.com.SoundManager;

public class Jugador {
    private Texture[][] texturasDirecciones = new Texture[4][]; // [dirección][frame]
    private Animation<TextureRegion>[] animacionesDirecciones = new Animation[4];
    private TextureRegion frameActual;
    
    Rectangle hitbox;
    float maxW, maxH;
    private volatile boolean up, down, left, right;
    private float posInicialX, posInicialY;
    private float posObjetivoX, posObjetivoY;
    private boolean animandoMovimiento;
    private float tiempoAnimacionMovimiento;
    private float duracionAnimacionMovimiento = 0.15f;
    private float tiempoAnimacionSprite = 0f;
    private int direccionActual = 0; // 0=abajo, 1=arriba, 2=derecha, 3=izq
    private int direccionAnterior = 0;
    private boolean estaMoviendo = false;
    
    private float TILE;
    private String[] nombresDirecciones = {"abajo", "arriba", "derecha", "izquierda"};

    public Jugador(float x, float y, float width, float height, float TILE) {
        this.TILE = TILE;
        cargarTexturas();
        crearAnimaciones();
        posInicialX = posObjetivoX = x;
        posInicialY = posObjetivoY = y;
        
        maxW = width - TILE;
        maxH = height - TILE;
        hitbox = new Rectangle(x, y, TILE, TILE);
        
        // Frame inicial
        frameActual = animacionesDirecciones[0].getKeyFrame(0);
        
        Thread inputThread = new Thread(() -> {
            while (true) {
                up = Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
                down = Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
                left = Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
                right = Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
    }
    
    private void cargarTexturas() {
        for (int dir = 0; dir < 4; dir++) {
            String direccion = nombresDirecciones[dir];
            java.util.List<Texture> texturesTemp = new java.util.ArrayList<>();
            
            for (int frame = 1; frame <= 8; frame++) {
                try {
                    String rutaArchivo = "jugador/" + direccion + "/" + direccion + frame + ".png";
                    Texture textura = new Texture(rutaArchivo);
                    texturesTemp.add(textura);
                    System.out.println("Cargado: " + rutaArchivo);
                } catch (Exception e) {
                    
                    System.out.println("No se pudo cargar frame " + frame + " para dir " + direccion);
                    break;
                }
            }
            
            
            
            texturasDirecciones[dir] = texturesTemp.toArray(new Texture[0]);
        }
    }
    
    private void crearAnimaciones() {
        for (int dir = 0; dir < 4; dir++) {
            Texture[] texturasDir = texturasDirecciones[dir];
            
            TextureRegion[] frames = new TextureRegion[texturasDir.length];
            
            for (int i = 0; i < texturasDir.length; i++) {
                frames[i] = new TextureRegion(texturasDir[i]);
            }
            
            //crear animacion
            animacionesDirecciones[dir] = new Animation<TextureRegion>(0.1f, frames);
            animacionesDirecciones[dir].setPlayMode(Animation.PlayMode.LOOP);
        }
    }

    public void update() {
        if (animandoMovimiento) {
            tiempoAnimacionMovimiento += Gdx.graphics.getDeltaTime();
            float progreso = Math.min(tiempoAnimacionMovimiento / duracionAnimacionMovimiento, 1.0f);
            
            float factor = Interpolation.smooth.apply(progreso);
            
            float x = posInicialX + (posObjetivoX - posInicialX) * factor;
            float y = posInicialY + (posObjetivoY - posInicialY) * factor;
            
            if (progreso >= 1.0f) {
                animandoMovimiento = false;
                estaMoviendo = false;
                x = posObjetivoX;
                y = posObjetivoY;
            }
            
            hitbox.setPosition(x, y);
        }
        actualizarAnimacionSprite();
    }
    
    private void actualizarAnimacionSprite() {
        if (estaMoviendo) {
            //avanzar la anim
            tiempoAnimacionSprite += Gdx.graphics.getDeltaTime();
        } else {
            //usar el frame 0
            tiempoAnimacionSprite = 0f;
        }
        
        // Obtener el frame actual 
        frameActual = animacionesDirecciones[direccionActual].getKeyFrame(tiempoAnimacionSprite);
    }
    
    public void setPos(float x, float y) {
        if (!animandoMovimiento) {
            posInicialX = hitbox.x;
            posInicialY = hitbox.y;
            posObjetivoX = x;
            posObjetivoY = y;
            animandoMovimiento = true;
            estaMoviendo = true;
            tiempoAnimacionMovimiento = 0f;
            tiempoAnimacionSprite = 0f; // Reiniciar anim
            SoundManager.playCaminar(1.0f);//Agregar aqui sonido de nad
        }
    }
    
    public void setDireccion(int nuevaDireccion) {
        if (nuevaDireccion != direccionActual) {
            direccionAnterior = direccionActual;
            direccionActual = nuevaDireccion;
            tiempoAnimacionSprite = 0f; // Reiniciar 
        }
    }
    
    public boolean estaAnimando() {
        return animandoMovimiento;
    }

    public void render(SpriteBatch batch) {
        float x = hitbox.x;
        float y = hitbox.y;
        
        batch.draw(frameActual, x, y, TILE, TILE);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
    
    public void dispose() {
        for (int dir = 0; dir < 4; dir++) {
            if (texturasDirecciones[dir] != null) {
                for (Texture textura : texturasDirecciones[dir]) {
                    if (textura != null) {
                        textura.dispose();
                    }
                }
            }
        }
    }
    
    public float getX() {
        return hitbox.x;
    }
    
    public float getY() {
        return hitbox.y;
    }
    public boolean consumeUp() {
        if (up && !animandoMovimiento) { 
            up = false;
            setDireccion(1); // arriba
            return true; 
        }
        if (up) up = false;
        return false;
    }

    public boolean consumeDown() {
        if (down && !animandoMovimiento) { 
            down = false;
            setDireccion(0); // abajo
            return true; 
        }
        if (down) down = false;
        return false;
    }

    public boolean consumeLeft() {
        if (left && !animandoMovimiento) { 
            left = false;
            setDireccion(3); // izquierda
            return true; 
        }
        if (left) left = false;
        return false;
    }

    public boolean consumeRight() {
        if (right && !animandoMovimiento) { 
            right = false;
            setDireccion(2); // derecha
            return true; 
        }
        if (right) right = false;
        return false;
    }
}