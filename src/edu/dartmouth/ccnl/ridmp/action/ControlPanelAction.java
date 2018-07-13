package edu.dartmouth.ccnl.ridmp.action;

import edu.dartmouth.ccnl.ridmp.dao.UserDAO;
import edu.dartmouth.ccnl.ridmp.dto.PersonTO;
import edu.dartmouth.ccnl.ridmp.dto.ProfileDTO;
import edu.dartmouth.ccnl.ridmp.form.ControlPanelForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by ccnl on 6/15/2015.
 */
public class ControlPanelAction extends DispatchAction {

    private static UserDAO userDAO = UserDAO.INSTANCE;

    public ActionForward userList(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ControlPanelForm controlPanelForm = (ControlPanelForm) form;
        String params = request.getParameter("vals");
        if (params != null && params.trim().length() > 0) {
            StringTokenizer st = new StringTokenizer(params, ",");
            List<String> userSigns = new ArrayList<String>();
            while (st.hasMoreElements()) {
                userSigns.add("" + st.nextElement());
//            System.out.println("StringTokenizer Output: " + st.nextElement());
            }
            HashMap<Integer, Integer> hashAward = new HashMap<Integer, Integer>();
            int perId, statusAward;
            for (String sign : userSigns) {
                int m = sign.indexOf("@");
                perId = Integer.valueOf(sign.substring(0, m));
                statusAward = Integer.valueOf(sign.substring(m + 1));
                hashAward.put(perId, statusAward);
            }
            System.out.println();
        }
        List<PersonTO> users = userDAO.getPersons();
        request.setAttribute("users", users);
        return mapping.findForward("userList");
    }

    public ActionForward allocateUser(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ControlPanelForm controlPanelForm = (ControlPanelForm) form;
        String params = request.getParameter("vals");
        StringTokenizer st = new StringTokenizer(params, ",");
        List<String> userSigns = new ArrayList<String>();
        while (st.hasMoreElements()) {
            userSigns.add("" + st.nextElement());
//            System.out.println("StringTokenizer Output: " + st.nextElement());
        }
        HashMap<Integer, Integer> hashAward = new HashMap<Integer, Integer>();
        int perId, statusAward;
        for (String sign: userSigns) {
            int m = sign.indexOf("@");
            perId = Integer.valueOf(sign.substring(0, m));
            statusAward = Integer.valueOf(sign.substring(m + 1));
            hashAward.put(perId, statusAward);
        }
        List<PersonTO> users = userDAO.getPersons();
        request.setAttribute("users", users);
        return mapping.findForward("userList");
    }
}
