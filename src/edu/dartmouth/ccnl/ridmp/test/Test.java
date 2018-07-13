package edu.dartmouth.ccnl.ridmp.test;

import edu.dartmouth.ccnl.ridmp.com.HibernateEXC;
import edu.dartmouth.ccnl.ridmp.com.PersistenceManager;
import edu.dartmouth.ccnl.ridmp.com.PersistenceManagerImp;
import edu.dartmouth.ccnl.ridmp.com.PersistenceUtils;
import edu.dartmouth.ccnl.ridmp.dto.ProfileDTO;

import java.util.List;

/**
 * Created by ccnl on 4/25/2015.
 */
public class Test {
    static PersistenceManager persistenceManager = PersistenceManagerImp.getInstance();
    public static void main(String[] args) {
        PersistenceUtils.init();
        boolean b = availableUser("Amirhossein.Sharifzadeh.18@dartmouth.edu", "fdffdf");
        System.out.println();
    }

    private static boolean availableUser(String email, String did) {
        ProfileDTO profileDTO = new ProfileDTO();
        List<ProfileDTO> users = null;
        try {
            profileDTO.setEmailAddress(email);
            users = persistenceManager.findByExample(profileDTO);
            if (users.size() > 0)
                return false;

            profileDTO = new ProfileDTO();
            profileDTO.setDid(did);
            users = persistenceManager.findByExample(profileDTO);
            if (users.size() > 0)
                return false;
        } catch (HibernateEXC hibernateEXC) {
            hibernateEXC.printStackTrace();
        }
        return true;
    }
}
