package Estados;

import Personajes.Personaje;

// Estado DOT: quita vida cada turno ignorando la defensa
public class Veneno extends Estado {

	public Veneno(int turnos, int potencia) {
		super("VENENO", turnos, potencia, TipoEstado.DOT);
	}

	@Override
	public void alAplicar(Personaje objetivo) {
		System.out.println("  " + objetivo.getNombre() + " queda envenenado. (Veneno x" + turnosRestantes + "t)");
	}

	@Override
	public void alProcesarTurno(Personaje objetivo) {
		if (!objetivo.estaVivo()) return;
		System.out.println(" Veneno: " + objetivo.getNombre() + " pierde " + potenciaPorTurno + " HP.");
		objetivo.recibirDañoPuro(potenciaPorTurno);
	}

	@Override
	public void alExpirar(Personaje objetivo) {
		System.out.println(" Veneno en " + objetivo.getNombre() + " se disipa.");
	}
}
