package Personajes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import Clases.Guerrero;
import Clases.Mago;

@DisplayName("Tests de la clase Personaje")
class TestPersonaje {

	private Personaje guerrero;
	private Personaje mago;

	@BeforeEach
	void setUp() {
		guerrero = new Guerrero("Gustave");
		mago     = new Mago("Lune");
	}

	@Test
	@DisplayName("Personaje nuevo está vivo")
	void personajeNuevoEstaVivo() {
		assertTrue(guerrero.estaVivo());
	}

	@Test
	@DisplayName("Personaje con vida cero no está vivo")
	void personajeConVidaCeroNoEstaVivo() {
		guerrero.recibirDañoPuro(9999);
		assertFalse(guerrero.estaVivo());
	}

	@Test
	@DisplayName("Daño puro reduce vida correctamente")
	void danioPuroReduceVidaCorrectamente() {
		guerrero.recibirDañoPuro(50);
		assertEquals(150, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Daño puro no deja vida negativa")
	void danioPuroNoDejaVidaNegativa() {
		guerrero.recibirDañoPuro(9999);
		assertEquals(0, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Daño físico se reduce con defensa")
	void danioFisicoSeReduceConDefensa() {
		int vidaAntes = guerrero.getVidaActual();
		guerrero.recibirDaño(20);
		assertEquals(vidaAntes - 6, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Daño físico es mínimo 1 si defensa es mayor")
	void danioFisicoEsMinimo1SiDefensaEsMayor() {
		int vidaAntes = guerrero.getVidaActual();
		guerrero.recibirDaño(5);
		assertEquals(vidaAntes - 1, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Curar recupera vida")
	void curarRecuperaVida() {
		guerrero.recibirDañoPuro(80);
		guerrero.curar(30);
		assertEquals(150, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Curar no supera vida máxima")
	void curarNoSuperaVidaMaxima() {
		guerrero.curar(9999);
		assertEquals(guerrero.getVidaMax(), guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Gastar recurso con saldo suficiente devuelve true")
	void gastarRecurso_conSaldoSuficiente_devuelveTrue() {
		assertTrue(mago.gastarRecurso(50));
		assertEquals(110, mago.getRecursoActual());
	}

	@Test
	@DisplayName("Gastar recurso sin saldo devuelve false")
	void gastarRecurso_sinSaldo_devuelveFalse() {
		assertFalse(mago.gastarRecurso(9999));
		assertEquals(mago.getRecursoMax(), mago.getRecursoActual());
	}

	@Test
	@DisplayName("Set vida actual no pasa de máximo")
	void setVidaActual_noPasaDeMaximo() {
		guerrero.setVidaActual(9999);
		assertEquals(guerrero.getVidaMax(), guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Set vida actual no es negativa")
	void setVidaActual_noEsNegativa() {
		guerrero.setVidaActual(-100);
		assertEquals(0, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Set recurso actual no pasa de máximo")
	void setRecursoActual_noPasaDeMaximo() {
		mago.setRecursoActual(9999);
		assertEquals(mago.getRecursoMax(), mago.getRecursoActual());
	}
}