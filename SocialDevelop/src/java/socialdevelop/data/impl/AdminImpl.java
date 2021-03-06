/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import socialdevelop.data.model.Admin;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class AdminImpl implements Admin{
    
    private int key;
    private int developerkey;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public AdminImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        developerkey = 0;
        dirty = false;
    } 

    @Override
    public int getKey() {
        return key;
    }

    
    @Override
    public void setDeveloperKey(int developerkey) {
        this.developerkey = developerkey;
    }

    @Override
    public int getDevelperKey() {
        return developerkey;
    }

    @Override
    public void copyFrom(Admin admin) throws DataLayerException {
        this.key = admin.getKey();
        this.developerkey = admin.getDevelperKey();
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

    @Override
    public void setKey(int key) {
        this.key = key;
    }
}
