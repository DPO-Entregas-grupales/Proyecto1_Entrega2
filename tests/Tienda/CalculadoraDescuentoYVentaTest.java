package Tienda;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Usuarios.Entrenador;
import Usuarios.Socio;

@DisplayName("Descuentos, Bonos de Torneo y Redención de Puntos en Venta")
public class CalculadoraDescuentoYVentaTest {

    // Reglas de descuento: Aplica 0% sin código, 8% código general, 15% empleado y 10% bono único de torneo.
    @Test
    @DisplayName("Calcula porcentajes de descuento por rol y consume bono de torneo")
    void testPorcentajesDescuentoYConsumoBono() {
        CalculadoraDescuento calc = new CalculadoraDescuento();
        calc.agregarCodigoValido("GEN8");
        calc.agregarCodigoValido("EMP-CARLOS");

        Socio socio = new Socio("S1", "Ana", "Gómez", "ana", "123", 0, 22, true);
        Entrenador entrenador = new Entrenador("E1", "Carlos", "DT", "carlos", "123", 0, "EMP-CARLOS");

        String bono = "BONO-T1-S1";
        socio.agregarBonoDescuento(bono);
        calc.agregarBonoTorneoValido(bono);

        assertEquals(0.0, calc.calcularDescuento(socio, ""), 0.001);
        assertEquals(0.08, calc.calcularDescuento(socio, "GEN8"), 0.001);
        assertEquals(0.15, calc.calcularDescuento(entrenador, "EMP-CARLOS"), 0.001);
        assertEquals(0.10, calc.calcularDescuento(socio, bono), 0.001);
        assertThrows(IllegalArgumentException.class, () -> calc.calcularDescuento(entrenador, bono));

        calc.consumirBonoTorneo(bono);
        assertThrows(IllegalArgumentException.class, () -> calc.calcularDescuento(socio, bono));
    }

    // HU-06: Aplica bono único antes de IVA, impide acumular bonos y recalcula puntos tras redención.
    @Test
    @DisplayName("Aplica bono no acumulable y redime puntos de fidelidad en Venta")
    void testVentaConBonoYPuntos() {
        Socio socio = new Socio("S1", "Ana", "Gómez", "ana", "123", 5000, 22, true);
        String bono = "BONO-T1-S1";
        socio.agregarBonoDescuento(bono);

        CalculadoraDescuento calc = new CalculadoraDescuento();
        calc.agregarBonoTorneoValido(bono);

        ArticuloTienda tenis = new ArticuloTienda("Tenis Court", 200000.0, 5, "Calzado");
        Venta venta = new Venta("V-10", "2026-11-15", 16, Venta.TIENDADEPORTIVA, socio);
        venta.setItemsVenta(Collections.singletonList(new ItemVenta(1, tenis)));

        venta.aplicarBonoDescuento(bono, calc);
        assertEquals(20000.0, venta.getDescuento(), 0.01);
        assertEquals(214200.0, venta.getTotal(), 0.01);
        assertThrows(IllegalArgumentException.class, () -> venta.aplicarBonoDescuento(bono, calc));

        venta.restarPuntosAlTotal(4200);
        assertEquals(210000.0, venta.getTotal(), 0.01);
        assertEquals(4200, (int) venta.getPuntosGanados());
    }
}
