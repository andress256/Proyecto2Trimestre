package Personajes;

import java.util.ArrayList;
import java.util.Map;


public abstract class Personaje {


	    // --- 1. IDENTIDAD ---
	    protected String nombre;      
	    protected String tipoClase;   

	    // --- 2. VIDA  ---
	    protected int vidaMax;        
	    protected int vidaActual;     

	    // --- 3. RECURSO (Energía/Maná) ---
	    protected int recursoMax;     
	    protected int recursoActual;  

	    // --- 4. ESTADÍSTICAS BASE  ---
	    protected int ataqueBase;     // Poder físico para armas 
	    protected int poderMagico;    // Potencia para hechizos (Luz/Pintura) 
	    protected int defensaBase;    // Reducción de daño recibido 
	    protected int precision;      // Probabilidad de acierto (opcional) 

	    // --- 5. EQUIPAMIENTO ---
	    //protected Armas arma;          // Referencia obligatoria al objeto Arma 

	    // --- 6. COLECCIONES ASOCIADAS ---
	    //protected ArrayList<Estados> estadosActivos; // Lista de DoTs/HoTs activos 
	    //protected ArrayList<Hechizos> hechizos;      // Lista de habilidades disponibles 
	    protected Map<String, Integer> cooldowns;   // Mapa para turnos de reutilización 

	    // --- 7. ESTADO DE COMBATE ---
	    protected boolean vivo;       // Indica si sigue en pie (vidaActual > 0) 
	    protected boolean defendiendo;// Indica si está reduciendo daño este turno 
	    
	    
	    
	    public Personaje(String nombre, String tipoClase, int vidaMax, int vidaActual, int recursoMax, int recursoActual, int ataqueBase, int poderMagico, int defensaBase, int precision) {
	    	this.nombre = nombre;
	    	this.tipoClase = tipoClase;
	    	this.vidaMax = vidaMax;
	    	this.vidaActual = vidaActual;
	    	this.recursoMax = recursoMax;
	    	this.recursoActual = recursoActual;
	    	this.ataqueBase = ataqueBase;
	    	this.poderMagico = poderMagico;
	    	this.defensaBase = defensaBase;
	    	this.precision = precision;
	    }
	    
	    
	}

