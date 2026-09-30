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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tuusuario.rpg.model.*;

public class SeleccionPersonajeScreen implements Screen {

    private final MainGdx game;
    private Stage escenario;
    private Skin skin;

    private Texture texCamorrista;
    private Texture texPiromano;
    private Texture texQuarterback;
    private Texture texMatematico;

    public SeleccionPersonajeScreen(MainGdx game) {
        this.game = game;
    }

    @Override
    public void show() {
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        texCamorrista = new Texture(Gdx.files.internal("camorrista.png"));
        texPiromano   = new Texture(Gdx.files.internal("piromano.png"));
        texQuarterback= new Texture(Gdx.files.internal("quarterback.png"));
        texMatematico = new Texture(Gdx.files.internal("matematico.png"));

        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();

        Label titulo = new Label("SELECCIONA TU ESTUDIANTE", skin);
        tablaPrincipal.add(titulo).colspan(4).padBottom(25).row();

        // Margen entre tarjetas (pad) ajustado para que quepan bien las imágenes más anchas
        tablaPrincipal.add(crearTarjetaPersonaje(texCamorrista, "Salvador", "Camorrista", new Camorrista())).pad(10);
        tablaPrincipal.add(crearTarjetaPersonaje(texPiromano, "Yuso", "Piromano", new Piromano())).pad(10);
        tablaPrincipal.add(crearTarjetaPersonaje(texQuarterback, "Antonio", "QuarterBack", new QuarterBack())).pad(10);
        tablaPrincipal.add(crearTarjetaPersonaje(texMatematico, "Migue", "Matematico", new Matemático())).pad(10);

        escenario.addActor(tablaPrincipal);
    }

    private Table crearTarjetaPersonaje(Texture textura, String nombre, String clase, Personaje personajeObj) {
        Table tarjeta = new Table();

        Image imgPersonaje = new Image(textura);
        Label lblNombre = new Label(nombre, skin);
        Label lblClase = new Label("(" + clase + ")", skin);
        TextButton btnElegir = new TextButton("ELEGIR", skin);

        ClickListener clickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HistoriaScreen(game,personajeObj));
            }
        };

        btnElegir.addListener(clickListener);
        imgPersonaje.addListener(clickListener);

        // 🔹 CAMBIO AQUÍ: Aumentamos el ancho a 190 px (y ajustamos el botón a 160 px)
        tarjeta.add(imgPersonaje).size(190, 280).padBottom(10).row();
        tarjeta.add(lblNombre).padBottom(2).row();
        tarjeta.add(lblClase).padBottom(10).row();
        tarjeta.add(btnElegir).width(160).height(35);

        return tarjeta;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
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
        texCamorrista.dispose();
        texPiromano.dispose();
        texQuarterback.dispose();
        texMatematico.dispose();
    }
}
