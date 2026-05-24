package Armas;

import Personajes.Personaje;

// Arma melee: el daño escala con el ataque base del personaje
// y tiene en cuenta la defensa del defensor (bidireccional)
public class ArmaCuerpoACuerpo extends Arma {

	public ArmaCuerpoACuerpo(String nombre, int dañoBase, double probCritico, double multiplicadorCritico) {
		super(nombre, dañoBase, probCritico, multiplicadorCritico);
	}

	@Override
	public int calcularDaño(Personaje atacante, Personaje defensor) {
	    // Daño base + 130% del ataque del atacante
	    // (la defensa se resta después en recibirDañoDeArma, no aquí)
	    int daño = dañoBase + (int)(atacante.getAtaqueBase() * 1.3);
	    if (esCritico()) {
	        daño = (int)(daño * multiplicadorCritico);
	        System.out.println("  [CRITICO!]");
	    }
	    return Math.max(1, daño);
	}

	@Override
	public String descripcion() { return "[CaC] " + super.descripcion(); }
}