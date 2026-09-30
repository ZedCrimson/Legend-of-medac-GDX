package com.tuusuario.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuPrincipalScreen implements Screen {

    private final MainGdx game;
    private Stage escenario;
    private Skin skin;

    public MenuPrincipalScreen(MainGdx game) {
        this.game = game;
    }

    @Override
    public void show() {
        // 1. El escenario controla todos los botones y textos
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario); // Permite hacer clic con el ratón

        // 2. Cargamos los archivos de la skin de la carpeta assets
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // 3. La tabla ayuda a colocar las cosas organizadas como en un HTML o Excel
        Table tabla = new Table();
        tabla.setFillParent(true); // Ocupa toda la ventana
        tabla.center();            // Centra todo en medio de la pantalla

        // 4. Creación del título y botones
        Label titulo = new Label("LEGEND OF MEDAC", skin);
        TextButton btnJugar = new TextButton("NUEVA PARTIDA", skin);
        TextButton btnSalir = new TextButton("SALIR", skin);

        // 5. Acción al pulsar el botón "SALIR"
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.introVista = false; // Reiniciamos el control de la intro para una nueva partida
                game.setScreen(new HistoriaScreen(game, null)); // Abrimos la historia sin personaje inicial
            }
        });

        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Cierra la ventana del juego
            }
        });

        // 6. Colocamos los elementos en la tabla de arriba a abajo
        tabla.add(titulo).padBottom(30).row();
        tabla.add(btnJugar).width(200).height(45).padBottom(10).row();
        tabla.add(btnSalir).width(200).height(45);

        // 7. Añadimos la tabla al escenario
        escenario.addActor(tabla);
    }

    @Override
    public void render(float delta) {
        // Pinta el fondo de color azul oscuro
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibuja los botones y detecta el ratón
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
    }
}
