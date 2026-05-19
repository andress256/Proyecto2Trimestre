package Juego;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
import java.util.Arrays;
import java.util.List;

/**
 * Genera gráficos estadísticos del juego usando XChart.
 * Los datos de armas y personajes se toman directamente del código Java (sin BD).
 * 
 * @author Equipo DAM
 * @version 1.0
 */
public class GraficosEstadisticas {

	/**
	 * Muestra un gráfico de barras con el daño base de todas las armas del juego,
	 * ordenadas de menor a mayor daño.
	 */
	public static void graficoDanoArmas() {
		// Armas ordenadas de menor a mayor daño base (datos de CatalogoArmas)
		List<String>  nombres = Arrays.asList(
				"Varita Monoco",
				"Baston Lune",
				"Arco Sciel",
				"Cetro Pintora",
				"Garras Esquie",
				"Estoque Maelle",
				"Arco Expedicion",
				"Cuchillas Verso",
				"Rifle Cromatico",
				"Hoja Olvido",
				"Espadon Gustave",
				"Ballesta Abismo",
				"Martillo Cromatico",
				"Hacha Monolito",
				"Gran Maza Renoir"
		);
		List<Integer> danos = Arrays.asList(
				11, 13, 15, 16, 15, 17, 18, 18, 20, 20, 22, 24, 28, 32, 38
		);

		CategoryChart chart = new CategoryChartBuilder()
				.width(950).height(500)
				.title("Daño Base de las Armas")
				.xAxisTitle("Arma")
				.yAxisTitle("Daño Base")
				.theme(Styler.ChartTheme.Matlab)
				.build();

		chart.getStyler().setAvailableSpaceFill(0.5);
		chart.getStyler().setPlotGridLinesVisible(true);

		chart.addSeries("Daño base", nombres, danos);

		new SwingWrapper<>(chart).displayChart();
	}

	/**
	 * Muestra un gráfico de barras con la vida máxima de todas las clases
	 * de personajes del juego, ordenadas de menor a mayor vida.
	 */
	public static void graficoVidaPersonajes() {
		// Personajes ordenados de menor a mayor vida máxima (datos de cada clase)
		List<String>  clases = Arrays.asList(
				"Mago",
				"Ilusionista",
				"Sacerdote",
				"Guardian Pintado",
				"Duelista",
				"Explorador",
				"Bruto Pintado",
				"Mago Oscuro",
				"Guerrero",
				"Caballero Oscuro"
		);
		List<Integer> vidas = Arrays.asList(
				130, 135, 140, 140, 150, 155, 160, 170, 200, 250
		);

		CategoryChart chart = new CategoryChartBuilder()
				.width(800).height(500)
				.title("Vida Maxima por Clase de Personaje")
				.xAxisTitle("Clase")
				.yAxisTitle("Vida Maxima")
				.theme(Styler.ChartTheme.Matlab)
				.build();

		chart.getStyler().setAvailableSpaceFill(0.5);
		chart.getStyler().setPlotGridLinesVisible(true);

		chart.addSeries("Vida maxima", clases, vidas);

		new SwingWrapper<>(chart).displayChart();
	}
}