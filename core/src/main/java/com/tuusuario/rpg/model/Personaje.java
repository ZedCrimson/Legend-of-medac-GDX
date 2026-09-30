package com.tuusuario.rpg.model;


public class Personaje {
    // 1. Atributos privados
    protected String nombre;
    protected String clase;
    protected int PS;
    protected int ATQ;
    protected int DEF;
    protected int PM;

    // 2. El constructor DEBE ir DENTRO de las llaves de la clase
    public Personaje(String nombre, String clase, int PS, int ATQ, int DEF,int PM) {
        this.nombre = nombre;
        this.clase = clase;
        this.PS = PS;
        this.ATQ = ATQ;
        this.DEF = DEF;
        this.PM = PM;
    }

    // 3. Métodos Getter para poder leer this.PS, this.ATQ, etc. desde el selector
    public String getNombre() {
        return nombre;
    }

    public String getClase() {
        return clase;
    }

    public int getPs() {
        return PS;
    }

    public int getAtq() {
        return ATQ;
    }

    public int getDef() {
        return DEF;
    }

    public int getPM(){
        return PM;
    }

    // 4. Método básico para recibir daño en los combates
    public void recibirDanio(int cantidad) {
        int danioReal = cantidad - this.DEF;
        if (danioReal < 5) {
            danioReal = 5; // Daño mínimo para que los ataques siempre hagan algo
        }
        this.PS -= danioReal;
        if (this.PS < 0) {
            this.PS = 0;
        }
        System.out.println(this.nombre + " recibe " + danioReal + " de daño. PS restantes: " + this.PS);
    }

    public boolean consumirPm(int cantidad) {
        if (this.PM >= cantidad) {
            this.PM -= cantidad;
            return true; // Tenía suficiente maná y se restó
        }
        return false; // No hay suficiente maná
    }
}
