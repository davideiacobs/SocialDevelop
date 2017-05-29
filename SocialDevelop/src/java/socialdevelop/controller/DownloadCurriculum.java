/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import socialdevelop.data.impl.SocialDevelopDataLayerMysqlImpl;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author iacobs
 */
public class DownloadCurriculum extends SocialDevelopBaseController {
    
    @Resource(name = "jdbc/mydb")
    private DataSource ds;
   
    
    private void action_download(HttpServletRequest request, HttpServletResponse response) throws IOException, DataLayerException, SQLException, NamingException {
        StreamResult result = new StreamResult(getServletContext());
        //in base a res, dovremmo determinare la risorsa da scaricare, quindi aprire uno stream di input
        //per leggerla in binario
        //...
        //con la classe StreamResult possiamo usare un file o direttamente uno stream, anche aperto da una base di dati
        //qui, per esempio, prendiamo sempre un file di test all'interno della nostra applicazione
        //we should determine the file to download using the res parameter, but in this toy example we always download the same file
        //the StreamResult utility class provides methods to start a binary download from a file or any data stream
        //
        //usate questa versione per leggere una risorsa incorporata nel WAR e se siete certi che sia stato espanso sul disco, o per una risorsa prelevata da una cartella esterna al contesto
        //use this version only if you want to read a resource embedded in the WAR file AND you know that it has been expanded to disk, or to read a resource from a folder outside the context
        int res = Integer.parseInt(request.getParameter("curriculum_pdf"));
        SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
        datalayer.init();
        Files curriculum = datalayer.getFile(res);
        
        File in = new File(getServletContext().getRealPath("") + File.separatorChar + "curriculums" + File.separatorChar + curriculum.getLocalFile());
        result.activate(in, request, response);
        //
        //usate questa versione per leggere una risorsa incorporata nel WAR
        //use this version to read a resource is inside the WAR
//        URL resource = getServletContext().getResource("/" + "file_di_esempio.txt");
//        if (resource != null) {
//            URLConnection connection = resource.openConnection();
//            String url = resource.toString();
//            //settiamo il tipo del file da trasmettere
//            //set the media type of the file to send
//            request.setAttribute("contentType", connection.getContentType());
//            result.activate(connection.getInputStream(), connection.getContentLength(), url.substring(url.lastIndexOf('/') + 1), request, response);
//        } else {
//            request.setAttribute("exception", new FileNotFoundException("Resource not found: "+res));
//            action_error(request, response);
//        }
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_download(request, response);
        } catch (IOException ex) {
            Logger.getLogger(MakeLoginReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLayerException ex) {
            Logger.getLogger(DownloadCurriculum.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DownloadCurriculum.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(DownloadCurriculum.class.getName()).log(Level.SEVERE, null, ex);
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
