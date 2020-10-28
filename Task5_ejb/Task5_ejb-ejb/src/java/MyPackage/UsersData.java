/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;


/**
 *
 * @author Andrey
 */


@Singleton
@Startup
@LocalBean
public class UsersData {
    
    private final HashMap<Integer, User> m_ud;

    
    public UsersData() {
    
        this.m_ud = this.loadDefaultUsersData();
        
    }
    
    
    private HashMap<Integer, User> loadDefaultUsersData() {
        
        HashMap<Integer, User> result  = new HashMap<Integer, User>();
        
        result.put("ADM".hashCode() , new User("ADM", "1234567890!"));
        result.put("Andrey".hashCode(), new User("Andrey", "Aa11111111"));
        result.put("STAR".hashCode() , new User("STAR", "asAS1!"));
        result.put("void".hashCode(), new User("void", "NULL?"));
        
        return result;
    }
    
    
    public boolean containsUser(String login, String password) throws NullPointerException {

        if (login == null || password == null) {
            throw new NullPointerException("входные параметры равны null");
        }
        
        if (this.m_ud.containsKey(login.hashCode())) {
            User user = this.m_ud.get(login.hashCode());
            
            if (user.getPassword().equals(password)) {
                user.setLoginStatus(true);
                return true;
            }
        }
        
        return false;
    }

    
    public boolean logout(String login) throws NullPointerException {
    
        if (login == null) {
            throw new NullPointerException("входной параметр равен null");
        }
        
        if (this.m_ud.containsKey(login.hashCode())) {
            User user = this.m_ud.get(login.hashCode());
            user.setLoginStatus(false);
            return true;
        }
        
        return false;
    }

    
    public ArrayList<String> getActiveUsers() {
    
        ArrayList<String> result = new ArrayList<String>();
        for(Map.Entry<Integer, User> item : this.m_ud.entrySet()) {
            User temp = item.getValue();
            if (temp.getLoginStatus()) {
                result.add(temp.getLogin());
            }
        }
        return result;
        
    }
    
    @PostConstruct
    public void init() {
                
        System.out.println("---> UsersData: "  + (new Date()).toString() + " - инициализация объекта");
    
    }
    
    
    @PreDestroy
    void preDestroy() {
        
        System.out.println("---> UsersData: "  + (new Date()).toString() + " - уничтожение объекта");
    
    }
}
