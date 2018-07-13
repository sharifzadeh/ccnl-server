package edu.dartmouth.ccnl.ridmp.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ccnl on 2/25/2015.
 */
public class ProfileForm extends ActionForm {
    private String ids[];
    private Integer profileId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String did;
    private String localAddress;
    private String phoneNumber;
    private Integer levelType;
    private Integer gender;

    private String method;
    private String submit;

    private Integer status;

    private Integer terms;

    private String termsTitle;

    private String termsInfo = "CONSENT TO TAKE PART IN RESEARCH\n" +
            "Dartmouth College\n" +
            "\n" +
            "Study title: Effects of Reward on Choice Behavior and Perception    \n" +
            "Principal Investigator: Alireza Soltani\n" +
            "\n" +
            "You are being asked to take part in a research study.  Taking part in research is voluntary.  \n" +
            "\n" +
            "Your decision whether or not to take part will have no effect on the quality of your academic standing or job status. Please ask questions if there is anything about this study you do not understand.\n" +
            "\n" +
            "What is the purpose of this study?\n" +
            "The purpose of the study is to understand how reward is integrated over time and affects choice behavior and perception.\n" +
            "\n" +
            "Will you benefit from taking part in this study?\n" +
            "You will not personally benefit from being in this research study. We hope to gather information that may help people in the future.\n" +
            "\n" +
            "What does this study involve?\n" +
            "Your participation in this study may last up to 3 hours, and you may potentially participate more than once in this study.\n" +
            "\n" +
            "During this study, you will view two or more visual targets on a computer screen. You will be asked to choose between these targets and will be provided with reward feedback after each choice. You may also be asked to discriminate between different objects and decide whether they are similar or not. You will report your decisions using a keyboard, button press, or by moving your eyes. The objects that you will see may consist of photographs, drawings, geometric shapes, or sinusoidal gratings. This process will be repeated several times.\n" +
            "\n" +
            "We may monitor your eye movements using an infrared camera sitting on the table in front of you, while asking you to rest your chin on a chinrest (to minimize your head movement). The infrared camera is similar to any other camera expect it is more sensitive to infrared light.\n" +
            "\n" +
            "If Applicable: What are the options if you do not want to take part in this study?\n" +
            "The alternative is not to take part in this study.  \n" +
            "\n" +
            "If you take part in this study, what activities will be done only for research purposes?\n" +
            "If you take part in this study, the following activities will be done only for research purposes: Monitoring your eye movements, recording your responses on the keyboard.\n" +
            "\n" +
            "What are the risks involved with being enrolled in this study? \n" +
            "There are no physical or psychological side effects or dangers involved with this study more than tiredness associated with looking at a computer screen and pressing keys on the keyboard. Nevertheless, you can stop at any time. There are no known health risks to be monitored by an infrared camera. If you wish to learn more about this eyetracker, please visit http://www.eyelinkinfo.com. The data we collect from you during this study is confidential and your name will not be in any report of the results of this study.\n" +
            "\n" +
            "Other important items you should know: \n" +
            "- Leaving the study:  You may choose to stop taking part in this study at any time. If you decide to stop taking part, it will have no effect on your academic standing or job status.  \n" +
            "- Number of people in this study:  We expect 200 people, total, to enroll in this study.\n" +
            "- Funding: There is no outside funding for this research project.\n" +
            "\n" +
            "How will your privacy be protected?\n" +
            "The information collected as data for this study includes: \n" +
            "The data collected in this study include behavioral measures, such as button press data and eye movements. Your name and age will be collected along with your answers to the questionnaire. We will also record your gender, handedness, age, and other basic information that will not and cannot be used to identify anything private about you. We may also ask for your average household income to better understand how the incentive provided by compensation from the experiment could affect your performance.\n" +
            "\n" +
            "We are careful to protect the identities of the people in this study.  We also keep the information collected for this study secure and confidential. The behavioral data and computer-based questionnaire will be stored on password-protected computers in the lab, using randomized ID. Data collected for this study will be maintained indefinitely.\n" +
            "\n" +
            "Will you be paid to take part in this study?\n" +
            "You will be paid for your participation based on the number of hours that you participate in the experiment (overall $10-20/hour). You may also choose to instead get one T-point for every hour of experimental data collection.\n" +
            "\n" +
            "Whom should you call with questions about this study?\n" +
            "If you have questions about this study or concerns about a research related injury, you can call the research director for this study: Dr. Alireza Soltani at (603) 646-2998 during normal business hours.  \n" +
            "\n" +
            "If you have questions, concerns, complaints, or suggestions about human research at Dartmouth, you may call the Office of the Committee for the Protection of Human Subjects at Dartmouth College (603) 646-6482 during normal business hours.\n";


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.profileId = null;
        firstName = "";
        lastName = "";
        emailAddress = "";
        localAddress = "";
        phoneNumber = "";
        levelType = 0;
        gender = 0;
        status = 0;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
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

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTerms() {
        return terms;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
    }

    public String getTermsInfo() {
        return termsInfo;
    }

    public void setTermsInfo(String termsInfo) {
        this.termsInfo = termsInfo;
    }

    public String getTermsTitle() {
        return "I have read the above information and I agree to take part in this study.";
    }

    public void setTermsTitle(String termsTitle) {
        this.termsTitle = termsTitle;
    }
}
