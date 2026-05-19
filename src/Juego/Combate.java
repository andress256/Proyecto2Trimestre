package Juego;

import Personajes.Personaje;
import java.sql.SQLException;
import java.util.List;

public class Combate {

	private List<Personaje> equipoHeroes;
	private List<Personaje> equipoVillanos;
	private int ronda;
	private final PartidaDAO   dao;
	private final HistorialDAO historialDAO;
	private final int    idPartida;
	private static final int PAUSA_MS = 300;

	public Combate(List<Personaje> equipoHeroes, List<Personaje> equipoVillanos,
			PartidaDAO dao, int idPartida) {
		this(equipoHeroes, equipoVillanos, dao, idPartida, 0);
	}

	public Combate(List<Personaje> equipoHeroes, List<Personaje> equipoVillanos,
			PartidaDAO dao, int idPartida, int rondaInicial) {
		this.equipoHeroes   = equipoHeroes;
		this.equipoVillanos = equipoVillanos;
		this.dao            = dao;
		this.idPartida      = idPartida;
		this.ronda          = rondaInicial;
		this.historialDAO   = (dao != null) ? new HistorialDAO() : null;
	}

	public void iniciar() {
		registrarEvento("INICIO", "Heroes: " + resumirEquipo(equipoHeroes)
				+ " | Villanos: " + resumirEquipo(equipoVillanos));

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

			registrarEvento("RONDA", "Heroes: " + resumirEquipoVivos(equipoHeroes)
					+ " | Villanos: " + resumirEquipoVivos(equipoVillanos));

			guardarAutomatico();
		}

		marcarResultadoFinal();
		mostrarResumenFinal();
	}

	private void guardarAutomatico() {
		if (dao == null) return;
		try {
			dao.guardarPartida(idPartida, ronda, equipoHeroes, equipoVillanos);
			System.out.println("  [Guardado automatico: Partida " + idPartida + " - Ronda " + ronda + "]");
		} catch (SQLException e) {
			System.err.println("  [Error al guardar la partida] " + e.getMessage());
		}
	}

	private void marcarResultadoFinal() {
		if (dao == null) return;
		try {
			boolean heroesGanan = !todosDerrota(equipoHeroes);
			String resultado = heroesGanan ? "VICTORIA" : "DERROTA";
			dao.marcarResultado(idPartida, resultado);
			System.out.println("  [Resultado registrado en BD: " + resultado + "]");

			String supervivientes = heroesGanan
					? resumirEquipoVivos(equipoHeroes)
					: resumirEquipoVivos(equipoVillanos);
			registrarEvento("FIN", resultado + " en ronda " + ronda
					+ ". Supervivientes: " + supervivientes);
		} catch (SQLException e) {
			System.err.println("  [Error al marcar resultado] " + e.getMessage());
		}
	}

	private void registrarEvento(String tipo, String descripcion) {
		if (historialDAO == null) return;
		try {
			historialDAO.registrarEvento(idPartida, ronda, tipo, descripcion);
		} catch (SQLException e) {
			System.err.println("  [Error al registrar historial] " + e.getMessage());
		}
	}

	private String resumirEquipo(List<Personaje> equipo) {
		StringBuilder sb = new StringBuilder();
		for (Personaje p : equipo) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(p.getNombre()).append(" (").append(p.getVidaMax()).append("HP)");
		}
		return sb.toString();
	}

	private String resumirEquipoVivos(List<Personaje> equipo) {
		StringBuilder sb = new StringBuilder();
		for (Personaje p : equipo) {
			if (sb.length() > 0) sb.append(", ");
			if (p.estaVivo())
				sb.append(p.getNombre()).append(" (").append(p.getVidaActual()).append("HP)");
			else
				sb.append(p.getNombre()).append(" [CAIDO]");
		}
		return sb.toString();
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
