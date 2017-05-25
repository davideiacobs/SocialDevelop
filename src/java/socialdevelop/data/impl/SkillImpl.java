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
    private int parent_id; //nel caso in cui non ha padre, come parent_id avrà il suo stesso id
    private Skill parent;
    private List<Skill> child; //serve?? PESANTE!
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public SkillImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        parent_id = 0;
        parent = null;
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
    public int getParentKey() throws DataLayerException{
        if(parent_id == 0){
            parent_id = ownerdatalayer.getParent(this.key).getKey();
        }
        return parent_id;
    }
    
    @Override
    public void setParentKey(int skill_id){
        this.parent_id = skill_id;
        this.dirty = true;
    }
    
    
    @Override
    public void setParent(Skill parent) {
        this.parent = parent;
        this.parent_id = parent.getKey();
        this.dirty = true;
    }
    
    @Override
    public Skill getParent() throws DataLayerException{
        //notare come il coordinatore in relazione venga caricato solo su richiesta
        if (parent == null && parent_id > 0) {
            parent = ownerdatalayer.getSkill(parent_id);
        }
        //attenzione: il coordinatore caricato viene lagato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se il coordinatore viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
       
        return parent;
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
    
    @Override
    public void copyFrom(Skill skill) throws DataLayerException {
        key = skill.getKey();
        parent_id = skill.getParentKey();
        name = skill.getName();
        this.dirty = true;
    }
}   