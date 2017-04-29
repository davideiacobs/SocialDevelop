/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;
import socialdevelop.data.model.CollaborationRequest;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author david
 */


public class CollaborationRequestImpl implements CollaborationRequest{
    
    private Developer coordinator;
    private int coordinator_key;
    private Task task;
    private int task_key;
    private Developer collaborator;
    private int collaborator_key;
    private GregorianCalendar date;
    private int state;
    private int sender; //0 se inviata da coordinator, 1 se inviata da collaborator
    
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public CollaborationRequestImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        coordinator = null;
        coordinator_key = 0;
        task = null;
        sender = 0;
        task_key = 0;
        collaborator = null;
        collaborator_key = 0;
        date = null;
        state = 0;
        dirty = false;
    }
    
    @Override
    public int getSender(){
        return sender;
    }
    
    @Override 
    public void setSender(int sender){
        if(sender > 1){
            this.sender = 1;
        }else{
            this.sender = 0;
        }
           
        this.dirty = true;
    }
    
    @Override
    public Developer getCoordinatorRequest() throws DataLayerException{
        if(coordinator == null){
            coordinator = ownerdatalayer.getCoordinatorRequest(this.task);
        }
        return coordinator;
    }

    @Override
    public void setCoordinatorRequest(Developer coordinator) {
        this.coordinator = coordinator;
        this.coordinator_key = coordinator.getKey();
        this.dirty = true;
    }

    @Override
    public void setCoordinatorKey(int coordinator_key){
        this.coordinator_key = coordinator_key;
        this.coordinator = null;
        this.dirty = true;
    }
    
    @Override
    public int getCoordinatorKey(){
        return coordinator_key;
    }
    
    @Override
    public Task getTaskByRequest() throws DataLayerException {
        if(task == null){
            task = ownerdatalayer.getTaskByRequest(this);
        }
        return task;
    }
    
    @Override
    public void setTaskKey(int task_key){
        this.task_key = task_key;
        this.task = null;
        this.dirty = true;
    }
    
    @Override
    public int getTaskKey(){
        return task_key;
    }

    @Override
    public void setTaskRequest(Task task) {
        this.task = task;
        this.task_key = task.getKey();
        this.dirty = true;
    }

    @Override
    public Developer getCollaboratorRequest() throws DataLayerException {
        if(collaborator == null){
            collaborator = ownerdatalayer.getCollaboratorRequest(this);
        }
        return collaborator;
    }

    @Override
    public void setCollaboratorRequest(Developer collaborator) {
        this.collaborator = collaborator;
        this.collaborator_key = collaborator.getKey();
        this.dirty = true;
    }
    
    
    @Override
    public void setCollaboratorKey(int collaborator_key){
        this.collaborator_key = collaborator_key;
        this.collaborator = null;
        this.dirty = true;
    }
    
    @Override
    public int getCollaboratorKey(){
        return collaborator_key;
    }

    @Override
    public GregorianCalendar getDate() {
        return date;
    }

    @Override
    public void setDate(GregorianCalendar data) {
        this.date = data;
        this.dirty = true;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
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
        
}
