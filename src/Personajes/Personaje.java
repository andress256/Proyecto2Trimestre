package Personajes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Armas.Arma;
import Estados.Aturdido;
import Estados.Estado;
import Hechizos.Hechizo;

// Clase base abstracta para todos los personajes del juego.
// Contiene los atributos comunes y la logica compartida de combate.

public abstract class Personaje {

	protected String nombre;
	protected TipoClase tipoClase;

	protected int vidaMax;
	protected int vidaActual;
	protected int recursoMax; // mana, energia, etc.
	protected int recursoActual;

	protected int ataqueBase;
	protected int poderMagico;
	protected int defensaBase;

	protected Arma arma;
	protected List<Estado> estadosActivos; // estados activos en este momento (veneno, quemadura...)
	protected List<Hechizo> hechizos; // hechizos disponibles del personaje
	protected Map<String, Integer> cooldowns; // hechizos en recarga: nombre -> turnos restantes

	protected int barraAturdimiento; // se llena al recibir daño
	protected int maxBarraAturdimiento; // cuando se llena, el personaje queda aturdido

	public Personaje(String nombre, TipoClase tipoClase, int vidaMax, int recursoMax, int ataqueBase, int poderMagico,
			int defensaBase, int maxBarraAturdimiento) {
		this.nombre = nombre;
		this.tipoClase = tipoClase;
		this.vidaMax = vidaMax;
		this.vidaActual = vidaMax; // empieza con vida al maximo
		this.recursoMax = recursoMax;
		this.recursoActual = recursoMax; // empieza con recurso al maximo
		this.ataqueBase = ataqueBase;
		this.poderMagico = poderMagico;
		this.defensaBase = defensaBase;
		this.maxBarraAturdimiento = maxBarraAturdimiento;
		this.barraAturdimiento = 0;
		this.estadosActivos = new ArrayList<>();
		this.hechizos = new ArrayList<>();
		this.cooldowns = new HashMap<>();
	}

	// Devuelve true si el personaje sigue con vida
	public boolean estaVivo() {
		return vidaActual > 0;
	}

	// Comprueba si tiene el estado Aturdido activo
	public boolean estaAturdido() {
		for (Estado e : estadosActivos) {
			if (e instanceof Aturdido)
				return true;
		}
		return false;
	}

	// Daño fisico: se reduce con la defensa del personaje
	public void recibirDaño(int cantidad) {
		int dañoReal = Math.max(1, cantidad - defensaBase);
		vidaActual = Math.max(0, vidaActual - dañoReal);
		incrementarBarraAturdimiento(dañoReal);
		if (!estaVivo()) {
			System.out.println("  >>  " + nombre + " ha caido!");
		}
	}

	// Daño puro: ignora la defensa (usado por magias y estados como veneno)
	public void recibirDañoPuro(int cantidad) {
		vidaActual = Math.max(0, vidaActual - cantidad);
		if (!estaVivo()) {
			System.out.println("  >>  " + nombre + " ha caido!");
		}
	}

	// Recupera vida sin superar el maximo
	public void curar(int cantidad) {
		int antes = vidaActual;
		vidaActual = Math.min(vidaMax, vidaActual + cantidad);
	}

	// Gasta recurso si hay suficiente. Devuelve false si no puede
	public boolean gastarRecurso(int coste) {
		if (recursoActual >= coste) {
			recursoActual -= coste;
			return true;
		}
		return false;
	}

	public void equiparArma(Arma arma) {
		this.arma = arma;
	}

	// Aplica un estado. Si ya existe el mismo, lo renueva en lugar de apilarlo
	public void aplicarEstado(Estado estado) {
		for (int i = 0; i < estadosActivos.size(); i++) {
			if (estadosActivos.get(i).getNombre().equals(estado.getNombre())) {
				estadosActivos.set(i, estado);
				estado.alAplicar(this);
				return;
			}
		}
		estadosActivos.add(estado);
		estado.alAplicar(this);
	}

