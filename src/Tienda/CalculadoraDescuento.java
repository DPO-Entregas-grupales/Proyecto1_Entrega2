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
        this.codigosValidos.add(codigo);
    }

    public double calcularDescuento(Usuario usuario, String codigoDescuento) {
        if (usuario instanceof Empleado && codigosValidos.contains(codigoDescuento)) {
            return DESCUENTO_PARA_EMPLEADOS;
        }
        if (codigosValidos.contains(codigoDescuento)) {
            return DESCUENTO_GENERAL;
        }
        return 0;
    }
}