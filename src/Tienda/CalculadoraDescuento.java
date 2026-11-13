package Tienda;

import java.util.ArrayList;
import java.util.List;
import Usuarios.Empleado;
import Usuarios.Socio;
import Usuarios.Usuario;

public class CalculadoraDescuento {
    public static double DESCUENTO_PARA_EMPLEADOS = 0.15;
    public static double DESCUENTO_GENERAL = 0.08;
    public static double DESCUENTO_BONO_TORNEO = 0.10;
    private List<String> codigosValidos;
    private List<String> bonosTorneoValidos;

    public CalculadoraDescuento() {
        this.codigosValidos = new ArrayList<>();
        this.bonosTorneoValidos = new ArrayList<>();
    }

    public void agregarCodigoValido(String codigo) {
        if (codigo != null && !codigo.trim().isEmpty() && !this.codigosValidos.contains(codigo.trim())) {
            this.codigosValidos.add(codigo.trim());
        }
    }

    public void agregarBonoTorneoValido(String codigoBono) {
        if (codigoBono != null && !codigoBono.trim().isEmpty()) {
            String limpio = codigoBono.trim();
            agregarCodigoValido(limpio);
            if (!this.bonosTorneoValidos.contains(limpio)) {
                this.bonosTorneoValidos.add(limpio);
            }
        }
    }

    public boolean esBonoTorneo(String codigo) {
        return codigo != null && (this.bonosTorneoValidos.contains(codigo.trim()) || codigo.trim().startsWith("BONO-"));
    }

    public void consumirBonoTorneo(String codigoBono) {
        if (codigoBono != null) {
            String limpio = codigoBono.trim();
            this.bonosTorneoValidos.remove(limpio);
            this.codigosValidos.remove(limpio);
        }
    }

    public List<String> getBonosTorneoValidos() {
        return bonosTorneoValidos;
    }

    public double calcularDescuento(Usuario usuario, String codigoDescuento) {
        if (codigoDescuento == null || codigoDescuento.trim().isEmpty()) {
            return 0;
        }
        String codigo = codigoDescuento.trim();

        // Validación específica para bonos de torneo (HU-06)
        if (esBonoTorneo(codigo)) {
            if (!(usuario instanceof Socio) || !((Socio) usuario).tieneBonosDisponibles()) {
                throw new IllegalArgumentException("No tiene ningún bono de descuento por redimir");
            }
            if (!bonosTorneoValidos.contains(codigo) || !((Socio) usuario).tieneBonoDescuento(codigo)) {
                throw new IllegalArgumentException("El bono de descuento no es válido para este socio");
            }
            return DESCUENTO_BONO_TORNEO;
        }

        if (!codigosValidos.contains(codigo)) {
            throw new IllegalArgumentException("El código de descuento no es válido");
        }
        if (usuario instanceof Empleado && codigo.equals(((Empleado) usuario).getCodigoDescuento())) {
            return DESCUENTO_PARA_EMPLEADOS;
        }
        return DESCUENTO_GENERAL;
    }
}