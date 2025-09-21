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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.GestorArchivos;
import com.sokoban.com.Idiomas;
import com.sokoban.com.Juegito;
import com.sokoban.com.SistemaUsuarios;
import com.sokoban.com.Usuario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PantallaRanking implements Screen {

    private Stage stage;
    private Skin skin;
    private Texture bg;
    private boolean puedeInteractuar = true;
    private SistemaUsuarios sistemaUsuarios;
    private GestorArchivos gestorArchivos;
    private BitmapFont mediumFont;

    // texturas de botones
    private Texture texturaExtra, texturaExtra2, texturaVolver, texturaVolver2;
    private Button.ButtonStyle estiloExtra, estiloVolver;
    private Texture texturaActualizar, texturaActualizar2, texturaPerfil, texturaPerfil2;
    private Button.ButtonStyle estiloActualizar, estiloPerfil;

    // componentes UI
    private SelectBox<String> filtroTipo;
    private SelectBox<String> filtroPeriodo;
    private ScrollPane scrollRanking;
    private Table tablaRanking;
    private Label labelPosicionActual;

    // datos de ranking
    private List<DatosUsuario> datosUsuarios;

    private enum TipoFiltro {
        PUNTUACION_TOTAL, NIVEL_MAXIMO, PARTIDAS_COMPLETADAS, TIEMPO_JUGADO
    }

    private enum PeriodoFiltro {
        HISTORICO, ESTE_MES, ESTA_SEMANA
    }

    private TipoFiltro tipoFiltroActual = TipoFiltro.PUNTUACION_TOTAL;
    private PeriodoFiltro periodoFiltroActual = PeriodoFiltro.HISTORICO;

    private static class DatosUsuario {

        String nombreUsuario;
        String nombreCompleto;
        int puntuacionTotal;
        int nivelMaximo;
        int partidasTotales;
        int partidasCompletadas;
        long tiempoTotal;
        String rutaAvatar;

        public DatosUsuario(Usuario usuario) {
            this.nombreUsuario = usuario.getNombreUsuario();
            this.nombreCompleto = usuario.getNombreCompleto();
            this.puntuacionTotal = usuario.getPuntuacionTotal();
            this.nivelMaximo = usuario.getNivelMaximoAlcanzado();
            this.partidasTotales = usuario.getPartidasTotales();
            this.partidasCompletadas = usuario.getPartidasCompletadas();
            this.tiempoTotal = usuario.getTiempoTotalJugado();
            this.rutaAvatar = usuario.getRutaAvatar();
        }
    }

    public PantallaRanking() {
        sistemaUsuarios = SistemaUsuarios.getInstance();
        gestorArchivos = GestorArchivos.getInstancia();
        datosUsuarios = new ArrayList<>();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        bg = new Texture("fondoM.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-vaV7.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        mediumFont = generator.generateFont(parameter);
        generator.dispose();
        skin.add("medium-font", mediumFont, BitmapFont.class);

        cargarTexturasBotones();
        cargarDatosUsuarios();
        crearInterfaz();
    }

    private void cargarTexturasBotones() {
        texturaExtra = new Texture(Gdx.files.internal("extra.png"));
        texturaExtra2 = new Texture(Gdx.files.internal("extra2.png"));
        estiloExtra = new Button.ButtonStyle();
        estiloExtra.up = new TextureRegionDrawable(new TextureRegion(texturaExtra));
        estiloExtra.down = new TextureRegionDrawable(new TextureRegion(texturaExtra2));

        texturaVolver = new Texture(Gdx.files.internal("volver.png"));
        texturaVolver2 = new Texture(Gdx.files.internal("volver2.png"));
        estiloVolver = new Button.ButtonStyle();
        estiloVolver.up = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        estiloVolver.down = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

        texturaActualizar = new Texture(Gdx.files.internal("fondoNormal.png"));
        texturaActualizar2 = new Texture(Gdx.files.internal("fondoNormal2.png"));
        estiloActualizar = new Button.ButtonStyle();
        estiloActualizar.up = new TextureRegionDrawable(new TextureRegion(texturaActualizar));
        estiloActualizar.down = new TextureRegionDrawable(new TextureRegion(texturaActualizar2));

        texturaPerfil = new Texture(Gdx.files.internal("perfil.png"));
        texturaPerfil2 = new Texture(Gdx.files.internal("perfil2.png"));
        estiloPerfil = new Button.ButtonStyle();
        estiloPerfil.up = new TextureRegionDrawable(new TextureRegion(texturaPerfil));
        estiloPerfil.down = new TextureRegionDrawable(new TextureRegion(texturaPerfil2));
    }

    private void cargarDatosUsuarios() {
        datosUsuarios.clear();
        String[] nombresUsuarios = sistemaUsuarios.listarUsuarios();

        for (String nombreUsuario : nombresUsuarios) {
            try {
                Usuario usuario = gestorArchivos.cargarUsuario(nombreUsuario);
                if (usuario != null) {
                    datosUsuarios.add(new DatosUsuario(usuario));
                }
            } catch (Exception e) {
                System.out.println("Error cargando usuario para ranking: " + nombreUsuario);
            }
        }

        ordenarDatos();
    }

    private void crearInterfaz() {
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center().top();
        tablaPrincipal.pad(20);

        Label.LabelStyle estiloLabel = new Label.LabelStyle();
        estiloLabel.font = mediumFont;

        // titulo
        Label titulo = new Label(Idiomas.getInstance().obtenerTexto("ranking.titulo"), estiloLabel);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // filtros
        Table tablaFiltros = new Table();

        Label labelFiltroTipo = new Label(Idiomas.getInstance().obtenerTexto("ranking.filtro_tipo"), estiloLabel);
        labelFiltroTipo.setColor(Color.WHITE);

        filtroTipo = new SelectBox<>(skin);
        filtroTipo.setItems(
                Idiomas.getInstance().obtenerTexto("ranking.tipo_puntuacion"),
                Idiomas.getInstance().obtenerTexto("ranking.tipo_nivel"),
                Idiomas.getInstance().obtenerTexto("ranking.tipo_partidas"),
                Idiomas.getInstance().obtenerTexto("ranking.tipo_tiempo")
        );
        filtroTipo.setSelectedIndex(tipoFiltroActual.ordinal());
        filtroTipo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!puedeInteractuar) {
                    return;
                }
                tipoFiltroActual = TipoFiltro.values()[filtroTipo.getSelectedIndex()];
                actualizarRanking();
            }
        });

        Label labelFiltroPeriodo = new Label(Idiomas.getInstance().obtenerTexto("ranking.filtro_periodo"), estiloLabel);
        labelFiltroPeriodo.setColor(Color.WHITE);

        filtroPeriodo = new SelectBox<>(skin);
        filtroPeriodo.setItems(
                Idiomas.getInstance().obtenerTexto("ranking.periodo_historico"),
                Idiomas.getInstance().obtenerTexto("ranking.periodo_mes"),
                Idiomas.getInstance().obtenerTexto("ranking.periodo_semana")
        );
        filtroPeriodo.setSelectedIndex(periodoFiltroActual.ordinal());
        filtroPeriodo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!puedeInteractuar) {
                    return;
                }
                periodoFiltroActual = PeriodoFiltro.values()[filtroPeriodo.getSelectedIndex()];
                actualizarRanking();
            }
        });

        tablaFiltros.add(labelFiltroTipo).padRight(10);
        tablaFiltros.add(filtroTipo).width(150).padRight(30);
        tablaFiltros.add(labelFiltroPeriodo).padRight(10);
        tablaFiltros.add(filtroPeriodo).width(120);

        tablaPrincipal.add(tablaFiltros).padBottom(20).row();

        // tabla ranking
        tablaRanking = new Table();
        actualizarTablaRanking();

        scrollRanking = new ScrollPane(tablaRanking, skin);
        scrollRanking.setScrollingDisabled(true, false);
        scrollRanking.setFadeScrollBars(false);
        tablaPrincipal.add(scrollRanking).size(700, 300).padBottom(20).row();

        // info posicion actual
        labelPosicionActual = new Label("", estiloLabel);
        labelPosicionActual.setColor(Color.YELLOW);
        actualizarPosicionActual();
        tablaPrincipal.add(labelPosicionActual).padBottom(20).row();

        // botones
        Table tablaBotones = new Table();
        // estilo para ImageTextButton
        ImageTextButton.ImageTextButtonStyle estiloBtnActualizar = new ImageTextButton.ImageTextButtonStyle();
        estiloBtnActualizar.up = new TextureRegionDrawable(new TextureRegion(texturaActualizar));
        estiloBtnActualizar.down = new TextureRegionDrawable(new TextureRegion(texturaActualizar2));
        estiloBtnActualizar.font = mediumFont; // 

        ImageTextButton btnActualizar = new ImageTextButton(
                Idiomas.getInstance().obtenerTexto("ranking.boton_actualizar"),
                estiloBtnActualizar
        );
        btnActualizar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!puedeInteractuar) {
                    return;
                }
                actualizarRanking();
                mostrarMensaje(Idiomas.getInstance().obtenerTexto("ranking.actualizado"), Color.GREEN);
            }
        });

        ImageTextButton btnMiPerfil = new ImageTextButton(
                Idiomas.getInstance().obtenerTexto("ranking.boton_perfil"),
                estiloBtnActualizar
        );
        btnMiPerfil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!puedeInteractuar) {
                    return;
                }
                if (sistemaUsuarios.haySesionActiva()) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                } else {
                    mostrarMensaje(Idiomas.getInstance().obtenerTexto("ranking.no_sesion"), Color.RED);
                }
            }
        });

        ImageTextButton btnVolver = new ImageTextButton(
                Idiomas.getInstance().obtenerTexto("ranking.boton_volver"),
                estiloBtnActualizar
        );
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!puedeInteractuar) {
                    return;
                }
                ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
            }
        });

        tablaBotones.add(btnActualizar).size(120, 50).padRight(15);
        tablaBotones.add(btnMiPerfil).size(150, 50).padRight(15);
        tablaBotones.add(btnVolver).size(120, 50);

        tablaPrincipal.add(tablaBotones).row();
        stage.addActor(tablaPrincipal);
    }

    private void actualizarRanking() {
        cargarDatosUsuarios();
        actualizarTablaRanking();
        actualizarPosicionActual();
    }

    private void ordenarDatos() {
        Comparator<DatosUsuario> comparador = switch (tipoFiltroActual) {
            case PUNTUACION_TOTAL ->
                (a, b) -> Integer.compare(b.puntuacionTotal, a.puntuacionTotal);
            case NIVEL_MAXIMO ->
                (a, b) -> {
                    int cmp = Integer.compare(b.nivelMaximo, a.nivelMaximo);
                    return cmp != 0 ? cmp : Integer.compare(b.puntuacionTotal, a.puntuacionTotal);
                };
            case PARTIDAS_COMPLETADAS ->
                (a, b) -> Integer.compare(b.partidasCompletadas, a.partidasCompletadas);
            case TIEMPO_JUGADO ->
                (a, b) -> Long.compare(b.tiempoTotal, a.tiempoTotal);
        };

        Collections.sort(datosUsuarios, comparador);
    }

    private void actualizarTablaRanking() {
        tablaRanking.clear();

        Table header = new Table(skin);
        header.setBackground(skin.newDrawable("default-round", new Color(0.3f, 0.3f, 0.3f, 0.9f)));
        header.pad(8);

        Label headerPos = new Label(Idiomas.getInstance().obtenerTexto("ranking.header_pos"), skin);
        headerPos.setColor(Color.YELLOW);
        Label headerNombre = new Label(Idiomas.getInstance().obtenerTexto("ranking.header_jugador"), skin);
        headerNombre.setColor(Color.YELLOW);
        Label headerValor = new Label(obtenerTituloValor(), skin);
        headerValor.setColor(Color.YELLOW);
        Label headerNivel = new Label(Idiomas.getInstance().obtenerTexto("ranking.header_nivel"), skin);
        headerNivel.setColor(Color.YELLOW);

        header.add(headerPos).width(50).padRight(10);
        header.add(headerNombre).width(200).padRight(20);
        header.add(headerValor).width(150).padRight(20);
        header.add(headerNivel).width(50);

        tablaRanking.add(header).width(680).padBottom(5).row();

        for (int i = 0; i < datosUsuarios.size(); i++) {
            crearFilaUsuario(i + 1, datosUsuarios.get(i));
        }
    }

    private void crearFilaUsuario(int posicion, DatosUsuario datos) {
        Table fila = new Table(skin);
        Color colorFondo = new Color(0.15f, 0.15f, 0.15f, 0.8f);

        boolean esUsuarioActual = sistemaUsuarios.haySesionActiva()
                && sistemaUsuarios.getUsuarioActual().getNombreUsuario().equals(datos.nombreUsuario);

        if (esUsuarioActual) {
            colorFondo = new Color(0.2f, 0.4f, 0.2f, 0.8f);
        } else if (posicion <= 3) {
            colorFondo = new Color(0.3f, 0.2f, 0.1f, 0.8f);
        }

        fila.setBackground(skin.newDrawable("default-round", colorFondo));
        fila.pad(8);

        String textoPos = String.valueOf(posicion);
        Color colorPos = Color.WHITE;
        if (posicion == 1) {
            colorPos = Color.GOLD;
        } else if (posicion == 2) {
            colorPos = Color.LIGHT_GRAY;
        } else if (posicion == 3) {
            colorPos = new Color(0.8f, 0.5f, 0.2f, 1f);
        }

        Label labelPos = new Label(textoPos, skin);
        labelPos.setColor(colorPos);

        String nombreMostrar = datos.nombreCompleto + " (" + datos.nombreUsuario + ")";
        if (nombreMostrar.length() > 25) {
            nombreMostrar = nombreMostrar.substring(0, 22) + "...";
        }
        Label labelNombre = new Label(nombreMostrar, skin);
        labelNombre.setColor(esUsuarioActual ? Color.CYAN : Color.WHITE);

        Label labelValor = new Label(obtenerValorMostrar(datos), skin);
        labelValor.setColor(Color.LIGHT_GRAY);

        Label labelNivel = new Label(datos.nivelMaximo + "/7", skin);
        labelNivel.setColor(datos.nivelMaximo == 7 ? Color.GREEN : Color.YELLOW);

        fila.add(labelPos).width(50).padRight(10);
        fila.add(labelNombre).width(200).left().padRight(20);
        fila.add(labelValor).width(150).right().padRight(20);
        fila.add(labelNivel).width(50).center();

        tablaRanking.add(fila).width(680).padBottom(3).row();
    }

    private String obtenerTituloValor() {
        return switch (tipoFiltroActual) {
            case PUNTUACION_TOTAL ->
                Idiomas.getInstance().obtenerTexto("ranking.header_puntos");
            case NIVEL_MAXIMO ->
                Idiomas.getInstance().obtenerTexto("ranking.header_nivel");
            case PARTIDAS_COMPLETADAS ->
                Idiomas.getInstance().obtenerTexto("ranking.header_completadas");
            case TIEMPO_JUGADO ->
                Idiomas.getInstance().obtenerTexto("ranking.header_tiempo");
        };
    }

    private String obtenerValorMostrar(DatosUsuario datos) {
        return switch (tipoFiltroActual) {
            case PUNTUACION_TOTAL ->
                String.format("%,d %s", datos.puntuacionTotal, Idiomas.getInstance().obtenerTexto("ranking.puntos_abreviatura"));
            case NIVEL_MAXIMO ->
                Idiomas.getInstance().obtenerTexto("ranking.nivel") + " " + datos.nivelMaximo;
            case PARTIDAS_COMPLETADAS ->
                datos.partidasCompletadas + " " + Idiomas.getInstance().obtenerTexto("ranking.partidas");
            case TIEMPO_JUGADO -> {
                long horas = datos.tiempoTotal / 3600000;
                long minutos = (datos.tiempoTotal % 3600000) / 60000;
                yield String.format("%dh %dm", horas, minutos);
            }
        };
    }

    private void actualizarPosicionActual() {
        if (!sistemaUsuarios.haySesionActiva()) {
            labelPosicionActual.setText(Idiomas.getInstance().obtenerTexto("ranking.sin_sesion"));
            labelPosicionActual.setColor(Color.GRAY);
            return;
        }

        String nombreUsuario = sistemaUsuarios.getUsuarioActual().getNombreUsuario();
        int posicion = -1;

        for (int i = 0; i < datosUsuarios.size(); i++) {
            if (datosUsuarios.get(i).nombreUsuario.equals(nombreUsuario)) {
                posicion = i + 1;
                break;
            }
        }

        if (posicion != -1) {
            String texto = String.format(Idiomas.getInstance().obtenerTexto("ranking.posicion_actual"), posicion, datosUsuarios.size());
            labelPosicionActual.setText(texto);
            labelPosicionActual.setColor(Color.YELLOW);
        } else {
            labelPosicionActual.setText(Idiomas.getInstance().obtenerTexto("ranking.no_encontrado"));
            labelPosicionActual.setColor(Color.RED);
        }
    }

    private void mostrarMensaje(String texto, Color color) {
        Label mensajeTemporal = new Label(texto, skin);
        mensajeTemporal.setColor(color);
        mensajeTemporal.setPosition(10, Gdx.graphics.getHeight() - 50);
        stage.addActor(mensajeTemporal);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Gdx.app.postRunnable(mensajeTemporal::remove);
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
        if (texturaActualizar != null) {
            texturaActualizar.dispose();
        }
        if (texturaActualizar2 != null) {
            texturaActualizar2.dispose();
        }
        if (texturaPerfil != null) {
            texturaPerfil.dispose();
        }
        if (texturaPerfil2 != null) {
            texturaPerfil2.dispose();
        }
    }
}
