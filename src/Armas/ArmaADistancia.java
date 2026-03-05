package Armas;

import Personajes.Personaje;

public class ArmaADistancia extends Arma{
	private double ignorarDefensa;
	public ArmaADistancia(String nombre, int dañoBase, double probCritico, double multiplicadorCritico, double ignorarDefensa) {
		super(nombre, dañoBase, probCritico, multiplicadorCritico);
		this.ignorarDefensa = ignorarDefensa;
	}
	@Override
	public int calcularDaño(Personaje atacante, Personaje defensor) {
		// TODO Auto-generated method stub
		int daño = dañoBase + atacante.getAtaqueBase() + (int)(defensor.getDefensaBase() * ignorarDefensa);
		if (esCritico()) {
			daño = (int)(daño * multiplicadorCritico);
			System.out.println(" [CRITICO]");
		}
		return daño;
	}
	
	@Override public String descripcion() {return "[Dist] " + super.descripcion(); }
}
