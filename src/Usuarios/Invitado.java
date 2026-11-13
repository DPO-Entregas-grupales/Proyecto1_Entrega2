package Usuarios;

public class Invitado extends Usuario {
    private static String TIPO = "INVITADO";
    private int edad;
    public Invitado(String idUsuario, String nombre, String apellido, String login, String contraseña, int puntosFidelidad, int edad) {
        super(idUsuario, nombre, apellido, login, contraseña, puntosFidelidad);
        this.edad = edad;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
}
