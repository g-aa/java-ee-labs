package task1_udp.udp_items;

import task1_udp.message_items.Command;
import task1_udp.message_items.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UDP_Client {

    private int m_socketTimeout_ms;
    private int m_serverPort;
    private InetAddress m_serverAddress;
    private byte[] m_buffer;

    public UDP_Client(InetAddress serverAddress, int serverPort, int socketTimeout_ms) throws IllegalArgumentException {
        if (serverPort < 0 || serverAddress == null) {
            throw new IllegalArgumentException("illegal input arguments!");
        }
        m_socketTimeout_ms = Math.max(100, socketTimeout_ms);
        m_serverPort = serverPort;
        m_serverAddress = serverAddress;
        m_buffer = new byte[Message.BUFFER_SIZE];
    }


    // for ADD, PING, QUIT:
    private Message SendMessage(Message message, InetAddress address, int port) throws IOException, NullPointerException, IllegalArgumentException {
        try (DatagramSocket socket = new DatagramSocket();) {
            socket.setSoTimeout(m_socketTimeout_ms);

            // отправка сообщения:
            byte[] bytes = Message.Serialize(message);
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(sendPacket);

            // прием результата:
            DatagramPacket answerPacket = new DatagramPacket(m_buffer, m_buffer.length);
            socket.receive(answerPacket);
            return Message.Deserialize(answerPacket.getData());
        }
    }

    // for TOP_MESSAGE, CLIENT_MASSAGE:
    private List<Message> GetStatistics(Message message, InetAddress address, int port) throws IOException, NullPointerException, IllegalArgumentException {
        try (DatagramSocket socket = new DatagramSocket();) {
            socket.setSoTimeout(m_socketTimeout_ms);

            // отправка сообщения:
            byte[] bytes = Message.Serialize(message);
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(sendPacket);

            // прием результата:
            // получть колличество сообщений:
            DatagramPacket answerPacket = new DatagramPacket(m_buffer, m_buffer.length);
            socket.receive(answerPacket);
            Message sm = Message.Deserialize(answerPacket.getData());
            int messageCount = Integer.parseInt(sm.GetMessage());

            // чтение сообщений из перечня:
            List<Message> result = new ArrayList<Message>(messageCount);
            for (int i = 0; i < messageCount; i++) {
                socket.receive(answerPacket);
                result.add(Message.Deserialize(answerPacket.getData()));
            }
            return result;
        }
    }


    public void Run() {
        System.out.println("Клиент запущен...");
        boolean flag = true;
        while (flag) {
            System.out.println("\nВыберете команду:");
            
            for (Command item : task1_udp.message_items.Command.values()) {
                System.out.println( item.GetValue() + " - " + item.toString());
            }

            try {
                Command command = Command.values()[new Scanner(System.in).nextInt()];
                Message serverResult = null;
                List<Message> statistics = null;
                switch (command) {
                    case ADD:
                        System.out.println("Введите текстовое сообщение (не более " + Message.MAX_MESSAGE_LENGTH + " символов):");
                        String inputString = new Scanner(System.in).nextLine();
                        serverResult = this.SendMessage(new Message(command, inputString), m_serverAddress, m_serverPort);
                        break;
                    case PING:
                        serverResult = this.SendMessage(new Message(command, ""), m_serverAddress, m_serverPort);
                        break;
                    case TOP_MESSAGE:
                    case CLIENT_MASSAGE:
                        statistics = this.GetStatistics(new Message(command, ""), m_serverAddress, m_serverPort);
                        break;
                    case QUIT:
                        // serverResult = this.SendMessage(new Message(command, ""), m_serverAddress, m_serverPort);
                        flag = false;
                        break;
                }

                if (serverResult != null) {
                    System.out.println("[SERVER RESPONSE]: " + serverResult.GetDate() + " - '" + serverResult.GetMessage() + "'");
                }
                else if(statistics != null) {
                    System.out.println("[SERVER RESPONSE]:");
                    if (statistics.size() != 0){
                        for (Message m : statistics) {
                            System.out.println("<" + m.GetAddress().toString() + " : "+ m.GetPort() + ">-<" + m.GetMessage() + ">-<" + m.GetDate() + ">");
                        }
                    }
                    else {
                        System.out.println("<Empty list>");
                    }
                }
            }
            catch (SocketTimeoutException e) {
                System.out.println("Истекло время ожидание ответа: " + m_socketTimeout_ms + " мс");
            }
            catch (NullPointerException | IllegalArgumentException |IOException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.println("Клиент остановлен!");
    }


    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            UDP_Client client = new UDP_Client(serverAddress, UDP_Server.BASE_SERVER_PORT, 5000);
            client.Run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}