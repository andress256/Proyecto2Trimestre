package Clases;

import Armas.CatalogoArmas;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

public class CaballeroOscuro extends Personaje{
	private int contadorGolpesRecibidos = 0;

	public CaballeroOscuro(String nombre) {
		super(nombre, TipoClase.CABALLERO_OSCURO, 250, 0 , 35, 0, 18, 150);
		// TODO Auto-generated constructor stub
		this.arma = CatalogoArmas.defectoCaballeroOscuro();
	}

	@Override
	public void recibirDaño(int cantidad) {
		contadorGolpesRecibidos++;
		// TODO Auto-generated method stub
		if (contadorGolpesRecibidos % 3 == 0) {
			System.out.println("   [!] La armadura cromatica de " + nombre + "ABSORBE el golpe!");
			return;
		}
		super.recibirDaño(cantidad);
	}

	@Override
	public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
		// TODO Auto-generated method stub
		Personaje objetivo = null;
		int mayorVida = -1;
		for (Personaje p : enemigos) {
			if (p.estaVivo() && p.getVidaActual() > mayorVida) {
				mayorVida = p.getVidaActual();
				objetivo = p;
			}
		}
		if (objetivo != null) atacarCon(objetivo);
	}

}
