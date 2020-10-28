package task2_tcp.tcp_items;

import task2_tcp.registration_items.Operation;
import task2_tcp.registration_items.Registration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ClientHandler implements Runnable {

    private final Socket m_clientSocket;
    private final InputStream m_inStream;
    private final OutputStream m_outStream;

    private final ConcurrentMap<String, List<Registration>> m_users; // ссылка на общуюю БД юзеров:

    public ClientHandler(Socket socket, ConcurrentMap<String, List<Registration>> users) throws NullPointerException, IOException {
        if(socket == null || users == null) {
            throw new NullPointerException("неверные входные параметры");
        }
        this.m_clientSocket = socket;
        this.m_inStream = socket.getInputStream();
        this.m_outStream = socket.getOutputStream();
        this.m_users = users;
    }


    private Registration getRegistration() throws IOException {
        byte[] bArray = new byte[Registration.BUFFER_SIZE];
        this.m_inStream.read(bArray);
        return Registration.deserialize(bArray);
    }

    private int addToDatabase(Registration user) {
        if(m_users.containsKey(user.getUserName())) {
            List<Registration> list = m_users.get(user.getUserName());
            list.add(user);
        }
        else {
            List<Registration> list = new ArrayList<>();
            list.add(user);
            m_users.put(user.getUserName(), list);
        }
        return 0;
    }

    private void sendRegistrationResponse(String userName, Operation operation, int value) throws IOException {
        m_outStream.write(Registration.serialize(new Registration(new Date(), userName, operation, value)));
        m_outStream.flush();
    }


    private List<Registration> getRegistrationStatistics(String userName) {
        if(m_users.containsKey(userName)) {
            return new ArrayList<>(m_users.get(userName));
        }
        else {
            return new ArrayList<Registration>();
        }
    }

    private void sendRegistrationStatistics(List<Registration> registrations) throws IOException {
        // отправка технологического сообщения (размер посылки):
        this.sendRegistrationResponse("t", Operation.GET_STATISTICS, registrations.size());

        // отправка основного сообщения:
        if (registrations.size() != 0) {
            ByteBuffer buffer = ByteBuffer.allocate(registrations.size()*Registration.BUFFER_SIZE);
            for (Registration r : registrations) {
                buffer.put(Registration.serialize(r));
            }
            m_outStream.write(buffer.array());
            m_outStream.flush();
        }
    }


    @Override
    public void run() {
        System.out.println("Запуск потока: '" + Thread.currentThread().getName() + "' на обработку адреса: " + m_clientSocket.getInetAddress().toString());
        try {
            Registration user = this.getRegistration();
            System.out.println("[CLIENT REQUEST]:\t" + user.getDate() + " - " + user.getUserName() + " - operation: " + user.getOperation().toString());
            switch (user.getOperation()) {
                case REGISTER:
                    int code = this.addToDatabase(user);
                    this.sendRegistrationResponse(user.getUserName(), user.getOperation(), code);
                    break;
                case GET_STATISTICS:
                    List<Registration> statistics = this.getRegistrationStatistics(user.getUserName());
                    this.sendRegistrationStatistics(statistics);
                    break;
            }
        }
        catch (IOException e){
            System.err.println("Error from ClientHandler : '" + e.getMessage() + "'");
        }
        finally {
            if (m_clientSocket != null && !m_clientSocket.isClosed()) {
                try {
                    m_clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Завершение работы потока: '" + Thread.currentThread().getName() + "'");
    }
}