/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.List;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class SkillImpl implements Skill{
    
    private int key;
    private String name;
    private int parent_id;
    private List<Skill> child;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public SkillImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        parent_id = 0;
        child = null;
        dirty = false;
    }
    
    @Override
    public int getKey(){
        return key;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public void setName(String name){
        this.name = name;
        this.dirty = true;
    }
    
    @Override
    public List<Skill> getChild() throws DataLayerException{
        if(child == null){
            child = ownerdatalayer.getChild(this.key);
        }
        return child;
    }
    
    @Override
    public int getParent() throws DataLayerException{
        if(parent_id == 0){
            parent_id = ownerdatalayer.getParent(this.key).getKey();
        }
        return parent_id;
    }
    
    @Override
    public void setParent(int skill_id){
        this.parent_id = skill_id;
        this.dirty = true;
    }
    
    @Override
    public void setChild(List<Skill> child){
        this.child = child;
        this.dirty = true;
    }
    
    @Override
    public void addChild(Skill child){
        this.child.add(child);
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
