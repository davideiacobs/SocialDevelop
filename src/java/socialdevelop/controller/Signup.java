/*
 * Signup.java
 *
 * Questo esempio mostra come utilizzare le sessioni per autenticare un utente
 * 
 * This example shows how to use sessions to authenticate the user
 *
 */
package socialdevelop.controller;


import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import socialdevelop.data.impl.DeveloperImpl;
import socialdevelop.data.impl.SocialDevelopDataLayerMysqlImpl;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Ingegneria del Web
 * @version
 */
public class Signup extends SocialDevelopBaseController {
    
      @Resource(name = "jdbc/mydb")
    private DataSource ds;
   
     private void action_registrati(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException, SQLException, NamingException {
            
            String name = request.getParameter("first_name");
            String surname = request.getParameter("second_name");
            String username = request.getParameter("username");
            String mail = request.getParameter("mail");
            String pwd = request.getParameter("pwd");
            String pwd2 = request.getParameter("pwd2");
            String bday = request.getParameter("birthdate");
            //SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer)request.getAttribute("datalayer");
            //socialdevelopdatalayer è null
            SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
            datalayer.init();
            DeveloperImpl dev = new DeveloperImpl(datalayer);
            if(pwd.equals(pwd2)){
                dev.setName(name);
                dev.setSurname(surname);
                dev.setUsername(username);
                dev.setMail(mail);
                dev.setPwd(pwd);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setLenient(false);
                gc.set(GregorianCalendar.YEAR, Integer.valueOf(bday.split("/")[2]));
                gc.set(GregorianCalendar.MONTH, Integer.valueOf(bday.split("/")[1])-1); //parte da 0
                gc.set(GregorianCalendar.DATE, Integer.valueOf(bday.split("/")[0]));
                dev.setBirthDate(gc);
                datalayer.storeDeveloper(dev);
                request.setAttribute("username", username);
                request.setAttribute("page_title", username+", ");
                request.setAttribute("page_subtitle", "Welcome in SocialDevelop!");
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("completa_registrazione.html",request, response);  
            }else{
                //errore e torna sulla stessa pagina
            }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_registrati(request, response);
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
