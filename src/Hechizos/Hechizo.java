package Hechizos;

import Personajes.Personaje;

public abstract class Hechizo {

    public enum TipoObjetivo { ENEMIGO_UNICO, ALIADO_UNICO, PROPIO, TODOS_ENEMIGOS }

    protected String nombre;
    protected int costeRecurso;
    protected TipoObjetivo tipoObjetivo;
    protected String descripcion;
    protected int cooldownMax;
    protected int potenciaBase;

    public Hechizo(String nombre, int costeRecurso, TipoObjetivo tipoObjetivo,
                   String descripcion, int cooldownMax, int potenciaBase) {
        this.nombre = nombre;
        this.costeRecurso = costeRecurso;
        this.tipoObjetivo = tipoObjetivo;
        this.descripcion = descripcion;
        this.cooldownMax = cooldownMax;
        this.potenciaBase = potenciaBase;
    }

    public boolean puedeLanzarse(Personaje caster) {
        if (caster.getRecursoActual() < costeRecurso) return false;
        if (caster.getCooldowns().containsKey(nombre)) return false;
        return true;
    }

    public abstract void lanzar(Personaje caster, Personaje objetivo);

    protected void aplicarCooldown(Personaje caster) {
        if (cooldownMax > 0) caster.getCooldowns().put(nombre, cooldownMax);
    }

    public String getNombre() { return nombre; }
    public int getCosteRecurso() { return costeRecurso; }
    public TipoObjetivo getTipoObjetivo() { return tipoObjetivo; }
    public String getDescripcion() { return descripcion; }
}
