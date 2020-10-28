package task3_rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMI_Server {

    public static final String BINDING_NAME = "DateTimer";
    public static final int SERVER_PORT = 9000;

    public static void main(String[] args) {
        try {
            System.out.println("Сервер запущен...");

            MyDateTime dateTimer = new MyDateTime();
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            System.out.println("registry создан");

            IDateTime stub = (IDateTime) UnicastRemoteObject.exportObject(dateTimer, 0);
            registry.bind(BINDING_NAME, stub);
            System.out.println("заглушка создана");

            while (true) {
                if (dateTimer.getStopFlag()) {
                    registry.unbind(BINDING_NAME);
                    UnicastRemoteObject.unexportObject(dateTimer, true);
                    break;
                }
            }
            System.out.println("Сервер остановлен.");
        }
        catch (RemoteException | AlreadyBoundException | NotBoundException e) {
            System.out.println(e.getMessage());
        }
    }
}