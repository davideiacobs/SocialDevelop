/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import static java.lang.Math.ceil;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
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
public class List_project extends SocialDevelopBaseController {
     
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    } 
   
     private void action_listproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Progetti disponibili");
        request.setAttribute("home", "hidden");
        request.setAttribute("page_subtitle", "Progetti");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                request.setAttribute("logout", "Logout");
            }else{
                request.setAttribute("MyProfile", "hidden");
        }
        int n = ((Integer.parseInt(request.getParameter("n")))-1)*6;
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        double pagesize = ceil((double)(datalayer.getProjects().size())/6);
        request.setAttribute("page",pagesize);
        request.setAttribute("selected",request.getParameter("n"));
        List<Project> pro = datalayer.getProjectsLimit(n);
            if (pro.size()!=0) {
               request.setAttribute("listaprogetti", pro);
               Files foto = null ;
               Date startdate[] = new Date[pro.size()];
               Developer coordinatore ;
               int ncollaboratori[] = new int[pro.size()];
               String fotos[] = new String[pro.size()];
               int count = 0;
               int c = 0;
               int[] ntasks=new int[pro.size()];
               startdate[c] = null;
               for(Project progetto : pro){
                   coordinatore=datalayer.getDeveloper(progetto.getCoordinatorKey());
                   List <Task> tasks = datalayer.getTasks(progetto.getKey());
                   ncollaboratori[count] = 0;
                   ntasks[count]=tasks.size();
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
               request.setAttribute("ntasks", ntasks); 
            }
            else{
                request.setAttribute("listaprogetti", pro);
                request.setAttribute("nontrovato","Nessun progetto trovato");
            }
            List<Skill> skills = datalayer.getSkillsParentList();
            if(skills!=null)
            {
                for(Skill skill : skills){
                    List<Skill> child = datalayer.getChild(skill.getKey());
                    if(child!=null){
                        skill.setChild(child);
                    }
                }
                request.setAttribute("skills", skills);
            }
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
        } catch (TemplateManagerException ex) {
            request.setAttribute("excpetion", ex);
            action_error(request, response);
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
