package Hechizos;
import Estados.Renovar;
import Personajes.Personaje;
public class BrilloRenovador extends Hechizo {
    public BrilloRenovador() { super("Brillo Renovador", 25, TipoObjetivo.ALIADO_UNICO, "Curacion por turnos", 2, 0); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Brillo Renovador -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.aplicarEstado(new Renovar(3, 12 + caster.getPoderMagico() / 3));
        aplicarCooldown(caster);
    }
}

