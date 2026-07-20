package mx.ipn.esimecu.rpc;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class CalculadoraImpl extends UnicastRemoteObject implements Calculadora {
    private static final long serialVersionUID = 1L;
    private final BitacoraRemota bitacora;

    protected CalculadoraImpl(BitacoraRemota bitacora, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
            throws RemoteException {
        super(0, csf, ssf);
        this.bitacora = bitacora;
    }

    private void log(String op, double a, double b, double r) throws RemoteException {
        if (bitacora != null) bitacora.registrar(op + "(" + a + ", " + b + ") = " + r);
    }

    @Override public double sumar(double a, double b) throws RemoteException {
        double r = a + b; log("sumar", a, b, r); return r;
    }
    @Override public double restar(double a, double b) throws RemoteException {
        double r = a - b; log("restar", a, b, r); return r;
    }
    @Override public double multiplicar(double a, double b) throws RemoteException {
        double r = a * b; log("multiplicar", a, b, r); return r;
    }

    @Override
    public double dividir(double a, double b) throws RemoteException {
        if (b == 0.0) {
            if (bitacora != null) bitacora.registrar("dividir(" + a + ", " + b + ") -> ERROR división entre cero");
            throw new RemoteException("División entre cero");
        }
        double r = a / b; log("dividir", a, b, r); return r;
    }

    @Override
    public String quienSoy() throws RemoteException {
        try {
            return "Calculadora remota en " + InetAddress.getLocalHost();
        } catch (Exception e) {
            throw new RemoteException("No se pudo resolver el host", e);
        }
    }
}
