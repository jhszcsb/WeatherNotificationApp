package weathernotificationapp.service;

import org.springframework.transaction.annotation.Transactional;
import weathernotificationapp.dao.ISubscriptionDAO;
import weathernotificationapp.mail.MailService;
import weathernotificationapp.model.SubscriptionEntity;

import javax.faces.bean.ManagedBean;

import java.util.*;


@Transactional(readOnly = true) // todo delete this?
@ManagedBean(name = "subscriptionService")
public class SubscriptionService implements ISubscriptionService {

    ISubscriptionDAO subscriptionDAO;

    @Transactional(readOnly = false)
    @Override
    public void createSubscription(String email, String city, double temperature) {
        getSubscriptionDAO().createSubscription(email, city, temperature);
    }

    @Override
    public boolean isAlreadySubscribed(String email, String city) {
        if(getSubscriptionDAO().isDuplicateSubscription(email, city)) {
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
        MailService mailService = new MailService();    // todo dependency injection
        mailService.testMail();
        return "mailSent";
    }

    @Override
    @Transactional(readOnly = false)
    public void updateSubscription(String email, String city, double temperature) {
        getSubscriptionDAO().updateSubscription(email, city, temperature);
    }
}
