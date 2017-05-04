/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class provaDB2 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Resource(name = "jdbc/mydb")
    private DataSource ds;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataLayerException {
        response.setContentType("text/html;charset=UTF-8");
        String sel = "";
        try {
            SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
            datalayer.init();
            TypeImpl type = new TypeImpl(datalayer);
            type.setType("mariotti");
            datalayer.storeType(type);
            sel = datalayer.getType(10).getType();
            
            /*DeveloperImpl dev = new DeveloperImpl(datalayer);
            dev.setName("davide");
            dev.setSurname("iacobelli");
            dev.setUsername("iacobs");
            dev.setMail("blabla@blabla");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(false);
            gc.set(GregorianCalendar.YEAR, 1995);
            gc.set(GregorianCalendar.MONTH, 04);
            gc.set(GregorianCalendar.DATE, 27);
            dev.setBirthDate(gc);
            dev.setPwd("aooooo");
            dev.setBiography("come andiamo??");
            dev.setCurriculum("eeeeeehheeheheh");
            datalayer.storeDeveloper(dev);*/
            
        } catch (SQLException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet provaDB2</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet provaDB2 at " + sel + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataLayerException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataLayerException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
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

}
