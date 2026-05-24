package Juego;

import Personajes.catalogoPersonajes;
import Personajes.Personaje;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Juego {

	private final Scanner       sc            = new Scanner(System.in);
	private final PartidaDAO    dao           = new PartidaDAO();
	private final DificultadDAO dificultadDAO = new DificultadDAO();
	private final HistorialDAO  historialDAO  = new HistorialDAO();

	public void iniciar() {
		System.out.println("=================================================");
		System.out.println("    EXPEDITION 33 - RPG por turnos");
		System.out.println("=================================================");

		boolean salir = false;
		while (!salir) {
			mostrarMenu();
			int opcion = leerEntero();
			switch (opcion) {
				case 1 -> nuevaPartida();
				case 2 -> listarPartidas();
				case 3 -> cargarPartida();
				case 4 -> borrarPartida();
				case 5 -> mostrarRanking();
				case 6 -> mostrarHistorial();
				case 7 -> mostrarGraficos();
				case 8 -> salir = true;
				default -> System.out.println(" Opcion no valida.");
			}
		}

		sc.close();
		System.out.println("\nHasta pronto!");
	}

	private void mostrarMenu() {
		System.out.println("\n----------- MENU PRINCIPAL -----------");
		System.out.println(" 1. Nueva partida");
		System.out.println(" 2. Listar partidas guardadas");
		System.out.println(" 3. Cargar partida");
		System.out.println(" 4. Borrar partida");
		System.out.println(" 5. Ver ranking");
		System.out.println(" 6. Ver historial de partida");
		System.out.println(" 7. Ver estadisticas (graficos)");
		System.out.println(" 8. Salir");
		System.out.print(" Opcion: ");
	}

	private void nuevaPartida() {
	    System.out.print(" Nombre del jugador: ");
	    String nombre = sc.nextLine().trim();
	    if (nombre.isEmpty()) {
	        System.out.println(" El nombre no puede estar vacio.");
	        return;
	    }
	    if (nombre.length() > 50) {
	        System.out.println(" El nombre no puede superar los 50 caracteres. (Has puesto " + nombre.length() + ")");
	        return;
	    }

		try {
			DificultadDAO.Dificultad dificultad = seleccionarDificultad();
			System.out.println(" Dificultad seleccionada: " + dificultad.nombre);

			List<Personaje> heroes   = catalogoPersonajes.generarEquipoHeroesRandom();
			List<Personaje> villanos = catalogoPersonajes.generarEquipoVillanosRandom();

			for (Personaje h : heroes)   h.escalarConDificultad(dificultad.vidaHeroes,  dificultad.recursoHeroes, 1.0);
			for (Personaje v : villanos) v.escalarConDificultad(dificultad.vidaEnemigos, 1.0, dificultad.danioEnemigos);

			int idPartida = dao.crearPartida(nombre, dificultad.nombre, heroes, villanos);
			System.out.println("\n Partida creada con ID: " + idPartida);
			System.out.println(" Heroes:   " + nombres(heroes));
			System.out.println(" Villanos: " + nombres(villanos));
			pausar(1000);

			new Combate(heroes, villanos, dao, idPartida).iniciar();
		} catch (SQLException e) {
			System.err.println(" Error al crear la partida: " + e.getMessage());
		}
	}

	private DificultadDAO.Dificultad seleccionarDificultad() throws SQLException {
		List<DificultadDAO.Dificultad> lista = dificultadDAO.listarDificultades();

		System.out.println("\n Selecciona la dificultad:");
		for (int i = 0; i < lista.size(); i++) {
			DificultadDAO.Dificultad d = lista.get(i);
			System.out.printf(" %d. %-8s  (Villanos: x%.2f vida / x%.2f ataque | Heroes: x%.2f vida / x%.2f recurso)%n",
					i + 1, d.nombre, d.vidaEnemigos, d.danioEnemigos, d.vidaHeroes, d.recursoHeroes);
		}
		System.out.print(" Opcion: ");

		int idx = leerEntero() - 1;
		if (idx < 0 || idx >= lista.size()) {
			System.out.println(" Opcion no valida. Se usara NORMAL.");
			return dificultadDAO.obtenerDificultad("NORMAL");
		}
		return lista.get(idx);
	}

	private void listarPartidas() {
		try {
			List<String> lista = dao.listarPartidas();
			if (lista.isEmpty()) { System.out.println(" No hay partidas guardadas."); return; }
			System.out.println("\n--- PARTIDAS GUARDADAS ---");
			for (String linea : lista) System.out.println(" " + linea);
		} catch (SQLException e) {
			System.err.println(" Error al listar partidas: " + e.getMessage());
		}
	}

	private void cargarPartida() {
		System.out.print(" ID de la partida a cargar: ");
		int id = leerEntero();
		if (id <= 0) { System.out.println(" ID no valido."); return; }

		try {
			if (!dao.existePartida(id)) {
				System.out.println(" No existe ninguna partida con ID " + id + ".");
				return;
			}

			// Mostrar los turnos disponibles para que el jugador elija
			List<Integer> turnos = dao.obtenerTurnosDisponibles(id);
			if (turnos.isEmpty()) {
				System.out.println(" Esta partida no tiene turnos guardados.");
				return;
			}

			System.out.println(" Turnos disponibles: " + turnos);
			System.out.print(" Desde que turno quieres continuar? ");
			int turno = leerEntero();

			if (!turnos.contains(turno)) {
				System.out.println(" Turno no disponible. Cargando el ultimo turno guardado.");
				turno = turnos.get(turnos.size() - 1);
			}

			PartidaDAO.DatosPartida datos = dao.cargarPartida(id, turno);
			System.out.println("\n Cargando partida de " + datos.nombreJugador
					+ " desde el turno " + turno
					+ " (" + datos.nombreDificultad + ")");
			System.out.println(" Heroes:   " + nombres(datos.heroes));
			System.out.println(" Villanos: " + nombres(datos.villanos));
			pausar(1000);

			new Combate(datos.heroes, datos.villanos, dao, datos.idPartida, datos.rondaActual).iniciar();
		} catch (SQLException e) {
			System.err.println(" Error al cargar la partida: " + e.getMessage());
		}
	}

	private void borrarPartida() {
		System.out.print(" ID de la partida a borrar: ");
		int id = leerEntero();
		if (id <= 0) { System.out.println(" ID no valido."); return; }

		try {
			if (!dao.existePartida(id)) {
				System.out.println(" No existe ninguna partida con ID " + id + ".");
				return;
			}
			System.out.print(" Confirmar borrado de la partida " + id + " (s/n): ");
			if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
				System.out.println(" Borrado cancelado.");
				return;
			}
			dao.borrarPartida(id);
			System.out.println(" Partida " + id + " borrada correctamente.");
		} catch (SQLException e) {
			System.err.println(" Error al borrar la partida: " + e.getMessage());
		}
	}

	private void mostrarRanking() {
		try {
			List<PartidaDAO.EntradaRanking> ranking = dao.obtenerRanking();
			if (ranking.isEmpty()) {
				System.out.println(" Todavia no hay partidas terminadas. El ranking esta vacio.");
				return;
			}
			System.out.println("\n=========== RANKING DE JUGADORES ===========");
			System.out.printf(" %-3s | %-18s | %-9s | %-8s | %-5s | %-7s%n",
					"#", "Jugador", "Victorias", "Derrotas", "Total", "% Vict");
			System.out.println(" --------------------------------------------------------------");
			int pos = 1;
			for (PartidaDAO.EntradaRanking e : ranking) {
				System.out.printf(" %-3d | %-18s | %-9d | %-8d | %-5d | %5.1f%%%n",
						pos++, e.nombreJugador, e.victorias,
						e.derrotas, e.totalPartidas, e.porcentajeVictorias);
			}
			System.out.println(" ==============================================================");
		} catch (SQLException ex) {
			System.err.println(" Error al obtener el ranking: " + ex.getMessage());
		}
	}

	private void mostrarHistorial() {
		System.out.print(" ID de la partida: ");
		int id = leerEntero();
		if (id <= 0) { System.out.println(" ID no valido."); return; }

		try {
			if (!dao.existePartida(id)) {
				System.out.println(" No existe ninguna partida con ID " + id + ".");
				return;
			}
			List<HistorialDAO.EventoHistorial> historial = historialDAO.obtenerHistorial(id);
			if (historial.isEmpty()) {
				System.out.println(" Esta partida no tiene historial registrado.");
				return;
			}
			System.out.println("\n--- HISTORIAL DE LA PARTIDA " + id + " ---");
			for (HistorialDAO.EventoHistorial e : historial) {
				System.out.printf(" [%-6s - Turno %2d] %s%n", e.tipo, e.ronda, e.descripcion);
			}
			System.out.println(" -------------------------------------------");
		} catch (SQLException e) {
			System.err.println(" Error al obtener el historial: " + e.getMessage());
		}
	}

	private void mostrarGraficos() {
		boolean volver = false;
		while (!volver) {
			System.out.println("\n--- GRAFICOS DE ESTADISTICAS ---");
			System.out.println(" 1. Daño base de las armas");
			System.out.println(" 2. Vida maxima por clase de personaje");
			System.out.println(" 3. Volver al menu principal");
			System.out.print(" Opcion: ");

			switch (leerEntero()) {
				case 1 -> GraficosEstadisticas.graficoDañoArmas();
				case 2 -> GraficosEstadisticas.graficoVidaPersonajes();
				case 3 -> volver = true;
				default -> System.out.println(" Opcion no valida.");
			}
		}
	}

	private int leerEntero() {
		try { return Integer.parseInt(sc.nextLine().trim()); }
		catch (NumberFormatException e) { return -1; }
	}

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