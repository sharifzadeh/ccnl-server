package edu.dartmouth.ccnl.ridmp.action;

import edu.dartmouth.ccnl.ridmp.dao.UserDAO;
import edu.dartmouth.ccnl.ridmp.dto.ProfileDTO;
import edu.dartmouth.ccnl.ridmp.form.ProfileForm;
import org.apache.struts.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by ccnl on 2/25/2015.
 */

public class ProfileAction extends DispatchAction {

    private static UserDAO userDAO = UserDAO.INSTANCE;

    public ActionForward userList(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ProfileForm profileForm = (ProfileForm) form;
        profileForm.reset(mapping, request);

        List<ProfileDTO> users =
                userDAO.getUsers();

        request.setAttribute("users", users);
        return mapping.findForward("userList");
    }

    public ActionForward addUser(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ProfileForm profileForm = (ProfileForm) form;
//        profileForm.reset(mapping, request);

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setEmailAddress(profileForm.getEmailAddress().trim().toUpperCase());
        profileDTO.setDid(profileForm.getDid().trim().toUpperCase());
        profileDTO.setGender(profileForm.getGender());
        profileDTO.setLastName(profileForm.getLastName().trim());
        profileDTO.setName(profileForm.getFirstName().trim());
        profileDTO.setPhoneNumber(profileForm.getPhoneNumber().trim());
        profileDTO.setLocalAddress(profileForm.getLocalAddress().trim());
        profileDTO.setLevelType(profileForm.getLevelType());
        profileDTO.setStatus(0);

        String uuid = UUID.randomUUID().toString();

        if (userDAO.saveUserProfile(uuid, profileDTO)) {

            String subject = "CCNL Request Application";
            String content = "Dear " + profileDTO.getName() + " " +
                    profileDTO.getLastName() + "\n\n," + "Thank you for your participation." +
                    "<p> Your confirmation key is: <a href=http://ccnl.dartmouth.edu:8080/webApp/profileACT.do?method=activate&key=" + uuid + ">Confirmation Key</a>";

            userDAO.sendEmail(profileDTO, content, subject);
        }

        return activate(mapping, form, request, response);
    }

    public ActionForward activate(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//        ProfileForm profileForm = (ProfileForm)form;
//        profileForm.reset(mapping, request);
//
//        ProfileDTO profileDTO = null;
//        String key = request.getParameter("key");
//        String genKey = "";
//
//        if (key != null && key.length() > 0) {
//            genKey = keyGen();
//            profileDTO = userDAO.activateUser(key, genKey);
//        }
//
//        if (profileDTO != null) {
//                    String subject = "Confirming Activation";
//            String content = "Dear " + profileDTO.getName() + " " +
//                    profileDTO.getLastName() + "," + "<p>Your key has been generated.</p>" +
//                    "<p>" + genKey + "</p>";
//
//            userDAO.sendEmail(profileDTO, content, subject);
//            userDAO.sentEmail(profileDTO, 1);
//        }

        ProfileForm profileForm = (ProfileForm) form;
//        profileForm.reset(mapping, request);

        String email = profileForm.getEmailAddress().trim().toUpperCase();
        String did = profileForm.getDid().trim().toUpperCase();

        if (userDAO.availableUser(email, did))
            return mapping.findForward("duplicate");

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setEmailAddress(email);
        profileDTO.setDid(did);

        profileDTO.setGender(profileForm.getGender());
        profileDTO.setLastName(profileForm.getLastName().trim());
        profileDTO.setName(profileForm.getFirstName().trim());
        profileDTO.setPhoneNumber(profileForm.getPhoneNumber().trim());
        profileDTO.setLocalAddress(profileForm.getLocalAddress().trim());
        profileDTO.setLevelType(profileForm.getLevelType());
        profileDTO.setStatus(0);

        String pin = generatePIN();
        userDAO.saveUserProfile(profileDTO, pin);

        String subject = "Confirming Activation";


        String content = "Dear " + profileDTO.getName() + " " +
                profileDTO.getLastName() + ",\n\n" + "Thank you for your participation.\n" +
                "Your pin is: " + pin + "\nYou can download the application here:\nhttp://ccnl.dartmouth.edu:8080/ridmp/RIDMPAward.jar\n\n" +
                "\nAttached you can find instructions for the experiment. Please email us at choper.ccnl@gmail.com if you have any further questions.\n\n" +
                "Thanks,\n" +
                "Cognitive and Computational Neuroscience Lab\n" +
                "Department of Psychological and Brain Sciences\n" +
                "Dartmouth College, NH";


        userDAO.sendEmail(profileDTO, content, subject);
        userDAO.sentEmail(profileDTO, 1);

        return mapping.findForward("activateuser");
    }

    private String generatePIN() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+|}{/".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output.substring(0, 5);
    }
}
