package Hechizos;
import Estados.Quemadura;
import Estados.Veneno;
import Personajes.Personaje;
public class MaldicionDeLaPintora extends Hechizo {
    public MaldicionDeLaPintora() { super("Maldicion de la Pintora", 45, TipoObjetivo.ENEMIGO_UNICO, "Quemadura + Veneno", 4, 10); }
    @Override
    public void lanzar(Personaje caster, Personaje objetivo) {
        System.out.println("  " + caster.getNombre() + " -> Maldicion de la Pintora -> " + objetivo.getNombre());
        caster.gastarRecurso(costeRecurso);
        objetivo.recibirDañoPuro(potenciaBase + caster.getPoderMagico() / 2);
        objetivo.aplicarEstado(new Quemadura(3, 12));
        objetivo.aplicarEstado(new Veneno(3, 8));
        aplicarCooldown(caster);
    }
}
