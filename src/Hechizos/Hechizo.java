package Hechizos;

import Personajes.Personaje;

// Clase base abstracta para todos los hechizos del juego.
// Cada hechizo tiene un coste de recurso, un cooldown y un tipo de objetivo.
public abstract class Hechizo {

	// Define a quien puede ir dirigido el hechizo
	public enum TipoObjetivo { ENEMIGO_UNICO, ALIADO_UNICO, PROPIO, TODOS_ENEMIGOS }

	protected String nombre;
	protected int costeRecurso;
	protected TipoObjetivo tipoObjetivo;
	protected String descripcion;
	protected int cooldownMax;   // turnos de espera tras usarlo
	protected int potenciaBase;  // dano o curacion base del hechizo

	public Hechizo(String nombre, int costeRecurso, TipoObjetivo tipoObjetivo,
			String descripcion, int cooldownMax, int potenciaBase) {
		this.nombre = nombre;
		this.costeRecurso = costeRecurso;
		this.tipoObjetivo = tipoObjetivo;
		this.descripcion = descripcion;
		this.cooldownMax = cooldownMax;
		this.potenciaBase = potenciaBase;
	}

	// Comprueba si el hechizo puede lanzarse: recurso suficiente y sin cooldown activo
	public boolean puedeLanzarse(Personaje caster) {
		if (caster.getRecursoActual() < costeRecurso) return false;
		if (caster.getCooldowns().containsKey(nombre)) return false;
		return true;
	}

	// Cada subclase define el efecto concreto del hechizo
	public abstract void lanzar(Personaje caster, Personaje objetivo);

	// Pone el hechizo en cooldown tras usarlo
	protected void aplicarCooldown(Personaje caster) {
		if (cooldownMax > 0) caster.getCooldowns().put(nombre, cooldownMax);
	}

	public String getNombre() { return nombre; }
	public int getCosteRecurso() { return costeRecurso; }
	public TipoObjetivo getTipoObjetivo() { return tipoObjetivo; }
	public String getDescripcion() { return descripcion; }
}
