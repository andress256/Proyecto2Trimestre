package Armas;

// Enum que actua como catalogo de todas las armas disponibles en el juego.
// Cada entrada del enum tiene asociada un objeto Arma con sus estadisticas.
public enum CatalogoArmas {

	// Armas Cuerpo a Cuerpo
	ESPADON_GUSTAVE       (new ArmaCuerpoACuerpo("Espadon de Gustave",        22, 0.15, 2.0)),
	ESTOQUE_MAELLE        (new ArmaCuerpoACuerpo("Estoque de Maelle",          17, 0.28, 1.8)),
	HOJA_OLVIDO           (new ArmaCuerpoACuerpo("Hoja del Olvido",            20, 0.20, 2.0)),
	MARTILLO_CROMATICO    (new ArmaCuerpoACuerpo("Martillo Cromatico",         28, 0.10, 2.3)),
	CUCHILLAS_VERSO       (new ArmaCuerpoACuerpo("Cuchillas Gemelas de Verso", 18, 0.30, 1.7)),
	HACHA_MONOLITO        (new ArmaCuerpoACuerpo("Hacha del Monolito",         32, 0.08, 2.5)),
	GARRAS_ESQUIE         (new ArmaCuerpoACuerpo("Garras de Esquie",           15, 0.35, 1.6)),
	GRAN_MAZA_RENOIR      (new ArmaCuerpoACuerpo("Gran Maza de Renoir",        38, 0.10, 2.2)),
	// Armas a Distancia
	BASTON_LUNE           (new ArmaADistancia("Baston de Lune",                13, 0.20, 1.7, 0.40)),
	ARCO_ESTELAR_SCIEL    (new ArmaADistancia("Arco Estelar de Sciel",         15, 0.18, 1.6, 0.35)),
	VARITA_MONOCO         (new ArmaADistancia("Varita de Monoco",              11, 0.15, 1.5, 0.50)),
	RIFLE_CROMATICO       (new ArmaADistancia("Rifle Cromatico",               20, 0.12, 2.0, 0.30)),
	ARCO_LARGO_EXPEDICION (new ArmaADistancia("Arco Largo de la Expedicion",   18, 0.16, 1.8, 0.45)),
	CETRO_PINTORA         (new ArmaADistancia("Cetro de la Pintora",           16, 0.22, 1.9, 0.55)),
	BALLESTA_ABISMO       (new ArmaADistancia("Ballesta del Abismo",           24, 0.14, 2.1, 0.25));

	private final Arma arma;

	CatalogoArmas(Arma arma) {
		this.arma = arma;
	}

	// Devuelve el objeto Arma asociado a esta entrada del enum
	public Arma getArma() {
		return arma;
	}
}