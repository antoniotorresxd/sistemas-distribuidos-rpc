package mx.ipn.esimecu.rpc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/*
 * Backend JVM REST independiente que nginx expone como el "ramo 2" del
 * gateway heterogéneo (proxy inverso hacia /api/v1/saldo). Usa el
 * HttpServer embebido del JDK para no depender de un framework externo
 * ni de descargas adicionales.
 */
public class ServicioSaldoJVM {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/saldo", new SaldoHandler());
        server.start();
        System.out.println("Servicio JVM REST escuchando en :9090/saldo");
    }

    static class SaldoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws java.io.IOException {
            String cuenta = "IPN-ESIMECU-0001";
            String json = "{\"cuenta\":\"" + cuenta + "\",\"saldo\":15340.75,\"moneda\":\"MXN\"}";
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
    }
}
