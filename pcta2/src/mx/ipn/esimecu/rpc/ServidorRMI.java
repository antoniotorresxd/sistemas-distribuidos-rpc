package mx.ipn.esimecu.rpc;

import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class ServidorRMI {
    public static void main(String[] args) throws Exception {
        SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();

        LocateRegistry.createRegistry(1099);

        BitacoraRemota bitacora = new BitacoraRemotaImpl(Path.of("/tmp/bitacora.log"), csf, ssf);
        Naming.rebind("rmi://localhost:1099/BitacoraIPN", bitacora);

        Calculadora servicio = new CalculadoraImpl(bitacora, csf, ssf);
        Naming.rebind("rmi://localhost:1099/CalculadoraIPN", servicio);

        System.out.println("Servidor RMI (TLS) listo en rmi://localhost:1099/CalculadoraIPN");
        System.out.println("Bitácora remota publicada en rmi://localhost:1099/BitacoraIPN");
    }
}
