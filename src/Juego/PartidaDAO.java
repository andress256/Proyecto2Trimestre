package Juego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Clases.BrutoPintado;
import Clases.CaballeroOscuro;
import Clases.Duelista;
import Clases.Explorador;
import Clases.GuardianPintado;
import Clases.Guerrero;
import Clases.Ilusionista;
import Clases.Mago;
import Clases.MagoOscuro;
import Clases.Sacerdote;
import Personajes.Personaje;

public class PartidaDAO {

	// Datos de una partida cargada desde la BD
	public static class DatosPartida {
		public int    idPartida;
		public String nombreJugador;
		public int    rondaActual;
		public String nombreDificultad = "NORMAL";
		public List<Personaje> heroes   = new ArrayList<>();
		public List<Personaje> villanos = new ArrayList<>();
	}

	// Entrada del ranking de jugadores
	public static class EntradaRanking {
		public String nombreJugador;
		public int    victorias;
		public int    derrotas;
		public int    totalPartidas;
		public double porcentajeVictorias;
	}

	// SQL reutilizable para insertar un personaje con todos sus campos.
	// Los 10 parametros son: id_partida, ronda, nombre, clase,
	// vida_actual, vida_max, recurso_actual, recurso_max, barra_aturdimiento, es_aliado
	private static final String SQL_INSERTAR_PERSONAJE =
			"INSERT INTO personaje_partida "
			+ "(id_partida, ronda, nombre, clase, vida_actual, vida_max, "
			+ "recurso_actual, recurso_max, barra_aturdimiento, es_aliado) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	// --- CREATE ---

