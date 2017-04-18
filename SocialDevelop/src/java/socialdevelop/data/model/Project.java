/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

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
    
    Developer getCoordinator();
    
    void setCoordinator(Developer coordinator);
    
    List<Task> getTask();
    
    void setTask(List<Task> tasks);
    
    void addTask(Task task);
    
    List<Message> getMessages();
    
    void addMessage(Message message);
    
    boolean isFull();
    
}