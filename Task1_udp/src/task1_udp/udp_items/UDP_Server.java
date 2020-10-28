package task1_udp.udp_items;

import task1_udp.message_items.Command;
import task1_udp.message_items.Message;
import task1_udp.message_items.MessageCollection;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.List;

public class UDP_Server implements AutoCloseable {

    public static final int BASE_SERVER_PORT = 7000;


    private int m_shutdown_cycles;  // если нет подписчиков, через какое число циклов остановимся
    private int m_socketTimeout_ms;
    private DatagramSocket m_socket;
    private MessageCollection m_messages;
    private byte[] m_buffer;


    public UDP_Server(int shutdown_cycles, int socketTimeout_ms) throws SocketException {
        m_shutdown_cycles = Math.max(100, shutdown_cycles);
        m_socketTimeout_ms = Math.max(100, socketTimeout_ms);
        m_socket = new DatagramSocket(BASE_SERVER_PORT);
        m_socket.setSoTimeout(m_socketTimeout_ms);
        m_messages = new MessageCollection();
        m_buffer = new byte[Message.BUFFER_SIZE];
    }


    private Message GetMessage() throws IOException, NullPointerException, IllegalArgumentException {
        DatagramPacket packet = new DatagramPacket(m_buffer, m_buffer.length);
        m_socket.receive(packet);

        Message message = Message.Deserialize(packet.getData());
        message.SetPort(packet.getPort());
        return message;
    }


    private void SendMessage(Message message, InetAddress address, int port) throws IOException, NullPointerException {
        byte[] bytes = Message.Serialize(message);
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, address, port);
        m_socket.send(sendPacket);
    }


    private void SendStatistics(Command command, InetAddress address, int port) throws IOException {
        List<Message> sendMessages = null;
        switch (command) {
            case CLIENT_MASSAGE:
                sendMessages = m_messages.GetTopClientMessage(10, address);
                break;
            case TOP_MESSAGE:
                sendMessages = m_messages.GetTopMessage(10);
                break;
        }

        // отправляем число сообщений отобранных из базы:
        this.SendMessage(new Message(command, Integer.toString(sendMessages.size()), new Date(), address, port), address, port);

        // передача сообщений:
        for (Message m : sendMessages) {
            this.SendMessage(m, address, port);
        }
    }


    public void Run() throws IOException {
        System.out.println("Сервер запущен...");
        int count = 0;
        while (count < m_shutdown_cycles) {
            try {
                Message clientMessage = this.GetMessage();
                switch (clientMessage.GetCommand()) {
                    case ADD:
                        m_messages.AddNewMessage(clientMessage);
                        clientMessage.SetMessage("OK");
                        this.SendMessage(clientMessage, clientMessage.GetAddress(), clientMessage.GetPort());
                        break;
                    case PING:
                        clientMessage.SetMessage("READY");
                        this.SendMessage(clientMessage, clientMessage.GetAddress(), clientMessage.GetPort());
                        break;
                    case TOP_MESSAGE:
                    case CLIENT_MASSAGE:
                        this.SendStatistics(clientMessage.GetCommand(), clientMessage.GetAddress(), clientMessage.GetPort());
                        break;
                }
                System.out.println("[CLIENT MESSAGE]: " + clientMessage.GetDate() + " - " + clientMessage.GetAddress().toString() + " - " + clientMessage.GetCommand().toString());
            }
            catch (SocketTimeoutException e) {
                System.out.println("Истекло время ожидание ответа: " + m_socketTimeout_ms + " мс, цикл: " + count);
            }
            catch (NullPointerException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
            count++;
        }
        System.out.println("Сервер остановлен!");
    }


    @Override
    public void close() {
        if (m_socket != null && !m_socket.isClosed()) {
            m_socket.close();
        }
    }


    public static void main(String[] args) {
        try (UDP_Server server = new UDP_Server(101,5000)) {
            server.Run();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}