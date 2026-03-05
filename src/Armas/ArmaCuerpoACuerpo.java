package Armas;

import Personajes.Personaje;

public class ArmaCuerpoACuerpo extends Arma{

	public ArmaCuerpoACuerpo(String nombre, int dañoBase, double probCritico, double multiplicadorCritico) {
		super(nombre, dañoBase, probCritico, multiplicadorCritico);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calcularDaño(Personaje atacante, Personaje defensor) {
		// TODO Auto-generated method stub
		 int daño = dañoBase + (int)(atacante.getAtaqueBase() * 1.3);
	        if (esCritico()) {
	            daño = (int)(daño * multiplicadorCritico);
	            System.out.println("  [CRITICO!]");
	        }
	        return daño; 
	        
    } //44y
	@Override public String descripcion() { return "[CaC] " + super.descripcion(); }
	
}
