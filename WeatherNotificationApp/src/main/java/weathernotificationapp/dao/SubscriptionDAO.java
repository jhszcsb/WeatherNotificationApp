package weathernotificationapp.dao;

import org.hibernate.SessionFactory;
import weathernotificationapp.entity.SubscriptionEntity;

import java.util.List;

public class SubscriptionDAO {

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createSubscription(String email, String city, double temperature) {
        getSessionFactory().getCurrentSession().
                createSQLQuery("INSERT INTO `SUBSCRIPTION` (`EMAIL`, `CITY`, `TEMPERATURE`, `NOTIFICATION`) VALUES (:email, :city, :temperature, 0);").
                setParameter("email", email).
                setParameter("city", city).
                setParameter("temperature", temperature).executeUpdate();
    }

    public SubscriptionEntity findByEmail(String email) {
        return (SubscriptionEntity) getSessionFactory().getCurrentSession().createQuery("from subscription s where s.email = :email").setParameter("email", email).uniqueResult();
    }

    public List<SubscriptionEntity> findAll() {
        return (List<SubscriptionEntity>) getSessionFactory().getCurrentSession().createQuery("from subscription").list();
    }

    public boolean isDuplicateSubscription(String email, String city) {
        SubscriptionEntity duplicate = (SubscriptionEntity) getSessionFactory().getCurrentSession().createQuery("from subscription s where s.email = :email and s.city = :city").setParameter("email", email).setParameter("city", city).uniqueResult();
        if(duplicate == null) return false;
        else return true;
    }

    public void updateSubscription(String email, String city, double temperature) {
        getSessionFactory().getCurrentSession().createSQLQuery("UPDATE `SUBSCRIPTION` SET TEMPERATURE = :temperature WHERE EMAIL = :email AND CITY = :city").setParameter("email", email).setParameter("city", city).setParameter("temperature", temperature).executeUpdate();
    }

    public void setNotificationForSubscription(SubscriptionEntity s) {
        getSessionFactory().getCurrentSession().createSQLQuery("UPDATE `SUBSCRIPTION` SET NOTIFICATION = 1 WHERE EMAIL = :email AND CITY = :city").setParameter("email", s.getEmail()).setParameter("city", s.getCity()).executeUpdate();
    }

    public void clearNotificationForSubscription(SubscriptionEntity s) {
        getSessionFactory().getCurrentSession().createSQLQuery("UPDATE `SUBSCRIPTION` SET NOTIFICATION = 0 WHERE EMAIL = :email AND CITY = :city").setParameter("email", s.getEmail()).setParameter("city", s.getCity()).executeUpdate();
    }
}
