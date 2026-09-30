package com.tuusuario.rpg.model;

public class Combate {
    private Personaje jugador;
    private Enemigo enemigo;
    private boolean turnoJugador=true;
    private boolean combateActivo=true;


    public Combate(Personaje jugador, Enemigo enemigo) {
        this.jugador = jugador;
        this.enemigo = enemigo;
        this.turnoJugador = true; // Empieza el jugador
    }

    public void defender() {
        if (!estaActivo() || !esTurnoJugador()) return;

        // Opcional: Si tienes una variable para indicar que el personaje se defiende este turno
        // jugador.setDefendiendo(true);

        // Pasa el turno al enemigo (ajusta 'turnoJugador' al nombre real de tu atributo booleano)
        this.turnoJugador = false;
    }

    // Acciones del jugador
    public boolean realizarAtaqueBasico() {
        if (!turnoJugador || !estaActivo()) return false;

        enemigo.recibirDanio(jugador.getAtq());
        turnoJugador = false; // Pasa el turno al enemigo
        return true;
    }

    public boolean realizarHabilidadEspecial() {
        if (!turnoJugador || !estaActivo()) return false;

        // Comprobamos si tiene PM según la clase de personaje
        if (jugador instanceof Camorrista) {
            Camorrista c = (Camorrista) jugador;
            if (c.getPM() >= 10) {
                c.Especial(enemigo);
                turnoJugador = false;
                return true;
            }
        } else if (jugador instanceof Piromano) {
            Piromano p = (Piromano) jugador;
            if (p.getPM() >= 15) {
                p.Especial(enemigo);
                turnoJugador = false;
                return true;
            }
        }
        return false; // No había suficiente PM
    }

    // Acción del enemigo
    public void ejecutarTurnoEnemigo() {
        if (turnoJugador || !estaActivo()) return;

        jugador.recibirDanio(enemigo.getAtq());
        turnoJugador = true; // Vuelve el turno al jugador
    }

    // Verificaciones de estado
    public boolean estaActivo() {
        return jugador.getPs() > 0 && enemigo.getPs() > 0;
    }

    // Getters y Setters
    public Personaje getJugador() { return jugador; }
    public Enemigo getEnemigo() { return enemigo; }
    public boolean esTurnoJugador() { return turnoJugador; }
}
