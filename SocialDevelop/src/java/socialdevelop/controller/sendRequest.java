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
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.SocialDevelopDataLayer;


/**
 *
 * @author david
 */
public class sendRequest extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    private void sendRequest(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        
        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            int user_key = (int) s.getAttribute("userid");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            int task_id = Integer.parseInt(request.getParameter("task_key"));
            int dev_key = Integer.parseInt(request.getParameter("dev_key"));
            
            Project p = datalayer.getProjectByTask(task_id);
            int coordinator_key = p.getCoordinatorKey();
            if(user_key==coordinator_key){
                int ret = datalayer.storeTaskHasDeveloper(task_id, dev_key, 0, -1, user_key);

                //sender=1 --> inviata da collaboratore
                //stato=0 --> in attesa
                //voto=-1 --> non rilasciato

                //l'utente che sta cercando di rimuovere il collaboratore è effettivamente
                //il coordiantore del progetto quindi gli è permesso falo
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
            sendRequest(request, response);
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
