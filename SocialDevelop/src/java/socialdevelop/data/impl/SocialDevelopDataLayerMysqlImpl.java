/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.data.DataLayerMysqlImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.sql.DataSource;
import socialdevelop.data.model.CollaborationRequest;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Message;
import socialdevelop.data.model.Project;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;
import socialdevelop.data.model.Type;

/**
 *
 * @author david
 */
public class SocialDevelopDataLayerMysqlImpl extends DataLayerMysqlImpl implements SocialDevelopDataLayer{
    
    private PreparedStatement sProjectByID, sTaskByID, sProjects, sDeveloperByID, sSkillByID;
    private PreparedStatement sProjectsByFilter, sSkillsByTask, sSkillsByDeveloper, sRequestByTask; 
    private PreparedStatement sCollaboratorsByTask, sVoteByTaskandDeveloper, sTypeByTask, sMessagesByProject;
    private PreparedStatement sDeveloperBySkillWithLevel, sDeveloperBySkill, sTasksByProject, sTaskByRequest ;
    private PreparedStatement sParentBySkill, sMessageByID, sCollaboratorsByProjectID, sProjectsByDeveloperID;
    private PreparedStatement sProjectsByDeveloperIDandDate, sInvitesByCoordinatorID, sRequestByCollaboratorID;
    private PreparedStatement sOffertsByDeveloperID, sDeveloperByRequest, sTasksByDeveloper;
    private PreparedStatement iProject, uProject, dProject;
    private PreparedStatement iSkill, uSkill, dSkill;
    private PreparedStatement iTask, uTask, dTask;
    private PreparedStatement iMessage, uMessage, dMessage;
    private PreparedStatement iType, uType, dType;
    private PreparedStatement iRequest, uRequest, dRequest;
    
    public SocialDevelopDataLayerMysqlImpl(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }
    
    
    @Override
    public void init() throws DataLayerException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate
            sProjectByID = connection.prepareStatement("SELECT * FROM project WHERE ID=?");
            sTaskByID = connection.prepareStatement("SELECT * FROM task WHERE ID=?");
            sDeveloperByID = connection.prepareStatement("SELECT * FROM developer WHERE ID=?");
            sMessagesByProject = connection.prepareStatement("SELECT ID FROM messages WHERE project_ID=?");
            sProjects = connection.prepareStatement("SELECT ID FROM project");
            sSkillByID = connection.prepareStatement("SELECT * FROM skill WHERE ID=?");
            sProjectsByFilter = connection.prepareStatement("SELECT ID FROM project WHERE "
                                                         + "(name LIKE ? or description LIKE ?)");
            sSkillsByTask = connection.prepareStatement("SELECT skill.ID,task_has_skill.level_min FROM skill INNER JOIN task_has_skill ON"
                                    + "(skill.ID = task_has_skill.skill_ID) WHERE task_has_skill.task_ID=?");
            sSkillsByDeveloper = connection.prepareStatement("SELECT skill_has_developer.skill_ID, skill_has_developer.level FROM developer INNER JOIN skill_has_developer"
                                        + "ON (developer.ID = skill_has_developer.developer_ID) WHERE skill_has_developer.developer_ID=?");
            sTasksByDeveloper = connection.prepareStatement("SELECT task_ID,vote FROM task_has_developer WHERE developer_ID=? AND state=1");
            sRequestByTask = connection.prepareStatement("SELECT * FROM task_has_developer WHERE task_ID=?");
            sCollaboratorsByTask = connection.prepareStatement("SELECT developer.* FROM developer INNER JOIN task_has_developer "
                                     + "ON (developer.ID = task_has_developer.ID)  WHERE task_has_developer.task_ID=?"
                                                            + "AND task_has_developer.state=1");
            sVoteByTaskandDeveloper = connection.prepareStatement("SELECT vote FROM task_has_developer WHERE task_ID=? "
                                        + "AND developer_ID=?");
            sTypeByTask = connection.prepareStatement("SELECT type_ID FROM task_has_skill WHERE task_ID=?");
            sDeveloperBySkillWithLevel = connection.prepareStatement("SELECT developer.*, skill_has_developer.level FROM developer INNER JOIN "
                                + "skill_has_developer ON (developer.ID = skill_has_developer.developer_ID)"
                                            + "WHERE skill_has_developer.skill_ID=? AND skill_has_developer.level=?");
            sDeveloperBySkill = connection.prepareStatement("SELECT developer.*, skill_has_developer.level FROM developer INNER JOIN skill_has_developer "
                                            + "ON(developer.ID = skill_has_developer.developer_ID) WHERE skill_has_developer.skill_ID=?");
            sTasksByProject = connection.prepareStatement("SELECT * FROM task WHERE project_ID=?");
            sTaskByRequest = connection.prepareStatement("SELECT task.* FROM task_has_developer INNER JOIN task ON"
                                            + "(task_has_developer.task_ID = task.ID) WHERE task_has_developer.task_ID=? AND"
                                             + "task_has_developer.developer_ID=?");
            sParentBySkill = connection.prepareStatement("SELECT parent_ID FROM skill WHERE ID=?");
            sCollaboratorsByProjectID = connection.prepareStatement("SELECT developer.* "
                                                         + "FROM developer INNER JOIN (task_has_developer INNER JOIN task ON "
                                                  + "(task_has_developer.task_ID = task.ID) WHERE task.project_ID=?) ON (developer.ID = task_has_developer.developer_ID)");
                                                
