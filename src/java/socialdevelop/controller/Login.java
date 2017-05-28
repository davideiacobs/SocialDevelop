/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import socialdevelop.data.impl.SocialDevelopDataLayerMysqlImpl;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class Login extends SocialDevelopBaseController {
    
    @Resource(name = "jdbc/mydb")
    private DataSource ds;
   
     private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException, SQLException, NamingException {
            
            String mail_username = request.getParameter("username");
            String pwd = request.getParameter("pwd");
            //SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer)request.getAttribute("datalayer");
            //socialdevelopdatalayer è null
            SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
            datalayer.init();
            int dev_key = 0;
            if(mail_username.contains("@")){
                dev_key = datalayer.getDeveloperByMail(mail_username);
            }else{
                dev_key = datalayer.getDeveloperByUsername(mail_username);
            }
            Developer dev = datalayer.getDeveloper(dev_key);
            if(dev.getPwd().equals(pwd)){
                SecurityLayer.createSession(request, dev.getUsername(), dev_key);
                request.setAttribute("username", dev.getUsername());
                request.setAttribute("logout", "Logout");
                request.setAttribute("page_title", dev.getUsername()+", ");
                request.setAttribute("page_subtitle", "Welcome back in SocialDevelop!");
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate(null,request, response);
            } else {
                //action_error(request, response);
            }
            
            
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_login(request, response);
        } catch (IOException ex) {
            Logger.getLogger(MakeLoginReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(MakeLoginReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLayerException ex) {
             Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
              Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
          } catch (NamingException ex) {
              Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
          }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}