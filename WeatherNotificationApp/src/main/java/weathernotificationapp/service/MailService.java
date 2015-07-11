package weathernotificationapp.service;

import org.springframework.stereotype.Service;
import weathernotificationapp.entity.SubscriptionEntity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailService {

    public static final String SENDER_USER_MAIL_ADDRESS = "weathernews@outlook.hu";
    public static final String SENDER_PASSWORD = "";
    public static final String MAIL_SUBJECT = "Weather Notification";
    public static final String TEST_RECEIVER_MAIL_ADDRESS = "jhszcsb@gmail.com";
    public static final String TEST_CITY = "Test City";
    public static final double TEST_TEMPERATURE = 30;

    public void sendMailNotification(SubscriptionEntity s) {
        final String username = SENDER_USER_MAIL_ADDRESS;
        final String password = SENDER_PASSWORD;

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_USER_MAIL_ADDRESS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s.getEmail()));
            message.setSubject(MAIL_SUBJECT);
            message.setText(prepareMailText(s.getCity(), s.getTemperature()));

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void testMail() {
        SubscriptionEntity testEntity = new SubscriptionEntity();
        testEntity.setEmail(TEST_RECEIVER_MAIL_ADDRESS);
        testEntity.setCity(TEST_CITY);
        testEntity.setTemperature(TEST_TEMPERATURE);
        sendMailNotification(testEntity);
    }

    private String prepareMailText(String city, double temperature) {
        return "Dear Subscriber,"
                + "\n\n This is a notification about weather."
                + "\n\n The temperature in " + city
                + " is above " + temperature
                + " degrees (Celsius).";
    }
}
