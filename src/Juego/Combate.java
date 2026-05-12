package Juego;

import Personajes.Personaje;
import java.sql.SQLException;
import java.util.List;

// Gestiona el bucle principal de un combate entre dos equipos.
// El combate es automatico: cada personaje actua segun su IA.
// Tras cada ronda se guarda automaticamente el estado en BD.
public class Combate {

	private List<Personaje> equipoHeroes;
	private List<Personaje> equipoVillanos;
	private int ronda;
	private final PartidaDAO dao;
	private final int idPartida;
	private static final int PAUSA_MS = 300; // pausa entre acciones en milisegundos

	// Constructor para una nueva partida (empieza en ronda 0)
	public Combate(List<Personaje> equipoHeroes, List<Personaje> equipoVillanos,
			PartidaDAO dao, int idPartida) {
		this(equipoHeroes, equipoVillanos, dao, idPartida, 0);
	}

	// Constructor para cargar una partida y retomar el combate desde la ronda guardada
	public Combate(List<Personaje> equipoHeroes, List<Personaje> equipoVillanos,
			PartidaDAO dao, int idPartida, int rondaInicial) {
		this.equipoHeroes = equipoHeroes;
		this.equipoVillanos = equipoVillanos;
		this.dao = dao;
		this.idPartida = idPartida;
		this.ronda = rondaInicial;
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

			if (combateTerminado()) { guardarAutomatico(); break; }

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

			if (combateTerminado()) { guardarAutomatico(); break; }

			// Fase de efectos: procesar estados (veneno, quemadura...) y reducir cooldowns
			System.out.println("  [Efectos de turno]");
			for (Personaje p : equipoHeroes)   { if (p.estaVivo()) p.procesarEstados(); }
			for (Personaje p : equipoVillanos) { if (p.estaVivo()) p.procesarEstados(); }
			for (Personaje p : equipoHeroes)   p.reducirCooldowns();
			for (Personaje p : equipoVillanos) p.reducirCooldowns();

			// Guardado automatico al final de la ronda
			guardarAutomatico();
		}

		mostrarResumenFinal();
	}

	// Guarda el estado actual de la partida en BD tras cada ronda
	private void guardarAutomatico() {
		try {
			dao.guardarPartida(idPartida, ronda, equipoHeroes, equipoVillanos);
			System.out.println("  [Guardado automatico: Partida " + idPartida + " - Ronda " + ronda + "]");
		} catch (SQLException e) {
			System.err.println("  [Error al guardar la partida] " + e.getMessage());
		}
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
