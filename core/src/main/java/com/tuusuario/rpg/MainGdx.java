package com.tuusuario.rpg;

import com.badlogic.gdx.Game;

public class MainGdx extends Game {

    public boolean introVista = false;


    @Override
    public void create() {
        // Al arrancar, le decimos a LibGDX que abra el Menú Principal
        this.setScreen(new MenuPrincipalScreen(this));
    }

    @Override
    public void render() {
        // Mantiene el bucle del juego actualizado
        super.render();
    }
}
