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
import java.sql.SQLException;
import java.util.List;
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
 * @author Andrea
 */
public class BackEndSkill extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    
    private void action_backends(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {    
                
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");           
                HttpSession s = request.getSession(true);
                if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0){
                   
                request.setAttribute("page_title", "SKILL BACKEND");
                request.setAttribute("page_subtitle", "Manage the Skills");
                
                List <Skill> skills = datalayer.getSkills();
                request.setAttribute("skills",skills);
                
                List <Type> types = datalayer.getTypes();
                request.setAttribute("types", types);
                request.setAttribute("logout", "Logout");
            
               
                
                datalayer.destroy();
                String act_url = request.getRequestURI();
                s.setAttribute("previous_url", act_url);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("backend_skills.html",request, response);  
    
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
            action_backends(request,response);
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


