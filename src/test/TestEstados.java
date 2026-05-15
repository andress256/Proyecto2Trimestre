package test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import Clases.Guerrero;
import Clases.Sacerdote;
import Personajes.Personaje;
import Estados.Veneno;
import Estados.Quemadura;
import Estados.Renovar;

// Tests unitarios de los estados activos (DOT, HOT).
// Se comprueba que cada estado causa el efecto correcto por turno
// y que expira correctamente al llegar a 0 turnos.
public class TestEstados {

	private Personaje guerrero;
	private Personaje sacerdote;

	@Before
	public void setUp() {
		guerrero  = new Guerrero("Gustave");   // vida 200
		sacerdote = new Sacerdote("Sciel");    // vida 140
	}

	// --- Veneno ---

	@Test
	public void veneno_causaDanioPorTurno() {
		guerrero.aplicarEstado(new Veneno(3, 10));
		int vidaAntes = guerrero.getVidaActual();
		guerrero.procesarEstados();
		assertEquals(vidaAntes - 10, guerrero.getVidaActual());
	}

	@Test
	public void veneno_duracionBajaUnoPorTurno() {
		guerrero.aplicarEstado(new Veneno(3, 5));
		guerrero.procesarEstados();
		// Despues de un turno quedan 2
		assertEquals(2, guerrero.getEstadosActivos().get(0).getTurnosRestantes());
	}

	@Test
	public void veneno_expiraAlLlegarACero() {
		guerrero.aplicarEstado(new Veneno(1, 5)); // solo 1 turno
		guerrero.procesarEstados();
		// Debe haber expirado -> lista vacia
		assertTrue(guerrero.getEstadosActivos().isEmpty());
	}

	// --- Quemadura ---

	@Test
	public void quemadura_causaDanioPorTurno() {
		guerrero.aplicarEstado(new Quemadura(2, 15));
		int vidaAntes = guerrero.getVidaActual();
		guerrero.procesarEstados();
		assertEquals(vidaAntes - 15, guerrero.getVidaActual());
	}

	@Test
	public void quemadura_expiraAlLlegarACero() {
		guerrero.aplicarEstado(new Quemadura(1, 10));
		guerrero.procesarEstados();
		assertTrue(guerrero.getEstadosActivos().isEmpty());
	}

	// --- Renovar ---

	@Test
	public void renovar_recuperaVidaPorTurno() {
		guerrero.recibirDañoPuro(80);      // vida = 120
		guerrero.aplicarEstado(new Renovar(2, 20));
		guerrero.procesarEstados();
		assertEquals(140, guerrero.getVidaActual());
	}

	@Test
	public void renovar_noSuperaVidaMaxima() {
		// Guerrero ya tiene vida maxima (200). Renovar no debe pasarla.
		guerrero.aplicarEstado(new Renovar(2, 50));
		guerrero.procesarEstados();
		assertEquals(guerrero.getVidaMax(), guerrero.getVidaActual());
	}

	@Test
	public void renovar_expiraAlLlegarACero() {
		guerrero.aplicarEstado(new Renovar(1, 10));
		guerrero.procesarEstados();
		assertTrue(guerrero.getEstadosActivos().isEmpty());
	}

	// --- Apilamiento y renovacion ---

	@Test
	public void estado_duplicadoSeRenueva_noSeApila() {
		guerrero.aplicarEstado(new Veneno(3, 5));
		guerrero.aplicarEstado(new Veneno(5, 10)); // renueva el existente
		// Solo debe existir 1 veneno
		assertEquals(1, guerrero.getEstadosActivos().size());
	}

	@Test
	public void dosEstadosDiferentes_seAcumulan() {
		guerrero.aplicarEstado(new Veneno(3, 5));
		guerrero.aplicarEstado(new Quemadura(2, 8));
		assertEquals(2, guerrero.getEstadosActivos().size());
	}

	@Test
	public void procesarEstados_personajeMuerto_noHaceNada() {
		guerrero.recibirDañoPuro(9999); // muerto
		guerrero.aplicarEstado(new Veneno(3, 10));
		// procesarEstados no debe lanzar excepcion con personaje muerto
		guerrero.procesarEstados();
	}
}