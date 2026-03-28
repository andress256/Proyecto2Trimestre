package Hechizos;
import Estados.Veneno;
import Personajes.Personaje;
public class SombraCromatica extends Hechizo {
    public SombraCromatica() { super("Sombra Cromatica", 20, TipoObjetivo.ENEMIGO_UNICO, "Aplica veneno", 3, 0); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Sombra Cromatica -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.aplicarEstado(new Veneno(4, 8 + caster.getPoderMagico() / 4));
        aplicarCooldown(caster);
    }
}
