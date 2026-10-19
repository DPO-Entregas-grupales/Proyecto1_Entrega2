package Persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.ClubDeportivoSeneca;
import Usuarios.Entrenador;
import Usuarios.Socio;
import Deportes.Deporte;
import Deportes.Equipo;
import Deportes.Competencia;
import Deportes.InscripcionIndividual;
import Deportes.InscripcionEquipo;
import Deportes.InscripcionCompetencia;
import ReservasInstalaciones.Instalacion;
import ReservasInstalaciones.Reserva;
import Partidos.Partido;
import Turnos.Turno;
import Turnos.SolicitudCambioTurnos;
import SesionEntrenamiento.SesionEntrenamiento;
import SesionEntrenamiento.AsistenciaEvaluacion;
import Estadisticas.Estadistica;

public class PersistenciaClubDeportivoJson {

    private static final String DEPORTES = "deportes";
    private static final String EQUIPOS = "equipos";
    private static final String COMPETENCIAS = "competencias";
    private static final String INSCRIPCIONES_INDIVIDUAL = "inscripcionesIndividual";
    private static final String INSCRIPCIONES_EQUIPO = "inscripcionesEquipo";
    private static final String INSCRIPCIONES_COMPETENCIA = "inscripcionesCompetencia";
    private static final String INSTALACIONES = "instalaciones";
    private static final String RESERVAS = "reservas";
    private static final String PARTIDOS = "partidos";
    private static final String SESIONES_ENTRENAMIENTO = "sesionesEntrenamiento";
    private static final String ASISTENCIA_EVALUACIONES = "asistenciaEvaluaciones";
    private static final String TURNOS = "turnos";
    private static final String SOLICITUDES_CAMBIO_TURNO = "solicitudesCambioTurno";
    private static final String ESTADISTICAS = "estadisticas";

    private PersistenciaUsuariosJson persistenciaUsuarios = new PersistenciaUsuariosJson();

    public void cargarUsuarios(ClubDeportivoSeneca club, String archivo) throws IOException {
        persistenciaUsuarios.cargarUsuarios(club, archivo);
    }

    public void salvarUsuarios(ClubDeportivoSeneca club, String archivo) throws IOException {
        persistenciaUsuarios.salvarUsuarios(club, archivo);
    }

    public void cargarClubDeportivo(ClubDeportivoSeneca club, String archivo) throws IOException {
        String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(jsonCompleto);

        if (raiz.has(DEPORTES)) cargarDeportes(club, raiz.getJSONArray(DEPORTES));
        if (raiz.has(EQUIPOS)) cargarEquipos(club, raiz.getJSONArray(EQUIPOS));
        if (raiz.has(COMPETENCIAS)) cargarCompetencias(club, raiz.getJSONArray(COMPETENCIAS));
        if (raiz.has(INSCRIPCIONES_INDIVIDUAL)) cargarInscripcionesIndividual(club, raiz.getJSONArray(INSCRIPCIONES_INDIVIDUAL));
        if (raiz.has(INSCRIPCIONES_EQUIPO)) cargarInscripcionesEquipo(club, raiz.getJSONArray(INSCRIPCIONES_EQUIPO));
        if (raiz.has(INSCRIPCIONES_COMPETENCIA)) cargarInscripcionesCompetencia(club, raiz.getJSONArray(INSCRIPCIONES_COMPETENCIA));
        if (raiz.has(INSTALACIONES)) cargarInstalaciones(club, raiz.getJSONArray(INSTALACIONES));
        if (raiz.has(RESERVAS)) cargarReservas(club, raiz.getJSONArray(RESERVAS));
        if (raiz.has(PARTIDOS)) cargarPartidos(club, raiz.getJSONArray(PARTIDOS));
        if (raiz.has(SESIONES_ENTRENAMIENTO)) cargarSesionesEntrenamiento(club, raiz.getJSONArray(SESIONES_ENTRENAMIENTO));
        if (raiz.has(ASISTENCIA_EVALUACIONES)) cargarAsistenciaEvaluaciones(club, raiz.getJSONArray(ASISTENCIA_EVALUACIONES));
        if (raiz.has(TURNOS)) cargarTurnos(club, raiz.getJSONArray(TURNOS));
        if (raiz.has(SOLICITUDES_CAMBIO_TURNO)) cargarSolicitudesCambioTurno(club, raiz.getJSONArray(SOLICITUDES_CAMBIO_TURNO));
        if (raiz.has(ESTADISTICAS)) cargarEstadisticas(club, raiz.getJSONArray(ESTADISTICAS));
    }

