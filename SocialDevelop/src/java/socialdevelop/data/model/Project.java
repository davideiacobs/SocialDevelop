/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.List;


/**
 *
 * @author david
 */

public interface Project  {
    
    int getKey();
    
    void setName(String name);
    
    String getName();
        
    String getDescription();
    
    void setDescription(String description);
        
    void setCoordinator(int coordinator_key);
    
    Developer getCoordinator() throws DataLayerException;
    
    List<Task> getTasks() throws DataLayerException;
    
    void setTasks(List<Task> tasks);
    
    void addTask(Task task);
    
    void removeTask(Task task);
    
    List<Message> getMessages() throws DataLayerException;
    
    //List<Message> getPublicMessages() throws DataLayerException;
        
    void addMessage(Message message);
    
    void removeMessage(Message message);
        
    void setDirty(boolean dirty);

    boolean isDirty();
}