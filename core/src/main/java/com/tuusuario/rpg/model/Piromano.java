package com.tuusuario.rpg.model;

public class Piromano extends Personaje {

    // Constructor por defecto para la clase Piromano (con las estadísticas prefijadas)
    public Piromano() {
        // llama al constructor de Personaje(nombre, clase, ps, atq, def)
        super("Yuso", "Piromano", 60, 70, 30,60);
    }

    // Habilidad especial o propia del Camorrista
    public void Especial(Personaje objetivo) {
        System.out.println("\n¡" + this.nombre + " lanza un vial inflamable a " + objetivo.getNombre() + "!");
        int danioEspecial = this.ATQ + 15; // Golpe mejorado
        objetivo.recibirDanio(danioEspecial);
    }
}
