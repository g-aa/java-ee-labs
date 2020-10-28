package task3_rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.*;

public class RMI_Client {

    public static void main(String[] args) {
        try {
            System.out.println("Клиент запущен...");

            Registry registry = LocateRegistry.getRegistry(RMI_Server.SERVER_PORT);
            System.out.println("registry получен");

            IDateTime myDateTime = (IDateTime) registry.lookup(RMI_Server.BINDING_NAME);
            System.out.println("dateTime создан");

            boolean stopFlag = false;
            while(!stopFlag) {
                try {
                    System.out.println("\nвведите одну из следующих команд:"
                            + "\n0 - получить текущую дату;"
                            + "\n1 - получить текущее время;"
                            + "\n2 - остановить сервер и закрыть приложение.");
                    System.out.print(">");

                    int cIndex = Integer.parseInt(new Scanner(System.in).nextLine());
                    switch (cIndex) {
                        case 0:
                            // распечатать дату:
                            System.out.println("Дата сервера: " + myDateTime.getDate());
                            break;
                        case 1:
                            // распечатать время:
                            System.out.println("Время сервера: " + myDateTime.getTime());
                            break;
                        case 2:
                            // остановить сервер выйти из приложения:
                            stopFlag = myDateTime.stop();
                            System.out.println("был отправлен запрос на остановку сервера.");
                            break;
                        default:
                            // неверный входной параметр:
                            System.err.println("Error: введено неправильное число!");
                            break;
                    }
                }
                catch (NumberFormatException e) {
                    System.err.println("Error: "+ e.getMessage());
                }
            }
            System.out.println("Клиент остановлен!");
        }
        catch (RemoteException | NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}