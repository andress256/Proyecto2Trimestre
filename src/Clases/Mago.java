package Clases;


import Personajes.Personaje;

public class Mago extends Personaje {

	public Mago(String nombre, String tipoClase, int vidaMax, int vidaActual, int recursoMax, int recursoActual,
			int ataqueBase, int poderMagico, int defensaBase, int precision) {
		super(nombre, tipoClase, vidaMax, vidaActual, recursoMax, recursoActual, ataqueBase, poderMagico, defensaBase,
				precision);
		// TODO Auto-generated constructor stub
	}
//cambiar variables de recursos
	public Mago(String string, TipoClase mago, int i, int j, int k, int l, int m) {
		super("Lune", TipoClase.MAGO, 150, 0, 30, 15, 0);
		// TODO Auto-generated constructor stub
	}

	

	
}
