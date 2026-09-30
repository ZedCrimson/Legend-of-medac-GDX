package com.tuusuario.rpg.model; // Usa el nombre de tu paquete actual

public class Enemigo extends Personaje {

    // Constructor que recibe todos los datos (útil cuando lo cargamos desde MySQL)
    public Enemigo(String nombre, int PS, int ATQ, int DEF,int PM){
        // Llamamos al constructor de la clase padre (Personaje)
        // Le pasamos el nombre, la clase ("Enemigo"), los PS, ATQ y DEF
        super(nombre, "Enemigo", PS, ATQ, DEF,PM);
    }

    // --- MÉTODOS DE ACCIÓN ---

    // Ataque básico del enemigo hacia el jugador
    public void atacar(Personaje objetivo) {
        System.out.println("\n¡El " + this.nombre + " realiza un ataque a " + objetivo.getNombre() + "!");
        objetivo.recibirDanio(this.ATQ);
    }
}
