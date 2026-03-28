package Juego;

import Personajes.catalogoPersonajes;
import Personajes.Personaje;
import java.util.List;

// Clase principal que arranca y controla el flujo del juego.
// Genera los equipos aleatoriamente y lanza los combates.
public class Juego {

	private static final int TOTAL_COMBATES = 1; // numero de combates a simular

	public void iniciar() {
		System.out.println("=== EXPEDITION 33 - RPG por turnos ===\n");

		int victorias = 0;

		for (int i = 1; i <= TOTAL_COMBATES; i++) {
			System.out.println("\n========================================");
			System.out.println("  COMBATE " + i + " / " + TOTAL_COMBATES);
			System.out.println("========================================");

			// Genera equipos de 3 personajes aleatorios de cada bando
			List<Personaje> heroes   = catalogoPersonajes.generarEquipoHeroesRandom();
			List<Personaje> villanos = catalogoPersonajes.generarEquipoVillanosRandom();

			System.out.println("  Heroes:   " + nombres(heroes));
			System.out.println("  Villanos: " + nombres(villanos));

			new Combate(heroes, villanos).iniciar();

			// Comprueba si queda algun heroe vivo para contar la victoria
			boolean heroesVivos = false;
			for (Personaje p : heroes) { if (p.estaVivo()) { heroesVivos = true; break; } }
			if (heroesVivos) victorias++;

			pausar(4000);
		}

		System.out.println("\n=== RESUMEN FINAL ===");
		System.out.println("  Heroes ganaron: " + victorias + "/" + TOTAL_COMBATES + " combates.");
	}

	// Construye una cadena con los nombres separados por coma
	private String nombres(List<Personaje> lista) {
		StringBuilder sb = new StringBuilder();
		for (Personaje p : lista) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(p.getNombre());
		}
		return sb.toString();
	}

	private void pausar(int ms) {
		try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
	}
}

