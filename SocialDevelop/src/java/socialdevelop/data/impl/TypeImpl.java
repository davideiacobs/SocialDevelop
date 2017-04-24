/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Type;

/**
 *
 * @author david
 */
public class TypeImpl implements Type{
    
    private int key;
    private String type;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public TypeImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        type = "";
        dirty = false;
    } 

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
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
