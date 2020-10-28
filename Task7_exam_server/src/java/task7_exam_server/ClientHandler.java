/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task7_exam_server;

import MyData.EmployeeException;
import MyData.IHistoryRemote;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Andrey
 */
public class ClientHandler implements Runnable {

    private static final String EJB_OBJ_NAME = "java:global/Task7_exam/Task7_exam-ejb/History!MyData.IHistoryRemote";
        
    private final Socket m_clientSocket;
    private final InputStream m_inStream;
    private final OutputStream m_outStream;
    private final IHistoryRemote m_history;

    public ClientHandler(Socket socket) throws NullPointerException, IOException, NamingException {
        
        if(socket == null) {
            throw new NullPointerException("client socket = null!");
        }
        
        InitialContext ic = new InitialContext();
        this.m_history = (IHistoryRemote)ic.lookup(ClientHandler.EJB_OBJ_NAME);
        
        this.m_clientSocket = socket;
        this.m_inStream = socket.getInputStream();
        this.m_outStream = socket.getOutputStream();
        
    }

    private TCP_Message getMessage() throws IOException {
        
        byte[] bs = new byte[TCP_Message.BUFFER_SIZE];
        this.m_inStream.read(bs);
        return TCP_Message.deserialize(bs);
        
    }

    private void sendResponse(TCP_Message message) throws IOException {
        
        this.m_outStream.write(TCP_Message.serialize(message));
        this.m_outStream.flush();
        
    }
    
    @Override
    public void run() {
        System.out.println("Запуск потока: '" + Thread.currentThread().getName() + "' на обработку адреса: " + m_clientSocket.getInetAddress().toString());
        try {
            TCP_Message msg = this.getMessage();
            System.out.println("[CLIENT REQUEST]:\t" + msg.getLogin() + " - operation code: " + msg.getCode());
            
            try {
                switch (msg.getCode()) {
                    case 1:
                        if(!this.m_history.login(msg.getLogin(), msg.getPassvord())) {
                            msg.setCode(-1);
                            msg.setMessage("операция регистрации завершилась неудачей!");
                        }
                        else {
                            msg.setMessage("пользователь зарегистрирован!");
                        }
                        break;
                    case 2:
                        if(!this.m_history.logout()) {
                            msg.setCode(-1);
                            msg.setMessage("операция выхода из системы завершилась неудачно, пользователь не зарегистрирован!");
                        }
                        else{
                            msg.setMessage("пользователь вышел из системы!");
                        }
                        break;
                    case 3:
                        String[] sh1 = this.m_history.getHistory(msg.getMessage());
                        msg.setMessage(String.join(";", sh1));
                        break;
                    case 4:
                        int code = Integer.parseInt(msg.getMessage());
                        String[] sh2 = this.m_history.getHistory(code);
                        msg.setMessage(String.join(";", sh2));
                        break;
                    default:
                        msg.setCode(-1);
                        msg.setMessage("была отправлена неверная команда на сервер!");
                }
            }
            catch(EmployeeException e) {
                msg.setCode(-1);
                msg.setMessage(e.getMessage());
            }
            
            this.sendResponse(msg); // отправить сообщение обратно
        }
        catch (IOException e){
            System.err.println("Error from ClientHandler : '" + e.getMessage() + "'");
        }
        finally {
            if (m_clientSocket != null && !m_clientSocket.isClosed()) {
                try {
                    m_clientSocket.close();
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Завершение работы потока: '" + Thread.currentThread().getName() + "'");
    }
}