/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task6_jdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author Andrey
 */
public class MyDataBaseContext implements AutoCloseable {
    
    private static final String DB_URL = "jdbc:derby://localhost:1527/task6_jdbc";
    private static final String DB_USER = "adm";
    private static final String DB_PAS = "ADM";
    
    private String m_userSeq = "seq_users_id";
    private String m_userTable = "users";
    
    private String m_regSeq = "seq_registration_code";
    private String m_regTable = "registration";
    
    private Connection m_con;
    
    
    public MyDataBaseContext() throws SQLException {
   
        m_con = DriverManager.getConnection(DB_URL, DB_USER, DB_PAS);
        
        String sqlCreateUsers = "create table users (\n"
            + "id integer,\n"
            + "login char(8) unique,\n"
            + "constraint pk_users_id primary key(id)\n"
            + ")";   
        boolean b1 = this.createTable(sqlCreateUsers, DB_USER, this.m_userTable, this.m_userSeq);
        
        String sqlCreateReg = "create table registration (\n"
            + "code integer primary key,\n"
            + "id integer,\n"
            + "role varchar(16),\n"
            + "date timestamp,\n"
            + "constraint fk_users_id foreign key (id) references users(id)\n"
            + ")";
        boolean b2 = this.createTable(sqlCreateReg, DB_USER, this.m_regTable, this.m_regSeq);
    }
    
    
    public boolean insertIntoUsers(UserRecord record) throws SQLException {

        String login = record.getLogin();
        int user_id = getUserId(login);
            
        if (user_id != -1) {
            return false;
        }

        String id = "next value for seq_users_id";
        try (Statement st = m_con.createStatement()) {
            String sqlUsersAddRow = "insert into users(id, login) "
                          + "values(" + id + ", '" + login + "')";
            st.executeUpdate(sqlUsersAddRow);
            return true;
        }
    }
    
    
    public boolean insertIntoRegistration(UserRecord record) throws SQLException {

        int user_id = this.getUserId(record.getLogin());
        
        if (user_id == -1) {
            return false;
        }
        
        String code = "next value for seq_registration_code";
        String sql = "insert into registration(code, id, role, date)\n"
            + "values(" + code + ", " + user_id + ", '" + record.getRole() + "', '" + record.getRegistrationTime().toString() + "')";
        
        try(Statement st = m_con.createStatement()) {
            st.executeUpdate(sql);
            return true;
        }        
    }
    
    
    private int getUserId(String login) throws SQLException {

        String sql = "select id, login from users\n"
                   + "where login like '" + login + "%'";

        Statement st = m_con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            String userLogin = rs.getString("login").trim();
            int user_id = rs.getInt("id");

            if (userLogin.equals(login)) {
                return user_id;
            }
        }
        return -1;
    }
    
    
    public ArrayList<String> selectUsersRegAfterDate(String dateString) throws SQLException, ParseException {

        ArrayList<String> result = new ArrayList<String>();
        
        if("".equals(dateString.trim())) {
            return result;
        }
    
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Timestamp tsDate = new Timestamp((dateFormat.parse(dateString)).getTime());

        String sql = "select login\n"
            + "from registration\n"
            + "join users\n"
            + "using (id)\n"
            + "where date > ?\n"
            + "order by login";
            
        PreparedStatement pst = m_con.prepareStatement(sql);
        pst.setTimestamp(1, tsDate);
        
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            result.add(rs.getString("login"));
        }
        return result;
    }
    
    
    public ArrayList<String> selectUsersRegDiffRoles() throws SQLException {

        String sql = "select distinct u.login\n"
            + "from registration r1\n"
            + "join registration r2\n"
            + "on (r1.id = r2.id)\n"
            + "join users u\n"
            + "on (r1.id = u.id)\n"
            + "where r1.role <> r2.role\n"
            + "order by u.login";

        Statement st = m_con.createStatement();
        ResultSet rs = st.executeQuery(sql);
    
        ArrayList<String> result = new ArrayList<String>();
        while (rs.next()) {
            String user = rs.getString(1);
            result.add(user);
        }
        return result;
    }
    
    
    public ArrayList<String> selectDatesRegDiffUsers() throws SQLException {

        String sql = "select distinct cast (r1.date as DATE) onlydate\n"
                + "from registration r1\n"
                + "join registration r2\n"
                + "on (cast (r1.date as DATE) = cast (r2.date as DATE))\n"
                + "where r1.id <> r2.id";

        Statement st = m_con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        ArrayList<String> result = new ArrayList<String>();
        while (rs.next()) {
            Date date = rs.getDate(1);
            result.add(date.toString());
        }
        return result;
    }
    
    
    public void saveAllToFile(String path) throws IOException, SQLException {
        
        File file = new File(path);
        
        try (FileWriter fileWriter = new FileWriter(file);
                      BufferedWriter bw = new BufferedWriter(fileWriter)) {
            
            String sql = "select u.login, r.role, r.date\n"
                + "from registration r\n"
                + "join users u\n"
                + "on (r.id = u.id)";
            
            Statement st = m_con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            boolean firstLine = true;
            while (rs.next()) {

                // if (firstLine == false) {
                //     bw.newLine();
                // } else {
                //     firstLine = false;
                // }
                
                String login = rs.getString("login").trim();
                String role = rs.getString("role");
                Timestamp tsDate = rs.getTimestamp("date");

                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date date = new Date(tsDate.getTime());

                String sDate = dateFormat.format(date);

                bw.append(login + " " + role + " " + sDate + "\n");
                bw.newLine();
            }
        }
    }
    
    
    @Override
    public void close() throws Exception {
        this.m_con.close();
    }
    
    
    private boolean tableExists(String schemaName, String tableName) {
        
        boolean result = false;
        ResultSet rs = null;
        
        try {
            schemaName = schemaName.toUpperCase();
            tableName = tableName.toUpperCase();
            DatabaseMetaData dbmd = m_con.getMetaData();

            rs = dbmd.getTables(null, null, null, null);
            while (rs.next()) {
                String table = rs.getString(3);
                String schema = rs.getString(2);
                
                if ( schema.equals(schemaName) && table.equals(tableName)) {
                    result = true;
                    break;
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("SQL err: " + e.getMessage());
        } 
        finally {
            try {
                rs.close();
            } 
            catch (SQLException e) {
                System.out.println("ResultSet err: " + e.getMessage());
            }
        }
        
        return result;
    }
    
    
    private boolean sequenceExists(String seqName) {
        
        boolean result = false;
        seqName = seqName.toUpperCase();

        String sql = "select sequencename from sys.syssequences\n"
                + "where sequencename like '" + seqName + "'";
        
        try {
            Statement st = m_con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                String seq = rs.getString(1);
                result = true;
            } 
            else {
                result = false;
            }
        } catch (SQLException ex) {
            System.out.println("sequenceExist method err: " + ex.getMessage());
        }

        return result;
    }
    
    private boolean createTable(String sql, String schema, String table, String seq) throws SQLException {
    
        if (tableExists(schema, table)) {
            return false;
        }
        
        try (Statement st = m_con.createStatement()) {
            boolean res = st.execute(sql);
            if (res == false) {
                System.out.println(" - table " + table + " was created");
            }
            
            // DELETE SEQUENCE
            if (sequenceExists(seq)) {
                String sqlDelete = "drop sequence " + seq + " restrict";
                st.execute(sqlDelete);
                System.out.println(" - sequence " + seq + " was deleted");
            }
            
            // CREATE SEQUENCE
            String sqlCreate = "create sequence " + seq + " start with 1\n"
                + "minvalue 1 cycle";
            
            st.execute(sqlCreate);
            System.out.println(" - sequence " + seq + " was created");
        }
        return true;
    }
}
