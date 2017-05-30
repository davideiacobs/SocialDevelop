/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.HTMLResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class Login extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    private void login_error(HttpServletRequest request, HttpServletResponse response, String problem) throws TemplateManagerException {
        if(problem.equals("pwd")){
            //password errata  
            request.setAttribute("error_pwd", "password is not correct");
        }else{
            if(problem.equals("user")){
                //username/mail errata
                request.setAttribute("error_user", "username/mail is not correct");
                
            }
        }
        request.setAttribute("slider", "hidden");
        request.setAttribute("home_background", "home_background");
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("index.html",request, response);
    }
    
    
     private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException, SQLException, NamingException {
            
            String mail_username = request.getParameter("username");
            String pwd = request.getParameter("pwd");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            int dev_key = 0;
            if(mail_username.contains("@")){
                dev_key = datalayer.getDeveloperByMail(mail_username);
            }else{
                dev_key = datalayer.getDeveloperByUsername(mail_username);
            }
            Developer dev = datalayer.getDeveloper(dev_key);
            if(dev!=null){
                if(dev.getPwd().equals(pwd)){
                    SecurityLayer.createSession(request, dev.getUsername(), dev_key);
                    request.setAttribute("username", dev.getUsername());
                    request.setAttribute("logout", "Logout");
                    request.setAttribute("page_title", dev.getUsername()+", ");
                    request.setAttribute("page_subtitle", "Welcome back in SocialDevelop!");
                    TemplateResult res = new TemplateResult(getServletContext());
                    res.activate(null,request, response);
                } else {
                    //password errata
                    login_error(request, response, "pwd");
                }
            }else{
                //mail o username errato
                login_error(request, response, "user");
            }
            
            
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_login(request, response);
        } catch (IOException ex) {
             request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
             request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
             request.setAttribute("exception", ex);
            action_error(request, response);
         } catch (SQLException ex) {
               request.setAttribute("exception", ex);
            action_error(request, response);
          } catch (NamingException ex) {
               request.setAttribute("exception", ex);
            action_error(request, response);
          }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}