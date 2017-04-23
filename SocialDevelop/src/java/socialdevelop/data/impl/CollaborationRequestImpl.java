/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import java.util.GregorianCalendar;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Task;

/**
 *
 * @author david
 */

//non va nel data model, ma serve


public class CollaborationRequestImpl {
    
    int key;
    
    Developer coordinator;
    
    Task task;
    
    Developer collaborator;
    
    GregorianCalendar data;
    
    int state;
    
    
    
}
