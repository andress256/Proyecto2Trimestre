package Clases;

import Armas.CatalogoArmas;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

public class BrutoPintado extends Personaje {

	public BrutoPintado(String nombre) {
		super(nombre, TipoClase.BRUTO_PINTADO, 160, 0, 26, 0, 10, 100);
		// TODO Auto-generated constructor stub
		this.arma = CatalogoArmas.defectoBruto();
	}

	@Override
	public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
		// TODO Auto-generated method stub
		for (Personaje p : enemigos) {
			if (p.estaVivo()) {atacarCon(p); return;}
		}
	}

}
