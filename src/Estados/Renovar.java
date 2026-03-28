package Estados;

import Personajes.Personaje;

// Estado HOT: recupera vida cada turno
public class Renovar extends Estado {

	public Renovar(int turnos, int potencia) {
		super("RENOVAR", turnos, potencia, TipoEstado.HOT);
	}

	@Override
	public void alAplicar(Personaje objetivo) {
		System.out.println("  " + objetivo.getNombre() + " recibe Renovar. (+" + potenciaPorTurno + " HP x" + turnosRestantes + "t)");
	}

	@Override
	public void alProcesarTurno(Personaje objetivo) {
		if (!objetivo.estaVivo()) return;
		int antes = objetivo.getVidaActual();
		objetivo.curar(potenciaPorTurno);
		System.out.println(" Renovar: " + objetivo.getNombre() + " recupera " + (objetivo.getVidaActual() - antes) + " HP.");
	}

	@Override
	public void alExpirar(Personaje objetivo) {
		// El estado Renovar no muestra mensaje al expirar
	}
}