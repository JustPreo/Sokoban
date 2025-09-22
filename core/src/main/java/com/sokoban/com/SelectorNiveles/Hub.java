package com.sokoban.com.SelectorNiveles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.com.Base.IntentoDeMenu.MenuScreen;
import com.sokoban.com.Base.Jugador;
import com.sokoban.com.Base.Pared;
import com.sokoban.com.Base.PisoHub;
import com.sokoban.com.Base.Puerta;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.SoundManager;
import com.sokoban.com.ThanosRoom;

import java.util.ArrayList;

public class Hub implements Screen {

    private Idiomas idiomas;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
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
    ArrayList<Puerta> puertas = new ArrayList<>();
    private ArrayList<PisoHub> pisosHub = new ArrayList<>();

    protected int jugadorX = 2, jugadorY = 4;
    private int nivelSeleccionado = -1; // Para mostrar info del nivel actual

    // Tu mapa original
    private int[][] mapa = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 4, 0, 1, 0, 5, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 1, 9, 1, 1, 1, 9, 1, 1, 1, 9, 1, 1, 1, 9, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 9, 1, 1, 1, 9, 1, 1, 1, 9, 1, 1, 1, 9, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 0, 6, 0, 1, 0, 7, 0, 1, 0, 8, 0, 1, 0, 10, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private int[][] puertasReferencia = {{2, 3}, {6, 3}, {10, 3}, {14, 3}, {2, 7}, {10, 7}, {14, 7}};
    // Las demás puertas normales
    // 2 = lvl 1 | 3 = lvl 2 | ... 9 = puerta en vez de pared , 10 = puerta de thanos

    @Override
    public void show() {
        idiomas = Idiomas.getInstance();
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(COLUMNAS * TILE, FILAS * TILE);
        layout = new GlyphLayout();

        setupPixelFonts();

        jogador = new Jugador(jugadorX * TILE, jugadorY * TILE,
                viewport.getWorldWidth(), viewport.getWorldHeight(), TILE);

        spawnearNiveles();
        verificarNivelActual();
    }

    private void setupPixelFonts() {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-vaV7.ttf"));

            FreeTypeFontGenerator.FreeTypeFontParameter paramTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramTitulo.size = Math.max(16, TILE / 2);
            paramTitulo.color = Color.CYAN;
            paramTitulo.magFilter = paramTitulo.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
            fontTitulo = generator.generateFont(paramTitulo);

            FreeTypeFontGenerator.FreeTypeFontParameter paramInstrucciones = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramInstrucciones.size = Math.max(10, TILE / 4);
            paramInstrucciones.color = Color.LIGHT_GRAY;
            paramInstrucciones.magFilter = paramInstrucciones.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
            fontInstrucciones = generator.generateFont(paramInstrucciones);

            FreeTypeFontGenerator.FreeTypeFontParameter paramInfo = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramInfo.size = Math.max(8, TILE / 5);
            paramInfo.color = Color.WHITE;
            paramInfo.magFilter = paramInfo.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest;
            fontInfo = generator.generateFont(paramInfo);

            generator.dispose();
        } catch (Exception e) {
            fontTitulo = new BitmapFont();
            fontInstrucciones = new BitmapFont();
            fontInfo = new BitmapFont();
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            SoundManager.playMusic("lobby", true, 0.5f);
            ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
        }

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

