package test;

import static org.junit.Assert.*;
import org.junit.Test;

import Clases.Guerrero;
import Clases.BrutoPintado;
import Clases.Mago;
import Clases.MagoOscuro;
import Personajes.Personaje;
import Juego.Combate;

import java.util.ArrayList;
import java.util.List;

// Tests de integracion del combate.
// Se usa dao=null para ejecutar sin BD (el guardado se omite automaticamente).
// Los equipos se crean con condiciones controladas para resultados predecibles.
public class TestCombate {

	// Crea una lista con un solo personaje
	private List<Personaje> lista(Personaje p) {
		List<Personaje> l = new ArrayList<>();
		l.add(p);
		return l;
	}

	// --- Combate completo sin BD ---

	@Test
	public void combate_terminaSinExcepciones() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		villano.setVidaActual(1); // muere al primer golpe

		// dao=null omite el guardado automatico
		new Combate(lista(heroe), lista(villano), null, 0).iniciar();
	}

	@Test
	public void combate_heroeGana_si_villanoEmpiezaConUnaDePuntosDeVida() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		villano.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();

		assertTrue(heroe.estaVivo());
		assertFalse(villano.estaVivo());
	}

	@Test
	public void combate_villanoGana_si_heroeEmpiezaConUnPuntosDeVida() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		heroe.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();

		assertFalse(heroe.estaVivo());
		assertTrue(villano.estaVivo());
	}

	@Test
	public void combate_conRondaInicial_arrancaDesdeEsaRonda() {
		// Verificamos que el combate funciona cuando se carga desde ronda 5
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");
		villano.setVidaActual(1);

		// No debe lanzar excepcion al arrancar desde ronda 5
		new Combate(lista(heroe), lista(villano), null, 0, 5).iniciar();
	}

	// --- Combate con varios personajes ---

	@Test
	public void combate_tresvsTres_terminaSinExcepciones() {
		List<Personaje> heroes = new ArrayList<>();
		heroes.add(new Guerrero("G1"));
		heroes.add(new Guerrero("G2"));
		heroes.add(new Guerrero("G3"));

		List<Personaje> villanos = new ArrayList<>();
		villanos.add(new BrutoPintado("B1"));
		villanos.add(new BrutoPintado("B2"));
		villanos.add(new BrutoPintado("B3"));

		// Villanos con poca vida para que el combate sea corto
		for (Personaje v : villanos) v.setVidaActual(1);

		new Combate(heroes, villanos, null, 0).iniciar();

		// Todos los heroes deben sobrevivir
		for (Personaje h : heroes) assertTrue(h.estaVivo());
	}

	@Test
	public void combate_conMagos_usaHechizos_sinExcepciones() {
		Personaje heroe   = new Mago("Lune");
		Personaje villano = new MagoOscuro("La Pintora");
		villano.setVidaActual(1);

		new Combate(lista(heroe), lista(villano), null, 0).iniciar();
	}

	// --- Personaje muerto no actua ---

	@Test
	public void personajeMuerto_noActuaEnCombate() {
		Personaje heroe   = new Guerrero("Gustave");
		Personaje villano = new BrutoPintado("Bruto");

		heroe.recibirDañoPuro(9999); // muerto antes del combate
		villano.setVidaActual(1);

		// El combate debe terminar porque el heroe esta muerto desde el inicio
		new Combate(lista(heroe), lista(villano), null, 0).iniciar();

		assertFalse(heroe.estaVivo());
	}
}