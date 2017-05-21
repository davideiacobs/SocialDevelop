/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.File;
import java.util.GregorianCalendar;
import java.util.Map;
import socialdevelop.data.model.Developer;
import socialdevelop.data.model.Skill;
import socialdevelop.data.model.SocialDevelopDataLayer;
import socialdevelop.data.model.Task;

/**
 *
 * @author david
 */
public class DeveloperImpl implements Developer {
    
    private int key;
    private String name;
    private String surname;
    private String username;
    private String pwd;
    private String mail;
    private GregorianCalendar birthdate;
    private String biography;
    private String curriculumString;
    private File curriculumFile;
    private Map<Skill,Integer> skills;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public DeveloperImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        surname = "";
        username = "";
        pwd = "";
        mail = "";
        birthdate = null;
        biography = "";
        curriculumString = "";
        curriculumFile = null;
        skills = null;
        dirty = false;
    }
    
    @Override
    public int getKey(){
        return key;
    }
    
    @Override
    public void setUsername(String username){
        this.username = username;
        this.dirty = true;
    }
    
    @Override
    public String getUsername(){
        return username;
    }
    
    @Override
    public void setName(String name){
        this.name = name;
        this.dirty = true;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public void setSurname(String surname){
        this.surname = surname;
        this.dirty = true;
    }
    
    @Override
    public String getSurname(){
        return surname;
    }
    
    @Override
    public void setMail(String mail){
        this.mail = mail;
        this.dirty = true;
    }
    
    @Override
    public String getMail(){
        return mail;
    }
    
    @Override
    public void setPwd(String pwd){
        this.pwd = pwd;
        this.dirty = true;
    }
    
    @Override
    public String getPwd(){
        return pwd;
    }
    
    @Override
    public void setBirthDate(GregorianCalendar birthdate){
        this.birthdate = birthdate;
        this.dirty = true;
    }
    
    @Override
    public GregorianCalendar getBirthDate(){
        return birthdate;
    }
    
    @Override
    public void setBiography(String biography){
        this.biography = biography;
        this.dirty = true;
    }
    
    @Override
    public String getBiography(){
        return biography;
    }
    
    @Override
    public void setCurriculum(File curriculum){
        this.curriculumFile = curriculum;
        this.dirty = true;
    }
    
    @Override
    public void setCurriculum(String curriculum){
        this.curriculumString = curriculum;
        this.dirty = true;
    }
    
    @Override
    public File getCurriculumFile(){
        return curriculumFile;
    }
    
    @Override
    public String getCurriculumString(){
        return curriculumString;
    }
    
    @Override
    public void setSkills(Map<Skill, Integer> skills){
        this.skills = skills;
        this.dirty = true;
    }
    
    @Override
    public Map<Skill, Integer> getSkillsByDeveloper() throws DataLayerException{
        if(skills == null){
            skills = ownerdatalayer.getSkillsByDeveloper(this.key);
        }
        return skills;
    }
    
     @Override
    public void addSkill(Skill skill, int level){
        this.skills.put(skill,level);
        this.dirty = true;
    }
    
    @Override
    public void removeSkill(Skill skill){
        this.skills.remove(skill);
        this.dirty = true;
    }
    
    @Override
    public void modSkillLevel(Skill skill, int level){
        this.skills.put(skill, level);
        this.dirty = true;
    }
    

    @Override
    public Map<Task, Integer> getTasksByDeveloper() throws DataLayerException{
        return ownerdatalayer.getTasksByDeveloper(this.key);
    } 
        
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
    
    protected void setKey(int key) {
        this.key = key;
    }
      
    @Override
    public void copyFrom(Developer developer) throws DataLayerException {
        key = developer.getKey();
        name = developer.getName();
        surname = developer.getSurname();
        username = developer.getUsername();
        mail = developer.getMail();
        pwd = developer.getPwd();
        birthdate = developer.getBirthDate();
        biography = developer.getBiography();
        curriculumFile = developer.getCurriculumFile();
        curriculumString = developer.getCurriculumString();
        this.dirty = true;
    }
}
