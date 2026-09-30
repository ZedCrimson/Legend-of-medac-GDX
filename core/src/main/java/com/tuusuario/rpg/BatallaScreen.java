package com.tuusuario.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tuusuario.rpg.dao.EnemigoDAO;
import com.tuusuario.rpg.model.*;

import java.util.List;

public class BatallaScreen implements Screen {

    private final MainGdx game;
    private final Personaje jugador;
    private Enemigo enemigo;
    private Combate combate;

    private Stage escenarioUI;
    private Skin skin;
    private SpriteBatch batch;

    // Texturas de escena
    private Texture texFondo;
    private Texture texJugadorEspalda;
    private Texture texEnemigo;

    // Música de fondo
    private Music musicaBatalla;

    // Valores máximos de stats
    private int psMaxJugador;
    private int pmMaxJugador;

    // Elementos de la Tarjeta del Personaje (UI)
    private ProgressBar barVida;
    private ProgressBar barMana;
    private Label lblVidaValor;
    private Label lblManaValor;

    // Elementos del HUD General
    private Label lblEnemigoNombre;
    private Label lblEnemigoStats;
    private Label lblLog;

    public BatallaScreen(MainGdx game, Personaje jugador) {
        this.game = game;
        this.jugador = jugador;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        escenarioUI = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenarioUI);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // 🎵 Música de batalla
        try {
            musicaBatalla = Gdx.audio.newMusic(Gdx.files.internal("musica_batalla.mp3"));
            musicaBatalla.setLooping(true);
            musicaBatalla.setVolume(0.4f);
            musicaBatalla.play();
        } catch (Exception e) {
            System.err.println("No se pudo cargar la música: " + e.getMessage());
        }

        // Cargar texturas de sprites
        texFondo = new Texture(Gdx.files.internal("ClaseFondo.png"));
        texJugadorEspalda = new Texture(Gdx.files.internal(obtenerRutaImagenJugador(jugador)));

