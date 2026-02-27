package Personajes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class catalogoPersonajes {

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

	public static List<Personaje> obtenerVillanos() {
		List<Personaje> lista = new ArrayList<>();
		lista.add(new CaballeroOscuro("Renoir"));
		lista.add(new MagoOscuro("La Pintora"));
		lista.add(new Guardian("Esquie"));
		lista.add(new BrutoPintado("Bruto de Cendo"));
		lista.add(new BrutoPintado("Centinela Oscuro"));
		lista.add(new GuardianPintado("Arquero del Monolito"));
		return lista;
	}
	//genera equipo de los heroes
	public static List<Personaje> generarEquipoHeroesRandom() {
		List<Personaje> todos = obtenerHeroes();
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
