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

	public Personaje(String nombre, TipoClase tipoClase, int vidaMax, int recursoMax, int ataqueBase, int poderMagico,
			int defensaBase, int maxBarraAturdimiento) {
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

	public boolean estaVivo() {
		return vidaActual > 0;
	}

	public boolean estaAturdido() {
		for (Estado e : estadosActivos) {
			if (e instanceof Aturdido) return true;
		}
		return false;
	}

	// Daño fisico generico: resta la defensa del personaje.
	// Usado por hechizos fisicos como FlechaEnvenenada.
	public void recibirDaño(int cantidad) {
		int dañoReal = Math.max(1, cantidad - defensaBase);
		vidaActual = Math.max(0, vidaActual - dañoReal);
		incrementarBarraAturdimiento(dañoReal);
		if (!estaVivo()) {
			System.out.println("  >>  " + nombre + " ha caido!");
		}
	}

	// Daño de arma: la defensa YA fue calculada en el arma (calcularDaño es bidireccional).
	// Solo aplica el daño y sube la barra de aturdimiento, sin restar defensa otra vez.
	public void recibirDañoDeArma(int cantidad) {
		int dañoReal = Math.max(1, cantidad);
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

	public void curar(int cantidad) {
		vidaActual = Math.min(vidaMax, vidaActual + cantidad);
	}

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

	// Procesa todos los estados activos al final de cada ronda
	public void procesarEstados() {
		if (!estaVivo() || estadosActivos.isEmpty()) return;

		List<Estado> copia = new ArrayList<>(estadosActivos);
		List<Estado> aEliminar = new ArrayList<>();

		for (Estado e : copia) {
			if (!estaVivo()) break;
			e.alProcesarTurno(this);
			e.reducirDuracion();
			if (e.getTurnosRestantes() <= 0) {
				e.alExpirar(this);
				aEliminar.add(e);
			}
		}
		estadosActivos.removeAll(aEliminar);

		if (!estaVivo()) estadosActivos.clear();
	}

	public void reducirCooldowns() {
		List<String> aEliminar = new ArrayList<>();
		for (String nom : cooldowns.keySet()) {
			int valor = cooldowns.get(nom) - 1;
			if (valor <= 0) aEliminar.add(nom);
			else cooldowns.put(nom, valor);
		}
		for (String nom : aEliminar) cooldowns.remove(nom);
	}

	private void incrementarBarraAturdimiento(int cantidad) {
		if (estaAturdido()) return;
		barraAturdimiento += cantidad;
		if (barraAturdimiento >= maxBarraAturdimiento) {
			barraAturdimiento = 0;
			aplicarEstado(new Aturdido());
		}
	}

	// Calcula el daño del ataque basico usando el arma equipada.
	// El arma ya tiene en cuenta atacante y defensor (bidireccional).
	public int calcularDañoBasicoContra(Personaje objetivo) {
		if (arma == null) return Math.max(1, ataqueBase - objetivo.defensaBase);
		return Arma.calcularDaño(this, objetivo);
	}

	// Realiza un ataque fisico con el arma contra otro personaje.
	// Usa recibirDañoDeArma porque la defensa ya se calculo en el arma.
	public void atacarCon(Personaje objetivo) {
		int daño = calcularDañoBasicoContra(objetivo);
		System.out.printf("  %-16s ataca a %-16s -> %d dmg%n", nombre, objetivo.getNombre(), daño);
		objetivo.recibirDañoDeArma(daño);
	}

	// Aplica los multiplicadores de dificultad al personaje.
	public void escalarConDificultad(double multVida, double multRecurso, double multAtaque) {
		this.vidaMax       = Math.max(1, (int)(this.vidaMax * multVida));
		this.vidaActual    = this.vidaMax;
		this.recursoMax    = Math.max(0, (int)(this.recursoMax * multRecurso));
		this.recursoActual = this.recursoMax;
		this.ataqueBase    = Math.max(1, (int)(this.ataqueBase * multAtaque));
	}

	public abstract void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos);

	public String resumenCombate() {
		int barras = maxBarraAturdimiento > 0
				? (int) ((double) barraAturdimiento / maxBarraAturdimiento * 8) : 0;
		String barra = "[" + "=".repeat(barras) + " ".repeat(8 - barras) + "]";

		StringBuilder sb = new StringBuilder();
		sb.append(String.format(" %18s %16s  HP:%3d/%-3d  MP:%3d/%-3d  %s",
				nombre, "[" + tipoClase + "]", vidaActual, vidaMax, recursoActual, recursoMax, barra));

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
	public String getNombre()                  { return nombre; }
	public TipoClase getTipoClase()            { return tipoClase; }
	public int getVidaActual()                 { return vidaActual; }
	public int getVidaMax()                    { return vidaMax; }
	public int getAtaqueBase()                 { return ataqueBase; }
	public int getPoderMagico()                { return poderMagico; }
	public int getDefensaBase()                { return defensaBase; }
	public int getRecursoActual()              { return recursoActual; }
	public int getRecursoMax()                 { return recursoMax; }
	public int getBarraAturdimiento()          { return barraAturdimiento; }
	public Arma getArma()                      { return arma; }
	public List<Hechizo> getHechizos()         { return hechizos; }
	public Map<String, Integer> getCooldowns() { return cooldowns; }
	public List<Estado> getEstadosActivos()    { return estadosActivos; }

	// Setters para cargar partidas desde la BD
	public void setVidaActual(int v)         { this.vidaActual = Math.max(0, Math.min(vidaMax, v)); }
	public void setRecursoActual(int v)      { this.recursoActual = Math.max(0, Math.min(recursoMax, v)); }
	public void setBarraAturdimiento(int v)  { this.barraAturdimiento = Math.max(0, v); }
}