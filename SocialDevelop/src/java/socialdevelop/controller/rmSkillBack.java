/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
import static java.sql.Types.NULL;
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
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author Andrea
 */

public class rmSkillBack extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    
    private void action_rmskillback(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {    
                
                           
                HttpSession s = request.getSession(true);
                String u = (String) s.getAttribute("previous_url");
                if(s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                    if(s.getAttribute("previous_url") != null && ((String) s.getAttribute("previous_url")).equals("/socialdevelop/BackEndSkill")){

                        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                        Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
                        Admin admin = datalayer.getAdmin(dev.getKey());
                        if (admin != null && admin.getDevelperKey() > 0){
                            Skill deadSkill = datalayer.getSkill(parseInt(request.getParameter("rm-skill-b")));
                            List <Skill> skills = deadSkill.getChild();
                            if(!(skills.isEmpty())){
                                for (Skill skill : skills){
                                    skill.setParentKey(NULL);
                                    datalayer.storeSkill(skill);
                                }
                            }
                            
                            //controlliamo se è utilizzata, se lo è non può essere rimossa
                            Map<Developer, Integer> devs = datalayer.getDevelopersBySkill(deadSkill.getKey());
                            if(devs.size()==0){
                                List<Task> tasks = datalayer.getTasksBySkill(deadSkill.getKey());
                                if(tasks.size()==0){
                                    datalayer.deleteSkill(deadSkill);
                                }
                            }
                            
                            datalayer.destroy();
                            s.removeAttribute("previous_url");
                            response.sendRedirect(u.split("/")[2]);
                        }else{
                        s.removeAttribute("previous_url");
                        response.sendRedirect("index");
                    }
                    }else{
                        s.removeAttribute("previous_url");
                        response.sendRedirect("index");
                    }
                }else{
                    s.removeAttribute("previous_url");
                    response.sendRedirect("index");
                }
            }
    
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            action_rmskillback(request,response);
        }
        catch (IOException ex) {
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
            action_error(request, response);
        }
        }
    }


