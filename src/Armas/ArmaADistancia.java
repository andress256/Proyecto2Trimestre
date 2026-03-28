package Armas;

import Personajes.Personaje;

// Arma a distancia: ignora parte de la defensa del enemigo
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
		// Suma parte de la defensa del enemigo al daño para simular penetracion
		int daño = dañoBase + atacante.getAtaqueBase() + (int)(defensor.getDefensaBase() * ignorarDefensa);
		if (esCritico()) {
			daño = (int)(daño * multiplicadorCritico);
			System.out.println(" [CRITICO]");
		}
		return daño;
	}

	@Override
	public String descripcion() { return "[Dist] " + super.descripcion(); }
}

