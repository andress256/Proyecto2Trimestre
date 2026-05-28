package Clases;

import Armas.CatalogoArmas;
import Hechizos.BrilloRenovador;
import Hechizos.Hechizo;
import Hechizos.LuzCelestial;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

// Heroe de soporte. Cura a los aliados y tiene un bonus del 20% en curaciones.
public class Sacerdote extends Personaje {
	public Sacerdote(String nombre) {
		super(nombre, TipoClase.SACERDOTE, 140, 130, 8, 22, 9, 100);
		equiparArma(CatalogoArmas.ARCO_ESTELAR_SCIEL.getArma());
		this.hechizos.add(new LuzCelestial());
		this.hechizos.add(new BrilloRenovador());
	}

	@Override
	public void curar(int cantidad) {
		super.curar((int) (cantidad * 1.20));
	}

	@Override
	public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
		Personaje masDebil = null;
		int menorVida = Integer.MAX_VALUE;
		for (Personaje p : aliados) {
			if (p.estaVivo() && p.getVidaActual() < menorVida) {
				menorVida = p.getVidaActual();
				masDebil = p;
			}
		}
		if (masDebil != null && masDebil.getVidaActual() < (int) (masDebil.getVidaMax() * 0.60)) {
			for (Hechizo h : hechizos) {
				if (h.getTipoObjetivo() == Hechizo.TipoObjetivo.ALIADO_UNICO && h.puedeLanzarse(this)) {
					h.lanzar(this, masDebil);
					return;
				}
			}
		}
		for (Personaje p : enemigos) {
			if (p.estaVivo()) {
				atacarCon(p);
				return;
			}
		}
	}
}
