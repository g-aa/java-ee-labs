package task2_tcp.tcp_items;

import task2_tcp.registration_items.Operation;
import task2_tcp.registration_items.Registration;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TCP_Client {

    private final int m_serverPort;
    private final InetAddress m_serverAddress;

    public TCP_Client(InetAddress serverAddress, int serverPort) throws NullPointerException {
        if (serverAddress == null) {
            throw new NullPointerException("неверные входные параметры");
        }
        m_serverPort = serverPort;
        m_serverAddress = serverAddress;
    }


    private Registration sendRegistration(Registration user) throws NullPointerException, IOException {
        try (Socket socket = new Socket(m_serverAddress, m_serverPort)) {
            // socket.setSoTimeout(m_socketTimeout_ms);

            // выполнить обмен даынными:
            OutputStream outStream = socket.getOutputStream();
            outStream.write(Registration.serialize(user));
            outStream.flush();

            InputStream inStream = socket.getInputStream();
            
            byte[] bArray = new byte[Registration.BUFFER_SIZE];
            inStream.read(bArray);
            return Registration.deserialize(bArray);
        }
    }


    public List<Registration> getRegistrationStatistics(Registration user) throws NullPointerException, IOException {
        try (Socket socket = new Socket(m_serverAddress, m_serverPort)) {
            // socket.setSoTimeout(m_socketTimeout_ms);

            // отправить запрос на получение статистики:
            OutputStream outStream = socket.getOutputStream();
            outStream.write(Registration.serialize(user));
            outStream.flush();

            // получение статистики:
            // получение технологического сообщения:
            InputStream inStream = socket.getInputStream();
            
            byte[] bArray = new byte[Registration.BUFFER_SIZE];
            inStream.read(bArray);
            Registration tech = Registration.deserialize(bArray);
            if(tech.getOperation() == Operation.GET_STATISTICS && 0 < tech.getValue()) {
                // получение статистики:
                List<Registration> statistics = new ArrayList<>(tech.getValue());
                byte[] bStatistics = new byte[tech.getValue() * Registration.BUFFER_SIZE];

                if(inStream.read(bStatistics) < 0 ) {
                    throw new IOException("буфер пуст");
                }
                ByteBuffer buffer = ByteBuffer.wrap(bStatistics);
                byte[] bytes = new byte[Registration.BUFFER_SIZE];
                for (int i = 0; i < tech.getValue(); i++) {
                    buffer.get(bytes);
                    statistics.add(Registration.deserialize(bytes));
                }
                return statistics;
            }
            return new ArrayList<>();
        }
    }


    public void Run() {
        System.out.println("Клиент запущен...\n");
        while (true) {
            System.out.println("Выберете действие:");
            for (Operation o : Operation.values()) {
                System.out.println( o.GetIndex() + " - " + o.toString());
            }
            try {
                int index = Integer.parseInt(new Scanner(System.in).nextLine());
                Operation operation = Operation.values()[index];
                if (operation == Operation.QUIT) {
                    break;
                }

                System.out.println("Введите имя пользователя (не более " + Registration.USERNAME_LENGTH + " символов):");
                String userName = new Scanner(System.in).nextLine();
                Registration user = new Registration(userName, operation);

                if(operation == Operation.REGISTER) {
                    System.out.println("[REGISTRATION]: " + user.getDate() + ", name = '" + user.getUserName() + "'");
                    Registration answer = this.sendRegistration(user);
                    System.out.println("[SERVER RESPONSE]: " + answer.getDate() + ", name = '" + answer.getUserName() + "', code: " + answer.getValue());
                }
                else if (operation == Operation.GET_STATISTICS) {
                    System.out.println( "[REGISTRATION STATISTICS]: " + user.getDate() + ", name = '" + user.getUserName() + "'");
                    List<Registration> statistics = this.getRegistrationStatistics(user);
                    System.out.println("[SERVER RESPONSE]: ");
                    if(statistics.size() != 0) {
                        for (Registration r : statistics) {
                            System.out.println(r.toString());
                        }
                    }
                    else {
                        System.out.println("Список регистраций пуст!");
                    }
                }

            }
            catch (NullPointerException | IllegalArgumentException | IOException e) {
                System.err.println("Error: '" + e.getMessage() + "'");
            }
            System.out.println("\n");
        }
        System.out.println("Клиент остановлен!");
    }


    public static void main(String[] args) {
        try {
            TCP_Client client = new TCP_Client(InetAddress.getLocalHost(), TCP_Server.BASE_SERVER_PORT);
            client.Run();
        }
        catch (Exception e) {
            System.err.println("Error: '" + e.getMessage() + "'");
        }
    }
}