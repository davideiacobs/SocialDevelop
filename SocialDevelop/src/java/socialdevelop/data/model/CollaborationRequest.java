/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import java.util.GregorianCalendar;

/**
 *
 * @author david
 */
public interface CollaborationRequest {
    
    int getKey();
    
    Developer getCoordinator();
    
    void setCoordinator(Developer coordinator);
    
    Developer getCollaborator();
    
    void setCollaborator(Developer collaborator);
    
    GregorianCalendar getDate();
    
    void setDate(GregorianCalendar date);
    
    int getState();
    
    void setState(int state);
    
    Task getTask();
    
    void setTask(Task task);
}
