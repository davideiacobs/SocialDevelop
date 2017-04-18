/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author david
 */
public interface Task {
    
    int getKey();
    
    void setNumCollaborator(int num);
    
    int getNumCollaborator();
    
    boolean isOpen();
    
    void setOpen();
    
    void setDescription(String description);
    
    String getDescription();
    
    Period getTimeInterval();
    
    void setTimeInterval(Period interval);
    
    void setType(Type type);
    
    Type getType();
    
    Map<Skill, Integer> getSkill();
    
    void setSkill(Map<Skill, Integer> skill_level);
    
    void addSkill(Skill skill, int level);
    
    List<Developer> getCollaborators();
    
    Developer getCollaborator(int key);
    
    void setCollaborator(List<Developer>);
    
    void addCollaborator(Developer collaborator);
    
    void removeCollaborator(Developer collaborator);
    
    void setState(Developer collaborator);
    
    int getState(Developer collaborator);
    
    Date getDataReq(Developer collaborator);
    
    void setDataReq(Developer collaborator, Date dataReq);
    
    int getVote(Developer collaborator);
    
    void setVote(Developer collaborator, int vote);
    
    
}
