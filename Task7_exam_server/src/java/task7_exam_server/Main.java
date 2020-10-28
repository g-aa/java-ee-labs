/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task7_exam_server;

/**
 *
 * @author Andrey
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println("Инициализация сервера.");
        try (TCP_Server server = new TCP_Server(TCP_Server.BASE_SERVER_PORT)) {
            server.Run();
        }
        catch (Exception e) {
            System.err.println("Error: from main function - '" + e.getMessage() + "'");
        }
    }
    
}
