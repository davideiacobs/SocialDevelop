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
public class acceptProposal extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    private void acceptproposal(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        
        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            int user_key = (int) s.getAttribute("userid");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            int developer_key = 0;
            if(request.getParameter("developer_key")!=null){
                developer_key = Integer.parseInt(request.getParameter("developer_key"));
            }
            int task_key = Integer.parseInt(request.getParameter("task_key"));
            int state = Integer.parseInt(request.getParameter("state"));
            
            if(developer_key==0){
                int sender_key = Integer.parseInt(request.getParameter("sender"));
                datalayer.storeTaskHasDeveloper(task_key, user_key, state, -1, sender_key);
            }else{
                datalayer.storeTaskHasDeveloper(task_key, developer_key, state, -1, developer_key);
            }
            
            if(state==1){
                Task task = datalayer.getTask(task_key);
                if(task.getNumCollaborators()!=0){
                    int n = task.getNumCollaborators()-1;
                    task.setNumCollaborators(n);
                    datalayer.storeTask(task);
                }
            }
            
            datalayer.destroy();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            
            try {
                out.println("Accepted");
            }finally {
                out.close();
            }
        }
        
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            acceptproposal(request, response);
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
