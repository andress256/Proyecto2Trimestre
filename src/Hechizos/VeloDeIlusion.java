package Hechizos;
import Estados.Renovar;
import Personajes.Personaje;
public class VeloDeIlusion extends Hechizo {
    public VeloDeIlusion() { super("Velo de Ilusion", 20, TipoObjetivo.ALIADO_UNICO, "Escudo curativo 2 turnos", 2, 0); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Velo de Ilusion -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.aplicarEstado(new Renovar(2, 15 + caster.getPoderMagico() / 2));
        aplicarCooldown(caster);
    }
}
