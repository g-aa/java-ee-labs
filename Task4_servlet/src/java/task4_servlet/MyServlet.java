/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task4_servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andrey
 */
public class MyServlet extends HttpServlet {

    private double m_sumCount = 0;
    private ArrayList<String> m_strings = new ArrayList<String>();
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String s = request.getParameter("inputString");
        
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append(
            "<head>\n" +
            "<title>Task 4 - servlet page</title>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "</head>\n"
        );
        
        sb.append("<body style = \"font-size: 18px\">\n");
        
        sb.append("<div><h2>Task 4 - servlet page</h2></div>\n");
        
        sb.append("<form name = \"myServletForm\" method = \"get\" action = \"MyServlet\">\n");
        sb.append("<label id = \"lbl_txt\" for = \"txt_input\">Текст: </label>\n");
        sb.append("<input id =\"txt_input\" name = \"inputString\" type = \"text\" placeholder = \"введите символы...\" size = \"100\" maxlength=\"80\">\n");
        sb.append("<input type=\"submit\" value = \"Отправить\">\n");
        sb.append("<p>(пожалуйста введите текст длинной не более 80 символов)</p>\n");
        sb.append("</form>\n");
        
        sb.append("<div><a href = \"index.html\" style = 'font-size: 18px;'>go to start page</a></div>\n");
        
        sb.append("<div>");
        sb.append(this.analyzeInputString(s));
        sb.append("</div>");
        
        sb.append("</body>\n");
        sb.append("</html>\n");
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.println(sb.toString());
        }
        catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    
    // обработчик входных данных:
    private String analyzeInputString(String s) {
        
        if(s == null || "".equals(s.trim())){
            return "<p>Input string is empty!</p>";
        }
        
        try {
            this.m_sumCount += Double.parseDouble(s);
            return "<p>Total number result: " + this.m_sumCount +"</p>";
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        }
        
        this.m_strings.add(s);
        
        StringBuilder sb = new StringBuilder("<p>Total strings result:<p>\n");
        for (String item : this.m_strings) {
            sb.append("<p>").append(item).append("</p>\n");
        }
        return sb.toString();
    }
    
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}