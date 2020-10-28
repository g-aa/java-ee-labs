/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task7_exam_client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import task7_exam_server.TCP_Message;

/**
 *
 * @author Andrey
 */
public class TCP_Client implements AutoCloseable {
    
    private final int m_serverPort;
    private final InetAddress m_serverAddress;
    private final Socket m_clientSocket;
    
    public TCP_Client(InetAddress serverAddress, int serverPort) throws NullPointerException, IOException {
        
        if (serverAddress == null) {
            throw new NullPointerException("serverAddress == null!");
        }
        
        this.m_serverPort = serverPort;
        this.m_serverAddress = serverAddress;
        this.m_clientSocket = new Socket(m_serverAddress, m_serverPort);
        
    }

    private TCP_Message sendMessage(TCP_Message msg) throws NullPointerException, IOException {
                    
        // выполнить обмен даынными:
        OutputStream outStream = this.m_clientSocket.getOutputStream();
        outStream.write(TCP_Message.serialize(msg));
        outStream.flush();

        InputStream inStream = this.m_clientSocket.getInputStream();
        byte[] bs = new byte[TCP_Message.BUFFER_SIZE];
        inStream.read(bs);
        return TCP_Message.deserialize(bs);

    }

    public void Run() {
        System.out.println("Клиент запущен...\n");
        boolean flag = true;
        while (flag) {
            System.out.println("Выберете действие:");
            System.out.println("1 - login\n2 - logout\n3 - getHistory ( last_name )\n4 - getHistory ( user id )\n0 - quit");
            try {
                Scanner scanner = new Scanner(System.in);
                TCP_Message msg = null;
                int index = Integer.parseInt(scanner.nextLine());
                switch(index) {
                    case 0:
                        flag = false;
                        break;
                    case 1:
                        System.out.println("Введите имя пользователя (login):");
                        String login = scanner.nextLine();
                        System.out.println("Введите пароль (password):");
                        String password = scanner.nextLine();
                        msg = new TCP_Message(login, password);
                        msg.setCode(index);
                        break;
                    case 2:
                        msg = new TCP_Message("", "");
                        msg.setCode(index);
                        break;
                    case 3:
                        System.out.println("Введите имя пользователя (last_name):");
                        String last_name = scanner.nextLine();
                        msg = new TCP_Message("", "");
                        msg.setMessage(last_name);
                        msg.setCode(index);
                        break;
                    case 4:
                        System.out.println("Введите имя пользователя (user id):");
                        int user_id = Integer.parseInt(scanner.nextLine());
                        msg = new TCP_Message("", "");
                        msg.setMessage(String.valueOf(user_id));
                        msg.setCode(index);
                        break;
                    default:
                        System.out.println("Был введен неверный параметр!");
                }
                
                if (msg != null) {
                    System.out.println("[SEND MESSEGE]: " + (new Date()).toString() + ", operation: '" + msg.getCode() + "'");                    
                    TCP_Message result = this.sendMessage(msg);
                    System.out.println("[SERVER RESPONSE]: " + (new Date()).toString() + ", operation: '" + msg.getCode() + "', result:");
                    System.out.println(result.getMessage());
                }
            }
            catch (NullPointerException | IllegalArgumentException | IOException e) {
                System.err.println("Error: '" + e.getMessage() + "'");
            }
            System.out.println("\n");
        }
        System.out.println("Клиент остановлен!");
    }
    
    
    @Override
    public void close() throws IOException {
        
        if (m_clientSocket != null && !m_clientSocket.isClosed()) { 
            
            m_clientSocket.close();
        
        }
        
    }
}