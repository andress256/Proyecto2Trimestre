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
	    // La defensa ignorada se suma al daño (representa la penetración)
	    // Si ignorarDefensa = 0.40, suma el 40% de la defensa del enemigo al daño
	    int defensaIgnorada = (int)(defensor.getDefensaBase() * ignorarDefensa);
	    int daño = dañoBase + atacante.getAtaqueBase() + defensaIgnorada;
	    if (esCritico()) {
	        daño = (int)(daño * multiplicadorCritico);
	        System.out.println("  [CRITICO]");
	    }
	    return Math.max(1, daño);
	}

	@Override
	public String descripcion() { return "[Dist] " + super.descripcion(); }
}