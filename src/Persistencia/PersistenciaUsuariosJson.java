package Persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.ClubDeportivoSeneca;
import Usuarios.Usuario;

public class PersistenciaUsuariosJson {

    private static final String ID_USUARIO = "idUsuario";
    private static final String NOMBRE = "nombre";
    private static final String APELLIDO = "apellido";
    private static final String LOGIN = "login";
    private static final String CONTRASENA = "contrasena";
    private static final String PUNTOS_FIDELIDAD = "puntosFidelidad";
    private static final String TIPO_USUARIO = "tipoUsuario";
    private static final String EDAD = "edad";
    private static final String ACTIVO = "activo";
    private static final String CODIGO_DESCUENTO = "codigoDescuento";

    public void cargarUsuarios(ClubDeportivoSeneca club, String archivo) throws IOException {
        String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONArray jUsuarios = new JSONArray(jsonCompleto);

        int numUsuarios = jUsuarios.length();
        for (int i = 0; i < numUsuarios; i++) {
            JSONObject jUsuario = jUsuarios.getJSONObject(i);
            String tipo = jUsuario.optString(TIPO_USUARIO, "Socio");
            String id = jUsuario.getString(ID_USUARIO);
            String nombre = jUsuario.getString(NOMBRE);
            String apellido = jUsuario.getString(APELLIDO);
            String login = jUsuario.getString(LOGIN);
            String contrasena = jUsuario.getString(CONTRASENA);
            int puntos = jUsuario.getInt(PUNTOS_FIDELIDAD);

            if ("Socio".equalsIgnoreCase(tipo)) {
                int edad = jUsuario.getInt(EDAD);
                boolean activo = jUsuario.getBoolean(ACTIVO);
                club.crearSocio(id, nombre, apellido, login, contrasena, puntos, edad, activo);
            } else if ("Entrenador".equalsIgnoreCase(tipo)) {
                String codigo = jUsuario.optString(CODIGO_DESCUENTO, "");
                club.crearEntrenador(id, nombre, apellido, login, contrasena, puntos, codigo);
            } else if ("Fisioterapeuta".equalsIgnoreCase(tipo)) {
                String codigo = jUsuario.optString(CODIGO_DESCUENTO, "");
                club.crearFisioterapeuta(id, nombre, apellido, login, contrasena, puntos, codigo);
            }
        }
    }

    public void salvarUsuarios(ClubDeportivoSeneca club, String archivo) throws IOException {
        JSONArray jUsuarios = new JSONArray();

        if (club != null && club.getUsuarios() != null) {
            for (Usuario usuario : club.getUsuarios().values()) {
                JSONObject jUsuario = new JSONObject();
                jUsuario.put(ID_USUARIO, usuario.getIdUsuario());
                jUsuario.put(NOMBRE, usuario.getNombre());
                jUsuario.put(APELLIDO, usuario.getApellido());
                jUsuario.put(LOGIN, usuario.getLogin());
                jUsuario.put(CONTRASENA, usuario.getContraseña());
                jUsuario.put(PUNTOS_FIDELIDAD, usuario.getPuntosFidelidad());
                jUsuarios.put(jUsuario);
            }
        }

        PrintWriter pw = new PrintWriter(archivo);
        jUsuarios.write(pw, 2, 0);
        pw.close();
    }
}