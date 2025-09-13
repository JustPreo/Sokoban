package com.sokoban.com.SelectorNiveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.Base.IntentoDeMenu.MenuScreen;
import com.sokoban.com.Base.Jugador;
import com.sokoban.com.Base.Pared;
import com.sokoban.com.Base.Piso;
import com.sokoban.com.Juegito;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.SoundManager;
import java.util.ArrayList;

public class Hub implements Screen {

    private SpriteBatch spriteBatch;
    private ScreenViewport viewport;
    private BitmapFont fontTitulo;
    private BitmapFont fontInstrucciones;
    private BitmapFont fontInfo;
    private GlyphLayout layout; // Para medir texto

    protected Jugador jogador;

    protected int FILAS = 11;
    protected int COLUMNAS = 17;
    protected int TILE = 800 / COLUMNAS;
    protected ArrayList<padNiveles> pads = new ArrayList<>();
    ArrayList<Pared> paredes = new ArrayList<>();
    ArrayList<Piso> pisos = new ArrayList<>();//Proximamente 

    protected int jugadorX = 2, jugadorY = 4;
    private int nivelSeleccionado = -1; // Para mostrar info del nivel actual

    private int[][] mapa = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 4, 0, 1, 0, 5, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 0, 6, 0, 1, 0, 7, 0, 1, 0, 8, 0, 1, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private int[][] puertasReferencia = {{2, 3}, {6, 3}, {10, 3}, {14, 3}, {2, 7}, {10, 7}, {14, 7}};
    //2 = lvl 1 | 3 = lvl 2 | 4 = lvl 3 | 5 = lvl 4 | 6 = lvl 5 | 7 = lvl 6 | 8 = lvl 7

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        viewport = viewport = new ScreenViewport();
        layout = new GlyphLayout();

        // Configurar fuentes Press Start 2P
        setupPixelFonts();

        jogador = new Jugador(jugadorX * TILE, jugadorY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight(), TILE);

        spawnearNiveles();

