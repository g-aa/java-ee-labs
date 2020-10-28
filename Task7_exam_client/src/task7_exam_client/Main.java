/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task7_exam_client;

import java.net.InetAddress;
import task7_exam_server.TCP_Server;

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
        
        try (TCP_Client client = new TCP_Client(InetAddress.getLocalHost(), TCP_Server.BASE_SERVER_PORT)) {
            client.Run();
        }
        catch (Exception e) {
            System.err.println("Error: '" + e.getMessage() + "'");
        }
    }
    
}
