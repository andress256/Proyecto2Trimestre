package Juego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Gestiona los logros del juego en BD.
// Verifica condiciones al terminar cada combate y registra los nuevos desbloqueos.
public class LogroDAO {

	// Representa una fila de la tabla logro
	public static class Logro {
		public int    id;
		public String nombre;
		public String descripcion;
		public String condicion;
	}

	// Representa un logro desbloqueado por un jugador
	public static class LogroDesbloqueado {
		public String nombre;
		public String descripcion;
		public String fechaDesbloqueo;
	}

	// Devuelve todos los logros definidos en BD
	public List<Logro> obtenerTodosLosLogros() throws SQLException {
		String sql = "SELECT id_logro, nombre, descripcion, condicion FROM logro ORDER BY id_logro";
		List<Logro> lista = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				Logro l      = new Logro();
				l.id          = rs.getInt("id_logro");
				l.nombre      = rs.getString("nombre");
				l.descripcion = rs.getString("descripcion");
				l.condicion   = rs.getString("condicion");
				lista.add(l);
			}
		}
		return lista;
	}

	// Devuelve los logros ya desbloqueados por un jugador
	public List<LogroDesbloqueado> obtenerLogrosJugador(String nombreJugador) throws SQLException {
		String sql = "SELECT l.nombre, l.descripcion, lj.fecha_desbloqueo "
				+ "FROM logro_jugador lj "
				+ "JOIN logro l ON lj.id_logro = l.id_logro "
				+ "WHERE lj.nombre_jugador = ? "
				+ "ORDER BY lj.fecha_desbloqueo ASC";
		List<LogroDesbloqueado> lista = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, nombreJugador);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					LogroDesbloqueado ld = new LogroDesbloqueado();
					ld.nombre           = rs.getString("nombre");
					ld.descripcion      = rs.getString("descripcion");
					ld.fechaDesbloqueo  = rs.getTimestamp("fecha_desbloqueo").toString();
					lista.add(ld);
				}
			}
		}
		return lista;
	}

	// Intenta desbloquear un logro para un jugador.
	// Devuelve true si se ha desbloqueado ahora, false si ya lo tenia.
	// INSERT IGNORE ignora el INSERT si ya existe esa combinacion (clave UNIQUE).
	public boolean desbloquearLogro(String nombreJugador, int idLogro) throws SQLException {
		String sql = "INSERT IGNORE INTO logro_jugador (nombre_jugador, id_logro) VALUES (?, ?)";
		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, nombreJugador);
			ps.setInt(2, idLogro);
			return ps.executeUpdate() > 0; // true = nuevo desbloqueo
		}
	}

	// Cuenta las partidas terminadas (VICTORIA o DERROTA) de un jugador
	private int contarPartidasTerminadas(String nombreJugador) throws SQLException {
		String sql = "SELECT COUNT(*) FROM partida WHERE nombre_jugador = ? AND resultado != 'EN_CURSO'";
		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, nombreJugador);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	// Verifica todas las condiciones y desbloquea los logros que correspondan.
	// Se llama automaticamente al terminar cada combate desde Combate.java.
	public void verificarYDesbloquearLogros(String nombreJugador, boolean victoria,
			String dificultad, int rondasTotales, int heroesVivos) throws SQLException {

		if (nombreJugador == null || nombreJugador.isEmpty()) return;

		int totalPartidas = contarPartidasTerminadas(nombreJugador);
		List<Logro> todos = obtenerTodosLosLogros();

		for (Logro logro : todos) {
			// Evaluar si se cumple la condicion segun el codigo de BD
			boolean cumplida = switch (logro.condicion) {
				case "PRIMERA_VICTORIA" -> victoria;
				case "HEROE_LEGENDARIO" -> victoria && "DIFICIL".equals(dificultad);
				case "VICTORIA_RAPIDA"  -> victoria && rondasTotales <= 5;
				case "EQUIPO_INVICTO"   -> victoria && heroesVivos == 3;
				case "VETERANO"         -> totalPartidas >= 3;
				default                 -> false;
			};

			if (cumplida) {
				boolean esNuevo = desbloquearLogro(nombreJugador, logro.id);
				if (esNuevo) {
					System.out.println("\n  *** LOGRO DESBLOQUEADO: " + logro.nombre
							+ " *** (" + logro.descripcion + ")");
				}
			}
		}
	}
}