        // Cargar Enemigo desde la BD
        try {
            EnemigoDAO dao = new EnemigoDAO();
            List<Enemigo> enemigos = dao.obtenerTodosLosEnemigos();
            if (enemigos != null && !enemigos.isEmpty()) {
                enemigo = enemigos.get(0);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener enemigo de BD: " + e.getMessage());
        }

        if (enemigo == null) {
            enemigo = new Enemigo("Enemigo de Prueba", 50, 10, 2,1);
        }

        texEnemigo = new Texture(Gdx.files.internal(obtenerRutaImagenEnemigo(enemigo)));


        // Guardar valores máximos e inicializar lógica
        if (jugador != null) {
            psMaxJugador = Math.max(1, jugador.getPs());
            pmMaxJugador = Math.max(1, jugador.getPM());
            combate = new Combate(jugador, enemigo);
        }

        // ──────────────────────────────────────────────────────────
        // ESTRUCTURA DE LA INTERFAZ (UI)
        // ──────────────────────────────────────────────────────────

        Table rootTable = new Table();
        rootTable.setFillParent(true);

        // 1. Cabecera superior (Enemigo)
        Table tablaEnemigoTop = new Table();
        lblEnemigoNombre = new Label(enemigo.getNombre().toUpperCase(), skin);
        lblEnemigoStats = new Label("", skin);

        tablaEnemigoTop.add(lblEnemigoNombre).padTop(10).row();
        tablaEnemigoTop.add(lblEnemigoStats).padBottom(10);

        // 2. Panel Inferior
        Table panelInferior = new Table(skin);
        panelInferior.setBackground("default-pane");

        // --- Menú de comandos (Izquierda) ---
        TextButton btnAtaque = new TextButton("1. ATAQUE BASICO", skin);
        TextButton btnEspecial = new TextButton("2. ESPECIAL", skin);
        TextButton btnDefender = new TextButton("3. DEFENDER", skin);
        TextButton btnHuir = new TextButton("4. HUIR", skin);

        Table menuComandos = new Table();
        menuComandos.add(btnAtaque).fillX().padBottom(5).row();
        menuComandos.add(btnEspecial).fillX().padBottom(5).row();
        menuComandos.add(btnDefender).fillX().padBottom(5).row();
        menuComandos.add(btnHuir).fillX();

        // --- Tarjeta del Personaje (Centro - Estilo Fear & Hunger) ---
        Table tarjetaPJ = new Table(skin);

        Label lblNombrePJ = new Label(jugador != null ? jugador.getNombre() : "Héroe", skin);
        Image imgRetrato = new Image(texJugadorEspalda);

        // Barras de progreso de PS (rojo) y PM (azul)
        barVida = new ProgressBar(0, psMaxJugador, 1, false, crearEstiloBarra(new Color(0.85f, 0.15f, 0.15f, 1f)));
        barMana = new ProgressBar(0, pmMaxJugador, 1, false, crearEstiloBarra(new Color(0.15f, 0.45f, 0.85f, 1f)));

        lblVidaValor = new Label("", skin);
        lblManaValor = new Label("", skin);

        Table filaVidaTexto = new Table();
        filaVidaTexto.add(new Label("PS", skin)).left().expandX();
        filaVidaTexto.add(lblVidaValor).right();

        Table filaManaTexto = new Table();
        filaManaTexto.add(new Label("PM", skin)).left().expandX();
        filaManaTexto.add(lblManaValor).right();

        // Construcción vertical de la tarjeta[cite: 1]
        tarjetaPJ.add(lblNombrePJ).center().padBottom(4).row();
        tarjetaPJ.add(imgRetrato).size(110, 140).padBottom(6).row();
        tarjetaPJ.add(filaVidaTexto).fillX().row();
        tarjetaPJ.add(barVida).fillX().height(10).padBottom(4).row();
        tarjetaPJ.add(filaManaTexto).fillX().row();
        tarjetaPJ.add(barMana).fillX().height(10).row();

        // --- Log de combate (Derecha) ---
        lblLog = new Label("¡Un " + enemigo.getNombre() + " te frena el paso!", skin);
        lblLog.setWrap(true);

        Table areaLog = new Table();
        areaLog.add(lblLog).expandX().fillX().left();

        // Unir secciones al panel inferior
        panelInferior.add(menuComandos).width(170).pad(10).left();
        panelInferior.add(tarjetaPJ).width(150).pad(10).left();
        panelInferior.add(areaLog).expandX().fillX().pad(10);

        // Montar tabla raíz con centro transparente para los sprites
        rootTable.add(tablaEnemigoTop).top().expandX().row();
        rootTable.add().expand().row();
        rootTable.add(panelInferior).bottom().fillX();

        actualizarHUD();

        // ──────────────────────────────────────────────────────────
        // LISTENERS DE BOTONES
        // ──────────────────────────────────────────────────────────

        btnAtaque.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (combate != null && combate.estaActivo() && combate.esTurnoJugador()) {
                    combate.realizarAtaqueBasico();
                    lblLog.setText("Atacaste con éxito a " + enemigo.getNombre() + ".");
                    procesarTurnoEnemigo();
                }
            }
        });

        btnEspecial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (combate != null && combate.estaActivo() && combate.esTurnoJugador()) {
                    boolean exito = combate.realizarHabilidadEspecial();
                    if (exito) {
                        lblLog.setText("¡Has liberado tu habilidad especial!");
                        procesarTurnoEnemigo();
                    } else {
                        lblLog.setText("No tienes suficiente maná.");
                    }
                }
            }
        });

        btnDefender.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (combate != null && combate.estaActivo() && combate.esTurnoJugador()) {
                    combate.defender();
                    lblLog.setText("Te pones en posición defensiva.");
                    procesarTurnoEnemigo();
                }
            }
        });

        btnHuir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SeleccionPersonajeScreen(game));
            }
        });

        escenarioUI.addActor(rootTable);
    }

    private ProgressBar.ProgressBarStyle crearEstiloBarra(Color colorRelleno) {
        Pixmap pixFondo = new Pixmap(100, 10, Pixmap.Format.RGBA8888);
        pixFondo.setColor(new Color(0.15f, 0.15f, 0.15f, 0.9f));
        pixFondo.fill();
        TextureRegionDrawable drawableFondo = new TextureRegionDrawable(new TextureRegion(new Texture(pixFondo)));
        pixFondo.dispose();

        Pixmap pixRelleno = new Pixmap(100, 10, Pixmap.Format.RGBA8888);
        pixRelleno.setColor(colorRelleno);
        pixRelleno.fill();
        TextureRegionDrawable drawableRelleno = new TextureRegionDrawable(new TextureRegion(new Texture(pixRelleno)));
        pixRelleno.dispose();

        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = drawableFondo;
        style.knobBefore = drawableRelleno;
        return style;
    }

    private String obtenerRutaImagenEnemigo(Enemigo e) {
        if (e == null || e.getNombre() == null) {
            return "enemigo.png";
        }

        switch (e.getNombre().toLowerCase()) {
            case "goblin":
                return "goblin.png";
            case "orco":
                return "orco.png";
            case "esqueleto":
                return "esqueleto.png";
            default:
                return "enemigo.png";
        }
    }



    private String obtenerRutaImagenJugador(Personaje p) {
        if (p instanceof Camorrista) return "camorrista.png";
        if (p instanceof Piromano) return "piromano.png";
        if (p instanceof QuarterBack) return "quarterback.png";
        if (p instanceof Matemático) return "matematico.png";
        return "camorristaSprite.png";
    }

    private void procesarTurnoEnemigo() {
        actualizarHUD();

        if (combate == null) {
            lblLog.setText("Error: El combate no se ha iniciado correctamente.");
            return;
        }

        if (!combate.estaActivo()) {
            lblLog.setText("¡Has derrotado a " + enemigo.getNombre() + "!");
            return;
        }

        combate.ejecutarTurnoEnemigo();
        actualizarHUD();

        if (!combate.estaActivo()) {
            lblLog.setText("Has caído derrotado...");
        }
    }

    private void actualizarHUD() {
        if (jugador != null) {
            if (barVida != null) barVida.setValue(jugador.getPs());
            if (barMana != null) barMana.setValue(jugador.getPM());
            if (lblVidaValor != null) lblVidaValor.setText(String.valueOf(jugador.getPs()));
            if (lblManaValor != null) lblManaValor.setText(String.valueOf(jugador.getPM()));
        }

        if (enemigo != null && lblEnemigoStats != null) {
            lblEnemigoStats.setText("PS Enemigo: " + enemigo.getPs());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();

        batch.setProjectionMatrix(escenarioUI.getCamera().combined);
        batch.begin();

        // 1. Fondo de pantalla
        batch.draw(texFondo, 0, 0, ancho, alto);

        // 2. Sprite del Enemigo
        float anchoEnemigo = 260;
        float altoEnemigo = 340;
        float posXEnemigo = (ancho - anchoEnemigo) / 2f;
        float posYEnemigo = alto * 0.32f;
        batch.draw(texEnemigo, posXEnemigo, posYEnemigo, anchoEnemigo, altoEnemigo);

        batch.end();

        // 3. Dibujar la Interfaz por encima
        escenarioUI.act(delta);
        escenarioUI.draw();
    }

    @Override
    public void resize(int width, int height) {
        escenarioUI.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        if (musicaBatalla != null && musicaBatalla.isPlaying()) {
            musicaBatalla.pause();
        }
    }

    @Override
    public void resume() {
        if (musicaBatalla != null && !musicaBatalla.isPlaying()) {
            musicaBatalla.play();
        }
    }

    @Override
    public void hide() {
        if (musicaBatalla != null && musicaBatalla.isPlaying()) {
            musicaBatalla.stop();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        escenarioUI.dispose();
        skin.dispose();

        if (texFondo != null) texFondo.dispose();
        if (texJugadorEspalda != null) texJugadorEspalda.dispose();
        if (texEnemigo != null) texEnemigo.dispose();

        if (musicaBatalla != null) {
            musicaBatalla.stop();
            musicaBatalla.dispose();
        }
    }
}
