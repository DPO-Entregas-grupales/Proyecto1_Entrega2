package Persistencia;

public class CentralPersistencia {


    public static final String JSON = "JSON";

    public static PersistenciaClubDeportivoJson getPersistenciaClubDeportivo() {
        return new PersistenciaClubDeportivoJson();
    }

    public static PersistenciaTiendaJson getPersistenciaTienda() {
        return new PersistenciaTiendaJson();
    }
}