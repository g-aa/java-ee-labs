/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebPackage;

import MyPackage.IMessageBean;
import MyPackage.MessageException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Andrey
 */
@WebServlet(name = "Task5", urlPatterns = {"/Task5"})
public class Task5 extends HttpServlet {

    private static final String EJB_OBJNAME = "java:global/Task5_ejb/Task5_ejb-ejb/MessageBean!MyPackage.IMessageBean";
    private static final String EJB_OBJATR = "msgBean";
    
    private IMessageBean m_msgBean;
    HttpSession m_session;
    
    
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
        
        if(request.getParameter("logOut") != null) {
            this.m_msgBean = (IMessageBean)m_session.getAttribute(Task5.EJB_OBJATR);
            this.m_msgBean.logout();

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println(this.getMessageResponse("Вы вышли из учетной записи!"));
            }
        }
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
        
        this.m_session = request.getSession();
        if (this.m_session.isNew()) {
            try {
                InitialContext ic = new InitialContext();
                this.m_msgBean = (IMessageBean)ic.lookup(Task5.EJB_OBJNAME);
                this.m_session.setAttribute(Task5.EJB_OBJATR, this.m_msgBean);
            } 
            catch (NamingException e) {
                System.out.println("---> IMessageBean ошибка инициализации: " + e.getMessage());
                
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println(this.ejbInitialisationFaultResponse(e.getMessage()));
                }
                return;
            }
        } 
        else {
            this.m_msgBean = (IMessageBean)m_session.getAttribute(Task5.EJB_OBJATR);
        }

        
        if (request.getParameter("register") != null) {
            // регистрация:
            
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            
            // переходн на страницу с сообщениями:
            if(this.m_msgBean.login(login, password)) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println(this.getMessageResponse(""));
                }
            }
            else {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println(this.authorizationFaultResponse(login));
                }
            }
        }
        else if (request.getParameter("getMsg") != null) { 
        // вывод сообщения:
        
            String sIdx = request.getParameter("msgIndex");
            String s = null;
            try{
                int idx = Integer.parseInt(sIdx);
                s = this.m_msgBean.getMessage(idx);
            }
            catch (MessageException e) {
                s = e.getMessage();
            }
            catch (NumberFormatException e) {
                s = "Было введено не целое число!";
            }
        
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println(this.getMessageResponse(s));
            }
        }
        else if(request.getParameter("getUsers") != null) { 
            // получить список зарегестрированных пользователей:
            String s = null;
            try {
                String[] users = this.m_msgBean.getUsers();
                s = String.join(", ", users);
            }
            catch (MessageException e) {
                s = e.getMessage();
            }
            
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println(this.getMessageResponse(s));
            }
        }
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

    
    private String ejbInitialisationFaultResponse(String message) {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<title>Task 5  - ejb component page</title>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("</head>\n");
        sb.append("<body style = \"font-size: 18px\">\n");
        sb.append("<div align=\"center\">\n");
        sb.append("<h2>Task 5 - ejb component page</h2>\n");
        sb.append("<h4>Инициализация ejb компонента завершилась ошибкой!</h4>\n");
        
        // вывод сообщения:
        sb.append("<h4>Исключение: '");
        sb.append(message);
        sb.append("'</h4>\n");
        
        sb.append("<a href = \"index.html\" style = 'font-size: 18px;'>go to: start page</a>\n");
        sb.append("</form>\n");
        sb.append("</div>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        
        return sb.toString();
    }
    
    
    private String authorizationFaultResponse(String message) {
    
        StringBuilder sb = new StringBuilder();
        
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<title>Task 5  - ejb component page</title>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("</head>\n");
        sb.append("<body style = \"font-size: 18px\">\n");
        sb.append("<div align=\"center\">\n");
        sb.append("<h2>Task 5 - ejb component page</h2>\n");
        sb.append("<h4>Процесс аутентификации завершился неудачно!</h4>\n");
        
        // вывод сообщения:
        sb.append("<h4>В базе отсутствует данный '");
        sb.append(message);
        sb.append("' пользователь!</h4>\n");
        
        sb.append("<a href = \"task5_authentication.html\" style = 'font-size: 18px;'>go to: authentication page</a>\n");
        sb.append("</form>\n");
        sb.append("</div>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        
        return sb.toString();
    }
    
    
    private String getMessageResponse(String message){
    
        StringBuilder sb = new StringBuilder();
        
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<title>Task 5  - ejb component page</title>\n");
        sb.append("<meta charset=\"UTF-8\">\n");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("</head>\n");
        sb.append("<body style = \"font-size: 18px\">\n");
        sb.append("<div align=\"center\">\n");
        sb.append("<h2>Task 5 - ejb component page</h2>\n");
        sb.append("<form action=\"Task5\" method=\"post\"><br>\n");
        sb.append("<input type=\"text\" name=\"msgIndex\" placeholder=\"индекс сообщения\">\n");
        sb.append("<input type=\"submit\" name=\"getMsg\" value=\"получить сообщение\"><br><br>\n");
        sb.append("<input type=\"submit\" name=\"getUsers\" value=\"список авторизованных пользователей\"><br><br>");
        sb.append("</form>\n");
        
        // выход из учетной записи:
        sb.append("<form action=\"Task5\" method=\"get\"><br>\n");
        sb.append("<input type=\"submit\" name=\"logOut\" value=\"выйти из учетной записи\"><br><br>\n");
        sb.append("</form>\n");
        sb.append("<a href = \"task5_authentication.html\" style = 'font-size: 18px;'>go to: authentication page</a><br><br>\n");
            
        // вывод сообщения:
        sb.append("<p>");
        sb.append(message);
        sb.append("</p><br><br>\n");

        sb.append("</div>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        
        return sb.toString();
    }
    
}
