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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import socialdevelop.data.impl.SocialDevelopDataLayerMysqlImpl;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author manuel
 */
public class List_project extends HttpServlet {
    
     @Resource(name = "jdbc/mydb")
    private DataSource ds;

     private void action_listproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            request.setAttribute("page_title", "Progetti disponibili");
            request.setAttribute("page_subtitle", "Progetti");
                SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
                datalayer.init();
                List<Project> pro = datalayer.getProjects();
                request.setAttribute("listaprogetti", pro);
                Files foto = null ;
                Developer coordinatore ;
                for(Project progetto : pro){
                    coordinatore=datalayer.getDeveloper(progetto.getCoordinatorKey());
                    List <Task> tasks = datalayer.getTasks(progetto.getKey());
                    int ncollaboratoritot = 0 ;
                    for(Task task : tasks){
                        ncollaboratoritot+=task.getNumCollaborators();
                    }
                    request.setAttribute("ntask", tasks.size());
                    request.setAttribute("collaboratoriTot", ncollaboratoritot);
                    progetto.setCoordinator(coordinatore);
                    int foto_key=coordinatore.getFoto();
                    if(foto_key != 0){
                        foto = datalayer.getFile(foto_key);
                        request.setAttribute("fotoCoordinatore", "extra-images/" + foto.getLocalFile());
                    }else{
                        request.setAttribute("fotoCoordinatore", "extra-images/foto_profilo_default.png");             
                    }
                    request.setAttribute("nomeCoordinatore", coordinatore.getName());
                    }
                
                 TemplateResult res = new TemplateResult(getServletContext());
                res.activate("project_list.html",request, response);  //al posto di ciao va inserito il nome dell'html da attivare 
    }
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            action_listproject(request, response);
        } catch (IOException ex) {
            Logger.getLogger(MakeLoginReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateManagerException ex) {
            Logger.getLogger(MakeLoginReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MyProfile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(MyProfile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLayerException ex) {
            Logger.getLogger(MyProfile.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
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
        processRequest(request, response);
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
