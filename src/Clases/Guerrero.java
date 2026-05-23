package Clases;
import Armas.CatalogoArmas;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

// Heroe tanque: alta vida y defensa. Reduce el daño recibido un 10%.
// Ataca al enemigo con menos vida para rematar.
public class Guerrero extends Personaje {
    public Guerrero(String nombre) {
        super(nombre, TipoClase.GUERRERO, 200, 0, 28, 0, 14, 120);
        this.arma = CatalogoArmas.ESPADON_GUSTAVE.getArma();
    }

    // Reduce un 10% el daño fisico de hechizos (donde la defensa se resta en recibirDaño)
    @Override
    public void recibirDaño(int cantidad) {
        super.recibirDaño((int)(cantidad * 0.90));
    }

    // Reduce un 10% el daño de ataques con arma (donde la defensa ya se calculo en el arma)
    @Override
    public void recibirDañoDeArma(int cantidad) {
        super.recibirDañoDeArma((int)(cantidad * 0.90));
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
        if (objetivo != null) atacarCon(objetivo);
    }
}