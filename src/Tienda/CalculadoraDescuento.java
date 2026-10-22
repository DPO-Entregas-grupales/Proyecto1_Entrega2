package Tienda;

import java.util.ArrayList;
import java.util.List;
import Usuarios.Empleado;
import Usuarios.Usuario;

public class CalculadoraDescuento {
    static double DESCUENTO_PARA_EMPLEADOS = 0.15;
    static double DESCUENTO_GENERAL = 0.08;
    private List<String> codigosValidos;

    public CalculadoraDescuento() {
        this.codigosValidos = new ArrayList<>();
    }

    public void agregarCodigoValido(String codigo) {
        if (codigo != null && !codigo.trim().isEmpty() && !this.codigosValidos.contains(codigo.trim())) {
            this.codigosValidos.add(codigo.trim());
        }
    }

    // Devuelve el porcentaje de descuento (0 si no hay código). Lanza IllegalArgumentException si el código no es válido.
    public double calcularDescuento(Usuario usuario, String codigoDescuento) {
        if (codigoDescuento == null || codigoDescuento.trim().isEmpty()) {
            return 0;
        }
        String codigo = codigoDescuento.trim();
        if (!codigosValidos.contains(codigo)) {
            throw new IllegalArgumentException("El código de descuento no es válido");
        }
        if (usuario instanceof Empleado && codigo.equals(((Empleado) usuario).getCodigoDescuento())) {
            return DESCUENTO_PARA_EMPLEADOS;
        }
        return DESCUENTO_GENERAL;
    }
}