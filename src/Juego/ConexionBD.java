package Juego;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;

	public class ConexionBD {

		private static final String URL      = "jdbc:mysql://localhost:3306/proyectorpg";
		private static final String USUARIO  = "root";
		private static final String PASSWORD = "";

		// Devuelve una nueva conexion a la BD. Lanza SQLException si falla.
		public static Connection getConexion() throws SQLException {
			return DriverManager.getConnection(URL, USUARIO, PASSWORD);
		}
	}

