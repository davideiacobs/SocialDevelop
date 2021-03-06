/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.result.FailureResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author david
 */
public class removeCollaboratorFromTask extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
        
     private void remove_coll(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        
        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            
            int task_id = Integer.parseInt(request.getParameter("task_key"));
            int developer_key = Integer.parseInt(request.getParameter("dev_key"));    
            Task task = datalayer.getTask(task_id);
            int coord_key = datalayer.getProject(task.getProjectKey()).getCoordinatorKey();
            if(coord_key == (int) s.getAttribute("userid")){
                //l'utente che sta cercando di rimuovere il collaboratore è effettivamente
                //il coordiantore del progetto quindi gli è permesso falo
                int ret = datalayer.deleteTaskHasDeveloper(task_id, developer_key);
                
                int n = task.getNumCollaborators()+1;
                task.setNumCollaborators(n);
                datalayer.storeTask(task);
                
                datalayer.destroy();
                response.setContentType("text/plain"); 
                response.setCharacterEncoding("UTF-8"); 
                PrintWriter out = response.getWriter();

                try {
                    out.println(ret);
                }finally {
                    out.close();
                }   
                
            }else{
                response.sendRedirect("index");
            }
        }else{
           response.sendRedirect("index");
        }
    }
    
     
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            remove_coll(request, response);
        } catch (SQLException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (NamingException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (Exception ex) { 
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