	// Procesa todos los estados activos al final de cada ronda.
	// Usa una copia de la lista para evitar errores si un estado mata al personaje.
	public void procesarEstados() {
		if (!estaVivo() || estadosActivos.isEmpty())
			return;

		List<Estado> copia = new ArrayList<>(estadosActivos);
		List<Estado> aEliminar = new ArrayList<>();

		for (Estado e : copia) {
			if (!estaVivo())
				break;
			e.alProcesarTurno(this);
			e.reducirDuracion();
			if (e.getTurnosRestantes() <= 0) {
				e.alExpirar(this);
				aEliminar.add(e);
			}
		}
		estadosActivos.removeAll(aEliminar);

		if (!estaVivo()) {
			estadosActivos.clear();
		}
	}

	// Reduce en 1 el cooldown de todos los hechizos al final de cada ronda
	public void reducirCooldowns() {
		List<String> aEliminar = new ArrayList<>();
		for (String nom : cooldowns.keySet()) {
			int valor = cooldowns.get(nom) - 1;
			if (valor <= 0)
				aEliminar.add(nom);
			else
				cooldowns.put(nom, valor);
		}
		for (String nom : aEliminar)
			cooldowns.remove(nom);
	}

	// Incrementa la barra de aturdimiento. Si se llena, aplica el estado Aturdido
	private void incrementarBarraAturdimiento(int cantidad) {
		if (estaAturdido())
			return;
		barraAturdimiento += cantidad;
		if (barraAturdimiento >= maxBarraAturdimiento) {
			barraAturdimiento = 0;
			aplicarEstado(new Aturdido());
		}
	}

	// Calcula el daño del ataque basico usando el arma equipada
	public int calcularDañoBasicoContra(Personaje objetivo) {
		if (arma == null)
			return Math.max(1, ataqueBase - objetivo.defensaBase);
		return arma.calcularDaño(this, objetivo);
	}

	// Realiza un ataque fisico contra otro personaje
	public void atacarCon(Personaje objetivo) {
		int daño = calcularDañoBasicoContra(objetivo);
		System.out.printf("  %-16s ataca a %-16s -> %d dmg%n", nombre, objetivo.getNombre(), daño);
		objetivo.recibirDaño(daño);
	}

	// Cada subclase define su propia logica de combate (IA)
	public abstract void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos);

	// Devuelve una linea con el estado actual del personaje para mostrar en combate
	public String resumenCombate() {
		// Barra de aturdimiento visual [==== ]
		int barras = maxBarraAturdimiento > 0 ? (int) ((double) barraAturdimiento / maxBarraAturdimiento * 8) : 0;
		String barra = "[" + "=".repeat(barras) + " ".repeat(8 - barras) + "]";

		StringBuilder sb = new StringBuilder();
		sb.append(String.format(" %18s %16s  HP:%3d/%-3d  MP:%3d/%-3d  %s", nombre, "[" + tipoClase + "]", vidaActual,
				vidaMax, recursoActual, recursoMax, barra));

		if (!estadosActivos.isEmpty()) {
			sb.append("  (");
			for (Estado est : estadosActivos) {
				sb.append(est.getNombre()).append(":").append(est.getTurnosRestantes()).append("t ");
			}
			sb.append(")");
		}
		return sb.toString();
	}

	// Getters
	public String getNombre() {
		return nombre;
	}

	public TipoClase getTipoClase() {
		return tipoClase;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getVidaMax() {
		return vidaMax;
	}

	public int getAtaqueBase() {
		return ataqueBase;
	}

	public int getPoderMagico() {
		return poderMagico;
	}

	public int getDefensaBase() {
		return defensaBase;
	}

	public int getRecursoActual() {
		return recursoActual;
	}

	public int getRecursoMax() {
		return recursoMax;
	}

	public int getBarraAturdimiento() {
		return barraAturdimiento;
	}

	public Arma getArma() {
		return arma;
	}

	public List<Hechizo> getHechizos() {
		return hechizos;
	}

	public Map<String, Integer> getCooldowns() {
		return cooldowns;
	}

	// Setters necesarios para cargar partidas desde la BD
	public void setVidaActual(int v) {
		this.vidaActual = Math.max(0, Math.min(vidaMax, v));
	}

	public void setRecursoActual(int v) {
		this.recursoActual = Math.max(0, Math.min(recursoMax, v));
	}

	public void setBarraAturdimiento(int v) { this.barraAturdimiento = Math.max(0, v); }
