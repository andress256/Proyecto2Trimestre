package Armas;

import Personajes.Personaje;

	// Clase base abstracta para todas las armas del juego.
	// Cada tipo de arma calcula el daño de forma distinta.
public abstract class Arma {
	protected String nombre;
	protected int dañoBase;
	protected double probCritico;         // probabilidad de critico entre 0 y 1
	protected double multiplicadorCritico; // por cuanto se multiplica el daño en critico

	protected Personaje portador;
	
	public void setPortador(Personaje portador) {
		this.portador = portador;
	}
	
	public Arma(String nombre, int dañoBase, double probCritico, double multiplicadorCritico) {
		this.nombre = nombre;
		this.dañoBase = dañoBase;
		this.probCritico = probCritico;
		this.multiplicadorCritico = multiplicadorCritico;
	}

	// Cada subclase define como calcula su daño
	public abstract int calcularDaño(Personaje defensor);

	// Devuelve true aleatoriamente segun la probabilidad de critico
	protected boolean esCritico() {
		return Math.random() < probCritico;
	}

	public String descripcion() {
		return nombre + " [Daño: " + dañoBase + " | Crit: " + (int)(probCritico * 100) + "%]";
	}

	public String getNombre() { return nombre; }
	public int getDañoBase() { return dañoBase; }

	
}
