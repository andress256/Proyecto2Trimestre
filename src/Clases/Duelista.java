package Clases;
import Armas.CatalogoArmas;
import Hechizos.Hechizo;
import Hechizos.SombraCromatica;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

// Heroe rapido con altos criticos. Usa Sombra Cromatica para envenenar.
public class Duelista extends Personaje {
    public Duelista(String nombre) {
        super(nombre, TipoClase.DUELISTA, 150, 80, 24, 12, 7, 90);
        equiparArma (CatalogoArmas.ESTOQUE_MAELLE.getArma());
        this.hechizos.add(new SombraCromatica());
    }

    @Override
    public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
        Personaje objetivo = primerEnemigo(enemigos);
        if (objetivo == null) return;
        for (Hechizo h : hechizos) {
            if (h.puedeLanzarse(this)) { h.lanzar(this, objetivo); return; }
        }
        atacarCon(objetivo);
    }

    private Personaje primerEnemigo(List<Personaje> enemigos) {
        for (Personaje p : enemigos) { if (p.estaVivo()) return p; }
        return null;
    }
}
