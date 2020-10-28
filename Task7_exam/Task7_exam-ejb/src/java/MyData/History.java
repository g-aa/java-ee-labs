/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyData;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

/**
 *
 * @author Andrey
 */
@Stateful
// @Stateless
@Remote(IHistoryRemote.class)
public class History implements IHistoryRemote {
    
    private boolean m_loginStatus;
    
    @EJB
    private HistoryService_test m_hs;
    // private HistoryService_db m_hs;
    
    
    @Override
    public String[] getHistory(String last_name) throws EmployeeException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
        if (!this.m_loginStatus) {
            throw new EmployeeException("Для получение истории необходимо пройти процедуру аутентификации!");
        }
        
        if(last_name == null) {
            throw new NullPointerException("Параметр last_name = null!");
        }
        
        if("".equals(last_name.trim())) {
            return new String[0];
        }
        
        return this.m_hs.getHistory(last_name);
    }

    @Override
    public String[] getHistory(int code) throws EmployeeException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
        if (!this.m_loginStatus) {
            throw new EmployeeException("Для получение истории необходимо пройти процедуру аутентификации!");
        }
        
        return this.m_hs.getHistory(code);
    }

    @Override
    public boolean login(String user, String password) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
        if (user != null && password != null) {
            this.m_loginStatus = this.m_hs.login(user, password);
            return this.m_loginStatus;
        }
        return false;
    }

    @Override
    public boolean logout() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        if (this.m_loginStatus) {
            this.m_loginStatus = false;
            return true;
        }
        return false;
    }
        
    @PostConstruct
    void init() {
        this.m_loginStatus = false;
        System.out.println("--> @PostConstruct --> " + (new Date()).toString() + " : HistoryBean created!");
    }

    @PreDestroy
    void preDestroy() {
        System.out.println("--> @PreDestroy --> " + (new Date()).toString() + " : HistoryBean destroyed!");
    }
    
}
