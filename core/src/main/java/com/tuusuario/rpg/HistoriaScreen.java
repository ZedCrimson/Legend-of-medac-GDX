package com.tuusuario.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tuusuario.rpg.model.*;

public class HistoriaScreen implements Screen {

    private final MainGdx game;
    private final Personaje jugador;
    private Stage escenario;
    private Skin skin;
    private Texture texPersonaje;

    private Table tablaHistoria;
    private Table tablaDialogo;

    public HistoriaScreen(MainGdx game, Personaje jugador) {
        this.game = game;
        this.jugador = jugador;
    }

    @Override
    public void show() {
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Solo cargamos la imagen si ya tenemos un personaje seleccionado
        if (jugador != null) {
            String archivoImagen = obtenerRutaImagen(jugador);
            texPersonaje = new Texture(Gdx.files.internal(archivoImagen));
            crearTablaDialogo();
        }

        crearTablaHistoria();

        // 🔹 Si ya leyó la intro y tiene personaje, enseña el diálogo. De lo contrario, la historia.
        if (game.introVista && jugador != null) {
            escenario.addActor(tablaDialogo);
        } else {
            escenario.addActor(tablaHistoria);
        }
    }

    private String obtenerRutaImagen(Personaje p) {
        if (p instanceof Camorrista) return "camorrista.png";
        if (p instanceof Piromano) return "piromano.png";
        if (p instanceof QuarterBack) return "quarterback.png";
        if (p instanceof Matemático) return "matematico.png";
        return "camorrista.png";
    }

    private String obtenerFraseDialogo(Personaje p) {
        if (p instanceof Camorrista) {
            return "Salvador: \"¿Invasion en el instituto? Me da igual de que ciclo sean, ¡a golpe limpio voy a solucionar esto!\"";
        } else if (p instanceof Piromano) {
            return "Yuso: \"Huele a quemado... pero esta vez no he sido yo. ¡Es hora de darle un poco de calor a este problema!\"";
        } else if (p instanceof QuarterBack) {
            return "Antonio: \"He ganado partidos con peor tactica que esta. Toca arrollar a cualquiera que intente bloquearnos.\"";
        } else if (p instanceof Matemático) {
            return "Migue: \"Sus calculos para romper la realidad tienen un fallo evidente. Vamos a corregir sus numeros en la practica.\"";
        }
        return "...";
    }

    private void crearTablaHistoria() {
        tablaHistoria = new Table();
        tablaHistoria.setFillParent(true);
        tablaHistoria.center();

        Label lblTitulo = new Label("INTRODUCCION", skin);

        String textoHistoria = "Llegas a tu centro de estudios como un día cualquiera, mochila al hombro y con el cafe a medio terminar.\n\n"
            + "Pero al cruzar el umbral del edificio, algo anda mal. El aire huele a ozono quemado, las luces parpadean en un ritmo frenetico y los pasillos que conocias de memoria ahora parecen un laberinto distorsionado y fuera de control.\n\n"
            + "Un rumor corre entre los pocos estudiantes desorientados: los del otro Ciclo Superior han metido la mano donde no debian. Su ultimo \"experimento de clase\" se les ha salido de las manos y ha alterado la realidad de todo el instituto.\n\n"
            + "Tu zona de clase ya no es un lugar seguro. Es hora de organizarte con los otros estudiantes y arreglar esto.";

        Label lblHistoria = new Label(textoHistoria, skin);
        lblHistoria.setWrap(true);
        lblHistoria.setAlignment(Align.center);

        TextButton btnSiguiente = new TextButton("SIGUIENTE", skin);
        btnSiguiente.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.introVista = true; // Marcamos que la historia ya fue leída
                // 🔹 Nos lleva directamente a la pantalla de selección de personaje
                game.setScreen(new SeleccionPersonajeScreen(game));
            }
        });

        tablaHistoria.add(lblTitulo).padBottom(25).row();
        tablaHistoria.add(lblHistoria).width(750).padBottom(35).row();
        tablaHistoria.add(btnSiguiente).width(200).height(45);
    }

    private void crearTablaDialogo() {
        tablaDialogo = new Table();
        tablaDialogo.setFillParent(true);
        tablaDialogo.center();

        Image imgActor = new Image(texPersonaje);
        Label lblDialogo = new Label(obtenerFraseDialogo(jugador), skin);
        lblDialogo.setWrap(true);

        Table cuadroTexto = new Table(skin);
        cuadroTexto.setBackground("default-pane");
        cuadroTexto.add(lblDialogo).width(480).pad(20);

        // Botón para cambiar de personaje si el jugador quiere elegir otro
        TextButton btnVolver = new TextButton("ELEGIR OTRO", skin);
        btnVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SeleccionPersonajeScreen(game));
            }
        });

        TextButton btnEntrarBatalla = new TextButton("IR A LA BATALLA", skin);
        btnEntrarBatalla.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new BatallaScreen(game, jugador));
            }
        });

        Table layoutHorizontal = new Table();
        layoutHorizontal.add(imgActor).size(260, 320).padRight(20);
        layoutHorizontal.add(cuadroTexto).width(480).padBottom(10);

        Table botonesBot = new Table();
        botonesBot.add(btnVolver).width(180).height(45).padRight(20);
        botonesBot.add(btnEntrarBatalla).width(220).height(45);

        tablaDialogo.add(layoutHorizontal).padBottom(25).row();
        tablaDialogo.add(botonesBot);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        escenario.act(delta);
        escenario.draw();
    }

    @Override public void resize(int width, int height) { escenario.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        escenario.dispose();
        skin.dispose();
        if (texPersonaje != null) texPersonaje.dispose();
    }
}
