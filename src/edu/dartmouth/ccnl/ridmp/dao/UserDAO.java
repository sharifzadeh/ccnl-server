package edu.dartmouth.ccnl.ridmp.dao;

import edu.dartmouth.ccnl.ridmp.com.HibernateEXC;
import edu.dartmouth.ccnl.ridmp.com.PersistenceManager;
import edu.dartmouth.ccnl.ridmp.com.PersistenceManagerImp;
import edu.dartmouth.ccnl.ridmp.dto.PersonTO;
import edu.dartmouth.ccnl.ridmp.dto.ProfileDTO;
import edu.dartmouth.ccnl.ridmp.dto.RewardTO;
import org.apache.taglibs.standard.lang.jpath.example.Person;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by ccnl on 3/12/2015.
 */
public enum UserDAO {

    INSTANCE;

    private static PersistenceManager persistenceManager = PersistenceManagerImp.getInstance();

    private static final String host = "ccnldart@gmail.com";

    private static final String from = "info@ccnl.dartmouth.edu";

    private static final String username = "ccnldart@gmail.com"; //requires valid gmail id
    private static final String password = "Aab27au#"; // correct password for gmail id

    public void saveUserProfile(ProfileDTO profileDTO, String pin) {

        try {
            persistenceManager.save(profileDTO);

            profileDTO.setStatus(1);
            persistenceManager.update(profileDTO);

            PersonTO personTO = new PersonTO();
            personTO.setdId(profileDTO.getDid());
            personTO.setEmail(profileDTO.getEmailAddress());
            personTO.setTicket(pin);
            personTO.setFirstName(profileDTO.getName());
            personTO.setLastName(profileDTO.getLastName());

//            TODO: hard code:
            RewardTO rewardTO = (RewardTO)persistenceManager.findByPrimaryKey(RewardTO.class, 3);
            personTO.setRewardId(3);
            personTO.setRewardTO(rewardTO);

            personTO.setNumberOfChoice(30);
            personTO.setReversal(15);
            personTO.setOrientations("37, 53");
            personTO.setSpecialFrequencies(".03, .04");
            personTO.setProbabilities(".8, .2");

            persistenceManager.save(personTO);
        } catch (HibernateEXC hibernateEXC) {
            hibernateEXC.printStackTrace();
            System.out.println(hibernateEXC.toString());
        }
    }

    public boolean saveUserProfile(String uuid, ProfileDTO profileDTO) {

        profileDTO.setConfKey(uuid);
        try {
            persistenceManager.save(profileDTO);
            return true;
        } catch (HibernateEXC hibernateEXC) {
            hibernateEXC.printStackTrace();
            System.out.println(hibernateEXC.toString());
            return false;
        }
    }

    public List<ProfileDTO> getUsers() {
        try {
            return persistenceManager.findAll(ProfileDTO.class);
        } catch (HibernateEXC hibernateEXC) {
            hibernateEXC.printStackTrace();
        }
        return new ArrayList<ProfileDTO>();
    }

    public List<PersonTO> getPersons() {
        try {
            return persistenceManager.findAll(PersonTO.class);
        } catch (HibernateEXC hibernateEXC) {
            hibernateEXC.printStackTrace();
        }
        return new ArrayList<PersonTO>();
    }

    public ProfileDTO activateUser(String key, String genKey) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setConfKey(key);
        List<ProfileDTO> users;
        try {
            users = persistenceManager.findByExample(profileDTO);
            if (users == null || users.size() == 0)
                return null;

            profileDTO = users.get(0);
            profileDTO.setStatus(1);
            persistenceManager.update(profileDTO);

            PersonTO personTO = new PersonTO();
            personTO.setdId(profileDTO.getDid());
            personTO.setEmail(profileDTO.getEmailAddress());
            personTO.setTicket(genKey);
            personTO.setFirstName(profileDTO.getName());
            personTO.setLastName(profileDTO.getLastName());

            persistenceManager.save(personTO);
            return profileDTO;
        } catch (HibernateEXC hibernateEXC) {
            hibernateEXC.printStackTrace();
        }

        return null;
    }


    public boolean availableUser(String email, String did) {
        ProfileDTO profileDTO = new ProfileDTO();
        List<ProfileDTO> users = null;
        try {
            profileDTO.setEmailAddress(email);
            users = persistenceManager.findByExample(profileDTO);
            if (users.size() > 0)
                return true;

            profileDTO = new ProfileDTO();
            profileDTO.setDid(did);
            users = persistenceManager.findByExample(profileDTO);
            if (users.size() > 0)
                return true;
        } catch (HibernateEXC hibernateEXC) {
            hibernateEXC.printStackTrace();
        }
        return false;
    }

    public static void sendEmail(ProfileDTO profileDTO, String content, String subject) {
        Properties properties = new Properties();
        properties.put("mail.smtp.user", "username");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.EnableSSL.enable", "true");

        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        String to = profileDTO.getEmailAddress();


        try {
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart =
                    new MimeBodyPart();
            messageBodyPart.setText(content);

            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String fileAttachment = "C:\\apache-tomcat-7.0.57\\webapps\\ROOT\\ridmp\\Instructions.pdf";
            DataSource source =
                    new FileDataSource(fileAttachment);
            messageBodyPart.setDataHandler(
                    new DataHandler(source));
            messageBodyPart.setFileName(fileAttachment);
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message


            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("CCNL@dartmouth.edu"));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(subject);

            //message.setContent(content, "text/html");
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }

    public void sentEmail(ProfileDTO personTO, int status) throws RemoteException {
        Properties properties = new Properties();
        properties.put("mail.smtp.user", "username");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.EnableSSL.enable", "true");

        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("CCNL@dartmouth.edu"));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("choper.ccnl@gmail.com"));

            String content = "<p>User info:</p><p>" + personTO.getName() + " " + personTO.getLastName() + "</p><p>Dartmouth ID:" +
                    personTO.getDid() + "</p><p>Email: " + personTO.getEmailAddress() + "</p>";

            if (status == 1)
                message.setSubject("User Registered");
            else if (status == 3)
                message.setSubject("User failed the task");
            if (status == 4)
                message.setSubject("User Finalized the task");

            message.setContent(content, "text/html");

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private String keyGen() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output.substring(0, 5);
    }

    public static void main(String[] args) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName("Amir");
        profileDTO.setLastName("Sharif");
        profileDTO.setEmailAddress("Amir.18@dartmouth.edu");

//        sendEmail(profileDTO);
    }

}