	// Crea la partida y guarda el estado inicial en el turno 0
	public int crearPartida(String nombreJugador, String dificultad,
			List<Personaje> heroes, List<Personaje> villanos) throws SQLException {

		String sqlPartida = "INSERT INTO partida (nombre_jugador, ronda_actual, nombre_dificultad) VALUES (?, 0, ?)";

		try (Connection conn = ConexionBD.getConexion()) {
			conn.setAutoCommit(false);
			int idPartida;

			try (PreparedStatement ps = conn.prepareStatement(sqlPartida, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, nombreJugador);
				ps.setString(2, dificultad);
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (!rs.next()) throw new SQLException("No se pudo obtener id_partida");
					idPartida = rs.getInt(1);
				}
			}

			// Estado inicial guardado en turno 0
			try (PreparedStatement ps = conn.prepareStatement(SQL_INSERTAR_PERSONAJE)) {
				for (Personaje p : heroes)   insertarPersonaje(ps, idPartida, 0, p, true);
				for (Personaje p : villanos) insertarPersonaje(ps, idPartida, 0, p, false);
				ps.executeBatch();
			}

			conn.commit();
			return idPartida;
		}
	}

	// Rellena los 10 parametros del PreparedStatement y lo añade al batch.
	// CORRECCION: antes faltaba el parametro 9 (barra_aturdimiento),
	// lo que causaba el error "No value specified for parameter 10"
	private void insertarPersonaje(PreparedStatement ps, int idPartida, int ronda,
			Personaje p, boolean esAliado) throws SQLException {
		ps.setInt(1, idPartida);
		ps.setInt(2, ronda);
		ps.setString(3, p.getNombre());
		ps.setString(4, p.getTipoClase().toString());
		ps.setInt(5, p.getVidaActual());
		ps.setInt(6, p.getVidaMax());
		ps.setInt(7, p.getRecursoActual());
		ps.setInt(8, p.getRecursoMax());
		ps.setInt(9, p.getBarraAturdimiento());  
		ps.setBoolean(10, esAliado);             
		ps.addBatch();
	}

	// --- READ ---

	public List<String> listarPartidas() throws SQLException {
		String sql = "SELECT id_partida, nombre_jugador, nombre_dificultad, ronda_actual, resultado, fecha_guardado "
				+ "FROM partida ORDER BY fecha_guardado DESC";
		List<String> resultado = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				resultado.add(String.format("[ID:%d] %-15s | %-8s | Ultimo turno: %-3d | %-9s | %s",
						rs.getInt("id_partida"),
						rs.getString("nombre_jugador"),
						rs.getString("nombre_dificultad"),
						rs.getInt("ronda_actual"),
						rs.getString("resultado"),
						rs.getTimestamp("fecha_guardado")));
			}
		}
		return resultado;
	}

	public boolean existePartida(int idPartida) throws SQLException {
		String sql = "SELECT COUNT(*) FROM partida WHERE id_partida = ?";
		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		}
	}

	// Devuelve los turnos guardados disponibles para una partida
	public List<Integer> obtenerTurnosDisponibles(int idPartida) throws SQLException {
		String sql = "SELECT DISTINCT ronda FROM personaje_partida WHERE id_partida = ? ORDER BY ronda ASC";
		List<Integer> turnos = new ArrayList<>();
		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) turnos.add(rs.getInt("ronda"));
			}
		}
		return turnos;
	}

	// Carga la cabecera de la partida (jugador, dificultad, ronda)
	public DatosPartida cargarCabecera(int idPartida) throws SQLException {
		String sql = "SELECT nombre_jugador, ronda_actual, nombre_dificultad FROM partida WHERE id_partida = ?";
		DatosPartida datos = new DatosPartida();
		datos.idPartida = idPartida;
		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) throw new SQLException("Partida no encontrada");
				datos.nombreJugador    = rs.getString("nombre_jugador");
				datos.rondaActual      = rs.getInt("ronda_actual");
				datos.nombreDificultad = rs.getString("nombre_dificultad");
			}
		}
		return datos;
	}

	// Carga el estado de los personajes desde un turno concreto
	public DatosPartida cargarPartida(int idPartida, int turno) throws SQLException {
		DatosPartida datos = cargarCabecera(idPartida);
		datos.rondaActual = turno;

		String sqlPersonajes = "SELECT nombre, clase, vida_actual, vida_max, recurso_actual, recurso_max, "
				+ "barra_aturdimiento, es_aliado "
				+ "FROM personaje_partida "
				+ "WHERE id_partida = ? AND ronda = ? "
				+ "ORDER BY es_aliado DESC, id_personaje ASC";

		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sqlPersonajes)) {
			ps.setInt(1, idPartida);
			ps.setInt(2, turno);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Personaje p = instanciarPorClase(rs.getString("clase"), rs.getString("nombre"));
					if (p == null) continue;
					p.setVidaActual(rs.getInt("vida_actual"));
					p.setRecursoActual(rs.getInt("recurso_actual"));
					p.setBarraAturdimiento(rs.getInt("barra_aturdimiento"));
					if (rs.getBoolean("es_aliado")) datos.heroes.add(p);
					else                            datos.villanos.add(p);
				}
			}
		}
		return datos;
	}

	private Personaje instanciarPorClase(String tipoClase, String nombre) {
		return switch (tipoClase) {
			case "GUERRERO"         -> new Guerrero(nombre);
			case "DUELISTA"         -> new Duelista(nombre);
			case "MAGO"             -> new Mago(nombre);
			case "SACERDOTE"        -> new Sacerdote(nombre);
			case "EXPLORADOR"       -> new Explorador(nombre);
			case "ILUSIONISTA"      -> new Ilusionista(nombre);
			case "CABALLERO_OSCURO" -> new CaballeroOscuro(nombre);
			case "MAGO_OSCURO"      -> new MagoOscuro(nombre);
			case "BRUTO_PINTADO"    -> new BrutoPintado(nombre);
			case "GUARDIAN_PINTADO" -> new GuardianPintado(nombre);
			default                 -> null;
		};
	}

	// --- UPDATE ---

	// Guarda el estado de los personajes en el turno indicado.
	// Hace INSERT (no UPDATE) para conservar el historial turno a turno.
	public void guardarPartida(int idPartida, int ronda,
			List<Personaje> heroes, List<Personaje> villanos) throws SQLException {

		String sqlPartida = "UPDATE partida SET ronda_actual = ? WHERE id_partida = ?";

		try (Connection conn = ConexionBD.getConexion()) {
			conn.setAutoCommit(false);

			// Actualiza el ultimo turno guardado en la cabecera
			try (PreparedStatement ps = conn.prepareStatement(sqlPartida)) {
				ps.setInt(1, ronda);
				ps.setInt(2, idPartida);
				ps.executeUpdate();
			}

			// Inserta el estado de todos los personajes para este turno
			try (PreparedStatement ps = conn.prepareStatement(SQL_INSERTAR_PERSONAJE)) {
				for (Personaje p : heroes)   insertarPersonaje(ps, idPartida, ronda, p, true);
				for (Personaje p : villanos) insertarPersonaje(ps, idPartida, ronda, p, false);
				ps.executeBatch();
			}

			conn.commit();
		}
	}

	public void marcarResultado(int idPartida, String resultado) throws SQLException {
		String sql = "UPDATE partida SET resultado = ? WHERE id_partida = ?";
		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, resultado);
			ps.setInt(2, idPartida);
			ps.executeUpdate();
		}
	}

	// --- DELETE ---

	public void borrarPartida(int idPartida) throws SQLException {
		String sql = "DELETE FROM partida WHERE id_partida = ?";
		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			ps.executeUpdate();
		}
	}

	// --- RANKING ---

	public List<EntradaRanking> obtenerRanking() throws SQLException {
		String sql = "SELECT nombre_jugador, "
				+ "SUM(CASE WHEN resultado = 'VICTORIA' THEN 1 ELSE 0 END) AS victorias, "
				+ "SUM(CASE WHEN resultado = 'DERROTA'  THEN 1 ELSE 0 END) AS derrotas, "
				+ "COUNT(*) AS total "
				+ "FROM partida WHERE resultado != 'EN_CURSO' "
				+ "GROUP BY nombre_jugador "
				+ "ORDER BY victorias DESC, total ASC";

		List<EntradaRanking> ranking = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				EntradaRanking e      = new EntradaRanking();
				e.nombreJugador       = rs.getString("nombre_jugador");
				e.victorias           = rs.getInt("victorias");
				e.derrotas            = rs.getInt("derrotas");
				e.totalPartidas       = rs.getInt("total");
				e.porcentajeVictorias = e.totalPartidas > 0
						? (e.victorias * 100.0 / e.totalPartidas) : 0;
				ranking.add(e);
			}
		}
		return ranking;
	}
}