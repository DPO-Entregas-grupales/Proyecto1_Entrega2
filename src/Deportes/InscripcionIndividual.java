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
	
	public InscripcionIndividual(Socio socio, Deporte deporteIndividual, String nivel, double puntajeRanking) {
		super();
		this.socio = socio;
		this.deporteIndividual = deporteIndividual;
		this.nivel = nivel;
		this.puntajeRanking = puntajeRanking;
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
