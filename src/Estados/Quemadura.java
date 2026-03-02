package Estados;

import Personajes.Personaje;

public class Quemadura extends Estado{

	public Quemadura(int turnos, int potencia) {
		super("QUEMADURA", turnos, potencia, TipoEstado.DOT);;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void alAplicar(Personaje objetivo) {
		// TODO Auto-generated method stub
		System.out.println("  " + objetivo.getNombre() + "queda en llamas. (Quemadura x" + turnosRestantes + "t)");
	}

	@Override
	public void alProcesarTurno(Personaje objetivo) {
		// TODO Auto-generated method stub
		if(!objetivo.estaVivo()) return;
		System.out.println(" Quemadura: " + objetivo.getNombre() + " pierde " + potenciaPorTurno + " HP. ");
		objetivo.recibirDanioPuro(potenciaPorTurno);
	}

	@Override
	public void alExpirar(Personaje objetivo) {
		// TODO Auto-generated method stub
		System.out.println("  Quemadura en " + objetivo.getNombre() + " se apaga.");
	}

}
