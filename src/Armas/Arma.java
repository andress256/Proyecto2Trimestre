package Armas;

import Personajes.Personaje;

public abstract class Arma {
	protected String nombre;
	protected int dañoBase;
	protected double probCritico;
	protected double multiplicadorCritico;
	
	
	public Arma (String nombre, int dañoBase, double probCritico, double multiplicadorCritico) {
		
		this.nombre = nombre;
		this.dañoBase = dañoBase;
		this.probCritico = probCritico;
		this.multiplicadorCritico = multiplicadorCritico;
	}
	
	public abstract int calcularDaño(Personaje atacante, Personaje defensor);
		
	protected boolean esCritico() {
		return Math.random() < probCritico;
	}
	
	public String descripcion() {
		return nombre + " [Daño: " + dañoBase + " | Crit: " + (int)(probCritico * 100) + "%]";
	}
	
	public String getNombre() { return nombre;}
	public int getDañoBase() { return dañoBase;}
}
