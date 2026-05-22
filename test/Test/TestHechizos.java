package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import Clases.Mago;
import Clases.Guerrero;
import Hechizos.Hechizo;
import Hechizos.LlamaPicta;
import Hechizos.ImpactoCalcinante;
import Hechizos.LuzCelestial;
import Hechizos.SombraCromatica;

@DisplayName("Tests de Hechizos")
class TestHechizos {

	private Mago mago;
	private Guerrero enemigo;

	@BeforeEach
	void setUp() {
		mago = new Mago("Lune");
		enemigo = new Guerrero("Gustave");
	}

	@Test
	@DisplayName("Hechizo gasta recurso correctamente")
	void hechizoGastaRecursoCorrectamente() {
		Hechizo llama = new LlamaPicta();
		int recursoAntes = mago.getRecursoActual();
		llama.lanzar(mago, enemigo);
		assertTrue(mago.getRecursoActual() < recursoAntes);
	}

	@Test
	@DisplayName("Hechizo no se puede lanzar sin recurso")
	void hechizoNoSeLanzaSinRecurso() {
		Hechizo llama = new LlamaPicta();
		while (mago.getRecursoActual() > 0) {
			mago.gastarRecurso(mago.getRecursoActual());
		}
		assertFalse(llama.puedeLanzarse(mago));
	}

	@Test
	@DisplayName("Hechizo causa daño al enemigo")
	void hechizoCausaDañoAlEnemigo() {
		Hechizo llama = new LlamaPicta();
		int vidaAntes = enemigo.getVidaActual();
		llama.lanzar(mago, enemigo);
		assertTrue(enemigo.getVidaActual() < vidaAntes);
	}

	@Test
	@DisplayName("Hechizo entra en cooldown después de lanzarse")
	void hechizoEntraEnCooldownDespuesDeLanzarse() {
		Hechizo llama = new LlamaPicta();
		llama.lanzar(mago, enemigo);
		assertFalse(llama.puedeLanzarse(mago));
	}

	@Test
	@DisplayName("PuedeLanzarse devuelve true con recursos suficientes")
	void puedeLanzarseDevuelveTrueConRecursos() {
		Hechizo impacto = new ImpactoCalcinante();
		assertTrue(impacto.puedeLanzarse(mago));
	}

	@Test
	@DisplayName("PuedeLanzarse devuelve false sin recursos suficientes")
	void puedeLanzarseDevuelveFalseSinRecursos() {
		Hechizo impacto = new ImpactoCalcinante();
		mago.gastarRecurso(126);
		assertFalse(impacto.puedeLanzarse(mago));
	}

	@Test
	@DisplayName("Hechizo de curación cura al aliado")
	void hechizoDeCuracionCuraAlAliado() {
		Hechizo luz = new LuzCelestial();
		mago.recibirDañoPuro(50);
		int vidaAntes = mago.getVidaActual();
		luz.lanzar(mago, mago);
		assertTrue(mago.getVidaActual() > vidaAntes);
	}

	@Test
	@DisplayName("Hechizo con estado aplica estado al enemigo")
	void hechizoConEstadoAplicaEstadoAlEnemigo() {
		Hechizo sombra = new SombraCromatica();
		sombra.lanzar(mago, enemigo);
		assertFalse(enemigo.getEstadosActivos().isEmpty());
	}
}