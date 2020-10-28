/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task7_exam_server;

import java.nio.ByteBuffer;

/**
 *
 * @author Andrey
 */
public class TCP_Message {
 
    public static final int BUFFER_SIZE = 256;
    
    // можно добавить перечень используемых команд:
    
    private int m_code; // 1 - login; 2 - logout; 3 - getHistory (last_name); 4 - getHistory ( id ); -1 - ошибка детализация в сообщении
    
    private final String m_login;
    
    private final String m_password;
    
    private String m_message;
    
    
    
    public TCP_Message(String login, String password) {
    
        this.m_login = login;
        this.m_password = password;
        this.m_message = "";
        this.m_code = 0;
    }
    
    
    
    public String getLogin() {
        return this.m_login;
    }
    
    public String getPassvord() {
        return this.m_password;
    }
    
    public int getCode() {
        return this.m_code;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    
    
    public void setCode(int code) {
        this.m_code = code;
    }
    
    public void setMessage(String message) {
        this.m_message = message;
    }
    
    
    
    public static TCP_Message deserialize(byte[] bArray) throws NullPointerException {
        if (bArray != null) {
            
            ByteBuffer buffer = ByteBuffer.wrap(bArray);

            int code = buffer.getInt();

            byte[] bLogin = new byte[buffer.getInt()];
            buffer.get(bLogin);
            String login = new String(bLogin);

            byte[] bPassword = new byte[buffer.getInt()];
            buffer.get(bPassword);
            String password = new String(bPassword);

            byte[] bMessage = new byte[buffer.getInt()];
            buffer.get(bMessage);
            String message = new String(bMessage);

            TCP_Message msg = new TCP_Message(login, password);
            msg.setCode(code);
            msg.setMessage(message);

            return msg;
        }
        else {
            throw new NullPointerException("bArray == null");
        } 
    }
    
    public static byte[] serialize(TCP_Message msg) throws NullPointerException {
        if (msg != null) {
            int size = 4*Integer.SIZE + msg.m_login.length() + msg.m_password.length() + msg.m_message.length();
            
            ByteBuffer buffer = ByteBuffer.allocate(size);
            
            buffer.putLong(msg.m_code);
            
            buffer.putInt(msg.m_login.length());
            buffer.put(msg.m_login.getBytes());
            
            buffer.putInt(msg.m_password.length());
            buffer.put(msg.m_password.getBytes());
            
            buffer.putInt(msg.m_message.length());
            buffer.put(msg.m_message.getBytes());
            
            return buffer.array();
        }
        else{
            throw new NullPointerException("registration == null");
        }
    }
    
}
