package Clases;
import Armas.CatalogoArmas;
import Hechizos.FlechaEnvenenada;
import Hechizos.Hechizo;
import Hechizos.SombraCromatica;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

// Enemigo arquero. Aplica veneno y sombra cromatica a distancia.
public class GuardianPintado extends Personaje {
    public GuardianPintado(String nombre) {
        super(nombre, TipoClase.GUARDIAN_PINTADO, 140, 70, 18, 10, 8, 90);
        this.arma = CatalogoArmas.BALLESTA_ABISMO.getArma();
        this.hechizos.add(new FlechaEnvenenada());
        this.hechizos.add(new SombraCromatica());
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
