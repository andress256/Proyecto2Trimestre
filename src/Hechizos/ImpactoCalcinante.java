package Hechizos;
import Estados.Quemadura;
import Personajes.Personaje;
public class ImpactoCalcinante extends Hechizo {
    public ImpactoCalcinante() { super("Impacto Calcinante", 35, TipoObjetivo.ENEMIGO_UNICO, "Dano + quemadura", 3, 20); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Impacto Calcinante -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.recibirDañoPuro(potenciaBase + caster.getPoderMagico() / 2);
        objetivo.aplicarEstado(new Quemadura(3, 10 + caster.getPoderMagico() / 4));
        aplicarCooldown(caster);
    }
}
