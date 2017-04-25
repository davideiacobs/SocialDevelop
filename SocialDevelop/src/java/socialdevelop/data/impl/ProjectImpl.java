/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.Task;
import java.util.List;
import socialdevelop.data.model.Message;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class ProjectImpl implements Project {
    
    private int key;
    private Developer coordinator;
    private int coordinator_key;
    private String name;
    private String description;
    private List<Task> tasks;
    private List<Message> messages;
    
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public ProjectImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        coordinator = null;
        coordinator_key = 0;
        name = "";
        description = "";
        tasks = null;
        messages = null;
        dirty = false;
    }
    
    @Override
    public int getKey() {
        return key;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public void setDescription(String description){
        this.description = description;
        this.dirty = true;
    }
    
    @Override 
    public String getDescription(){
        return description;
    }
    
    @Override
    public void setCoordinator(int coordinator_key){
        this.coordinator = ownerdatalayer.getDeveloper(coordinator_key);
        this.coordinator_key = coordinator_key;
        this.dirty = true;
    }
    
    @Override
    public Developer getCoordinator() throws DataLayerException{
        //notare come il coordinatore in relazione venga caricato solo su richiesta
        if (coordinator == null && coordinator_key > 0) {
            coordinator = ownerdatalayer.getDeveloper(coordinator_key);
        }
        //attenzione: il coordinatore caricato viene lagato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se il coordinatore viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
       
        return coordinator;
    }
    
    @Override
    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
        this.dirty = true;
    }
    
    @Override
    public List<Task> getTasks() throws DataLayerException {
        if(tasks == null){
           tasks = ownerdatalayer.getTasks(this);
        }
        return tasks;
    }
    
    @Override
    public void addTask(Task task){
        this.tasks.add(task);
        this.dirty = true;
    }
    
    @Override
    public void removeTask(Task task){
        this.tasks.remove(task);
        this.dirty = true;
    }
        
    @Override
    public List<Message> getMessages() throws DataLayerException {
        if(messages == null){
           messages = ownerdatalayer.getMessages(this);
        }
        return messages;
    }
    
    @Override
    public void addMessage(Message message){
        this.messages.add(message);
        this.dirty = true;
    }
    
    @Override
    public void removeMessage(Message message){
        this.messages.remove(message);
        this.dirty = true;
    }
    
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
    
    protected void setKey(int key) {
        this.key = key;
    }
}
