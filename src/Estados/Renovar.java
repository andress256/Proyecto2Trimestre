package Estados;

import Personajes.Personaje;

public class Renovar extends Estado{

	public Renovar(int turnos, int potencia) {
		super("RENOVAR", turnos, potencia, TipoEstado.HOT);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void alAplicar(Personaje objetivo) {
		// TODO Auto-generated method stub
		System.out.println("  " + objetivo.getNombre() + " recibe Renovar. (+" + potenciaPorTurno + " HP x" + turnosRestantes + "t)");
		
	}

	@Override
	public void alProcesarTurno(Personaje objetivo) {
		// TODO Auto-generated method stub
		if(!objetivo.estaVivo()) return;
		int antes = objetivo.getVidaActual();
		objetivo.curar(potenciaPorTurno);
		System.out.println(" Renovar: " + objetivo.getNombre() + " recupera " + (objetivo.getVidaActual() - antes) + " HP.");
	}

	@Override
	public void alExpirar(Personaje objetivo) {
		// TODO Auto-generated method stub
		
	}

}
