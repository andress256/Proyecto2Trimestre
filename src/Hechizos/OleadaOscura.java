package Hechizos;

import Estados.Quemadura;
import Personajes.Personaje;

public class OleadaOscura extends Hechizo {
	public OleadaOscura() {
		super("Oleada Oscura", 50, TipoObjetivo.TODOS_ENEMIGOS, "Quemadura a todos", 4, 15);
	}

	@Override
	public void lanzar(Personaje caster, Personaje objetivo) {
		// Llamada individual por cada objetivo desde MagoOscuro
		if (objetivo != null && objetivo.estaVivo()) {
			objetivo.recibirDañoPuro(15 + caster.getPoderMagico() / 3);
			objetivo.aplicarEstado(new Quemadura(2, 8));
		}
	}
}
