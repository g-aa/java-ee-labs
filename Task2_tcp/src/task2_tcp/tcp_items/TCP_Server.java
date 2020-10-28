package task2_tcp.tcp_items;

import task2_tcp.registration_items.Registration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TCP_Server implements AutoCloseable {

    public static final int BASE_SERVER_PORT = 9000;

    private final ServerSocket m_serverSocket;
    private final ConcurrentMap<String, List<Registration>> m_users;

    public TCP_Server(int port) throws IOException {
        m_serverSocket = new ServerSocket(port);
        m_users = new ConcurrentHashMap<>();
    }


    public void Run() {
        System.out.println("Сервер запущен...");
        try {
            while (true) {
                Socket clientSocket = m_serverSocket.accept();
                try {
                    new Thread(new ClientHandler(clientSocket, m_users)).start();
                }
                catch (NullPointerException | IOException e) {
                    System.err.println("Error: '" + e.getMessage() + "'");
                }
            }
        }
        catch(IOException e) {
            System.err.println("Error from TCP_Server: '" + e.getMessage() + "'");
        }
        System.out.println("Сервер остановлен!");
    }


    @Override
    public void close() throws IOException {
        if (m_serverSocket != null && !m_serverSocket.isClosed()) {
            m_serverSocket.close();
        }
    }


    public static void main(String[] args) {
        System.out.println("Инициализация сервера.");
        try (TCP_Server server = new TCP_Server(BASE_SERVER_PORT)) {
            server.Run();
        }
        catch (Exception e) {
            System.err.println("Error: from main'" + e.getMessage() + "'");
        }
    }
}