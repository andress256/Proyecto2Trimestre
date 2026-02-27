package Personajes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Armas.Arma;
import Clases.TipoClase;
import Estados.Estado;
import Hechizos.Hechizo;

public abstract class Personaje {
    
   
    protected String nombre;
    protected TipoClase tipoClase;
    
    // Estadísticas vitales de la Expedición
    protected int vidaMax;
    protected int vidaActual;
    protected int recursoMax; 
    protected int recursoActual;
    
    // Estadísticas de combate
    protected int ataqueBase;
    protected int poderMagico; 
    protected int defensaBase;
    
    protected Arma armaEquipada; 
    
    // Listas 
    protected List<Estado> estadosActivos;
    protected List<Hechizo> hechizos;
    protected Map<String, Integer> cooldowns;
    
    protected boolean vivo;
    protected boolean defendiendo;

    
    public Personaje(String nombre2, String tipoClase2, int vidaMax2, int vidaActual2, int recursoMax2, int recursoActual2, int ataqueBase2, int poderMagico2, int defensaBase2, int precision) {
        this.nombre = "Expedicionario Desconocido";
        this.vidaMax = 100;
        this.vidaActual = 100;
        this.recursoMax = 0;
        this.recursoActual = 0;
        this.ataqueBase = 10;
        this.defensaBase = 10;
        this.poderMagico = 10;
        
        // Inicializamos para evitar NullPointerException
        this.estadosActivos = new ArrayList<>();
        this.hechizos = new ArrayList<>();
        this.cooldowns = new HashMap<>();
        this.vivo = true;
        this.defendiendo = false;
    }

    // Constructor con parámetros
    public Personaje(String nombre, TipoClase tipoClase, int vidaMax, int recursoMax, int ataqueBase, int defensaBase, int poderMagico) {
        this.nombre = nombre;
        this.tipoClase = tipoClase;
        this.vidaMax = vidaMax;
        this.vidaActual = vidaMax; 
        this.recursoMax = recursoMax;
        this.recursoActual = recursoMax; 
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.poderMagico = poderMagico;
        
        this.estadosActivos = new ArrayList<>();
        this.hechizos = new ArrayList<>();
        this.cooldowns = new HashMap<>();
        this.vivo = true;
        this.defendiendo = false;
    }

    public boolean estaVivo() {
        return this.vidaActual > 0;
    }

    public void recibirDanio(int cantidad) {
        int danioFinal = Math.max(0, cantidad - this.defensaBase); 
        this.vidaActual -= danioFinal;
        
        if (this.vidaActual <= 0) {
            this.vidaActual = 0;
            this.vivo = false; 
        }
    }

    public void curar(int cantidad) {
        if (estaVivo()) {
            this.vidaActual += cantidad;
            if (this.vidaActual > this.vidaMax) {
                this.vidaActual = this.vidaMax;
            }
        }
    }

    public boolean gastarRecurso(int coste) {
        if (this.recursoActual >= coste) {
            this.recursoActual -= coste;
            return true;
        }
        return false;
    }

    public void equiparArma(Arma nuevaArma) {
        this.armaEquipada = nuevaArma;
    }

    public void aplicarEstado(Estado nuevoEstado) {
        for (Estado e : this.estadosActivos) {
            if (e.getNombre().equals(nuevoEstado.getNombre())) {
                e.setTurnosRestantes(nuevoEstado.getTurnosRestantes());
                return;
            }
        }
        this.estadosActivos.add(nuevoEstado);
    }

    public void procesarEstados() {
        if (!estaVivo()) return;

        this.estadosActivos.removeIf(estado -> {
            estado.alProcesarTurno(this);
            estado.reducirDuracion();
            return estado.getTurnosRestantes().equals(0);
        });
    }

    // Adaptado con la estética visual del juego
    public String pedirInfo() {
        String nombreArma = (this.armaEquipada != null) ? this.armaEquipada.getNombre() : "Sin arma";
        return "[Expedicionario] " + this.nombre + " (" + this.tipoClase + ")\n" +
               "\t HP: " + this.vidaActual + "/" + this.vidaMax + "\n" +
               "\t Lúmen: " + this.recursoActual + "/" + this.recursoMax + "\n" +
               "\t Arma: " + nombreArma + "\n" +
               "\t Estados (Trazos): " + this.estadosActivos.size();
    }

    // --- Getters y Setters ---
    public String getNombre() { return this.nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public TipoClase getTipoClase() { return this.tipoClase; }
    public void setTipoClase(TipoClase tipoClase) { this.tipoClase = tipoClase; }
    
    public Arma getArma() { return this.armaEquipada; }
    
    public List<Hechizo> getHechizos() { return this.hechizos; }
    public void setHechizos(List<Hechizo> hechizos) { this.hechizos = hechizos; }
    
    public List<Estado> getEstadosActivos() { return this.estadosActivos; }
    
    public void setDefendiendo(boolean defendiendo) { this.defendiendo = defendiendo; }
    public boolean isDefendiendo() { return this.defendiendo; }
}
