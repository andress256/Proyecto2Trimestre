package Clases;
import Armas.CatalogoArmas;
import Hechizos.Hechizo;
import Hechizos.ImpactoCalcinante;
import Hechizos.LlamaPicta;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

// Heroe magico con alto poder. Lanza hechizos de daño directo y quemadura.
public class Mago extends Personaje {
    public Mago(String nombre) {
        super(nombre, TipoClase.MAGO, 130, 160, 8, 35, 5, 80);
        equiparArma (CatalogoArmas.BASTON_LUNE.getArma());
        this.hechizos.add(new ImpactoCalcinante());
        this.hechizos.add(new LlamaPicta());
    }

    @Override
    public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
        Personaje objetivo = null;
        for (Personaje p : enemigos) { if (p.estaVivo()) { objetivo = p; break; } }
        if (objetivo == null) return;
        for (Hechizo h : hechizos) {
            if (h.puedeLanzarse(this)) { h.lanzar(this, objetivo); return; }
        }
        atacarCon(objetivo);
    }
}