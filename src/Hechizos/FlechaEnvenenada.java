package Hechizos;
import Estados.Veneno;
import Personajes.Personaje;
public class FlechaEnvenenada extends Hechizo {
    public FlechaEnvenenada() { super("Flecha Envenenada", 15, TipoObjetivo.ENEMIGO_UNICO, "Dano + veneno", 2, 10); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Flecha Envenenada -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.recibirDaño(potenciaBase + caster.getAtaqueBase() / 2);
        objetivo.aplicarEstado(new Veneno(3, 6 + caster.getAtaqueBase() / 5));
        aplicarCooldown(caster);
    }
}
