package Juego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Accede a la tabla dificultad de la BD.
// El juego consulta aqui los multiplicadores en lugar de tenerlos en el codigo.
public class DificultadDAO {

	// Representa una fila de la tabla dificultad
	public static class Dificultad {
		public String nombre;
		public double vidaEnemigos;
		public double danioEnemigos;
		public double vidaHeroes;
		public double recursoHeroes;
	}

	// Devuelve todas las dificultades disponibles ordenadas por vida_enemigos
	public List<Dificultad> listarDificultades() throws SQLException {
		String sql = "SELECT nombre, vida_enemigos, danio_enemigos, vida_heroes, recurso_heroes "
				+ "FROM dificultad ORDER BY vida_enemigos ASC";

		List<Dificultad> lista = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				Dificultad d = new Dificultad();
				d.nombre         = rs.getString("nombre");
				d.vidaEnemigos   = rs.getDouble("vida_enemigos");
				d.danioEnemigos  = rs.getDouble("danio_enemigos");
				d.vidaHeroes     = rs.getDouble("vida_heroes");
				d.recursoHeroes  = rs.getDouble("recurso_heroes");
				lista.add(d);
			}
		}
		return lista;
	}

	// Devuelve la dificultad con ese nombre. Lanza excepcion si no existe.
	public Dificultad obtenerDificultad(String nombre) throws SQLException {
		String sql = "SELECT nombre, vida_enemigos, danio_enemigos, vida_heroes, recurso_heroes "
				+ "FROM dificultad WHERE nombre = ?";

		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, nombre);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					throw new SQLException("Dificultad no encontrada: " + nombre);
				Dificultad d = new Dificultad();
				d.nombre        = rs.getString("nombre");
				d.vidaEnemigos  = rs.getDouble("vida_enemigos");
				d.danioEnemigos = rs.getDouble("danio_enemigos");
				d.vidaHeroes    = rs.getDouble("vida_heroes");
				d.recursoHeroes = rs.getDouble("recurso_heroes");
				return d;
			}
		}
	}
}
