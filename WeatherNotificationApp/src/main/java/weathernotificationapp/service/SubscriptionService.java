package weathernotificationapp.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weathernotificationapp.dao.SubscriptionDAO;
import weathernotificationapp.entity.SubscriptionEntity;

import javax.faces.bean.ManagedBean;

import java.util.*;


@Transactional(readOnly = true) // todo delete this?
@ManagedBean(name = "subscriptionService")
@Component
public class SubscriptionService {

    SubscriptionDAO subscriptionDAO;

    MailService mailService = new MailService();

    @Transactional(readOnly = false)
    public void createSubscription(String email, String city, double temperature) {
        getSubscriptionDAO().createSubscription(email, city, temperature);
    }

    public boolean isAlreadySubscribed(String email, String city) {
        if(getSubscriptionDAO().isDuplicateSubscription(email, city)) {
            return false;
        }
        else return true;
    }

    public List<SubscriptionEntity> findAll() {
        return subscriptionDAO.findAll();
    }

    public SubscriptionDAO getSubscriptionDAO() {
        return subscriptionDAO;
    }

    public void setSubscriptionDAO(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public String testSendMail() {
        mailService.testMail();
        return "mailSent";
    }

    @Transactional(readOnly = false)
    public void updateSubscription(String email, String city, double temperature) {
        getSubscriptionDAO().updateSubscription(email, city, temperature);
    }

    @Transactional(readOnly = false)
    public void setNotification(SubscriptionEntity s) {
        getSubscriptionDAO().setNotificationForSubscription(s);
    }

    @Transactional(readOnly = false)
    public void clearNotification(SubscriptionEntity s) {
        getSubscriptionDAO().clearNotificationForSubscription(s);
    }
}
