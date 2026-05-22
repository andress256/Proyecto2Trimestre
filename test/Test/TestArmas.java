package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import Armas.ArmaCuerpoACuerpo;
import Armas.ArmaADistancia;
import Clases.Guerrero;
import Personajes.Personaje;

@DisplayName("Tests de Armas")
class TestArmas {

	private Personaje atacante;
	private Personaje defensor;

	@BeforeEach
	void setUp() {
		atacante = new Guerrero("Atacante");
		defensor = new Guerrero("Defensor");
	}

	@Test
	@DisplayName("Arma CaC daño es mayor que cero")
	void armaCaCDañoEsMayorQueCero() {
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("TestEspada", 20, 0.0, 2.0);
		int daño = arma.calcularDaño(atacante, defensor);
		assertTrue(daño > 0);
	}

	@Test
	@DisplayName("Arma CaC daño incluye 130% de ataque")
	void armaCaCDañoIncluye130PorCientoDeAtaque() {
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("TestEspada", 20, 0.0, 2.0);
		int daño = arma.calcularDaño(atacante, defensor);
		assertEquals(56, daño);
	}

	@Test
	@DisplayName("Arma CaC daño crítico es mayor")
	void armaCaCDañoCriticoEsMayor() {
		ArmaCuerpoACuerpo armaNormal  = new ArmaCuerpoACuerpo("Normal",  20, 0.0, 2.0);
		ArmaCuerpoACuerpo armaCritica = new ArmaCuerpoACuerpo("Critica", 20, 1.0, 2.0);
		int dañoNormal  = armaNormal.calcularDaño(atacante, defensor);
		int dañoCritico = armaCritica.calcularDaño(atacante, defensor);
		assertTrue(dañoCritico > dañoNormal);
	}

	@Test
	@DisplayName("Arma CaC multiplicador crítico se cumple")
	void armaCaCMultiplicadorCriticoSeCumple() {
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("TestEspada", 20, 1.0, 2.0);
		int daño = arma.calcularDaño(atacante, defensor);
		assertEquals(112, daño);
	}

	@Test
	@DisplayName("Arma a distancia daño es mayor que cero")
	void armaDistanciaDañoEsMayorQueCero() {
		ArmaADistancia arma = new ArmaADistancia("TestArco", 15, 0.0, 1.8, 0.4);
		int daño = arma.calcularDaño(atacante, defensor);
		assertTrue(daño > 0);
	}

	@Test
	@DisplayName("Arma a distancia ignora parte defensa enemigo")
	void armaDistanciaIgnoraParteDefensaEnemigo() {
		ArmaADistancia arma = new ArmaADistancia("TestArco", 15, 0.0, 1.8, 0.4);
		int daño = arma.calcularDaño(atacante, defensor);
		assertEquals(48, daño);
	}

	@Test
	@DisplayName("Arma a distancia con ignorar defensa cero")
	void armaDistanciaConIgnorarDefensaCero() {
		ArmaADistancia arma = new ArmaADistancia("TestArco", 15, 0.0, 1.8, 0.0);
		int daño = arma.calcularDaño(atacante, defensor);
		assertEquals(43, daño);
	}

	@Test
	@DisplayName("Arma a distancia daño crítico es mayor")
	void armaDistanciaDañoCriticoEsMayor() {
		ArmaADistancia armaNormal  = new ArmaADistancia("Normal",  15, 0.0, 2.0, 0.4);
		ArmaADistancia armaCritica = new ArmaADistancia("Critica", 15, 1.0, 2.0, 0.4);
		int dañoNormal  = armaNormal.calcularDaño(atacante, defensor);
		int dañoCritico = armaCritica.calcularDaño(atacante, defensor);
		assertTrue(dañoCritico > dañoNormal);
	}

	@Test
	@DisplayName("Arma CaC descripción contiene nombre")
	void armaCaCDescripcionContieneNombre() {
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("Espada Mágica", 20, 0.15, 2.0);
		assertTrue(arma.descripcion().contains("Espada Mágica"));
	}

	@Test
	@DisplayName("Arma a distancia descripción contiene nombre")
	void armaDistanciaDescripcionContieneNombre() {
		ArmaADistancia arma = new ArmaADistancia("Arco Estelar", 15, 0.18, 1.6, 0.35);
		assertTrue(arma.descripcion().contains("Arco Estelar"));
	}
}