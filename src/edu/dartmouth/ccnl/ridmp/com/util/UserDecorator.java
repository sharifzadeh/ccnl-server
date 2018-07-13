package edu.dartmouth.ccnl.ridmp.com.util;

import edu.dartmouth.ccnl.ridmp.dto.PersonTO;

import org.displaytag.decorator.TableDecorator;

import java.util.Date;

public class UserDecorator extends TableDecorator {

    public String getEmail() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        String emailId = "<a href=\"mailto:" + userData.getEmail() + "\">" + userData.getEmail() + "</a>";
        return emailId;
    }

    public String getRewardName() {
        String code = "<select name=\"rw\" id=\"reward\">";

        PersonTO userData = (PersonTO) getCurrentRowObject();
        int perId = userData.getPerId();
        if (userData.getRewardId() == 1)
            code += "<option selected=\"selected\" value='" + perId + "@" + 1 + "'>Probability Rule | Orientation</option>" +
                    "<option value='" + perId + "@" + 2 + "'>Probability Rule | Special Frequency</option>" +
                    "<option value='" + perId + "@" + 3 + "'>Feature Rule | Orientation</option>" +
                    "<option value='" + perId + "@" + 4 + "'>Feature Rule | Special Frequency</option>";

        else if (userData.getRewardId() == 2)
            code += "<option selected=\"selected\" value='" + perId + "@" + 2 + "'>Probability Rule | Special Frequency</option>" +
                    "<option value='" + perId + "@" + 1 + "'>Probability Rule | Orientation</option>" +
                    "<option value='" + perId + "@" + 3 + "'>Feature Rule | Orientation</option>" +
                    "<option value='" + perId + "@" + 4 + "'>Feature Rule | Special Frequency</option>";

        else if (userData.getRewardId() == 3)
            code += "<option selected=\"selected\" value='" + perId + "@" + 3 + "'>Feature Rule | Orientation</option>" +
                    "<option value='" + perId + "@" + 1 + "'>Probability Rule | Orientation</option>" +
                    "<option value='" + perId + "@" + 2 + "'>Probability Rule | Special Frequency</option>" +
                    "<option value='" + perId + "@" + 4 + "'>Feature Rule | Special Frequency</option>";

        else if (userData.getRewardId() == 4)
            code += "<option selected=\"selected\" value='" + perId + "@" + 4 + "'>Feature Rule | Special Frequency</option>" +
                    "<option value='" + perId + "@" + 1 + "'>Probability Rule | Orientation</option>" +
                    "<option value='" + perId + "@" + 2 + "'>Probability Rule | Special Frequency</option>" +
                    "<option value='" + perId + "@" + 3 + "'>Feature Rule | Orientation</option>";

        code += "</select>";

        return code;
    }

    public Integer getPerId() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getPerId();
    }

    public String getFirstName() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getFirstName();
    }

    public String getLastName() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getLastName();
    }

    public String getdId() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getdId();
    }

    public Integer getProbabilityId() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getProbabilityId();
    }

    public Integer getReversal() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getReversal();
    }

    public String getOrientations() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getOrientations();
    }

    public String getSpecialFrequencies() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getSpecialFrequencies();
    }

    public String getProbabilities() {
        PersonTO userData = (PersonTO) getCurrentRowObject();
        return userData.getProbabilities();
    }
}