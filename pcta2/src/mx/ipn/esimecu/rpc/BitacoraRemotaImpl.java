package mx.ipn.esimecu.rpc;

import java.io.IOException;
import java.nio.file.*;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

/*
 * Bitácora persistida en un archivo de texto plano (append-only) en vez de
 * una base de datos relacional (PostgreSQL/SQLite), dado que el contenedor
 * de esta práctica no cuenta con salida a Internet para descargar un
 * controlador JDBC. El archivo cumple el mismo rol didáctico: un segundo
 * objeto remoto con estado persistente, consultable por el cliente.
 */
public class BitacoraRemotaImpl extends UnicastRemoteObject implements BitacoraRemota {
    private static final long serialVersionUID = 1L;
    private final Path archivo;

    protected BitacoraRemotaImpl(Path archivo, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
            throws RemoteException {
        super(0, csf, ssf);
        this.archivo = archivo;
    }

    @Override
    public synchronized void registrar(String entrada) throws RemoteException {
        String linea = LocalDateTime.now() + " | " + entrada + System.lineSeparator();
        try {
            Files.write(archivo, linea.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RemoteException("No se pudo escribir la bitácora", e);
        }
    }

    @Override
    public synchronized List<String> consultar() throws RemoteException {
        try {
            if (!Files.exists(archivo)) return List.of();
            return Files.readAllLines(archivo);
        } catch (IOException e) {
            throw new RemoteException("No se pudo leer la bitácora", e);
        }
    }
}
