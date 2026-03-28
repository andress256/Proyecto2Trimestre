package Estados;

import Personajes.Personaje;

// Clase base abstracta para todos los estados que puede tener un personaje.
// Un estado se aplica, se procesa cada turno y expira cuando sus turnos llegan a 0.
public abstract class Estado {

	// DOT: daño por turno | HOT: curacion por turno | MODIFICADOR: cambia estadisticas | CONTROL: impide actuar
	public enum TipoEstado { DOT, HOT, MODIFICADOR, CONTROL }

	protected String nombre;
	protected int turnosRestantes;
	protected int potenciaPorTurno;  // daño o curacion por turno segun el tipo
	protected TipoEstado tipo;

	public Estado(String nombre, int turnosRestantes, int potenciaPorTurno, TipoEstado tipo) {
		this.nombre = nombre;
		this.turnosRestantes = turnosRestantes;
		this.potenciaPorTurno = potenciaPorTurno;
		this.tipo = tipo;
	}

	// Se ejecuta al aplicar el estado por primera vez
	public abstract void alAplicar(Personaje objetivo);

	// Se ejecuta al final de cada ronda mientras el estado siga activo
	public abstract void alProcesarTurno(Personaje objetivo);

	// Se ejecuta cuando el estado llega a 0 turnos y desaparece
	public abstract void alExpirar(Personaje objetivo);

	// Reduce en 1 los turnos restantes
	public void reducirDuracion() {
		turnosRestantes--;
	}

	public String getNombre() { return nombre; }
	public int getTurnosRestantes() { return turnosRestantes; }
	public int getPotenciaPorTurno() { return potenciaPorTurno; }
	public TipoEstado getTipo() { return tipo; }
}
