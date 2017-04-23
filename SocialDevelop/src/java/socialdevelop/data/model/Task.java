/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import java.time.Period;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author david
 */
public interface Task {
    
    int getKey();
    
    void setNumCollaborator(int num); //tramite questa si può anche modificare il numCollaboratori
    
    int getNumCollaborator();
    
    boolean isOpen();
    
    void setOpen();
    
    void setDescription(String description);
    
    String getDescription();
    
    Period getTimeInterval();
    
    void setTimeInterval(Period interval);
    
    void setType(Type type);
    
    Type getType();
    
    Map<Skill, Integer> getSkills();
    
    void setSkills(Map<Skill, Integer> skill_levelMin);
    
    void addSkill(Skill skill, int levelMin);
    
    List<Developer> getCollaborators();
        
    void setCollaborators(List<Developer> listDev);
    
    void addCollaborator(Developer collaborator);
    
    void removeCollaborator(Developer collaborator);
        
    int getVote(Developer collaborator);
    
    void setVote(Developer collaborator, int vote);
    
    
}
