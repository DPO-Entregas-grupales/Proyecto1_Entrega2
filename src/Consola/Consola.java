package Consola;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import Modelo.ClubDeportivoSeneca;
import Modelo.Tienda;
import Modelo.Autentificador;
import Usuarios.Usuario;
import Usuarios.Socio;
import Usuarios.Administrador;
import Usuarios.Entrenador;
import Usuarios.Fisioterapeuta;
import Tienda.ArticuloTienda;
import Tienda.ProductoCafeteria;
import Deportes.Equipo;
import Tienda.ItemVenta;
import Tienda.Producto;
import Tienda.Venta;
import Usuarios.Empleado;
import Partidos.Partido;
import Partidos.PartidoCompetencia;
import Partidos.PartidoInterno;
import ReservasInstalaciones.Instalacion;
import SesionEntrenamiento.SesionEntrenamiento;
import Turnos.Turno;
import Deportes.Deporte;
import Deportes.InscripcionIndividual;
import Partidos.PartidoIndividual;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.time.LocalDateTime;

import Partidos.Torneo;
import Partidos.TorneoConjunto;
import Partidos.TorneoIndividual;
import Tienda.CalculadoraDescuento;
import Usuarios.Invitado;
import ReservasInstalaciones.Reserva;

public class Consola {

    private static final String RUTA_CLUB = "./datos/ClubDeportivo.json";
    private static final String RUTA_TIENDA = "./datos/Tienda.json";
    private static final String RUTA_USUARIOS = "./datos/Usuarios.json";
    private final ClubDeportivoSeneca club;
    private final Tienda tienda;
    private final Autentificador autentificador;
    private final Scanner scanner;
    private Usuario usuarioAutenticado;
    private boolean salirApp;

    // Contadores para generación automática de IDs secuenciales
    private int contadorUsuarios = 1;
    private int contadorReservas = 1;
    private int contadorPartidos = 1;

    public Consola() {
        this.club = new ClubDeportivoSeneca();
        this.tienda = new Tienda();
        this.autentificador = new Autentificador();
        this.scanner = new Scanner(System.in);
        this.usuarioAutenticado = null;
        this.salirApp = false;
    }

    public static void main(String[] args) {
        Consola consola = new Consola();
        consola.iniciar();
    }

    public void iniciar() {
        club.cargarUsuarios(RUTA_USUARIOS);
        club.cargarClubDeportivo(RUTA_CLUB);
        tienda.cargarTienda(RUTA_TIENDA, club);
        registrarCodigosDescuento();
        
        contadorUsuarios = obtenerMaxId(club.getUsuarios().keySet()) + 1;
        contadorReservas = obtenerMaxId(club.getReservas().keySet()) + 1;
        contadorPartidos = obtenerMaxId(club.getPartidos().keySet()) + 1;

        System.out.println("==========================================");
        System.out.println("  BIENVENIDO AL CLUB DEPORTIVO SÉNECA");
        System.out.println("==========================================");

        if (club.getUsuarios().isEmpty()) {
            System.out.println("No hay usuarios registrados. Agregue un administrador en " + RUTA_USUARIOS + " y reinicie la aplicación.");
            return;
        }

        while (!salirApp) {
            if (usuarioAutenticado == null) {
                menuAutenticacion();
            } else {
                menuPrincipal();
            }
        }
        System.out.println("Aplicación finalizada.");
    }

    // ==========================================
    // FLUJO DE AUTENTICACIÓN Y SESIÓN
    // ==========================================

    private void menuAutenticacion() {
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.println("1. Iniciar Sesion");
        System.out.println("0. Salir de la aplicacion");

        int opcion = leerEntero("Seleccione una opcion: ");
        switch (opcion) {
            case 1 -> {
                String login = leerTexto("Login: ");
                String pass = leerTexto("Contraseña: ");

                List<Usuario> listaUsuarios = new ArrayList<>(club.getUsuarios().values());
                Usuario u = autentificador.autenticar(login, pass, listaUsuarios);
                if (u instanceof Invitado) {
                    System.out.println("Los invitados no tienen acceso al sistema.");
                } else if (u != null && u instanceof Socio && !((Socio) u).isActivo()) {
                    System.out.println("Su cuenta está dada de baja.");
                } else if (u != null) {
                    this.usuarioAutenticado = u;
                    System.out.println("\n>>> Bienvenido, " + u.getNombre() + " " + u.getApellido() + " <<<");
                } else {
                    System.out.println("Login o contraseña incorrectos.");
                } 
            }
            case 0 -> salir();
            default -> System.out.println("Opcion inválida.");
        }
    }

    private void cerrarSesion() {
        System.out.println("Sesion cerrada para: " + usuarioAutenticado.getNombre());
        this.usuarioAutenticado = null;
    }

    private void salir() {
        guardarTodo();
        salirApp = true;
    }

    // Guarda los tres archivos. Se invoca después de cada operación que modifica datos.
    private void guardarTodo() {
        club.salvarUsuarios(RUTA_USUARIOS);
        club.salvarClubDeportivo(RUTA_CLUB);
        tienda.salvarTienda(RUTA_TIENDA);
    }

    // ==========================================
    // MENÚ SEGÚN EL TIPO DE USUARIO
    // ==========================================

    private void menuPrincipal() {
        if (usuarioAutenticado instanceof Administrador) {
            menuAdministrador();
        } else if (usuarioAutenticado instanceof Entrenador) {
            menuEntrenador();
        } else if (usuarioAutenticado instanceof Fisioterapeuta) {
            menuFisioterapeuta();
        } else {
            menuSocio();
        }
    }

    private void encabezadoSesion(String rol) {
        System.out.println("\n==========================================");
        System.out.println("  SESION ACTIVA: " + usuarioAutenticado.getNombre() + " " + usuarioAutenticado.getApellido() + " (" + rol + ")");
        System.out.println("==========================================");
        
        System.out.println("  Puntos de fidelidad: " + usuarioAutenticado.getPuntosFidelidad());
    }

    // ---------- Administrador ----------

