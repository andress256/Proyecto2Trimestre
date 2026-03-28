package Juego;

import Personajes.Personaje;
import java.util.List;

// Gestiona el bucle principal de un combate entre dos equipos.
// El combate es automatico: cada personaje actua segun su IA.
public class Combate {

	private List<Personaje> equipoHeroes;
	private List<Personaje> equipoVillanos;
	private int ronda;
	private static final int PAUSA_MS = 3000; // pausa entre acciones en milisegundos

	public Combate(List<Personaje> equipoHeroes, List<Personaje> equipoVillanos) {
		this.equipoHeroes = equipoHeroes;
		this.equipoVillanos = equipoVillanos;
		this.ronda = 0;
	}

	// Inicia el combate y lo ejecuta hasta que un equipo quede sin vida
	public void iniciar() {
		while (!combateTerminado()) {
			ronda++;
			System.out.println("\n--- Ronda " + ronda + " ---");
			mostrarEstadoCombate();
			pausar(PAUSA_MS);

			// Turno de los heroes
			for (Personaje p : equipoHeroes) {
				if (!p.estaVivo() || todosDerrota(equipoVillanos)) continue;
				if (p.estaAturdido()) {
					System.out.println("  " + p.getNombre() + " esta aturdido, pierde su turno.");
					continue;
				}
				pausar(PAUSA_MS);
				p.elegirAccionIA(equipoHeroes, equipoVillanos);
			}

			if (combateTerminado()) break;

			// Turno de los villanos
			for (Personaje p : equipoVillanos) {
				if (!p.estaVivo() || todosDerrota(equipoHeroes)) continue;
				if (p.estaAturdido()) {
					System.out.println("  " + p.getNombre() + " esta aturdido, pierde su turno.");
					continue;
				}
				pausar(PAUSA_MS);
				p.elegirAccionIA(equipoVillanos, equipoHeroes);
			}

			if (combateTerminado()) break;

			// Fase de efectos: procesar estados (veneno, quemadura...) y reducir cooldowns
			System.out.println("  [Efectos de turno]");
			for (Personaje p : equipoHeroes)  { if (p.estaVivo()) p.procesarEstados(); }
			for (Personaje p : equipoVillanos) { if (p.estaVivo()) p.procesarEstados(); }
			for (Personaje p : equipoHeroes)   p.reducirCooldowns();
			for (Personaje p : equipoVillanos)  p.reducirCooldowns();
		}

		mostrarResumenFinal();
	}

	private boolean tieneEstados(Personaje p) {
		return !p.getHechizos().isEmpty();
	}

	// Devuelve true si todos los personajes del equipo estan caidos
	private boolean todosDerrota(List<Personaje> equipo) {
		for (Personaje p : equipo) { if (p.estaVivo()) return false; }
		return true;
	}

	// El combate termina cuando uno de los dos equipos queda sin vida
	private boolean combateTerminado() {
		return todosDerrota(equipoHeroes) || todosDerrota(equipoVillanos);
	}

	// Muestra el HP y estado de todos los personajes
	private void mostrarEstadoCombate() {
		System.out.println("  Heroes:");
		for (Personaje p : equipoHeroes) {
			if (p.estaVivo()) System.out.println(p.resumenCombate());
			else              System.out.println("  " + p.getNombre() + " [CAIDO]");
		}
		System.out.println("  Villanos:");
		for (Personaje p : equipoVillanos) {
			if (p.estaVivo()) System.out.println(p.resumenCombate());
			else              System.out.println("  " + p.getNombre() + " [CAIDO]");
		}
	}

	private void mostrarResumenFinal() {
		System.out.println("\n=== FIN DEL COMBATE (Ronda " + ronda + ") ===");
		boolean heroesGanan = !todosDerrota(equipoHeroes);
		System.out.println(heroesGanan ? "  VICTORIA de los heroes!" : "  DERROTA de los heroes.");
		for (Personaje p : equipoHeroes)
			System.out.printf("    %-18s %s%n", p.getNombre(), p.estaVivo() ? "VIVO  HP:" + p.getVidaActual() : "CAIDO");
		for (Personaje p : equipoVillanos)
			System.out.printf("    %-18s %s%n", p.getNombre(), p.estaVivo() ? "VIVO  HP:" + p.getVidaActual() : "CAIDO");
	}

	private void pausar(int ms) {
		try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
	}
}
