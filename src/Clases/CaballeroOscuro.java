package Clases;
import Armas.CatalogoArmas;
import Personajes.Personaje;
import Personajes.TipoClase;
import java.util.List;

// Villano tanque. Su armadura cromatica absorbe 1 de cada 3 golpes.
public class CaballeroOscuro extends Personaje {
    private int contadorGolpesRecibidos = 0;

    public CaballeroOscuro(String nombre) {
        super(nombre, TipoClase.CABALLERO_OSCURO, 250, 0, 35, 0, 18, 150);
        this.arma = CatalogoArmas.GRAN_MAZA_RENOIR.getArma();
    }

    // Absorbe 1 de cada 3 golpes fisicos de hechizo
    @Override
    public void recibirDaño(int cantidad) {
        contadorGolpesRecibidos++;
        if (contadorGolpesRecibidos % 3 == 0) {
            System.out.println("   [!] La armadura cromatica de " + nombre + " ABSORBE el golpe!");
            return;
        }
        super.recibirDaño(cantidad);
    }

    // Absorbe 1 de cada 3 golpes de arma
    @Override
    public void recibirDañoDeArma(int cantidad) {
        contadorGolpesRecibidos++;
        if (contadorGolpesRecibidos % 3 == 0) {
            System.out.println("   [!] La armadura cromatica de " + nombre + " ABSORBE el golpe!");
            return;
        }
        super.recibirDañoDeArma(cantidad);
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
        if (objetivo != null) atacarCon(objetivo);
    }
}

