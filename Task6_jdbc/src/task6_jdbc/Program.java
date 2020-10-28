/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task6_jdbc;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Andrey
 */
public class Program {
    
    private static final String FILEPATH = "D:\\MyRepository\\Java\\JavaEE\\Labs\\NetBeansProjects\\dataSourceFiles\\data.log";
    private static final String NEW_FILEPATH = "D:\\MyRepository\\Java\\JavaEE\\Labs\\NetBeansProjects\\dataSourceFiles\\newData.log";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try(MyDataBaseContext context = new MyDataBaseContext()) {
            
            
            // загрузка данных в БД:
            ArrayList<UserRecord> records = MyIOFile.readFile(FILEPATH);
            records.forEach((UserRecord record) -> {    
                try {
                    context.insertIntoUsers(record);
                    context.insertIntoRegistration(record);
                } 
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });
            
            
            // выгрузка содержимого из БД в файл:
            context.saveAllToFile(NEW_FILEPATH);
            
            
            // получить списокпользователей до определенной даты:
            String dateString = "16.09.2012 10:31:46";
            System.out.println("Перечень пользователей, зарегистрированных после: " + dateString);
            for(String s : context.selectUsersRegAfterDate(dateString)) {
                System.out.println(s);
            }
            
            
            // получить всех пользователей которые регистрировались под разными ролями:
            System.out.println("Перечень пользователей, которые регистрировались под разными ролями");
            for(String s : context.selectUsersRegDiffRoles()) {
                System.out.println(s);
            }
            
            
            // получить все даты в которые регистрировались два и более пользователей:
            System.out.println("Перечень дат, в которые регистрировались два и более пользователей");
            for(String s : context.selectDatesRegDiffUsers()) {
                System.out.println(s);
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
