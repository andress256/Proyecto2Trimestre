package Estados;

import Personajes.Personaje;

public class Aturdido extends Estado{

	public Aturdido() {super("ATURDIDO", 1, 0, TipoEstado.CONTROL);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void alAplicar(Personaje objetivo) {
		// TODO Auto-generated method stub	
		System.out.println(" *** " + objetivo.getNombre() + " queda ATURDIDO (pierde 1 turno) ***");
	}
	@Override
	public void alProcesarTurno(Personaje objetivo) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void alExpirar(Personaje objetivo) {
		// TODO Auto-generated method stub	
		System.out.println("  " + objetivo.getNombre() + " se recupera del aturdimiento.");
	}
	public boolean bloqueaAccion() {
		return true;
	}
}
