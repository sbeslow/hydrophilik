package com.hydrophilik.javaCrons.utils;

import com.hydrophilik.javaCrons.db.ErrorLogger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailPerson {

    public static void sendErrorEmail(String theMessage) {

        Config config = Config.getConfiguration();

        String errorEmailAddress = config.getSetting("errorEmailAddress");
        String emailHost = config.getSetting("emailHost");
        String emailUser = config.getSetting("emailUser");
        String emailPassword = config.getSetting("emailPassword");
        String emailPort = config.getSetting("emailPort");

        if ((null == errorEmailAddress) || (null == emailPort) ||
                (null == emailHost) || (null == emailUser) || (null == emailPassword)) {
            ErrorLogger.logError("Config is not set up to send error e-mail", config);
            return;
        }

        // Get system properties
        final Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", emailHost);
        properties.setProperty("mail.smtp.port", emailPort);
        properties.setProperty("mail.smtp.auth", "true");


        properties.setProperty("mail.user", emailUser);
        properties.setProperty("mail.password", emailPassword);
        properties.setProperty("mail.smtp.starttls.enable", "true");

        // Get the default Session object.
        //Session session = Session.getDefaultInstance(properties);
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {

                        String emailUser = properties.getProperty("mail.user");
                        String emailPassword = properties.getProperty("mail.password");
                        return new PasswordAuthentication(emailUser, emailPassword);
                    }
                });

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(emailUser));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(errorEmailAddress));

            // Set Subject: header field
            message.setSubject("hydrophilik error report");

            // Now set the actual message
            message.setText(theMessage);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }

}
