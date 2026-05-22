package test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import Armas.ArmaCuerpoACuerpo;
import Armas.ArmaADistancia;
import Clases.Guerrero;
import Personajes.Personaje;

// Tests unitarios de las armas.
// Se comprueba que el calculo de daño es correcto segun el tipo de arma.
public class TestArmas {

	private Personaje atacante;
	private Personaje defensor;

	@Before
	public void setUp() {
		atacante = new Guerrero("Atacante"); // ataqueBase 28, defensa 14
		defensor = new Guerrero("Defensor"); // defensaBase 14
	}

	// --- ArmaCuerpoACuerpo ---

	@Test
	public void armaCaC_danioEsMayorQueCero() {
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("TestEspada", 20, 0.0, 2.0);
		int danio = arma.calcularDaño(atacante, defensor);
		assertTrue(danio > 0);
	}

	@Test
	public void armaCaC_danioIncluye130PorCientoDeAtaque() {
		// danio = dañoBase + ataqueBase * 1.3
		// Con prob critico 0: danio = 20 + (int)(28 * 1.3) = 20 + 36 = 56
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("TestEspada", 20, 0.0, 2.0);
		int danio = arma.calcularDaño(atacante, defensor);
		assertEquals(56, danio);
	}

	@Test
	public void armaCaC_danioCriticoEsMayor() {
		// Con prob critico 1.0 siempre critica
		ArmaCuerpoACuerpo armaNormal  = new ArmaCuerpoACuerpo("Normal",  20, 0.0, 2.0);
		ArmaCuerpoACuerpo armaCritica = new ArmaCuerpoACuerpo("Critica", 20, 1.0, 2.0);
		int danioNormal  = armaNormal.calcularDaño(atacante, defensor);
		int danioCritico = armaCritica.calcularDaño(atacante, defensor);
		assertTrue(danioCritico > danioNormal);
	}

	@Test
	public void armaCaC_multiplicadorCriticoSeCumple() {
		// danio normal = 56, multiplicador 2.0 -> critico = 112
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("TestEspada", 20, 1.0, 2.0);
		int danio = arma.calcularDaño(atacante, defensor);
		assertEquals(112, danio);
	}

	// --- ArmaADistancia ---

	@Test
	public void armaDistancia_danioEsMayorQueCero() {
		ArmaADistancia arma = new ArmaADistancia("TestArco", 15, 0.0, 1.8, 0.4);
		int danio = arma.calcularDaño(atacante, defensor);
		assertTrue(danio > 0);
	}

	@Test
	public void armaDistancia_ignoraParteDefensaEnemigo() {
		// danio = dañoBase + ataqueBase + (int)(defensaDefensor * ignorarDefensa)
		// = 15 + 28 + (int)(14 * 0.4) = 15 + 28 + 5 = 48
		ArmaADistancia arma = new ArmaADistancia("TestArco", 15, 0.0, 1.8, 0.4);
		int danio = arma.calcularDaño(atacante, defensor);
		assertEquals(48, danio);
	}

	@Test
	public void armaDistancia_conIgnorarDefensaCero_noBonificacion() {
		// ignorarDefensa = 0.0 -> danio = 15 + 28 + 0 = 43
		ArmaADistancia arma = new ArmaADistancia("TestArco", 15, 0.0, 1.8, 0.0);
		int danio = arma.calcularDaño(atacante, defensor);
		assertEquals(43, danio);
	}

	@Test
	public void armaDistancia_danioCriticoEsMayor() {
		ArmaADistancia armaNormal  = new ArmaADistancia("Normal",  15, 0.0, 2.0, 0.4);
		ArmaADistancia armaCritica = new ArmaADistancia("Critica", 15, 1.0, 2.0, 0.4);
		int danioNormal  = armaNormal.calcularDaño(atacante, defensor);
		int danioCritico = armaCritica.calcularDaño(atacante, defensor);
		assertTrue(danioCritico > danioNormal);
	}

	// --- Descripcion ---

	@Test
	public void armaCaC_descripcionContieneNombre() {
		ArmaCuerpoACuerpo arma = new ArmaCuerpoACuerpo("Espada Magica", 20, 0.15, 2.0);
		assertTrue(arma.descripcion().contains("Espada Magica"));
	}

	@Test
	public void armaDistancia_descripcionContieneNombre() {
		ArmaADistancia arma = new ArmaADistancia("Arco Estelar", 15, 0.18, 1.6, 0.35);
		assertTrue(arma.descripcion().contains("Arco Estelar"));
	}
}