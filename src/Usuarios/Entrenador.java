package Usuarios;
import java.util.HashMap;
import java.util.Map;

import SesionEntrenamiento.SesionEntrenamiento;

public class Entrenador extends Empleado {
	static String TIPO = "Entrenador";
	private Map<String, SesionEntrenamiento> sesionesEntrenamiento;
	public Entrenador(String idUsuario, String nombre, String apellido, String login, String contraseña,
			int puntosFidelidad, String codigoDescuento) {
		super(idUsuario, nombre, apellido, login, contraseña, puntosFidelidad, codigoDescuento);
		
		this.sesionesEntrenamiento = new HashMap<>();
	}
	
	public Map<String, SesionEntrenamiento> getSesionesEntrenamiento() {
		return sesionesEntrenamiento;
	}

	public void agregarSesionEntrenamiento(SesionEntrenamiento sesion) {
		if(sesion != null) {
			this.sesionesEntrenamiento.put(sesion.getIdEntrenamiento(), sesion);
		}
	}
}