            //seleziona i progetti ai quali il developer ha partecipato
            sProjectsByDeveloperID = connection.prepareStatement("SELECT project.* FROM project INNER JOIN (task INNER JOIN task_has_developer"
                                                                + "ON (task.ID = task_has_developer.task_ID) "
                                                               + "WHERE task_has_developer.developer_ID=?) ON (project.ID = task.project_ID");
            sProjectsByDeveloperIDandDate = connection.prepareStatement("SELECT project.* FROM project INNER JOIN (task INNER JOIN task_has_developer"
                                                                + "ON (task.ID = task_has_developer.task_ID) "
                                                               + "WHERE task_has_developer.developer_ID=? AND "
                                                              + "task_has_developer.date=?) ON (project.ID = task.project_ID)");

            sInvitesByCoordinatorID = connection.prepareStatement("SELECT thd.* FROM ((task_has_developer AS thd INNERJOIN task AS t ON t.ID=thd.task_ID) INNERJOIN project AS p ON t.project_ID=p.ID) WHERE thd.ID=? AND thd.state=0");
            sRequestByCollaboratorID = connection.prepareStatement("SELECT thd.* FROM task_has_developer AS thd WHERE thd.developer_ID=?");  
            sOffertsByDeveloperID = connection.prepareStatement("SELECT t.* FROM ((((task AS t INNERJOIN task_has_skill AS ths ON t.ID=ths.task_ID) INNERJOIN skill AS s ON ths.skill_ID=s.ID) INNERJOIN skill_has_developer AS shd ON shd.skill_id=s.ID) INNERJOIN developer AS d ON shd.developer_ID=d.ID) WHERE d.ID=?");
            sDeveloperByRequest = connection.prepareStatement("SELECT d.* from (developer AS d INNERJOIN task_has_developer AS thd d.ID=thd.developer_ID) WHERE thd.ID=?");
            
