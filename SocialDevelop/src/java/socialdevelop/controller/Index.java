/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author iacobs
 */
public class Index extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    
    private void action_home(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
            
            HttpSession s = request.getSession(true);
            if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                response.sendRedirect("MyProfile");
            }else{
                String problem = (String) s.getAttribute("problem");
                if(s.getAttribute("problem")!= null){
                    if(problem.equals("login_all")){
                        request.setAttribute("error_login_all", "please, compile the fields");
                        request.setAttribute("hidden_login_all", " ");
                    }else if(problem.equals("login_pwd")){
                        request.setAttribute("error_pwd", "password is not correct");
                        request.setAttribute("hidden_login_pwd", " ");
                    }else if(problem.equals("login_user")){
                        request.setAttribute("hidden_login_user", " ");
                        request.setAttribute("error_user", "username/mail is not correct");
                    }else if(problem.equals("reg_all")){
                        request.setAttribute("error_register_all", "please, compile the fields");
                        request.setAttribute("hidden_register_all", " ");
                    }else if(problem.equals("reg_pwd")){
                        request.setAttribute("hidden_register_pwd", " ");
                        request.setAttribute("error_pwd", "passwords do not match");
                    }else if(problem.equals("reg_username")){
                        request.setAttribute("hidden_register_username", " ");
                        request.setAttribute("error_username", "username already exists");
                    }else if(problem.equals("reg_email")){
                        request.setAttribute("hidden_register_email", " ");
                        request.setAttribute("error_email", "email already exists");
                    }
                    s.removeAttribute("problem");
                }
                
                request.setAttribute("slider", "hidden");
                request.setAttribute("home_background", "home_background");
                String u = (String) s.getAttribute("previous_url");
                if(s.getAttribute("previous_url") != null && (u.equals("/socialdevelop/Signup"))){
                    request.setAttribute("active", "register");
                }else{
                    request.setAttribute("active", "login");
                } 
                request.setAttribute("MyProfile", "hidden");
                String act_url = request.getRequestURI();
                s.setAttribute("previous_url", act_url);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("index.html",request, response);
            }
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_home(request, response);
        } catch (IOException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (TemplateManagerException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
