package Estadisticas;

import Usuarios.Socio;

import Partidos.Partido;

public abstract class Estadistica {
	
	private String idEstadistica;
	private Socio socio;
	private Partido partido;
	public Estadistica(String idEstadistica, Socio socio, Partido partido) {
		super();
		this.idEstadistica = idEstadistica;
		this.socio = socio;
		this.partido = partido;
	}
	public String getIdEstadistica() {
		return idEstadistica;
	}
	public Socio getSocio() {
		return socio;
	}
	public Partido getPartido() {
		return partido;
	}
	
}
