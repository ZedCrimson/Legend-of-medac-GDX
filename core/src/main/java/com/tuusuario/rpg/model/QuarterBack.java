package com.tuusuario.rpg.model;

public class QuarterBack extends Personaje {

    // Constructor por defecto para la clase Camorrista (con las estadísticas prefijadas)
    public QuarterBack() {
        // llama al constructor de Personaje(nombre, clase, ps, atq, def)
        super("Antonio", "QuarterBack", 100, 20, 60,20);
    }

    // Habilidad especial o propia del Camorrista
    public void Especial(Personaje objetivo) {
        System.out.println("\n¡" + this.nombre + " hace un placaje a " + objetivo.getNombre() + "!");
        int danioEspecial = this.ATQ + 15; // Golpe mejorado
        objetivo.recibirDanio(danioEspecial);
    }
}
