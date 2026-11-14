package Turnos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Usuarios.Entrenador;
import Usuarios.Fisioterapeuta;

@DisplayName("Cobertura Mínima de Personal en Turnos")
public class TurnoTest {

    // Cobertura operativa: exige al menos 1 fisioterapeuta y 2 entrenadores asignados al turno.
    @Test
    @DisplayName("Verifica cobertura mínima de 1 fisioterapeuta y 2 entrenadores")
    void testCoberturaMinimaPersonal() {
        Turno turno = new Turno("T-LUN-AM", 1, true);
        Entrenador ent1 = new Entrenador("E1", "Carlos", "DT1", "c1", "123", 0, "EMP-1");
        Entrenador ent2 = new Entrenador("E2", "Luis", "DT2", "c2", "123", 0, "EMP-2");
        Fisioterapeuta fis1 = new Fisioterapeuta("F1", "Ana", "Fisio", "f1", "123", 0, "EMP-3");

        turno.agregarEmpleado(ent1);
        turno.agregarEmpleado(fis1);
        assertFalse(turno.cumpleFisioterapeutaEntrenadores());

        turno.agregarEmpleado(ent2);
        assertTrue(turno.cumpleFisioterapeutaEntrenadores());

        turno.quitarEmpleado(fis1);
        assertFalse(turno.cumpleFisioterapeutaEntrenadores());
    }
}
