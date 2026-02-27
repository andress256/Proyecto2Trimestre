package Clases;

import Personajes.Personaje;

public class Guerrero extends Personaje {

    // Constructor vacío por defecto 
    public Guerrero(String string, TipoClase guerrero, int i, int j, int k, int l, int m) {
        super("Gus (Vanguardia)", TipoClase.GUERRERO, 150, 0, 30, 15, 0);
    }

    // Constructor con parámetros que llama a super
    public Guerrero(String nombre, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico) {
        super(nombre, TipoClase.GUERRERO, vidaMax, recursoMax, ataqueBase, defensaBase, poderMagico);
    }

    @Override
    public void recibirDanio(int cantidad) {
        if (this.defendiendo) {
        
            super.recibirDanio(cantidad / 2);
        } else {
            super.recibirDanio(cantidad);
        }
    }
    
    @Override
    public String pedirInfo() {
        return super.pedirInfo() + "\n\t [Pasiva Guerrero]: Gran resistencia física.";
    }
    
    public String datos() {
    	
    	
    }
}