        jogador.update();

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        for (PisoHub piso : pisosHub) {
            piso.render(spriteBatch);
        }
        for (padNiveles pad : pads) {
            pad.render(spriteBatch);
        }
        for (Puerta puerta : puertas) {
            puerta.render(spriteBatch);
        }
        jogador.render(spriteBatch);
        for (Pared pared : paredes) {
            pared.render(spriteBatch);
        }
        renderTexto();
        spriteBatch.end();
    }

    private void verificarNivelActual() {
        nivelSeleccionado = -1;

        for (padNiveles pad : pads) {
            if (jogador.getHitbox().overlaps(pad.getHitbox())) {
                nivelSeleccionado = pad.getNivel();

                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                        || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    // Si es la “puerta especial Thanos” (nivel = 9)
                    if (nivelSeleccionado == 9) {
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new ThanosRoom());
                        return;
                    }

                    pad.irANivel(); // Para los demás niveles
                }
                break;
            }
        }

        // Revisar puerta especial Thanos
        for (Puerta puerta : puertas) {
            if (puerta.isThanos() && jogador.getHitbox().overlaps(puerta.getHitbox())) {
                nivelSeleccionado = 9; // Para mostrar info
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                        || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new ThanosRoom());
                }
                return;
            }
        }
    }

    private void renderTexto() {
    float worldWidth = viewport.getWorldWidth();
    float worldHeight = viewport.getWorldHeight();

    // Si es nivel 9, solo mostrar ENTER
    if (nivelSeleccionado == 9) {
        GlyphLayout layout = new GlyphLayout();
        String accion = "ENTER";
        layout.setText(fontInfo, accion);
        fontInfo.setColor(Color.LIGHT_GRAY);
        fontInfo.draw(spriteBatch, accion, worldWidth / 2 - layout.width / 2, worldHeight / 2);
        return; // No mostrar el resto de instrucciones ni info
    }

    // Mostrar instrucciones normales si no es nivel 9
    String[] instrucciones = {
        idiomas.obtenerTexto("hub.instruccion1"),
        idiomas.obtenerTexto("hub.instruccion2"),
        idiomas.obtenerTexto("hub.instruccion3"),
        idiomas.obtenerTexto("hub.instruccion4")
    };

    float baseY = worldHeight - TILE * 0.1f;
    float separacionLinea = TILE * 0.35f;

    for (int i = 0; i < instrucciones.length; i++) {
        layout.setText(fontInstrucciones, instrucciones[i]);
        float instruccionX = (worldWidth - layout.width) / 2;
        float instruccionY = baseY - (i * separacionLinea);
        fontInstrucciones.draw(spriteBatch, instrucciones[i], instruccionX, instruccionY);
    }

    if (nivelSeleccionado != -1) {
        renderInfoNivel();
    }
}

private void renderInfoNivel() {
    if (nivelSeleccionado == 9) return; // No mostrar info adicional si es Thanos

    float centerX = (COLUMNAS * TILE) / 2f;
    float centerY = (FILAS * TILE) / 2f;
    String infoNivel = idiomas.obtenerTexto("hub.nivel") + " " + nivelSeleccionado;
    String dificultad = idiomas.obtenerTexto("hub.dificultad") + ": " + getDificultad(nivelSeleccionado);
    String estado = idiomas.obtenerTexto("hub.estado") + ": " + getEstadoNivel(nivelSeleccionado);
    String accion = idiomas.obtenerTexto("hub.accion");

    float separacion = TILE * 0.3f;

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

    fontInfo.setColor(Color.WHITE);
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
        int nivelMaximo = sistema.haySesionActiva() ? sistema.getUsuarioActual().getNivelMaximoAlcanzado() : 1;
        if (nivel < nivelMaximo) {
            return "Completado";
        }
        return "No completado";
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        if (mapa[filaMatriz][nuevoX] == 1 || mapa[filaMatriz][nuevoX] == 9) {//No pasar por puertas
            return;
        }

        jugadorX = nuevoX;
        jugadorY = nuevoY;
        jogador.setPos(jugadorX * TILE, jugadorY * TILE);
    }

    private void spawnearNiveles() {
        SistemaUsuarios sistema = SistemaUsuarios.getInstance();
        int nivelMaximo = sistema.haySesionActiva() ? sistema.getUsuarioActual().getNivelMaximoAlcanzado() : 1;

        paredes.clear();
        pads.clear();
        pisosHub.clear();
        puertas.clear();

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                boolean esPuerta = false;
                int nivelPuerta = 0;

                for (int i = 0; i < puertasReferencia.length; i++) {
                    int pf = puertasReferencia[i][1];
                    int pc = puertasReferencia[i][0];
                    if (f == pf && c == pc) {
                        esPuerta = true;
                        nivelPuerta = i + 1;
                        break;
                    }
                }

                // Puerta desbloqueada
                if (esPuerta && nivelPuerta <= nivelMaximo) {
                    mapa[FILAS - 1 - f][c] = 0; // colisión ok
                    esPuerta = false;
                }

                if (mapa[f][c] == 1) {
                    paredes.add(new Pared(c * TILE, (FILAS - 1 - f) * TILE, TILE));
                }
                if (mapa[f][c] == 9) {
                    puertas.add(new Puerta(c * TILE, (FILAS - 1 - f) * TILE, TILE, false));
                }
                if (mapa[f][c] == 10) {
                    puertas.add(new Puerta(c * TILE, f * TILE, TILE, true));
                }

                if (mapa[f][c] != 1 && mapa[f][c] != -1) {
                    crearPisoHub(c, FILAS - 1 - f);
                }

                int valor = mapa[f][c];
                if (valor != 0 && valor != 1 && valor != 9 && valor != 10) {
                    int nivelPad = valor - 1;
                    padNiveles pad = new padNiveles(c * TILE, f * TILE, TILE, nivelPad);
                    pads.add(pad);
                }
            }
        }
    }

    private void crearPisoHub(int x, int y) {
        PisoHub piso = new PisoHub(x * TILE, y * TILE, TILE);
        pisosHub.add(piso);
    }
}
