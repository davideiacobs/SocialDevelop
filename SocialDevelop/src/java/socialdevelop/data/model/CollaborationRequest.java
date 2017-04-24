/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;

/**
 *
 * @author david
 */
public interface CollaborationRequest {
    
    int getKey();
    
    Developer getCoordinatorRequest() throws DataLayerException;
    
    void setCoordinatorRequest(Developer coordinator);
    
    Developer getCollaboratorRequest() throws DataLayerException;
    
    void setCollaboratorRequest(Developer collaborator);
    
    GregorianCalendar getDate();
    
    void setDate(GregorianCalendar date);
    
    int getState();
    
    void setState(int state);
    
    Task getTaskRequest() throws DataLayerException;
    
    void setTaskRequest(Task task);
    
    void setDirty(boolean dirty);

    boolean isDirty();
}
