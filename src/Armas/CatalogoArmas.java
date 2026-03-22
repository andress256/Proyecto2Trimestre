package Armas;

import java.util.HashMap;
import java.util.Map;

public class CatalogoArmas {

	private static Map<Integer, Arma> catalogo = new HashMap<>();
	// Orden: nombre, dañoBase, probCritico, multiplicadorCritico
	static {
		// Armas Cuerpo a Cuerpo
		catalogo.put(1, new ArmaCuerpoACuerpo("Espadon de Gustave", 22, 0.15, 2.0));
		catalogo.put(2, new ArmaCuerpoACuerpo("Estoque de Maelle", 17, 0.28, 1.8));
		catalogo.put(3, new ArmaCuerpoACuerpo("Hoja del Olvido", 20, 0.20, 2.0));
		catalogo.put(4, new ArmaCuerpoACuerpo("Martillo Cromatico", 28, 0.10, 2.3));
		catalogo.put(5, new ArmaCuerpoACuerpo("Cuchillas Gemelas de Verso", 18, 0.30, 1.7));
		catalogo.put(6, new ArmaCuerpoACuerpo("Hacha del Monolito", 32, 0.08, 2.5));
		catalogo.put(7, new ArmaCuerpoACuerpo("Garras de Esquie", 15, 0.35, 1.6));
		catalogo.put(8, new ArmaCuerpoACuerpo("Gran Maza de Renoir", 38, 0.10, 2.2));

		// Armas a Distancia
		catalogo.put(9, new ArmaADistancia("Baston de Lune", 13, 0.20, 1.7, 0.40));
		catalogo.put(10, new ArmaADistancia("Arco Estelar de Sciel", 15, 0.18, 1.6, 0.35));
		catalogo.put(11, new ArmaADistancia("Varita de Monoco", 11, 0.15, 1.5, 0.50));
		catalogo.put(12, new ArmaADistancia("Rifle Cromatico", 20, 0.12, 2.0, 0.30));
		catalogo.put(13, new ArmaADistancia("Arco Largo de la Expedicion", 18, 0.16, 1.8, 0.45));
		catalogo.put(14, new ArmaADistancia("Cetro de la Pintora", 16, 0.22, 1.9, 0.55));
		catalogo.put(15, new ArmaADistancia("Ballesta del Abismo", 24, 0.14, 2.1, 0.25));

	}

	public static Arma getArma(int id) {
		return catalogo.get(id);
	}

	public static Map<Integer, Arma> getCatalogo() {
		return catalogo;
	}

	// Armas por defecto por clase
	public static Arma defectoGuerrero() {
		return new ArmaCuerpoACuerpo("Espadon de Gustave", 22, 0.15, 2.0);
	}

	public static Arma defectoDuelista() {
		return new ArmaCuerpoACuerpo("Estoque de Maelle", 17, 0.28, 1.8);
	}

	public static Arma defectoMago() {
		return new ArmaADistancia("Baston de Lune", 13, 0.20, 1.7, 0.40);
	}

	public static Arma defectoSacerdote() {
		return new ArmaADistancia("Arco Estelar de Sciel", 15, 0.18, 1.6, 0.35);
	}

	public static Arma defectoExplorador() {
		return new ArmaADistancia("Arco Largo de la Expedicion", 18, 0.16, 1.8, 0.45);
	}

	public static Arma defectoIlusionista() {
		return new ArmaADistancia("Varita de Monoco", 11, 0.15, 1.5, 0.50);
	}

	public static Arma defectoCaballeroOscuro() {
		return new ArmaCuerpoACuerpo("Gran Maza de Renoir", 38, 0.10, 2.2);
	}

	public static Arma defectoMagoOscuro() {
		return new ArmaADistancia("Cetro de la Pintora", 16, 0.22, 1.9, 0.55);
	}

	public static Arma defectoBruto() {
		return new ArmaCuerpoACuerpo("Martillo Cromatico", 28, 0.10, 2.3);
	}

	public static Arma defectoGuardian() {
		return new ArmaADistancia("Ballesta del Abismo", 24, 0.14, 2.1, 0.25);
	}
}
