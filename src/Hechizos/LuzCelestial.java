package Hechizos;
import Personajes.Personaje;
public class LuzCelestial extends Hechizo {
    public LuzCelestial() { super("Luz Celestial", 30, TipoObjetivo.ALIADO_UNICO, "Curacion directa", 1, 40); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Luz Celestial -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.curar(potenciaBase + caster.getPoderMagico());
        aplicarCooldown(caster);
    }
}
