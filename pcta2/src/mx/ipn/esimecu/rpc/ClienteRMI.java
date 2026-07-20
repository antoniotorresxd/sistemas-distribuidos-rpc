package mx.ipn.esimecu.rpc;

import java.rmi.Naming;
import java.util.List;

public class ClienteRMI {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        Calculadora c = (Calculadora) Naming.lookup("rmi://" + host + ":1099/CalculadoraIPN");
        BitacoraRemota b = (BitacoraRemota) Naming.lookup("rmi://" + host + ":1099/BitacoraIPN");

        System.out.println("Conectado a " + c.quienSoy());
        System.out.println("3 + 4  = " + c.sumar(3, 4));
        System.out.println("10 - 6 = " + c.restar(10, 6));
        System.out.println("7 * 8  = " + c.multiplicar(7, 8));
        try {
            System.out.println("5 / 0  = " + c.dividir(5, 0));
        } catch (Exception e) {
            System.out.println("Error remoto controlado: " + e.getMessage());
        }

        System.out.println("\n--- Historial (BitacoraRemota) ---");
        List<String> historial = b.consultar();
        historial.forEach(System.out::println);
    }
}
