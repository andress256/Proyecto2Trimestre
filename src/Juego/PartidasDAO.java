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

public class PartidasDAO {

	public static class DatosPartida {
		public int idPartida;
		public String nombreJugador;
		public int rondaActual;
		public List<Personaje> heroes = new ArrayList<>();
		public List<Personaje> villanos = new ArrayList<>();
	}

	public int crearPartida(String nombreJugador, List<Personaje> heroes, List<Personaje> villanos)
			throws SQLException {
		String sqlPartida = "INSERT INTO partida (nombre_jugador, ronda_actual) VALUES (?, 1)";
		String sqlPersonaje = "INSERT INTO personaje_partida (id_partida, nombre, clase, vida_actual, vida_max, recurso_actual, recurso_max, barra_aturdimiento, es_aliado) VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?)";

		try (Connection conn = ConexionBD.getConexion()) {
			conn.setAutoCommit(false);
			int idPartida;

			try (PreparedStatement ps = conn.prepareStatement(sqlPartida, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, nombreJugador);
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (!rs.next())
						throw new SQLException("No se pudo obtener id_partida");
					idPartida = rs.getInt(1);
				}
			}

			try (PreparedStatement ps = conn.prepareStatement(sqlPersonaje)) {
				for (Personaje p : heroes)
					insertarPersonaje(ps, idPartida, p, true);
				for (Personaje p : villanos)
					insertarPersonaje(ps, idPartida, p, false);
				ps.executeBatch();
			}

			conn.commit();
			return idPartida;
		}
	}

	private void insertarPersonaje(PreparedStatement ps, int idPartida, Personaje p, boolean esAliado)
			throws SQLException {
		ps.setInt(1, idPartida);
		ps.setString(2, p.getNombre());
		ps.setString(3, p.getTipoClase().toString());
		ps.setInt(4, p.getVidaActual());
		ps.setInt(5, p.getVidaMax());
		ps.setInt(6, p.getRecursoActual());
		ps.setInt(7, p.getRecursoMax());
		ps.setBoolean(8, esAliado);
		ps.addBatch();
	}

	public List<String> listarPartidas() throws SQLException {
		String sql = "SELECT id_partida, nombre_jugador, ronda_actual, fecha_guardado FROM partida ORDER BY fecha_guardado DESC";
		List<String> resultado = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				resultado.add(String.format("[%d] %s - Ronda %d (%s)", rs.getInt("id_partida"),
						rs.getString("nombre_jugador"), rs.getInt("ronda_actual"), rs.getTimestamp("fecha_guardado")));
			}
		}
		return resultado;
	}

	public boolean existePartida(int idPartida) throws SQLException {
		String sql = "SELECT COUNT(*) FROM partida WHERE id_partida = ?";
		try (Connection conn = ConexionBD.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		}
	}

	public DatosPartida cargarPartida(int idPartida) throws SQLException {
		DatosPartida datos = new DatosPartida();

		String sqlCabecera = "SELECT nombre_jugador, ronda_actual FROM partida WHERE id_partida = ?";
		String sqlPersonajes = "SELECT nombre, clase, vida_actual, recurso_actual, barra_aturdimiento, es_aliado FROM personaje_partida WHERE id_partida = ? ORDER BY es_aliado DESC, id_personaje ASC";

		try (Connection conn = ConexionBD.getConexion()) {
			datos.idPartida = idPartida;

			try (PreparedStatement ps = conn.prepareStatement(sqlCabecera)) {
				ps.setInt(1, idPartida);
				try (ResultSet rs = ps.executeQuery()) {
					if (!rs.next())
						throw new SQLException("Partida no encontrada");
					datos.nombreJugador = rs.getString("nombre_jugador");
					datos.rondaActual = rs.getInt("ronda_actual");
				}
			}

			try (PreparedStatement ps = conn.prepareStatement(sqlPersonajes)) {
				ps.setInt(1, idPartida);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Personaje p = instanciarPorClase(rs.getString("clase"), rs.getString("nombre"));
						if (p == null)
							continue;

						p.setVidaActual(rs.getInt("vida_actual"));
						p.setRecursoActual(rs.getInt("recurso_actual"));
						p.setBarraAturdimiento(rs.getInt("barra_aturdimiento"));

						if (rs.getBoolean("es_aliado"))
							datos.heroes.add(p);
						else
							datos.villanos.add(p);
					}
				}
			}
		}
		return datos;
	}

	private Personaje instanciarPorClase(String tipoClase, String nombre) {
		return switch (tipoClase) {
		case "GUERRERO" -> new Guerrero(nombre);
		case "DUELISTA" -> new Duelista(nombre);
		case "MAGO" -> new Mago(nombre);
		case "SACERDOTE" -> new Sacerdote(nombre);
		case "EXPLORADOR" -> new Explorador(nombre);
		case "ILUSIONISTA" -> new Ilusionista(nombre);
		case "CABALLERO_OSCURO" -> new CaballeroOscuro(nombre);
		case "MAGO_OSCURO" -> new MagoOscuro(nombre);
		case "BRUTO_PINTADO" -> new BrutoPintado(nombre);
		case "GUARDIAN_PINTADO" -> new GuardianPintado(nombre);
		default -> null;
		};
	}

	public void guardarPartida(int idPartida, int rondaActual, List<Personaje> heroes, List<Personaje> villanos)
			throws SQLException {
		String sqlPartida = "UPDATE partida SET ronda_actual = ? WHERE id_partida = ?";
		String sqlPersonaje = "UPDATE personaje_partida SET vida_actual = ?, recurso_actual = ?, barra_aturdimiento = ? WHERE id_partida = ? AND nombre = ? AND es_aliado = ?";

		try (Connection conn = ConexionBD.getConexion()) {
			conn.setAutoCommit(false);

			try (PreparedStatement ps = conn.prepareStatement(sqlPartida)) {
				ps.setInt(1, rondaActual);
				ps.setInt(2, idPartida);
				ps.executeUpdate();
			}

			try (PreparedStatement ps = conn.prepareStatement(sqlPersonaje)) {
				for (Personaje p : heroes)
					actualizarPersonaje(ps, idPartida, p, true);
				for (Personaje p : villanos)
					actualizarPersonaje(ps, idPartida, p, false);
				ps.executeBatch();
			}

			conn.commit();
		}
	}

	private void actualizarPersonaje(PreparedStatement ps, int idPartida, Personaje p, boolean esAliado)
			throws SQLException {
		ps.setInt(1, p.getVidaActual());
		ps.setInt(2, p.getRecursoActual());
		ps.setInt(3, p.getBarraAturdimiento());
		ps.setInt(4, idPartida);
		ps.setString(5, p.getNombre());
		ps.setBoolean(6, esAliado);
		ps.addBatch();
	}

	public void borrarPartida(int idPartida) throws SQLException {
		String sql = "DELETE FROM partida WHERE id_partida = ?";
		try (Connection conn = ConexionBD.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			ps.executeUpdate();
		}
	}
}
