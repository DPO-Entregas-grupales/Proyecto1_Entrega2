package Turnos;

import java.util.ArrayList;
import java.util.List;
import Usuarios.Empleado;
import Usuarios.Entrenador;
import Usuarios.Fisioterapeuta;

public class Turno {
	
	private String idTurno;
	private int diaSemana;
	private boolean esDiurno;
	private List<Entrenador> entrenadores;
	private List<Fisioterapeuta> fisioterapeutas;


	public Turno(String idTurno, int diaSemana, boolean esDiurno) {
		super();
		this.idTurno = idTurno;
		this.diaSemana = diaSemana;
		this.esDiurno = esDiurno;
		this.entrenadores = new ArrayList<>();
		this.fisioterapeutas = new ArrayList<>();
	}
	public void agregarEmpleado(Empleado empleado) {
		if(empleado != null) {
			if(empleado instanceof Entrenador) {
				this.entrenadores.add((Entrenador) empleado);
			}
			if(empleado instanceof Fisioterapeuta) {
				this.fisioterapeutas.add((Fisioterapeuta) empleado);
			}
				
		}
	}
	
	public void quitarEmpleado(Empleado empleado) {
		if(empleado != null) {
			if(empleado instanceof Entrenador) {
				this.entrenadores.remove(empleado);
			}
			if(empleado instanceof Fisioterapeuta) {
				this.fisioterapeutas.remove(empleado);
			}
		}
	}
	
	public boolean cumpleFisioterapeutaEntrenadores() {
		int cantidadFisioterapeutas = fisioterapeutas.size();
		int cantidadEntrenadores = entrenadores.size();
		return cantidadFisioterapeutas >=1 && cantidadEntrenadores >= 2;
	}
	
	public String getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(String idTurno) {
		this.idTurno = idTurno;
	}
	public int getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(int diaSemana) {
		this.diaSemana = diaSemana;
	}
	public boolean isEsDiurno() {
		return esDiurno;
	}
	public void setEsDiurno(boolean esDiurno) {
		this.esDiurno = esDiurno;
	}
	public List<Entrenador> getEntrenadores() {
		return entrenadores;
	}
	public List<Fisioterapeuta> getFisioterapeutas() {
		return fisioterapeutas;
	}
}
