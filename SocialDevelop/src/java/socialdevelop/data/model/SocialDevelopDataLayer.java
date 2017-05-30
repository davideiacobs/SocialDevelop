/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.File;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Part;

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
    
    Files createFiles();
    
    Project getProject(int project_key) throws DataLayerException;
    
    List<Project> getProjects() throws DataLayerException;
    
    List<Project> getProjects(String filtro) throws DataLayerException;
    
    Skill getSkill(int skill_key) throws DataLayerException;
    
    Task getTask(int task_key) throws DataLayerException;
    
    Map<Skill, Integer> getSkillsByTask(int task_key) throws DataLayerException;
    
    Map<Skill, Integer> getSkillsByDeveloper(int developer_key) throws DataLayerException;
    
    Map<Task, Integer> getTasksByDeveloper(int developer_key) throws DataLayerException;
        
    Map<Developer,Integer> getCollaboratorsByTask(int task_key) throws DataLayerException;
    
    int getNumberTaskByProjectID(int project_key) throws DataLayerException;
            
    int getVote(int task_key, int developer_key) throws DataLayerException;
    
    Type getType(int type_key) throws DataLayerException;
    
    Type getTypeByTask(int task_key) throws DataLayerException;
    
    Developer getDeveloper(int developer_key) throws DataLayerException;
        
    Map<Developer, Integer> getDevelopersBySkill(int skill_key) throws DataLayerException;
    
    Map<Developer, Integer> getDevelopersBySkill(int skill_key, int level) throws DataLayerException;
        
    List<Task> getTasks(int project_key) throws DataLayerException;
    
    //Message getMessage(int message_key);
    //recupera oggetto message
    Message getMessage(int message_key) throws DataLayerException;
    
    List<Message> getMessages(int project_key) throws DataLayerException;
    
    List<Message> getPublicMessages(int project_key) throws DataLayerException;
    //se il developer appartiene al progetto chiama la getMessage di Project che restituisce tutti
    //i messaggi, altrimenti chiama quella che restituisce i soli messaggi pubblici
    
    List<Skill> getChild(int skill_key) throws DataLayerException;
    
    Skill getParent(int skill_key)throws DataLayerException;
 
    List<Developer> getProjectCollaborators(int project_key) throws DataLayerException;
    //utilizza getTasks
    
    List<Project> getDeveloperProjects (int developer_key) throws DataLayerException;
    
    List<Project> getDeveloperProjects (int developer_key, GregorianCalendar data) throws DataLayerException;
    
    CollaborationRequest createCollaborationRequest();
    
    List<CollaborationRequest> getInvitesByCoordinator(int coordinator_key) throws DataLayerException;

    List<CollaborationRequest> getProposalsByCollaborator(int collaborator_key) throws DataLayerException;
    
    List<CollaborationRequest> getOffertsByDeveloper(int developer_key) throws DataLayerException;
    
    //richiede la lista delle skills del developer e cerca i task aperti che richiedono
    //tali skills
    
    Task getTaskByRequest(int collaborator_key, int coordinator_key) throws DataLayerException;
        
    List<CollaborationRequest> getQuestionsByCoordinator(int coordinator_key) throws DataLayerException;

    Developer getCoordinatorByTask(int task_key) throws DataLayerException;
    
    CollaborationRequest getCollaborationRequest(int collaborator_key, int task_key ) throws DataLayerException;

    void storeProject(Project project) throws DataLayerException;
    
    void deleteProject(Project project) throws DataLayerException;
    
    void storeDeveloper(Developer developer) throws DataLayerException;
    
    void deleteDeveloper(Developer developer) throws DataLayerException;
    
    void storeTask(Task task) throws DataLayerException;
    
    void deleteTask(Task task) throws DataLayerException;
    
    void storeSkill(Skill skill) throws DataLayerException;
    
    void deleteSkill(Skill skill) throws DataLayerException;
    
    void storeMessage(Message message) throws DataLayerException;
    
    void deleteMessage(Message message) throws DataLayerException;
    
    void storeType(Type type) throws DataLayerException;
    
    void deleteType(Type type) throws  DataLayerException;
    
    void storeTaskHasDeveloper(int task_ID, int developer_ID, int state, GregorianCalendar date, int vote, int sender) throws DataLayerException;
    
    void deleteRequest(CollaborationRequest request) throws DataLayerException;
    
    void storeTaskHasSkill(int task_ID, int skill_ID, int type_ID, int level_min) throws DataLayerException;
        
    void storeSkillHasDeveloper(int skill_ID, int developer_ID,int level) throws DataLayerException;
    
    void deleteTaskHasSkill(int task_ID, int skill_ID, int type_ID) throws DataLayerException;
        
    void deleteSkillHasDeveloper(int skill_ID, int developer_ID) throws DataLayerException;
    
    void deleteTaskHasDeveloper(int skill_ID, int developer_ID) throws DataLayerException;
    
    int getDeveloperByUsername(String username) throws DataLayerException;

    int getDeveloperByMail(String mail) throws DataLayerException;
    
    int storeFile(Part file_to_upload, File uploaded_file, String sdigest) throws DataLayerException;
        
    Files getFile(int key) throws DataLayerException;
    
    Project getProjectByTask(int task_key) throws DataLayerException;
}
