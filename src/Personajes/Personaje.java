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
	
	public boolean estaVivo() {
		return vidaActual > 0;
	}

	public boolean estaAturdido() {
		for (Estado e : estadosActivos) {
			if (e instanceof Aturdido) return true;
		}
		return false;
	}
	
	public void recibirDanio(int cantidad) {
		int danioReal = Math.max(1, cantidad - defensaBase);
		vidaActual = Math.max(0, vidaActual - danioReal);
		incrementarBarraAturdimiento(danioReal);
		if (!estaVivo()) {
			System.out.println("  >>  " + nombre + " ha caido!");
		}
	}
	
	public void recibirDanioPuro(int cantidad) {
		vidaActual = Math.max(0, vidaActual - cantidad);
		if (!estaVivo()) {
			System.out.println("  >>  " + nombre + " ha caido!");
		}
	}
	
	public void curar(int cantidad) {
		int antes = vidaActual;
		vidaActual = Math.min(vidaMax, vidaActual + cantidad);
	}

	
	public boolean gastarRecurso(int coste) {
		if (recursoActual >= coste) {
			recursoActual -= coste;
			return true;
		}
		return false;
	}
	
	public void equiparArma (Arma arma) {
		this.arma = arma;
	}

	public void aplicarEstado(Estado estado) {
		for(int i = 0; i < estadosActivos.size(); i++) {
			if(estadosActivos.get(i).getNombre().equals(estado.getNombre())) {
				estadosActivos.set(i, estado);
				estado.alAplicar(this);
				return;
			}
		}
		estadosActivos.add(estado);
		estado.alAplicar(this);
	}
	
	public void procesarEstados() {
		if(!estaVivo() || estadosActivos.isEmpty()) return;
		
		List<Estado> copia = new ArrayList<>(estadosActivos);
		List<Estado> aEliminar = new ArrayList<>();
		
		for (Estado e : copia) {
			if (!estaVivo()) break;
			e.alProcesarTurno(this);
			e.reducirDuracion();
			if(e.getTurnosRestantes() <= 0) {
				e.alExpirar(this);
				aElminar.add(e);
			}
		}
		estadosActivos.removeAll(aEliminar);
		
		if (!estaVivo()) {
			estadosActivos.clear();
		}
	}
	
	public void reducirCooldowns() {
		List<String> aEliminar = new ArrayList<>();
		for (String nom : cooldowns.keySet()) {
			int valor = cooldowns.get(nom) - 1;
			if( valor <= 0) aEliminar.add(nom);
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
	
	public int calcularDanioBasicoContra(Personaje objetivo) {
		if (arma == null) return Math.max(1, ataqueBase - objetivo.defensaBase);
		return arma.calcularDanio(this, objetivo);
	}
	
	public void atacarCon(Personaje objetivo) {
		int danio = calcularDanioBasicoContra(objetivo);
		int danioReal = Math.max(1, danio - objetivo.getDefensaBase());
		System.out.printf("  %-16s ataca a %-16s -> %d dmg (HP: %d/%d)%n",
				nombre, objetivo.getNombre(), danioReal,
				Math.max(0, objetivo.getVidaActual() - danioReal), objetivo.getVidaMax());
		objetivo.recibirDanio(danioReal);
	}
	
	public abstract void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos);
	
	public String resumenCombate() {
		//barra aturdimiento visual
		int barras = maxBarraAturdimiento > 0
				? (int)((double) barraAturdimiento / maxBarraAturdimiento * 8) : 0;
		String barra = "[" + "=".repeat(barras) + " ".repeat(8 - barras) + "]";
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(" %18s %16s  HP:%3d/%-3d  MP:%3d/%-3d  %s",
			nombre, "[" + tipoClase + "]", vidaActual, vidaMax,
			recursoActual, recursoMax, barra));
		
		if (!estadosActivos.isEmpty()) {
			sb.append("  (");
			for (Estado est : estadosActivos) {
				sb.append(est.getNombre()).append(":").append(est.getTurnosRestantes()).append("t ");
			}
			sb.append(")");
		}
		return sb.toString();
	}
	
	public String getNombre() { return nombre;}
	public TipoClase getTipoClase() { return tipoClase;}
	public int getVidaActual() { return vidaActual;}
	public int getVidaMax() { return vidaMax; }
    public int getAtaqueBase() { return ataqueBase; }
    public int getPoderMagico() { return poderMagico; }
    public int getDefensaBase() { return defensaBase; }
    public int getRecursoActual() { return recursoActual; }
    public Arma getArma() { return arma; }
    public List<Hechizo> getHechizos() { return hechizos; }
    public Map<String, Integer> getCooldowns() { return cooldowns; }
}
