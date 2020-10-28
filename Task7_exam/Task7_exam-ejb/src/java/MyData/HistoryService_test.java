/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyData;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Andrey
 */
@Singleton
@Startup
@LocalBean
public class HistoryService_test {
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public boolean login(String login, String password) {
    
        return login.equals("ADM") && password.equals("1234567890!");
        
    }
    
    public String[] getHistory(String last_name) {
    
        if ("Petrov".equals(last_name)) {
            
            return new String[] { "Строка 1", "Строка 2", "Строка 3", "Строка 4" };
            
        }
        
        return new String[0];
    }
    
    public String[] getHistory(int code) {
        
        if (code == 4) {
            
            return new String[] { "Строка 5", "Строка 6", "Строка 7", "Строка 8" };
        
        }
        
        return new String[0];
    }
    
}
