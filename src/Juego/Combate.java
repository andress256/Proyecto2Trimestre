package Juego;

import Personajes.Personaje;
import java.sql.SQLException;
import java.util.List;

// Gestiona el bucle principal de un combate entre dos equipos.
// El combate es automatico: cada personaje actua segun su IA.
// Tras cada ronda se guarda automaticamente el estado en BD.
// Si dao es null (modo test), el guardado se omite.
public class Combate {

	private List<Personaje> equipoHeroes;
	private List<Personaje> equipoVillanos;
	private int ronda;
	private final PartidaDAO dao;
	private final int idPartida;
	private static final int PAUSA_MS = 300;

	// Constructor para nueva partida
	public Combate(List<Personaje> equipoHeroes, List<Personaje> equipoVillanos,
			PartidaDAO dao, int idPartida) {
		this(equipoHeroes, equipoVillanos, dao, idPartida, 0);
	}

	// Constructor para cargar partida desde una ronda concreta
	public Combate(List<Personaje> equipoHeroes, List<Personaje> equipoVillanos,
			PartidaDAO dao, int idPartida, int rondaInicial) {
		this.equipoHeroes = equipoHeroes;
		this.equipoVillanos = equipoVillanos;
		this.dao = dao;
		this.idPartida = idPartida;
		this.ronda = rondaInicial;
	}

	public void iniciar() {
		while (!combateTerminado()) {
			ronda++;
			System.out.println("\n--- Ronda " + ronda + " ---");
			mostrarEstadoCombate();
			pausar(PAUSA_MS);

			for (Personaje p : equipoHeroes) {
				if (!p.estaVivo() || todosDerrota(equipoVillanos)) continue;
				if (p.estaAturdido()) {
					System.out.println("  " + p.getNombre() + " esta aturdido, pierde su turno.");
					continue;
				}
				pausar(PAUSA_MS);
				p.elegirAccionIA(equipoHeroes, equipoVillanos);
			}

			if (combateTerminado()) { guardarAutomatico(); break; }

			for (Personaje p : equipoVillanos) {
				if (!p.estaVivo() || todosDerrota(equipoHeroes)) continue;
				if (p.estaAturdido()) {
					System.out.println("  " + p.getNombre() + " esta aturdido, pierde su turno.");
					continue;
				}
				pausar(PAUSA_MS);
				p.elegirAccionIA(equipoVillanos, equipoHeroes);
			}

			if (combateTerminado()) { guardarAutomatico(); break; }

			System.out.println("  [Efectos de turno]");
			for (Personaje p : equipoHeroes)   { if (p.estaVivo()) p.procesarEstados(); }
			for (Personaje p : equipoVillanos) { if (p.estaVivo()) p.procesarEstados(); }
			for (Personaje p : equipoHeroes)   p.reducirCooldowns();
			for (Personaje p : equipoVillanos) p.reducirCooldowns();

			guardarAutomatico();
		}

		mostrarResumenFinal();
	}

	// Guarda el estado en BD. Si dao es null (modo test), no hace nada.
	private void guardarAutomatico() {
		if (dao == null) return;
		try {
			dao.guardarPartida(idPartida, ronda, equipoHeroes, equipoVillanos);
			System.out.println("  [Guardado automatico: Partida " + idPartida + " - Ronda " + ronda + "]");
		} catch (SQLException e) {
			System.err.println("  [Error al guardar la partida] " + e.getMessage());
		}
	}

	private boolean todosDerrota(List<Personaje> equipo) {
		for (Personaje p : equipo) { if (p.estaVivo()) return false; }
		return true;
	}

	private boolean combateTerminado() {
		return todosDerrota(equipoHeroes) || todosDerrota(equipoVillanos);
	}

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
			System.out.printf("    %-18s %s%n", p.getNombre(),
					p.estaVivo() ? "VIVO  HP:" + p.getVidaActual() : "CAIDO");
		for (Personaje p : equipoVillanos)
			System.out.printf("    %-18s %s%n", p.getNombre(),
					p.estaVivo() ? "VIVO  HP:" + p.getVidaActual() : "CAIDO");
	}

	private void pausar(int ms) {
		try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
	}
}
