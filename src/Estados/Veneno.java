package Estados;

import Personajes.Personaje;

public class Veneno extends Estado{

	public Veneno(int turnos, int potencia) {
		super("VENENO", turnos, potencia, TipoEstado.DOT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void alAplicar(Personaje objetivo) {
		// TODO Auto-generated method stub
		System.out.println("  " + objetivo.getNombre() + "queda envenenado. (Veneno x" + turnosRestantes + "t)");
	}

	@Override
	public void alProcesarTurno(Personaje objetivo) {
		// TODO Auto-generated method stub
		if(!objetivo.estaVivo()) return;
		System.out.println(" Veneno: " + objetivo.getNombre() + " pierde " + potenciaPorTurno + " HP.");
		objetivo.recibirDanioPuro(potenciaPorTurno);
	}

	@Override
	public void alExpirar(Personaje objetivo) {
		// TODO Auto-generated method stub
		System.out.println(" Veneno en " + objetivo.getNombre() + " se disipa.");
	}

}
