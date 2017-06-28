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
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author manuel
 */
public class FindDeveloper extends SocialDevelopBaseController {

   private void action_listproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Sviluppatori");
        request.setAttribute("page_subtitle", "Sviluppatori");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                request.setAttribute("logout", "Logout");
            }
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        List<Project> pro = datalayer.getProjects();
            if (pro.size()!=0) {
               request.setAttribute("listaprogetti", pro);
               Files foto = null ;
               Date startdate[] = new Date[pro.size()];
               Developer coordinatore ;
               int ncollaboratori[] = new int[pro.size()];
               String fotos[] = new String[pro.size()];
               int count = 0;
               int c = 0;
               startdate[c] = null;
               for(Project progetto : pro){
                   coordinatore=datalayer.getDeveloper(progetto.getCoordinatorKey());
                   List <Task> tasks = datalayer.getTasks(progetto.getKey());
                   ncollaboratori[count] = 0;
                   startdate[c] = datalayer.getDateOfTaskByProject(progetto.getKey());
                   for(Task task : tasks){
                       ncollaboratori[count]+=task.getNumCollaborators();
                   }
                   progetto.setCoordinator(coordinatore);
                   int foto_key=coordinatore.getFoto();
                   if(foto_key != 0){
                       foto = datalayer.getFile(foto_key);
                       fotos[count] = "extra-images/" + foto.getLocalFile();
                   }else{
                       fotos[count] = "extra-images/foto_profilo_default.png";
                   }
                   count ++;
                   c++;
               }
               request.setAttribute("inizioprogetto", startdate);
               request.setAttribute("ncollaboratori", ncollaboratori);
               request.setAttribute("fotoCoordinatore", fotos); 
            }
            else{
                request.setAttribute("listaprogetti", pro);
             request.setAttribute("nontrovato","Nessun progetto trovato");
            }
            List<Skill> skills = datalayer.getSkillsParentList();
        //ora recuperiamo per ognuna di esse le skills figlie
        if(skills!=null){
            for(Skill skill : skills){
                List<Skill> child = datalayer.getChild(skill.getKey());
                if(child!=null){
                    skill.setChild(child);
                }
            }
        }
        request.setAttribute("skills", skills);
           datalayer.destroy();
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
}
