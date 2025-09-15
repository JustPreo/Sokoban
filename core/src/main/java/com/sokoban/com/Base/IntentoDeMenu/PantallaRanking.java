package com.sokoban.com.Base.IntentoDeMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sokoban.com.GestorArchivos;
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
    private String tipoFiltroActual = "Puntuacion Total";
    private String periodoFiltroActual = "Historico";

    // clase interna para manejar datos de usuarios
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
        // setup de texturas
        texturaExtra = new Texture(Gdx.files.internal("extra.png"));
        Drawable fondoExtra = new TextureRegionDrawable(new TextureRegion(texturaExtra));
        texturaExtra2 = new Texture(Gdx.files.internal("extra2.png"));
        Drawable fondoExtra2 = new TextureRegionDrawable(new TextureRegion(texturaExtra2));

        estiloExtra = new Button.ButtonStyle();
        estiloExtra.up = fondoExtra;
        estiloExtra.down = fondoExtra2;

        texturaVolver = new Texture(Gdx.files.internal("volver.png"));
        Drawable fondoVolver = new TextureRegionDrawable(new TextureRegion(texturaVolver));
        texturaVolver2 = new Texture(Gdx.files.internal("volver2.png"));
        Drawable fondoVolver2 = new TextureRegionDrawable(new TextureRegion(texturaVolver2));

        estiloVolver = new Button.ButtonStyle();
        estiloVolver.up = fondoVolver;
        estiloVolver.down = fondoVolver2;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        bg = new Texture("fondoM.png");

        cargarDatosUsuarios();
        crearInterfaz();
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

        // titulo
        Label titulo = new Label("TABLA DE LIDERES", skin);
        titulo.setColor(Color.CYAN);
        tablaPrincipal.add(titulo).padBottom(20).row();

        // filtros
        Table tablaFiltros = new Table();

        Label labelFiltroTipo = new Label("Ordenar por:", skin);
        labelFiltroTipo.setColor(Color.WHITE);
        filtroTipo = new SelectBox<>(skin);
        filtroTipo.setItems("Puntuacion Total", "Nivel Maximo", "Partidas Completadas", "Tiempo Jugado");
        filtroTipo.setSelected(tipoFiltroActual);
        filtroTipo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    tipoFiltroActual = filtroTipo.getSelected();
                    actualizarRanking();
                }
            }
        });

        Label labelFiltroPeriodo = new Label("Periodo:", skin);
        labelFiltroPeriodo.setColor(Color.WHITE);
        filtroPeriodo = new SelectBox<>(skin);
        filtroPeriodo.setItems("Historico", "Este Mes", "Esta Semana");
        filtroPeriodo.setSelected(periodoFiltroActual);
        filtroPeriodo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    periodoFiltroActual = filtroPeriodo.getSelected();
                    actualizarRanking();
                }
            }
        });

        tablaFiltros.add(labelFiltroTipo).padRight(10);
        tablaFiltros.add(filtroTipo).width(150).padRight(30);
        tablaFiltros.add(labelFiltroPeriodo).padRight(10);
        tablaFiltros.add(filtroPeriodo).width(120);

        tablaPrincipal.add(tablaFiltros).padBottom(20).row();

        // tabla de ranking
        tablaRanking = new Table();
        actualizarTablaRanking();

        scrollRanking = new ScrollPane(tablaRanking, skin);
        scrollRanking.setScrollingDisabled(true, false);
        scrollRanking.setFadeScrollBars(false);
        tablaPrincipal.add(scrollRanking).size(700, 300).padBottom(20).row();

        // info de posicion actual
        labelPosicionActual = new Label("", skin);
        labelPosicionActual.setColor(Color.YELLOW);
        actualizarPosicionActual();
        tablaPrincipal.add(labelPosicionActual).padBottom(20).row();

        // botones
        Table tablaBotones = new Table();

        // texturas botones
        texturaActualizar = new Texture(Gdx.files.internal("actualizar.png"));
        texturaActualizar2 = new Texture(Gdx.files.internal("actualizar2.png"));
        estiloActualizar = new Button.ButtonStyle();
        estiloActualizar.up = new TextureRegionDrawable(new TextureRegion(texturaActualizar));
        estiloActualizar.down = new TextureRegionDrawable(new TextureRegion(texturaActualizar2));

        texturaPerfil = new Texture(Gdx.files.internal("perfil.png"));
        texturaPerfil2 = new Texture(Gdx.files.internal("perfil2.png"));
        estiloPerfil = new Button.ButtonStyle();
        estiloPerfil.up = new TextureRegionDrawable(new TextureRegion(texturaPerfil));
        estiloPerfil.down = new TextureRegionDrawable(new TextureRegion(texturaPerfil2));

        // actualziar
        Button btnActualizar = new Button(estiloActualizar);
        btnActualizar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    actualizarRanking();
                    mostrarMensaje("Ranking actualizado", Color.GREEN);
                }
            }
        });

        // perfil
        Button btnMiPerfil = new Button(estiloPerfil);
        btnMiPerfil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    if (sistemaUsuarios.haySesionActiva()) {
                        ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                    } else {
                        mostrarMensaje("No hay sesion activa", Color.RED);
                    }
                }
            }
        });

        // volver
        Button btnVolver = new Button(estiloVolver);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (puedeInteractuar) {
                    ((Juegito) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                }
            }
        });

        tablaBotones.add(btnActualizar).size(120, 50).padRight(15);
        tablaBotones.add(btnMiPerfil).size(120, 50).padRight(15);
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
        Comparator<DatosUsuario> comparador = null;

        switch (tipoFiltroActual) {
            case "Puntuacion Total":
                comparador = (a, b) -> Integer.compare(b.puntuacionTotal, a.puntuacionTotal);
                break;
            case "Nivel Maximo":
                comparador = (a, b) -> {
                    int cmp = Integer.compare(b.nivelMaximo, a.nivelMaximo);
                    return cmp != 0 ? cmp : Integer.compare(b.puntuacionTotal, a.puntuacionTotal);
                };
                break;
            case "Partidas Completadas":
                comparador = (a, b) -> Integer.compare(b.partidasCompletadas, a.partidasCompletadas);
                break;
            case "Tiempo Jugado":
                comparador = (a, b) -> Long.compare(b.tiempoTotal, a.tiempoTotal);
                break;
        }

        if (comparador != null) {
            Collections.sort(datosUsuarios, comparador);
        }
    }

    private void actualizarTablaRanking() {
        tablaRanking.clear();

        // header
        Table header = new Table(skin);
        header.setBackground(skin.newDrawable("default-round", new Color(0.3f, 0.3f, 0.3f, 0.9f)));
        header.pad(8);

        Label headerPos = new Label("Pos", skin);
        headerPos.setColor(Color.YELLOW);
        Label headerNombre = new Label("Jugador", skin);
        headerNombre.setColor(Color.YELLOW);
        Label headerValor = new Label(obtenerTituloValor(), skin);
        headerValor.setColor(Color.YELLOW);
        Label headerNivel = new Label("Niv", skin);
        headerNivel.setColor(Color.YELLOW);

        header.add(headerPos).width(50).padRight(10);
        header.add(headerNombre).width(200).padRight(20);
        header.add(headerValor).width(150).padRight(20);
        header.add(headerNivel).width(50);

        tablaRanking.add(header).width(680).padBottom(5).row();

        // filas de usuarios
        for (int i = 0; i < datosUsuarios.size(); i++) {
            crearFilaUsuario(i + 1, datosUsuarios.get(i));
        }
    }

    private void crearFilaUsuario(int posicion, DatosUsuario datos) {
        Table fila = new Table(skin);
        Color colorFondo = new Color(0.15f, 0.15f, 0.15f, 0.8f);

        // resaltar usuario actual
        boolean esUsuarioActual = sistemaUsuarios.haySesionActiva()
                && sistemaUsuarios.getUsuarioActual().getNombreUsuario().equals(datos.nombreUsuario);

        if (esUsuarioActual) {
            colorFondo = new Color(0.2f, 0.4f, 0.2f, 0.8f); // verde oscuro
        } // destacar top 3
        else if (posicion <= 3) {
            colorFondo = new Color(0.3f, 0.2f, 0.1f, 0.8f); // dorado oscuro
        }

        fila.setBackground(skin.newDrawable("default-round", colorFondo));
        fila.pad(8);

        // posicion con medallas
        String textoPos = String.valueOf(posicion);
        Color colorPos = Color.WHITE;
        if (posicion == 1) {
            colorPos = Color.GOLD;
        } else if (posicion == 2) {
            colorPos = Color.LIGHT_GRAY;
        } else if (posicion == 3) {
            colorPos = new Color(0.8f, 0.5f, 0.2f, 1f); // bronce
        }

        Label labelPos = new Label(textoPos, skin);
        labelPos.setColor(colorPos);

        // nombre del jugador
        String nombreMostrar = datos.nombreCompleto + " (" + datos.nombreUsuario + ")";
        if (nombreMostrar.length() > 25) {
            nombreMostrar = nombreMostrar.substring(0, 22) + "...";
        }
        Label labelNombre = new Label(nombreMostrar, skin);
        labelNombre.setColor(esUsuarioActual ? Color.CYAN : Color.WHITE);

        // valor segun filtro
        Label labelValor = new Label(obtenerValorMostrar(datos), skin);
        labelValor.setColor(Color.LIGHT_GRAY);

        // nivel maximo
        Label labelNivel = new Label(datos.nivelMaximo + "/7", skin);
        labelNivel.setColor(datos.nivelMaximo == 7 ? Color.GREEN : Color.YELLOW);

        fila.add(labelPos).width(50).padRight(10);
        fila.add(labelNombre).width(200).left().padRight(20);
        fila.add(labelValor).width(150).right().padRight(20);
        fila.add(labelNivel).width(50).center();

        tablaRanking.add(fila).width(680).padBottom(3).row();
    }

    private String obtenerTituloValor() {
        switch (tipoFiltroActual) {
            case "Puntuacion Total":
                return "Puntos";
            case "Nivel Maximo":
                return "Nivel Max";
            case "Partidas Completadas":
                return "Completadas";
            case "Tiempo Jugado":
                return "Tiempo";
            default:
                return "Valor";
        }
    }

    private String obtenerValorMostrar(DatosUsuario datos) {
        switch (tipoFiltroActual) {
            case "Puntuacion Total":
                return String.format("%,d pts", datos.puntuacionTotal);
            case "Nivel Maximo":
                return "Nivel " + datos.nivelMaximo;
            case "Partidas Completadas":
                return datos.partidasCompletadas + " partidas";
            case "Tiempo Jugado":
                long horas = datos.tiempoTotal / 3600000;
                long minutos = (datos.tiempoTotal % 3600000) / 60000;
                return String.format("%dh %dm", horas, minutos);
            default:
                return "N/A";
        }
    }

    private void actualizarPosicionActual() {
        if (!sistemaUsuarios.haySesionActiva()) {
            labelPosicionActual.setText("Inicia sesion para ver tu posicion");
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
            String texto = String.format("Tu posicion actual: #%d de %d jugadores", posicion, datosUsuarios.size());
            labelPosicionActual.setText(texto);
            labelPosicionActual.setColor(Color.YELLOW);
        } else {
            labelPosicionActual.setText("No se encontro tu posicion en el ranking");
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
                Gdx.app.postRunnable(() -> mensajeTemporal.remove());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
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
    }
}
