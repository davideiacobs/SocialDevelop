/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class DeveloperProfile extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    private void getImg(HttpServletRequest request, HttpServletResponse response, Developer dev) throws IOException, SQLException, DataLayerException, NamingException {
        StreamResult result = new StreamResult(getServletContext());
        
         SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
         if(dev.getFoto() != 0){
            Files foto_profilo = datalayer.getFile(dev.getFoto());
            request.setAttribute("foto_profilo", "extra-images/" + foto_profilo.getLocalFile());
         }else{
            request.setAttribute("foto_profilo", "extra-images/foto_profilo_default.png");             
         }
        
    }
    
    
    
    private void action_devprofile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            //recuperare developer
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer)request.getAttribute("datalayer");
            int dev_key = Integer.parseInt(request.getParameter("n"));
            Developer dev = datalayer.getDeveloper(dev_key);
            if(dev!=null){
                request.setAttribute("id", dev_key);
                request.setAttribute("username", dev.getUsername());
                request.setAttribute("fullname", dev.getName()+" "+dev.getSurname());
                long currentTime = System.currentTimeMillis();
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(currentTime);
                request.setAttribute("age", now.get(Calendar.YEAR) - dev.getBirthDate().get(Calendar.YEAR));
                request.setAttribute("bio", dev.getBiography());
                request.setAttribute("mail", dev.getMail());
                request.setAttribute("curriculum", dev.getCurriculumString());
                request.setAttribute("curriculum_pdf", dev.getCurriculumFile());
                //request.setAttribute("datalayer", datalayer);
                getImg(request, response, dev);
                datalayer.destroy();

                request.setAttribute("page_title", "Developer");
                request.setAttribute("page_subtitle", dev.getUsername());
                
                HttpSession s = request.getSession(true);
                if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {  
                    request.setAttribute("logout", "Logout");
                }   
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("developer_profile.html",request, response);  //al posto di ciao va inserito il nome dell'html da attivare
            }else{
                response.sendRedirect("index");
            }
           
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_devprofile(request, response);
        } catch (IOException ex) {
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}