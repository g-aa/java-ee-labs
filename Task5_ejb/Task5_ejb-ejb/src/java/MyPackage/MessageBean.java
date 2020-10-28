/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyPackage;

import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 *
 * @author Andrey
 */

@Stateful
@Local(IMessageBean.class)
public class MessageBean implements IMessageBean {
    
    private static final int MAX_MSG_COUNT = 3;
    
    @EJB
    private UsersData m_ud;
    
    private String m_login;
    private boolean m_userLoginStatus;
    int m_msgCount;

    
    public MessageBean() {
        
        this.m_login = "";
        this.m_userLoginStatus = false;
        this.m_msgCount = 0;
        
    }
    
    
    @Override
    public boolean login(String login, String password) {
        
        this.logout();
        
        if (this.m_ud.containsUser(login, password)) {
            this.m_userLoginStatus = true;
            this.m_login = login;
        }
        
        return this.m_userLoginStatus;
        
    }

    
    @Override
    public String[] getUsers() throws MessageException {
        
        if (this.m_userLoginStatus != true) {
            throw new MessageException("Для получения списка зарегистрированных пользователей необходимо пройти аутентификацию!");
        }
        
        ArrayList<String> logins = this.m_ud.getActiveUsers();
        return logins.toArray(new String[logins.size()]);
        
    }

    
    @Override
    public String getMessage (int index) throws MessageException {
        
        if (this.m_userLoginStatus) {
            this.m_msgCount++;
            String outMsg = null;
            switch (index) {
                case 1:
                    outMsg = "Вы получили ПЕРВОЕ сообщение, из трех вариантов возможных!";
                    break;
                case 2: 
                    outMsg = "Вы получили ВТОРОЕ сообщение, из трех вариантов возможных!";
                    break;
                case 3:
                    outMsg = "Вы получили ТРЕТЬЕ сообщение, из трех вариантов возможных!";
                    break;
                default:
                    throw new MessageException("Был введен неверный индекс сообщения, поддерживаемые варианты индексов: 1, 2, 3! ");
            }
            
            if (this.m_msgCount < MAX_MSG_COUNT) {
                return outMsg + "Осталось " + (MAX_MSG_COUNT - this.m_msgCount) + " запросов!";
            }
            else {
                this.logout();
                return outMsg + "\nВы исчерпали лимит сообщений! Вы переведены в статус незарегистрированного пользователя!";
            }
        }
        else {
            return "Вы являетесь незарегистрированным пользователем системы!";
        }
        
    }

    
    @Override
    public boolean logout () {
        
        this.m_ud.logout(m_login);
        this.m_login = "";
        this.m_userLoginStatus = false;
        this.m_msgCount = 0;
        return this.m_userLoginStatus;
        
    }
    
    
    @PostConstruct
    void init() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PostConstruct --> " + date + " : UserServiceBean created, hash: " + this.hashCode());
    }

    
    @PreDestroy
    void preDestroy() {
        this.logout(); // передвыходом снять флаг авторизации в базе данных
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PreDestroy --> " + date + " : hash: " + this.hashCode());
    }

    
    @PostActivate
    void postActivate() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PostActivate --> " + date + " : hash: " + this.hashCode());
    }

    
    @PrePassivate
    void prePassivate() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("--> @PrePassivate --> " + date + " : hash: " + this.hashCode());
    }

}
