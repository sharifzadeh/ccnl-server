package edu.dartmouth.ccnl.ridmp.com;

import java.io.Serializable;

/**
 * @author Amir  Sharifzadeh
 * @date Aug 15, 2005
 */
public class SearchParameters implements Serializable {
    private String className;
    private String fixedCriteria;
    private String userCriteria;
    private String userCriteriaText;
    private int maxResults;

    private boolean userCriteriaNotValid;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFixedCriteria() {
        return fixedCriteria;
    }

    public void setFixedCriteria(String fixedCriteria) {
        this.fixedCriteria = fixedCriteria;
    }

    public String getUserCriteria() {
        return userCriteria;
    }

    public void setUserCriteria(String userCriteria) {
        this.userCriteria = userCriteria;
    }

    public String getUserCriteriaText() {
        return userCriteriaText;
    }

    public void setUserCriteriaText(String userCriteriaText) {
        this.userCriteriaText = userCriteriaText;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public boolean isUserCriteriaNotValid() {
        return userCriteriaNotValid;
    }

    public void setUserCriteriaNotValid(boolean userCriteriaNotValid) {
        this.userCriteriaNotValid = userCriteriaNotValid;
    }
}
