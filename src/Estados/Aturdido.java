package Estados;

import Personajes.Personaje;

// Estado CONTROL: el personaje pierde su turno mientras este aturdido.
// Se aplica cuando la barra de aturdimiento se llena al recibir golpes.
public class Aturdido extends Estado {

	public Aturdido() {
		super("ATURDIDO", 2, 0, TipoEstado.CONTROL);
	}

	@Override
	public void alAplicar(Personaje objetivo) {
		System.out.println(" *** " + objetivo.getNombre() + " queda ATURDIDO (pierde 1 turno) ***");
	}

	@Override
	public void alProcesarTurno(Personaje objetivo) {
		// El aturdimiento no hace daño, solo bloquea la accion en Combate
	}

	@Override
	public void alExpirar(Personaje objetivo) {
		System.out.println("  " + objetivo.getNombre() + " se recupera del aturdimiento.");
	}

	// Indica que este estado bloquea la accion del personaje
	public boolean bloqueaAccion() {
		return true;
	}
}
