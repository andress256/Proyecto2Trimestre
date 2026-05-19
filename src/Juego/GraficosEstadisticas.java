package Juego;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GraficosEstadisticas {

	// Grafico 1: victorias y derrotas por jugador
	public static void graficoVictoriasPorJugador() throws SQLException {
		String sql = "SELECT nombre_jugador, "
				+ "SUM(CASE WHEN resultado = 'VICTORIA' THEN 1 ELSE 0 END) AS victorias, "
				+ "SUM(CASE WHEN resultado = 'DERROTA'  THEN 1 ELSE 0 END) AS derrotas "
				+ "FROM partida WHERE resultado != 'EN_CURSO' "
				+ "GROUP BY nombre_jugador ORDER BY victorias DESC";

		List<String>  jugadores = new ArrayList<>();
		List<Integer> victorias = new ArrayList<>();
		List<Integer> derrotas  = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				jugadores.add(rs.getString("nombre_jugador"));
				victorias.add(rs.getInt("victorias"));
				derrotas.add(rs.getInt("derrotas"));
			}
		}

		if (jugadores.isEmpty()) {
			System.out.println(" No hay datos suficientes para mostrar el grafico.");
			return;
		}

		CategoryChart chart = new CategoryChartBuilder()
				.width(700).height(450)
				.title("Victorias y Derrotas por Jugador")
				.xAxisTitle("Jugador")
				.yAxisTitle("Partidas")
				.theme(Styler.ChartTheme.Matlab)
				.build();

		chart.getStyler().setPlotGridLinesVisible(true);
		chart.getStyler().setAvailableSpaceFill(0.5);

		chart.addSeries("Victorias", jugadores, victorias);
		chart.addSeries("Derrotas",  jugadores, derrotas);

		new SwingWrapper<>(chart).displayChart();
	}

	// Grafico 2: duracion media de combate por jugador (en rondas)
	public static void graficoDuracionMedia() throws SQLException {
		String sql = "SELECT nombre_jugador, AVG(ronda_actual) AS promedio "
				+ "FROM partida WHERE resultado != 'EN_CURSO' "
				+ "GROUP BY nombre_jugador ORDER BY promedio DESC";

		List<String> jugadores = new ArrayList<>();
		List<Double>  promedios = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				jugadores.add(rs.getString("nombre_jugador"));
				promedios.add(rs.getDouble("promedio"));
			}
		}

		if (jugadores.isEmpty()) {
			System.out.println(" No hay datos suficientes para mostrar el grafico.");
			return;
		}

		CategoryChart chart = new CategoryChartBuilder()
				.width(700).height(450)
				.title("Duracion Media de Combate por Jugador")
				.xAxisTitle("Jugador")
				.yAxisTitle("Rondas (promedio)")
				.theme(Styler.ChartTheme.Matlab)
				.build();

		chart.getStyler().setPlotGridLinesVisible(true);
		chart.getStyler().setAvailableSpaceFill(0.4);

		chart.addSeries("Rondas promedio", jugadores, promedios);

		new SwingWrapper<>(chart).displayChart();
	}

	// Grafico 3: victorias y derrotas segun la dificultad
	public static void graficoResultadosPorDificultad() throws SQLException {
		String sql = "SELECT nombre_dificultad, "
				+ "SUM(CASE WHEN resultado = 'VICTORIA' THEN 1 ELSE 0 END) AS victorias, "
				+ "SUM(CASE WHEN resultado = 'DERROTA'  THEN 1 ELSE 0 END) AS derrotas "
				+ "FROM partida WHERE resultado != 'EN_CURSO' "
				+ "GROUP BY nombre_dificultad "
				+ "ORDER BY CASE nombre_dificultad "
				+ "  WHEN 'FACIL' THEN 1 WHEN 'NORMAL' THEN 2 WHEN 'DIFICIL' THEN 3 ELSE 4 END";

		List<String>  dificultades = new ArrayList<>();
		List<Integer> victorias    = new ArrayList<>();
		List<Integer> derrotas     = new ArrayList<>();

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				dificultades.add(rs.getString("nombre_dificultad"));
				victorias.add(rs.getInt("victorias"));
				derrotas.add(rs.getInt("derrotas"));
			}
		}

		if (dificultades.isEmpty()) {
			System.out.println(" No hay datos suficientes para mostrar el grafico.");
			return;
		}

		CategoryChart chart = new CategoryChartBuilder()
				.width(600).height(400)
				.title("Resultados por Dificultad")
				.xAxisTitle("Dificultad")
				.yAxisTitle("Partidas")
				.theme(Styler.ChartTheme.Matlab)
				.build();

		chart.getStyler().setPlotGridLinesVisible(true);
		chart.getStyler().setAvailableSpaceFill(0.5);

		chart.addSeries("Victorias", dificultades, victorias);
		chart.addSeries("Derrotas",  dificultades, derrotas);

		new SwingWrapper<>(chart).displayChart();
	}
}