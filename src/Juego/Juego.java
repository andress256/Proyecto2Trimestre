package Juego;

import Personajes.catalogoPersonajes;
import Personajes.Personaje;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Clase principal del juego. Menu interactivo con CRUD, ranking,
// historial, dificultad configurable, logros y graficos estadisticos.
public class Juego {

	private final Scanner       sc            = new Scanner(System.in);
	private final PartidaDAO    dao           = new PartidaDAO();
	private final DificultadDAO dificultadDAO = new DificultadDAO();
	private final HistorialDAO  historialDAO  = new HistorialDAO();
	private final LogroDAO      logroDAO      = new LogroDAO();

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
				case 7 -> mostrarLogros();
				case 8 -> mostrarGraficos();
				case 9 -> salir = true;
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
		System.out.println(" 7. Ver mis logros");
		System.out.println(" 8. Ver estadisticas (graficos)");
		System.out.println(" 9. Salir");
		System.out.print(" Opcion: ");
	}

	// Opcion 1: pide nombre y dificultad, genera equipos y arranca combate
	private void nuevaPartida() {
		System.out.print(" Nombre del jugador: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) {
			System.out.println(" El nombre no puede estar vacio.");
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

			new Combate(heroes, villanos, dao, idPartida, 0, nombre, dificultad.nombre).iniciar();
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

	// Opcion 2: lista todas las partidas
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

	// Opcion 3: carga una partida y retoma el combate
	private void cargarPartida() {
		System.out.print(" ID de la partida a cargar: ");
		int id = leerEntero();
		if (id <= 0) { System.out.println(" ID no valido."); return; }

		try {
			if (!dao.existePartida(id)) {
				System.out.println(" No existe ninguna partida con ID " + id + ".");
				return;
			}
			PartidaDAO.DatosPartida datos = dao.cargarPartida(id);
			System.out.println("\n Cargando partida de " + datos.nombreJugador
					+ " (Ronda " + datos.rondaActual + " - " + datos.nombreDificultad + ")");
			System.out.println(" Heroes:   " + nombres(datos.heroes));
			System.out.println(" Villanos: " + nombres(datos.villanos));
			pausar(1000);

			new Combate(datos.heroes, datos.villanos, dao, datos.idPartida,
					datos.rondaActual, datos.nombreJugador, datos.nombreDificultad).iniciar();
		} catch (SQLException e) {
			System.err.println(" Error al cargar la partida: " + e.getMessage());
		}
	}

	// Opcion 4: borra una partida
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

	// Opcion 5: ranking de jugadores
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

	// Opcion 6: historial de una partida
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
				System.out.printf(" [%-6s - Ronda %2d] %s%n", e.tipo, e.ronda, e.descripcion);
			}
			System.out.println(" -------------------------------------------");
		} catch (SQLException e) {
			System.err.println(" Error al obtener el historial: " + e.getMessage());
		}
	}

	// Opcion 7: logros de un jugador
	private void mostrarLogros() {
		System.out.print(" Nombre del jugador: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) { System.out.println(" El nombre no puede estar vacio."); return; }

		try {
			List<LogroDAO.Logro>             todos         = logroDAO.obtenerTodosLosLogros();
			List<LogroDAO.LogroDesbloqueado> desbloqueados = logroDAO.obtenerLogrosJugador(nombre);

			List<String> nombresObtenidos = new ArrayList<>();
			for (LogroDAO.LogroDesbloqueado ld : desbloqueados) nombresObtenidos.add(ld.nombre);

			System.out.println("\n=========== LOGROS DE " + nombre.toUpperCase() + " ===========");

			if (desbloqueados.isEmpty()) {
				System.out.println(" Todavia no has desbloqueado ningun logro.");
			} else {
				System.out.println(" Desbloqueados (" + desbloqueados.size() + "/" + todos.size() + "):");
				for (LogroDAO.LogroDesbloqueado ld : desbloqueados) {
					System.out.printf("  [v] %-20s - %s  (%s)%n",
							ld.nombre, ld.descripcion, ld.fechaDesbloqueo.substring(0, 16));
				}
			}

			List<LogroDAO.Logro> pendientes = new ArrayList<>();
			for (LogroDAO.Logro l : todos) {
				if (!nombresObtenidos.contains(l.nombre)) pendientes.add(l);
			}
			if (!pendientes.isEmpty()) {
				System.out.println(" Pendientes:");
				for (LogroDAO.Logro l : pendientes) {
					System.out.printf("  [ ] %-20s - %s%n", l.nombre, l.descripcion);
				}
			}
			System.out.println(" ================================================");
		} catch (SQLException e) {
			System.err.println(" Error al obtener los logros: " + e.getMessage());
		}
	}

	// Opcion 8: submenú de gráficos estadísticos con XChart
	private void mostrarGraficos() {
		boolean volver = false;
		while (!volver) {
			System.out.println("\n--- GRAFICOS DE ESTADISTICAS ---");
			System.out.println(" 1. Victorias y derrotas por jugador (barras)");
			System.out.println(" 2. Resultados globales (sectores)");
			System.out.println(" 3. Resultados por dificultad (barras)");
			System.out.println(" 4. Volver al menu principal");
			System.out.print(" Opcion: ");

			switch (leerEntero()) {
				case 1 -> {
					try { GraficosEstadisticas.graficoVictoriasPorJugador(); }
					catch (SQLException e) { System.err.println(" Error: " + e.getMessage()); }
				}
				case 2 -> {
					try { GraficosEstadisticas.graficoResultadosGlobales(); }
					catch (SQLException e) { System.err.println(" Error: " + e.getMessage()); }
				}
				case 3 -> {
					try { GraficosEstadisticas.graficoResultadosPorDificultad(); }
					catch (SQLException e) { System.err.println(" Error: " + e.getMessage()); }
				}
				case 4 -> volver = true;
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