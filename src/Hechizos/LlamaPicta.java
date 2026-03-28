package Hechizos;
import Personajes.Personaje;
public class LlamaPicta extends Hechizo {
    public LlamaPicta() { super("Llama Picta", 25, TipoObjetivo.ENEMIGO_UNICO, "Dano magico directo", 2, 35); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Llama Picta -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.recibirDañoPuro(potenciaBase + caster.getPoderMagico());
        aplicarCooldown(caster);
    }
}

