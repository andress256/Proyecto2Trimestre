package Personajes;

import java.util.ArrayList;
import java.util.List;
import Clases.*;

// Catalogo con todos los personajes disponibles del juego.
// Permite obtener listas de heroes y villanos, y generar equipos aleatorios.
public class catalogoPersonajes {

	// Devuelve la lista completa de heroes disponibles
	public static List<Personaje> obtenerHeroes() {
		List<Personaje> lista = new ArrayList<>();
		lista.add(new Guerrero("Gustave"));
		lista.add(new Duelista("Maelle"));
		lista.add(new Mago("Lune"));
		lista.add(new Sacerdote("Sciel"));
		lista.add(new Explorador("Verso"));
		lista.add(new Ilusionista("Monoco"));
		return lista;
	}

	// Devuelve la lista completa de villanos disponibles
	public static List<Personaje> obtenerVillanos() {
		List<Personaje> lista = new ArrayList<>();
		lista.add(new CaballeroOscuro("Renoir"));
		lista.add(new MagoOscuro("La Pintora"));
		lista.add(new GuardianPintado("Esquie"));
		lista.add(new BrutoPintado("Bruto de Cendo"));
		lista.add(new BrutoPintado("Centinela Oscuro"));
		lista.add(new GuardianPintado("Arquero del Monolito"));
		return lista;
	}

	// Genera un equipo aleatorio de 3 heroes sin repetir
	public static List<Personaje> generarEquipoHeroesRandom() {
		List<Personaje> todos = obtenerHeroes();
		List<Personaje> equipo = new ArrayList<>();
		List<Integer> usados = new ArrayList<>();

		while (equipo.size() < 3) {
			int idx = (int) (Math.random() * todos.size());
			// Solo añade el personaje si ese indice no se ha usado antes
			if (!usados.contains(idx)) {
				usados.add(idx);
				equipo.add(todos.get(idx));
			}
		}
		return equipo;
	}

	// Genera un equipo aleatorio de 3 villanos sin repetir
	public static List<Personaje> generarEquipoVillanosRandom() {
		List<Personaje> todos = obtenerVillanos();
		List<Personaje> equipo = new ArrayList<>();
		List<Integer> usados = new ArrayList<>();

		while (equipo.size() < 3) {
			int idx = (int) (Math.random() * todos.size());
			if (!usados.contains(idx)) {
				usados.add(idx);
				equipo.add(todos.get(idx));
			}
		}
		return equipo;
	}
}

