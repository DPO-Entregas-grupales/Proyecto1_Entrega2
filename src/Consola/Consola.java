package Consola;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import Modelo.ClubDeportivoSeneca;
import Modelo.Tienda;
import Modelo.Autentificador;
import Usuarios.Usuario;
import Usuarios.Socio;
import Usuarios.Empleado;
import Usuarios.Administrador;
import Tienda.ArticuloTienda;
import Tienda.ProductoCafeteria;
import Persistencia.CentralPersistencia;
import Persistencia.PersistenciaClubDeportivoJson;
import Persistencia.PersistenciaTiendaJson;

public class Consola {
	
	private static final String RUTA_CLUB = "./datos/ClubDeportivo.json";
	private static final String RUTA_TIENDA = "./datos/Tienda.json";
	private static final String RUTA_USUARIOS = "./datos/Usuarios.json";
    private final ClubDeportivoSeneca club;
    private final Tienda tienda;
    private final Autentificador autentificador;
    private final Scanner scanner;
    private Usuario usuarioAutenticado;

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
    }

    public static void main(String[] args) {
        Consola consola = new Consola();
        consola.iniciar();
    }

    public void iniciar() {
    	club.cargarUsuarios(RUTA_USUARIOS);
    	club.cargarClubDeportivo(RUTA_CLUB);
    	tienda.cargarTienda(RUTA_TIENDA);
    	contadorUsuarios = obtenerMaxId(club.getUsuarios().keySet())+1;
        contadorReservas = obtenerMaxId(club.getReservas().keySet())+1;
        contadorPartidos = obtenerMaxId(club.getPartidos().keySet())+1;
    	
        System.out.println("==========================================");
        System.out.println("  BIENVENIDO AL CLUB DEPORTIVO SÉNECA");
        System.out.println("==========================================");

        boolean salirApp = false;
        while (!salirApp) {
            if (usuarioAutenticado == null) {
            	salirApp = menuAutenticacion();
            } else {
                menuPrincipal();
            }
        }
        System.out.println("Aplicación finalizada. ¡Hasta luego!");
    }

    // ==========================================
    // FLUJO DE AUTENTICACIÓN Y SESIÓN
    // ==========================================

    private boolean menuAutenticacion() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrar Usuario Inicial");
        System.out.println("0. Salir de la aplicación");

        int opcion = leerEntero("Seleccione una opción: ");
        switch (opcion) {
            case 1 -> {
                String id = leerTexto("ID Usuario: ");
                String pass = leerTexto("Contraseña: ");

                List<Usuario> listaUsuarios = new ArrayList<>(club.getUsuarios().values());
                Usuario u = autentificador.autenticar(id, pass, listaUsuarios);

                if (u != null) {
                    this.usuarioAutenticado = u;
                    System.out.println("\n>>> Bienvenido, " + u.getNombre() + " " + u.getApellido() + " <<<");
                } else {
                    System.out.println("Credenciales incorrectas o usuario no registrado.");
                }
            }
            case 2 -> registrarUsuarioInicial();
            case 0 -> {
            	club.salvarUsuarios(RUTA_USUARIOS);
                club.salvarClubDeportivo(RUTA_CLUB);
                tienda.salvarTienda(RUTA_TIENDA);
                return true;
            }
            default -> System.out.println("Opción inválida.");
        }
        return false;
    }
    
    

    private void registrarUsuarioInicial() {
        System.out.println("\n--- REGISTRO DE USUARIO ---");
        System.out.println("1. Registrar Socio");
        System.out.println("2. Registrar Entrenador");
        System.out.println("3. Registrar Fisioterapeuta");
        System.out.println("4. Registrar Administrador");
        System.out.println("0. Volver");

        int op = leerEntero("Seleccione el tipo de usuario: ");
        switch (op) {
            case 1 -> registrarSocio();
            case 2 -> registrarEntrenador();
            case 3 -> registrarFisioterapeuta();
            case 4 -> registrarAdministrador();
            case 0 -> System.out.println("Volviendo...");
            default -> System.out.println("Opción no válida.");
        }
    }

    private void registrarSocio() {
        System.out.println("\n--- REGISTRO DE SOCIO ---");
        String id = String.valueOf(contadorUsuarios++);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        String pass = leerTexto("Contraseña: ");
        int edad = leerEntero("Edad: ");

        club.crearSocio(id, nombre, apellido, login, pass, 0, edad, true);
        System.out.println("Socio registrado con éxito (ID: " + id + ").");
    }

    private void registrarEntrenador() {
        System.out.println("\n--- REGISTRO DE ENTRENADOR ---");
        String id = String.valueOf(contadorUsuarios++);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        String pass = leerTexto("Contraseña: ");
        int puntos = leerEntero("Puntos iniciales: ");
        String codigo = leerTexto("Código descuento: ");

        club.crearEntrenador(id, nombre, apellido, login, pass, puntos, codigo);
        System.out.println("Entrenador registrado con éxito (ID: " + id + ").");
    }

    private void registrarFisioterapeuta() {
        System.out.println("\n--- REGISTRO DE FISIOTERAPEUTA ---");
        String id = String.valueOf(contadorUsuarios++);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        String pass = leerTexto("Contraseña: ");
        int puntos = leerEntero("Puntos iniciales: ");
        String codigo = leerTexto("Código descuento: ");

        club.crearFisioterapeuta(id, nombre, apellido, login, pass, puntos, codigo);
        System.out.println("Fisioterapeuta registrado con éxito (ID: " + id + ").");
    }

    private void registrarAdministrador() {
        System.out.println("\n--- REGISTRO DE ADMINISTRADOR ---");
        String id = String.valueOf(contadorUsuarios++);
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String login = leerTexto("Login: ");
        String pass = leerTexto("Contraseña: ");
        int puntos = leerEntero("Puntos iniciales: ");

        Administrador admin = new Administrador(id, nombre, apellido, login, pass, puntos);
        club.agregarUsuario(admin);
        System.out.println("Administrador registrado con éxito (ID: " + id + ").");
    }

    private void cerrarSesion() {
        System.out.println("Sesión cerrada para: " + usuarioAutenticado.getNombre());
        this.usuarioAutenticado = null;
    }

    // ==========================================
    // MENÚ PRINCIPAL Y MÓDULOS
    // ==========================================

    private void menuPrincipal() {
        System.out.println("\n==========================================");
        System.out.println("  SESIÓN ACTIVA: " + usuarioAutenticado.getNombre() + " " + usuarioAutenticado.getApellido());
        System.out.println("==========================================");
        System.out.println("1. Gestión de Usuarios");
        System.out.println("2. Deportes, Equipos e Inscripciones");
        System.out.println("3. Instalaciones y Reservas");
        System.out.println("4. Tienda y Cafetería");
        System.out.println("5. Partidos y Entrenamientos");
        System.out.println("6. Guardar");
        System.out.println("9. Cerrar Sesión");
        System.out.println("0. Salir de la aplicación");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> menuUsuarios();
            case 2 -> menuDeportesYEquipos();
            case 3 -> menuInstalacionesYReservas();
            case 4 -> menuTiendaYCafeteria();
            case 5 -> menuPartidosYEntrenamientos();
            case 6 -> menuPersistencia();
            case 9 -> cerrarSesion();
            case 0 -> {
            	club.salvarUsuarios(RUTA_USUARIOS);
                club.salvarClubDeportivo(RUTA_CLUB);
                tienda.salvarTienda(RUTA_TIENDA);
            	System.out.println("Aplicación finalizada. ¡Hasta luego!");
            	System.exit(0);}
            default -> System.out.println("Opción no válida.");
        }
    }

    private void menuUsuarios() {
        System.out.println("\n--- MÓDULO DE USUARIOS ---");
        System.out.println("1. Registrar Socio");
        System.out.println("2. Registrar Entrenador");
        System.out.println("3. Registrar Fisioterapeuta");
        System.out.println("4. Registrar Administrador");
        System.out.println("5. Consultar Usuario");
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
        }
    }

    private void menuDeportesYEquipos() {
        System.out.println("\n--- DEPORTES Y EQUIPOS ---");
        System.out.println("1. Crear Deporte");
        System.out.println("2. Crear Equipo");
        System.out.println("3. Crear Competencia");
        System.out.println("4. Inscribir Socio a Equipo");
        System.out.println("5. Realizar Inscripción a Competencia");
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
                club.crearDeporte(nombre, minJugadores, new ArrayList<>(), empate, pVictoria, pDerrota, pEmpate);
                System.out.println("Deporte creado.");
            }
            case 2 -> {
                String nombre = leerTexto("Nombre equipo: ");
                int cupo = leerEntero("Cupo máximo: ");
                int catEdad = leerEntero("Categoría edad: ");
                String deporte = leerTexto("Nombre deporte: ");
                club.crearEquipo(nombre, cupo, catEdad, deporte, true);
                System.out.println("Equipo creado.");
            }
            case 3 -> {
                String nombre = leerTexto("Nombre competencia: ");
                String entidad = leerTexto("Entidad organizadora: ");
                String anio = leerTexto("Año/Temporada: ");
                String formato = leerTexto("Formato: ");
                String fase = leerTexto("Fase actual: ");
                int cat = leerEntero("Categoría: ");
                club.crearCompetencia(nombre, entidad, anio, formato, fase, cat);
                System.out.println("Competencia creada.");
            }
            case 4 -> {
                String idSocio = leerTexto("ID Socio: ");
                String equipo = leerTexto("Nombre Equipo: ");
                String posicion = leerTexto("Posición: ");
                int numero = leerEntero("Número camiseta: ");
                club.crearInscripcionEquipo(idSocio, equipo, posicion, numero, 0);
                System.out.println("Inscripción de equipo registrada.");
            }
            case 5 -> {
                int puntos = leerEntero("Puntos acumulados: ");
                String posicion = leerTexto("Posición: ");
                String equipo = leerTexto("Nombre Equipo: ");
                String competencia = leerTexto("Nombre Competencia: ");
                List<Socio> nomina = pedirNominaJugadores();

                club.realizarInscripcionCompetencia(puntos, posicion, equipo, competencia, nomina);
                System.out.println("Proceso de inscripción a competencia finalizado.");
            }
        }
    }

    private void menuInstalacionesYReservas() {
        System.out.println("\n--- INSTALACIONES Y RESERVAS ---");
        System.out.println("1. Registrar Instalación");
        System.out.println("2. Generar Horarios");
        System.out.println("3. Realizar Reserva");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String nombre = leerTexto("Nombre instalación: ");
                String tipo = leerTexto("Tipo: ");
                boolean techada = leerTexto("¿Techada? (s/n): ").equalsIgnoreCase("s");
                int cap = leerEntero("Capacidad: ");
                club.crearInstalacion(nombre, tipo, techada, cap);
                System.out.println("Instalación creada.");
            }
            case 2 -> {
                String nombre = leerTexto("Instalación: ");
                String fecha = leerTexto("Fecha (YYYY-MM-DD): ");
                int hInicio = leerEntero("Hora inicio (0-23): ");
                int hFin = leerEntero("Hora fin (0-23): ");
                club.nuevosHorarios(nombre, fecha, hInicio, hFin);
                System.out.println("Horarios agregados.");
            }
            case 3 -> {
                String idReserva = String.valueOf(contadorReservas++);
                String inst = leerTexto("Instalación: ");
                String fecha = leerTexto("Fecha: ");
                String hora = leerTexto("Hora inicio: ");
                int cant = leerEntero("Cantidad jugadores: ");
                String mod = leerTexto("Modalidad: ");
                club.crearReserva(idReserva, usuarioAutenticado.getIdUsuario(), inst, fecha, hora, cant, mod);
                System.out.println("Proceso de reserva ejecutado (ID: " + idReserva + ").");
            }
        }
    }

    private void menuTiendaYCafeteria() {
        System.out.println("\n--- TIENDA Y CAFETERÍA ---");
        System.out.println("1. Crear Artículo de Tienda");
        System.out.println("2. Crear Producto de Cafetería");
        System.out.println("3. Ver Catálogo Tienda");
        System.out.println("4. Ver Catálogo Cafetería");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String nombre = leerTexto("Nombre artículo: ");
                double precio = leerDouble("Precio base: ");
                int cant = leerEntero("Stock: ");
                String cat = leerTexto("Categoría: ");
                tienda.crearArticuloTienda(nombre, precio, cant, cat);
                System.out.println("Artículo registrado.");
            }
            case 2 -> {
                String nombre = leerTexto("Nombre producto: ");
                double precio = leerDouble("Precio base: ");
                int cant = leerEntero("Stock: ");
                boolean caliente = leerTexto("¿Bebida caliente? (s/n): ").equalsIgnoreCase("s");
                String alergenos = leerTexto("Alérgenos: ");
                tienda.crearProductoCafeteria(nombre, precio, cant, caliente, alergenos);
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
        }
    }

    private void menuPartidosYEntrenamientos() {
        System.out.println("\n--- PARTIDOS Y ENTRENAMIENTOS ---");
        System.out.println("1. Crear Partido Interno");
        System.out.println("2. Crear Partido Competencia");
        System.out.println("3. Realizar Partido Competencia");
        System.out.println("4. Realizar Partido Interno");
        System.out.println("0. Volver");

        int op = leerEntero("Opción: ");
        switch (op) {
            case 1 -> {
                String id = String.valueOf(contadorPartidos++);
                String fecha = leerTexto("Fecha: ");
                String e1 = leerTexto("Equipo 1: ");
                String e2 = leerTexto("Equipo 2: ");
                club.crearPartidoInterno(id, fecha, "0-0", "Programado", e1, e2);
                System.out.println("Partido interno programado (ID: " + id + ").");
            }
            case 2 -> {
                String id = String.valueOf(contadorPartidos++);
                String fecha = leerTexto("Fecha: ");
                String equipo = leerTexto("Equipo Local: ");
                String rival = leerTexto("Rival: ");
                String escenario = leerTexto("Escenario: ");
                club.crearPartidoCompetencia(id, fecha, "0-0", "Programado", equipo, rival, escenario);
                System.out.println("Partido de competencia programado (ID: " + id + ").");
            }
            case 3 -> {
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
                System.out.println("Proceso de partido de competencia ejecutado (ID: " + id + ").");
            }
            case 4 -> {
                String id = String.valueOf(contadorPartidos++);
                String fecha = leerTexto("Fecha: ");
                String marcador = leerTexto("Marcador Final: ");
                String estado = leerTexto("Estado Partido: ");
                String e1 = leerTexto("Equipo 1: ");
                String e2 = leerTexto("Equipo 2: ");

                club.realizarPartidoInterno(id, fecha, marcador, estado, e1, e2);
                System.out.println("Proceso de partido interno ejecutado (ID: " + id + ").");
            }
        }
    }

    private void menuPersistencia() {
    	club.salvarUsuarios(RUTA_USUARIOS);
    	System.out.println("Guardado de usuarios ejecutado.");
        club.salvarClubDeportivo(RUTA_CLUB);
        System.out.println("Guardado de datos ejecutado.");
        tienda.salvarTienda(RUTA_TIENDA);
        System.out.println("Guardado de tiendas ejecutado.");
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

    
    
    
    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

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
    
    
    
    // ==========================================
    // MÉTODOS AUXILIARES DE LECTURA ROBUSTA
    // ==========================================

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
    
}