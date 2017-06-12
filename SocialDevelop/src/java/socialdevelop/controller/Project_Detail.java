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
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import socialdevelop.data.impl.SocialDevelopDataLayerMysqlImpl;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.Message;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author Andrea
 */
//@WebServlet(name = "Project_Detail", urlPatterns = {"/Project_Detail"})
public class Project_Detail extends SocialDevelopBaseController {
    @Resource(name = "jdbc/mydb")
    private DataSource ds;
    
    private void action_project(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
                SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
                datalayer.init();
                
                
                
                HttpSession s = request.getSession(true);
                Project project = datalayer.getProject(1);
                request.setAttribute("page_title", "Project" + " " + project.getName());
                request.setAttribute("page_subtitle", "Check project info");
                int foto_key;
                Map<Developer,Integer> collaborators = new HashMap<Developer,Integer>();
                request.setAttribute("projectname", project.getName());
                request.setAttribute("projectdescr", project.getDescription());
                List <Task> tasks = datalayer.getTasks(project.getKey());
                List <Task> tasksEnded = new ArrayList();
                request.setAttribute("tasks", tasks);
                
                
                for (Task task : tasks){
                    if(!task.isOpen()){
                        tasksEnded.add(task);
                    }
                    
                    collaborators.putAll(datalayer.getCollaboratorsByTask(task.getKey()));
                    request.setAttribute("collaborators", task.getNumCollaborators());
                    GregorianCalendar start = task.getStartDate();
                    GregorianCalendar end = task.getEndDate();
                    Date startDate = start.getTime();
                    Date endDate = end.getTime();
                    long startTime = startDate.getTime();
                    long endTime = endDate.getTime();
                    long diffTime = endTime - startTime;
                    long diffDays = diffTime / (1000 * 60 * 60 * 24);
                    request.setAttribute("daysleft",diffDays);
                    
                    
                    Map <Skill, Integer> skillsList = datalayer.getSkillsByTask(task.getKey());
                    request.setAttribute("skillsList", skillsList);
                    if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                        Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
                        Map <Skill,Integer> devSkill = dev.getSkillsByDeveloper();
                        for( Map.Entry <Skill,Integer> entryP : skillsList.entrySet()){
                            if(devSkill.containsKey(entryP.getKey())){
                                for (Map.Entry <Skill,Integer> entryD : devSkill.entrySet() ){
                                    if(entryP.getKey().equals(entryD.getKey())){
                                        request.setAttribute("uskill",entryD.getValue());
                                    }
                                }
                            }
                            else{
                                request.setAttribute("skillN", 1);
                            }
                        }
                    }
                    
                    
                
                }
                double percProg = Math.round(((double)tasksEnded.size() / (double)tasks.size())*100) ;
                
                request.setAttribute("percProg", percProg);
                request.setAttribute("numCollaborators", collaborators.size());
                
                
                
                
                boolean m  = false;
                
                if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0){
                    Developer dev=datalayer.getDeveloper((int)s.getAttribute("userid"));
                    if (collaborators.containsKey(dev)){
                        m = true;
                        
                    }
                }
                
                    List <Message> messages = new ArrayList();
                
                if(m){
                    
                    messages = datalayer.getMessages(project.getKey());
                }
                else{
                    messages = datalayer.getPublicMessages(project.getKey());
                }
                
                request.setAttribute("mex_number",messages.size());
                request.setAttribute("messages", messages);
                
                for (Message message : messages){
                    
                    Developer dev2 = message.getDeveloper();
                    foto_key=dev2.getFoto();
                            
                    
                        
                        if(foto_key != 0){
                            Files foto = datalayer.getFile(foto_key);
                            request.setAttribute("coordinatorpic", "extra-images/" + foto.getLocalFile());
                        }
                        else{
                            request.setAttribute("coordinatorpic", "extra-images/foto_profilo_default.png");
                        }
                            
                        request.setAttribute("username", dev2.getUsername());
                        request.setAttribute("text", message.getText());
                            //insert only private messages
                        
                    
                    
                }     
                
                Developer coordinator=datalayer.getDeveloper(project.getCoordinatorKey());
                foto_key=coordinator.getFoto();
                if(foto_key != 0){
                    Files foto = datalayer.getFile(foto_key);
                    request.setAttribute("coordinatorpic", "extra-images/" + foto.getLocalFile());
                }
                else{
                    request.setAttribute("coordinatorpic", "extra-images/foto_profilo_default.png");
                }
                
                String name = coordinator.getName().substring(0,1).toUpperCase() + coordinator.getName().substring(1);
                String surname =coordinator.getSurname().substring(0,1).toUpperCase() + coordinator.getSurname().substring(1);
                request.setAttribute("coordinatorusername",name+ " " + surname) ;
                request.setAttribute("coordinatorbiography",coordinator.getBiography());
               
           
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("project_detail.html",request, response);  
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    }
    
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            action_project(request,response);
        }
        catch (IOException | TemplateManagerException | SQLException | NamingException | DataLayerException ex) {
            Logger.getLogger(Project_Detail.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }


