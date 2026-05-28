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
	public int calcularDaño(Personaje defensor) {
	    int defensaIgnorada = (int)(defensor.getDefensaBase() * ignorarDefensa);
	    int daño = dañoBase + defensaIgnorada; // tampoco puede usar atacante.getAtaqueBase()
	    if (esCritico()) {
	        daño = (int)(daño * multiplicadorCritico);
	        System.out.println("  [CRITICO]");
	    }
	    return Math.max(1, daño);
	}

	@Override
	public String descripcion() { return "[Dist] " + super.descripcion(); }
}