package Deportes;
import java.util.ArrayList;
import java.util.List;

public class Deporte {
	
	private String nombreDeporte;
	private int minJugadores;
	private List<String> posiciones;
	private boolean permiteEmpate;
	private int puntosVictoria;
	private int puntosDerrota;
	private int puntosEmpate;
	
	public Deporte(String nombreDeporte, int minJugadores,List<String> posiciones ,boolean permiteEmpate, int puntosVictoria, int puntosDerrota,
			int puntosEmpate) {
		
		this.nombreDeporte = nombreDeporte;
		this.minJugadores = minJugadores;
		this.permiteEmpate = permiteEmpate;
		this.puntosVictoria = puntosVictoria;
		this.puntosDerrota = puntosDerrota;
		this.puntosEmpate = puntosEmpate;

		this.posiciones = new ArrayList<>();
		if (posiciones != null) {
			this.posiciones.addAll(posiciones);
		}
	}
	public String getNombreDeporte() {
		return nombreDeporte;
	}
	public void setNombreDeporte(String nombreDeporte) {
		this.nombreDeporte = nombreDeporte;
	}
	public int getMinJugadores() {
		return minJugadores;
	}
	public void setMinJugadores(int minJugadores) {
		this.minJugadores = minJugadores;
	}
	public List<String> getPosiciones() {
		return posiciones;
	}	
	public void agregarPosiciones(String posicion) {
		this.posiciones.add(posicion);
	}
	public boolean isPermiteEmpate() {
		return permiteEmpate;
	}
	public void setPermiteEmpate(boolean permiteEmpate) {
		this.permiteEmpate = permiteEmpate;
	}
	public int getPuntosVictoria() {
		return puntosVictoria;
	}
	public void setPuntosVictoria(int puntosVictoria) {
		this.puntosVictoria = puntosVictoria;
	}
	public int getPuntosDerrota() {
		return puntosDerrota;
	}
	public void setPuntosDerrota(int puntosDerrota) {
		this.puntosDerrota = puntosDerrota;
	}
	public int getPuntosEmpate() {
		return puntosEmpate;
	}
	public void setPuntosEmpate(int puntosEmpate) {
		this.puntosEmpate = puntosEmpate;
	}
	
	public boolean esIndividual() {
		return this.minJugadores <= 1;
	}
	
	
}
