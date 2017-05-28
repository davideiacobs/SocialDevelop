/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.Part;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class CompletaRegistrazione extends SocialDevelopBaseController {
    
     @Resource(name = "jdbc/mydb")
    private DataSource ds;
     
     private void completa_reg(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {

        int fileID = 0;
        String bio = request.getParameter("biography");
        Part file_to_upload = request.getPart("foto-profilo");

        //vogliamo creare il digest sha-1 del file
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //creiamo un nuovo file (con nome univoco) e copiamoci il file scaricato
        File uploaded_file = File.createTempFile("upload_", "", new File(getServletContext().getInitParameter("extra-images.directory")));
        InputStream is = file_to_upload.getInputStream(); 
             OutputStream os = new FileOutputStream(uploaded_file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                //durante la copia, aggreghiamo i byte del file nel digest sha-1
                //while copying, we aggregate the file bytes in the sha-1 digest
                md.update(buffer, 0, read);
                os.write(buffer, 0, read);
            }
        

        //get the file digest as a string
        //covertiamo il digest in una stringa
        byte[] digest = md.digest();
        String sdigest = "";
        for (byte b : digest) {
            sdigest += String.valueOf(b);
        }
        
        SocialDevelopDataLayer datalayer = new SocialDevelopDataLayerMysqlImpl(ds);
        datalayer.init();
        datalayer.storeImg(file_to_upload, uploaded_file, sdigest);
        
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            completa_reg(request, response);
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
          } catch (Exception ex) { 
             Logger.getLogger(CompletaRegistrazione.class.getName()).log(Level.SEVERE, null, ex);
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
