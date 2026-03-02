package Estados;

import Estados.Estado.TipoEstado;
import Personajes.Personaje;

public abstract class Estado {

	public enum TipoEstado { DOT, HOT, MODIFICADOR, CONTROL}
	
	protected String nombre;
	protected int turnosRestantes;
	protected int potenciaPorTurno;
	protected TipoEstado tipo;
	
	public Estado(String nombre, int turnosRestantes, int potenciaPorTurno, TipoEstado tipo) {
		this.nombre = nombre;
		this.turnosRestantes = turnosRestantes;
		this.potenciaPorTurno = potenciaPorTurno;
		this.tipo = tipo;
	}
	
	public abstract void alAplicar(Personaje objetivo);
	public abstract void alProcesarTurno(Personaje objetivo);
	public abstract void alExpirar(Personaje objetivo);
	
	public void reducirDuracion() {
		turnosRestantes--;
	}
	
	public String getNombre() { return nombre; }
    public int getTurnosRestantes() { return turnosRestantes; }
    public int getPotenciaPorTurno() { return potenciaPorTurno; }
    public TipoEstado getTipo() { return tipo; }
	
}
