package edu.dartmouth.ccnl.ridmp.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ccnl on 6/15/2015.
 */
public class ControlPanelForm extends ActionForm {
    private String ids[];
    private Integer profileId;
    private String method;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String did;
    private String phoneNumber;
    private Integer levelType;
    private Integer gender;

    private Integer status;
    private Integer ruleAssignment;
    private String reward;
    private String vals;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.profileId = null;
        firstName = "";
        lastName = "";
        emailAddress = "";
        phoneNumber = "";
        levelType = 0;
        gender = 0;
        status = 0;
    }
    public String[] getIds() {
        return ids;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRuleAssignment() {
        return ruleAssignment;
    }

    public void setRuleAssignment(Integer ruleAssignment) {
        this.ruleAssignment = ruleAssignment;
    }

    public String getVals() {
        return vals;
    }

    public void setVals(String vals) {
        this.vals = vals;
    }
}
