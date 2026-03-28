package Estados;

import Personajes.Personaje;

// Estado DOT: quita vida cada turno ignorando la defensa
public class Quemadura extends Estado {

	public Quemadura(int turnos, int potencia) {
		super("QUEMADURA", turnos, potencia, TipoEstado.DOT);
	}

	@Override
	public void alAplicar(Personaje objetivo) {
		System.out.println("  " + objetivo.getNombre() + " queda en llamas. (Quemadura x" + turnosRestantes + "t)");
	}

	@Override
	public void alProcesarTurno(Personaje objetivo) {
		if (!objetivo.estaVivo()) return;
		System.out.println(" Quemadura: " + objetivo.getNombre() + " pierde " + potenciaPorTurno + " HP. ");
		objetivo.recibirDañoPuro(potenciaPorTurno);
	}

	@Override
	public void alExpirar(Personaje objetivo) {
		System.out.println("  Quemadura en " + objetivo.getNombre() + " se apaga.");
	}
}
