package Juego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Accede a la tabla historial_partida de la BD.
// Registra los eventos de cada partida y permite consultarlos despues.
public class HistorialDAO {

	// Representa una fila del historial
	public static class EventoHistorial {
		public int    ronda;
		public String tipo;
		public String descripcion;
		public String fecha;
	}

	// Guarda un evento en el historial de la partida
	public void registrarEvento(int idPartida, int ronda, String tipo, String descripcion) throws SQLException {
		String sql = "INSERT INTO historial_partida (id_partida, ronda, tipo, descripcion) VALUES (?, ?, ?, ?)";

		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			ps.setInt(2, ronda);
			ps.setString(3, tipo);
			// Recortar a 500 caracteres por si la descripcion es muy larga
			ps.setString(4, descripcion.length() > 500 ? descripcion.substring(0, 497) + "..." : descripcion);
			ps.executeUpdate();
		}
	}

	// Devuelve todos los eventos de una partida ordenados por id (cronologico)
	public List<EventoHistorial> obtenerHistorial(int idPartida) throws SQLException {
		String sql = "SELECT ronda, tipo, descripcion, fecha FROM historial_partida "
				+ "WHERE id_partida = ? ORDER BY id_historial ASC";

		List<EventoHistorial> lista = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idPartida);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					EventoHistorial e = new EventoHistorial();
					e.ronda       = rs.getInt("ronda");
					e.tipo        = rs.getString("tipo");
					e.descripcion = rs.getString("descripcion");
					e.fecha       = rs.getTimestamp("fecha").toString();
					lista.add(e);
				}
			}
		}
		return lista;
	}
}
