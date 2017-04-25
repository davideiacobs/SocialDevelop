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
    
    private int key; 
    private Developer coordinator;
    private Task task;
    private Developer collaborator;
    private GregorianCalendar date;
    private int state;
    
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    @Override
    public int getKey() {
        return key;
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
        this.dirty = true;
    }

    @Override
    public Task getTaskRequest() throws DataLayerException {
        if(task == null){
            task = ownerdatalayer.getTaskRequest(this);
        }
        return task;
    }

    @Override
    public void setTaskRequest(Task task) {
        this.task = task;
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
        this.dirty = true;
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
    
    protected void setKey(int key) {
        this.key = key;
    }
    
    
    
    
    
}
