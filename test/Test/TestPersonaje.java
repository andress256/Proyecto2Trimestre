package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import Clases.Guerrero;
import Clases.Mago;
import Personajes.Personaje;

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
	void dañoPuroReduceVidaCorrectamente() {
		guerrero.recibirDañoPuro(50);
		assertEquals(150, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Daño puro no deja vida negativa")
	void dañoPuroNoDejaVidaNegativa() {
		guerrero.recibirDañoPuro(9999);
		assertEquals(0, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Daño físico se reduce con defensa")
	void dañoFisicoSeReduceConDefensa() {
		int vidaAntes = guerrero.getVidaActual();
		guerrero.recibirDaño(20);
		// Guerrero reduce 10%: 20 * 0.90 = 18
		// Defensa 14: 18 - 14 = 4 daño real
		assertEquals(vidaAntes - 4, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Daño físico es mínimo 1 si defensa es mayor")
	void dañoFisicoEsMinimo1SiDefensaEsMayor() {
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
	void gastarRecursoConSaldoSuficienteDevuelveTrue() {
		assertTrue(mago.gastarRecurso(50));
		assertEquals(110, mago.getRecursoActual());
	}

	@Test
	@DisplayName("Gastar recurso sin saldo devuelve false")
	void gastarRecursoSinSaldoDevuelveFalse() {
		assertFalse(mago.gastarRecurso(9999));
		assertEquals(mago.getRecursoMax(), mago.getRecursoActual());
	}

	@Test
	@DisplayName("Set vida actual no pasa de máximo")
	void setVidaActualNoPasaDeMaximo() {
		guerrero.setVidaActual(9999);
		assertEquals(guerrero.getVidaMax(), guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Set vida actual no es negativa")
	void setVidaActualNoEsNegativa() {
		guerrero.setVidaActual(-100);
		assertEquals(0, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Set recurso actual no pasa de máximo")
	void setRecursoActualNoPasaDeMaximo() {
		mago.setRecursoActual(9999);
		assertEquals(mago.getRecursoMax(), mago.getRecursoActual());
	}
}