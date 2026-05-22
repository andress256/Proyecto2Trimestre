package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import Clases.Guerrero;
import Clases.BrutoPintado;
import Clases.Mago;
import Clases.MagoOscuro;
import Personajes.Personaje;
import Juego.Combate;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Tests de Combate")
class TestCombate {

	private List<Personaje> lista(Personaje p) {
		List<Personaje> l = new ArrayList<>();
		l.add(p);
		return l;
	}

	@Test
	@DisplayName("Combate termina sin excepciones")
	void combateTerminaSinExcepciones() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		villano.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();
	}

	@Test
	@DisplayName("Héroe gana si villano empieza con 1 punto de vida")
	void heroeGanaSiVillanoEmpiezaConUnPuntoDeVida() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		villano.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();

		assertTrue(heroe.estaVivo());
		assertFalse(villano.estaVivo());
	}

	@Test
	@DisplayName("Villano gana si héroe empieza con 1 punto de vida")
	void villanoGanaSiHeroeEmpiezaConUnPuntoDeVida() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		heroe.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();

		assertFalse(heroe.estaVivo());
		assertTrue(villano.estaVivo());
	}

	@Test
	@DisplayName("Combate arranca desde ronda inicial")
	void combateArancaDesdeRondaInicial() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		villano.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0, 5).iniciar();
	}

	@Test
	@DisplayName("Combate 3 vs 3 termina sin excepciones")
	void combateTresVsTresTerminaSinExcepciones() {
		List<Personaje> heroes = new ArrayList<>();
		heroes.add(new Guerrero("G1"));
		heroes.add(new Guerrero("G2"));
		heroes.add(new Guerrero("G3"));

		List<Personaje> villanos = new ArrayList<>();
		villanos.add(new BrutoPintado("B1"));
		villanos.add(new BrutoPintado("B2"));
		villanos.add(new BrutoPintado("B3"));

		for (Personaje v : villanos) v.setVidaActual(1);

		new Combate(heroes, villanos, null, 0).iniciar();

		for (Personaje h : heroes) assertTrue(h.estaVivo());
	}

	@Test
	@DisplayName("Combate con magos usa hechizos sin excepciones")
	void combateConMagosUsaHechizos() {
		Personaje heroe   = new Mago("Lune");
		Personaje villano = new MagoOscuro("La Pintora");
		villano.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();
	}

	@Test
	@DisplayName("Personaje muerto no actúa en combate")
	void personajeMuertoNoActuaEnCombate() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");

		heroe.recibirDañoPuro(9999);
		villano.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();

		assertFalse(heroe.estaVivo());
	}
}