/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task6_jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Andrey
 */
public class MyIOFile {
    
    public static ArrayList<UserRecord> readFile(String path) {
        try {
            Scanner sc = new Scanner(new File(path));
            System.out.println("\n- reading data from file: '" + path + "'");
            
            ArrayList<UserRecord> users = new ArrayList<>();
            while (sc.hasNextLine()) {
                String txtLine = sc.nextLine();
                if (!"".equals(txtLine.trim())) {
                    UserRecord ur = UserRecord.parse(" ", txtLine);
                    users.add(ur);
                }
            }
            
            System.out.println("\n- reading data from file is finished!");
            return users;
        } catch (FileNotFoundException | IllegalArgumentException | ParseException e) {
            System.out.println("\n- reading data from file:eErr - " + e.getMessage());
            return new ArrayList<UserRecord>();
        }
    }
    
}
