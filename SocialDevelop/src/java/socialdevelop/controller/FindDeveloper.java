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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.model.Admin;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author manuel
 */
public class FindDeveloper extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    } 
   
   private void action_listproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Search Developer");
        request.setAttribute("page_subtitle", "who are you looking for?");
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        int skilluser = Integer.parseInt(request.getParameter("skill"));
        int level = Integer.parseInt(request.getParameter("level"));
        int n = ((Integer.parseInt(request.getParameter("n")))-1)*6;
        double pagesize = ceil((double) datalayer.getDevelopersBySkill(skilluser, level).size()/6);
        request.setAttribute("page",pagesize);
        Map<Developer,Integer> devdl = datalayer.getDevelopersBySkillLimit(skilluser,level,n);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                request.setAttribute("logout", "Logout");
                Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
                if(admin!=null){
                    request.setAttribute("admin", "admin");
                }
                Developer currentUser = datalayer.getDeveloper((int) s.getAttribute("userid"));
                if(devdl.keySet().contains(currentUser)){
                    devdl.remove(currentUser);
                    pagesize = pagesize-1;
                }
            }
       
        //double pagesize = ceil((double)(datalayer.getDevelopersBySkill(skilluser,level).size())/6);
        
        request.setAttribute("selected",request.getParameter("n"));
        List <Developer> dev = new ArrayList<Developer>(devdl.keySet());
            if (dev.size() != 0) {
               request.setAttribute("listasviluppatori", dev);
               Files foto = null ;
               String fotos[] = new String[dev.size()];
               int projects[] = new int[dev.size()];
               int vote[] = new int[dev.size()];
               int count = 0;
               for(Developer developer : dev){
                   int foto_key=(developer).getFoto();
                   projects[count] = datalayer.getProjectCollaborators(developer.getKey()).size()+datalayer.getProjectsByCoordinator(developer.getKey()).size();
                   List<Integer> votes = new ArrayList<Integer>(datalayer.getTasksByDeveloper(developer.getKey()).values());
                   if(votes.size()> 0)
                    {
                        int count2 = 0;
                        vote[count] = 0;
                        for (int vote1 : votes ) 
                        {           
                            if(vote1>=0)
                            {
                                count2++;
                                vote[count] = vote[count]+vote1;
                            }
                        }
                        if(count2 != 0)
                        {
                            vote[count] = vote[count]/count2;
                        }
                    }
                    else
                    {
                        vote[count]=0;
                    }
                   if(foto_key != 0){
                       foto = datalayer.getFile(foto_key);
                       fotos[count] = "extra-images/" + foto.getLocalFile();
                   }else{
                       fotos[count] = "extra-images/foto_profilo_default.png";
                   }
                   count ++;
               }
               request.setAttribute("foto", fotos); 
               request.setAttribute("progetto", projects); 
               request.setAttribute("voto", vote); 
            }
            else{
                request.setAttribute("listasviluppatori", dev);
                request.setAttribute("nontrovato","There are no developers with these parameters in the system..");
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
        res.activate("developer_list.html",request, response);  //al posto di ciao va inserito il nome dell'html da attivare 
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
}
