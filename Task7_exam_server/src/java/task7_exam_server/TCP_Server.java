/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task7_exam_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.naming.NamingException;

/**
 *
 * @author Andrey
 */
public class TCP_Server implements AutoCloseable {
    
    public static final int BASE_SERVER_PORT = 9000;

    private final ServerSocket m_serverSocket;
        
    public TCP_Server(int port) throws IOException {
        
        this.m_serverSocket = new ServerSocket(port);
        
    }
    
    public void Run() {
        
        System.out.println("Сервер запущен...");
        try {
            while (true) {
                Socket clientSocket = m_serverSocket.accept();
                try {
                    new Thread(new ClientHandler(clientSocket)).start();
                }
                catch (NullPointerException | IOException | NamingException e) {
                    System.err.println("Error: '" + e.getMessage() + "'");
                    clientSocket.close();
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
}