    private void menuAdministrador() {
        encabezadoSesion("Administrador");
        System.out.println("1. Gestión de usuarios");
        System.out.println("2. Deportes, equipos y competencias");
        System.out.println("3. Instalaciones");
        System.out.println("4. Gestión de la tienda y la cafetería");
        System.out.println("5. Programar partidos");
        System.out.println("6. Comprar en la tienda deportiva");
        System.out.println("7. Comprar en la cafetería");
        System.out.println("8. Gestión de turnos");
        System.out.println("9. Cerrar sesión");
        System.out.println("10. Reportes y consultas");
        System.out.println("11. Gestión de torneos");
        System.out.println("0. Salir de la aplicación");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> menuUsuarios();
            case 2 -> menuDeportesYEquipos();
            case 3 -> menuInstalaciones();
            case 4 -> menuTienda();
            case 5 -> menuProgramarPartidos();
            case 6 -> comprar(Venta.TIENDADEPORTIVA);
            case 7 -> comprar(Venta.CAFETERIA);
            case 8 -> menuTurnos();
            case 9 -> cerrarSesion();
            case 10 -> menuReportes();
            case 11 -> menuTorneos();
            case 0 -> salir();
            default -> System.out.println("Opción no válida.");
        }
    }

    private void menuUsuarios() {
        System.out.println("\n--- GESTIÓN DE USUARIOS ---");
        System.out.println("1. Registrar Socio");
        System.out.println("2. Registrar Entrenador");
        System.out.println("3. Registrar Fisioterapeuta");
        System.out.println("4. Registrar Administrador");
        System.out.println("5. Consultar Usuario");
        System.out.println("6. Dar de baja un socio");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> registrarSocio();
            case 2 -> registrarEntrenador();
            case 3 -> registrarFisioterapeuta();
            case 4 -> registrarAdministrador();
            case 5 -> {
                String id = leerTexto("ID a consultar: ");
                Usuario u = club.getUsuarios().get(id);
                if (u != null) {
                    System.out.println("Encontrado: " + u.getNombre() + " " + u.getApellido() + " (" + u.getClass().getSimpleName() + ")");
                } else {
                    System.out.println("Usuario no encontrado.");
                }
            }
            case 6 -> {
                String id = leerTexto("ID del socio: ");
                try {
                    club.realizarBajaJugador(id);
                    guardarTodo();
                    System.out.println("Socio dado de baja.");
                } catch (IllegalArgumentException e) {
                    System.out.println("No se pudo dar de baja: " + e.getMessage());
                }
            }
        }
    }

    private void menuDeportesYEquipos() {
        System.out.println("\n--- DEPORTES, EQUIPOS Y COMPETENCIAS ---");
        System.out.println("1. Crear Deporte");
        System.out.println("2. Crear Equipo");
        System.out.println("3. Crear Competencia");
        System.out.println("4. Inscribir Equipo en Competencia");
        System.out.println("5. Consultar goleador de una competencia");
        System.out.println("6. Dar de baja un equipo");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String nombre = leerTexto("Nombre deporte: ");
                int minJugadores = leerEntero("Mínimo jugadores: ");
                boolean empate = leerTexto("¿Permite empate? (s/n): ").equalsIgnoreCase("s");
                int pVictoria = leerEntero("Puntos victoria: ");
                int pDerrota = leerEntero("Puntos derrota: ");
                int pEmpate = leerEntero("Puntos empate: ");
                
                String textoPosiciones = leerTexto("Posiciones (separadas por coma, vacío si no aplica): ");
                List<String> posiciones = new ArrayList<>();
                if (!textoPosiciones.isEmpty()) {
                    for (String pos : textoPosiciones.split(",")) {
                        posiciones.add(pos.trim());
                    }
                }
                club.crearDeporte(nombre, minJugadores, posiciones, empate, pVictoria, pDerrota, pEmpate);
                guardarTodo();
                System.out.println("Deporte creado.");
            }
            case 2 -> {
                String nombre = leerTexto("Nombre equipo: ");
                int cupo = leerEntero("Cupo máximo: ");
                int catEdad = leerEntero("Edad máxima de la categoría (13, 15, 17 o 99 para Mayores): ");
                String deporte = leerTexto("Nombre deporte: ");
                club.crearEquipo(nombre, cupo, catEdad, deporte, true);
                guardarTodo();
                System.out.println("Equipo creado.");
            }
            case 3 -> {
                String nombre = leerTexto("Nombre competencia: ");
                String entidad = leerTexto("Entidad organizadora: ");
                String anio = leerTexto("Año/Temporada: ");
                String formato = leerTexto("Formato: ");
                String fase = leerTexto("Fase actual: ");
                int cat = leerEntero("Categoría (edad máxima: 13, 15, 17 o 99 para Mayores: ");
                club.crearCompetencia(nombre, entidad, anio, formato, fase, cat);
                guardarTodo();
                System.out.println("Competencia creada.");
            }
            case 4 -> {
                String equipo = leerTexto("Nombre Equipo: ");
                String competencia = leerTexto("Nombre Competencia: ");
                List<Socio> nomina = pedirNominaJugadores();
                try {
                    club.realizarInscripcionCompetencia(0, "-", equipo, competencia, nomina);
                    guardarTodo();
                    System.out.println("Equipo inscrito en la competencia.");
                } catch (IllegalArgumentException e) {
                    System.out.println("No se pudo inscribir el equipo: " + e.getMessage());
                }
            }
            case 5 -> {
                String competencia = leerTexto("Nombre de la competencia: ");
                Socio goleador = club.goleadorTorneoCompetencia(competencia);
                if (goleador == null) {
                    System.out.println("Todavía no hay goles registrados en esa competencia.");
                } else {
                    System.out.println("Goleador: " + goleador.getNombre() + " " + goleador.getApellido()
                            + " con " + club.golesEnCompetencia(competencia, goleador) + " goles.");
                }
            }
            case 6 -> {
                String nombre = leerTexto("Nombre del equipo: ");
                try {
                    club.realizarBajaEquipo(nombre);
                    guardarTodo();
                    System.out.println("Equipo dado de baja.");
                } catch (IllegalArgumentException e) {
                    System.out.println("No se pudo dar de baja: " + e.getMessage());
                }
            }
        }
    }

    private void menuInstalaciones() {
        System.out.println("\n--- INSTALACIONES ---");
        System.out.println("1. Registrar Instalación");
        System.out.println("2. Generar Horarios");
        System.out.println("3. Definir el máximo de instalaciones simultáneas de un deporte");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String nombre = leerTexto("Nombre instalación: ");
                String tipo = leerTexto("Tipo: ");
                boolean techada = leerTexto("¿Techada? (s/n): ").equalsIgnoreCase("s");
                int cap = leerEntero("Capacidad: ");
                String deporte = leerTexto("Deporte de la instalación (por ejemplo Tenis o Pádel): ");
                if (club.getDeportes().get(deporte) == null) {
                    System.out.println("El deporte no existe.");
                    return;
                }
                club.crearInstalacion(nombre, tipo, techada, cap, deporte);
                guardarTodo();
                System.out.println("Instalación creada.");
            }
            case 2 -> {
                String nombre = leerTexto("Instalación: ");
                String fecha = leerTexto("Fecha (YYYY-MM-DD): ");
                int hInicio = leerEntero("Hora inicio (0-23): ");
                int hFin = leerEntero("Hora fin (0-23): ");
                club.nuevosHorarios(nombre, fecha, hInicio, hFin);
                guardarTodo();
                System.out.println("Horarios agregados.");
            }
            case 3 -> {
                System.out.println("Deportes:");
                for (Deporte d : club.getDeportes().values()) {
                    System.out.println("- " + d.getNombreDeporte() + " | máximo actual: " + d.getMaxInstalacionesSimultaneas()
                            + " (0 = sin límite)");
                }
                String deporte = leerTexto("Deporte: ");
                int maximo = leerEntero("Máximo de instalaciones simultáneas (0 = sin límite): ");
                try {
                    club.definirMaximoInstalacionesSimultaneas(deporte, maximo);
                    guardarTodo();
                    System.out.println("Máximo actualizado.");
                } catch (IllegalArgumentException e) {
                    System.out.println("No se pudo definir el máximo: " + e.getMessage());
                }
            }
        }
    }

    private void menuTienda() {
        System.out.println("\n--- GESTIÓN DE LA TIENDA Y LA CAFETERÍA ---");
        System.out.println("1. Crear Artículo de Tienda");
        System.out.println("2. Crear Producto de Cafetería");
        System.out.println("3. Ver Catálogo Tienda");
        System.out.println("4. Ver Catálogo Cafetería");
        System.out.println("5. Reabastecer un producto");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String nombre = leerTexto("Nombre artículo: ");
                double precio = leerDouble("Precio base: ");
                int cant = leerEntero("Stock: ");
                String cat = leerTexto("Categoría: ");
                tienda.crearArticuloTienda(nombre, precio, cant, cat);
                guardarTodo();
                System.out.println("Artículo registrado.");
            }
            case 2 -> {
                String nombre = leerTexto("Nombre producto: ");
                double precio = leerDouble("Precio base: ");
                int cant = leerEntero("Stock: ");
                boolean caliente = leerTexto("¿Bebida caliente? (s/n): ").equalsIgnoreCase("s");
                String alergenos = leerTexto("Alérgenos: ");
                tienda.crearProductoCafeteria(nombre, precio, cant, caliente, alergenos);
                guardarTodo();
                System.out.println("Producto registrado.");
            }
            case 3 -> {
                System.out.println("\nArtículos Tienda:");
                for (ArticuloTienda a : tienda.obtenerArticulosTienda().values()) {
                    System.out.println("- " + a.getNombre() + " | $" + a.getPrecioBase() + " | Stock: " + a.getCantidadInventario());
                }
            }
            case 4 -> {
                System.out.println("\nProductos Cafetería:");
                for (ProductoCafeteria p : tienda.obtenerProductosCafeteria().values()) {
                    System.out.println("- " + p.getNombre() + " | $" + p.getPrecioBase() + " | Stock: " + p.getCantidadInventario());
                }
            }
            case 5 -> {
                String nombre = leerTexto("Nombre del producto: ");
                int cantidad = leerEntero("Cantidad a agregar: ");
                try {
                    tienda.reabastecer(nombre, cantidad);
                    guardarTodo();
                    System.out.println("Inventario actualizado.");
                } catch (IllegalArgumentException e) {
                    System.out.println("No se pudo reabastecer: " + e.getMessage());
                }
            }
        }
    }

    private void menuProgramarPartidos() {
        System.out.println("\n--- PROGRAMAR PARTIDOS ---");
        System.out.println("1. Crear Partido Interno");
        System.out.println("2. Crear Partido Competencia");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String id = String.valueOf(contadorPartidos++);
                String fecha = leerTexto("Fecha: ");
                String e1 = leerTexto("Equipo 1: ");
                String e2 = leerTexto("Equipo 2: ");
                club.crearPartidoInterno(id, fecha, "0-0", "Programado", e1, e2);
                guardarTodo();
                System.out.println("Partido interno programado (ID: " + id + ").");
            }
            case 2 -> {
                String id = String.valueOf(contadorPartidos++);
                String fecha = leerTexto("Fecha: ");
                String equipo = leerTexto("Equipo Local: ");
                String rival = leerTexto("Rival: ");
                String escenario = leerTexto("Escenario: ");
                club.crearPartidoCompetencia(id, fecha, "0-0", "Programado", equipo, rival, escenario);
                guardarTodo();
                System.out.println("Partido de competencia programado (ID: " + id + ").");
            }
        }
    }

    //MENU DEL SOCIO
    private void menuSocio() {
        encabezadoSesion("Socio");
        System.out.println("1. Equipos y deportes");
        System.out.println("2. Partidos de deportes individuales");
        System.out.println("3. Reservar una instalación");
        System.out.println("4. Comprar en la tienda deportiva");
        System.out.println("5. Comprar en la cafetería");
        System.out.println("6. Torneos");
        System.out.println("7. Comprar en la tienda deportiva con un bono");
        System.out.println("8. Ingresar a una instalación reservada");
        System.out.println("9. Cerrar sesión");
        System.out.println("0. Salir de la aplicación");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> menuSocioDeportes();
            case 2 -> menuSocioPartidos();
            case 3 -> reservarInstalacion();
            case 4 -> comprar(Venta.TIENDADEPORTIVA);
            case 5 -> comprar(Venta.CAFETERIA);
            case 6 -> menuTorneosSocio();
            case 7 -> comprarConBono();
            case 8 -> ingresarAInstalacionReservada();
            case 9 -> cerrarSesion();
            case 0 -> salir();
            default -> System.out.println("Opción no válida.");
        }
    }

    private void menuSocioDeportes() {
        System.out.println("\n--- EQUIPOS Y DEPORTES ---");
        System.out.println("1. Inscribirme a un equipo");
        System.out.println("2. Inscribirme a un deporte individual");
        System.out.println("3. Ver mi nivel y ranking");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> inscribirseAEquipo();
            case 2 -> inscribirseADeporteIndividual();
            case 3 -> verMiRanking();
        }
    }

    private void menuSocioPartidos() {
        System.out.println("\n--- PARTIDOS INDIVIDUALES ---");
        System.out.println("1. Programar un partido");
        System.out.println("2. Registrar el resultado de un partido");
        System.out.println("3. Registrar mis estadísticas de un partido");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> programarPartidoIndividual();
            case 2 -> registrarResultadoIndividual();
            case 3 -> registrarEstadisticasIndividual();
        }
    }

    private void inscribirseADeporteIndividual() {
        System.out.println("\n--- INSCRIPCIÓN A UN DEPORTE INDIVIDUAL ---");
        System.out.println("Deportes individuales:");
        for (Deporte d : club.getDeportes().values()) {
            if (d.esIndividual()) {
                System.out.println("- " + d.getNombreDeporte());
            }
        }
        String deporte = leerTexto("Nombre del deporte: ");
        try {
            club.realizarInscripcionIndividual(usuarioAutenticado.getIdUsuario(), deporte);
            guardarTodo();
            System.out.println("Inscripción registrada con nivel Principiante.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la inscripción: " + e.getMessage());
        }
    }

    private void verMiRanking() {
        Socio socio = (Socio) usuarioAutenticado;
        if (socio.getInscripcionesIndividuales().isEmpty()) {
            System.out.println("No está inscrito en deportes individuales.");
        }
        for (InscripcionIndividual i : socio.getInscripcionesIndividuales()) {
            System.out.println("- " + i.getDeporteIndividual().getNombreDeporte() + " | nivel: " + i.getNivel() + " | ranking: " + i.getPuntajeRanking());
        }
    }

    // Lista los partidos individuales del socio en el estado indicado. Devuelve falso si no hay ninguno
    private boolean listarPartidosIndividuales(String estado) {
        boolean hay = false;
        for (Partido p : club.getPartidos().values()) {
            if (p instanceof PartidoIndividual && estado.equalsIgnoreCase(p.getEstadoPartido())) {
                PartidoIndividual individual = (PartidoIndividual) p;
                boolean juega = individual.getSocio1() == usuarioAutenticado || individual.getSocio2() == usuarioAutenticado;
                if (juega) {
                    hay = true;
                    System.out.println("- ID " + p.getIdPartido() + " | " + individual.getSocio1().getNombre() + " vs "
                            + individual.getSocio2().getNombre() + " | " + p.getFecha() + " | marcador: " + p.getMarcadorFinal());
                }
            }
        }
        return hay;
    }

    private void programarPartidoIndividual() {
        System.out.println("\n--- PROGRAMAR PARTIDO INDIVIDUAL ---");
        String id = String.valueOf(contadorPartidos);
        String deporte = leerTexto("Deporte individual: ");
        String rival = leerTexto("ID del socio rival: ");
        String fecha = leerTexto("Fecha (YYYY-MM-DD): ");
        try {
            club.programarPartidoIndividual(id, fecha, usuarioAutenticado.getIdUsuario(), rival, deporte);
            contadorPartidos++;
            guardarTodo();
            System.out.println("Partido individual programado (ID: " + id + ").");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo programar el partido: " + e.getMessage());
        }
    }

    private void registrarResultadoIndividual() {
        System.out.println("\n--- REGISTRAR RESULTADO ---");
        System.out.println("Sus partidos programados:");
        if (!listarPartidosIndividuales("Programado")) {
            System.out.println("No tiene partidos programados.");
            return;
        }
        String id = leerTexto("ID del partido: ");
        int propios = leerEntero("Sets ganados por usted: ");
        int rival = leerEntero("Sets ganados por su rival: ");
        try {
            club.registrarResultadoIndividual(id, usuarioAutenticado.getIdUsuario(), propios, rival);
            guardarTodo();
            System.out.println("Resultado registrado. El ranking de los dos jugadores se actualizó.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar el resultado: " + e.getMessage());
        }
    }

    private void registrarEstadisticasIndividual() {
        System.out.println("\n--- REGISTRAR MIS ESTADÍSTICAS ---");
        System.out.println("Sus partidos finalizados:");
        if (!listarPartidosIndividuales("Finalizado")) {
            System.out.println("No tiene partidos finalizados.");
            return;
        }
        String id = leerTexto("ID del partido: ");
        Partido partido = club.getPartidos().get(id);
        if (!(partido instanceof PartidoIndividual) || partido.getDeporte() == null) {
            System.out.println("El partido no existe.");
            return;
        }
        String nombre = partido.getDeporte().getNombreDeporte().toLowerCase();
        int sets = leerEntero("Sets ganados: ");
        int games = 0;
        int aces = 0;
        int errores = 0;
        int puntos = 0;
        if (nombre.contains("mesa")) {
            puntos = leerEntero("Puntos totales: ");
        } else {
            games = leerEntero("Games ganados: ");
            aces = leerEntero("Aces: ");
            errores = leerEntero("Errores no forzados: ");
        }
        try {
            club.registrarEstadisticaRaqueta(id, usuarioAutenticado.getIdUsuario(), sets, games, aces, errores, puntos);
            guardarTodo();
            System.out.println("Estadísticas registradas.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void inscribirseAEquipo() {
        System.out.println("\n--- INSCRIPCIÓN A UN EQUIPO ---");
        System.out.println("Equipos disponibles:");
        for (Equipo e : club.getEquipos().values()) {
            System.out.println("- " + e.getNombreEquipo() + " - " + e.getDeporteEquipo().getNombreDeporte()
                    + " - edad máxima: " + e.getCategoriaEdad()
                    + " - cupos: " + e.getSocios().size() + "/" + e.getCupoMaximo()
                    + " - posiciones: " + e.getDeporteEquipo().getPosiciones());
        }
        String equipo = leerTexto("Nombre del equipo: ");
        String posicion = leerTexto("Posición: ");
        int numero = leerEntero("Número de camiseta: ");
        try {
            club.realizarInscripcionEquipo(usuarioAutenticado.getIdUsuario(), equipo, posicion, numero);
            guardarTodo();
            Equipo elegido = club.getEquipos().get(equipo);
            System.out.println("Inscripción registrada: dorsal " + numero + ", posición " + posicion
                    + ". Cupos libres en el equipo: " + (elegido.getCupoMaximo() - elegido.getSocios().size()) + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la inscripción: " + e.getMessage());
        }
    }
    
    private void reservarInstalacion() {
        System.out.println("\n--- RESERVA DE INSTALACIÓN ---");
        System.out.println("Instalaciones:");
        
        for (Instalacion i : club.getInstalaciones().values()) {
            String nombreDeporte = "sin disciplina";
            if (i.getDeporte() != null) {
                nombreDeporte = i.getDeporte().getNombreDeporte();
            }
            System.out.println("- " + i.getNombre() + " | " + i.getTipo() + " | " + nombreDeporte
                    + " | capacidad: " + i.getCapacidadMaximaJugadores());
        }
        
        String idReserva = String.valueOf(contadorReservas);
        String inst = leerTexto("Instalación: ");
        String fecha = leerTexto("Fecha (YYYY-MM-DD): ");
        String hora = leerTexto("Hora de inicio (0-23): ");
        int cant = leerEntero("Cantidad de jugadores: ");
        String mod = leerTexto("Modalidad (por ejemplo Sencillos o Dobles): ");
        try {
            club.realizarReserva(idReserva, usuarioAutenticado.getIdUsuario(), inst, fecha, hora, cant, mod);
            contadorReservas++;
            guardarTodo();
            System.out.println("Reserva realizada (ID: " + idReserva + ").");
            
            Instalacion elegida = club.getInstalaciones().get(inst);
            if (elegida.getDeporte() != null) {
                int libres = club.cuposSimultaneosLibres(elegida.getDeporte(), fecha, hora);
                if (libres >= 0) {
                    System.out.println("Cupos simultáneos libres de " + elegida.getDeporte().getNombreDeporte()
                            + " en ese bloque: " + libres + ".");
                }
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la reserva: " + e.getMessage());
        }
    }

    //ENTRENADOR
    private void menuEntrenador() {
        encabezadoSesion("Entrenador");
        System.out.println("1. Registrar marcador de un partido");
        System.out.println("2. Registrar estadísticas de un jugador en un partido");
        System.out.println("3. Programar un entrenamiento");
        System.out.println("4. Registrar asistencia a un entrenamiento");
        System.out.println("5. Solicitar cambio de turno");
        System.out.println("6. Comprar en la tienda deportiva");
        System.out.println("7. Comprar en la cafetería");
        System.out.println("8. Torneos");
        System.out.println("10. Consultar si un equipo puede jugar");
        System.out.println("9. Cerrar sesión");
        System.out.println("0. Salir de la aplicación");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> registrarMarcador();
            case 2 -> registrarEstadisticas();
            case 3 -> programarEntrenamiento();
            case 4 -> registrarAsistencia();
            case 5 -> solicitarCambioTurno();
            case 6 -> comprar(Venta.TIENDADEPORTIVA);
            case 7 -> comprar(Venta.CAFETERIA);
            case 8 -> menuTorneosEntrenador();
            case 9 -> cerrarSesion();
            case 10 -> consultarDisponibilidadEquipo();
            case 0 -> salir();
            default -> System.out.println("Opción no válida.");
        }
    }

    // Lista los partidos de equipos en el estado indicado. Devuelve false si no hay ninguno.
    private boolean listarPartidosPorEstado(String estado) {
        boolean hay = false;
        for (Partido p : club.getPartidos().values()) {
            if (estado.equalsIgnoreCase(p.getEstadoPartido())) {
                String detalle = "";
                if (p instanceof PartidoInterno) {
                    PartidoInterno interno = (PartidoInterno) p;
                    detalle = "Interno: " + interno.getEquipo1().getNombreEquipo() + " vs " + interno.getEquipo2().getNombreEquipo();
                } else if (p instanceof PartidoCompetencia) {
                    PartidoCompetencia competencia = (PartidoCompetencia) p;
                    detalle = "Competencia: " + competencia.getEquipo().getNombreEquipo() + " vs " + competencia.getInstitucionRival();
                }
                if (!detalle.isEmpty()) {
                    hay = true;
                    System.out.println("- ID " + p.getIdPartido() + " | " + detalle + " | " + p.getFecha() + " | marcador: " + p.getMarcadorFinal());
                }
            }
        }
        return hay;
    }

    private void registrarMarcador() {
        System.out.println("\n--- REGISTRAR MARCADOR ---");
        System.out.println("Partidos programados:");
        if (!listarPartidosPorEstado("Programado")) {
            System.out.println("No hay partidos programados.");
            return;
        }
        String id = leerTexto("ID del partido: ");
        String marcador = leerTexto("Marcador final (ejemplo 2-1, primero el equipo del club): ");
        try {
            club.registrarMarcadorPartido(id, marcador);
            guardarTodo();
            System.out.println("Marcador registrado. El partido quedó finalizado.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar el marcador: " + e.getMessage());
        }
    }

    private void registrarEstadisticas() {
        System.out.println("\n--- REGISTRAR ESTADÍSTICAS DE UN JUGADOR ---");
        System.out.println("Partidos finalizados:");
        if (!listarPartidosPorEstado("Finalizado")) {
            System.out.println("No hay partidos finalizados.");
            return;
        }
        String idPartido = leerTexto("ID del partido: ");
        Partido partido = club.getPartidos().get(idPartido);
        if (partido == null) {
            System.out.println("El partido no existe.");
            return;
        }
        String idSocio = leerTexto("ID del socio: ");
        String deporte = partido.getDeporte().getNombreDeporte();
        try {
            if (deporte.equalsIgnoreCase("Fútbol")) {
                int goles = leerEntero("Goles: ");
                int asistencias = leerEntero("Asistencias: ");
                int amarillas = leerEntero("Tarjetas amarillas: ");
                int rojas = leerEntero("Tarjetas rojas: ");
                boolean suspendido = club.registrarEstadisticaFutbol(idPartido, idSocio, goles, asistencias, amarillas, rojas);
                guardarTodo();
                System.out.println("Estadísticas registradas.");
                if (suspendido) {
                    System.out.println("El jugador queda suspendido para el siguiente partido de su equipo.");
                }
            } else if (deporte.equalsIgnoreCase("Baloncesto")) {
                int puntos = leerEntero("Puntos: ");
                int rebotes = leerEntero("Rebotes: ");
                club.registrarEstadisticaBaloncesto(idPartido, idSocio, puntos, rebotes);
                guardarTodo();
                System.out.println("Estadísticas registradas.");
            } else if (deporte.equalsIgnoreCase("Voleibol")) {
                int aces = leerEntero("Aces: ");
                int bloqueos = leerEntero("Bloqueos: ");
                club.registrarEstadisticaVoleibol(idPartido, idSocio, aces, bloqueos);
                guardarTodo();
                System.out.println("Estadísticas registradas.");
            } else {
                System.out.println("Este deporte no tiene estadísticas por jugador definidas.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }
    
    private void programarEntrenamiento() {
        System.out.println("\n--- PROGRAMAR ENTRENAMIENTO ---");
        String id = club.generarIdSesionEntrenamiento();
        String fecha = leerTexto("Fecha (YYYY-MM-DD): ");
        String hora = leerTexto("Hora (por ejemplo 17:00): ");
        int duracion = leerEntero("Duración en minutos: ");
        String instalacion = leerTexto("Instalación utilizada: ");
        String equipo = leerTexto("Equipo convocado (Enter si es de un deporte individual): ");
        String deporte = "";
        List<String> ids = new ArrayList<>();
        if (equipo.isEmpty()) {
            deporte = leerTexto("Deporte individual convocado: ");
            String texto = leerTexto("IDs de los socios convocados separados por coma (Enter para todos los inscritos): ");
            if (!texto.isEmpty()) {
                for (String parte : texto.split(",")) {
                    ids.add(parte.trim());
                }
            }
        }
        try {
            club.programarSesionEntrenamiento(id, fecha, hora, duracion, usuarioAutenticado.getIdUsuario(), instalacion, equipo, deporte, ids);
            guardarTodo();
            System.out.println("Entrenamiento programado (ID: " + id + ").");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo programar el entrenamiento: " + e.getMessage());
        }
    }

    private void registrarAsistencia() {
        System.out.println("\n--- REGISTRAR ASISTENCIA ---");
        System.out.println("Sus entrenamientos:");
        boolean hay = false;
        for (SesionEntrenamiento s : club.getSesionesEntrenamiento().values()) {
            if (s.getEntrenador() == usuarioAutenticado) {
                hay = true;
                System.out.println("- " + s.getIdEntrenamiento() + " | " + s.getFecha() + " " + s.getHora()
                        + " | convocados: " + s.getEntrenamientoSocios().size());
            }
        }
        if (!hay) {
            System.out.println("No tiene entrenamientos programados.");
            return;
        }
        String idSesion = leerTexto("ID del entrenamiento: ");
        String idSocio = leerTexto("ID del socio: ");
        System.out.println("1. Asistió   2. Faltó   3. Llegó tarde");
        int opcion = leerEntero("Estado: ");
        String estado = "";
        if (opcion == 1) {
            estado = "Asistió";
        } else if (opcion == 2) {
            estado = "Faltó";
        } else if (opcion == 3) {
            estado = "Llegó tarde";
        }
        double nota = leerDouble("Nota de desempeño de 1 a 5 (0 para omitir): ");
        String comentario = leerTexto("Comentario (opcional): ");
        try {
            club.registrarAsistencia(idSesion, idSocio, estado, nota, comentario);
            guardarTodo();
            System.out.println("Asistencia registrada.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar la asistencia: " + e.getMessage());
        }
    }

    private String describirTurno(Turno t) {
        String jornada;
        if (t.isEsDiurno()) {
            jornada = "diurno";
        } else {
            jornada = "nocturno";
        }
        return "- " + t.getIdTurno() + " - día " + t.getDiaSemana() + " - " + jornada
                + " - entrenadores: " + t.getEntrenadores().size() + " - fisioterapeutas: " + t.getFisioterapeutas().size();
    }

    private void solicitarCambioTurno() {
        System.out.println("\n--- SOLICITAR CAMBIO DE TURNO ---");
        Empleado empleado = (Empleado) usuarioAutenticado;
        System.out.println("Sus turnos:");
        for (Turno t : empleado.getTurnos().values()) {
            System.out.println(describirTurno(t));
        }
        System.out.println("Turnos del club:");
        for (Turno t : club.getTurnos().values()) {
            System.out.println(describirTurno(t));
        }
        String actual = leerTexto("ID del turno que deja: ");
        String nuevo = leerTexto("ID del turno al que pasa: ");
        try {
            boolean aprobada = club.realizarSolicitudCambioTurno(empleado.getIdUsuario(), actual, nuevo);
            guardarTodo();
            if (aprobada) {
                System.out.println("Solicitud aprobada: ahora tiene el turno " + nuevo + ".");
            } else {
                System.out.println("Solicitud rechazada: el turno que deja quedaría sin el mínimo de 2 entrenadores y 1 fisioterapeuta.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo solicitar el cambio: " + e.getMessage());
        }
    }

    // Menú del administrador para preparar los turnos.
    private void menuTurnos() {
        System.out.println("\n--- GESTIÓN DE TURNOS ---");
        System.out.println("1. Crear turno");
        System.out.println("2. Asignar empleado a un turno");
        System.out.println("3. Ver turnos");
        System.out.println("4. Torneos");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String id = leerTexto("ID del turno: ");
                int dia = leerEntero("Día de la semana (1-7): ");
                boolean diurno = leerTexto("¿Diurno? (s/n): ").equalsIgnoreCase("s");
                if (club.getTurnos().containsKey(id)) {
                    System.out.println("Ya existe un turno con ese ID.");
                } else if (dia < 1 || dia > 7) {
                    System.out.println("El día debe estar entre 1 y 7.");
                } else {
                    club.crearTurno(id, dia, diurno);
                    guardarTodo();
                    System.out.println("Turno creado.");
                }
            }
            case 2 -> {
                String idTurno = leerTexto("ID del turno: ");
                String idEmpleado = leerTexto("ID del empleado: ");
                Turno turno = club.getTurnos().get(idTurno);
                Usuario u = club.getUsuarios().get(idEmpleado);
                if (turno == null) {
                    System.out.println("El turno no existe.");
                } else if (!(u instanceof Empleado)) {
                    System.out.println("El empleado no existe.");
                } else if (((Empleado) u).getTurnos().containsKey(idTurno)) {
                    System.out.println("El empleado ya tiene ese turno.");
                } else {
                    club.asignarTurnoAEmpleado(idTurno, idEmpleado);
                    guardarTodo();
                    System.out.println("Turno asignado.");
                }
            }
            case 3 -> {
                for (Turno t : club.getTurnos().values()) {
                    System.out.println(describirTurno(t));
                }
            }
            case 4 -> menuTorneosFisioterapeuta();
        }
    } 

    private void registrarPartidoCompetencia() {
        String id = String.valueOf(contadorPartidos++);
        String fecha = leerTexto("Fecha: ");
        String marcador = leerTexto("Marcador Final: ");
        String estado = leerTexto("Estado Partido: ");
        String equipo = leerTexto("Nombre Equipo: ");
        String competencia = leerTexto("Nombre Competencia: ");
        String rival = leerTexto("Institución Rival: ");
        String escenario = leerTexto("Escenario: ");
        List<Socio> nomina = pedirNominaJugadores();

        club.realizarPartidoCompetencia(id, fecha, marcador, estado, equipo, competencia, rival, escenario, nomina);
        guardarTodo();
        System.out.println("Proceso de partido de competencia ejecutado (ID: " + id + ").");
    }

    private void registrarPartidoInterno() {
        String id = String.valueOf(contadorPartidos++);
        String fecha = leerTexto("Fecha: ");
        String marcador = leerTexto("Marcador Final: ");
        String estado = leerTexto("Estado Partido: ");
        String e1 = leerTexto("Equipo 1: ");
        String e2 = leerTexto("Equipo 2: ");

        club.realizarPartidoInterno(id, fecha, marcador, estado, e1, e2);
        guardarTodo();
        System.out.println("Proceso de partido interno ejecutado (ID: " + id + ").");
    }

    // ---------- Fisioterapeuta ----------

    private void menuFisioterapeuta() {
        encabezadoSesion("Fisioterapeuta");
        System.out.println("1. Solicitar cambio de turno");
        System.out.println("2. Comprar en la tienda deportiva");
        System.out.println("3. Comprar en la cafetería");
        System.out.println("9. Cerrar sesión");
        System.out.println("0. Salir de la aplicación");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> solicitarCambioTurno();
            case 2 -> comprar(Venta.TIENDADEPORTIVA);
            case 3 -> comprar(Venta.CAFETERIA);
            case 9 -> cerrarSesion();
            case 0 -> salir();
            default -> System.out.println("Opción no válida.");
        }
    }
    
    // ----------------------------------
    // COMPRAS (de todos los usuarios)
 
    //Se registran como códigos validos los de todos los empleados.
    private void registrarCodigosDescuento() {
        for (Usuario u : club.getUsuarios().values()) {
            if (u instanceof Empleado) {
                tienda.getCalculadoraDescuento().agregarCodigoValido(((Empleado) u).getCodigoDescuento());
            }
            // Enunciado Proyecto 2
            if (u instanceof Socio) {
                for (String bono : ((Socio) u).getBonosDescuento()) {
                    tienda.registrarBonoTorneo(bono);
                }
            }
        }
    }

    private String dinero(double valor) {
        return "$" + String.format(Locale.US, "%.2f", valor);
    }

    private void comprar(String tipo) {
        boolean esCafeteria = Venta.CAFETERIA.equals(tipo);
        if (esCafeteria) {
            System.out.println("\n--- COMPRA EN LA CAFETERÍA ---");
        } else {
            System.out.println("\n--- COMPRA EN LA TIENDA DEPORTIVA ---");
        }
        Map<String, ? extends Producto> catalogo;
        if (esCafeteria) {
            catalogo = tienda.obtenerProductosCafeteria();
        } else {
            catalogo = tienda.obtenerArticulosTienda();
        }
        if (catalogo.isEmpty()) {
            System.out.println("No hay productos disponibles.");
            return;
        }

        List<ItemVenta> items = new ArrayList<>();
        boolean seguir = true;
        while (seguir) {
            System.out.println("Productos disponibles:");
            for (Producto p : catalogo.values()) {
                System.out.println("- " + p.getNombre() + " | " + dinero(p.getPrecioBase()) + " | Stock: " + p.getCantidadInventario());
            }
            String nombre = leerTexto("Nombre del producto: ");
            Producto producto = catalogo.get(nombre);
            if (producto == null) {
                System.out.println("Producto no encontrado.");
            } else {
                int cantidad = leerEntero("Cantidad: ");
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a cero.");
                } else {
                    if (producto instanceof ProductoCafeteria) {
                        List<String> alergenos = tienda.informarAlergenos(producto);
                        if (!alergenos.isEmpty()) {
                            System.out.println("Alérgenos de " + producto.getNombre() + ": " + String.join(", ", alergenos));
                        }
                    }
                    ItemVenta existente = null;
                    for (ItemVenta item : items) {
                        if (item.getProducto() == producto) {
                            existente = item;
                        }
                    }
                    if (existente != null) {
                        existente.setCantidad(existente.getCantidad() + cantidad);
                    } else {
                        items.add(new ItemVenta(cantidad, producto));
                    }
                    System.out.println("Agregado al carrito.");
                }
            }
            seguir = leerTexto("¿Agregar otro producto? (s/n): ").equalsIgnoreCase("s");
        }
        if (items.isEmpty()) {
            System.out.println("No se agregaron productos.");
            return;
        }

        String codigo = "";
        if (!esCafeteria) {
            codigo = leerTexto("Código de descuento (Enter si no tiene): ");
            
            if (codigo.trim().toUpperCase().startsWith("BONO-")) {
                System.out.println("Los bonos se redimen con la opción 'Comprar en la tienda deportiva con un bono'.");
                return;
            }
        }
        int puntos = 0;
        if (usuarioAutenticado.getPuntosFidelidad() > 0) {
            String respuesta = leerTexto("Tiene " + usuarioAutenticado.getPuntosFidelidad() + " puntos (1 punto = $1). ¿Desea redimir puntos? (s/n): ");
            if (respuesta.equalsIgnoreCase("s")) {
                puntos = leerEntero("Puntos a redimir: ");
            }
        }

        LocalDateTime ahora = LocalDateTime.now();
        String idVenta = tienda.generarIdVenta();
        try {
            if (puntos > 0) {
                tienda.realizarVentaConPuntos(idVenta, ahora.toLocalDate().toString(), ahora.getHour(),
                        usuarioAutenticado.getIdUsuario(), codigo, items, club, tipo, puntos);
            } else {
                tienda.realizarVenta(idVenta, ahora.toLocalDate().toString(), ahora.getHour(), null,
                        usuarioAutenticado.getIdUsuario(), codigo, items, club, tipo);
            }
            guardarTodo();
            List<Venta> ventas = tienda.getVentas();
            mostrarResumenVenta(ventas.get(ventas.size() - 1));
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la compra: " + e.getMessage());
        }
    }

    private void mostrarResumenVenta(Venta venta) {
        System.out.println("\n--- COMPRA REALIZADA: " + venta.getIdVenta() + " ---");
        for (ItemVenta item : venta.getItemsVenta()) {
            System.out.println("- " + item.getProducto().getNombre() + " x" + item.getCantidad() + " = " + dinero(item.precioItemVenta()));
        }
        System.out.println("Subtotal: " + dinero(venta.getSubTotal()));
        if (venta.getDescuento() > 0) {
            System.out.println("Descuento: -" + dinero(venta.getDescuento()));
        }
        boolean esCafeteria = Venta.CAFETERIA.equals(venta.getTipo());
        String detalleImpuestos;
        if (esCafeteria) {
            detalleImpuestos = " (con impuesto al consumo y propina sugerida)";
        } else {
            detalleImpuestos = " (con IVA)";
        }
        System.out.println("Total a pagar" + detalleImpuestos + ": " + dinero(venta.getTotal()));
        System.out.println("Puntos ganados: " + (int) venta.getPuntosGanados());
        System.out.println("Sus puntos de fidelidad: " + usuarioAutenticado.getPuntosFidelidad());
    }
    
    // Compra en la tienda deportiva con un bono de torneo (HU-06). Es una compra "en progreso":
    // se arma, se aplica el bono y solo al confirmar se descuenta el inventario y se consume el bono.
    private void comprarConBono() {
        System.out.println("\n--- COMPRA EN LA TIENDA DEPORTIVA CON BONO ---");
        Socio socio = (Socio) usuarioAutenticado;
        if (!socio.tieneBonosDisponibles()) {
            System.out.println("No tiene ningún bono de descuento por redimir.");
            return;
        }
        Map<String, ArticuloTienda> catalogo = tienda.obtenerArticulosTienda();
        if (catalogo.isEmpty()) {
            System.out.println("No hay productos disponibles.");
            return;
        }

        List<ItemVenta> items = new ArrayList<>();
        boolean seguir = true;
        while (seguir) {
            System.out.println("Productos disponibles:");
            for (ArticuloTienda a : catalogo.values()) {
                System.out.println("- " + a.getNombre() + " // " + dinero(a.getPrecioBase()) + " // Stock: " + a.getCantidadInventario());
            }
            String nombre = leerTexto("Nombre del producto: ");
            ArticuloTienda articulo = catalogo.get(nombre);
            if (articulo == null) {
                System.out.println("Producto no encontrado.");
            } else {
                int cantidad = leerEntero("Cantidad: ");
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a cero.");
                } else {
                    ItemVenta existente = null;
                    for (ItemVenta item : items) {
                        if (item.getProducto() == articulo) {
                            existente = item;
                        }
                    }
                    if (existente != null) {
                        existente.setCantidad(existente.getCantidad() + cantidad);
                    } else {
                        items.add(new ItemVenta(cantidad, articulo));
                    }
                    System.out.println("Agregado al carrito.");
                }
            }
            seguir = leerTexto("¿Agregar otro producto? (s/n): ").equalsIgnoreCase("s");
        }
        if (items.isEmpty()) {
            System.out.println("No se agregaron productos.");
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();
        try {
            Venta venta = tienda.iniciarCompraTienda(tienda.generarIdVenta(), ahora.toLocalDate().toString(),
                    ahora.getHour(), usuarioAutenticado.getIdUsuario(), items, club);
            System.out.println("Subtotal de la compra: " + dinero(venta.getSubTotal()));
            System.out.println("Sus bonos:");
            for (String bono : socio.getBonosDescuento()) {
                System.out.println("- " + bono);
            }
            String codigoBono = leerTexto("Código del bono a aplicar (Enter para el primero): ");
            if (!codigoBono.isEmpty() && !socio.tieneBonoDescuento(codigoBono)) {
                System.out.println("Ese bono no está entre sus bonos.");
                return;
            }
            tienda.aplicarBonoDescuento(venta, codigoBono);
            System.out.println("Bono aplicado. Descuento: " + dinero(venta.getDescuento()));

            String otro = leerTexto("¿Desea aplicar otro bono a esta compra? (s/n): ");
            if (otro.equalsIgnoreCase("s")) {
                try {
                    tienda.aplicarBonoDescuento(venta, "");
                } catch (IllegalArgumentException e) {
                    System.out.println("No se pudo aplicar: " + e.getMessage());
                }
            }

            String confirmar = leerTexto("¿Confirmar la compra? (s/n): ");
            if (confirmar.equalsIgnoreCase("s")) {
                tienda.confirmarCompraTienda(venta);
                guardarTodo();
                mostrarResumenVenta(venta);
            } else {
                System.out.println("Compra cancelada. El inventario y el bono no cambiaron.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la compra: " + e.getMessage());
        }
    }

    private boolean loginDisponible(String login) {
        boolean libre = autentificador.esLoginDisponible(login, new ArrayList<>(club.getUsuarios().values()));
        if (!libre) {
            System.out.println("Ese login no está disponible. Intente con otro.");
        }
        return libre;
    }

    private void registrarSocio() {
        System.out.println("\n--- REGISTRO DE SOCIO ---");
        String id = String.valueOf(contadorUsuarios);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        if (!loginDisponible(login)) {
            return;
        }
        String pass = leerTexto("Contraseña: ");
        int edad = leerEntero("Edad: ");

        club.crearSocio(id, nombre, apellido, login, pass, 0, edad, true);
        contadorUsuarios++;
        guardarTodo();
        System.out.println("Socio registrado con éxito (ID: " + id + ").");
    }

    private void registrarEntrenador() {
        System.out.println("\n--- REGISTRO DE ENTRENADOR ---");
        String id = String.valueOf(contadorUsuarios);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        if (!loginDisponible(login)) {
            return;
        }
        String pass = leerTexto("Contraseña: ");
        int puntos = leerEntero("Puntos iniciales: ");
        String codigo = leerTexto("Código descuento: ");

        club.crearEntrenador(id, nombre, apellido, login, pass, puntos, codigo);
        tienda.getCalculadoraDescuento().agregarCodigoValido(codigo);
        contadorUsuarios++;
        guardarTodo();
        System.out.println("Entrenador registrado con éxito (ID: " + id + ").");
    }

    private void registrarFisioterapeuta() {
        System.out.println("\n--- REGISTRO DE FISIOTERAPEUTA ---");
        String id = String.valueOf(contadorUsuarios);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        if (!loginDisponible(login)) {
            return;
        }
        String pass = leerTexto("Contraseña: ");
        int puntos = leerEntero("Puntos iniciales: ");
        String codigo = leerTexto("Código descuento: ");

        club.crearFisioterapeuta(id, nombre, apellido, login, pass, puntos, codigo);
        tienda.getCalculadoraDescuento().agregarCodigoValido(codigo);
        contadorUsuarios++;
        guardarTodo();
        System.out.println("Fisioterapeuta registrado con éxito (ID: " + id + ").");
    }

    private void registrarAdministrador() {
        System.out.println("\n--- REGISTRO DE ADMINISTRADOR ---");
        String id = String.valueOf(contadorUsuarios);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        if (!loginDisponible(login)) {
            return;
        }
        String pass = leerTexto("Contraseña: ");
        int puntos = leerEntero("Puntos iniciales: ");

        club.crearAdministrador(id, nombre, apellido, login, pass, puntos);
        contadorUsuarios++;
        guardarTodo();
        System.out.println("Administrador registrado con éxito (ID: " + id + ").");
    }

   

    private List<Socio> pedirNominaJugadores() {
        List<Socio> nomina = new ArrayList<>();
        String idsCadena = leerTexto("IDs de Socios para la nómina (separados por coma): ");
        if (!idsCadena.isEmpty()) {
            String[] ids = idsCadena.split(",");
            for (String id : ids) {
                id = id.trim();
                Usuario u = club.getUsuarios().get(id);
                if (u instanceof Socio) {
                    nomina.add((Socio) u);
                } else {
                    System.out.println("Advertencia: El ID '" + id + "' no corresponde a un Socio registrado.");
                }
            }
        }
        return nomina;
    }

    private int obtenerMaxId(Iterable<String> llaves) {
        int max = 0;
        if (llaves != null) {
            for (String key : llaves) {
                int idNum = Integer.parseInt(key);
                if (idNum > max) {
                    max = idNum;
                }
            }
        }
        return max;
    }


    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = scanner.nextLine().trim();
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número entero válido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = scanner.nextLine().trim();
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número decimal válido.");
            }
        }
    }
    
    private void imprimir(List<String> lineas) {
        if (lineas.isEmpty()) {
            System.out.println("(sin registros)");
        }
        for (String linea : lineas) {
            System.out.println("- " + linea);
        }
    }

    private void menuReportes() {
        System.out.println("\n--- REPORTES Y CONSULTAS ---");
        System.out.println("1. Inventario de instalaciones y productos");
        System.out.println("2. Historial de reservas");
        System.out.println("3. Historial de partidos y resultados");
        System.out.println("4. Historial de entrenamientos");
        System.out.println("5. Asistencia a entrenamientos");
        System.out.println("6. Goleadores de una competencia");
        System.out.println("7. Tabla de posiciones de una competencia");
        System.out.println("8. Ranking de un deporte individual");
        System.out.println("9. Reporte de ventas (diario, semanal o mensual)");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        try {
            switch (op) {
                case 1 -> {
                    imprimir(club.reporteInventarioInstalaciones());
                    imprimir(tienda.reporteInventarioProductos());
                }
                case 2 -> imprimir(club.reporteHistorialReservas());
                case 3 -> imprimir(club.reporteHistorialPartidos());
                case 4 -> imprimir(club.reporteHistorialSesiones());
                case 5 -> imprimir(club.reporteAsistencia());
                case 6 -> imprimir(club.reporteGoleadores(leerTexto("Nombre de la competencia: ")));
                case 7 -> imprimir(club.reporteTablaPosiciones(leerTexto("Nombre de la competencia: ")));
                case 8 -> imprimir(club.reporteRankingIndividual(leerTexto("Nombre del deporte: ")));
                case 9 -> reporteVentas();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo generar el reporte: " + e.getMessage());
        }
    }

    private void reporteVentas() {
        System.out.println("1. Diario   2. Semanal (últimos 7 días)   3. Mensual");
        int periodo = leerEntero("Periodo: ");
        String texto = leerTexto("Fecha de referencia (YYYY-MM-DD, Enter para hoy): ");
        LocalDate fecha = LocalDate.now();
        if (!texto.isEmpty()) {
            try {
                fecha = LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida.");
                return;
            }
        }
        LocalDate inicio = fecha;
        LocalDate fin = fecha;
        if (periodo == 2) {
            inicio = fecha.minusDays(6);
        } else if (periodo == 3) {
            inicio = fecha.withDayOfMonth(1);
            fin = fecha.withDayOfMonth(fecha.lengthOfMonth());
        } else if (periodo != 1) {
            System.out.println("Periodo no válido.");
            return;
        }
        System.out.println("Ventas del " + inicio + " al " + fin + ":");
        imprimir(tienda.reporteVentas(inicio.toString(), fin.toString()));
    }
    
    // -------------------------------
    // ADMIN (ENUNCIADO PROYECTO 2)

    private void menuTorneos() {
        System.out.println("\n--- GESTIÓN DE TORNEOS ---");
        System.out.println("1. Crear torneo de equipos (deporte de conjunto)");
        System.out.println("2. Crear torneo individual");
        System.out.println("3. Ver torneos");
        System.out.println("4. Registrar un partido de torneo");
        System.out.println("5. Finalizar un torneo");
        System.out.println("6. Inscribir un invitado en un torneo individual");
        System.out.println("7. Retirar a un invitado de un torneo individual");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> crearTorneo(false);
            case 2 -> crearTorneo(true);
            case 3 -> {
                if (!imprimirTorneos(false)) {
                    System.out.println("No hay torneos creados.");
                }
            }
            case 4 -> registrarPartidoTorneo();
            case 5 -> finalizarTorneo();
            case 6 -> registrarInvitadoEnTorneo();
            case 7 -> retirarInvitadoDeTorneo();
            case 0 -> { }
            default -> System.out.println("Opción no válida.");
        }
    }

    private String generarIdTorneo() {
        int max = 0;
        for (String id : club.getTorneos().keySet()) {
            String digitos = id.replaceAll("[^0-9]", "");
            if (!digitos.isEmpty()) {
                max = Math.max(max, Integer.parseInt(digitos));
            }
        }
        return "TOR-" + (max + 1);
    }

    private String describirTorneo(Torneo t) {
        String tipo = "Individual";
        int inscritos = 0;
        if (t instanceof TorneoConjunto) {
            tipo = "Equipos";
            inscritos = ((TorneoConjunto) t).getEquiposInscritos().size();
        } else if (t instanceof TorneoIndividual) {
            inscritos = ((TorneoIndividual) t).getParticipantesInscritos().size();
        }
        String modalidad = "Recreativo";
        if (t.isCompetitivo()) {
            modalidad = "Competitivo ($" + Math.round(t.getTarifaInscripcion()) + ")";
        }
        return t.getIdTorneo() + " | " + t.getNombre() + " | " + tipo + " | " + modalidad + " | " + t.getDeporte().getNombreDeporte()
                + " | categoría: " + t.getCategoria() + " | " + t.getEstado() + " | inscritos: " + inscritos
                + " | cupos generales: " + t.getCuposGenerales() + " | preferenciales: " + t.getCuposPreferenciales();
    }

    // Imprime los torneos (todos o solo los que están en curso) y retorna false si no hay ninguno.
    private boolean imprimirTorneos(boolean soloEnCurso) {
        boolean hay = false;
        for (Torneo t : club.getTorneos().values()) {
            if (!soloEnCurso || Torneo.EN_CURSO.equalsIgnoreCase(t.getEstado())) {
                hay = true;
                System.out.println("- " + describirTorneo(t));
            }
        }
        return hay;
    }

    private String nombreParticipante(String clave) {
        Usuario usuario = club.getUsuarios().get(clave);
        if (usuario != null) {
            return usuario.getNombre() + " " + usuario.getApellido();
        }
        return clave;
    }

    private void crearTorneo(boolean individual) {
        System.out.println("\n--- CREAR TORNEO ---");
        System.out.println("Deportes disponibles:");
        for (Deporte d : club.getDeportes().values()) {
            if (d.esIndividual() == individual) {
                System.out.println("- " + d.getNombreDeporte());
            }
        }
        String id = generarIdTorneo();
        String nombre = leerTexto("Nombre del torneo: ");
        String deporte = leerTexto("Deporte: ");
        int categoria = leerEntero("Categoría (edad máxima: 13, 15, 17 o 99 para Mayores): ");
        int cupos = leerEntero("Número de cupos totales: ");
        System.out.println("1. Recreativo (sin costo, premia con un bono de descuento)");
        System.out.println("2. Competitivo (con tarifa de inscripción, premia en efectivo)");
        int modalidad = leerEntero("Modalidad: ");
        double tarifa = 0;
        if (modalidad == 2) {
            String quien = "equipo";
            if (individual) {
                quien = "jugador";
            }
            tarifa = leerDouble("Tarifa de inscripción por " + quien + ": ");
        } else if (modalidad != 1) {
            System.out.println("Modalidad no válida.");
            return;
        }
        try {
            String idAdmin = usuarioAutenticado.getIdUsuario();
            if (modalidad == 2) {
                if (individual) {
                    club.crearTorneoIndividualCompetitivo(idAdmin, id, nombre, deporte, categoria, cupos, tarifa);
                } else {
                    club.crearTorneoConjuntoCompetitivo(idAdmin, id, nombre, deporte, categoria, cupos, tarifa);
                }
            } else {
                if (individual) {
                    club.crearTorneoIndividual(idAdmin, id, nombre, deporte, categoria, cupos);
                } else {
                    club.crearTorneoConjunto(idAdmin, id, nombre, deporte, categoria, cupos);
                }
            }
            guardarTodo();
            String texto = "recreativo";
            if (modalidad == 2) {
                texto = "competitivo";
            }
            System.out.println("Torneo " + texto + " creado (ID: " + id + "), en curso.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo crear el torneo: " + e.getMessage());
        }
    }

    private void registrarPartidoTorneo() {
        System.out.println("\n--- REGISTRAR PARTIDO DE TORNEO ---");
        System.out.println("Torneos en curso:");
        if (!imprimirTorneos(true)) {
            System.out.println("No hay torneos en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        Torneo torneo = club.getTorneos().get(idTorneo);
        if (torneo == null) {
            System.out.println("El torneo no existe.");
            return;
        }
        if (!Torneo.EN_CURSO.equalsIgnoreCase(torneo.getEstado())) {
            System.out.println("El torneo ya ha finalizado.");
            return;
        }
        String idPartido = String.valueOf(contadorPartidos);
        String fecha = leerTexto("Fecha (YYYY-MM-DD): ");
        try {
            if (torneo instanceof TorneoConjunto) {
                String e1 = leerTexto("Equipo 1: ");
                String e2 = leerTexto("Equipo 2: ");
                if (e1.equals(e2)) {
                    System.out.println("Los equipos deben ser distintos.");
                    return;
                }
                String marcador = leerTexto("Marcador final (ejemplo 2-1, primero el equipo 1): ");
                club.registrarPartidoTorneoConjunto(idTorneo, idPartido, fecha, e1, e2, marcador);
            } else {
                String j1 = leerTexto("ID del jugador 1: ");
                String j2 = leerTexto("ID del jugador 2: ");
                if (j1.equals(j2)) {
                    System.out.println("Los jugadores deben ser distintos.");
                    return;
                }
                int sets1 = leerEntero("Sets ganados por el jugador 1: ");
                int sets2 = leerEntero("Sets ganados por el jugador 2: ");
                club.registrarPartidoTorneoIndividual(idTorneo, idPartido, fecha, j1, j2, sets1, sets2);
            }
            contadorPartidos++;
            guardarTodo();
            System.out.println("Partido registrado (ID: " + idPartido + "). La tabla de posiciones se actualizó.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar el partido: " + e.getMessage());
        }
    }

    private void finalizarTorneo() {
        System.out.println("\n--- FINALIZAR TORNEO ---");
        System.out.println("Torneos:");
        if (!imprimirTorneos(false)) {
            System.out.println("No hay torneos creados.");
            return;
        }
        String id = leerTexto("ID del torneo: ");
        try {
            club.finalizarTorneo(usuarioAutenticado.getIdUsuario(), id, tienda);
            guardarTodo();
            System.out.println("El torneo quedó en estado 'Finalizado'.");
            Torneo torneo = club.getTorneos().get(id);
            String primero = torneo.obtenerPrimerLugar();
            if (primero == null) {
                System.out.println("No hubo participantes: no se otorgó ningún premio.");
            } else {
                System.out.println("Primer lugar: " + nombreParticipante(primero));
                if (torneo.isCompetitivo()) {
                    if (torneo.getGanadorPremio() == null) {
                        System.out.println("El primer lugar es un empleado: no recibe el premio en efectivo.");
                    } else {
                        System.out.println("Premio en efectivo: $" + Math.round(torneo.getPremio())
                                + " (" + Math.round(Torneo.PORCENTAJE_PREMIO * 100) + "% de $"
                                + Math.round(torneo.getTotalRecaudado()) + " recaudados).");
                    }
                } else {
                    System.out.println("Si el ganador es socio, su bono ya quedó disponible para la tienda deportiva.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo finalizar el torneo: " + e.getMessage());
        }
    }
    
    // -------------------------------
    // Socios (ENUNCIADO PROYECTO 2)

    private void menuTorneosSocio() {
        System.out.println("\n--- TORNEOS ---");
        System.out.println("1. Ver torneos");
        System.out.println("2. Inscribirme a un torneo individual");
        System.out.println("3. Salirme de un torneo individual");
        System.out.println("4. Ver mis bonos de descuento");
        System.out.println("5. Ver historial de mis torneos");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                if (!imprimirTorneos(false)) {
                    System.out.println("No hay torneos creados.");
                }
            }
            case 2 -> inscribirseATorneoIndividual();
            case 3 -> salirseDeTorneoIndividual();
            case 4 -> verMisBonos();
            case 5 -> consultarHistorialTorneos();
            case 0 -> { }
            default -> System.out.println("Opción no válida.");
        }
    }

    // Imprime los torneos individuales en curso y retorna false si no hay ninguno.
    private boolean imprimirTorneosIndividualesEnCurso() {
        boolean hay = false;
        for (Torneo t : club.getTorneos().values()) {
            if (t instanceof TorneoIndividual && Torneo.EN_CURSO.equalsIgnoreCase(t.getEstado())) {
                hay = true;
                System.out.println("- " + describirTorneo(t));
            }
        }
        return hay;
    }

    private void inscribirseATorneoIndividual() {
        System.out.println("\n--- INSCRIPCIÓN A UN TORNEO INDIVIDUAL ---");
        System.out.println("Torneos individuales en curso:");
        if (!imprimirTorneosIndividualesEnCurso()) {
            System.out.println("No hay torneos individuales en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        try {
            club.inscribirSocioTorneoIndividual(usuarioAutenticado.getIdUsuario(), idTorneo);
            guardarTodo();
            TorneoIndividual torneo = (TorneoIndividual) club.getTorneos().get(idTorneo);
            if (torneo.usoCupoPreferencial(usuarioAutenticado.getIdUsuario())) {
                System.out.println("Inscripción registrada con un cupo preferencial (por su ranking).");
            } else {
                System.out.println("Inscripción registrada con un cupo general.");
            }
            if (torneo.isCompetitivo()) {
                System.out.println("Se registró el cobro de la tarifa: $" + Math.round(torneo.getTarifaInscripcion())
                        + " (el pago lo gestiona un tercero).");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la inscripción: " + e.getMessage());
        }
    }

    private void salirseDeTorneoIndividual() {
        System.out.println("\n--- SALIRSE DE UN TORNEO INDIVIDUAL ---");
        System.out.println("Torneos individuales en curso:");
        if (!imprimirTorneosIndividualesEnCurso()) {
            System.out.println("No hay torneos individuales en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        try {
            club.desinscribirSocioTorneoIndividual(usuarioAutenticado.getIdUsuario(), idTorneo);
            guardarTodo();
            System.out.println("Se eliminó su inscripción y se liberó el cupo.");
            if (club.getTorneos().get(idTorneo).isCompetitivo()) {
                System.out.println("Se desconto su tarifa del total recaudado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo cancelar la inscripción: " + e.getMessage());
        }
    }

    private void verMisBonos() {
        Socio socio = (Socio) usuarioAutenticado;
        if (!socio.tieneBonosDisponibles()) {
            System.out.println("No tiene bonos de descuento por redimir.");
            return;
        }
        int porcentaje = (int) Math.round(CalculadoraDescuento.DESCUENTO_BONO_TORNEO * 100);
        for (String bono : socio.getBonosDescuento()) {
            System.out.println("- " + bono + " (" + porcentaje + "% de descuento en la tienda deportiva)");
        }
        System.out.println("Un bono se aplica durante una compra y no se acumula con otros descuentos.");
    }
    
    
    // ----------------------------------------------
    // Entrenadores y Fisios (ENUNCIADO PROYECTO 2)

    private void menuTorneosEntrenador() {
        System.out.println("\n--- TORNEOS ---");
        System.out.println("1. Ver torneos");
        System.out.println("2. Inscribir un equipo en un torneo de equipos");
        System.out.println("3. Inscribirme a un torneo individual");
        System.out.println("4. Ver historial de mis torneos");
        System.out.println("5. Salirme de un torneo individual");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                if (!imprimirTorneos(false)) {
                    System.out.println("No hay torneos creados.");
                }
            }
            case 2 -> inscribirEquipoATorneo();
            case 3 -> inscribirseComoEmpleadoATorneo();
            case 4 -> consultarHistorialTorneos();
            case 5 -> salirseComoEmpleadoDeTorneo();
            case 0 -> { }
            default -> System.out.println("Opción no válida.");
        }
    }

    private void menuTorneosFisioterapeuta() {
        System.out.println("\n--- TORNEOS ---");
        System.out.println("1. Ver torneos");
        System.out.println("2. Inscribirme a un torneo individual");
        System.out.println("3. Ver historial de mis torneos");
        System.out.println("4. Salirme de un torneo individual");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                if (!imprimirTorneos(false)) {
                    System.out.println("No hay torneos creados.");
                }
            }
            case 2 -> inscribirseComoEmpleadoATorneo();
            case 3 -> consultarHistorialTorneos();
            case 4 -> salirseComoEmpleadoDeTorneo();
            case 0 -> { }
            default -> System.out.println("Opción no válida.");
        }
    }

    // Imprime los torneos de equipos en curso y retorna false si no hay ninguno.
    private boolean imprimirTorneosDeEquiposEnCurso() {
        boolean hay = false;
        for (Torneo t : club.getTorneos().values()) {
            if (t instanceof TorneoConjunto && Torneo.EN_CURSO.equalsIgnoreCase(t.getEstado())) {
                hay = true;
                System.out.println("- " + describirTorneo(t));
            }
        }
        return hay;
    }

    private void inscribirEquipoATorneo() {
        System.out.println("\n--- INSCRIBIR UN EQUIPO EN UN TORNEO ---");
        System.out.println("Torneos de equipos en curso:");
        if (!imprimirTorneosDeEquiposEnCurso()) {
            System.out.println("No hay torneos de equipos en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        System.out.println("Equipos del club:");
        for (Equipo e : club.getEquipos().values()) {
            System.out.println("- " + e.getNombreEquipo() + " // " + e.getDeporteEquipo().getNombreDeporte()
                    + " // categoría: " + e.getCategoriaEdad());
        }
        String nombreEquipo = leerTexto("Nombre del equipo: ");
        try {
            club.inscribirEquipoTorneoConjunto(usuarioAutenticado.getIdUsuario(), idTorneo, nombreEquipo);
            guardarTodo();
            TorneoConjunto torneo = (TorneoConjunto) club.getTorneos().get(idTorneo);
            if (torneo.getEquiposCupoPreferencial().contains(nombreEquipo)) {
                System.out.println("Equipo inscrito con un cupo preferencial (por su desempeño).");
            } else {
                System.out.println("Equipo inscrito con un cupo general.");
            }
            if (torneo.isCompetitivo()) {
                System.out.println("Se registro el cobro de la tarifa del equipo: $" + Math.round(torneo.getTarifaInscripcion())
                        + " (el pago lo gestiona un tercero).");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo inscribir el equipo: " + e.getMessage());
        }
    }

    // Lo usan el entrenador y el fisioterapeuta para inscribirse ellos mismos.
    private void inscribirseComoEmpleadoATorneo() {
        System.out.println("\n--- INSCRIPCIÓN A UN TORNEO INDIVIDUAL ---");
        System.out.println("Torneos individuales en curso:");
        if (!imprimirTorneosIndividualesEnCurso()) {
            System.out.println("No hay torneos individuales en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        try {
            club.inscribirEmpleadoTorneoIndividual(usuarioAutenticado.getIdUsuario(), idTorneo);
            guardarTodo();
            System.out.println("Inscripción registrada con un cupo general.");
            if (club.getTorneos().get(idTorneo).isCompetitivo()) {
                System.out.println("Inscripción gratuita para empleados. Si queda en el primer lugar, no recibe el premio en efectivo.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo realizar la inscripción: " + e.getMessage());
        }
    }
    
    private void salirseComoEmpleadoDeTorneo() {
        System.out.println("\n--- SALIRSE DE UN TORNEO INDIVIDUAL ---");
        System.out.println("Torneos individuales en curso:");
        if (!imprimirTorneosIndividualesEnCurso()) {
            System.out.println("No hay torneos individuales en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        try {
            club.desinscribirParticipanteTorneoIndividual(usuarioAutenticado.getIdUsuario(), idTorneo);
            guardarTodo();
            System.out.println("Se eliminó su inscripción y se liberó el cupo.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo cancelar la inscripción: " + e.getMessage());
        }
    }
    
    // COMPARTIDO para Socios, Entrenadores y Fisios
    // Historial detallado de los torneos en los que participa el usuario autenticado (HU-05).
    private void consultarHistorialTorneos() {
        System.out.println("\n--- HISTORIAL DE MIS TORNEOS ---");
        String idUsuario = usuarioAutenticado.getIdUsuario();
        try {
            List<Torneo> misTorneos = club.obtenerTorneosDeParticipante(idUsuario);
            System.out.println("Sus torneos:");
            for (Torneo t : misTorneos) {
                System.out.println("- " + describirTorneo(t));
            }
            String idTorneo = leerTexto("ID del torneo a consultar: ");
            System.out.println(club.consultarHistorialTorneo(idUsuario, idTorneo));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // METODOS PARA GESTIONAR INVITADOS
    private void registrarInvitadoEnTorneo() {
        System.out.println("\n--- INSCRIBIR UN INVITADO EN UN TORNEO ---");
        System.out.println("Torneos individuales en curso:");
        if (!imprimirTorneosIndividualesEnCurso()) {
            System.out.println("No hay torneos individuales en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        String idInvitado = leerTexto("ID del invitado (Enter para crear uno nuevo): ");
        boolean nuevo = false;
        if (idInvitado.isEmpty()) {
            idInvitado = String.valueOf(contadorUsuarios);
            String nombre = leerTexto("Nombre: ");
            String apellido = leerTexto("Apellido: ");
            int edad = leerEntero("Edad: ");
            club.crearInvitado(idInvitado, nombre, apellido, edad);
            contadorUsuarios++;
            nuevo = true;
        }
        try {
            club.inscribirInvitadoTorneoIndividual(usuarioAutenticado.getIdUsuario(), idInvitado, idTorneo);
            guardarTodo();
            System.out.println("Invitado inscrito con un cupo general (ID del invitado: " + idInvitado + ").");
            if (club.getTorneos().get(idTorneo).isCompetitivo()) {
                System.out.println("Se registró el cobro de la tarifa (el pago lo gestiona un tercero).");
            }
        } catch (IllegalArgumentException e) {
            if (nuevo) {
                club.getUsuarios().remove(idInvitado);
                contadorUsuarios--;
            }
            System.out.println("No se pudo inscribir al invitado: " + e.getMessage());
        }
    }

    private void retirarInvitadoDeTorneo() {
        System.out.println("\n--- RETIRAR A UN INVITADO DE UN TORNEO ---");
        System.out.println("Torneos individuales en curso:");
        if (!imprimirTorneosIndividualesEnCurso()) {
            System.out.println("No hay torneos individuales en curso.");
            return;
        }
        String idTorneo = leerTexto("ID del torneo: ");
        String idInvitado = leerTexto("ID del invitado: ");
        try {
            club.desinscribirParticipanteTorneoIndividual(idInvitado, idTorneo);
            guardarTodo();
            System.out.println("Se eliminó la inscripción y se liberó el cupo.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo retirar al invitado: " + e.getMessage());
        }
    }
    
    private void consultarDisponibilidadEquipo() {
        System.out.println("\n--- DISPONIBILIDAD DE UN EQUIPO ---");
        System.out.println("Equipos del club:");
        for (Equipo e : club.getEquipos().values()) {
            System.out.println("- " + e.getNombreEquipo() + " | " + e.getDeporteEquipo().getNombreDeporte()
                    + " | habilitados: " + e.contarJugadoresHabilitados() + " de " + e.getSocios().size());
        }
        String nombre = leerTexto("Nombre del equipo: ");
        try {
            for (String linea : club.consultarDisponibilidadEquipo(nombre)) {
                System.out.println(linea);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo consultar: " + e.getMessage());
        }
    }
    
    private void ingresarAInstalacionReservada() {
        System.out.println("\n--- INGRESAR A UNA INSTALACIÓN RESERVADA ---");
        System.out.println("Sus reservas:");
        boolean hay = false;
        for (Reserva r : club.getReservas().values()) {
            if (r.getSocio() == usuarioAutenticado) {
                hay = true;
                System.out.println("- ID " + r.getIdReserva() + " | " + r.getInstalacion().getNombre()
                        + " | " + r.getFecha() + " " + r.getHoraInicio() + "h");
            }
        }
        if (!hay) {
            System.out.println("No tiene reservas.");
            return;
        }
        String idReserva = leerTexto("ID de la reserva: ");
        boolean lleva = leerTexto("¿Lleva consigo una bebida caliente? (s/n): ").equalsIgnoreCase("s");
        try {
            club.ingresarAInstalacion(usuarioAutenticado.getIdUsuario(), idReserva, lleva);
            System.out.println("Ingreso permitido. ¡Buen juego!");
        } catch (IllegalArgumentException e) {
            System.out.println("Ingreso denegado: " + e.getMessage());
        }
    }
    
}