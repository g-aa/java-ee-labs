/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyData;

import MyEntities.Employeehistory;
import MyEntities.Employees;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Andrey
 */
@Singleton
@Startup
@LocalBean
public class HistoryService_db {
    
    @PersistenceContext
    private EntityManager m_em;
    
    public boolean login(String login, String password) {
        
        try {
            Employees emp = this.m_em.find(Employees.class, login);
            
            if (emp != null) {
                return emp.getPassword().equals(password);
            }
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        return false;
    }
    
    public String[] getHistory(String last_name) {
    
        try {
            Employees emp = this.m_em.find(Employees.class, last_name);
            Collection<Employeehistory> ehs = emp.getEmployeehistoryCollection();
        
            ArrayList<String> result = new ArrayList<String>(ehs.size());
            for(Employeehistory eh : ehs) {
                String s = "ID = " + eh.getId() + 
                    ", POSITION = " + eh.getPosition() +
                    ", MANAGER = " + eh.getManager() +
                    ", HIRE = " + eh.getHire().toString() + 
                    ", DISMISS = " + eh.getDismiss().toString() + 
                    ", CODE = " + eh.getCode();
                result.add(s);
            }
        
            return result.toArray(new String[ehs.size()]);
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return new String[0];
    }
    
    public String[] getHistory(int code) {
        try {
            Employees emp = this.m_em.find(Employees.class, code);
            Collection<Employeehistory> ehs = emp.getEmployeehistoryCollection();
        
            ArrayList<String> result = new ArrayList<String>(ehs.size());
            for(Employeehistory eh : ehs) {
                String s = "ID = " + eh.getId() + 
                    ", POSITION = " + eh.getPosition() +
                    ", MANAGER = " + eh.getManager() +
                    ", HIRE = " + eh.getHire().toString() + 
                    ", DISMISS = " + eh.getDismiss().toString() + 
                    ", CODE = " + eh.getCode();
                result.add(s);
            }
        
            return result.toArray(new String[ehs.size()]);
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return new String[0];
    }

}
