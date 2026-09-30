package com.tuusuario.rpg.model;

public class Matemático extends Personaje {

    // Constructor por defecto para la clase Camorrista (con las estadísticas prefijadas)
    public Matemático() {
        // llama al constructor de Personaje(nombre, clase, ps, atq, def)
        super("Migue", "Matemático", 70, 20, 35,70);
    }

    // Habilidad especial o propia del Camorrista
    public void Especial(Personaje objetivo) {
        System.out.println("\n¡" + this.nombre + " hace cálculos mas allá de la comprensión humana " + objetivo.getNombre() + "!");
        int danioEspecial = this.ATQ + 20; // Golpe mejorado
        objetivo.recibirDanio(danioEspecial);
    }
}

