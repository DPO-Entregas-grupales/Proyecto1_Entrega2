package Deportes;

public class InscripcionCompetencia {
	
	private int puntosAcumulados;
	private String posicion;
	private Equipo equipo;
	private Competencia competencia;
	public InscripcionCompetencia(int puntosAcumulados, String posicion, Equipo equipo, Competencia competencia) {
		this.puntosAcumulados = puntosAcumulados;
		this.posicion = posicion;
		this.equipo = equipo;
		this.competencia = competencia;
	}
	public int getPuntosAcumulados() {
		return puntosAcumulados;
	}
	public void setPuntosAcumulados(int puntosAcumulados) {
		this.puntosAcumulados = puntosAcumulados;
	}
	public String getPosicion() {
		return posicion;
	}
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	public Equipo getEquipo() {
		return equipo;
	}
	public Competencia getCompetencia() {
		return competencia;
	}
	
	

}
