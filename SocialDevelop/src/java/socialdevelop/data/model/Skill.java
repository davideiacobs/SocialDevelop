/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
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
    
    List<Skill> getChild() throws DataLayerException;
    
    int getParentKey()throws DataLayerException;
    
    void setParentKey(int skill_id);
    
    Skill getParent() throws DataLayerException;
    
    void setParent(Skill parent) throws DataLayerException; 
    
    void setChild(List<Skill> skills);
    
    void addChild(Skill child);
    
    void setDirty(boolean dirty);

    boolean isDirty();
    
    void setType_key(int type_key);
    
    int getType_key();
    
   Type getType() throws DataLayerException;
   
   void setType(Type t);
    
   void copyFrom(Skill skill) throws DataLayerException;

}
