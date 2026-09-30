package com.tuusuario.rpg.model;

public class Camorrista extends Personaje {

    // Constructor por defecto para la clase Camorrista (con las estadísticas prefijadas)
    public Camorrista() {
        // llama al constructor de Personaje(nombre, clase, ps, atq, def)
        super("Salvador", "Camorrista", 100, 50, 45,30);
    }

    // Habilidad especial o propia del Camorrista
    public void Especial(Personaje objetivo) {
        System.out.println("\n¡" + this.nombre + " golpea al higado de " + objetivo.getNombre() + "!");
        int danioEspecial = this.ATQ + 15; // Golpe mejorado
        // Implementar gasto de PM al usar la habilidad

        //---------------------------------------------
        objetivo.recibirDanio(danioEspecial);
    }
}