    private void cargarDeportes(ClubDeportivoSeneca club, JSONArray jDeportes) {
        for (int i = 0; i < jDeportes.length(); i++) {
            JSONObject jDeporte = jDeportes.getJSONObject(i);
            String nombre = jDeporte.getString("nombreDeporte");
            int minJugadores = jDeporte.getInt("minJugadores");
            boolean permiteEmpate = jDeporte.getBoolean("permiteEmpate");
            int pVictoria = jDeporte.getInt("puntosVictoria");
            int pDerrota = jDeporte.getInt("puntosDerrota");
            int pEmpate = jDeporte.getInt("puntosEmpate");

            List<String> posiciones = new ArrayList<>();
            JSONArray jPos = jDeporte.optJSONArray("posiciones");
            if (jPos != null) {
                for (int j = 0; j < jPos.length(); j++) {
                    posiciones.add(jPos.getString(j));
                }
            }

            club.crearDeporte(nombre, minJugadores, posiciones, permiteEmpate, pVictoria, pDerrota, pEmpate);
        }
    }

    private void cargarEquipos(ClubDeportivoSeneca club, JSONArray jEquipos) {
        for (int i = 0; i < jEquipos.length(); i++) {
            JSONObject jEquipo = jEquipos.getJSONObject(i);
            String nombre = jEquipo.getString("nombreEquipo");
            int cupoMaximo = jEquipo.getInt("cupoMaximo");
            int categoriaEdad = jEquipo.getInt("categoriaEdad");
            String nombreDeporte = jEquipo.getString("nombreDeporte");

            club.crearEquipo(nombre, cupoMaximo, categoriaEdad, nombreDeporte, true);
        }
    }

    private void cargarCompetencias(ClubDeportivoSeneca club, JSONArray jCompetencias) {
        for (int i = 0; i < jCompetencias.length(); i++) {
            JSONObject jComp = jCompetencias.getJSONObject(i);
            String nombre = jComp.getString("nombreCompetencia");
            String entidad = jComp.getString("entidadOrganizadora");
            String anio = jComp.getString("anioTemporada");
            String formato = jComp.getString("formato");
            String fase = jComp.getString("faseActual");
            int categoria = jComp.optInt("categoria", 1);

            club.crearCompetencia(nombre, entidad, anio, formato, fase, categoria);
        }
    }