            iProject = connection.prepareStatement("INSERT INTO project (name,description,coordinator_ID) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uProject = connection.prepareStatement("UPDATE project SET name=?,description=?,coordinator_ID=? WHERE ID=?");
            dProject = connection.prepareStatement("DELETE FROM project WHERE ID=?");
            
            iSkill = connection.prepareStatement("INSERT INTO skill (name,parent_ID) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uSkill = connection.prepareStatement("UPDATE skill SET name=?,parent_ID=? WHERE ID=?");
            dSkill = connection.prepareStatement("DELETE FROM skill WHERE ID=?");
            
            iTask = connection.prepareStatement("INSERT INTO task (name,numCollaborators,timeInterval,description,open,project_ID) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uTask = connection.prepareStatement("UPDATE task SET name=?,numCollaborators=?,timeInterval=?,description=?,open=?,project_ID=? WHERE ID=?");
            dTask = connection.prepareStatement("DELETE FROM task WHERE ID=?");
            
            iMessage = connection.prepareStatement("INSERT INTO message (private,text,type,project_ID) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uMessage = connection.prepareStatement("UPDATE message SET private=?,text=?,type=?,project_ID=? WHERE ID=?");
            dMessage = connection.prepareStatement("DELETE FROM message WHERE ID=?");
            
            iType = connection.prepareStatement("INSERT INTO type (type) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            uType = connection.prepareStatement("UPDATE type SET type=? WHERE ID=?");
            dType = connection.prepareStatement("DELETE FROM type WHERE ID=?");
            
            iRequest = connection.prepareStatement("INSERT INTO request (task_ID,developer_ID,state,date,vote) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uRequest = connection.prepareStatement("UPDATE request SET task_ID=?,developer_ID=?,state=?,date=?,vote=? WHERE task_ID=? AND developer_ID=?");
            dRequest = connection.prepareStatement("DELETE FROM request WHERE task_ID=? AND developer_ID=?");
            
        } catch (SQLException ex) {
            throw new DataLayerException("Error initializing newspaper data layer", ex);
        }
    }
    
    //metodi "factory" che permettono di creare
    //e inizializzare opportune implementazioni
    //delle interfacce del modello dati, nascondendo
    //all'utente tutti i particolari
    //factory methods to create and initialize
    //suitable implementations of the data model interfaces,
    //hiding all the implementation details
    @Override
    public Project createProject() {
        return new ProjectImpl(this);
    }

    public Project createProject(ResultSet rs) throws DataLayerException {
        try {
            ProjectImpl a = new ProjectImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            a.setDescription(rs.getString("description"));
            a.setCoordinatorKey(rs.getInt("coordinator_ID"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create project object form ResultSet", ex);
        }
    }
    
     @Override
    public Task createTask() {
        return new TaskImpl(this);
    }

    public Task createTask(ResultSet rs) throws DataLayerException {
        try {
            TaskImpl a = new TaskImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            a.setTimeInterval(rs.getTimestamp("timeInterval"));
            a.setOpen(rs.getBoolean("open"));
            a.setNumCollaborators(rs.getInt("numCollaborators"));
            a.setDescription(rs.getString("description"));     
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create task object form ResultSet", ex);
        }
    }
        
    
    @Override
    public Skill createSkill() {
        return new SkillImpl(this);
    }

    public Skill createSkill(ResultSet rs) throws DataLayerException {
        try {
            SkillImpl a = new SkillImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            a.setParent(rs.getInt("parent_ID"));     
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create task object form ResultSet", ex);
        }
    }
    
    @Override
    public Developer createDeveloper() {
        return new DeveloperImpl(this);
    }

    public Developer createDeveloper(ResultSet rs) throws DataLayerException {
        try {
            DeveloperImpl a = new DeveloperImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            a.setSurname(rs.getString("surname"));
            a.setUsername(rs.getString("username"));
            a.setMail(rs.getString("mail"));
            a.setPwd(rs.getString("pwd"));
            a.setBiography(rs.getString("biography"));
            GregorianCalendar birthdate = new GregorianCalendar();
            java.sql.Date date;
            date = rs.getDate("birthdate");
            birthdate.setTime(date);
            a.setBirthDate(birthdate);
            a.setCurriculum(rs.getString("curriculumString"));
            //PROBLEMA CON FILE!!!!
            //a.setCurriculum(rs.getFile("curriculumFile"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create task object form ResultSet", ex);
        }
    }
    
    @Override
    public Type createType() {
        return new TypeImpl(this);
    }

    public Type createType(ResultSet rs) throws DataLayerException {
        try {
            TypeImpl a = new TypeImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setType(rs.getString("type"));  
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create task object form ResultSet", ex);
        }
    }
    
    @Override
    public Message createMessage() {
        return new MessageImpl(this);
    }

    public Message createMessage(ResultSet rs) throws DataLayerException {
        try {
            MessageImpl a = new MessageImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setText(rs.getString("text"));
            a.setPrivate(rs.getBoolean("private"));
            a.setType(rs.getString("type"));
            
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create message object form ResultSet", ex);
        }
    }
    
    @Override
    public CollaborationRequest createCollaborationRequest() {
        return new CollaborationRequestImpl(this);
    }

    public CollaborationRequest createCollaborationRequest(ResultSet rs) throws DataLayerException {
        try {
            CollaborationRequestImpl a = new CollaborationRequestImpl(this);
            a.setKey(rs.getInt("ID"));
            GregorianCalendar requestDate = new GregorianCalendar();
            java.sql.Date date;
            date = rs.getDate("date");
            requestDate.setTime(date);
            a.setDate(requestDate);
            a.setState(rs.getInt("state"));
            
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create collaborationRequest object form ResultSet", ex);
        }
    }
    
    
    @Override
    public Project getProject(int project_key) throws DataLayerException {
        try {
            sProjectByID.setInt(1, project_key); //setta primo parametro query a project_key
            try (ResultSet rs = sProjectByID.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    return createProject(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load project by ID", ex);
        }
        return null;
    }
    
    
    @Override
    public List<Project> getProjects() throws DataLayerException {
        List<Project> result = new ArrayList();

        try (ResultSet rs = sProjects.executeQuery()) {
            while (rs.next()) {
                result.add(getProject(rs.getInt("ID")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project esistenti
    }
    
    @Override
    public List<Project> getProjects(String filter) throws DataLayerException {
        List<Project> result = new ArrayList();
        try {
            sProjectsByFilter.setString(1, filter); //setta primo parametro query a project_key
            try (ResultSet rs = sProjectsByFilter.executeQuery()) {
                while (rs.next()) {
                    result.add(getProject(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project esistenti
    }
    
    @Override
    public List<Task> getTasks(int project_key) throws DataLayerException {
        List<Task> result = new ArrayList();
         try {
            sTasksByProject.setInt(1, project_key);
            try (ResultSet rs = sProjects.executeQuery()) {
                while (rs.next()) {
                    result.add(getTask(rs.getInt("ID")));

                }
            } 
         }catch (SQLException ex) {
                throw new DataLayerException("Unable to load tasks by project", ex);
            }
        return result; 
    }
    
    @Override
    public Message getMessage(int message_key) throws DataLayerException {
         try {
            sMessageByID.setInt(1, message_key); //setta primo parametro query a project_key
            try (ResultSet rs = sMessageByID.executeQuery()) {
                if (rs.next()) {
                    return createMessage(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load message by ID", ex);
        }
        return null;
    }
    
    @Override
    public List<Message> getMessages(int project_key) throws DataLayerException {
        List<Message> result = new ArrayList();
        try {
            sMessagesByProject.setInt(1, project_key);
            try (ResultSet rs = sMessagesByProject.executeQuery()) {
                while (rs.next()) {
                    result.add(getMessage(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project esistenti
    }
    
    @Override
    public Skill getSkill(int skill_key) throws DataLayerException {
        try {
            sSkillByID.setInt(1, skill_key); 
            try (ResultSet rs = sSkillByID.executeQuery()) {
                if (rs.next()) {
                    return createSkill(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skill by ID", ex);
        }
        return null;
    }
    
    
    
    @Override
     public Task getTask(int task_key) throws DataLayerException {
        try {
            sTaskByID.setInt(1, task_key); 
            try (ResultSet rs = sTaskByID.executeQuery()) {
                if (rs.next()) {
                    return createTask(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load task by ID", ex);
        }
        return null;
    }
  
    
    @Override
    public Type getTypeByTask(int type_key) throws DataLayerException{
        try{
            sTypeByTask.setInt(1, type_key);
            try (ResultSet rs = sTypeByTask.executeQuery()) {
                if (rs.next()){
                    return createType(rs);
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load typeByTask", ex);
            }
        return null;
    }
    
    @Override
    public Map<Skill, Integer> getSkillsByTask(int task_key) throws DataLayerException{
        Map<Skill,Integer> result = new HashMap<>();
        try{
            sSkillsByTask.setInt(1, task_key);
            try (ResultSet rs = sSkillsByDeveloper.executeQuery()) {
                while (rs.next()){
                    result.put(getSkill(rs.getInt("ID")), rs.getInt("level_min"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load skillByTask", ex);
            }
        return result;
    }
    
    /*@Override 
    public int getVote(int task_key, int developer_key) throws DataLayerException {
        try{
            sVote.setInt(1, task_key);
            sVote.setInt(2, task_key);
        }
    }*/
    
    @Override
    public Map<Task, Integer> getTasksByDeveloper(int developer_key) throws DataLayerException{
        Map<Task, Integer> result = new HashMap<>();
        try{
            sTasksByDeveloper.setInt(1, developer_key);
            try(ResultSet rs = sTasksByDeveloper.executeQuery()) {
                while (rs.next()){
                   result.put(getTask(rs.getInt("task_ID")), rs.getInt("vote"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load taskByDev", ex);
            }
        return result;
    }
    
    @Override
    public Developer getDeveloper(int developer_key) throws DataLayerException{
        try{
            sDeveloperByID.setInt(1, developer_key);
            try(ResultSet rs = sDeveloperByID.executeQuery()){
                if(rs.next()){
                return createDeveloper(rs);
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load developer", ex);
            }
        return null;
    }
    
    
    @Override
    public Map<Developer,Integer> getCollaboratorsByTask(int task_key) throws DataLayerException{
         Map<Developer, Integer> result = new HashMap<>();
        try{
            sCollaboratorsByTask.setInt(1, task_key);
            try(ResultSet rs = sCollaboratorsByTask.executeQuery()) {
                while (rs.next()){
                   result.put(getDeveloper(rs.getInt("developer_ID")), rs.getInt("vote"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load collaboratorByTask", ex);
            }
        return result;
    }
    
    
    @Override
    public Map<Developer,Integer> getDevelopersBySkill(int skill_key) throws DataLayerException{
        Map<Developer,Integer> result = new HashMap<>();
        try{
            sDeveloperBySkill.setInt(1, skill_key);
            try(ResultSet rs = sDeveloperBySkill.executeQuery()) {
                while (rs.next()){
                   result.put(getDeveloper(rs.getInt("ID")), rs.getInt("level"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load developerBySkill", ex);
            }
        return result;
    }
    
    @Override
    public Map<Developer,Integer> getDevelopersBySkill(int skill_key, int level) throws DataLayerException{
        Map<Developer,Integer> result = new HashMap<>();
        try{
            sDeveloperBySkillWithLevel.setInt(1, skill_key);
            sDeveloperBySkillWithLevel.setInt(2, level);
            
            try(ResultSet rs = sDeveloperBySkillWithLevel.executeQuery()) {
                while (rs.next()){
                   result.put(getDeveloper(rs.getInt("ID")), rs.getInt("level"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load developerBySkill", ex);
            }
        return result;
    }
    
    @Override
    public Map<Skill,Integer> getSkillsByDeveloper(int developer_key) throws DataLayerException{
        Map<Skill,Integer> result = new HashMap<>();
        try{
            sSkillsByDeveloper.setInt(1, developer_key);
            
            try(ResultSet rs = sSkillsByDeveloper.executeQuery()) {
                while (rs.next()){
                   result.put(getSkill(rs.getInt("ID")), rs.getInt("level"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load skillByDeveloper", ex);
            }
        return result;
    }
    
    @Override
    public Skill getParent(int skill_key) throws DataLayerException{
        try{
            sParentBySkill.setInt(1, skill_key);
            try(ResultSet rs = sParentBySkill.executeQuery()){
                if (rs.next()){
                return getSkill(rs.getInt("parent_ID"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load skillByDeveloper", ex);
            }
        return null;
        
    }
}


