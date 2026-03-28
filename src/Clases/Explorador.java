package Clases;
import Armas.CatalogoArmas;
import Hechizos.FlechaEnvenenada;
import Hechizos.Hechizo;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

public class Explorador extends Personaje {
    public Explorador(String nombre) {
        super(nombre, TipoClase.EXPLORADOR, 155, 90, 20, 10, 8, 85);
        this.arma = CatalogoArmas.ARCO_LARGO_EXPEDICION.getArma();
        this.hechizos.add(new FlechaEnvenenada());
    }

    @Override
    public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
        Personaje objetivo = null;
        int mayorVida = -1;
        for (Personaje p : enemigos) {
            if (p.estaVivo() && p.getVidaActual() > mayorVida) {
                mayorVida = p.getVidaActual();
                objetivo = p;
            }
        }
        if (objetivo == null) return;
        for (Hechizo h : hechizos) {
            if (h.puedeLanzarse(this)) { h.lanzar(this, objetivo); return; }
        }
        atacarCon(objetivo);
    }
}