package Modelo;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import Usuarios.Administrador;
import Usuarios.Entrenador;
import Usuarios.Fisioterapeuta;
import Usuarios.Socio;
import Usuarios.Usuario;
import Usuarios.Empleado;
import Usuarios.Invitado;
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
import Partidos.Torneo;
import Partidos.TorneoConjunto;
import Partidos.TorneoIndividual;
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
    private Map<String, Torneo> torneos;
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
        this.torneos = new HashMap<>();
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

    public void agregarTorneo(Torneo torneo) {
        if (torneo != null && torneo.getIdTorneo() != null) {
            torneos.put(torneo.getIdTorneo(), torneo);
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

    public void crearAdministrador(String idUsuario, String nombre, String apellido, String login, String contrasena, int puntosFidelidad) {
        Administrador admin = new Administrador(idUsuario, nombre, apellido, login, contrasena, puntosFidelidad);
        agregarUsuario(admin);
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

    // Operación del socio: valida las reglas y lanza excepcion con el motivo si no se cumple alguna regla.
    public void realizarInscripcionEquipo(String idSocio, String nombreEquipo, String posicion, int numeroCamiseta) {
        Usuario usuario = usuarios.get(idSocio);
        if (!(usuario instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        Socio socio = (Socio) usuario;
        Equipo equipo = equipos.get(nombreEquipo);
        if (!socio.sePuedeCrearInscripcionEquipo(equipo)) {
            throw new IllegalArgumentException(socio.motivoNoPuedeInscribirse(equipo));
        }
        if (!equipo.esPosicionDisponible(posicion)) {
            throw new IllegalArgumentException("La posición no es válida para el deporte");
        }
        if (!equipo.esCamisetaDisponible(numeroCamiseta)) {
            throw new IllegalArgumentException("La camiseta " + numeroCamiseta + " no está disponible");
        }
        crearInscripcionEquipo(idSocio, nombreEquipo, posicion, numeroCamiseta, 0);
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
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no existe");
        }
        if (!equipo.sePuedeCrearInscripcionCompetencia(competencia, nominaJugadores)) {
            throw new IllegalArgumentException(equipo.motivoNoPuedeInscribirseEnCompetencia(competencia, nominaJugadores));
        }
        crearInscripcionCompetencia(puntosAcumulados, posicion, nombreEquipo, nombreCompetencia);
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
        crearPartidoIndividual(idPartido, fecha, marcadorFinal, estadoPartido, idUsuario1, idUsuario2, "");
    }

    public void crearPartidoIndividual(String idPartido, String fecha, String marcadorFinal, String estadoPartido, String idUsuario1, String idUsuario2, String nombreDeporte) {
        Usuario u1 = usuarios.get(idUsuario1);
        Usuario u2 = usuarios.get(idUsuario2);
        if (u1 != null && u2 != null) {
            Deporte deporte = deportes.get(nombreDeporte);
            PartidoIndividual partido = new PartidoIndividual(idPartido, fecha, marcadorFinal, estadoPartido, deporte, u1, u2);
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
    public void programarPartidoInterno(String idPartido, String fecha, String nombreEquipo1, String nombreEquipo2) {
        Equipo equipo1 = equipos.get(nombreEquipo1);
        Equipo equipo2 = equipos.get(nombreEquipo2);
        if (equipo1 == null || equipo2 == null) {
            throw new IllegalArgumentException("Alguno de los equipos no existe");
        }
        if (!equipo1.sePuedeCrearPartidoInterno(equipo2)) {
            throw new IllegalArgumentException(equipo1.motivoNoPuedeJugarContra(equipo2));
        }
        crearPartidoInterno(idPartido, fecha, "0-0", "Programado", nombreEquipo1, nombreEquipo2);
    }

    public void programarPartidoCompetencia(String idPartido, String fecha, String nombreEquipo, String nombreCompetencia, String institucionRival, String escenario) {
        Equipo equipo = equipos.get(nombreEquipo);
        Competencia competencia = competencias.get(nombreCompetencia);
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no existe");
        }
        if (competencia == null) {
            throw new IllegalArgumentException("La competencia no existe");
        }
        if (!equipo.estaInscritoEn(competencia)) {
            throw new IllegalArgumentException("El equipo no está inscrito en la competencia");
        }
        if (institucionRival == null || institucionRival.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la institución rival");
        }
        crearPartidoCompetencia(idPartido, fecha, "0-0", "Programado", nombreEquipo, institucionRival, escenario);
        asociarPartidoACompetencia(idPartido, nombreCompetencia);
    }

    public void asociarPartidoACompetencia(String idPartido, String nombreCompetencia) {
        Partido partido = partidos.get(idPartido);
        Competencia competencia = competencias.get(nombreCompetencia);
        if (partido instanceof PartidoCompetencia && competencia != null) {
            PartidoCompetencia partidoCompetencia = (PartidoCompetencia) partido;
            partidoCompetencia.setCompetencia(competencia);
            competencia.agregarPartidosCompetencia(partidoCompetencia);
        }
    }

    private List<Equipo> equiposDelPartido(Partido partido) {
        List<Equipo> lista = new ArrayList<>();
        if (partido instanceof PartidoInterno) {
            lista.add(((PartidoInterno) partido).getEquipo1());
            lista.add(((PartidoInterno) partido).getEquipo2());
        } else if (partido instanceof PartidoCompetencia) {
            lista.add(((PartidoCompetencia) partido).getEquipo());
        }
        return lista;
    }

    private int[] separarMarcador(String marcador) {
        if (marcador == null) {
            throw new IllegalArgumentException("El marcador debe tener el formato 2-1");
        }
        String[] partes = marcador.trim().split("-");
        if (partes.length != 2) {
            throw new IllegalArgumentException("El marcador debe tener el formato 2-1");
        }
        int primero;
        int segundo;
        try {
            primero = Integer.parseInt(partes[0].trim());
            segundo = Integer.parseInt(partes[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El marcador debe tener el formato 2-1");
        }
        if (primero < 0 || segundo < 0) {
            throw new IllegalArgumentException("El marcador no puede tener valores negativos");
        }
        return new int[] {primero, segundo};
    }

    // Los jugadores suspendidos de los equipos del partido cumplen su suspensión con este partido
    private void cumplirSuspensiones(Partido partido) {
        for (Equipo equipo : equiposDelPartido(partido)) {
            for (InscripcionEquipo inscripcion : inscripcionesEquipo) {
                if (inscripcion.getEquipo() == equipo && inscripcion.isEstaSuspendido()) {
                    inscripcion.cumplirSuspension();
                }
            }
        }
    }

    // En un partido de competencia, el primer número del marcador es el del club.
    private void sumarPuntosCompetencia(PartidoCompetencia partido, int golesClub, int golesRival) {
        Competencia competencia = partido.getCompetencia();
        if (competencia == null) {
            return;
        }
        Deporte deporte = partido.getDeporte();
        for (InscripcionCompetencia inscripcion : partido.getEquipo().getInscripcionesCompetencia()) {
            if (inscripcion.getCompetencia() == competencia) {
                int puntos;
                if (golesClub > golesRival) {
                    puntos = deporte.getPuntosVictoria();
                } else if (golesClub == golesRival) {
                    puntos = deporte.getPuntosEmpate();
                } else {
                    puntos = deporte.getPuntosDerrota();
                }
                inscripcion.setPuntosAcumulados(inscripcion.getPuntosAcumulados() + puntos);
            }
        }
    }

    public void registrarMarcadorPartido(String idPartido, String marcador) {
        Partido partido = partidos.get(idPartido);
        if (partido == null) {
            throw new IllegalArgumentException("El partido no existe");
        }
        if (!(partido instanceof PartidoInterno) && !(partido instanceof PartidoCompetencia)) {
            throw new IllegalArgumentException("Solo se registra el marcador de partidos de equipos del club");
        }
        if (!"Programado".equalsIgnoreCase(partido.getEstadoPartido())) {
            throw new IllegalArgumentException("El partido ya fue registrado");
        }
        int[] goles = separarMarcador(marcador);
        if (goles[0] == goles[1] && !partido.getDeporte().isPermiteEmpate()) {
            throw new IllegalArgumentException("En este deporte no se permiten empates");
        }
        partido.setMarcadorFinal(goles[0] + "-" + goles[1]);
        partido.setEstadoPartido("Finalizado");
        cumplirSuspensiones(partido);
        if (partido instanceof PartidoCompetencia) {
            sumarPuntosCompetencia((PartidoCompetencia) partido, goles[0], goles[1]);
        }
    }

    // Valida que el socio pueda tener estadísticas en el partido y devuelve su inscripción en el equipo que jugó.
    private InscripcionEquipo validarParticipante(String idPartido, String idSocio) {
        Partido partido = partidos.get(idPartido);
        if (partido == null) {
            throw new IllegalArgumentException("El partido no existe");
        }
        if (!(partido instanceof PartidoInterno) && !(partido instanceof PartidoCompetencia)) {
            throw new IllegalArgumentException("Solo se registran estadísticas de partidos de equipos del club");
        }
        if (!"Finalizado".equalsIgnoreCase(partido.getEstadoPartido())) {
            throw new IllegalArgumentException("Primero registre el marcador del partido");
        }
        Usuario usuario = usuarios.get(idSocio);
        if (!(usuario instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        Socio socio = (Socio) usuario;
        for (Estadistica existente : partido.getEstadisticas()) {
            if (existente.getSocio() == socio) {
                throw new IllegalArgumentException("El socio ya tiene estadísticas en este partido");
            }
        }
        InscripcionEquipo inscripcion = null;
        for (Equipo equipo : equiposDelPartido(partido)) {
            InscripcionEquipo candidata = equipo.getInscripcionDe(socio);
            if (candidata != null) {
                inscripcion = candidata;
            }
        }
        if (inscripcion == null) {
            throw new IllegalArgumentException("El socio no pertenece a ninguno de los equipos del partido");
        }
        return inscripcion;
    }

    private void validarNoNegativos(int... valores) {
        for (int valor : valores) {
            if (valor < 0) {
                throw new IllegalArgumentException("Los valores no pueden ser negativos");
            }
        }
    }

    // DEPORTES INDIVIDUALES
    private InscripcionIndividual buscarInscripcionIndividual(Socio socio, Deporte deporte) {
        for (InscripcionIndividual inscripcion : inscripcionesIndividual) {
            if (inscripcion.getSocio() == socio && inscripcion.getDeporteIndividual() == deporte) {
                return inscripcion;
            }
        }
        return null;
    }

    public void realizarInscripcionIndividual(String idSocio, String nombreDeporte) {
        Usuario usuario = usuarios.get(idSocio);
        if (!(usuario instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        Socio socio = (Socio) usuario;
        if (!socio.isActivo()) {
            throw new IllegalArgumentException("El socio está inactivo");
        }
        Deporte deporte = deportes.get(nombreDeporte);
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no existe");
        }
        if (!deporte.esIndividual()) {
            throw new IllegalArgumentException("Ese deporte no es individual: la inscripción se hace a un equipo");
        }
        if (buscarInscripcionIndividual(socio, deporte) != null) {
            throw new IllegalArgumentException("El socio ya está inscrito en ese deporte");
        }
        crearInscripcionIndividual(idSocio, nombreDeporte, "Principiante", 0);
    }

    public void programarPartidoIndividual(String idPartido, String fecha, String idSocio1, String idSocio2, String nombreDeporte) {
        Usuario u1 = usuarios.get(idSocio1);
        Usuario u2 = usuarios.get(idSocio2);
        if (!(u1 instanceof Socio) || !(u2 instanceof Socio)) {
            throw new IllegalArgumentException("Alguno de los socios no existe");
        }
        if (idSocio1.equals(idSocio2)) {
            throw new IllegalArgumentException("Los jugadores deben ser distintos");
        }
        Deporte deporte = deportes.get(nombreDeporte);
        if (deporte == null || !deporte.esIndividual()) {
            throw new IllegalArgumentException("El deporte no existe o no es individual");
        }
        InscripcionIndividual inscripcion1 = buscarInscripcionIndividual((Socio) u1, deporte);
        InscripcionIndividual inscripcion2 = buscarInscripcionIndividual((Socio) u2, deporte);
        if (inscripcion1 == null || inscripcion2 == null) {
            throw new IllegalArgumentException("Los dos socios deben estar inscritos en el deporte");
        }
        int diferencia = Math.abs(inscripcion1.posicionNivel() - inscripcion2.posicionNivel());
        if (diferencia > 1) {
            throw new IllegalArgumentException("Los jugadores deben ser del mismo nivel o de niveles adyacentes");
        }
        crearPartidoIndividual(idPartido, fecha, "0-0", "Programado", idSocio1, idSocio2, nombreDeporte);
    }

    // El jugador que registra indica los sets ganados por él y por su rival. Actualiza el ranking de ambos.
    public void registrarResultadoIndividual(String idPartido, String idSocio, int setsPropios, int setsRival) {
        Partido partido = partidos.get(idPartido);
        if (!(partido instanceof PartidoIndividual)) {
            throw new IllegalArgumentException("El partido individual no existe");
        }
        PartidoIndividual individual = (PartidoIndividual) partido;
        if (!"Programado".equalsIgnoreCase(individual.getEstadoPartido())) {
            throw new IllegalArgumentException("El partido ya fue registrado");
        }
        if (individual.getDeporte() == null) {
            throw new IllegalArgumentException("El partido no tiene deporte asignado");
        }
        Socio registra = null;
        Socio rival = null;
        if (individual.getSocio1().getIdUsuario().equals(idSocio)) {
            registra = individual.getSocio1();
            rival = individual.getSocio2();
        } else if (individual.getSocio2().getIdUsuario().equals(idSocio)) {
            registra = individual.getSocio2();
            rival = individual.getSocio1();
        }
        if (registra == null) {
            throw new IllegalArgumentException("Solo los jugadores del partido pueden registrar el resultado");
        }
        validarNoNegativos(setsPropios, setsRival);
        if (setsPropios == setsRival) {
            throw new IllegalArgumentException("En los deportes individuales no hay empates");
        }
        Socio ganador = rival;
        Socio perdedor = registra;
        if (setsPropios > setsRival) {
            ganador = registra;
            perdedor = rival;
        }
        if (registra == individual.getSocio1()) {
            individual.setMarcadorFinal(setsPropios + "-" + setsRival);
        } else {
            individual.setMarcadorFinal(setsRival + "-" + setsPropios);
        }
        individual.setEstadoPartido("Finalizado");
        Deporte deporte = individual.getDeporte();
        InscripcionIndividual inscripcionGanador = buscarInscripcionIndividual(ganador, deporte);
        InscripcionIndividual inscripcionPerdedor = buscarInscripcionIndividual(perdedor, deporte);
        if (inscripcionGanador != null) {
            inscripcionGanador.sumarPuntosRanking(deporte.getPuntosVictoria());
        }
        if (inscripcionPerdedor != null) {
            inscripcionPerdedor.sumarPuntosRanking(deporte.getPuntosDerrota());
        }
    }

    // Tenis y pádel: sets, games, aces y errores. Tenis de mesa: sets y puntos totales.
    public void registrarEstadisticaRaqueta(String idPartido, String idSocio, int setGanados, int gamesGanados, int aces, int errores, int puntosTotales) {
        validarNoNegativos(setGanados, gamesGanados, aces, errores, puntosTotales);
        Partido partido = partidos.get(idPartido);
        if (!(partido instanceof PartidoIndividual) || partido.getDeporte() == null) {
            throw new IllegalArgumentException("El partido individual no existe");
        }
        PartidoIndividual individual = (PartidoIndividual) partido;
        if (!"Finalizado".equalsIgnoreCase(individual.getEstadoPartido())) {
            throw new IllegalArgumentException("Primero registre el resultado del partido");
        }
        boolean participa = individual.getSocio1().getIdUsuario().equals(idSocio) || individual.getSocio2().getIdUsuario().equals(idSocio);
        if (!participa) {
            throw new IllegalArgumentException("El socio no participó en el partido");
        }
        for (Estadistica existente : individual.getEstadisticas()) {
            if (existente.getSocio().getIdUsuario().equals(idSocio)) {
                throw new IllegalArgumentException("El socio ya tiene estadísticas en este partido");
            }
        }
        String nombre = individual.getDeporte().getNombreDeporte().toLowerCase();
        boolean esMesa = nombre.contains("mesa");
        boolean esTenisOPadel = nombre.contains("tenis") || nombre.contains("pádel") || nombre.contains("padel");
        if (!esTenisOPadel) {
            throw new IllegalArgumentException("Este deporte no tiene estadísticas definidas");
        }
        if (esMesa && (gamesGanados > 0 || aces > 0 || errores > 0)) {
            throw new IllegalArgumentException("En tenis de mesa solo se registran sets ganados y puntos totales");
        }
        if (!esMesa && puntosTotales > 0) {
            throw new IllegalArgumentException("Los puntos totales solo aplican a tenis de mesa");
        }
        crearEstadisticaRaqueta(generarIdEstadistica(), idSocio, idPartido, setGanados, gamesGanados, aces, errores, puntosTotales);
    }

    // ADMIN Encargado
    private String describirPartido(Partido partido) {
        String detalle = "Partido";
        if (partido instanceof PartidoInterno) {
            PartidoInterno interno = (PartidoInterno) partido;
            detalle = "Interno: " + interno.getEquipo1().getNombreEquipo() + " vs " + interno.getEquipo2().getNombreEquipo();
        } else if (partido instanceof PartidoCompetencia) {
            PartidoCompetencia competencia = (PartidoCompetencia) partido;
            detalle = "Competencia: " + competencia.getEquipo().getNombreEquipo() + " vs " + competencia.getInstitucionRival();
        } else if (partido instanceof PartidoIndividual) {
            PartidoIndividual individual = (PartidoIndividual) partido;
            Usuario j1 = individual.getJugador1() != null ? individual.getJugador1() : individual.getSocio1();
            Usuario j2 = individual.getJugador2() != null ? individual.getJugador2()
                    : (individual.getSocio2() != null ? individual.getSocio2() : individual.getEntrenador());
            String n1 = j1 != null ? j1.getNombre() : "";
            String n2 = j2 != null ? j2.getNombre() : "";
            detalle = "Individual: " + n1 + " vs " + n2;
        }
        return partido.getIdPartido() + " / " + detalle + " / " + partido.getFecha() + " / " + partido.getEstadoPartido()
                + " / marcador: " + partido.getMarcadorFinal();
    }

    public List<String> reporteInventarioInstalaciones() {
        List<String> lineas = new ArrayList<>();
        for (Instalacion instalacion : instalaciones.values()) {
            int libres = 0;
            int ocupados = 0;
            for (Boolean disponible : instalacion.getHorarios().values()) {
                if (disponible) {
                    libres++;
                } else {
                    ocupados++;
                }
            }
            String cubierta = "descubierta";
            if (instalacion.isEsTechado()) {
                cubierta = "techada";
            }
            lineas.add(instalacion.getNombre() + " / " + instalacion.getTipo() + " / " + cubierta
                    + " / capacidad: " + instalacion.getCapacidadMaximaJugadores()
                    + " / horarios libres: " + libres + " / ocupados: " + ocupados);
        }
        return lineas;
    }

    public List<String> reporteHistorialReservas() {
        List<String> lineas = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            lineas.add(reserva.getIdReserva() + " / " + reserva.getSocio().getNombre() + " / " + reserva.getInstalacion().getNombre()
                    + " / " + reserva.getFecha() + " " + reserva.getHoraInicio() + "h / jugadores: " + reserva.getCantidadJugadores()
                    + " / " + reserva.getModalidad());
        }
        return lineas;
    }

    public List<String> reporteHistorialPartidos() {
        List<String> lineas = new ArrayList<>();
        for (Partido partido : partidos.values()) {
            lineas.add(describirPartido(partido));
        }
        return lineas;
    }

    public List<String> reporteHistorialSesiones() {
        List<String> lineas = new ArrayList<>();
        for (SesionEntrenamiento sesion : sesionesEntrenamiento.values()) {
            lineas.add(sesion.getIdEntrenamiento() + " / entrenador: " + sesion.getEntrenador().getNombre()
                    + " / " + sesion.getFecha() + " " + sesion.getHora() + " / " + sesion.getDuracionMinutos() + " min"
                    + " / convocados: " + sesion.getEntrenamientoSocios().size()
                    + " / asistencias registradas: " + sesion.getAsistenciaEvaluaciones().size());
        }
        return lineas;
    }

    public List<String> reporteAsistencia() {
        List<Socio> vistos = new ArrayList<>();
        for (AsistenciaEvaluacion asistencia : asistenciaEvaluaciones) {
            if (!vistos.contains(asistencia.getSocio())) {
                vistos.add(asistencia.getSocio());
            }
        }
        List<String> lineas = new ArrayList<>();
        for (Socio socio : vistos) {
            int asistio = 0;
            int falto = 0;
            int tarde = 0;
            int evaluaciones = 0;
            double suma = 0;
            for (AsistenciaEvaluacion asistencia : asistenciaEvaluaciones) {
                if (asistencia.getSocio() == socio) {
                    String estado = asistencia.getEstadoAsistencia();
                    if ("Asistió".equals(estado)) {
                        asistio++;
                    } else if ("Faltó".equals(estado)) {
                        falto++;
                    } else if ("Llegó tarde".equals(estado)) {
                        tarde++;
                    }
                    if (asistencia.getNotaDesempeno() > 0) {
                        evaluaciones++;
                        suma += asistencia.getNotaDesempeno();
                    }
                }
            }
            String promedio = "sin notas";
            if (evaluaciones > 0) {
                promedio = String.valueOf(Math.round(suma / evaluaciones * 10) / 10.0);
            }
            lineas.add(socio.getNombre() + " " + socio.getApellido() + " | asistió: " + asistio + " | llegó tarde: " + tarde
                    + " | faltó: " + falto + " | nota promedio: " + promedio);
        }
        return lineas;
    }

    // Tabla de goleadores acumulados de una competencia, de mayor a menor.
    public List<String> reporteGoleadores(String nombreCompetencia) {
        if (competencias.get(nombreCompetencia) == null) {
            throw new IllegalArgumentException("La competencia no existe");
        }
        List<Socio> socios = new ArrayList<>();
        List<Integer> goles = new ArrayList<>();
        for (Estadistica estadistica : estadisticas) {
            Socio socio = estadistica.getSocio();
            if (!socios.contains(socio)) {
                int total = golesEnCompetencia(nombreCompetencia, socio);
                if (total > 0) {
                    socios.add(socio);
                    goles.add(total);
                }
            }
        }
        List<String> lineas = new ArrayList<>();
        while (!socios.isEmpty()) {
            int mejor = 0;
            for (int i = 1; i < socios.size(); i++) {
                if (goles.get(i) > goles.get(mejor)) {
                    mejor = i;
                }
            }
            lineas.add(socios.get(mejor).getNombre() + " " + socios.get(mejor).getApellido() + " | goles: " + goles.get(mejor));
            socios.remove(mejor);
            goles.remove(mejor);
        }
        return lineas;
    }

    // Solo cuenta a los equipos del club inscritos en la competencia.
    public List<String> reporteTablaPosiciones(String nombreCompetencia) {
        Competencia competencia = competencias.get(nombreCompetencia);
        if (competencia == null) {
            throw new IllegalArgumentException("La competencia no existe");
        }
        List<InscripcionCompetencia> pendientes = new ArrayList<>(competencia.getInscripcionesCompetencia());
        List<String> lineas = new ArrayList<>();
        int puesto = 1;
        while (!pendientes.isEmpty()) {
            InscripcionCompetencia mejor = pendientes.get(0);
            for (InscripcionCompetencia inscripcion : pendientes) {
                if (inscripcion.getPuntosAcumulados() > mejor.getPuntosAcumulados()) {
                    mejor = inscripcion;
                }
            }
            lineas.add(puesto + ". " + mejor.getEquipo().getNombreEquipo() + " / puntos: " + mejor.getPuntosAcumulados());
            pendientes.remove(mejor);
            puesto++;
        }
        return lineas;
    }

    public List<String> reporteRankingIndividual(String nombreDeporte) {
        Deporte deporte = deportes.get(nombreDeporte);
        if (deporte == null || !deporte.esIndividual()) {
            throw new IllegalArgumentException("El deporte no existe o no es individual");
        }
        List<InscripcionIndividual> pendientes = new ArrayList<>();
        for (InscripcionIndividual inscripcion : inscripcionesIndividual) {
            if (inscripcion.getDeporteIndividual() == deporte) {
                pendientes.add(inscripcion);
            }
        }
        List<String> lineas = new ArrayList<>();
        int puesto = 1;
        while (!pendientes.isEmpty()) {
            InscripcionIndividual mejor = pendientes.get(0);
            for (InscripcionIndividual inscripcion : pendientes) {
                if (inscripcion.getPuntajeRanking() > mejor.getPuntajeRanking()) {
                    mejor = inscripcion;
                }
            }
            lineas.add(puesto + ". " + mejor.getSocio().getNombre() + " " + mejor.getSocio().getApellido()
                    + " / nivel: " + mejor.getNivel() + " / puntaje: " + mejor.getPuntajeRanking());
            pendientes.remove(mejor);
            puesto++;
        }
        return lineas;
    }

    // Baja de un equipo - se rechaza si tiene partidos programados; se eliminan sus inscripciones
    public void realizarBajaEquipo(String nombreEquipo) {
        Equipo equipo = equipos.get(nombreEquipo);
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no existe");
        }
        for (Partido partido : partidos.values()) {
            if ("Programado".equalsIgnoreCase(partido.getEstadoPartido()) && equiposDelPartido(partido).contains(equipo)) {
                throw new IllegalArgumentException("El equipo tiene partidos programados");
            }
        }
        List<InscripcionEquipo> propias = new ArrayList<>();
        for (InscripcionEquipo inscripcion : inscripcionesEquipo) {
            if (inscripcion.getEquipo() == equipo) {
                propias.add(inscripcion);
            }
        }
        for (InscripcionEquipo inscripcion : propias) {
            inscripcionesEquipo.remove(inscripcion);
            inscripcion.getSocio().getInscripcionesEquipos().remove(inscripcion);
        }
        darDeBajaEquipo(equipo);
    }

    // Baja de un jugador - queda inactivo (no puede iniciar sesión ni cuenta como habilitado)
    public void realizarBajaJugador(String idSocio) {
        Usuario usuario = usuarios.get(idSocio);
        if (!(usuario instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        Socio socio = (Socio) usuario;
        if (!socio.isActivo()) {
            throw new IllegalArgumentException("El socio ya está dado de baja");
        }
        darDeBajaJugador(socio);
    }

    // ESTADISTICAS
    public String generarIdEstadistica() {
        int max = 0;
        for (Estadistica estadistica : estadisticas) {
            String digitos = estadistica.getIdEstadistica().replaceAll("[^0-9]", "");
            if (!digitos.isEmpty()) {
                max = Math.max(max, Integer.parseInt(digitos));
            }
        }
        return "EST-" + (max + 1);
    }

    // Devuelve true si el jugador queda suspendido por las tarjetas de este partido
    public boolean registrarEstadisticaFutbol(String idPartido, String idSocio, int goles, int asistencias, int amarillas, int rojas) {
        validarNoNegativos(goles, asistencias, amarillas, rojas);
        InscripcionEquipo inscripcion = validarParticipante(idPartido, idSocio);
        crearEstadisticaFutbol(generarIdEstadistica(), idSocio, idPartido, goles, asistencias, amarillas, rojas);
        inscripcion.registrarTarjetas(amarillas, rojas);
        return inscripcion.isEstaSuspendido();
    }

    public void registrarEstadisticaBaloncesto(String idPartido, String idSocio, int puntos, int rebotes) {
        validarNoNegativos(puntos, rebotes);
        validarParticipante(idPartido, idSocio);
        crearEstadisticaBaloncesto(generarIdEstadistica(), idSocio, idPartido, puntos, rebotes);
    }

    public void registrarEstadisticaVoleibol(String idPartido, String idSocio, int aces, int bloqueos) {
        validarNoNegativos(aces, bloqueos);
        validarParticipante(idPartido, idSocio);
        crearEstadisticaVoleibol(generarIdEstadistica(), idSocio, idPartido, aces, bloqueos);
    }

    public void crearSesionEntrenamiento(String idEntrenamiento, String fecha, String hora, int duracionMinutos, Entrenador entrenador) {
        SesionEntrenamiento sesion = new SesionEntrenamiento(idEntrenamiento, fecha, hora, duracionMinutos, entrenador);
        agregarSesionEntrenamiento(sesion);
        entrenador.agregarSesionEntrenamiento(sesion);
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

    public boolean realizarSolicitudCambioTurno(String idEmpleado, String idTurnoActual, String idTurnoCambio) {
        Usuario usuario = usuarios.get(idEmpleado);
        if (!(usuario instanceof Empleado)) {
            throw new IllegalArgumentException("El empleado no existe");
        }
        Empleado empleado = (Empleado) usuario;
        Turno turnoActual = turnos.get(idTurnoActual);
        Turno turnoNuevo = turnos.get(idTurnoCambio);
        if (turnoActual == null || turnoNuevo == null) {
            throw new IllegalArgumentException("El turno no existe");
        }
        if (!empleado.getTurnos().containsKey(idTurnoActual)) {
            throw new IllegalArgumentException("El empleado no tiene asignado el turno que quiere dejar");
        }
        if (empleado.getTurnos().containsKey(idTurnoCambio)) {
            throw new IllegalArgumentException("El empleado ya tiene asignado el turno solicitado");
        }
        retirarTurnoAEmpleado(idTurnoActual, idEmpleado);
        boolean aprobada = turnoActual.cumpleFisioterapeutaEntrenadores();
        if (aprobada) {
            asignarTurnoAEmpleado(idTurnoCambio, idEmpleado);
        } else {
            asignarTurnoAEmpleado(idTurnoActual, idEmpleado);
        }
        agregarSolicitudCambioTurno(new SolicitudCambioTurnos(idEmpleado, idTurnoActual, idTurnoCambio, aprobada));
        return aprobada;
    }

    public void crearSolicitudCambioTurno(String idEmpleado, String idTurnoActual, String idTurnoCambio, boolean esAprobada) {
        SolicitudCambioTurnos solicitud = new SolicitudCambioTurnos(idEmpleado, idTurnoActual, idTurnoCambio, esAprobada);
        agregarSolicitudCambioTurno(solicitud);
    }

    // RESERVAS Y ENTRENAMIENTOS
    public void realizarReserva(String idReserva, String idSocio, String nombreInstalacion, String fecha, String hora, int cantidadJugadores, String modalidad) {
        Usuario usuario = usuarios.get(idSocio);
        if (!(usuario instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        if (!((Socio) usuario).isActivo()) {
            throw new IllegalArgumentException("El socio está inactivo");
        }
        Instalacion instalacion = instalaciones.get(nombreInstalacion);
        if (instalacion == null) {
            throw new IllegalArgumentException("La instalación no existe");
        }
        if (!instalacion.horarioExiste(fecha, hora)) {
            throw new IllegalArgumentException("Ese horario no existe para la instalación");
        }
        if (!instalacion.sePuedeCrearReserva(fecha, hora)) {
            throw new IllegalArgumentException("Ese horario ya está reservado");
        }
        if (cantidadJugadores <= 0 || cantidadJugadores > instalacion.getCapacidadMaximaJugadores()) {
            throw new IllegalArgumentException("La cantidad de jugadores debe estar entre 1 y " + instalacion.getCapacidadMaximaJugadores());
        }
        if (modalidad == null || modalidad.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la modalidad");
        }
        crearReserva(idReserva, idSocio, nombreInstalacion, fecha, hora, cantidadJugadores, modalidad);
    }

    public String generarIdSesionEntrenamiento() {
        int max = 0;
        for (String id : sesionesEntrenamiento.keySet()) {
            String digitos = id.replaceAll("[^0-9]", "");
            if (!digitos.isEmpty()) {
                max = Math.max(max, Integer.parseInt(digitos));
            }
        }
        return "ENT-" + (max + 1);
    }

    public void programarSesionEntrenamiento(String idEntrenamiento, String fecha, String hora, int duracionMinutos, String idEntrenador, String nombreInstalacion, String nombreEquipo, String nombreDeporte, List<String> idsConvocados) {
        Usuario usuario = usuarios.get(idEntrenador);
        if (!(usuario instanceof Entrenador)) {
            throw new IllegalArgumentException("El entrenador no existe");
        }
        if (fecha == null || fecha.trim().isEmpty() || hora == null || hora.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la fecha y la hora");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
        Instalacion instalacion = instalaciones.get(nombreInstalacion);
        if (instalacion == null) {
            throw new IllegalArgumentException("La instalación no existe");
        }
        boolean hayEquipo = nombreEquipo != null && !nombreEquipo.trim().isEmpty();
        boolean hayDeporte = nombreDeporte != null && !nombreDeporte.trim().isEmpty();
        if (hayEquipo == hayDeporte) {
            throw new IllegalArgumentException("Debe indicar un equipo o un deporte individual, pero no ambos");
        }
        List<Socio> convocados = new ArrayList<>();
        if (hayEquipo) {
            Equipo equipo = equipos.get(nombreEquipo);
            if (equipo == null) {
                throw new IllegalArgumentException("El equipo no existe");
            }
            convocados.addAll(equipo.getSocios());
        } else {
            Deporte deporte = deportes.get(nombreDeporte);
            if (deporte == null) {
                throw new IllegalArgumentException("El deporte no existe");
            }
            List<Socio> inscritos = new ArrayList<>();
            for (InscripcionIndividual inscripcion : inscripcionesIndividual) {
                if (inscripcion.getDeporteIndividual() == deporte) {
                    inscritos.add(inscripcion.getSocio());
                }
            }
            if (idsConvocados == null || idsConvocados.isEmpty()) {
                convocados.addAll(inscritos);
            } else {
                for (String id : idsConvocados) {
                    Usuario candidato = usuarios.get(id);
                    if (!(candidato instanceof Socio) || !inscritos.contains(candidato)) {
                        throw new IllegalArgumentException("El socio " + id + " no está inscrito en el deporte");
                    }
                    convocados.add((Socio) candidato);
                }
            }
        }
        if (convocados.isEmpty()) {
            throw new IllegalArgumentException("No hay deportistas para convocar");
        }
        crearSesionEntrenamiento(idEntrenamiento, fecha, hora, duracionMinutos, (Entrenador) usuario);
        sesionesEntrenamiento.get(idEntrenamiento).setInstalacion(instalacion);
        for (Socio socio : convocados) {
            insertarSocioEnSesionEntrenamiento(idEntrenamiento, socio.getIdUsuario());
        }
    }

    public void asignarInstalacionASesion(String idEntrenamiento, String nombreInstalacion) {
        SesionEntrenamiento sesion = sesionesEntrenamiento.get(idEntrenamiento);
        Instalacion instalacion = instalaciones.get(nombreInstalacion);
        if (sesion != null && instalacion != null) {
            sesion.setInstalacion(instalacion);
        }
    }

    public void registrarAsistencia(String idEntrenamiento, String idSocio, String estadoAsistencia, double nota, String comentario) {
        SesionEntrenamiento sesion = sesionesEntrenamiento.get(idEntrenamiento);
        if (sesion == null) {
            throw new IllegalArgumentException("La sesión no existe");
        }
        Usuario usuario = usuarios.get(idSocio);
        if (!(usuario instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        if (!sesion.getEntrenamientoSocios().contains(usuario)) {
            throw new IllegalArgumentException("El socio no fue convocado a la sesión");
        }
        if (sesion.getAsistenciaEvaluaciones().containsKey(idSocio)) {
            throw new IllegalArgumentException("La asistencia de este socio ya fue registrada");
        }
        boolean estadoValido = "Asistió".equals(estadoAsistencia) || "Faltó".equals(estadoAsistencia) || "Llegó tarde".equals(estadoAsistencia);
        if (!estadoValido) {
            throw new IllegalArgumentException("El estado debe ser Asistió, Faltó o Llegó tarde");
        }
        if (!AsistenciaEvaluacion.esNotaValida(nota)) {
            throw new IllegalArgumentException("La nota debe estar entre 1 y 5");
        }
        crearAsistenciaEvaluacion(estadoAsistencia, nota, comentario, idSocio, idEntrenamiento);
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
    public int golesEnCompetencia(String nombreCompetencia, Socio socio) {
        Competencia competencia = competencias.get(nombreCompetencia);
        int total = 0;
        if (competencia == null || socio == null) {
            return total;
        }
        for (Estadistica estadistica : estadisticas) {
            if (estadistica instanceof EstadisticaFutbol && estadistica.getSocio() == socio
                    && estadistica.getPartido() instanceof PartidoCompetencia) {
                PartidoCompetencia partido = (PartidoCompetencia) estadistica.getPartido();
                if (partido.getCompetencia() == competencia) {
                    total += ((EstadisticaFutbol) estadistica).getGoles();
                }
            }
        }
        return total;
    }

    public Socio goleadorTorneoCompetencia(String nombreCompetencia) {
        Socio goleador = null;
        int maxGoles = 0;
        for (Estadistica estadistica : estadisticas) {
            Socio candidato = estadistica.getSocio();
            int goles = golesEnCompetencia(nombreCompetencia, candidato);
            if (goles > maxGoles) {
                maxGoles = goles;
                goleador = candidato;
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

    // =========================================================================
    // TORNEOS INTERNOS RECREATIVOS (HU-01, HU-02, HU-03, HU-04, HU-05, HU-07, HU-08)
    // =========================================================================

    // HU-01 CA-1 y CA-2: Crear torneo interno recreativo de conjunto
    public TorneoConjunto crearTorneoConjunto(String idAdministrador, String idTorneo, String nombreTorneo,
                                                String nombreDeporte, int categoria, int cuposTotales) {
        Usuario admin = usuarios.get(idAdministrador);
        if (!(admin instanceof Administrador)) {
            throw new IllegalArgumentException("Solo un administrador puede crear torneos internos");
        }
        Deporte deporte = deportes.get(nombreDeporte);
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte seleccionado no está disponible en el club");
        }
        if (deporte.esIndividual()) {
            throw new IllegalArgumentException("El deporte seleccionado no es un deporte de conjunto");
        }
        if (torneos.containsKey(idTorneo)) {
            throw new IllegalArgumentException("Ya existe un torneo con ese identificador");
        }
        TorneoConjunto torneo = new TorneoConjunto(idTorneo, nombreTorneo, deporte, categoria, cuposTotales);
        agregarTorneo(torneo);
        return torneo;
    }

    // Crear torneo interno recreativo individual
    public TorneoIndividual crearTorneoIndividual(String idAdministrador, String idTorneo, String nombreTorneo,
                                                    String nombreDeporte, int categoria, int cuposTotales) {
        Usuario admin = usuarios.get(idAdministrador);
        if (!(admin instanceof Administrador)) {
            throw new IllegalArgumentException("Solo un administrador puede crear torneos internos");
        }
        Deporte deporte = deportes.get(nombreDeporte);
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte seleccionado no está disponible en el club");
        }
        if (!deporte.esIndividual()) {
            throw new IllegalArgumentException("El deporte seleccionado no es una disciplina individual");
        }
        if (torneos.containsKey(idTorneo)) {
            throw new IllegalArgumentException("Ya existe un torneo con ese identificador");
        }
        TorneoIndividual torneo = new TorneoIndividual(idTorneo, nombreTorneo, deporte, categoria, cuposTotales);
        agregarTorneo(torneo);
        return torneo;
    }

    // HU-02 CA-1, CA-2 y CA-3: Finalizar un torneo ('En curso' -> 'Finalizado') y otorgar bonos si aplica
    public void finalizarTorneo(String idAdministrador, String idTorneo, Tienda tienda) {
        Usuario admin = usuarios.get(idAdministrador);
        if (!(admin instanceof Administrador)) {
            throw new IllegalArgumentException("Solo un administrador puede administrar torneos");
        }
        Torneo torneo = torneos.get(idTorneo);
        if (torneo == null) {
            throw new IllegalArgumentException("El torneo no existe");
        }
        if (Torneo.FINALIZADO.equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalArgumentException("El torneo ya ha finalizado");
        }
        if (!Torneo.EN_CURSO.equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalArgumentException("El torneo no se encuentra en curso");
        }

        torneo.setEstado(Torneo.FINALIZADO);
        String primerLugar = torneo.obtenerPrimerLugar();
        if (primerLugar == null) {
            return;
        }
        
        // No se entrega bono si el torneo es competitivo
        if (torneo.isCompetitivo()) {
            boolean ganadorEmpleado = usuarios.get(primerLugar) instanceof Empleado;
            if (!ganadorEmpleado) {
                torneo.otorgarPremio(primerLugar);
            }
            return;
        }

        if (torneo instanceof TorneoConjunto) {
            Equipo equipoGanador = equipos.get(primerLugar);
            if (equipoGanador != null) {
                for (Socio jugador : equipoGanador.getSocios()) {
                    String codigoBono = "BONO-" + torneo.getIdTorneo() + "-" + jugador.getIdUsuario();
                    jugador.agregarBonoDescuento(codigoBono);
                    if (tienda != null) {
                        tienda.registrarBonoTorneo(codigoBono);
                    }
                }
            }
        } else if (torneo instanceof TorneoIndividual) {
            Usuario ganador = usuarios.get(primerLugar);
            if (ganador instanceof Socio) {
                String codigoBono = "BONO-" + torneo.getIdTorneo() + "-" + ganador.getIdUsuario();
                ((Socio) ganador).agregarBonoDescuento(codigoBono);
                if (tienda != null) {
                    tienda.registrarBonoTorneo(codigoBono);
                }
            }
        }
    }

    // Determina si el socio está en el top 20% mejor rankeado de su disciplina individual
    public boolean esSocioMejorRankeado(Socio socio, Deporte deporte, int categoria) {
        List<InscripcionIndividual> elegibles = new ArrayList<>();
        for (InscripcionIndividual insc : inscripcionesIndividual) {
            if (insc.getDeporteIndividual() == deporte && insc.getSocio().isActivo()) {
                if (categoria <= 0 || insc.getSocio().getEdad() <= categoria) {
                    elegibles.add(insc);
                }
            }
        }
        elegibles.sort((a, b) -> Double.compare(b.getPuntajeRanking(), a.getPuntajeRanking()));

        InscripcionIndividual inscSocio = buscarInscripcionIndividual(socio, deporte);
        if (inscSocio == null || inscSocio.getPuntajeRanking() <= 0 || elegibles.isEmpty()) {
            return false;
        }
        int cantidadTop = Math.max(1, (int) Math.ceil(elegibles.size() * 0.20));
        for (int i = 0; i < Math.min(cantidadTop, elegibles.size()); i++) {
            if (elegibles.get(i).getSocio() == socio) {
                return true;
            }
        }
        return false;
    }

    // HU-03 CA-1 a CA-5: Inscripción de un socio a un torneo individual
    public void inscribirSocioTorneoIndividual(String idSocio, String idTorneo) {
        Usuario u = usuarios.get(idSocio);
        if (!(u instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        Socio socio = (Socio) u;
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoIndividual)) {
            throw new IllegalArgumentException("El torneo individual no existe");
        }
        TorneoIndividual torneo = (TorneoIndividual) t;

        if (!Torneo.EN_CURSO.equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalArgumentException("El torneo ya ha finalizado");
        }
        if (torneo.estaParticipanteInscrito(socio)) {
            throw new IllegalArgumentException("El socio ya se encuentra inscrito en el torneo");
        }
        InscripcionIndividual inscripcionDisciplina = buscarInscripcionIndividual(socio, torneo.getDeporte());
        if (inscripcionDisciplina == null) {
            throw new IllegalArgumentException("El socio no está registrado en la disciplina del torneo");
        }
        if (torneo.getCategoria() > 0 && socio.getEdad() > torneo.getCategoria()) {
            throw new IllegalArgumentException("El socio no cumple con la categoría del torneo");
        }

        boolean mejorRankeado = esSocioMejorRankeado(socio, torneo.getDeporte(), torneo.getCategoria());
        if (mejorRankeado && torneo.getCuposPreferenciales() > 0) {
            torneo.inscribirParticipante(socio, true);
        } else if (torneo.getCuposGenerales() > 0) {
            torneo.inscribirParticipante(socio, false);
        } else {
            throw new IllegalArgumentException("No hay cupos generales disponibles");
        }
    }

    // HU-04 CA-1 y CA-2: Desinscripción de un socio de un torneo individual
    public void desinscribirSocioTorneoIndividual(String idSocio, String idTorneo) {
        Usuario u = usuarios.get(idSocio);
        if (!(u instanceof Socio)) {
            throw new IllegalArgumentException("El socio no existe");
        }
        Socio socio = (Socio) u;
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoIndividual)) {
            throw new IllegalArgumentException("El torneo individual no existe");
        }
        TorneoIndividual torneo = (TorneoIndividual) t;

        if (!torneo.estaParticipanteInscrito(socio)) {
            throw new IllegalArgumentException("El socio no está inscrito en el torneo");
        }

        boolean liberarPreferencial = torneo.usoCupoPreferencial(socio.getIdUsuario())
                || (!torneo.tieneRegistroTipoCupo(socio.getIdUsuario())
                        && esSocioMejorRankeado(socio, torneo.getDeporte(), torneo.getCategoria()));
        torneo.desinscribirParticipante(socio, liberarPreferencial);
    }

    // Determina si un equipo está en el top 20% mejor rankeado de su deporte y categoría
    public boolean esEquipoMejorRankeado(Equipo equipo) {
        List<Equipo> mismaCategoria = new ArrayList<>();
        for (Equipo eq : equipos.values()) {
            if (eq.getDeporteEquipo() == equipo.getDeporteEquipo()
                    && eq.getCategoriaEdad() == equipo.getCategoriaEdad()) {
                mismaCategoria.add(eq);
            }
        }
        mismaCategoria.sort((a, b) -> Double.compare(b.obtenerPuntajeRankingTotal(), a.obtenerPuntajeRankingTotal()));

        if (equipo.obtenerPuntajeRankingTotal() <= 0 || mismaCategoria.isEmpty()) {
            return false;
        }
        int cantidadTop = Math.max(1, (int) Math.ceil(mismaCategoria.size() * 0.20));
        for (int i = 0; i < Math.min(cantidadTop, mismaCategoria.size()); i++) {
            if (mismaCategoria.get(i) == equipo) {
                return true;
            }
        }
        return false;
    }

    // HU-07 CA-1 a CA-5: Entrenador inscribe a su equipo en un torneo de conjunto
    public void inscribirEquipoTorneoConjunto(String idEntrenador, String idTorneo, String nombreEquipo) {
        Usuario u = usuarios.get(idEntrenador);
        if (!(u instanceof Entrenador)) {
            throw new IllegalArgumentException("Solo un entrenador puede inscribir equipos a un torneo");
        }
        Entrenador entrenador = (Entrenador) u;
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoConjunto)) {
            throw new IllegalArgumentException("El torneo de conjunto no existe");
        }
        TorneoConjunto torneo = (TorneoConjunto) t;
        Equipo equipo = equipos.get(nombreEquipo);
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no existe");
        }

        if (!Torneo.EN_CURSO.equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalArgumentException("El torneo ya ha finalizado");
        }
        if (torneo.estaEquipoInscrito(equipo)) {
            throw new IllegalArgumentException("El equipo ya se encuentra inscrito en el torneo");
        }
        if (torneo.getDeporte() != equipo.getDeporteEquipo() || torneo.getCategoria() != equipo.getCategoriaEdad()) {
            throw new IllegalArgumentException("El deporte o la categoría del torneo no corresponden a la del equipo que se intentó inscribir");
        }

        boolean mejorRankeado = esEquipoMejorRankeado(equipo);
        if (mejorRankeado && torneo.getCuposPreferenciales() > 0) {
            torneo.inscribirEquipo(equipo, entrenador, true);
        } else if (torneo.getCuposGenerales() > 0) {
            torneo.inscribirEquipo(equipo, entrenador, false);
        } else {
            throw new IllegalArgumentException("No quedan cupos generales disponibles en el torneo");
        }
    }

    // HU-08 CA-1, CA-2 y CA-3: Empleado (entrenador o fisioterapeuta) se inscribe a un torneo individual
    public void inscribirEmpleadoTorneoIndividual(String idEmpleado, String idTorneo) {
        Usuario u = usuarios.get(idEmpleado);
        if (!(u instanceof Empleado)) {
            throw new IllegalArgumentException("Solo un empleado (entrenador o fisioterapeuta) puede realizar esta inscripción");
        }
        Empleado empleado = (Empleado) u;
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoIndividual)) {
            throw new IllegalArgumentException("El torneo individual no existe");
        }
        TorneoIndividual torneo = (TorneoIndividual) t;

        if (!Torneo.EN_CURSO.equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalArgumentException("El torneo ya ha finalizado");
        }
        if (torneo.estaParticipanteInscrito(empleado)) {
            throw new IllegalArgumentException("El empleado ya se encuentra inscrito en el torneo");
        }
        if (torneo.getCuposGenerales() <= 0) {
            throw new IllegalArgumentException("No quedan cupos generales disponibles en el torneo");
        }
        torneo.inscribirParticipante(empleado, false);
    }

    public void registrarPartidoTorneoConjunto(String idTorneo, String idPartido, String fecha,
                                               String nombreEquipo1, String nombreEquipo2, String marcador) {
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoConjunto)) {
            throw new IllegalArgumentException("El torneo de conjunto no existe");
        }
        TorneoConjunto torneo = (TorneoConjunto) t;
        Equipo e1 = equipos.get(nombreEquipo1);
        Equipo e2 = equipos.get(nombreEquipo2);
        if (!torneo.estaEquipoInscrito(e1) || !torneo.estaEquipoInscrito(e2)) {
            throw new IllegalArgumentException("Ambos equipos deben estar inscritos en el torneo");
        }
        int[] goles = separarMarcador(marcador);
        Deporte deporte = torneo.getDeporte();
        if (goles[0] == goles[1] && !deporte.isPermiteEmpate()) {
            throw new IllegalArgumentException("En este deporte no se permiten empates");
        }
        PartidoInterno partido = new PartidoInterno(idPartido, fecha, goles[0] + "-" + goles[1], "Finalizado", deporte, e1, e2);
        agregarPartido(partido);
        torneo.agregarPartido(partido);

        if (goles[0] > goles[1]) {
            torneo.sumarPuntosParticipante(e1.getNombreEquipo(), deporte.getPuntosVictoria());
            torneo.sumarPuntosParticipante(e2.getNombreEquipo(), deporte.getPuntosDerrota());
        } else if (goles[0] < goles[1]) {
            torneo.sumarPuntosParticipante(e2.getNombreEquipo(), deporte.getPuntosVictoria());
            torneo.sumarPuntosParticipante(e1.getNombreEquipo(), deporte.getPuntosDerrota());
        } else {
            torneo.sumarPuntosParticipante(e1.getNombreEquipo(), deporte.getPuntosEmpate());
            torneo.sumarPuntosParticipante(e2.getNombreEquipo(), deporte.getPuntosEmpate());
        }
    }

    public void registrarPartidoTorneoIndividual(String idTorneo, String idPartido, String fecha,
                                                 String idJugador1, String idJugador2, int sets1, int sets2) {
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoIndividual)) {
            throw new IllegalArgumentException("El torneo individual no existe");
        }
        TorneoIndividual torneo = (TorneoIndividual) t;
        Usuario j1 = usuarios.get(idJugador1);
        Usuario j2 = usuarios.get(idJugador2);
        if (!torneo.estaParticipanteInscrito(j1) || !torneo.estaParticipanteInscrito(j2)) {
            throw new IllegalArgumentException("Ambos participantes deben estar inscritos en el torneo");
        }
        validarNoNegativos(sets1, sets2);
        if (sets1 == sets2) {
            throw new IllegalArgumentException("En los deportes individuales no hay empates");
        }
        Deporte deporte = torneo.getDeporte();
        PartidoIndividual partido = new PartidoIndividual(idPartido, fecha, sets1 + "-" + sets2, "Finalizado", deporte, j1, j2);
        agregarPartido(partido);
        torneo.agregarPartido(partido);

        Usuario ganador = sets1 > sets2 ? j1 : j2;
        Usuario perdedor = sets1 > sets2 ? j2 : j1;
        torneo.sumarPuntosParticipante(ganador.getIdUsuario(), deporte.getPuntosVictoria());
        torneo.sumarPuntosParticipante(perdedor.getIdUsuario(), deporte.getPuntosDerrota());
    }

    // HU-05 CA-1, CA-2 y CA-3: Consultar torneos en los que participa o participó un usuario
    public List<Torneo> obtenerTorneosDeParticipante(String idParticipante) {
        Usuario u = usuarios.get(idParticipante);
        if (u == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        Set<Torneo> asociados = new LinkedHashSet<>();
        for (Torneo torneo : torneos.values()) {
            if (torneo instanceof TorneoIndividual) {
                if (((TorneoIndividual) torneo).estaParticipanteInscrito(u)) {
                    asociados.add(torneo);
                }
            } else if (torneo instanceof TorneoConjunto) {
                TorneoConjunto tc = (TorneoConjunto) torneo;
                if (u instanceof Socio) {
                    for (Equipo eq : tc.getEquiposInscritos()) {
                        if (eq.getSocios().contains(u)) {
                            asociados.add(torneo);
                        }
                    }
                } else if (u instanceof Entrenador) {
                    if (tc.getEntrenadoresInscriptores().containsValue(u)) {
                        asociados.add(torneo);
                    }
                    for (Equipo eq : tc.getEquiposInscritos()) {
                        if (eq.getEntrenador() == u) {
                            asociados.add(torneo);
                        }
                    }
                }
            }
        }
        if (asociados.isEmpty()) {
            throw new IllegalArgumentException("No hay ningún torneo por consultar");
        }
        return new ArrayList<>(asociados);
    }

    // HU-05 CA-1 y CA-2: Consultar el historial detallado (encuentros, participantes, posiciones)
    public String consultarHistorialTorneo(String idParticipante, String idTorneo) {
        List<Torneo> misTorneos = obtenerTorneosDeParticipante(idParticipante);
        Torneo seleccionado = null;
        for (Torneo t : misTorneos) {
            if (t.getIdTorneo().equals(idTorneo)) {
                seleccionado = t;
                break;
            }
        }
        if (seleccionado == null) {
            throw new IllegalArgumentException("No se encuentra inscrito en el torneo seleccionado");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTORIAL DEL TORNEO: ").append(seleccionado.getNombre())
          .append(" (").append(seleccionado.getIdTorneo()).append(") ===\n");
        sb.append("Deporte: ").append(seleccionado.getDeporte().getNombreDeporte())
          .append(" | Categoría: ").append(seleccionado.getCategoria())
          .append(" | Estado: ").append(seleccionado.getEstado()).append("\n\n");
        
        if (seleccionado.isCompetitivo()) {
            sb.append("Modalidad: Competitivo | Tarifa de inscripción: $").append(Math.round(seleccionado.getTarifaInscripcion()))
              .append(" | Total recaudado: $").append(Math.round(seleccionado.getTotalRecaudado()));
            if (Torneo.FINALIZADO.equalsIgnoreCase(seleccionado.getEstado())) {
                if (seleccionado.getGanadorPremio() == null) {
                    sb.append(" | Premio: no se otorgó");
                } else {
                    sb.append(" | Premio en efectivo: $").append(Math.round(seleccionado.getPremio()));
                }
            } else {
                sb.append(" | Premio estimado: $").append(Math.round(seleccionado.calcularPremio()));
            }
            sb.append("\n\n");
        } else {
            sb.append("Modalidad: Recreativo (sin costo)\n\n");
        }
        
        sb.append("PARTICIPANTES:\n");
        if (seleccionado instanceof TorneoIndividual) {
            for (Usuario p : ((TorneoIndividual) seleccionado).getParticipantesInscritos()) {
                sb.append("- ").append(p.getNombre()).append(" ").append(p.getApellido())
                  .append(" (").append(p.getClass().getSimpleName()).append(")\n");
            }
        } else if (seleccionado instanceof TorneoConjunto) {
            for (Equipo eq : ((TorneoConjunto) seleccionado).getEquiposInscritos()) {
                sb.append("- ").append(eq.getNombreEquipo()).append(" (").append(eq.getSocios().size()).append(" jugadores)\n");
            }
        }

        sb.append("\nENCUENTROS:\n");
        if (seleccionado.getListaPartidos().isEmpty()) {
            sb.append("- Sin encuentros registrados hasta la fecha.\n");
        } else {
            for (Partido p : seleccionado.getListaPartidos()) {
                sb.append("- ").append(describirPartido(p)).append("\n");
            }
        }

        sb.append("\nTABLA DE POSICIONES:\n");
        int puesto = 1;
        for (String clave : seleccionado.obtenerPosicionesOrdenadas()) {
            String nombreMostrar = clave;
            if (seleccionado instanceof TorneoIndividual && usuarios.containsKey(clave)) {
                Usuario u = usuarios.get(clave);
                nombreMostrar = u.getNombre() + " " + u.getApellido() + " (" + u.getIdUsuario() + ")";
            }
            int puntos = seleccionado.getTablaPosiciones().get(clave);
            sb.append(puesto++).append(". ").append(nombreMostrar).append(" - ").append(puntos).append(" pts\n");
        }
        return sb.toString();
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

    // ENUNCIADO PROYECTO 2
    // Diferenciar un torneo competitivo de uno recreativo
    public TorneoConjunto crearTorneoConjuntoCompetitivo(String idAdministrador, String idTorneo, String nombreTorneo,
            String nombreDeporte, int categoria, int cuposTotales, double tarifa) {
		if (tarifa <= 0) {
		throw new IllegalArgumentException("La tarifa de inscripción debe ser mayor a cero");
		}
		TorneoConjunto torneo = crearTorneoConjunto(idAdministrador, idTorneo, nombreTorneo, nombreDeporte, categoria, cuposTotales);
		torneo.convertirEnCompetitivo(tarifa);
		return torneo;
		}
		
	public TorneoIndividual crearTorneoIndividualCompetitivo(String idAdministrador, String idTorneo, String nombreTorneo,
		                String nombreDeporte, int categoria, int cuposTotales, double tarifa) {
		if (tarifa <= 0) {
		throw new IllegalArgumentException("La tarifa de inscripción debe ser mayor a cero");
		}
		TorneoIndividual torneo = crearTorneoIndividual(idAdministrador, idTorneo, nombreTorneo, nombreDeporte, categoria, cuposTotales);
		torneo.convertirEnCompetitivo(tarifa);
		return torneo;
		}
    
    // Un empleado o un invitado se sale de un torneo individual: se libera un cupo general
    // y en un torneo competitivo, se descuenta la tarifa (los empleados no pagan).
    public void desinscribirParticipanteTorneoIndividual(String idUsuario, String idTorneo) {
        Usuario usuario = usuarios.get(idUsuario);
        if (!(usuario instanceof Empleado) && !(usuario instanceof Invitado)) {
            throw new IllegalArgumentException("Esta opción solo aplica a empleados e invitados");
        }
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoIndividual)) {
            throw new IllegalArgumentException("El torneo individual no existe");
        }
        TorneoIndividual torneo = (TorneoIndividual) t;
        if (!Torneo.EN_CURSO.equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalArgumentException("El torneo ya ha finalizado");
        }
        if (!torneo.estaParticipanteInscrito(usuario)) {
            throw new IllegalArgumentException("No está inscrito en el torneo");
        }
        torneo.desinscribirParticipante(usuario, false);
    }
    
    // METODOS QUE GESTIONAN A LOS INVITADOS
    
    public void crearInvitado(String idUsuario, String nombre, String apellido, int edad) {
        Invitado invitado = new Invitado(idUsuario, nombre, apellido, "invitado-" + idUsuario, "", 0, edad);
        agregarUsuario(invitado);
    }

    // El administrador inscribe a un invitado (estudiante o persona externa) en un torneo individual.
    public void inscribirInvitadoTorneoIndividual(String idAdministrador, String idInvitado, String idTorneo) {
        Usuario admin = usuarios.get(idAdministrador);
        if (!(admin instanceof Administrador)) {
            throw new IllegalArgumentException("Solo un administrador puede inscribir invitados");
        }
        Usuario usuario = usuarios.get(idInvitado);
        if (!(usuario instanceof Invitado)) {
            throw new IllegalArgumentException("El invitado no existe");
        }
        Invitado invitado = (Invitado) usuario;
        Torneo t = torneos.get(idTorneo);
        if (!(t instanceof TorneoIndividual)) {
            throw new IllegalArgumentException("El torneo individual no existe");
        }
        TorneoIndividual torneo = (TorneoIndividual) t;
        if (!Torneo.EN_CURSO.equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalArgumentException("El torneo ya ha finalizado");
        }
        if (torneo.estaParticipanteInscrito(invitado)) {
            throw new IllegalArgumentException("El invitado ya se encuentra inscrito en el torneo");
        }
        if (torneo.getCategoria() > 0 && invitado.getEdad() > torneo.getCategoria()) {
            throw new IllegalArgumentException("El invitado no cumple con la categoría del torneo");
        }
        if (torneo.getCuposGenerales() <= 0) {
            throw new IllegalArgumentException("No quedan cupos generales disponibles en el torneo");
        }
        torneo.inscribirParticipante(invitado, false);
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
    public Map<String, Torneo> getTorneos() { return torneos; }
    public Map<String, SesionEntrenamiento> getSesionesEntrenamiento() { return sesionesEntrenamiento; }
    public List<AsistenciaEvaluacion> getAsistenciaEvaluaciones() { return asistenciaEvaluaciones; }
    public Map<String, Turno> getTurnos() { return turnos; }
    public List<SolicitudCambioTurnos> getSolicitudesCambioTurno() { return solicitudesCambioTurno; }
    public List<Estadistica> getEstadisticas() { return estadisticas; }
}