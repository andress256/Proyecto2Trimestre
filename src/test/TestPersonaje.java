package test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import Clases.Guerrero;
import Clases.Mago;
import Personajes.Personaje;

// Tests unitarios de la clase Personaje.
// Se usa Guerrero (vida 200, defensa 14) y Mago (vida 130, recurso 160) como ejemplos concretos.
public class TestPersonaje {

	private Personaje guerrero;
	private Personaje mago;

	@Before
	public void setUp() {
		guerrero = new Guerrero("Gustave");
		mago     = new Mago("Lune");
	}

	// --- estaVivo() ---

	@Test
	public void personajeNuevoEstaVivo() {
		assertTrue(guerrero.estaVivo());
	}

	@Test
	public void personajeConVidaCeroNoEstaVivo() {
		guerrero.recibirDañoPuro(9999);
		assertFalse(guerrero.estaVivo());
	}

	// --- recibirDañoPuro() ---

	@Test
	public void danioPuroReduceVidaCorrectamente() {
		guerrero.recibirDañoPuro(50);
		assertEquals(150, guerrero.getVidaActual());
	}

	@Test
	public void danioPuroNoDejaVidaNegativa() {
		guerrero.recibirDañoPuro(9999);
		assertEquals(0, guerrero.getVidaActual());
	}

	@Test
	public void danioPuroCeroNoModificaVida() {
		int vidaAntes = guerrero.getVidaActual();
		guerrero.recibirDañoPuro(0);
		assertEquals(vidaAntes, guerrero.getVidaActual());
	}

	// --- recibirDaño() (fisico, reduce con defensa) ---

	@Test
	public void danioFisicoSeReduceConDefensa() {
		// Guerrero tiene defensa 14. Daño 20 -> real = 20 - 14 = 6
		int vidaAntes = guerrero.getVidaActual();
		guerrero.recibirDaño(20);
		assertEquals(vidaAntes - 6, guerrero.getVidaActual());
	}

	@Test
	public void danioFisicoEsMinimo1SiDefensaEsMayor() {
		// Defensa 14 > daño 5, daño real deberia ser 1 (minimo)
		int vidaAntes = guerrero.getVidaActual();
		guerrero.recibirDaño(5);
		assertEquals(vidaAntes - 1, guerrero.getVidaActual());
	}

	// --- curar() ---

	@Test
	public void curarRecuperaVida() {
		guerrero.recibirDañoPuro(80);  // vida = 120
		guerrero.curar(30);
		assertEquals(150, guerrero.getVidaActual());
	}

	@Test
	public void curarNoSuperaVidaMaxima() {
		guerrero.curar(9999);
		assertEquals(guerrero.getVidaMax(), guerrero.getVidaActual());
	}

	@Test
	public void curarConVidaLlenaNoModificaNada() {
		int vidaAntes = guerrero.getVidaActual();
		guerrero.curar(50);
		assertEquals(vidaAntes, guerrero.getVidaActual());
	}

	// --- gastarRecurso() ---

	@Test
	public void gastarRecurso_conSaldoSuficiente_devuelveTrue() {
		// Mago tiene 160 de recurso
		assertTrue(mago.gastarRecurso(50));
		assertEquals(110, mago.getRecursoActual());
	}

	@Test
	public void gastarRecurso_sinSaldo_devuelveFalse() {
		assertFalse(mago.gastarRecurso(9999));
		// El recurso no debe haberse modificado
		assertEquals(mago.getRecursoMax(), mago.getRecursoActual());
	}

	@Test
	public void gastarTodoElRecurso_dejaEnCero() {
		int recurso = mago.getRecursoActual();
		assertTrue(mago.gastarRecurso(recurso));
		assertEquals(0, mago.getRecursoActual());
	}

	// --- setters para BD ---

	@Test
	public void setVidaActual_noPasaDeMaximo() {
		guerrero.setVidaActual(9999);
		assertEquals(guerrero.getVidaMax(), guerrero.getVidaActual());
	}

	@Test
	public void setVidaActual_noEsNegativa() {
		guerrero.setVidaActual(-100);
		assertEquals(0, guerrero.getVidaActual());
	}

	@Test
	public void setVidaActual_valorNormal() {
		guerrero.setVidaActual(75);
		assertEquals(75, guerrero.getVidaActual());
	}

	@Test
	public void setRecursoActual_noPasaDeMaximo() {
		mago.setRecursoActual(9999);
		assertEquals(mago.getRecursoMax(), mago.getRecursoActual());
	}

	@Test
	public void setRecursoActual_noEsNegativo() {
		mago.setRecursoActual(-50);
		assertEquals(0, mago.getRecursoActual());
	}

	@Test
	public void setBarraAturdimiento_noEsNegativa() {
		guerrero.setBarraAturdimiento(-10);
		assertEquals(0, guerrero.getBarraAturdimiento());
	}
}
