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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import socialdevelop.data.impl.MessageImpl;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author manuel
 */
public class PostDiscussion extends SocialDevelopBaseController {

     private void action_postmsg(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if(s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            if(s.getAttribute("previous_url") != null && ((String) s.getAttribute("previous_url")).equals("/socialdevelop/Project_Detail")){
            
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                int user_key = (int) s.getAttribute("userid");
                
                MessageImpl msg = new MessageImpl(datalayer);
                msg.setDeveloperKey(user_key);
                msg.setText(request.getParameter("discussion"));
                String p = request.getParameter("isPrivate-discussion");
                msg.setType("discussione");
                int project_key = Integer.parseInt(request.getParameter("project_key"));
                msg.setProjectKey(project_key);
                boolean isPrivate = true;
                if(p.equals("0")){
                    isPrivate = false;
                }
                msg.setPrivate(isPrivate);
                datalayer.storeMessage(msg);
                datalayer.destroy();
                s.removeAttribute("previous_url");
                response.sendRedirect(u.split("/")[2]+"?n="+project_key);
                
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_postmsg(request, response);
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
