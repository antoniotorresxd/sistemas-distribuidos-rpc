package mx.ipn.esimecu.rpc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// Reto de extensión: segundo objeto remoto para consultar el historial
// de invocaciones registradas por CalculadoraImpl.
public interface BitacoraRemota extends Remote {
    void registrar(String entrada) throws RemoteException;
    List<String> consultar() throws RemoteException;
}
