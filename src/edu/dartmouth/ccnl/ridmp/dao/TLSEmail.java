package edu.dartmouth.ccnl.ridmp.dao;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TLSEmail {

    /**
     * Outgoing Mail (SMTP) Server
     * requires TLS or SSL: ****.***.*** (use authentication)
     * Use Authentication: Yes
     * Port for TLS/STARTTLS: 587
     */
    public static void main(String[] args) {
        try {
            sendSSLMessage("test", "test", "*******);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        if (true)
                return;

        final String toEmail = "********; // can be any email id

        Properties props = new Properties();


        props.put("mail.smtp.host", "***.****.***");
        props.put("mail.smtp.socketFactory.port", "***");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("dsd", "dsds");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("****@****.***"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void send() {

        final String username = "******; //requires valid gmail id
        final String password = "******; // correct password for gmail id
        final String toEmail = "******"; // can be any email id

        String SMTP_HOST_NAME = "****.****.****";
        String SMTP_PORT = "465";
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("username","password");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@no-spam.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("to@no-spam.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler," +
                    "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendSSLMessage(String subject,
                               String message, String from) throws MessagingException {
        boolean debug = true;

        final String username = "******@****.***"; //requires valid gmail id
        final String password = "********"; // correct password for gmail id
        final String toEmail = "******@****.***"; // can be any email id

        String SMTP_HOST_NAME = "****.***.***";
        String SMTP_PORT = "***";
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        session.setDebug(debug);

        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress addressTo = new InternetAddress(toEmail);

        msg.setRecipient(Message.RecipientType.TO, addressTo);



// Setting the Subject and Content Type

        msg.setSubject(subject);

        msg.setContent(message, "text/plain");

        Transport.send(msg);

    }
}
