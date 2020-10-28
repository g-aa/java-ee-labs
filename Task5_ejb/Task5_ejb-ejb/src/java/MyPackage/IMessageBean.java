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
public interface IMessageBean {
    
    boolean login(String login, String password);
    
    String[] getUsers() throws MessageException;

    String getMessage (int index) throws MessageException;

    boolean logout();

}
