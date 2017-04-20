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
    
    void setDescription(String description); //va bene anche per la modifica
    
    Developer getCoordinator();
    
    void setCoordinator(Developer coordinator);
    
    List<Task> getTasks();
    
    void setTasks(List<Task> tasks);
    
    void addTask(Task task);
    
    void removeTask(Task task);
    
    List<Message> getMessages();
        
    void addMessage(Message message);
    
    void removeMessage(Message message);
    
    boolean isFull();
    
    List<Project> getProjects(String filtro);
    
    
}