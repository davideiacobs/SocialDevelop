/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.time.Period;
import java.util.HashMap;
import socialdevelop.data.model.Task;
import socialdevelop.data.model.Type;
import socialdevelop.data.model.Skill;
import java.util.Map;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class TaskImpl implements Task{
    
    private int key;
    private String name;
    private Period timeInterval;
    private boolean open;
    private int numCollaborators;
    private String description;
    private Type type;
    private Map<Skill, Integer> skills;
    private Map<Developer, Integer> collaborators;
   
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public TaskImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        timeInterval = null;
        open = true;
        numCollaborators = 0;
        description = "";
        type = null;
        skills = null;
        collaborators = null;
        dirty = false;
    }
      
    @Override
    public int getKey(){
        return key;
    }
    
    @Override
    public void setNumCollaborators(int num){
        this.numCollaborators = num;
        this.dirty = true;
    }
    
    @Override
    public int getNumCollaborators(){
        return numCollaborators;
    }
    
    @Override 
    public void setOpen(boolean isOpen){
        this.open = isOpen;
        this.dirty = true;
    }
    
    @Override 
    public boolean isOpen(){
        return open;
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
    public Period getTimeInterval(){
        return timeInterval;
    }
    
    @Override
    public void setTimeInterval(Period timeInterval){
        this.timeInterval = timeInterval;
        this.dirty = true;
    }
    
    @Override
    public Type getType() throws DataLayerException{
        if(type==null){
            type = ownerdatalayer.getType(this);
        }
        return type;
    }
    
    @Override
    public void setType(Type type){
        this.type = type;
        this.dirty = true;
    }
    
    @Override
    public Map<Skill, Integer> getSkillsByTask() throws DataLayerException{
        if(skills == null){
            skills = ownerdatalayer.getSkillsByTask(this);
        }
        return skills;
    }
    
    @Override
    public void setSkills(Map<Skill, Integer> skill_levelMin){
        this.skills = skill_levelMin;
        this.dirty = true;
    }
    
    @Override
    public void addSkill(Skill skill, int levelMin){
        this.skills.put(skill,levelMin);
        this.dirty = true;
    }
    
    @Override
    public void removeSkill(Skill skill){
        this.skills.remove(skill);
        this.dirty = true;
    }
    
    @Override
    public Map<Developer,Integer> getCollaborators()throws DataLayerException{
        if(collaborators == null){
            collaborators = ownerdatalayer.getCollaborators(this);
        }
        return collaborators;
    }
    
    @Override
    public void setCollaborators(List<Developer> developers){
        Map<Developer,Integer> coll = new HashMap<>();
        for (Developer developer : developers){
            coll.put(developer, null);
        }
        this.collaborators = coll;
        this.dirty = true;
    }
    
    @Override
    public void addCollaborator(Developer collaborator){
        this.collaborators.put(collaborator, null);
        this.dirty = true;
    }
    
    @Override
    public void removeCollaborator(Developer developer){
        this.collaborators.remove(developer);
        this.dirty = true;
    }
    
    @Override
    public int getVote(Developer developer) throws DataLayerException {
        if(collaborators.get(developer)==null){
            collaborators.put(developer, ownerdatalayer.getVote(this, developer));
        }
        return collaborators.get(developer);
    }
    
    @Override
    public void setVote(Developer developer,int vote){
        this.collaborators.put(developer, vote);
        this.dirty = true;
    }
    
    @Override
    public boolean isFull(){
        boolean isFull = false ;
        try {
            isFull = (numCollaborators == this.getCollaborators().size());
        } catch (DataLayerException ex) {
            Logger.getLogger(TaskImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isFull;   
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

    @Override
    public String getName() {
        return name;
    }

   @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }
}
