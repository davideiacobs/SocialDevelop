/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import java.util.GregorianCalendar;

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
