/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task6_jdbc;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Andrey
 */
public class UserRecord {
    
    private String m_login;
    private String m_userRole;
    private Timestamp m_registrationTime;
    
    
    public UserRecord(String login, String role, Timestamp registrationTime) throws NullPointerException, IllegalArgumentException {
    
        if (login == null || role == null || registrationTime == null) {
            throw new NullPointerException("входные параметры = null");
        }
    
        if("".equals(login.trim()) || "".equals(role.trim())) {
            throw new IllegalArgumentException("неверные входные параметры");
        }
        
        this.m_login = login;
        this.m_userRole = role;
        this.m_registrationTime = registrationTime;
        
    }
    
    public UserRecord(String login, String role) throws NullPointerException, IllegalArgumentException {
        
        this(login, role,new Timestamp((new Date()).getTime()));
    
    }
    
    
    public String getLogin() {
        
        return this.m_login;
        
    }
    
    public String getRole() {
        
        return this.m_userRole;
        
    }
    
    public Timestamp getRegistrationTime() {
        
        return this.m_registrationTime;
        
    }
    
    
    public static UserRecord parse(String delimiter, String data) throws ParseException, IllegalArgumentException {
    
        String[] items = data.split(delimiter, 3);
    
        if ("".equals(items[0].trim())){
            throw new IllegalArgumentException("неверный пвраметр login");
        }
        
        if ("".equals(items[1].trim())){
            throw new IllegalArgumentException("неверный параметр role");
        }
        
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Timestamp time = null;
        Date date = dateFormat.parse(items[2]);
        time = new Timestamp(date.getTime());
        
        return new UserRecord(items[0], items[1], time);
    }
    
}