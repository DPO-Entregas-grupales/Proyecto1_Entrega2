package Deportes;

import Usuarios.Socio;

public class InscripcionIndividual {
	private Socio socio;
	private Deporte deporteIndividual;
	static String PRINCIPIANTE = "Principiante";
	static String INTERMEDIO = "Intermedio";
	static String AVANZADO = "Avanzado";
	private String nivel;
	private double puntajeRanking;
	public static final double PUNTOS_INTERMEDIO = 4;
	public static final double PUNTOS_AVANZADO = 10;
	
	public InscripcionIndividual(Socio socio, Deporte deporteIndividual, String nivel, double puntajeRanking) {
		super();
		this.socio = socio;
		this.deporteIndividual = deporteIndividual;
		this.nivel = nivel;
		this.puntajeRanking = puntajeRanking;
	}
	
	public void sumarPuntosRanking(double puntos) {
		this.puntajeRanking += puntos;
		actualizarNivel();
	}
	
	public void actualizarNivel() {
		if(this.puntajeRanking >= PUNTOS_AVANZADO) {
			this.nivel = "Avanzado";
		} else if(this.puntajeRanking >= PUNTOS_INTERMEDIO) {
			this.nivel = "Intermedio";
		} else {
			this.nivel = "Principiante";
		}
	}
	
	// 0 = Principiante, 1 = Intermedio y 2 = Avanzado.
	public int posicionNivel() {
		if("Avanzado".equals(this.nivel)) {
			return 2;
		}
		if("Intermedio".equals(this.nivel)) {
			return 1;
		}
		return 0;
	}
	
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public double getPuntajeRanking() {
		return puntajeRanking;
	}
	public void setPuntajeRanking(double puntajeRanking) {
		this.puntajeRanking = puntajeRanking;
	}
	public Socio getSocio() {
		return socio;
	}
	public Deporte getDeporteIndividual() {
		return deporteIndividual;
	}
}
