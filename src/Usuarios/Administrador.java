package Usuarios;

public class Administrador extends Usuario {
	static String TIPO = "Administrador";
	public Administrador(String idUsuario, String nombre, String apellido, String login, String contraseña,
			int puntosFidelidad) {
		super(idUsuario, nombre, apellido, login, contraseña, puntosFidelidad);
	}

}
