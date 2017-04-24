/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import java.util.List;

/**
 *
 * @author iacobs
 */
public interface Message {
    
    int getKey();
    
    void setText(String msg);
    
    String getText();
    
    void setPrivate(boolean flag);
    
    boolean isPrivate();
    
    void setType(String type); //type in {annuncio,discussione,richiesta}
    
    String getType();
    
    void setDirty(boolean dirty);

    boolean isDirty();
    
}
