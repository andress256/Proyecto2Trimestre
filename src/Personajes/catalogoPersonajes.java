package Personajes;

import java.util.HashMap;
import java.util.Map;

import Clases.Guerrero;
import Clases.Mago;
import Clases.Sacerdote;
import Clases.TipoClase;
import Personajes.Personaje;

public class catalogoPersonajes {

	// Diccionario que asocia un ID (Integer) con un molde de Personaje [cite: 24,
	// 449]
	private Map<Integer, Personaje> personajesDisponibles;

	public catalogoPersonajes() {
		personajesDisponibles = new HashMap<>();
		cargarCatalogo();
	}

	// Método para llenar el catálogo con los personajes del juego
	private void cargarCatalogo() {
		// ID 1: Guerrero (Gus)
		personajesDisponibles.put(1, new Guerrero("Gus", TipoClase.GUERRERO, 150, 0, 30, 15, 0));

		// ID 2: Mago (Lune)
		personajesDisponibles.put(2, new Mago("Lune", TipoClase.MAGO, 80, 100, 10, 5, 40));

		// ID 3: Sacerdote (Renoir)
		personajesDisponibles.put(3, new Sacerdote("Renoir", TipoClase.SACERDOTE, 100, 80, 15, 10, 35));
	}

	// Método para mostrar las opciones al usuario en consola
	public void mostrarOpciones() {
		System.out.println("--- Catálogo de Personajes ---");
		for (Map.Entry<Integer, Personaje> entry : personajesDisponibles.entrySet()) {
			int id = entry.getKey();
			Personaje p = entry.getValue();
			System.out.println("ID: " + id + " | " + p.getNombre() + " - Clase: " + p.getTipoClase());
		}
	}

	// Método para obtener una COPIA del personaje elegido y meterlo en el equipo
	// Es importante devolver una copia para permitir repeticiones en el equipo
	// [cite: 15, 428]
	public Sacerdote obtenerPersonajePorId(int id) {
		Personaje molde = personajesDisponibles.get(id);

		if (molde == null)
			return null;

		// Según la clase, devolvemos una nueva instancia con los mismos stats
		switch (molde.getTipoClase()) {
		case GUERRERO:
			return new Guerrero(molde.getNombre(), molde.getTipoClase(), molde.vidaMax, molde.recursoMax,
					molde.ataqueBase, molde.defensaBase, molde.poderMagico);
		case MAGO:
			return new Mago(molde.getNombre(), molde.getTipoClase(), molde.vidaMax, molde.recursoMax, molde.ataqueBase,
					molde.defensaBase, molde.poderMagico);
		case SACERDOTE:
			return new Sacerdote(molde.getNombre(), molde.getTipoClase(), molde.vidaMax, molde.recursoMax,
					molde.ataqueBase, molde.defensaBase, molde.poderMagico);
		default:
			return null;
		}
	}
}
