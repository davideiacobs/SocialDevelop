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
import javax.servlet.http.HttpSession;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Type;

/**
 *
 * @author manuel
 */
public class CreateProject extends SocialDevelopBaseController {

     private void action_createproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Crea Progetto");
        request.setAttribute("page_subtitle", "Nuovo Progetto");
        
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            request.setAttribute("logout", "Logout");
        }
        
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        //recupero skills che non hanno figli
        List<Type> types = datalayer.getTypes();
        if(types!=null){
            request.setAttribute("types", types);
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
        request.setAttribute("skills", skills);
        }
        datalayer.destroy();
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("create_project.html",request, response);  //al posto di ciao va inserito il nome dell'html da attivare 
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_createproject(request, response);
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
