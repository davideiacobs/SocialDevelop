/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author iacobs
 */
public class DeveloperCollaborators extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    
    private void getImg(HttpServletRequest request, HttpServletResponse response, Developer dev) throws IOException, SQLException, DataLayerException, NamingException {
        StreamResult result = new StreamResult(getServletContext());
        
         SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
         if(dev.getFoto() != 0){
            Files foto_profilo = datalayer.getFile(dev.getFoto());
            request.setAttribute("foto_profilo", "extra-images/" + foto_profilo.getLocalFile());
         }else{
            request.setAttribute("foto_profilo", "extra-images/foto_profilo_default.png");             
         }
        
    }
    
   
    
    private void action_mycollaborators(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer)request.getAttribute("datalayer");
            int dev_key = Integer.parseInt(request.getParameter("n"));
            Developer dev = datalayer.getDeveloper(dev_key);
            if(dev!=null){
                request.setAttribute("username", dev.getUsername());
                request.setAttribute("fullname", dev.getName()+" "+dev.getSurname());
                long currentTime = System.currentTimeMillis();
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(currentTime);
                //Get difference between years
                request.setAttribute("age", now.get(Calendar.YEAR) - dev.getBirthDate().get(Calendar.YEAR));
                request.setAttribute("bio", dev.getBiography());
                request.setAttribute("mail", dev.getMail());
                request.setAttribute("logout", "Logout");
                request.setAttribute("id", dev_key);
                request.setAttribute("datalayer", datalayer);
                getImg(request, response, dev);
                //recupero progetti gestiti dall'utente (progetti dei quali è il coordinatore)
                
                
                List<Task> tasks = new ArrayList<Task> (datalayer.getTasksByDeveloper(dev_key).keySet());
                List<Developer> collaborators = new ArrayList();
                for(Task t : tasks){
                    List<Developer> t_coll = new ArrayList<Developer> (t.getCollaborators().keySet());
                    for(Developer d : t_coll){
                        if(!d.equals(dev)){
                            if(!collaborators.contains(d)){
                                collaborators.add(d);
                            }
                        }
                    }
                }
                request.setAttribute("collaborators", collaborators);
                request.setAttribute("page_title", "Developer");
                request.setAttribute("page_subtitle", dev.getUsername());
                request.setAttribute("notmy", "notmy");
                
                
                datalayer.destroy();    
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("developer_collaborators.html",request, response);  //al posto di ciao va inserito il nome dell'html da attivare
                
            }else{
                 response.sendRedirect("index");
            }
           
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_mycollaborators(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}