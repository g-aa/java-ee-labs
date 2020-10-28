/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyData;

import javax.ejb.Remote;

/**
 *
 * @author Andrey
 */
@Remote
public interface IHistoryRemote {
       
    String[] getHistory (String last_name) throws EmployeeException;
    
    String[] getHistory (int code) throws EmployeeException;
    
    boolean login (String user, String password);
    
    boolean logout ();
    
}
