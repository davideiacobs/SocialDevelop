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
import java.sql.Timestamp;
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
    private PreparedStatement sProjectsByFilter, sSkillsByTask, sSkillsByDeveloper, sQuestionsByCoordinatorID; 
    private PreparedStatement sCollaboratorsByTask, sVoteByTaskandDeveloper, sTypeByTask, sMessagesByProject;
    private PreparedStatement sDeveloperBySkillWithLevel, sDeveloperBySkill, sTasksByProject, sTaskByRequest ;
    private PreparedStatement sParentBySkill, sMessageByID, sCollaboratorsByProjectID, sProjectsByDeveloperID;
    private PreparedStatement sProjectsByDeveloperIDandDate, sInvitesByCoordinatorID, sProposalsByCollaboratorID;
    private PreparedStatement sOffertsByDeveloperID, sCoordinatorByTask, sTasksByDeveloper, sChildBySkill,sPublicMessagesByProject;
    private PreparedStatement sTypeByID, iProject, uProject, dProject;
    private PreparedStatement iDeveloper, uDeveloper, dDeveloper;
    private PreparedStatement iSkill, uSkill, dSkill;
    private PreparedStatement iTask, uTask, dTask;
    private PreparedStatement iMessage, uMessage, dMessage;
    private PreparedStatement iType, uType, dType;
    private PreparedStatement iRequest, uRequest, dRequest;
    private PreparedStatement iTaskHasSkill, uTaskHasSkill, dTaskHasSkill;
    
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
            
            sTypeByID = connection.prepareStatement("SELECT * FROM type WHERE ID=?");
            
            sDeveloperByID = connection.prepareStatement("SELECT * FROM developer WHERE ID=?");
            
            sMessagesByProject = connection.prepareStatement("SELECT ID FROM message WHERE project_ID=?");
            
            sMessageByID = connection.prepareStatement("SELECT * FROM message WHERE ID=?");
            
            sPublicMessagesByProject= connection.prepareStatement("SELECT ID FROM message WHERE project_ID=? and private=0");
            
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
            
            //sRequestByTask = connection.prepareStatement("SELECT * FROM task_has_developer WHERE task_ID=?");
            
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
            sTasksByProject = connection.prepareStatement("SELECT task.ID FROM task WHERE project_ID=?");
            
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
            
            
            iDeveloper = connection.prepareStatement("INSERT INTO developer (name,surname,username,mail,pwd,birthdate,biography,curriculumString) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uDeveloper = connection.prepareStatement("UPDATE developer SET name=?,surname=?,username=?,mail=?,pwd=?,birthdate=?,biography=?,curriculumFile=?,curriculumText=? WHERE ID=?");
            dDeveloper = connection.prepareStatement("DELETE FROM developer WHERE ID=?");
            
            iTask = connection.prepareStatement("INSERT INTO task (name,numCollaborators,start,end,description,open,project_ID) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uTask = connection.prepareStatement("UPDATE task SET name=?,numCollaborators=?,start=?,end=?,description=?,open=?,project_ID=? WHERE ID=?");
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
            
            
            iTaskHasSkill = connection.prepareStatement("INSERT INTO task_has_skill (task_ID,skill_ID,type_ID,level_min) VALUES(?,?,?,?)");
            
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
            Timestamp ts = rs.getTimestamp("start");
            GregorianCalendar start = new GregorianCalendar();
            start.setTime(ts);
            a.setStartDate(start);
            Timestamp ts2 = rs.getTimestamp("end");
            //se sono permessi valori nulli bisogna fare check del resultSet
            GregorianCalendar end = new GregorianCalendar();
            end.setTime(ts2);
            a.setEndDate(end);
            a.setOpen(rs.getBoolean("open"));
            a.setNumCollaborators(rs.getInt("numCollaborators"));
            a.setDescription(rs.getString("description"));   
            a.setProjectKey(rs.getInt("project_ID"));
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
            if(rs.getObject("parent_ID") != null && !rs.wasNull()){
                a.setParentKey(rs.getInt("parent_ID"));     
            }else{
                a.setParentKey(-1);
            }
            
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
                result.add((Project) getProject(rs.getInt("ID")));

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
            sProjectsByFilter.setString(1, "%" + filter + "%"); 
            sProjectsByFilter.setString(2,  "%" + filter + "%"); 

            try (ResultSet rs = sProjectsByFilter.executeQuery()) {
                while (rs.next()) {
                    result.add((Project) getProject(rs.getInt("ID")));
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
            try (ResultSet rs = sTasksByProject.executeQuery()) {
                while (rs.next()) {
                    result.add((Task) getTask(rs.getInt("ID")));

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
            sMessageByID.setInt(1, message_key); 
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
                    result.add((Message) getMessage(rs.getInt("ID")));
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
    public Type getType(int type_key) throws DataLayerException {
        try{
           sTypeByID.setInt(1, type_key);
           try(ResultSet rs = sTypeByID.executeQuery()){
               if(rs.next()){
                   return createType(rs);
               }
           }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to create type by ID", ex);
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
    public Type getTypeByTask(int task_key) throws DataLayerException{
        try{
            sTypeByTask.setInt(1, task_key);
            try (ResultSet rs = sTypeByTask.executeQuery()) {
                if (rs.next()){
                    try{
                        sTypeByID.setInt(1, rs.getInt("type_ID"));
                        try(ResultSet rs1 = sTypeByID.executeQuery()) {
                            if (rs1.next()){
                                return createType(rs1);
                            }
                        }
                    }catch (SQLException ex) {
                    throw new DataLayerException("Unable to load typeByTask", ex);
                    }
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
                    result.put((Skill) getSkill(rs.getInt("ID")), rs.getInt("level_min"));
                }
            }
        }catch (SQLException ex) {
                throw new DataLayerException("Unable to load skillByTask", ex);
            }
        return result;
    }
    
    @Override
    public Map<Task, Integer> getTasksByDeveloper(int developer_key) throws DataLayerException{
        Map<Task, Integer> result = new HashMap<>();
        try{
            sTasksByDeveloper.setInt(1, developer_key);
            try(ResultSet rs = sTasksByDeveloper.executeQuery()) {
                while (rs.next()){
                   result.put((Task) getTask(rs.getInt("task_ID")), rs.getInt("vote"));
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
                   result.put((Developer) getDeveloper(rs.getInt("developer_ID")), rs.getInt("vote"));
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
                   result.put((Developer) getDeveloper(rs.getInt("ID")), rs.getInt("level"));
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
                   result.put((Developer) getDeveloper(rs.getInt("ID")), rs.getInt("level"));
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
                   result.put((Skill) getSkill(rs.getInt("ID")), rs.getInt("level"));
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
                return (Skill) getSkill(rs.getInt("parent_ID"));
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
                    result.add((Skill) getSkill(rs.getInt("ID")));
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
                    result.add((Developer) getDeveloper(rs.getInt("ID")));
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
                     result.add((Project) getProject(rs.getInt("ID")));
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
                     result.add((Project) getProject(rs.getInt("ID")));
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
                    result.add((Task) getTask(rs.getInt("ID")));
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
                result.add((Message) getMessage(rs.getInt("ID")));
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
                    result.add((CollaborationRequest) getCollaborationRequest(rs.getInt("developer_ID"), rs.getInt("task_ID")));
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
                    result.add((CollaborationRequest) getCollaborationRequest(collaborator_key, rs.getInt("task_ID")));
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
                    return (Developer) getDeveloper(rs.getInt("coordinator_ID"));
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
                    result.add((CollaborationRequest) getCollaborationRequest(rs.getInt("developer_ID"), rs.getInt("task_ID")));
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to load questions by coordinator key ", ex);
        }
        return result;
    }
    
    @Override
    public int getVote(int task_key, int developer_key) throws DataLayerException{
        try{
            sVoteByTaskandDeveloper.setInt(1,task_key);
            sVoteByTaskandDeveloper.setInt(2, developer_key);
            try(ResultSet rs = sVoteByTaskandDeveloper.executeQuery()){
                if(rs.next()){
                    return rs.getInt("vote");
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to load vote by developer key and task key ", ex);
        }
        return -1;
    }
    
    @Override
    public Task getTaskByRequest(CollaborationRequest request) throws DataLayerException{
        int developer_key = request.getCollaboratorKey();
        int task_key = request.getTaskKey();
        try{
            sTaskByRequest.setInt(1, developer_key);
            sTaskByRequest.setInt(2, task_key);
            try(ResultSet rs = sTaskByRequest.executeQuery()){
                if(rs.next()){
                    return (Task) getTask(rs.getInt("task_ID"));
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to load task by request ", ex);
        }
        return null;
    }
    
    
    @Override
    public void storeProject(Project project) throws DataLayerException {
        int key = project.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!project.isDirty()) {
                    return;
                }
                uProject.setString(1, project.getName());
                uProject.setString(2, project.getDescription());
                if (project.getCoordinator() != null) {
                    uProject.setInt(3, project.getCoordinator().getKey());
                } else {
                    uProject.setNull(3, java.sql.Types.INTEGER);
                }
                uProject.setInt(4, project.getKey());
                uProject.executeUpdate();
            } else { //insert
                iProject.setString(1, project.getName());
                iProject.setString(2, project.getDescription());
                if (project.getCoordinator() != null) {
                    iProject.setInt(3, project.getCoordinator().getKey());
                } else {
                    iProject.setNull(3, java.sql.Types.INTEGER);
                }
                
                if (iProject.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    try (ResultSet keys = iProject.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            key = keys.getInt(1);
                        }
                    }
                }
            }
            //restituiamo l'oggetto appena inserito RICARICATO
            //dal database tramite le API del modello. In tal
            //modo terremo conto di ogni modifica apportata
            //durante la fase di inserimento
           
            if (key > 0) {
                project.copyFrom(getProject(key));
            }
            project.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void deleteProject(Project project) throws DataLayerException{
        int key = project.getKey();
        try{
            dProject.setInt(1, key);
            try(ResultSet rs = dProject.executeQuery()){
                
                }
            }catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    

    @Override
    public void storeDeveloper(Developer developer) throws DataLayerException {
        int key = developer.getKey();
        try {

            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!developer.isDirty()) {
                    return;
                }
                uDeveloper.setString(1, developer.getName());
                uDeveloper.setString(2, developer.getSurname());
                uDeveloper.setString(3, developer.getUsername());
                uDeveloper.setString(4, developer.getMail());
                uDeveloper.setString(5, developer.getPwd());
                Date sqldate = new Date(developer.getBirthDate().getTimeInMillis());
                uDeveloper.setDate(6, sqldate);
                uDeveloper.setString(7, developer.getBiography());
                //uDeveloper.setFile(8, developer.getCurriculumFile());
                uDeveloper.setString(8, developer.getCurriculumString());
                uDeveloper.setInt(9, developer.getKey());
                uDeveloper.executeUpdate();
            } else { //insert
                iDeveloper.setString(1, developer.getName());
                iDeveloper.setString(2, developer.getSurname());
                iDeveloper.setString(3, developer.getUsername());
                iDeveloper.setString(4, developer.getMail());
                iDeveloper.setString(5, developer.getPwd());
                Date sqldate = new Date(developer.getBirthDate().getTimeInMillis());
                iDeveloper.setDate(6, sqldate);
                iDeveloper.setString(7, developer.getBiography());
                //iDeveloper.setFile(8, developer.getCurriculumFile());
                iDeveloper.setString(8, developer.getCurriculumString());
               
                if (iDeveloper.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    try (ResultSet keys = iDeveloper.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            key = keys.getInt(1);
                        }
                    }
                }
            }
            //restituiamo l'oggetto appena inserito RICARICATO
            //dal database tramite le API del modello. In tal
            //modo terremo conto di ogni modifica apportata
            //durante la fase di inserimento
           
            if (key > 0) {
                developer.copyFrom(getDeveloper(key));
            }
            developer.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void deleteDeveloper(Developer developer) throws DataLayerException{
        int key = developer.getKey();
        try{
            dDeveloper.setInt(1, key);
            try(ResultSet rs = dDeveloper.executeQuery()){
                
                }
            }catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void storeTask(Task task) throws DataLayerException {
        int key = task.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!task.isDirty()) {
                    return;
                }
                uTask.setString(1, task.getName());
                uTask.setInt(2, task.getNumCollaborators());
                Date sqldate1 = new Date(task.getStartDate().getTimeInMillis());
                uTask.setDate(3, sqldate1);
                Date sqldate2 = new Date(task.getEndDate().getTimeInMillis());
                uTask.setDate(4, sqldate2);
                uTask.setString(5, task.getDescription());
                uTask.setBoolean(6, task.isOpen());
                
                if (task.getProject() != null) {
                    uTask.setInt(7, task.getProject().getKey());
                } else {
                    uTask.setNull(7, java.sql.Types.INTEGER);
                }
                uTask.setInt(4, task.getKey());
                uTask.executeUpdate();
            } else { //insert
                iTask.setString(1, task.getName());
                iTask.setInt(2, task.getNumCollaborators());
                Date sqldate1 = new Date(task.getStartDate().getTimeInMillis());
                iTask.setDate(3, sqldate1);
                Date sqldate2 = new Date(task.getEndDate().getTimeInMillis());
                iTask.setDate(4, sqldate2);
                iTask.setString(5, task.getDescription());
                iTask.setBoolean(6, task.isOpen());                
                if (task.getProject() != null) {
                    iTask.setInt(7, task.getProject().getKey());
                } else {
                    iTask.setNull(7, java.sql.Types.INTEGER);
                }
                
                if (iTask.executeUpdate() == 1) {
                    try (ResultSet keys = iTask.getGeneratedKeys()) {
                       
                        if (keys.next()) {
                           key = keys.getInt(1);
                        }
                    }
                }
            }
           
            if (key > 0) {
                task.copyFrom(getTask(key));
            }
            task.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void deleteTask(Task task) throws DataLayerException{
        int key = task.getKey();
        try{
            dTask.setInt(1, key);
            try(ResultSet rs = dTask.executeQuery()){
                
                }
            }catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void storeSkill(Skill skill) throws DataLayerException {
        int key = skill.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!skill.isDirty()) {
                    return;
                }
                uSkill.setString(1, skill.getName());
                if(skill.getParent() != null){
                    uSkill.setInt(2, skill.getParent().getKey());
                }else{
                    uSkill.setNull(2, java.sql.Types.INTEGER);
                }
                uSkill.setInt(3, skill.getKey());
                uSkill.executeUpdate();
            } else { //insert
                iSkill.setString(1, skill.getName());
                if(skill.getParent() != null){
                    iSkill.setInt(2, skill.getParent().getKey());
                }else{
                    iSkill.setNull(2, java.sql.Types.INTEGER);
                    skill.setParentKey(-1);
                }
                
                if (iSkill.executeUpdate() == 1) {
                    try (ResultSet keys = iSkill.getGeneratedKeys()) {
                        if (keys.next()) {
                           key = keys.getInt(1);
                        }
                    }
                }
            }
           
            if (key > 0) {
               //getSkill(key).setParentKey(-1);
                skill.copyFrom(getSkill(key));
            }
            skill.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void deleteSkill(Skill skill) throws DataLayerException{
        int key = skill.getKey();
        try{
            dSkill.setInt(1, key);
            try(ResultSet rs = dSkill.executeQuery()){
                
                }
            }catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void storeMessage(Message message) throws DataLayerException {
        int key = message.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!message.isDirty()) {
                    return;
                }
                uMessage.setBoolean(1, message.isPrivate());
                uMessage.setString(2, message.getText());
                uMessage.setString(3, message.getType());
                if(message.getProject()!=null){
                    uMessage.setInt(4, message.getProject().getKey());
                }else{
                    uMessage.setNull(4, java.sql.Types.INTEGER);
                }
                uMessage.setInt(5, message.getKey());

                uMessage.executeUpdate();
            } else { //insert
                iMessage.setBoolean(1, message.isPrivate());
                iMessage.setString(2, message.getText());
                iMessage.setString(3, message.getType());
                if(message.getProject()!=null){
                    iMessage.setInt(4, message.getProject().getKey());
                }else{
                    iMessage.setNull(4, java.sql.Types.INTEGER);
                }
                if (iMessage.executeUpdate() == 1) {
                    try (ResultSet keys = iMessage.getGeneratedKeys()) {
                        if (keys.next()) {
                           key = keys.getInt(1);
                        }
                    }
                }
            }
           
            if (key > 0) {
                message.copyFrom(getMessage(key));
            }
            message.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    @Override
    public void deleteMessage(Message message) throws DataLayerException{
        int key = message.getKey();
        try{
            dMessage.setInt(1, key);
            try(ResultSet rs = dMessage.executeQuery()){
                
                }
            }catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }
    
    
    @Override
    public void storeType(Type type) throws DataLayerException {
        int key = type.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!type.isDirty()) {
                    return;
                }
                uType.setString(1, type.getType());
                uType.setInt(2, type.getKey());

                uType.executeUpdate();
            } else { //insert
                iType.setString(1, type.getType());                
                if (iType.executeUpdate() == 1) {
                    try (ResultSet keys = iType.getGeneratedKeys()) {
                        if (keys.next()) {
                           key = keys.getInt(1);
                        }
                    }
                }
            }
           
            if (key > 0) {
                type.copyFrom(getType(key));
            }
            type.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store type", ex);
        }
    }
    
    @Override
    public void deleteType(Type type) throws DataLayerException{
        int key = type.getKey();
        try{
            dType.setInt(1, key);
            try(ResultSet rs = dType.executeQuery()){
                
                }
            }catch (SQLException ex) {
            throw new DataLayerException("Unable to delete type", ex);
        }
    }
    
    
     @Override
     //NON FUNGE!!!!!!!!!!!!!!1
    // task_ID,developer_ID,state,date,vote
    public void storeRequest(CollaborationRequest request) throws DataLayerException {
        int task_key = request.getTaskKey();
        int collaborator_key = request.getCollaboratorKey();
        try {
            if (collaborator_key > 0 && task_key >0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!request.isDirty()) {
                    return;
                }
                uRequest.setInt(1, task_key);
                uRequest.setInt(2, collaborator_key);
                uRequest.setInt(3, request.getState());
                Date sqldate = new Date(request.getDate().getTimeInMillis());
                uRequest.setDate(4, sqldate);
                uRequest.setInt(5, request.getVote());
                uRequest.executeUpdate();
                
            } else { //insert
                iRequest.setInt(1, task_key);
                iRequest.setInt(2, collaborator_key);
                iRequest.setInt(3, request.getState());
                Date sqldate = new Date(request.getDate().getTimeInMillis());
                iRequest.setDate(4, sqldate);
                iRequest.setInt(5, request.getVote());
                if (iRequest.executeUpdate() == 1) {
                    try (ResultSet keys = iRequest.getGeneratedKeys()) {
                        if (keys.next()) {
                           task_key = keys.getInt(1);
                           collaborator_key = keys.getInt(2);
                        }
                    }
                }
            }
           
            if (task_key > 0 && collaborator_key > 0) {
                request.copyFrom(getCollaborationRequest(collaborator_key, task_key));
            }
            request.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store type", ex);
        }
    }
    
    @Override
    public void deleteRequest(CollaborationRequest request) throws DataLayerException{
        int task_key = request.getTaskKey();
        int collaborator_key = request.getCollaboratorKey();
        try{
            dRequest.setInt(1, task_key);
            dRequest.setInt(2, collaborator_key);
            try(ResultSet rs = dRequest.executeQuery()){
                
                }
            }catch (SQLException ex) {
            throw new DataLayerException("Unable to delete request", ex);
        }
    }
    
    @Override
    public void storeTaskHasSkill(int task_ID, int skill_ID, int type_ID, int level_min) throws DataLayerException {
        boolean flag = true;
        try {
            sTaskByID.setInt(1, task_ID);
            try(ResultSet rs = sTaskByID.executeQuery()){
                if(!rs.next()){
                    flag = false;
                }
            }
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to delete request", ex);
        }
        if(flag){
            try {
                sSkillByID.setInt(1, skill_ID);
                try(ResultSet rs = sSkillByID.executeQuery()){
                    if(!rs.next()){
                        flag = false;
                    }
                }
            }catch (SQLException ex) {
                throw new DataLayerException("Unable to delete request", ex);
            }
            if(flag){
                try {
                    sTypeByID.setInt(1, type_ID);
                    try(ResultSet rs = sTypeByID.executeQuery()){
                        if(!rs.next()){
                            flag = false;
                        }
                    }
                }catch (SQLException ex) {
                    throw new DataLayerException("Unable to delete request", ex);
                }
                //continua qui
                try{
                    iTaskHasSkill.setInt(1, task_ID);
                    iTaskHasSkill.setInt(2, skill_ID);
                    iTaskHasSkill.setInt(3, type_ID);
                    iTaskHasSkill.setInt(4, level_min);
                    iTaskHasSkill.executeUpdate();  
                        
                }catch (SQLException ex) {
                    throw new DataLayerException("Unable to insert task_has_skill", ex);
                }
                
            }
        }
            
    }
    
    
    
    
}
    
    


