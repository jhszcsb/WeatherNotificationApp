package weathernotificationapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weathernotificationapp.dao.SubscriptionDAO;
import weathernotificationapp.entity.SubscriptionEntity;

import java.util.*;


@Transactional(readOnly = true) // todo delete this?
@Service
public class SubscriptionService {

    @Autowired
    SubscriptionDAO subscriptionDAO;

    @Autowired
    MailService mailService;

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
