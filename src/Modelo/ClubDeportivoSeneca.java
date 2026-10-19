package Modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import Usuarios.Entrenador;
import Usuarios.Fisioterapeuta;
import Usuarios.Socio;
import Usuarios.Usuario;
import Usuarios.Empleado;
import Deportes.Deporte;
import Deportes.Equipo;
import Deportes.Competencia;
import Deportes.InscripcionIndividual;
import Deportes.InscripcionCompetencia;
import Deportes.InscripcionEquipo;
import ReservasInstalaciones.Instalacion;
import ReservasInstalaciones.Reserva;
import Partidos.Partido;
import Partidos.PartidoCompetencia;
import Partidos.PartidoIndividual;
import Partidos.PartidoInterno;
import SesionEntrenamiento.SesionEntrenamiento;
import SesionEntrenamiento.AsistenciaEvaluacion;
import Turnos.Turno;
import Turnos.SolicitudCambioTurnos;
import Estadisticas.Estadistica;
import Estadisticas.EstadisticaFutbol;
import Estadisticas.EstadisticaVoleibol;
import Estadisticas.EstadisticaRaqueta;
import Estadisticas.EstadisticaBaloncesto;
import Persistencia.CentralPersistencia;

public class ClubDeportivoSeneca {
    private Map<String, Usuario> usuarios;
    private Map<String, Deporte> deportes;
    private Map<String, Equipo> equipos;
    private Map<String, Competencia> competencias;
    private List<InscripcionIndividual> inscripcionesIndividual;
    private List<InscripcionEquipo> inscripcionesEquipo;
    private List<InscripcionCompetencia> inscripcionesCompetencia;
    private Map<String, Instalacion> instalaciones;
    private Map<String, Reserva> reservas;
    private Map<String, Partido> partidos;
    private Map<String, SesionEntrenamiento> sesionesEntrenamiento;
    private List<AsistenciaEvaluacion> asistenciaEvaluaciones;
    private Map<String, Turno> turnos;
    private List<SolicitudCambioTurnos> solicitudesCambioTurno;
    private List<Estadistica> estadisticas;

