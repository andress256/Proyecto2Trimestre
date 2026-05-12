package Juego;

import Personajes.catalogoPersonajes;
import Personajes.Personaje;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class Juego {

	private final Scanner sc = new Scanner(System.in);
	private final PartidaDAO dao = new PartidaDAO();

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
				case 5 -> salir = true;
				default -> System.out.println(" Opcion no valida.");
			}
		}

		sc.close();
		System.out.println("\nHasta pronto!");
	}

	// Imprime el menu principal
	private void mostrarMenu() {
		System.out.println("\n----------- MENU PRINCIPAL -----------");
		System.out.println(" 1. Nueva partida");
		System.out.println(" 2. Listar partidas guardadas");
		System.out.println(" 3. Cargar partida");
		System.out.println(" 4. Borrar partida");
		System.out.println(" 5. Salir");
		System.out.print(" Opcion: ");
	}

	// Opcion 1: Crea una nueva partida y arranca el combate
	private void nuevaPartida() {
		System.out.print(" Nombre del jugador: ");
		String nombre = sc.nextLine().trim();
		if (nombre.isEmpty()) {
			System.out.println(" El nombre no puede estar vacio.");
			return;
		}

		// Genera equipos aleatorios de 3 personajes cada uno
		List<Personaje> heroes   = catalogoPersonajes.generarEquipoHeroesRandom();
		List<Personaje> villanos = catalogoPersonajes.generarEquipoVillanosRandom();

		try {
			int idPartida = dao.crearPartida(nombre, heroes, villanos);
			System.out.println("\n Partida creada con ID: " + idPartida);
			System.out.println(" Heroes:   " + nombres(heroes));
			System.out.println(" Villanos: " + nombres(villanos));
			pausar(1000);

			// Lanza el combate con guardado automatico
			new Combate(heroes, villanos, dao, idPartida).iniciar();
		} catch (SQLException e) {
			System.err.println(" Error al crear la partida: " + e.getMessage());
		}
	}

	// Opcion 2: Muestra todas las partidas guardadas
	private void listarPartidas() {
		try {
			List<String> lista = dao.listarPartidas();
			if (lista.isEmpty()) {
				System.out.println(" No hay partidas guardadas.");
				return;
			}
			System.out.println("\n--- PARTIDAS GUARDADAS ---");
			for (String linea : lista) System.out.println(" " + linea);
		} catch (SQLException e) {
			System.err.println(" Error al listar partidas: " + e.getMessage());
		}
	}

	// Opcion 3: Carga una partida desde su ID y retoma el combate
	private void cargarPartida() {
		System.out.print(" ID de la partida a cargar: ");
		int id = leerEntero();
		if (id <= 0) {
			System.out.println(" ID no valido.");
			return;
		}

		try {
			if (!dao.existePartida(id)) {
				System.out.println(" No existe ninguna partida con ID " + id + ".");
				return;
			}

			PartidaDAO.DatosPartida datos = dao.cargarPartida(id);
			System.out.println("\n Cargando partida de " + datos.nombreJugador
					+ " (Ronda " + datos.rondaActual + ")");
			System.out.println(" Heroes:   " + nombres(datos.heroes));
			System.out.println(" Villanos: " + nombres(datos.villanos));
			pausar(1000);

			// Continua el combate desde la ronda guardada
			new Combate(datos.heroes, datos.villanos, dao, datos.idPartida, datos.rondaActual).iniciar();
		} catch (SQLException e) {
			System.err.println(" Error al cargar la partida: " + e.getMessage());
		}
	}

	// Opcion 4: Borra una partida tras confirmar
	private void borrarPartida() {
		System.out.print(" ID de la partida a borrar: ");
		int id = leerEntero();
		if (id <= 0) {
			System.out.println(" ID no valido.");
			return;
		}

		try {
			if (!dao.existePartida(id)) {
				System.out.println(" No existe ninguna partida con ID " + id + ".");
				return;
			}

			System.out.print(" Confirmar borrado de la partida " + id + " (s/n): ");
			String resp = sc.nextLine().trim();
			if (!resp.equalsIgnoreCase("s")) {
				System.out.println(" Borrado cancelado.");
				return;
			}

			dao.borrarPartida(id);
			System.out.println(" Partida " + id + " borrada correctamente.");
		} catch (SQLException e) {
			System.err.println(" Error al borrar la partida: " + e.getMessage());
		}
	}

	// Lee un entero de teclado. Devuelve -1 si la entrada no es valida
	private int leerEntero() {
		try {
			return Integer.parseInt(sc.nextLine().trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	// Devuelve los nombres de una lista de personajes separados por comas
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
