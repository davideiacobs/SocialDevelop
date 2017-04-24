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
    
    Project getProject(int project_key) throws DataLayerException;
    
    List<Project> getProjects() throws DataLayerException;
    
    List<Project> getProjects(String filtro);
    
    Skill getSkill(int skill_key);
    
    Map<Skill, Integer> getSkillsByTask(Task task) throws DataLayerException;
    
    Map<Skill, Integer> getSkillsByDeveloper(Developer developer) throws DataLayerException;
    
    Map<Task, Integer> getTasksByDeveloper(Developer developer) throws DataLayerException;
    
    Developer getCoordinatorRequest(Task task) throws DataLayerException;
    
    Map<Developer,Integer> getCollaborators(Task task) throws DataLayerException;
    
    int getVote(Task task, Developer developer) throws DataLayerException;
    
    Type getType(Task task) throws DataLayerException;
    
    Developer getDeveloper(int developer_key);
    
    //List<Developer> getDevelopersBySkill(Skill filtro);
    
    Map<Developer, Integer> getDevelopersBySkill(Skill filtro);
    
    Map<Developer, Integer> getDevelopersBySkill(Skill filtro, int level);
    
    //Task getTask(int task_key);
    
    List<Task> getTasks(Project project) throws DataLayerException;
    
    Task getTaskRequest(CollaborationRequest request) throws DataLayerException;
    
    //Message getMessage(int message_key);
    //recupera oggetto message
    List<Message> getMessages(Project project) throws DataLayerException;
    
    Message getPublicMessages(int project_key, int developer_key); 
    //se il developer appartiene al progetto chiama la getMessage di Project che restituisce tutti
    //i messaggi, altrimenti chiama quella che restituisce i soli messaggi pubblici
    
    List<Skill> getChild(Skill skill) throws DataLayerException;
    
    Skill getParent(Skill skill)throws DataLayerException;
 
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
    
}
