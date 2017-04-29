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
import java.sql.Date;
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
    
    //NB: sRequestByID --> ID è inteso come coppia collaborator_key-task_key)
    private PreparedStatement sProjectByID, sTaskByID, sProjects, sDeveloperByID, sSkillByID, sRequestByID;
    private PreparedStatement sProjectsByFilter, sSkillsByTask, sSkillsByDeveloper, sRequestByTask, sQuestionsByCoordinatorID; 
    private PreparedStatement sCollaboratorsByTask, sVoteByTaskandDeveloper, sTypeByTask, sMessagesByProject;
    private PreparedStatement sDeveloperBySkillWithLevel, sDeveloperBySkill, sTasksByProject, sTaskByRequest ;
    private PreparedStatement sParentBySkill, sMessageByID, sCollaboratorsByProjectID, sProjectsByDeveloperID;
    private PreparedStatement sProjectsByDeveloperIDandDate, sInvitesByCoordinatorID, sProposalsByCollaboratorID;
    private PreparedStatement sOffertsByDeveloperID, sCoordinatorByTask, sTasksByDeveloper, sChildBySkill,sPublicMessagesByProject;
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
            sPublicMessagesByProject= connection.prepareStatement("SELECT ID FROM messages WHERE project_ID=? and private=0");
            sProjects = connection.prepareStatement("SELECT ID FROM project");
            sRequestByID = connection.prepareStatement("SELECT task_has_developer.*,project.coordinator_ID"
                    + " FROM (task_has_developer INNER JOIN task ON (task.ID=task_has_developer.task_ID) "
                    + "WHERE developer_ID=? AND task_ID=?) INNER JOIN project ON (task.project_ID=project.ID)");
            sCoordinatorByTask = connection.prepareStatement("SELECT p.coordinator_ID FROM (task AS t WHERE t.ID=?) INNER JOIN project AS p "
                    + "ON (p.ID = task.project_ID)");
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
            sChildBySkill = connection.prepareStatement("SELECT ID FROM skill WHERE parent_ID=?");
            sCollaboratorsByProjectID = connection.prepareStatement("SELECT developer.ID "
                                                         + "FROM developer INNER JOIN (task_has_developer INNER JOIN task ON "
                                                  + "(task_has_developer.task_ID = task.ID) WHERE task.project_ID=?) ON (developer.ID = task_has_developer.developer_ID)");
                                                
            //seleziona i progetti ai quali il developer ha partecipato
            sProjectsByDeveloperID = connection.prepareStatement("SELECT project.ID FROM project INNER JOIN (task INNER JOIN task_has_developer"
                                                                + "ON (task.ID = task_has_developer.task_ID) "
                                                               + "WHERE task_has_developer.developer_ID=?) ON (project.ID = task.project_ID");
            sProjectsByDeveloperIDandDate = connection.prepareStatement("SELECT project.ID FROM project INNER JOIN (task INNER JOIN task_has_developer"
                                                                + "ON (task.ID = task_has_developer.task_ID) "
                                                               + "WHERE task_has_developer.developer_ID=? AND "
                                                              + "task_has_developer.date=?) ON (project.ID = task.project_ID)");
            
            //seleziona inviti di partecipazione inviati da un coordinatore(pannello degli inviti)
            sInvitesByCoordinatorID = connection.prepareStatement("SELECT thd.* FROM ((task_has_developer AS thd INNERJOIN task AS t ON t.ID=thd.task_ID) "
                             + "INNERJOIN project AS p ON t.project_ID=p.ID) WHERE thd.ID=? AND thd.sender=0");
            
            //seleziona proposte che un collaboratore riceve da un coordinatore(pannello delle proposte)
            sProposalsByCollaboratorID = connection.prepareStatement("SELECT thd.task_ID FROM task_has_developer AS thd WHERE "
                                        + "thd.developer_ID=? AND thd.sender=0");  
            
            //seleziona proposte che un coordinatore riceve da un collaboratore(pannello delle domande)
            
            /*SELECT thd.task_ID, thd.developer_ID FROM task_has_developer AS thd WHERE "
                                        + "thd.sender=1 INNER JOIN task AS t ON (thd.task_ID = t.ID) INNER JOIN project AS p ON (t.project_ID = p.ID) WHERE p.coordinator_ID=?");  
            */
            sQuestionsByCoordinatorID = connection.prepareStatement("SELECT thd.task_ID,thd.developer_ID FROM ((project AS p WHERE p.coordinator_ID=?) "
                        + "INNER JOIN task AS t ON (p.ID = t.project_ID) INNER JOIN task_has_developer AS thd ON (thd.task_ID = t.ID) WHERE thd.sender=1)");    
            
            //seleziona le skill per le quali il developer risulta idoneo a partecipare(pannello delle offerte)
            sOffertsByDeveloperID = connection.prepareStatement("SELECT t.ID FROM ((((task AS t INNERJOIN task_has_skill AS ths ON t.ID=ths.task_ID) "
                                                        + "INNERJOIN skill AS s ON ths.skill_ID=s.ID) INNERJOIN skill_has_developer AS shd ON shd.skill_id=s.ID) "
                                                        + "INNERJOIN developer AS d ON shd.developer_ID=d.ID) WHERE d.ID=? ");
                        
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
            //a.setKey(rs.getInt("ID"));
            GregorianCalendar requestDate = new GregorianCalendar();
            java.sql.Date date;
            date = rs.getDate("date");
            requestDate.setTime(date);
            a.setDate(requestDate);
            a.setState(rs.getInt("state"));
            a.setCollaboratorKey(rs.getInt("developer_ID"));
            a.setCoordinatorKey(rs.getInt("coordinator_ID"));
            a.setTaskKey(rs.getInt(("task_ID")));
            a.setSender(rs.getInt("sender"));
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
                throw new DataLayerException("Unable to load skill parent", ex);
            }
        return null;
        
    }
    
    @Override
    public List<Skill> getChild(int skill_key) throws DataLayerException{
        List<Skill> result = new ArrayList();
        try{
            sChildBySkill.setInt(1, skill_key);
            try(ResultSet rs = sChildBySkill.executeQuery()){
                while (rs.next()){
                    result.add(getSkill(rs.getInt("ID")));
                }
            }
            
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load skill child", ex);
            }
        return result;
    }
    
    @Override
    public  List<Developer> getProjectCollaborators(int project_key) throws DataLayerException{
        List<Developer> result = new ArrayList();
        try{
            sCollaboratorsByProjectID.setInt(1, project_key);
            try(ResultSet rs = sCollaboratorsByProjectID.executeQuery()){
                while(rs.next()){
                    result.add(getDeveloper(rs.getInt("ID")));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load skill child", ex);
            }
        return result;
    }
    
    @Override
     public List<Project> getDeveloperProjects (int developer_key) throws DataLayerException{
         List<Project> result = new ArrayList();
         try{
             sProjectsByDeveloperID.setInt(1, developer_key);
             try(ResultSet rs = sProjectsByDeveloperID.executeQuery()){
                 while(rs.next()){
                     result.add(getProject(rs.getInt("ID")));
                 }
             }
         }catch (SQLException ex) {
                throw new DataLayerException("Unable to load projects by developer", ex);
            }
        return result;
     }
     
     @Override
     public  List<Project> getDeveloperProjects (int developer_key, GregorianCalendar data) throws DataLayerException{
         List<Project> result = new ArrayList();
         try{
             sProjectsByDeveloperIDandDate.setInt(1, developer_key);
             Date sqldate = new Date(data.getTimeInMillis());
             sProjectsByDeveloperIDandDate.setDate(2, sqldate);
             try(ResultSet rs = sProjectsByDeveloperIDandDate.executeQuery()){
                 while (rs.next()){
                     result.add(getProject(rs.getInt("ID")));
                 }
             }
         }catch (SQLException ex) {
                throw new DataLayerException("Unable to load projects by developer and date", ex);
            }
        return result;
     }
     
    @Override
    public List<Task> getOffertsByDeveloper(int developer_key) throws DataLayerException{
        List<Task> result= new ArrayList();
        try{
            sOffertsByDeveloperID.setInt(1,developer_key);
            try(ResultSet rs = sOffertsByDeveloperID.executeQuery()){
                while(rs.next()){
                    result.add(getTask(rs.getInt("ID")));
                }

            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load projects by developer and date", ex);
            }
        return result;
        
        }
    
    @Override
     public List<Message> getPublicMessages(int project_key) throws DataLayerException{
         List<Message> result = new ArrayList();
         try{
            sPublicMessagesByProject.setInt(1, project_key);
            try(ResultSet rs = sPublicMessagesByProject.executeQuery()){
                while(rs.next()){
                result.add(getMessage(rs.getInt("ID")));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to public message by project", ex);
            }
        return result;
    }
    
    @Override
    public CollaborationRequest getCollaborationRequest(int collaborator_key, int task_key) throws DataLayerException{
         try {
            sRequestByID.setInt(1, collaborator_key);
            sRequestByID.setInt(2, task_key);
            try (ResultSet rs = sRequestByID.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    return createCollaborationRequest(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load collaboration request by ID", ex);
        }
        return null;
    }

     
     @Override
    public List<CollaborationRequest> getInvitesByCoordinator(int coordinator_key) throws DataLayerException{
        List<CollaborationRequest> result = new ArrayList();
        try{
            sInvitesByCoordinatorID.setInt(1,coordinator_key);
            try(ResultSet rs = sInvitesByCoordinatorID.executeQuery()){
                while(rs.next()){
                    result.add(getCollaborationRequest(rs.getInt("developer_ID"), rs.getInt("task_ID")));
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to load invetes by project coordinator key ", ex);
        }
        return result;
    }
    
    @Override
    public List<CollaborationRequest> getRequestsByCollaborator(int collaborator_key) throws DataLayerException{
        List<CollaborationRequest> result = new ArrayList();
        try{
            sProposalsByCollaboratorID.setInt(1, collaborator_key);
            try(ResultSet rs = sProposalsByCollaboratorID.executeQuery()){
                while(rs.next()){
                    result.add(getCollaborationRequest(collaborator_key, rs.getInt("task_ID")));
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to load requests by collaborator key ", ex);
        }
        return result;
        
    }
    
    @Override
    public Developer getCoordinatorByTask(int task_key) throws DataLayerException{
        try{
            sCoordinatorByTask.setInt(1, task_key);
            try(ResultSet rs = sCoordinatorByTask.executeQuery()){
                if(rs.next()){
                    return getDeveloper(rs.getInt("coordinator_ID"));
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to load coordiantor by task key ", ex);
        }
        return null;
    }
    
    
    @Override
    public List<CollaborationRequest> getQuestionsByCoordinator(int coordinator_key) throws DataLayerException{
        List<CollaborationRequest> result = new ArrayList();
        try{
            sQuestionsByCoordinatorID.setInt(1, coordinator_key);
            try(ResultSet rs = sQuestionsByCoordinatorID.executeQuery()){
                while(rs.next()){
                    result.add(getCollaborationRequest(rs.getInt("developer_ID"), rs.getInt("task_ID")));
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to load questions by coordinator key ", ex);
        }
        return result;
    }
   
    }   


