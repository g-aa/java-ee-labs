package task3_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDateTime extends Remote {

    String getDate() throws RemoteException;
    String getTime() throws RemoteException;
    boolean stop() throws RemoteException;

}