package Personajes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Armas.Arma;
import Clases.TipoClase;
import Estados.Estado;
import Hechizos.Hechizo;

public abstract class Personaje {

	protected String nombre;
	protected TipoClase tipoClase;

	protected int vidaMax;
	protected int vidaActual;
	protected int recursoMax;
	protected int recursoActual;

	protected int ataqueBase;
	protected int poderMagico;
	protected int defensaBase;
	
	protected Arma arma;
	protected List<Estado> estadosActivos;
	protected List<Hechizo> hechizos;
	protected Map<String, Integer> cooldowns;
	
	protected int barraAturdimiento;
	protected int maxBarraAturdimiento;
	
	public Personaje(String nombre, TipoClase tipoClase, int vidaMax, int recursoMax, int ataqueBase, int poderMagico, int defensaBase, int maxBarraAturdimiento) {
		this.nombre = nombre;
		this.tipoClase = tipoClase;
		this.vidaMax = vidaMax;
		this.vidaActual = vidaMax;
		this.recursoMax = recursoMax;
		this.recursoActual = recursoMax;
		this.ataqueBase = ataqueBase;
		this.poderMagico = poderMagico;
		this.defensaBase = defensaBase;
		this.maxBarraAturdimiento = maxBarraAturdimiento;
		this.barraAturdimiento = 0;
		this.estadosActivos = new ArrayList<>();
		this.hechizos = new ArrayList<>();
		this.cooldowns = new HashMap<>();
	}

}
