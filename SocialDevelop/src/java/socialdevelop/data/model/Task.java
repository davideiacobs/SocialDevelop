/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.time.Period;
import java.util.List;
import java.util.Map;

/**
 *
 * @author david
 */
public interface Task {
    
    int getKey();
    
    void setNumCollaborators(int num); //tramite questa si può anche modificare il numCollaboratori
    
    int getNumCollaborators();
    
    boolean isOpen();
    
    void setOpen(boolean isOpen);
    
    void setDescription(String description);
    
    String getDescription();
    
    Period getTimeInterval();
    
    void setTimeInterval(Period interval);
    
    void setType(Type type);
    
    Type getType() throws DataLayerException;
    
    Map<Skill, Integer> getSkills() throws DataLayerException;
    
    void setSkills(Map<Skill, Integer> skill_levelMin);
    
    void addSkill(Skill skill, int levelMin);
    
    void removeSkill(Skill skill);
    
    Map<Developer,Integer> getCollaborators() throws DataLayerException;
        
    void setCollaborators(List<Developer> listDev); 
    //al momento del setCollaborators Integer è null
    
    void addCollaborator(Developer collaborator);
    //voto all'inizio è null
    
    void removeCollaborator(Developer collaborator);
        
    int getVote(Developer collaborator) throws DataLayerException;
    
    void setVote(Developer collaborator, int vote);
    
    boolean isFull();

    void setDirty(boolean dirty);

    boolean isDirty();
    
}
