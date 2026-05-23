package Armas;

import Personajes.Personaje;

// Arma a distancia: ignora parte de la defensa del enemigo (bidireccional)
public class ArmaADistancia extends Arma {

	// Porcentaje de defensa del defensor que se ignora (0.0 a 1.0)
	private double ignorarDefensa;

	public ArmaADistancia(String nombre, int dañoBase, double probCritico,
			double multiplicadorCritico, double ignorarDefensa) {
		super(nombre, dañoBase, probCritico, multiplicadorCritico);
		this.ignorarDefensa = ignorarDefensa;
	}

	@Override
	public int calcularDaño(Personaje atacante, Personaje defensor) {
		// La defensa efectiva se reduce segun el porcentaje de penetracion.
		// Si ignorarDefensa = 0.40, solo se aplica el 60% de la defensa del enemigo.
		int defensaEfectiva = (int)(defensor.getDefensaBase() * (1.0 - ignorarDefensa));
		int daño = dañoBase + atacante.getAtaqueBase() - defensaEfectiva;
		if (esCritico()) {
			daño = (int)(daño * multiplicadorCritico);
			System.out.println("  [CRITICO]");
		}
		return Math.max(1, daño); // minimo 1 de daño
	}

	@Override
	public String descripcion() { return "[Dist] " + super.descripcion(); }
}