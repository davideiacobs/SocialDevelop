/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import socialdevelop.data.model.Files;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author iacobs
 */
public class DownloadCurriculum extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
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
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");

        Files curriculum = datalayer.getFile(res);
        
        File in = new File(getServletContext().getRealPath("") + File.separatorChar + "curriculums" + File.separatorChar + curriculum.getLocalFile());
        result.activate(in, request, response);
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_download(request, response);
        } catch (IOException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (DataLayerException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (SQLException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (NamingException ex) {
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
