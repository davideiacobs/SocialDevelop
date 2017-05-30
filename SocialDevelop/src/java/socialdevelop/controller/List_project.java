/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author manuel
 */
public class List_project extends SocialDevelopBaseController {
    
   
     private void action_listproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            request.setAttribute("page_title", "Progetti disponibili");
            request.setAttribute("page_subtitle", "Progetti");
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                List<Project> pro = datalayer.getProjects();
                request.setAttribute("listaprogetti", pro);
                Files foto = null ;
                Developer coordinatore ;
                int ncollaboratori[] = new int[pro.size()];
                int ntask[] = new int[pro.size()];
                String fotos[] = new String[pro.size()];
                int count = 0;
                for(Project progetto : pro){
                    coordinatore=datalayer.getDeveloper(progetto.getCoordinatorKey());
                    List <Task> tasks = datalayer.getTasks(progetto.getKey());
                    ncollaboratori[count] = 0;
                    for(Task task : tasks){
                        ncollaboratori[count]+=task.getNumCollaborators();
                    }
                    ntask[count] = tasks.size();
                    progetto.setCoordinator(coordinatore);
                    int foto_key=coordinatore.getFoto();
                    if(foto_key != 0){
                        foto = datalayer.getFile(foto_key);
                        fotos[count] = "extra-images/" + foto.getLocalFile();
                    }else{
                        fotos[count] = "extra-images/foto_profilo_default.png";
                    }
                    count ++;
                }
                request.setAttribute("ntask", ntask);
                request.setAttribute("ncollaboratori", ncollaboratori);
                request.setAttribute("fotoCoordinatore", fotos);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("project_list.html",request, response);  //al posto di ciao va inserito il nome dell'html da attivare 
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_listproject(request, response);
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
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
