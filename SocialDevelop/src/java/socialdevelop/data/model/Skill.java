/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author david
 */

public interface Skill {
    
    int getKey();
    
    void setName(String name);
    
    String getName();
    
    List<Skill> getChild();
    
    Skill getParent();
    
    void setParent(Skill parent);
    
    void setChild(List<Skill> skills);
    
    void addChild(Skill child);
    
    List<Developer> getDevelopers();
    
    
    Map<Developer, Integer> getDevelopersByLevel(int level);
    

    
}