    private void cargarInscripcionesIndividual(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String idSocio = jObj.getString("idSocio");
            String nombreDeporte = jObj.getString("nombreDeporte");
            String nivel = jObj.getString("nivel");
            double puntajeRanking = jObj.getDouble("puntajeRanking");

            club.crearInscripcionIndividual(idSocio, nombreDeporte, nivel, puntajeRanking);
        }
    }

    private void cargarInscripcionesEquipo(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String idSocio = jObj.getString("idSocio");
            String nombreEquipo = jObj.getString("nombreEquipo");
            String posicion = jObj.getString("posicion");
            int numeroCamiseta = jObj.getInt("numeroCamiseta");
            int tarjetasAmarillas = jObj.optInt("tarjetasAmarillasAcumuladas", 0);

            club.crearInscripcionEquipo(idSocio, nombreEquipo, posicion, numeroCamiseta, tarjetasAmarillas);
        }
    }

    private void cargarInscripcionesCompetencia(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String nombreEquipo = jObj.getString("nombreEquipo");
            String nombreCompetencia = jObj.getString("nombreCompetencia");
            int puntos = jObj.getInt("puntosAcumulados");
            String posicion = jObj.getString("posicion");

            club.crearInscripcionCompetencia(puntos, posicion, nombreEquipo, nombreCompetencia);
        }
    }

    private void cargarInstalaciones(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String nombre = jObj.getString("nombre");
            String tipo = jObj.getString("tipo");
            boolean esTechada = jObj.getBoolean("esTechada");
            int capacidad = jObj.getInt("capacidadMaximaJugadores");

            club.crearInstalacion(nombre, tipo, esTechada, capacidad);
        }
    }

    private void cargarReservas(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String idReserva = jObj.getString("idReserva");
            String idSocio = jObj.getString("idSocio");
            String nombreInstalacion = jObj.getString("nombreInstalacion");
            String fecha = jObj.getString("fecha");
            String horaInicio = jObj.getString("horaInicio");
            int cantidadJugadores = jObj.getInt("cantidadJugadores");
            String modalidad = jObj.getString("modalidad");

            club.crearReserva(idReserva, idSocio, nombreInstalacion, fecha, horaInicio, cantidadJugadores, modalidad);
        }
    }

    private void cargarPartidos(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String tipoPartido = jObj.getString("tipoPartido");
            String idPartido = jObj.getString("idPartido");
            String fecha = jObj.getString("fecha");
            String marcadorFinal = jObj.getString("marcadorFinal");
            String estadoPartido = jObj.getString("estadoPartido");

            if ("COMPETENCIA".equalsIgnoreCase(tipoPartido)) {
                String nombreEquipo = jObj.getString("nombreEquipo");
                String institucionRival = jObj.getString("institucionRival");
                String escenario = jObj.getString("escenario");
                club.crearPartidoCompetencia(idPartido, fecha, marcadorFinal, estadoPartido, nombreEquipo, institucionRival, escenario);
            } else if ("INTERNO".equalsIgnoreCase(tipoPartido)) {
                String nombreEquipo1 = jObj.getString("nombreEquipo1");
                String nombreEquipo2 = jObj.getString("nombreEquipo2");
                club.crearPartidoInterno(idPartido, fecha, marcadorFinal, estadoPartido, nombreEquipo1, nombreEquipo2);
            } else if ("INDIVIDUAL".equalsIgnoreCase(tipoPartido)) {
                String idUsuario1 = jObj.getString("idUsuario1");
                String idUsuario2 = jObj.getString("idUsuario2");
                club.crearPartidoIndividual(idPartido, fecha, marcadorFinal, estadoPartido, idUsuario1, idUsuario2);
            }
        }
    }

    private void cargarSesionesEntrenamiento(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String idEntrenamiento = jObj.getString("idEntrenamiento");
            String fecha = jObj.getString("fecha");
            String hora = jObj.getString("hora");
            int duracion = jObj.getInt("duracionMinutos");
            String idEntrenador = jObj.getString("idEntrenador");

            Entrenador entrenador = (Entrenador) club.getUsuarios().get(idEntrenador);
            if (entrenador != null) {
                club.crearSesionEntrenamiento(idEntrenamiento, fecha, hora, duracion, entrenador);
            }
        }
    }

    private void cargarAsistenciaEvaluaciones(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String estadoAsistencia = jObj.getString("estadoAsistencia");
            double notaDesempeno = jObj.getDouble("notaDesempeno");
            String comentario = jObj.optString("comentario", "");
            String idSocio = jObj.getString("idSocio");
            String idEntrenamiento = jObj.getString("idEntrenamiento");

            club.crearAsistenciaEvaluacion(estadoAsistencia, notaDesempeno, comentario, idSocio, idEntrenamiento);
        }
    }

    private void cargarTurnos(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String idTurno = jObj.getString("idTurno");
            int diaSemana = jObj.getInt("diaSemana");
            boolean esDiurno = jObj.getBoolean("esDiurno");

            club.crearTurno(idTurno, diaSemana, esDiurno);
        }
    }

    private void cargarSolicitudesCambioTurno(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String idEmpleado = jObj.getString("idEmpleado");
            String idTurnoActual = jObj.getString("idTurnoActual");
            String idTurnoCambio = jObj.getString("idTurnoCambio");
            boolean esAprobada = jObj.optBoolean("esAprobada", false);

            club.crearSolicitudCambioTurno(idEmpleado, idTurnoActual, idTurnoCambio, esAprobada);
        }
    }

    private void cargarEstadisticas(ClubDeportivoSeneca club, JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            String tipo = jObj.getString("tipoEstadistica");
            String idEstadistica = jObj.getString("idEstadistica");
            String idSocio = jObj.getString("idSocio");
            String idPartido = jObj.getString("idPartido");

            if ("FUTBOL".equalsIgnoreCase(tipo)) {
                int goles = jObj.getInt("goles");
                int asistencias = jObj.getInt("asistencias");
                int amarillas = jObj.getInt("tarjetasAmarillas");
                int rojas = jObj.getInt("tarjetasRojas");
                club.crearEstadisticaFutbol(idEstadistica, idSocio, idPartido, goles, asistencias, amarillas, rojas);
            } else if ("VOLEIBOL".equalsIgnoreCase(tipo)) {
                int aces = jObj.getInt("aces");
                int bloqueos = jObj.getInt("bloqueos");
                club.crearEstadisticaVoleibol(idEstadistica, idSocio, idPartido, aces, bloqueos);
            } else if ("RAQUETA".equalsIgnoreCase(tipo)) {
                int setGanados = jObj.getInt("setGanados");
                int gamesGanados = jObj.getInt("gamesGanados");
                int aces = jObj.getInt("aces");
                int errores = jObj.getInt("erroresNoForzados");
                int puntosTotales = jObj.getInt("puntosTotales");
                club.crearEstadisticaRaqueta(idEstadistica, idSocio, idPartido, setGanados, gamesGanados, aces, errores, puntosTotales);
            } else if ("BALONCESTO".equalsIgnoreCase(tipo)) {
                int puntos = jObj.getInt("puntos");
                int rebotes = jObj.getInt("rebotes");
                club.crearEstadisticaBaloncesto(idEstadistica, idSocio, idPartido, puntos, rebotes);
            }
        }
    }

    public void salvarClubDeportivo(ClubDeportivoSeneca club, String archivo) throws IOException {
        JSONObject raiz = new JSONObject();

        // Deportes
        JSONArray jDeportes = new JSONArray();
        for (Deporte dep : club.getDeportes().values()) {
            JSONObject jDep = new JSONObject();
            jDep.put("nombreDeporte", dep.getNombreDeporte());
            jDep.put("minJugadores", dep.getMinJugadores());
            jDep.put("permiteEmpate", dep.isPermiteEmpate());
            jDep.put("puntosVictoria", dep.getPuntosVictoria());
            jDep.put("puntosDerrota", dep.getPuntosDerrota());
            jDep.put("puntosEmpate", dep.getPuntosEmpate());
            jDep.put("posiciones", new JSONArray(dep.getPosiciones()));
            jDeportes.put(jDep);
        }
        raiz.put(DEPORTES, jDeportes);

        // Equipos
        JSONArray jEquipos = new JSONArray();
        for (Equipo eq : club.getEquipos().values()) {
            JSONObject jEq = new JSONObject();
            jEq.put("nombreEquipo", eq.getNombreEquipo());
            jEq.put("cupoMaximo", eq.getCupoMaximo());
            jEq.put("categoriaEdad", eq.getCategoriaEdad());
            if (eq.getDeporteEquipo() != null) {
                jEq.put("nombreDeporte", eq.getDeporteEquipo().getNombreDeporte());
            }
            jEquipos.put(jEq);
        }
        raiz.put(EQUIPOS, jEquipos);

        // Competencias
        JSONArray jCompetencias = new JSONArray();
        for (Competencia comp : club.getCompetencias().values()) {
            JSONObject jComp = new JSONObject();
            jComp.put("nombreCompetencia", comp.getNombreCompetencia());
            jComp.put("entidadOrganizadora", comp.getEntidadOrganizadora());
            jComp.put("anioTemporada", comp.getAnioTemporada());
            jComp.put("formato", comp.getFormato());
            jComp.put("faseActual", comp.getFaseActual());
            jComp.put("categoria", comp.getCategoria());
            jCompetencias.put(jComp);
        }
        raiz.put(COMPETENCIAS, jCompetencias);

        // Inscripciones Individuales
        JSONArray jInscInd = new JSONArray();
        for (InscripcionIndividual ii : club.getInscripcionesIndividual()) {
            JSONObject jObj = new JSONObject();
            if (ii.getSocio() != null) jObj.put("idSocio", ii.getSocio().getIdUsuario());
            if (ii.getDeporteIndividual() != null) jObj.put("nombreDeporte", ii.getDeporteIndividual().getNombreDeporte());
            jObj.put("nivel", ii.getNivel());
            jObj.put("puntajeRanking", ii.getPuntajeRanking());
            jInscInd.put(jObj);
        }
        raiz.put(INSCRIPCIONES_INDIVIDUAL, jInscInd);

        // Inscripciones Equipo
        JSONArray jInscEq = new JSONArray();
        for (InscripcionEquipo ie : club.getInscripcionesEquipo()) {
            JSONObject jObj = new JSONObject();
            if (ie.getSocio() != null) jObj.put("idSocio", ie.getSocio().getIdUsuario());
            if (ie.getEquipo() != null) jObj.put("nombreEquipo", ie.getEquipo().getNombreEquipo());
            jObj.put("posicion", ie.getPosicion());
            jObj.put("numeroCamiseta", ie.getNumeroCamiseta());
            jObj.put("tarjetasAmarillasAcumuladas", ie.getTarjetasAmarillasAcumuladas());
            jInscEq.put(jObj);
        }
        raiz.put(INSCRIPCIONES_EQUIPO, jInscEq);

        // Inscripciones Competencia
        JSONArray jInscComp = new JSONArray();
        for (InscripcionCompetencia ic : club.getInscripcionesCompetencia()) {
            JSONObject jObj = new JSONObject();
            if (ic.getEquipo() != null) jObj.put("nombreEquipo", ic.getEquipo().getNombreEquipo());
            if (ic.getCompetencia() != null) jObj.put("nombreCompetencia", ic.getCompetencia().getNombreCompetencia());
            jObj.put("puntosAcumulados", ic.getPuntosAcumulados());
            jObj.put("posicion", ic.getPosicion());
            jInscComp.put(jObj);
        }
        raiz.put(INSCRIPCIONES_COMPETENCIA, jInscComp);

        // Guardar en archivo JSON con formato indentado
        try (PrintWriter pw = new PrintWriter(archivo)) {
            pw.println(raiz.toString(4));
        }
    }
}