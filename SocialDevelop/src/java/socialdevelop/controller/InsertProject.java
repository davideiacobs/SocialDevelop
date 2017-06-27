/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.impl.ProjectImpl;
import socialdevelop.data.impl.TaskImpl;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author manuel
 */
public class InsertProject extends SocialDevelopBaseController {

     private void action_insertproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        //recuperare coordinatore dalla sessione!
        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            String project_name = request.getParameter("project_name");
            String project_descr = request.getParameter("project_descr");
            int userid = (int) s.getAttribute("userid");
            //memorizziamo il progetto
            ProjectImpl p = new ProjectImpl(datalayer);
            p.setCoordinatorKey(userid);
            p.setName(project_name);
            p.setDescription(project_descr);
            int project_key = datalayer.storeProject(p);
            //ora recuperiamo le info sui task e le memorizziamo
            String tasks = request.getParameter("tasks");
            String [] task = tasks.split("@");
            for(String t : task){
                
                String [] thistask = t.split("#");
                TaskImpl current = new TaskImpl(datalayer);
                current.setName(thistask[0]);
                current.setProjectKey(project_key);
                String start = thistask[1];
                GregorianCalendar gc = new GregorianCalendar();
                gc.setLenient(false);
                gc.set(GregorianCalendar.YEAR, Integer.valueOf(start.split("/")[2]));
                gc.set(GregorianCalendar.MONTH, Integer.valueOf(start.split("/")[1])-1); //parte da 0
                gc.set(GregorianCalendar.DATE, Integer.valueOf(start.split("/")[0]));
                current.setStartDate(gc);

                String end = thistask[2];
                GregorianCalendar gc1 = new GregorianCalendar();
                gc.setLenient(false);
                gc.set(GregorianCalendar.YEAR, Integer.valueOf(end.split("/")[2]));
                gc.set(GregorianCalendar.MONTH, Integer.valueOf(end.split("/")[1])-1); //parte da 0
                gc.set(GregorianCalendar.DATE, Integer.valueOf(end.split("/")[0]));
                current.setEndDate(gc1);

                current.setDescription(thistask[3]);
                current.setNumCollaborators(Integer.parseInt(thistask[4]));
                current.setType_key(datalayer.getTypeByName(thistask[6]));
                int task_key = datalayer.storeTask(current);

                //ora memorizziamo le skill associate e i livelli


//--------------------------CONTROLLARE DA QUI!!!! ------------------------------------------
                String [] skills = thistask[5].split(";");
                for(String skl : skills){
                    String n = skl.split("(")[0];
                    int l = Integer.parseInt(skl.split(")")[1]);
                    datalayer.storeTaskHasSkill(task_key, datalayer.getSkillByName(n), l);
                }
            datalayer.destroy();
            response.sendRedirect("MyProjects");
            }
        }else{
            response.sendRedirect("index");
        }
       
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_insertproject(request, response);
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
