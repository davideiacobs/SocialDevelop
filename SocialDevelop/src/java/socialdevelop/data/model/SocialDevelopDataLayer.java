/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import socialdevelop.data.impl.CollaborationRequestImpl;

/**
 *
 * @author david
 */
public interface SocialDevelopDataLayer extends DataLayer {
    
    Project createProject();

    Skill createSkill();

    Developer createDeveloper();
    
    Task createTask();
        
    Message createMessage();
    
    Type createType();
    
    Project getProject(int project_key) throws DataLayerException;
    
    List<Project> getProjects() throws DataLayerException;
    
    List<Project> getProjects(String filtro) throws DataLayerException;
    
    Skill getSkill(int skill_key) throws DataLayerException;
    
    Task getTask(int task_key) throws DataLayerException;
    
    Map<Skill, Integer> getSkillsByTask(int task_key) throws DataLayerException;
    
    Map<Skill, Integer> getSkillsByDeveloper(int developer_key) throws DataLayerException;
    
    Map<Task, Integer> getTasksByDeveloper(int developer_key) throws DataLayerException;
        
    Map<Developer,Integer> getCollaboratorsByTask(int task_key) throws DataLayerException;
    
    int getVote(int task_key, int developer_key) throws DataLayerException;
    
    Type getTypeByTask(int task_key) throws DataLayerException;
    
    Developer getDeveloper(int developer_key) throws DataLayerException;
        
    Map<Developer, Integer> getDevelopersBySkill(int skill_key) throws DataLayerException;
    
    Map<Developer, Integer> getDevelopersBySkill(int skill_key, int level) throws DataLayerException;
        
    List<Task> getTasks(int project_key) throws DataLayerException;
    
    Task getTaskByRequest(CollaborationRequest request) throws DataLayerException;
    
    //Message getMessage(int message_key);
    //recupera oggetto message
    Message getMessage(int message_key) throws DataLayerException;
    
    List<Message> getMessages(int project_key) throws DataLayerException;
    
    Message getPrivateMessages(int project_key, int developer_key); 
    //se il developer appartiene al progetto chiama la getMessage di Project che restituisce tutti
    //i messaggi, altrimenti chiama quella che restituisce i soli messaggi pubblici
    
    List<Skill> getChild(Skill skill) throws DataLayerException;
    
    int getParent(Skill skill)throws DataLayerException;
 
    List<Developer> getProjectCollaborators(int project_key) throws DataLayerException;
    //utilizza getTasks
    
    List<Project> getDeveloperProjects (int developer_key) throws DataLayerException;
    
    List<Project> getDeveloperProjects (int developer_key, GregorianCalendar data) throws DataLayerException;
    
    CollaborationRequest createCollaborationRequest();
    
    List<CollaborationRequest> getInvites(int coordinator_key);

    List<CollaborationRequest> getRequests(int collaborator_key);
    
    List<Task> getOfferts(int developer_key); 
    //richiede la lista delle skills del developer e cerca i task aperti che richiedono
    //tali skills

    Developer getCollaboratorRequest(CollaborationRequestImpl request) throws DataLayerException;
    
    Developer getCoordinatorRequest(Task task) throws DataLayerException;

}
