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
        
    void setCoordinator(Developer coordinator);
    
    Developer getCoordinator() throws DataLayerException;
    
    List<Task> getTasks();
    
    void setTasks(List<Task> tasks);
    
    void addTask(Task task);
    
    void removeTask(Task task);
    
    List<Message> getMessages();
    
    List<Message> getPublicMessages();
        
    void addMessage(Message message);
    
    void removeMessage(Message message);
    
    boolean isFull();
    
}