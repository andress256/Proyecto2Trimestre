package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import Clases.Guerrero;
import Clases.Sacerdote;
import Personajes.Personaje;
import Estados.Veneno;
import Estados.Quemadura;
import Estados.Renovar;

@DisplayName("Tests de Estados")
class TestEstados {

	private Personaje guerrero;
	private Personaje sacerdote;

	@BeforeEach
	void setUp() {
		guerrero  = new Guerrero("Gustave");
		sacerdote = new Sacerdote("Sciel");
	}

	@Test
	@DisplayName("Veneno causa daño por turno")
	void venenoCausaDañoPorTurno() {
		guerrero.aplicarEstado(new Veneno(3, 10));
		int vidaAntes = guerrero.getVidaActual();
		guerrero.procesarEstados();
		assertEquals(vidaAntes - 10, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Veneno duración baja uno por turno")
	void venoDuracionBajaUnoPorTurno() {
		guerrero.aplicarEstado(new Veneno(3, 5));
		guerrero.procesarEstados();
		assertEquals(2, guerrero.getEstadosActivos().get(0).getTurnosRestantes());
	}

	@Test
	@DisplayName("Veneno expira al llegar a cero")
	void venenoExpiraAlLlegarACero() {
		guerrero.aplicarEstado(new Veneno(1, 5));
		guerrero.procesarEstados();
		assertTrue(guerrero.getEstadosActivos().isEmpty());
	}

	@Test
	@DisplayName("Quemadura causa daño por turno")
	void quemaduraCausaDañoPorTurno() {
		guerrero.aplicarEstado(new Quemadura(2, 15));
		int vidaAntes = guerrero.getVidaActual();
		guerrero.procesarEstados();
		assertEquals(vidaAntes - 15, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Quemadura expira al llegar a cero")
	void quemaduraExpiraAlLlegarACero() {
		guerrero.aplicarEstado(new Quemadura(1, 10));
		guerrero.procesarEstados();
		assertTrue(guerrero.getEstadosActivos().isEmpty());
	}

	@Test
	@DisplayName("Renovar recupera vida por turno")
	void renovarRecuperaVidaPorTurno() {
		guerrero.recibirDañoPuro(80);
		guerrero.aplicarEstado(new Renovar(2, 20));
		guerrero.procesarEstados();
		assertEquals(140, guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Renovar no supera vida máxima")
	void renovarNoSuperaVidaMaxima() {
		guerrero.aplicarEstado(new Renovar(2, 50));
		guerrero.procesarEstados();
		assertEquals(guerrero.getVidaMax(), guerrero.getVidaActual());
	}

	@Test
	@DisplayName("Renovar expira al llegar a cero")
	void renovarExpiraAlLlegarACero() {
		guerrero.aplicarEstado(new Renovar(1, 10));
		guerrero.procesarEstados();
		assertTrue(guerrero.getEstadosActivos().isEmpty());
	}

	@Test
	@DisplayName("Estado duplicado se renueva, no se apila")
	void estadoDuplicadoSeRuevaNoSeApila() {
		guerrero.aplicarEstado(new Veneno(3, 5));
		guerrero.aplicarEstado(new Veneno(5, 10));
		assertEquals(1, guerrero.getEstadosActivos().size());
	}

	@Test
	@DisplayName("Dos estados diferentes se acumulan")
	void dosEstadosDiferentesSeAcumulan() {
		guerrero.aplicarEstado(new Veneno(3, 5));
		guerrero.aplicarEstado(new Quemadura(2, 8));
		assertEquals(2, guerrero.getEstadosActivos().size());
	}

	@Test
	@DisplayName("Procesar estados con personaje muerto no lanza excepción")
	void procesarEstadosConPersonajeMuertoNoLanzaExcepcion() {
		guerrero.recibirDañoPuro(9999);
		guerrero.aplicarEstado(new Veneno(3, 10));
		guerrero.procesarEstados();
	}
}