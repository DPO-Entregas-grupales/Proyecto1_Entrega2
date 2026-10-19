package Usuarios;

import java.util.HashMap;
import java.util.Map;
import Turnos.Turno;

public abstract class Empleado extends Usuario {
	
	private String codigoDescuento;
	private Map<String, Turno> turnos;
	
	public Empleado(String idUsuario, String nombre, String apellido, String login, String contraseña,
			int puntosFidelidad, String codigoDescuento) {
		super(idUsuario, nombre, apellido, login, contraseña, puntosFidelidad);
		this.codigoDescuento = codigoDescuento;
		this.turnos = new HashMap<>();
	}

	public String getCodigoDescuento() {
		return codigoDescuento;
	}
	
	public void agregarTurno(Turno turno) {
		if(turno != null) {
			this.turnos.put(turno.getIdTurno(), turno);
		}
	}
	public void eliminarTurno(String id) {
		this.turnos.remove(id);
	}
	
}
