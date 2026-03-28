package Clases;
import Armas.CatalogoArmas;
import Hechizos.Hechizo;
import Hechizos.LuzCelestial;
import Hechizos.VeloDeIlusion;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

public class Ilusionista extends Personaje {
    public Ilusionista(String nombre) {
        super(nombre, TipoClase.ILUSIONISTA, 135, 110, 10, 25, 8, 95);
        this.arma = CatalogoArmas.VARITA_MONOCO.getArma();
        this.hechizos.add(new VeloDeIlusion());
        this.hechizos.add(new LuzCelestial());
    }

    @Override
    public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
        Personaje masDebil = null;
        int menorVida = Integer.MAX_VALUE;
        for (Personaje p : aliados) {
            if (p.estaVivo() && p.getVidaActual() < menorVida) {
                menorVida = p.getVidaActual();
                masDebil = p;
            }
        }
        if (masDebil != null && masDebil.getVidaActual() < (int)(masDebil.getVidaMax() * 0.70)) {
            for (Hechizo h : hechizos) {
                if (h.getTipoObjetivo() == Hechizo.TipoObjetivo.ALIADO_UNICO && h.puedeLanzarse(this)) {
                    h.lanzar(this, masDebil); return;
                }
            }
        }
        for (Personaje p : enemigos) { if (p.estaVivo()) { atacarCon(p); return; } }
    }
}