    public ClubDeportivoSeneca() {
        this.usuarios = new HashMap<>();
        this.deportes = new HashMap<>();
        this.equipos = new HashMap<>();
        this.competencias = new HashMap<>();
        this.inscripcionesIndividual = new ArrayList<>();
        this.inscripcionesEquipo = new ArrayList<>();
        this.inscripcionesCompetencia = new ArrayList<>();
        this.instalaciones = new HashMap<>();
        this.reservas = new HashMap<>();
        this.partidos = new HashMap<>();
        this.sesionesEntrenamiento = new HashMap<>();
        this.asistenciaEvaluaciones = new ArrayList<>();
        this.turnos = new HashMap<>();
        this.solicitudesCambioTurno = new ArrayList<>();
        this.estadisticas = new ArrayList<>();
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario != null && usuario.getIdUsuario() != null) {
            usuarios.put(usuario.getIdUsuario(), usuario);
        }
    }

    public void agregarDeporte(Deporte deporte) {
        if (deporte != null && deporte.getNombreDeporte() != null) {
            deportes.put(deporte.getNombreDeporte(), deporte);
        }
    }

    public void agregarEquipo(Equipo equipo) {
        if (equipo != null && equipo.getNombreEquipo() != null) {
            equipos.put(equipo.getNombreEquipo(), equipo);
        }
    }

    public void agregarCompetencia(Competencia competencia) {
        if (competencia != null && competencia.getNombreCompetencia() != null) {
            competencias.put(competencia.getNombreCompetencia(), competencia);
        }
    }

    public void agregarInscripcionIndividual(InscripcionIndividual inscripcionIndividual) {
        if (inscripcionIndividual != null) {
            this.inscripcionesIndividual.add(inscripcionIndividual);
        }
    }

    public void agregarInscripcionEquipo(InscripcionEquipo inscripcionEquipo) {
        if (inscripcionEquipo != null) {
            this.inscripcionesEquipo.add(inscripcionEquipo);
        }
    }

    public void agregarInscripcionCompetencia(InscripcionCompetencia inscripcionCompetencia) {
        if (inscripcionCompetencia != null) {
            this.inscripcionesCompetencia.add(inscripcionCompetencia);
        }
    }

    public void agregarReserva(Reserva reserva) {
        if (reserva != null && reserva.getIdReserva() != null) {
            reservas.put(reserva.getIdReserva(), reserva);
        }
    }

    public void agregarInstalacion(Instalacion instalacion) {
        if (instalacion != null && instalacion.getNombre() != null) {
            instalaciones.put(instalacion.getNombre(), instalacion);
        }
    }

    public void agregarPartido(Partido partido) {
        if (partido != null && partido.getIdPartido() != null) {
            partidos.put(partido.getIdPartido(), partido);
        }
    }

    public void agregarSesionEntrenamiento(SesionEntrenamiento sesionEntrenamiento) {
        if (sesionEntrenamiento != null && sesionEntrenamiento.getIdEntrenamiento() != null) {
            sesionesEntrenamiento.put(sesionEntrenamiento.getIdEntrenamiento(), sesionEntrenamiento);
        }
    }

    public void agregarAsistenciaEvaluacion(AsistenciaEvaluacion asistenciaEvaluacion) {
        if (asistenciaEvaluacion != null) {
            asistenciaEvaluaciones.add(asistenciaEvaluacion);
        }
    }

    public void agregarTurno(String idTurno) {
        if (idTurno != null && !turnos.containsKey(idTurno)) {
            turnos.put(idTurno, new Turno(idTurno, 1, true));
        }
    }

    public void agregarSolicitudCambioTurno(SolicitudCambioTurnos solicitudCambioTurno) {
        if (solicitudCambioTurno != null) {
            solicitudesCambioTurno.add(solicitudCambioTurno);
        }
    }

    public void agregarEstadistica(Estadistica estadistica) {
        if (estadistica != null) {
            estadisticas.add(estadistica);
        }
    }

    // USUARIOS
    public void crearSocio(String idUsuario, String nombre, String apellido, String login, String contrasena, int puntosFidelidad, int edad, boolean activo) {
        Socio socio = new Socio(idUsuario, nombre, apellido, login, contrasena, puntosFidelidad, edad, activo);
        agregarUsuario(socio);
    }

    public void crearEntrenador(String idUsuario, String nombre, String apellido, String login, String contrasena, int puntosFidelidad, String codigoDescuento) {
        Entrenador entrenador = new Entrenador(idUsuario, nombre, apellido, login, contrasena, puntosFidelidad, codigoDescuento);
        agregarUsuario(entrenador);
    }

    public void crearFisioterapeuta(String idUsuario, String nombre, String apellido, String login, String contrasena, int puntosFidelidad, String codigoDescuento) {
        Fisioterapeuta fisio = new Fisioterapeuta(idUsuario, nombre, apellido, login, contrasena, puntosFidelidad, codigoDescuento);
        agregarUsuario(fisio);
    }

    // DEPORTES Y EQUIPOS
    public void crearDeporte(String nombreDeporte, int minJugadores, List<String> posiciones, boolean permiteEmpate, int puntosVictoria, int puntosDerrota, int puntosEmpate) {
        Deporte deporte = new Deporte(nombreDeporte, minJugadores, posiciones, permiteEmpate, puntosVictoria, puntosDerrota, puntosEmpate);
        agregarDeporte(deporte);
    }

    public void crearEquipo(String nombre, int cupoMaximo, int categoriaEdad, String nombreDeporte, boolean activo) {
        Deporte deporte = deportes.get(nombreDeporte);
        if (deporte != null) {
            Equipo equipo = new Equipo(nombre, cupoMaximo, categoriaEdad, deporte);
            agregarEquipo(equipo);
        }
    }

    public void crearCompetencia(String nombreCompetencia, String entidadOrganizadora, String anioTemporada, String formato, String faseActual, int categoria) {
        Competencia competencia = new Competencia(nombreCompetencia, entidadOrganizadora, anioTemporada, formato, faseActual, categoria);
        agregarCompetencia(competencia);
    }

    public void crearInscripcionIndividual(String idSocio, String nombreDeporte, String nivel, double puntajeRanking) {
        Socio socio = (Socio) usuarios.get(idSocio);
        Deporte deporte = deportes.get(nombreDeporte);
        if (socio != null && deporte != null) {
            InscripcionIndividual inscripcion = new InscripcionIndividual(socio, deporte, nivel, puntajeRanking);
            agregarInscripcionIndividual(inscripcion);
            socio.agregarInscripcionIndividual(inscripcion);
        }
    }

    public void crearInscripcionEquipo(String idSocio, String nombreEquipo, String posicion, int numeroCamiseta, int tarjetasAmarillas) {
        Socio socio = (Socio) usuarios.get(idSocio);
        Equipo equipo = equipos.get(nombreEquipo);
        if (socio != null && equipo != null) {
            InscripcionEquipo inscripcion = new InscripcionEquipo(socio, equipo, posicion, numeroCamiseta);
            inscripcion.setTarjetasAmarillasAcumuladas(tarjetasAmarillas);
            agregarInscripcionEquipo(inscripcion);
            socio.agregarInscripcionEquipo(inscripcion);
            equipo.agregarSocio(socio);
        }
    }

    public void crearInscripcionCompetencia(int puntosAcumulados, String posicion, String nombreEquipo, String nombreCompetencia) {
        Equipo equipo = equipos.get(nombreEquipo);
        Competencia competencia = competencias.get(nombreCompetencia);
        if (equipo != null && competencia != null) {
            InscripcionCompetencia inscripcion = new InscripcionCompetencia(puntosAcumulados, posicion, equipo, competencia);
            agregarInscripcionCompetencia(inscripcion);
            competencia.agregarInscripcionCompetencia(inscripcion);
            equipo.agregarInscripcionCompetencia(inscripcion);
        }
    }
    public void realizarInscripcionCompetencia(int puntosAcumulados, String posicion, String nombreEquipo, String nombreCompetencia, List<Socio> nominaJugadores) {
        Equipo equipo = equipos.get(nombreEquipo);
        Competencia competencia = competencias.get(nombreCompetencia);

        if (equipo != null && competencia != null && equipo.sePuedeCrearInscripcionCompetencia(competencia, nominaJugadores)) {
            crearInscripcionCompetencia(puntosAcumulados, posicion, nombreEquipo, nombreCompetencia);
        }
    }

    // INSTALACIONES Y RESERVAS
    public void crearInstalacion(String nombre, String tipo, boolean esTechada, int capacidadMaximaJugadores) {
        Instalacion instalacion = new Instalacion(nombre, tipo, esTechada, capacidadMaximaJugadores);
        agregarInstalacion(instalacion);
    }

    public void nuevosHorarios(String nombreInstalacion, String fecha, int horaInicio, int horaFin) {
        Instalacion instalacion = instalaciones.get(nombreInstalacion);
        if (instalacion != null) {
            for (int hora = horaInicio; hora < horaFin; hora++) {
                if (!instalacion.horarioExiste(fecha, String.valueOf(hora))) {
                    instalacion.crearHorario(fecha, hora);
                }
            }
        }
    }

    public void crearReserva(String idReserva, String idSocio, String nombreInstalacion, String fecha, String horaInicio, int cantidadJugadores, String modalidad) {
        Socio socio = (Socio) usuarios.get(idSocio);
        Instalacion inst = instalaciones.get(nombreInstalacion);
        if (socio != null && inst != null && inst.sePuedeCrearReserva(fecha, horaInicio)) {
            Reserva reserva = new Reserva(idReserva, socio, inst, fecha, horaInicio, cantidadJugadores, modalidad);
            inst.utilizarHorario(fecha, horaInicio);
            agregarReserva(reserva);
            socio.agregarReserva(reserva);
        }
    }

    // PARTIDOS
    public void crearPartidoCompetencia(String idPartido, String fecha, String marcadorFinal, String estadoPartido, String nombreEquipo, String institucionRival, String escenario) {
        Equipo equipo = equipos.get(nombreEquipo);
        if (equipo != null) {
            Deporte deporte = equipo.getDeporteEquipo();
            PartidoCompetencia partido = new PartidoCompetencia(idPartido, fecha, marcadorFinal, estadoPartido, deporte, equipo, institucionRival, escenario);
            agregarPartido(partido);
        }
    }

    public void crearPartidoInterno(String idPartido, String fecha, String marcadorFinal, String estadoPartido, String nombreEquipo1, String nombreEquipo2) {
        Equipo e1 = equipos.get(nombreEquipo1);
        Equipo e2 = equipos.get(nombreEquipo2);
        if (e1 != null && e2 != null) {
            Deporte deporte = e1.getDeporteEquipo();
            PartidoInterno partido = new PartidoInterno(idPartido, fecha, marcadorFinal, estadoPartido, deporte, e1, e2);
            agregarPartido(partido);
        }
    }

    public void crearPartidoIndividual(String idPartido, String fecha, String marcadorFinal, String estadoPartido, String idUsuario1, String idUsuario2) {
        Socio s1 = (Socio) usuarios.get(idUsuario1);
        Socio s2 = (Socio) usuarios.get(idUsuario2);
        if (s1 != null && s2 != null) {
            PartidoIndividual partido = new PartidoIndividual(idPartido, fecha, marcadorFinal, estadoPartido, null, s1, s2);
            agregarPartido(partido);
        }
    }

    public void realizarPartidoCompetencia(String idPartido, String fecha, String marcadorFinal, String estadoPartido, String nombreEquipo, String nombreCompetencia, String institucionRival, String escenario, List<Socio> nominaJugadores) {
        Equipo equipo = equipos.get(nombreEquipo);
        Competencia competencia = competencias.get(nombreCompetencia);

        if (equipo != null && competencia != null && equipo.sePuedeCrearPartidoCompetencia(competencia, nominaJugadores)) {
            crearPartidoCompetencia(idPartido, fecha, marcadorFinal, estadoPartido, nombreEquipo, institucionRival, escenario);
        }
    }

    public void realizarPartidoInterno(String idPartido, String fecha, String marcadorFinal, String estadoPartido, String nombreEquipo1, String nombreEquipo2) {
        Equipo equipo1 = equipos.get(nombreEquipo1);
        Equipo equipo2 = equipos.get(nombreEquipo2);
        if (equipo1 != null && equipo2 != null && equipo1.sePuedeCrearPartidoInterno(equipo2)) {
            crearPartidoInterno(idPartido, fecha, marcadorFinal, estadoPartido, nombreEquipo1, nombreEquipo2);
        }
    }

    // ENTRENAMIENTOS
    public void crearSesionEntrenamiento(String idEntrenamiento, String fecha, String hora, int duracionMinutos, Entrenador entrenador) {
        SesionEntrenamiento sesion = new SesionEntrenamiento(idEntrenamiento, fecha, hora, duracionMinutos, entrenador);
        agregarSesionEntrenamiento(sesion);
    }

    public void insertarSocioEnSesionEntrenamiento(String idEntrenamiento, String idSocio) {
        SesionEntrenamiento sesion = sesionesEntrenamiento.get(idEntrenamiento);
        Socio socio = (Socio) usuarios.get(idSocio);
        if (sesion != null && socio != null) {
            sesion.insertarSocio(socio);
        }
    }

    public void insertarEquipoEnSesionEntrenamiento(String idEntrenamiento, String nombreEquipo) {
        SesionEntrenamiento sesion = sesionesEntrenamiento.get(idEntrenamiento);
        Equipo equipo = equipos.get(nombreEquipo);
        if (sesion != null && equipo != null) {
            sesion.insertarEquipo(equipo);
        }
    }

    public void crearAsistenciaEvaluacion(String estadoAsistencia, double notaDesempeno, String comentario, String idSocio, String idEntrenamiento) {
        Socio socio = (Socio) usuarios.get(idSocio);
        SesionEntrenamiento sesion = sesionesEntrenamiento.get(idEntrenamiento);
        if (socio != null && sesion != null) {
            AsistenciaEvaluacion asistencia = new AsistenciaEvaluacion(estadoAsistencia, notaDesempeno, comentario, socio, sesion);
            agregarAsistenciaEvaluacion(asistencia);
            socio.agregaAsistenciaEvaluacion(asistencia);
            sesion.agregarAsistenciaEvaluaciones(asistencia);
        }
    }

    // TURNOS Y EMPLEADOS
    public void crearTurno(String idTurno, int diaSemana, boolean esDiurno) {
        if (idTurno != null && !turnos.containsKey(idTurno)) {
            Turno turno = new Turno(idTurno, diaSemana, esDiurno);
            turnos.put(idTurno, turno);
        }
    }

    public void asignarTurnoAEmpleado(String idTurno, String idEmpleado) {
        Turno turno = turnos.get(idTurno);
        Empleado empleado = (Empleado) usuarios.get(idEmpleado);
        if (turno != null && empleado != null) {
            turno.agregarEmpleado(empleado);
            empleado.agregarTurno(turno);
        }
    }

    public void retirarTurnoAEmpleado(String idTurno, String idEmpleado) {
        Turno turno = turnos.get(idTurno);
        Empleado empleado = (Empleado) usuarios.get(idEmpleado);
        if (turno != null && empleado != null) {
            turno.quitarEmpleado(empleado);
            empleado.eliminarTurno(idTurno);
        }
    }

    public void realizarSolicitudCambioTurno(String idEmpleado, String idTurnoActual, String idTurnoCambio) {
        Turno turnoAntiguo = turnos.get(idTurnoActual);
        retirarTurnoAEmpleado(idTurnoActual, idEmpleado);
        asignarTurnoAEmpleado(idTurnoCambio, idEmpleado);
        
        SolicitudCambioTurnos solicitud;
        if (turnoAntiguo != null && turnoAntiguo.cumpleFisioterapeutaEntrenadores()) {
            solicitud = new SolicitudCambioTurnos(idEmpleado, idTurnoActual, idTurnoCambio, true);
        } else {
            retirarTurnoAEmpleado(idTurnoCambio, idEmpleado);
            asignarTurnoAEmpleado(idTurnoActual, idEmpleado);
            solicitud = new SolicitudCambioTurnos(idEmpleado, idTurnoActual, idTurnoCambio);
        }
        agregarSolicitudCambioTurno(solicitud);
    }

    public void crearSolicitudCambioTurno(String idEmpleado, String idTurnoActual, String idTurnoCambio, boolean esAprobada) {
        SolicitudCambioTurnos solicitud = new SolicitudCambioTurnos(idEmpleado, idTurnoActual, idTurnoCambio, esAprobada);
        agregarSolicitudCambioTurno(solicitud);
    }

    // ESTADÍSTICAS
    public void crearEstadisticaFutbol(String idEstadistica, String idSocio, String idPartido, int goles, int asistencias, int tarjetasAmarillas, int tarjetasRojas) {
        Socio socio = (Socio) usuarios.get(idSocio);
        Partido partido = partidos.get(idPartido);
        if (socio != null && partido != null) {
            EstadisticaFutbol est = new EstadisticaFutbol(idEstadistica, socio, partido, goles, asistencias, tarjetasAmarillas, tarjetasRojas);
            agregarEstadistica(est);
            socio.agregarEstadistica(est);
            partido.agregarEstadistica(est);
        }
    }

    public void crearEstadisticaVoleibol(String idEstadistica, String idSocio, String idPartido, int aces, int bloqueos) {
        Socio socio = (Socio) usuarios.get(idSocio);
        Partido partido = partidos.get(idPartido);
        if (socio != null && partido != null) {
            EstadisticaVoleibol est = new EstadisticaVoleibol(idEstadistica, socio, partido, aces, bloqueos);
            agregarEstadistica(est);
            socio.agregarEstadistica(est);
            partido.agregarEstadistica(est);
        }
    }

    public void crearEstadisticaRaqueta(String idEstadistica, String idSocio, String idPartido, int setGanados, int gamesGanados, int aces, int erroresNoForzados, int puntosTotales) {
        Socio socio = (Socio) usuarios.get(idSocio);
        Partido partido = partidos.get(idPartido);
        if (socio != null && partido != null) {
            EstadisticaRaqueta est = new EstadisticaRaqueta(idEstadistica, socio, partido, setGanados, gamesGanados, aces, erroresNoForzados, puntosTotales);
            agregarEstadistica(est);
            socio.agregarEstadistica(est);
            partido.agregarEstadistica(est);
        }
    }

    public void crearEstadisticaBaloncesto(String idEstadistica, String idSocio, String idPartido, int puntos, int rebotes) {
        Socio socio = (Socio) usuarios.get(idSocio);
        Partido partido = partidos.get(idPartido);
        if (socio != null && partido != null) {
            EstadisticaBaloncesto est = new EstadisticaBaloncesto(idEstadistica, socio, partido, puntos, rebotes);
            agregarEstadistica(est);
            socio.agregarEstadistica(est);
            partido.agregarEstadistica(est);
        }
    }

    // REQUISITOS FUNCIONALES
    public Socio goleadorTorneoCompetencia(String nombreCompetencia) {
        Socio goleador = null;
        int maxGoles = -1;
        for (Estadistica est : estadisticas) {
            if (est instanceof EstadisticaFutbol) {
                EstadisticaFutbol estFutbol = (EstadisticaFutbol) est;
                if (estFutbol.getPartido() instanceof PartidoCompetencia) {
                    PartidoCompetencia pc = (PartidoCompetencia) estFutbol.getPartido();
                    if (pc.getEquipo() != null && pc.getEquipo().getInscripcionesCompetencia() != null) {
                        if (estFutbol.getGoles() > maxGoles) {
                            maxGoles = estFutbol.getGoles();
                            goleador = estFutbol.getSocio();
                        }
                    }
                }
            }
        }
        return goleador;
    }

    public void darDeBajaEquipo(Equipo equipo) {
        if (equipo != null && equipo.getNombreEquipo() != null) {
            equipos.remove(equipo.getNombreEquipo());
        }
    }

    public void darDeBajaJugador(Socio socio) {
        if (socio != null) {
            socio.setActivo(false);
        }
    }
 // PERSISTENCIA
    public void cargarUsuarios(String archivo) {
        try {
            CentralPersistencia.getPersistenciaClubDeportivo().cargarUsuarios(this, archivo);
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    public void salvarUsuarios(String archivo) {
        try {
            CentralPersistencia.getPersistenciaClubDeportivo().salvarUsuarios(this, archivo);
        } catch (Exception e) {
            System.err.println("Error al salvar usuarios: " + e.getMessage());
        }
    }

    public void cargarClubDeportivo(String archivo) {
        try {
            CentralPersistencia.getPersistenciaClubDeportivo().cargarClubDeportivo(this, archivo);
        } catch (Exception e) {
            System.err.println("Error al cargar club deportivo: " + e.getMessage());
        }
    }

    public void salvarClubDeportivo(String archivo) {
        try {
            CentralPersistencia.getPersistenciaClubDeportivo().salvarClubDeportivo(this, archivo);
        } catch (Exception e) {
            System.err.println("Error al salvar club deportivo: " + e.getMessage());
        }
    }

    // GETTERS
    public Map<String, Usuario> getUsuarios() { return usuarios; }
    public Map<String, Deporte> getDeportes() { return deportes; }
    public Map<String, Equipo> getEquipos() { return equipos; }
    public Map<String, Competencia> getCompetencias() { return competencias; }
    public List<InscripcionIndividual> getInscripcionesIndividual() { return inscripcionesIndividual; }
    public List<InscripcionEquipo> getInscripcionesEquipo() { return inscripcionesEquipo; }
    public List<InscripcionCompetencia> getInscripcionesCompetencia() { return inscripcionesCompetencia; }
    public Map<String, Instalacion> getInstalaciones() { return instalaciones; }
    public Map<String, Reserva> getReservas() { return reservas; }
    public Map<String, Partido> getPartidos() { return partidos; }
    public Map<String, SesionEntrenamiento> getSesionesEntrenamiento() { return sesionesEntrenamiento; }
    public List<AsistenciaEvaluacion> getAsistenciaEvaluaciones() { return asistenciaEvaluaciones; }
    public Map<String, Turno> getTurnos() { return turnos; }
    public List<SolicitudCambioTurnos> getSolicitudesCambioTurno() { return solicitudesCambioTurno; }
    public List<Estadistica> getEstadisticas() { return estadisticas; }
}