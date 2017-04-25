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
    private PreparedStatement sDeveloperBySkillWithVote, sDeveloperBySkill, sTasksByProject, sTaskByRequest ;
    private PreparedStatement sParentBySkill, sCollaboratorsByProjectID, sProjectsByDeveloperID;
    private PreparedStatement sProjectsByDeveloperIDandDate, sInvitesByCoordinatorID, sRequestByCollaboratorID;
    private PreparedStatement sOffertsByDeveloperID, getDeveloperByRequest;
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
            sGetLatestIssueNumber = connection.prepareStatement("SELECT MAX(number) AS number FROM issue");
            sArticlesByIssue = connection.prepareStatement("SELECT ID AS articleID FROM article WHERE issueID=?");
            sSkillsByTask = connection.prepareStatement("SELECT task_ID FROM task_has_skill WHERE"
                                                          + "skill_ID=?");
            sSkillsByDeveloper = connection.prepareStatement("SELECT skill_ID FROM skill_has_developer WHERE developer_ID=?");
            sRequestByTask = connection.prepareStatement("SELECT * FROM task_has_developer WHERE task_ID=?");
            sCollaboratorsByTask = connection.prepareStatement("SELECT developer_ID FROM task_has_developer WHERE task_ID=?"
                                                            + "AND state=1");
            sImagesByIssue = connection.prepareStatement("SELECT article_image.imageID FROM article_image INNER JOIN article ON (article_image.articleID = article.ID) WHERE article.issueID=?");
            sImagesByArticle = connection.prepareStatement("SELECT imageID FROM article_image WHERE articleID=?");
            sImageData = connection.prepareStatement("SELECT data FROM image WHERE ID=?");

            //notare l'ultimo paametro extra di questa chiamata a
            //prepareStatement: lo usiamo per assicurarci che il JDBC
            //restituisca la chiave generata automaticamente per il
            //record inserito
            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iArticle = connection.prepareStatement("INSERT INTO article (title,text,authorID,issueID,page) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uArticle = connection.prepareStatement("UPDATE article SET title=?,text=?,authorID=?,issueID=?, page=? WHERE ID=?");
            dArticle = connection.prepareStatement("DELETE FROM article WHERE ID=?");

            iIssue = connection.prepareStatement("INSERT INTO issue (date,number) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uIssue = connection.prepareStatement("UPDATE issue SET date=?,number=? WHERE ID=?");
            dIssue = connection.prepareStatement("DELETE FROM issue WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataLayerException("Error initializing newspaper data layer", ex);
        }
    }

    
}
