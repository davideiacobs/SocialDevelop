/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import socialdevelop.data.model.Message;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class MessageImpl implements Message{
    
    private int key;
    private String text;
    private boolean isPrivate;
    private String type;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public MessageImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        text = "";
        isPrivate = false;
        type = "";
        dirty = false;
    }

    
    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        this.dirty = true;
    }

    @Override
    public boolean isPrivate() {
        return isPrivate;
    }

    @Override
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.dirty = true;
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
