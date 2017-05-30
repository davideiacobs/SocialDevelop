/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author iacobs
 */
public class MyCollaborations extends SocialDevelopBaseController {
    
    @Resource(name = "jdbc/mydb")
    private DataSource ds;
    
    
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
    
    
    
    private void action_mycollaborations(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            HttpSession s = request.getSession(true);
            request.setAttribute("page_title", "My Profile");
            request.setAttribute("page_subtitle", "manage your data");
            if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
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
                request.setAttribute("datalayer", datalayer);
                getImg(request, response, dev);
                
                //recupero task a cui ha partecipato o sta partecipando (in cima quelli a cui sta partecipando
                Map<Task, Integer> tasks = datalayer.getTasksByDeveloper(dev.getKey());
                List<Developer> coordinators = new ArrayList();
                //recupero progetto e coordinatore
                for(Map.Entry<Task, Integer> entry : tasks.entrySet()){
                    Project p = datalayer.getProjectByTask(entry.getKey().getKey());
                    Developer c = datalayer.getDeveloper(p.getCoordinatorKey());
                    entry.getKey().setProject(p);
                    coordinators.add(c);   
                }
                request.setAttribute("tasksList", tasks);
                request.setAttribute("coordinators", coordinators);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("my_collaborations.html",request, response);  //al posto di ciao va inserito il nome dell'html da attivare
                
            }else{
                 response.sendRedirect("index");
            }
           
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_mycollaborations(request, response);
        } catch (IOException ex) {
            Logger.getLogger(MakeLoginReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(MakeLoginReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MyProfile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(MyProfile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLayerException ex) {
            Logger.getLogger(MyProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}