package weathernotificationapp.service;

import org.springframework.transaction.annotation.Transactional;
import weathernotificationapp.dao.ISubscriptionDAO;
import weathernotificationapp.model.SubscriptionEntity;

import javax.faces.bean.ManagedBean;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


@Transactional(readOnly = true)
@ManagedBean(name = "subscriptionService")
public class SubscriptionService implements ISubscriptionService {

    //@Autowired
    ISubscriptionDAO subscriptionDAO;

    @Transactional(readOnly = false)
    @Override
    public void createSubscription(SubscriptionEntity subscription) {
        System.out.println("SubscriptionService createSubscription");
        getSubscriptionDAO().createSubscription(subscription);
    }

    @Override
    public boolean isAlreadySubscribed(String email) {
        if(getSubscriptionDAO().findByEmail(email) == null) {
            return false;
        }
        else return true;
    }

    @Override
    public List<SubscriptionEntity> findAll() {
        return subscriptionDAO.findAll();
    }

    public ISubscriptionDAO getSubscriptionDAO() {
        return subscriptionDAO;
    }

    public void setSubscriptionDAO(ISubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    public String testSendMail() {
        testMail();
        return "mailSent";
    }

    private void testMail() {
        final String username = "";
        final String password = "í";

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
            message.setFrom(new InternetAddress(""));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("weathernotificationapp@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Subscriber,"
                    + "\n\n This is a notification about weather.");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
