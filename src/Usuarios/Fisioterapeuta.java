package Usuarios;

public class Fisioterapeuta extends Empleado {
	static String TIPO="Fisioterapeuta";
	public Fisioterapeuta(String idUsuario, String nombre, String apellido, String login, String contraseña,
			int puntosFidelidad, String codigoDescuento) {
		super(idUsuario, nombre, apellido, login, contraseña, puntosFidelidad, codigoDescuento);
	}

}