        // SOLUCIÓN: Verificar nivel inicial al cargar
        verificarNivelActual();
    }

    private void setupPixelFonts() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                    Gdx.files.internal("fonts/PressStart2P-vaV7.ttf")
            );

            // Fuente para título (más grande)
            FreeTypeFontGenerator.FreeTypeFontParameter paramTitulo
                    = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramTitulo.size = Math.max(16, TILE / 2);
            paramTitulo.color = Color.CYAN;
            paramTitulo.magFilter = Texture.TextureFilter.Nearest;
            paramTitulo.minFilter = Texture.TextureFilter.Nearest;
            paramTitulo.borderWidth = 2;
            paramTitulo.borderColor = Color.DARK_GRAY;
            fontTitulo = generator.generateFont(paramTitulo);

            // Fuente para instrucciones (mediana)
            FreeTypeFontGenerator.FreeTypeFontParameter paramInstrucciones
                    = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramInstrucciones.size = Math.max(10, TILE / 4);
            paramInstrucciones.color = Color.LIGHT_GRAY;
            paramInstrucciones.magFilter = Texture.TextureFilter.Nearest;
            paramInstrucciones.minFilter = Texture.TextureFilter.Nearest;
            paramInstrucciones.borderWidth = 1;
            paramInstrucciones.borderColor = Color.BLACK;
            fontInstrucciones = generator.generateFont(paramInstrucciones);

            // Fuente para información adicional (pequeña)
            FreeTypeFontGenerator.FreeTypeFontParameter paramInfo
                    = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramInfo.size = Math.max(8, TILE / 5);
            paramInfo.color = Color.WHITE;
            paramInfo.magFilter = Texture.TextureFilter.Nearest;
            paramInfo.minFilter = Texture.TextureFilter.Nearest;
            paramInfo.borderWidth = 1;
            paramInfo.borderColor = Color.BLACK;
            fontInfo = generator.generateFont(paramInfo);

            generator.dispose();
            System.out.println("Fuentes Press Start 2P cargadas correctamente en Hub");

        } catch (Exception e) {
            System.err.println("Error cargando fuentes Press Start 2P en Hub: " + e.getMessage());

            // Fallback con fuentes por defecto optimizadas
            fontTitulo = new BitmapFont();
            fontTitulo.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest
            );
            fontTitulo.setColor(Color.CYAN);
            fontTitulo.getData().setScale(TILE * 0.06f);

            fontInstrucciones = new BitmapFont();
            fontInstrucciones.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest
            );
            fontInstrucciones.setColor(Color.LIGHT_GRAY);
            fontInstrucciones.getData().setScale(TILE * 0.035f);

            fontInfo = new BitmapFont();
            fontInfo.getRegion().getTexture().setFilter(
                    Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest
            );
            fontInfo.setColor(Color.WHITE);
            fontInfo.getData().setScale(TILE * 0.03f);
        }
    }

    @Override
    public void render(float f) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());

        }

        // Procesar input solo si el jugador no está animando
        if (!jogador.estaAnimando()) {
            if (jogador.consumeUp()) {
                moverJugador(0, 1);
            } else if (jogador.consumeDown()) {
                moverJugador(0, -1);
            } else if (jogador.consumeLeft()) {
                moverJugador(-1, 0);
            } else if (jogador.consumeRight()) {
                moverJugador(1, 0);
            }

            verificarNivelActual();
        } else {

            jogador.consumeUp();
            jogador.consumeDown();
            jogador.consumeLeft();
            jogador.consumeRight();
        }

        // Actualizar jugador
        jogador.update();

        ScreenUtils.clear(Color.DARK_GRAY);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        // Render
        spriteBatch.begin();

        for (padNiveles pad : pads) {
            pad.render(spriteBatch);
        }

        jogador.render(spriteBatch);

        for (Pared pared : paredes) {
            pared.render(spriteBatch);
        }

        renderTexto();

        spriteBatch.end();
    }

    private void verificarNivelActual() {
        nivelSeleccionado = -1; // Reset sel

        for (padNiveles pad : pads) {
            if (jogador.getHitbox().overlaps(pad.getHitbox())) {
                nivelSeleccionado = pad.getNivel();

                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                        || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    System.out.println("Activando nivel: " + nivelSeleccionado);
                    pad.irANivel();
                }
                break;
            }
        }
    }

    private void renderTexto() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // TÍTULO - Centrado en la parte superior
        String titulo = "SELECCIONAR NIVEL";
        layout.setText(fontTitulo, titulo);
        float tituloX = (worldWidth - layout.width) / 2;
        float tituloY = worldHeight - TILE * 0.1f;
        //fontTitulo.draw(spriteBatch, titulo, tituloX, tituloY);

        // INSTRUCCIONES
        String[] instrucciones = {
            "Usa WASD o flechas para moverte",
            "Camina sobre un nivel para seleccionarlo",
            "ENTER/ESPACIO para entrar al nivel",
            "ESC para volver al menu principal"
        };

        float baseY = worldHeight - TILE * 0.1f;
        float separacionLinea = TILE * 0.35f; // Separacion

        for (int i = 0; i < instrucciones.length; i++) {
            layout.setText(fontInstrucciones, instrucciones[i]);
            float instruccionX = (worldWidth - layout.width) / 2; // Centrado
            float instruccionY = baseY - (i * separacionLinea);
            fontInstrucciones.draw(spriteBatch, instrucciones[i], instruccionX, instruccionY);
        }

        //info nivel
        if (nivelSeleccionado != -1) {
            renderInfoNivel();
        }
        /*else {
            // Mostrar mensaje cuando no hay nivel seleccionado
            String mensaje = "Camina sobre un pad para ver info del nivel";
            layout.setText(fontInfo, mensaje);
            float mensajeX = worldWidth - TILE * 5f;
            float mensajeY = worldHeight - TILE * 1.5f;
            fontInfo.setColor(Color.GRAY);
            fontInfo.draw(spriteBatch, mensaje, mensajeX, mensajeY);
            fontInfo.setColor(Color.WHITE); // Restaurar color
        }*/
    }

    private void renderInfoNivel() {
        float centerX = (COLUMNAS * TILE) / 2f;
        float centerY = (FILAS * TILE) / 2f;

        String infoNivel = "Level " + nivelSeleccionado;
        String dificultad = "Diff: " + getDificultad(nivelSeleccionado);
        String estado = "Estado: " + getEstadoNivel(nivelSeleccionado);
        String accion = "ENTER/ESPACIO: Jugar";

        float separacion = TILE * 0.3f;

        // Dibujar centrado
        layout.setText(fontInfo, infoNivel);
        fontInfo.setColor(Color.YELLOW);
        fontInfo.draw(spriteBatch, infoNivel, centerX - layout.width / 2f, centerY + separacion * 1.5f);

        layout.setText(fontInfo, dificultad);
        fontInfo.setColor(Color.WHITE);
        fontInfo.draw(spriteBatch, dificultad, centerX - layout.width / 2f, centerY + separacion * 0.5f);

        layout.setText(fontInfo, estado);
        fontInfo.draw(spriteBatch, estado, centerX - layout.width / 2f, centerY - separacion * 0.5f);

        layout.setText(fontInfo, accion);
        fontInfo.setColor(Color.LIGHT_GRAY);
        fontInfo.draw(spriteBatch, accion, centerX - layout.width / 2f, centerY - separacion * 1.5f);

        fontInfo.setColor(Color.WHITE); // Restaurar color por defecto
    }

    private String getDificultad(int nivel) {
        if (nivel <= 2) {
            return "Ez";
        }
        if (nivel <= 4) {
            return "Medium";
        }
        if (nivel <= 6) {
            return "Dificil";
        }
        return "Experto";
    }

    private String getEstadoNivel(int nivel) {
        SistemaUsuarios sistema = SistemaUsuarios.getInstance();
        int nivelMaximo = sistema.haySesionActiva() ? sistema.getUsuarioActual().getNivelMaximoAlcanzado() : 1;//Si hay sesion activa revisar el nivel max
        if (nivel < nivelMaximo){
        return "Completado";
        }
        return "No completado"; // Placeholder
    }

    @Override
    public void resize(int width, int height) {
        if (width > 0 && height > 0) {
            viewport.update(width, height, true);
        }
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
        spriteBatch.dispose();
        jogador.dispose();
        fontTitulo.dispose();
        fontInstrucciones.dispose();
        fontInfo.dispose();

        for (padNiveles pad : pads) {
            pad.dispose();
        }
        for (Pared pared : paredes) {
            pared.dispose();
        }
    }

    private void moverJugador(int dx, int dy) {
        int nuevoX = jugadorX + dx;
        int nuevoY = jugadorY + dy;

        if (nuevoX < 0 || nuevoX >= COLUMNAS || nuevoY < 0 || nuevoY >= FILAS) {
            return;
        }

        int filaMatriz = FILAS - 1 - nuevoY;
        if (mapa[filaMatriz][nuevoX] == 1) {
            return;
        }

        // Mover jugador
        jugadorX = nuevoX;
        jugadorY = nuevoY;
        jogador.setPos(jugadorX * TILE, jugadorY * TILE);

    }

    private void spawnearNiveles() {
        SistemaUsuarios sistema = SistemaUsuarios.getInstance();
        int nivelMaximo = sistema.haySesionActiva() ? sistema.getUsuarioActual().getNivelMaximoAlcanzado() : 1;//Si hay sesion activa revisar el nivel max

        paredes.clear(); // Limpiar paredes anteriores
        pads.clear();    // Limpiar pads anteriores

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                boolean esPuerta = false;
                int nivelPuerta = 0;

                // Revisar si la pos es una puerta
                for (int i = 0; i < puertasReferencia.length; i++) {
                    int pf = puertasReferencia[i][1];
                    int pc = puertasReferencia[i][0];
                    if (f == pf && c == pc) {
                        esPuerta = true;
                        nivelPuerta = i + 1;
                        break;
                    }
                }

                // Si es puerta y desbloqueada
                if (esPuerta && nivelPuerta <= nivelMaximo) {
                    mapa[FILAS - 1 - f][c] = 0; // colisión ok
                    esPuerta = false;           // no dibujar pared
                }

                if (mapa[f][c] == 1) { // solo dibujar si sigue siendo pared
                    paredes.add(new Pared(c * TILE, (FILAS - 1 - f) * TILE, TILE));
                }
                
                int valor = mapa[f][c];
                if (valor != 0 && valor != 1) {
                    int nivelPad = valor - 1;
                    padNiveles pad = new padNiveles(c * TILE, f * TILE, TILE, nivelPad);
                    pads.add(pad);
                }
            }
        }
    }
}
