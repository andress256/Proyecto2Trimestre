package Clases;
import Armas.CatalogoArmas;
import Estados.Quemadura;
import Hechizos.Hechizo;
import Hechizos.ImpactoCalcinante;
import Hechizos.MaldicionDeLaPintora;
import Hechizos.OleadaOscura;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

public class MagoOscuro extends Personaje {
    public MagoOscuro(String nombre) {
        super(nombre, TipoClase.MAGO_OSCURO, 170, 200, 10, 45, 6, 130);
        this.arma = CatalogoArmas.CETRO_PINTORA.getArma();
        this.hechizos.add(new MaldicionDeLaPintora());
        this.hechizos.add(new ImpactoCalcinante());
        this.hechizos.add(new OleadaOscura());
    }

    @Override
    public void elegirAccionIA(List<Personaje> aliados, List<Personaje> enemigos) {
        Personaje objetivo = null;
        int menorVida = Integer.MAX_VALUE;
        for (Personaje p : enemigos) {
            if (p.estaVivo() && p.getVidaActual() < menorVida) {
                menorVida = p.getVidaActual();
                objetivo = p;
            }
        }
        if (objetivo == null) return;

        for (Hechizo h : hechizos) {
            if (!h.puedeLanzarse(this)) continue;
            if (h.getTipoObjetivo() == Hechizo.TipoObjetivo.TODOS_ENEMIGOS) {
                System.out.println("  [Hechizo] " + nombre + " desata OLEADA OSCURA sobre todos los enemigos!");
                gastarRecurso(h.getCosteRecurso());
                getCooldowns().put(h.getNombre(), 4);
                for (Personaje enemigo : enemigos) {
                    if (enemigo.estaVivo()) {
                        enemigo.recibirDañoPuro(15 + getPoderMagico() / 3);
                        enemigo.aplicarEstado(new Quemadura(2, 8));
                    }
                }
                return;
            } else {
                h.lanzar(this, objetivo);
                return;
            }
        }
        atacarCon(objetivo);
    }
}