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
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.impl.TypeImpl;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Type;

/**
 *
 * @author Andrea
 */

public class addType extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    
    private void action_addType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {    
                
                           
                HttpSession s = request.getSession(true);
                String u = (String) s.getAttribute("previous_url");
                if(s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                    if(s.getAttribute("previous_url") != null && ((String) s.getAttribute("previous_url")).equals("/socialdevelop/BackEndType")){
            
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                
                
                Type type = new TypeImpl(datalayer);
                type.setType(request.getParameter("type-name"));
                datalayer.storeType(type);
                
                
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
              
    
    }
    
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            action_addType(request,response);
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


