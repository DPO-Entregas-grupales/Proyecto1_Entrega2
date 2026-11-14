package Modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ReservasInstalaciones.Instalacion;
import ReservasInstalaciones.Reserva;
import Tienda.ArticuloTienda;
import Tienda.ItemVenta;
import Tienda.ProductoCafeteria;
import Tienda.Venta;
import Usuarios.Socio;

@DisplayName("Operaciones de Tienda Deportiva y Cafetería")
public class TiendaTest {

    private ClubDeportivoSeneca club;
    private Tienda tienda;
    private Socio socio;
    private ArticuloTienda raqueta;
    private ProductoCafeteria cafeCaliente;
    private ProductoCafeteria jugoFrio;

    @BeforeEach
    void setUp() {
        club = new ClubDeportivoSeneca();
        tienda = new Tienda();

        club.crearSocio("SOC-1", "Valeria", "Ortiz", "valeria.o", "1234", 5000, 23, true);
        socio = (Socio) club.getUsuarios().get("SOC-1");

        tienda.crearArticuloTienda("Raqueta Pro", 100000.0, 5, "Raquetas");
        tienda.crearProductoCafeteria("Café Americano", 5000.0, 20, true, "Cafeína");
        tienda.crearProductoCafeteria("Jugo Natural", 6000.0, 15, false, "Fructosa");

        raqueta = tienda.getArticulosTienda().get("Raqueta Pro");
        cafeCaliente = tienda.getProductosCafeteria().get("Café Americano");
        jugoFrio = tienda.getProductosCafeteria().get("Jugo Natural");
    }

    // HU-06: Redime bono de torneo en tienda deportiva, descuenta inventario y elimina el bono del socio y de la tienda.
    @Test
    @DisplayName("Confirma compra con bono de torneo y consume el bono")
    void testFlujoCompletoBonoTorneo() {
        String bono = "BONO-TOR01-SOC-1";
        socio.agregarBonoDescuento(bono);
        tienda.registrarBonoTorneo(bono);

        Venta enProgreso = tienda.iniciarCompraTienda("V-200", "2026-11-12", 15, "SOC-1",
                Collections.singletonList(new ItemVenta(1, raqueta)), club);
        tienda.aplicarBonoDescuento(enProgreso, bono);
        tienda.confirmarCompraTienda(enProgreso);

        assertEquals(107100.0, enProgreso.getTotal(), 0.01);
        assertEquals(4, raqueta.getCantidadInventario());
        assertFalse(socio.tieneBonoDescuento(bono));
        assertFalse(tienda.getBonosValidos().contains(bono));
    }

    // HU-06 CA-2 / CA-3: Impide acumular dos bonos en una compra o redimir el bono de otro socio.
    @Test
    @DisplayName("Rechaza acumulación de bonos y uso de bonos ajenos")
    void testRestriccionesBonosTorneo() {
        socio.agregarBonoDescuento("BONO-1-SOC-1");
        socio.agregarBonoDescuento("BONO-2-SOC-1");
        tienda.registrarBonoTorneo("BONO-1-SOC-1");
        tienda.registrarBonoTorneo("BONO-2-SOC-1");
        tienda.registrarBonoTorneo("BONO-AJENO-SOC-99");

        Venta enProgreso = tienda.iniciarCompraTienda("V-201", "2026-11-12", 15, "SOC-1",
                Collections.singletonList(new ItemVenta(1, raqueta)), club);
        tienda.aplicarBonoDescuento(enProgreso, "BONO-1-SOC-1");

        assertThrows(IllegalArgumentException.class, () -> tienda.aplicarBonoDescuento(enProgreso, "BONO-2-SOC-1"));

        Venta otraVenta = tienda.iniciarCompraTienda("V-202", "2026-11-12", 15, "SOC-1",
                Collections.singletonList(new ItemVenta(1, raqueta)), club);
        assertThrows(IllegalArgumentException.class, () -> tienda.aplicarBonoDescuento(otraVenta, "BONO-AJENO-SOC-99"));
    }

    // 1HU14 / HU-JUG-01: Bloquea venta de bebidas calientes a socio con reserva activa en pádel techado, pero permite frías.
    @Test
    @DisplayName("Bloquea bebida caliente en cafetería si el socio tiene reserva activa techada")
    void testBloqueoBebidaCalienteConReservaActiva() {
        Instalacion padelTechada = new Instalacion("Pádel 1", "Cancha de Pádel", true, 4);
        String hoy = LocalDate.now().toString();
        String horaAhora = String.valueOf(LocalTime.now().getHour());
        socio.agregarReserva(new Reserva("RES-ACT", socio, padelTechada, hoy, horaAhora, 4, "Dobles"));

        assertThrows(IllegalArgumentException.class, () -> tienda.realizarVenta(
                "V-301", hoy, 12, 0.08, "SOC-1", "",
                Collections.singletonList(new ItemVenta(1, cafeCaliente)), club, Venta.CAFETERIA));
        assertEquals(20, cafeCaliente.getCantidadInventario());

        assertDoesNotThrow(() -> tienda.realizarVenta(
                "V-302", hoy, 12, 0.08, "SOC-1", "",
                Collections.singletonList(new ItemVenta(1, jugoFrio)), club, Venta.CAFETERIA));
    }

    // Control de inventario y cafetería: calcula impuesto (8%), propina (10%), puntos (2%) y bloquea ventas sin stock.
    @Test
    @DisplayName("Calcula venta de cafetería y rechaza ventas con inventario insuficiente")
    void testCafeteriaYControlStock() {
        List<ItemVenta> itemsCafe = Arrays.asList(new ItemVenta(2, cafeCaliente), new ItemVenta(1, jugoFrio));
        tienda.realizarVenta("V-101", "2026-11-12", 11, 0.08, "SOC-1", "", itemsCafe, club, Venta.CAFETERIA);

        Venta venta = tienda.getVentas().get(0);
        assertEquals(18880.0, venta.getTotal(), 0.01);
        assertEquals(5377, socio.getPuntosFidelidad());

        List<ItemVenta> exceso = Collections.singletonList(new ItemVenta(10, raqueta));
        assertThrows(IllegalArgumentException.class, () -> tienda.realizarVenta(
                "V-303", "2026-11-12", 12, 0.19, "SOC-1", "", exceso, club, Venta.TIENDADEPORTIVA));
        assertEquals(5, raqueta.getCantidadInventario());
    }
}
