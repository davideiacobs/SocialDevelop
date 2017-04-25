/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.data.DataLayerMysqlImpl;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.NamingException;
import javax.sql.DataSource;
import socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author david
 */
public class SocialDevelopDataLayerMysqlImpl extends DataLayerMysqlImpl implements SocialDevelopDataLayer{
    
    private PreparedStatement sProjectByID, sTaskByID, sProjects, sDeveloperByID, sSkillByID;
    private PreparedStatement sProjectByIDFilter, sSkillsByTask, sSkillsByDeveloper, sRequestByTask; 
    private PreparedStatement sCollaboratorsByTask, sVoteByTaskandDeveloper, sTypeByTask;
    private PreparedStatement sDeveloperBySkillWithLevel, sDeveloperBySkill, sTasksByProject, sTaskByRequest ;
    private PreparedStatement sParentBySkill, sCollaboratorsByProjectID, sProjectsByDeveloperID;
    private PreparedStatement sProjectsByDeveloperIDandDate, sInvitesByCoordinatorID, sRequestByCollaboratorID;
    private PreparedStatement sOffertsByDeveloperID, sDeveloperByRequest;
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
            sProjects = connection.prepareStatement("SELECT ID FROM project");
            sSkillByID = connection.prepareStatement("SELECT * FROM skill WHERE ID=?");
            sProjectByIDFilter = connection.prepareStatement("SELECT * FROM project WHERE ID=? and "
                                                         + "(name LIKE ? or description LIKE ?)");
            sSkillsByTask = connection.prepareStatement("SELECT skill.* FROM skill INNER JOIN task_has_skill ON"
                                    + "(skill.ID = task_has_skill.skill_ID) WHERE task_has_skill.task_ID=?");
            sSkillsByDeveloper = connection.prepareStatement("SELECT developer.* FROM developer INNER JOIN skill_has_developer"
                                        + "ON (developer.ID = skill_has_developer.developer_ID) WHERE skill_has_developer.developer_ID=?");
            sRequestByTask = connection.prepareStatement("SELECT * FROM task_has_developer WHERE task_ID=?");
            sCollaboratorsByTask = connection.prepareStatement("SELECT developer.* FROM developer INNER JOIN task_has_developer "
                                     + "ON (developer.ID = task_has_developer.ID)  WHERE task_has_developer.task_ID=?"
                                                            + "AND task_has_developer.state=1");
            sVoteByTaskandDeveloper = connection.prepareStatement("SELECT vote FROM task_has_developer WHERE task_ID=? "
                                        + "AND developer_ID=?");
            sTypeByTask = connection.prepareStatement("SELECT type_ID FROM task_has_skill WHERE task_ID=?");
            sDeveloperBySkillWithLevel = connection.prepareStatement("SELECT developer.* FROM developer INNER JOIN "
                                + "skill_has_developer ON (developer.ID = skill_has_developer.developer_ID)"
                                            + "WHERE skill_has_developer.skill_ID=? AND skill_has_developer.level=?");
            sDeveloperBySkill = connection.prepareStatement("SELECT developer.* FROM developer INNER JOIN skill_has_developer "
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

    
}
