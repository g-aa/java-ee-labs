/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyPackage;

/**
 *
 * @author Andrey
 */
public class User {
    
    private String m_login;
    
    private String m_password;
    
    private boolean m_loginStatus;
    
    
    public User(String login, String password) { 
    
        this.m_login = login;
        this.m_password = password;
        this.m_loginStatus = false;
    
    }
    
    public void setLoginStatus(boolean status) {
        
        this.m_loginStatus = status;
    
    }
    
    public boolean getLoginStatus() { 
        
        return this.m_loginStatus;
    
    }
    
    String getLogin() {
    
        return this.m_login;
        
    }
    
    String getPassword() {
    
        return this.m_password;
        
    }
}
