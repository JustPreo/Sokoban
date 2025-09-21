package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.ConfiguracionJuego;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;
import com.sokoban.com.SoundManager;

public class PantallaSettings implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private boolean puedeInteractuar = true;
    private Idiomas idiomas;
    private ConfiguracionJuego config;

    // Texturas de botones
    private Texture texturaExtra, texturaExtra2, texturaVolver, texturaVolver2, texturaJugar, texturaJugar2;
    private Button.ButtonStyle estiloExtra, estiloVolver, estiloJugar;

    // Componentes UI
    private Slider sliderMusica, sliderEfectos;
    private CheckBox checkMusica, checkEfectos, checkPantallaCompleta, checkVsync, checkFPS, checkAutoGuardar;

    //private SelectBox<String> selectorDificultad;
// private CheckBox checkTutorial;
    public PantallaSettings() {
        idiomas = Idiomas.getInstance();
        config = ConfiguracionJuego.getInstance();
    }

    @Override
    public void show() {
        // Setup texturas de botones
        texturaExtra = new Texture(Gdx.files.internal("fondoNormal.png"));
        Drawable fondoExtra = new TextureRegionDrawable(new TextureRegion(texturaExtra));
        texturaExtra2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoExtra2 = new TextureRegionDrawable(new TextureRegion(texturaExtra2));

        estiloExtra = new Button.ButtonStyle();
        estiloExtra.up = fondoExtra;
        estiloExtra.down = fondoExtra2;

        texturaVolver = new Texture(Gdx.files.internal("fondoNormal.png"));
        Drawable fondoVolver = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        texturaVolver2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoVolver2 = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

        estiloVolver = new Button.ButtonStyle();
        estiloVolver.up = fondoVolver;
        estiloVolver.down = fondoVolver2;

        texturaJugar = new Texture(Gdx.files.internal("fondoNormal.png"));
        Drawable fondoJugar = new TextureRegionDrawable(new TextureRegion(texturaJugar));
        texturaJugar2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        Drawable fondoJugar2 = new TextureRegionDrawable(new TextureRegion(texturaJugar2));

        estiloJugar = new Button.ButtonStyle();
        estiloJugar.up = fondoJugar;
        estiloJugar.down = fondoJugar2;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        bg = new Texture("fondoM.png");

        crearInterfaz();
    }

    private void crearInterfaz() {
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.top().padTop(20);

        // Título principal
        Label titulo = new Label(idiomas.obtenerTexto("settings.titulo"), skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // Scroll pane para el contenido
        Table contenidoTabla = new Table();
        ScrollPane scrollPane = new ScrollPane(contenidoTabla, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        tablaPrincipal.add(scrollPane).size(700, 450).padBottom(20).row();

        // === SECCIÓN AUDIO ===
        crearSeccionAudio(contenidoTabla);

        // === SECCIÓN GRÁFICOS ===
        crearSeccionGraficos(contenidoTabla);

        // === SECCIÓN JUEGO ===
        crearSeccionJuego(contenidoTabla);

        // Botones finales
        Table tablaBotones = new Table();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-vaV7.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 13;
        BitmapFont pixelFont = generator.generateFont(parameter);
        generator.dispose(); // liberar recursos


        Label.LabelStyle estiloPixel = new Label.LabelStyle();
        estiloPixel.font = pixelFont;
        Button btnGuardar = new Button(estiloJugar);
        Label labelGuardar = new Label(idiomas.obtenerTexto("settings.guardar"), estiloPixel);
        labelGuardar.setFontScale(1f); // Fuente más grande para botones
        btnGuardar.add(labelGuardar);
        btnGuardar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    guardarConfiguracion();
                }
            }
        });

        Button btnRestablecer = new Button(estiloExtra);
        Label labelRestablecer = new Label(idiomas.obtenerTexto("settings.restablecer"), estiloPixel);
        labelRestablecer.setFontScale(1f);
        btnRestablecer.add(labelRestablecer);
        btnRestablecer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    mostrarConfirmacionRestablecimiento();
                }
            }
        });

        Button btnVolver = new Button(estiloVolver);
        Label labelVolver = new Label(idiomas.obtenerTexto("general.volver"), estiloPixel);
        labelVolver.setFontScale(1f);
        btnVolver.add(labelVolver);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                }
            }
        });

        tablaBotones.add(btnGuardar).size(120, 45).padRight(15);
        tablaBotones.add(btnRestablecer).size(150, 45).padRight(15);
        tablaBotones.add(btnVolver).size(120, 45);

        tablaPrincipal.add(tablaBotones).row();
        stage.addActor(tablaPrincipal);
    }

    private void crearSeccionAudio(Table contenidoTabla) {
        // Título sección
        Label tituloAudio = new Label(idiomas.obtenerTexto("settings.audio"), skin);
        tituloAudio.setColor(Color.YELLOW);
        contenidoTabla.add(tituloAudio).left().padBottom(10).padTop(10).row();

        // Volumen Música
        Table filaMusicaVolumen = new Table();
        Label labelVolumenMusica = new Label(idiomas.obtenerTexto("settings.volumen_musica"), skin);
        labelVolumenMusica.setFontScale(0.8f);
        sliderMusica = new Slider(0f, 1f, 0.1f, false, skin);
        sliderMusica.setValue(config.getVolumenMusica());

        Label valorMusica = new Label(Math.round(config.getVolumenMusica() * 100) + "%", skin);
        valorMusica.setFontScale(0.7f);

        filaMusicaVolumen.add(labelVolumenMusica).width(150).left();
        filaMusicaVolumen.add(sliderMusica).width(200).padLeft(10);
        filaMusicaVolumen.add(valorMusica).width(50).padLeft(10);
        contenidoTabla.add(filaMusicaVolumen).left().padBottom(5).row();

        // Actualizar valor cuando cambie el slider
        sliderMusica.addListener(event -> {
            valorMusica.setText(Math.round(sliderMusica.getValue() * 100) + "%");
            return false;
        });

        // Volumen Efectos
        Table filaEfectosVolumen = new Table();
        Label labelVolumenEfectos = new Label(idiomas.obtenerTexto("settings.volumen_efectos"), skin);
        labelVolumenEfectos.setFontScale(0.8f);
        sliderEfectos = new Slider(0f, 1f, 0.1f, false, skin);
        sliderEfectos.setValue(config.getVolumenEfectos());

        Label valorEfectos = new Label(Math.round(config.getVolumenEfectos() * 100) + "%", skin);
        valorEfectos.setFontScale(0.7f);

        filaEfectosVolumen.add(labelVolumenEfectos).width(150).left();
        filaEfectosVolumen.add(sliderEfectos).width(200).padLeft(10);
        filaEfectosVolumen.add(valorEfectos).width(50).padLeft(10);
        contenidoTabla.add(filaEfectosVolumen).left().padBottom(5).row();

        sliderEfectos.addListener(event -> {
            valorEfectos.setText(Math.round(sliderEfectos.getValue() * 100) + "%");
            return false;
        });

        // Checkboxes de audio
        checkMusica = new CheckBox("", skin);
        checkMusica.setChecked(config.isMusicaActivada());
        Label labelCheckMusica = new Label(idiomas.obtenerTexto("settings.musica_activada"), skin);
        labelCheckMusica.setFontScale(0.8f);

        Table filaCheckMusica = new Table();
        filaCheckMusica.add(checkMusica).padRight(10);
        filaCheckMusica.add(labelCheckMusica).left();
        contenidoTabla.add(filaCheckMusica).left().padBottom(5).row();

        checkEfectos = new CheckBox("", skin);
        checkEfectos.setChecked(config.isEfectosActivados());
        Label labelCheckEfectos = new Label(idiomas.obtenerTexto("settings.efectos_activados"), skin);
        labelCheckEfectos.setFontScale(0.8f);

        Table filaCheckEfectos = new Table();
        filaCheckEfectos.add(checkEfectos).padRight(10);
        filaCheckEfectos.add(labelCheckEfectos).left();
        contenidoTabla.add(filaCheckEfectos).left().padBottom(15).row();
    }

    private void crearSeccionGraficos(Table contenidoTabla) {
        // Título sección
        Label tituloGraficos = new Label(idiomas.obtenerTexto("settings.graficos"), skin);
        tituloGraficos.setColor(Color.YELLOW);
        contenidoTabla.add(tituloGraficos).left().padBottom(10).row();

        // Pantalla completa
        checkPantallaCompleta = new CheckBox("", skin);
        checkPantallaCompleta.setChecked(config.isPantallaCompleta());
        Label labelPantallaCompleta = new Label(idiomas.obtenerTexto("settings.pantalla_completa"), skin);
        labelPantallaCompleta.setFontScale(0.8f);

        Table filaPantallaCompleta = new Table();
        filaPantallaCompleta.add(checkPantallaCompleta).padRight(10);
        filaPantallaCompleta.add(labelPantallaCompleta).left();
        contenidoTabla.add(filaPantallaCompleta).left().padBottom(5).row();

        // VSync
        checkVsync = new CheckBox("", skin);
        checkVsync.setChecked(config.isVsync());
        Label labelVsync = new Label(idiomas.obtenerTexto("settings.vsync"), skin);
        labelVsync.setFontScale(0.8f);

        Table filaVsync = new Table();
        filaVsync.add(checkVsync).padRight(10);
        filaVsync.add(labelVsync).left();
        contenidoTabla.add(filaVsync).left().padBottom(15).row();

        // Se elim el check de mostrar FPS
    }

    private void crearSeccionJuego(Table contenidoTabla) {
        // Título sección
        Label tituloJuego = new Label(idiomas.obtenerTexto("settings.juego"), skin);
        tituloJuego.setColor(Color.YELLOW);
        contenidoTabla.add(tituloJuego).left().padBottom(10).row();

        // Dificultad
        Table filaDificultad = new Table();
        Label labelDificultad = new Label(idiomas.obtenerTexto("settings.dificultad"), skin);
        labelDificultad.setFontScale(0.8f);

        /* selectorDificultad = new SelectBox<>(skin);
        String[] dificultades = {
            idiomas.obtenerTexto("dificultad.facil"),
            idiomas.obtenerTexto("dificultad.medio"),
            idiomas.obtenerTexto("dificultad.dificil")
        };
        selectorDificultad.setItems(dificultades);

        // Seleccionar dificultad actual
        switch (config.getDificultad()) {
            case "facil":
                selectorDificultad.setSelectedIndex(0);
                break;
            case "medio":
                selectorDificultad.setSelectedIndex(1);
                break;
            case "dificil":
                selectorDificultad.setSelectedIndex(2);
                break;
        }

        filaDificultad.add(labelDificultad).width(120).left();
        filaDificultad.add(selectorDificultad).width(150).padLeft(10);
        contenidoTabla.add(filaDificultad).left().padBottom(10).row();*/
        // Auto-guardar
        checkAutoGuardar = new CheckBox("", skin);
        checkAutoGuardar.setChecked(config.isAutoGuardar());
        Label labelAutoGuardar = new Label(idiomas.obtenerTexto("settings.auto_guardar"), skin);
        labelAutoGuardar.setFontScale(0.8f);

        Table filaAutoGuardar = new Table();
        filaAutoGuardar.add(checkAutoGuardar).padRight(10);
        filaAutoGuardar.add(labelAutoGuardar).left();
        contenidoTabla.add(filaAutoGuardar).left().padBottom(5).row();

        /* // Tutorial
        checkTutorial = new CheckBox("", skin);
        checkTutorial.setChecked(config.isMostrarTutorial());
        Label labelTutorial = new Label(idiomas.obtenerTexto("settings.tutorial"), skin);
        labelTutorial.setFontScale(0.8f);

        Table filaTutorial = new Table();
        filaTutorial.add(checkTutorial).padRight(10);
        filaTutorial.add(labelTutorial).left();
        contenidoTabla.add(filaTutorial).left().padBottom(10).row();*/
    }

    private void guardarConfiguracion() {
        // Aplicar configuraciones de audio
        config.setVolumenMusica(sliderMusica.getValue());
        config.setVolumenEfectos(sliderEfectos.getValue());
        config.setMusicaActivada(checkMusica.isChecked());
        config.setEfectosActivados(checkEfectos.isChecked());

        // Aplicar configuraciones de gráficos
        config.setPantallaCompleta(checkPantallaCompleta.isChecked());
        config.setVsync(checkVsync.isChecked());
        config.setMostrarFPS(checkFPS.isChecked());

        // Aplicar configuraciones de juego
        /* String dificultadSeleccionada;
        switch (selectorDificultad.getSelectedIndex()) {
            case 0:
                dificultadSeleccionada = "facil";
                break;
            case 1:
                dificultadSeleccionada = "medio";
                break;
            case 2:
                dificultadSeleccionada = "dificil";
                break;
            default:
                dificultadSeleccionada = "medio";
                break;
        }
        config.setDificultad(dificultadSeleccionada);*/
        config.setAutoGuardar(checkAutoGuardar.isChecked());
        //config.setMostrarTutorial(checkTutorial.isChecked());

        // Aplicar efectos inmediatos
        aplicarConfiguracionInmediata();

        // Guardar configuración
        config.guardarConfiguracion();

        // Mostrar mensaje de confirmación
        mostrarMensaje(idiomas.obtenerTexto("settings.configuracion_guardada"), Color.GREEN);
    }

    private void aplicarConfiguracionInmediata() {
        // Aplicar configuraciones de audio al SoundManager
        try {
            if (config.isMusicaActivada()) {
                SoundManager.setMusicVolume(config.getVolumenMusica());
            } else {
                SoundManager.setMusicVolume(0f);
            }

            if (config.isEfectosActivados()) {
                SoundManager.setSoundVolume(config.getVolumenEfectos());
            } else {
                SoundManager.setSoundVolume(0f);
            }
        } catch (Exception e) {
            System.err.println("Error aplicando configuración de audio: " + e.getMessage());
        }

        // Aplicar configuraciones de gráficos
        try {
            if (config.isPantallaCompleta()) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(800, 600);
            }

            // VSync se aplicaría en la configuración principal del juego
        } catch (Exception e) {
            System.err.println("Error aplicando configuración de gráficos: " + e.getMessage());
        }
    }

    private void mostrarConfirmacionRestablecimiento() {
        puedeInteractuar = false;

        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.setBackground(skin.newDrawable("default-round", new Color(0, 0, 0, 0.7f)));
        overlay.center();

        Table panel = new Table(skin);
        panel.setBackground(skin.newDrawable("default-round", Color.DARK_GRAY));
        panel.pad(20);
        overlay.add(panel);

        Label titulo = new Label(idiomas.obtenerTexto("general.confirmar"), skin);
        titulo.setColor(Color.YELLOW);
        panel.add(titulo).padBottom(15).row();

        Label mensaje = new Label(idiomas.obtenerTexto("settings.confirmar_restablecer"), skin);
        mensaje.setColor(Color.WHITE);
        panel.add(mensaje).padBottom(20).row();

        Table tablaBotones = new Table();

        Button btnSi = new Button(estiloJugar);
        Label labelSi = new Label(idiomas.obtenerTexto("general.si"), skin);
        labelSi.setFontScale(0.8f);
        btnSi.add(labelSi);
        btnSi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                restablecerConfiguracion();
                overlay.remove();
                puedeInteractuar = true;
                mostrarMensaje(idiomas.obtenerTexto("settings.restablecido"), Color.GREEN);
            }
        });

        Button btnNo = new Button(estiloVolver);
        Label labelNo = new Label(idiomas.obtenerTexto("general.no"), skin);
        labelNo.setFontScale(0.8f);
        btnNo.add(labelNo);
        btnNo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlay.remove();
                puedeInteractuar = true;
            }
        });

        tablaBotones.add(btnSi).size(80, 40).padRight(15);
        tablaBotones.add(btnNo).size(80, 40);
        panel.add(tablaBotones).row();

        stage.addActor(overlay);
    }

    private void restablecerConfiguracion() {
        config.restablecerPorDefecto();

        // Actualizar UI con valores por defecto
        sliderMusica.setValue(config.getVolumenMusica());
        sliderEfectos.setValue(config.getVolumenEfectos());
        checkMusica.setChecked(config.isMusicaActivada());
        checkEfectos.setChecked(config.isEfectosActivados());
        checkPantallaCompleta.setChecked(config.isPantallaCompleta());
        checkVsync.setChecked(config.isVsync());
        checkFPS.setChecked(config.isMostrarFPS());
        //selectorDificultad.setSelectedIndex(1); // medio
        checkAutoGuardar.setChecked(config.isAutoGuardar());
        //checkTutorial.setChecked(config.isMostrarTutorial());
    }

    private void mostrarMensaje(String texto, Color color) {
        Label mensajeTemporal = new Label(texto, skin);
        mensajeTemporal.setColor(color);
        mensajeTemporal.setPosition(10, Gdx.graphics.getHeight() - 50);
        stage.addActor(mensajeTemporal);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Gdx.app.postRunnable(() -> mensajeTemporal.remove());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getCamera().update();
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
        if (texturaExtra != null) {
            texturaExtra.dispose();
        }
        if (texturaExtra2 != null) {
            texturaExtra2.dispose();
        }
        if (texturaVolver != null) {
            texturaVolver.dispose();
        }
        if (texturaVolver2 != null) {
            texturaVolver2.dispose();
        }
        if (texturaJugar != null) {
            texturaJugar.dispose();
        }
        if (texturaJugar2 != null) {
            texturaJugar2.dispose();
        }
    }
}
