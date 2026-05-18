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

// Genera graficos estadisticos del juego usando XChart.
// Cada metodo consulta la BD, construye el grafico y lo muestra en una ventana.
public class GraficosEstadisticas {

	// Grafico de barras: victorias y derrotas de cada jugador
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
				.theme(Styler.ChartTheme.GGPlot2)
				.build();

		chart.addSeries("Victorias", jugadores, victorias);
		chart.addSeries("Derrotas",  jugadores, derrotas);

		new SwingWrapper<>(chart).displayChart();
	}

	// Grafico de sectores: porcentaje global de victorias y derrotas
	public static void graficoResultadosGlobales() throws SQLException {
		String sql = "SELECT resultado, COUNT(*) AS total FROM partida "
				+ "WHERE resultado != 'EN_CURSO' GROUP BY resultado";

		int totalVictorias = 0;
		int totalDerrotas  = 0;

		try (Connection conn = ConexionBD.getConexion();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				if ("VICTORIA".equals(rs.getString("resultado")))
					totalVictorias = rs.getInt("total");
				else
					totalDerrotas  = rs.getInt("total");
			}
		}

		if (totalVictorias + totalDerrotas == 0) {
			System.out.println(" No hay datos suficientes para mostrar el grafico.");
			return;
		}

		PieChart chart = new PieChartBuilder()
				.width(500).height(400)
				.title("Resultados Globales (" + (totalVictorias + totalDerrotas) + " partidas)")
				.theme(Styler.ChartTheme.GGPlot2)
				.build();

		chart.addSeries("Victorias (" + totalVictorias + ")", totalVictorias);
		chart.addSeries("Derrotas ("  + totalDerrotas  + ")", totalDerrotas);

		new SwingWrapper<>(chart).displayChart();
	}

	// Grafico de barras: victorias y derrotas segun la dificultad elegida
	public static void graficoResultadosPorDificultad() throws SQLException {
		String sql = "SELECT nombre, "
				+ "SUM(CASE WHEN resultado = 'VICTORIA' THEN 1 ELSE 0 END) AS victorias, "
				+ "SUM(CASE WHEN resultado = 'DERROTA'  THEN 1 ELSE 0 END) AS derrotas "
				+ "FROM partida WHERE resultado != 'EN_CURSO' "
				+ "GROUP BY nombre "
				+ "ORDER BY CASE nombre "
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
				.theme(Styler.ChartTheme.GGPlot2)
				.build();

		chart.addSeries("Victorias", dificultades, victorias);
		chart.addSeries("Derrotas",  dificultades, derrotas);

		new SwingWrapper<>(chart).displayChart();
	}
}
