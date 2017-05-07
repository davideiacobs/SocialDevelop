/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import socialdevelop.data.model.CollaborationRequest;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Message;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;
import socialdevelop.data.model.Type;

/**
 *
 * @author david
 */
public class provaDB2 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Resource(name = "jdbc/mydb")
    private DataSource ds;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataLayerException {
        response.setContentType("text/html;charset=UTF-8");
        String sel = "";
        try {
            SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
            datalayer.init();
            
            /*
            -- TEST STORE TYPE -- 
            TypeImpl type = new TypeImpl(datalayer);
            type.setType("mariotti");
            datalayer.storeType(type);
            //sel = datalayer.getType(10).getType();

            -- TEST STORE DEVELOPER --             
            DeveloperImpl dev = new DeveloperImpl(datalayer);
            dev.setName("dav");
            dev.setSurname("iacob");
            dev.setUsername("iacobs");
            dev.setMail("blabla@blabla");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(false);
            gc.set(GregorianCalendar.YEAR, 1995);
            gc.set(GregorianCalendar.MONTH, 03); //parte da 0
            gc.set(GregorianCalendar.DATE, 27);
            dev.setBirthDate(gc);
            dev.setPwd("aooooo");
            dev.setBiography("come andiamo??");
            dev.setCurriculum("eeeeeehheeheheh");
            datalayer.storeDeveloper(dev);
            
            -- TEST STORE PROJECT --             
            ProjectImpl prj = new ProjectImpl(datalayer);
            prj.setCoordinatorKey(2);
            prj.setName("project2");
            sel = prj.getName();
            prj.setDescription("blabla");
            datalayer.storeProject(prj);
            
            
            -- TEST STORE TASK --
            TaskImpl tsk = new TaskImpl(datalayer);
            tsk.setName("sviluppo");
            tsk.setDescription("come andiamo");
            tsk.setNumCollaborators(8);
            tsk.setOpen(true);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(false);
            gc.set(GregorianCalendar.YEAR, 2018);
            gc.set(GregorianCalendar.MONTH, 07); //parte da 0
            gc.set(GregorianCalendar.DATE, 28);   
            tsk.setStartDate(gc);
            gc.set(GregorianCalendar.YEAR, 2018);
            gc.set(GregorianCalendar.MONTH, 10); //parte da 0
            gc.set(GregorianCalendar.DATE, 28);  
            tsk.setEndDate(gc);
            tsk.setProjectKey(1);
            //tsk.setProject(datalayer.getProject(1));
            datalayer.storeTask(tsk);
            sel = tsk.getName();
            
            /*--TEST STORE SKILL--
            SkillImpl skl = new SkillImpl(datalayer);
            skl.setName("python");
            skl.setParentKey(1);
            datalayer.storeSkill(skl);
            sel = skl.getName();
            
            --TEST STORE MESSAGE--
            MessageImpl msg = new MessageImpl(datalayer);
            msg.setPrivate(false);
            msg.setProjectKey(2);
            msg.setText("blablabla");
            msg.setType("commento");
            datalayer.storeMessage(msg);
            sel = msg.getText();
            
            --TEST GET PROJECT BY KEY--
            Project prj = datalayer.getProject(2);
            sel = prj.getName();
            
            --TEST GET PROJECTS (project list)--
            List<Project> list = datalayer.getProjects();
            for (Project item : list) {
                sel = sel + " " + item.getName()
            }
            
            --TEST GET MESSAGE BY KEY--
            Message msg = datalayer.getMessage(1);
            sel = msg.getText();
            
            --TEST GET MESSAGES BY PROJECT--
            List<Message> list = datalayer.getMessages(1);
            for (Message item : list) {
                sel = sel + " " + item.getText();
            }
            
            --TEST GET PROJECT BY FILTER--
            List<Project> list = datalayer.getProjects("pro");
            for (Project item : list) {
                sel = sel + " " + item.getName();
            }
            
            --TEST GET TASKS BY PROJECT KEY--
            List<Task> list = datalayer.getTasks(1);
            for (Task item : list) {
                sel = sel + " " + item.getName();
            }
            
            --TEST GET SKILL BY KEY--
            Skill skl = datalayer.getSkill(1);
            sel = skl.getName();
            
            --TEST GET TYPE BY KEY--
            Type type = datalayer.getType(1);
            sel = type.getType();
            
            --TEST GET TASK BY KEY--
            Task tsk = datalayer.getTask(14);
            sel = tsk.getName();
            
            --TEST GET TYPE BY TASK KEY--
            Type type = datalayer.getTypeByTask(14);
            sel = type.getType();
            
            --TEST STORE TASK_HAS_SKILL--
            datalayer.storeTaskHasSkill(14, 1, 1, 7);
            
            --TEST GET SKILLS BY TASK--
            Map<Skill, Integer> res = datalayer.getSkillsByTask(14);
            for (Map.Entry<Skill, Integer> entry : res.entrySet()){
                sel = sel + " "+ entry.getKey().getName()+" "+ String.valueOf(entry.getValue());
            }
            
            --TEST GET DEVELOPER BY KEY--
            Developer dev = datalayer.getDeveloper(1);
            sel = dev.getName()+" "+dev.getSurname()+" "+dev.getUsername();
            
            --TEST GET PARENT SKILL BY KEY--
            Skill skl = datalayer.getParent(18);
            sel = skl.getName();
            
            --TEST GET CHILD SKILL BY KEY--
            List<Skill> list = datalayer.getChild(1);
             for (Skill item : list) {
                sel = sel + " " + item.getName();
            }
            
            TEST STORE TASK_HAS_DEVELOPER/COLLABORATION REQUEST--
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(false);
            gc.set(GregorianCalendar.YEAR, 2018);
            gc.set(GregorianCalendar.MONTH, 07); //parte da 0
            gc.set(GregorianCalendar.DATE, 28);
            datalayer.storeCollaborationRequest(19, 2, 1, gc,8,1);
            
            /*
            
            --TEST GET COORDINATOR BY TASK ID--
            Developer coord = datalayer.getCoordinatorByTask(14);
            sel = coord.getName();
            
            --TEST GET TASKS BY DEVELOPER ID--
            Map<Task,Integer> tsk = datalayer.getTasksByDeveloper(1);
            for (Map.Entry<Task, Integer> entry : tsk.entrySet()){
                sel = sel + " "+ entry.getKey().getName()+" "+ String.valueOf(entry.getValue());
            }
            
            --TEST STORE SKILL_HAS_DEVELOPER--
            datalayer.storeSkillHasDeveloper(18, 1, 10);
            
            --TEST GET COLLABORATORS BY TASK ID--
            Map<Developer, Integer> res = datalayer.getCollaboratorsByTask(14);
            for (Map.Entry<Developer, Integer> entry : res.entrySet()){
                sel = sel + " "+ entry.getKey().getName()+" "+ String.valueOf(entry.getValue());
            }
            
            --TEST GET DEVELOPERS BY SKILL--
            Map<Developer,Integer> res = datalayer.getDevelopersBySkill(18);
            for (Map.Entry<Developer, Integer> entry : res.entrySet()){
                sel = sel + " "+ entry.getKey().getName()+" "+ String.valueOf(entry.getValue());
            }
            
            --TEST GET DEVELOPERS BY SKILL KEY AND LEVEL--
            Map<Developer,Integer> res = datalayer.getDevelopersBySkill(18,10);
            for (Map.Entry<Developer, Integer> entry : res.entrySet()){
                sel = sel + " "+ entry.getKey().getName()+" "+ String.valueOf(entry.getValue());
            }
            
            --TEST GET SKILLS BY DEVELOPER KEY--
            Map<Skill, Integer> res = datalayer.getSkillsByDeveloper(1);
            for(Map.Entry<Skill, Integer> entry : res.entrySet()){
                sel = sel + " "+ entry.getKey().getName()+" "+ String.valueOf(entry.getValue());
            }
            
            --TEST GET COLLABORATORS BY PROJECT ID--
            List<Developer> list = datalayer.getProjectCollaborators(1);
            for (Developer item : list) {
                sel = sel + " " + item.getName();
            }
            
            --TEST GET PROJECTS BY DEVELOPER KEY--
            List<Project> list = datalayer.getDeveloperProjects(1);
            for (Project item : list) {
                sel = sel + " " + item.getName();
            }
            
            --TEST GET PROJECTS BY DEVELOPER KEY AND DATE--
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(false);
            gc.set(GregorianCalendar.YEAR, 2018);
            gc.set(GregorianCalendar.MONTH, 07); //parte da 0
            gc.set(GregorianCalendar.DATE, 28);
            List<Project> list = datalayer.getDeveloperProjects(1, gc);
            for (Project item : list) {
                sel = sel + " " + item.getName();
            }
            
            --TEST GET OFFERTS BY DEVELOPER ID--
            List<CollaborationRequest> list = datalayer.getOffertsByDeveloper(2);
            for (CollaborationRequest item : list) {
                sel = sel + " " + item.getCoordinatorRequest().getName();
            }
            
            --TEST GET PUBLIC MESSAGES BY PROJECT KEY-- 
            List<Message> list = datalayer.getPublicMessages(2);
            for (Message item : list) {
                sel = sel + " " + item.getKey();
            }
            
            --TEST GET COLLABORATION REQUEST BY TASK AND COLLABORATOR IDs--
            CollaborationRequest cr = datalayer.getCollaborationRequest(1, 14);
            sel = cr.getCoordinatorRequest().getName();
            
            --TEST GET INVITES BY COORDINATOR ID--
            List<CollaborationRequest> list = datalayer.getInvitesByCoordinator(1);
            for (CollaborationRequest item : list) {
                sel = sel + " " + item.getCoordinatorRequest().getName();
            }
            
            --TEST GET PROPOSALS BY COLLABORATOR ID--
            List<CollaborationRequest> list = datalayer.getProposalsByCollaborator(2);
            for (CollaborationRequest item : list) {
                sel = sel + " " + item.getCoordinatorRequest().getName();
            }
            
            --TEST GET COORDINATOR BY TASK ID--
            Developer dev = datalayer.getCoordinatorByTask(15);
            sel = dev.getName();
            
            --TEST QUESTIONS BY COORDINATOR ID--
            List<CollaborationRequest> list = datalayer.getQuestionsByCoordinator(1);
            for (CollaborationRequest item : list) {
                sel = sel + " " + item.getCoordinatorRequest().getName();
            }*/
            
            
        } catch (SQLException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet provaDB2</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet provaDB2 at " + sel + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataLayerException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataLayerException ex) {
            Logger.getLogger(provaDB2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
