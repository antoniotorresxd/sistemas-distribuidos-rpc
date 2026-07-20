package mx.ipn.esimecu.rpc;

import java.rmi.Naming;

/*
 * Medición de latencia de invocación remota pura (excluyendo el costo de
 * arranque de la JVM y de Naming.lookup), para comparar contra la práctica 3.
 */
public class LatencyBench {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int n = args.length > 1 ? Integer.parseInt(args[1]) : 50;

        Calculadora c = (Calculadora) Naming.lookup("rmi://" + host + ":1099/CalculadoraIPN");

        // Calentamiento: la primera invocación paga la carga de clases del stub.
        for (int i = 0; i < 5; i++) c.sumar(1, 1);

        long[] tiemposNs = new long[n];
        for (int i = 0; i < n; i++) {
            long t0 = System.nanoTime();
            c.sumar(3, 4);
            tiemposNs[i] = System.nanoTime() - t0;
        }

        double sumaMs = 0, minMs = Double.MAX_VALUE, maxMs = 0;
        for (long ns : tiemposNs) {
            double ms = ns / 1_000_000.0;
            sumaMs += ms;
            minMs = Math.min(minMs, ms);
            maxMs = Math.max(maxMs, ms);
        }
        double avgMs = sumaMs / n;

        System.out.printf("RMI sumar(): n=%d avg=%.3f ms min=%.3f ms max=%.3f ms%n", n, avgMs, minMs, maxMs);
    }
}
