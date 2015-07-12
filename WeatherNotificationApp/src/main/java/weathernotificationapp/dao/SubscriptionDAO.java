package weathernotificationapp.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import weathernotificationapp.entity.SubscriptionEntity;

import java.util.List;

@Repository
public class SubscriptionDAO {

    public static final String CREATE_SUBSCRIPTION_QUERY = "INSERT INTO `SUBSCRIPTION` (`EMAIL`, `CITY`, `TEMPERATURE`, `NOTIFICATION`) VALUES (:email, :city, :temperature, 0);";
    public static final String FIND_ALL_QUERY = "from subscription s where s.email = :email";
    public static final String IS_DUPLICATE_QUERY = "from subscription s where s.email = :email and s.city = :city";
    public static final String UPDATE_SUBSCRIPTION_QUERY = "UPDATE `SUBSCRIPTION` SET TEMPERATURE = :temperature, NOTIFICATION = 0 WHERE EMAIL = :email AND CITY = :city";
    public static final String SET_NOTIFICATION_QUERY = "UPDATE `SUBSCRIPTION` SET NOTIFICATION = 1 WHERE EMAIL = :email AND CITY = :city";
    public static final String CLEAR_NOTIFICATIONS_QUERY = "UPDATE `SUBSCRIPTION` SET NOTIFICATION = 0 WHERE EMAIL = :email AND CITY = :city";

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createSubscription(String email, String city, double temperature) {
        getSessionFactory().getCurrentSession().
                createSQLQuery(CREATE_SUBSCRIPTION_QUERY).
                setParameter("email", email).
                setParameter("city", city).
                setParameter("temperature", temperature).executeUpdate();
    }

    public SubscriptionEntity findByEmail(String email) {
        return (SubscriptionEntity) getSessionFactory().getCurrentSession().createQuery(FIND_ALL_QUERY).setParameter("email", email).uniqueResult();
    }

    public List<SubscriptionEntity> findAll() {
        return (List<SubscriptionEntity>) getSessionFactory().getCurrentSession().createQuery("from subscription").list();
    }

    public boolean isDuplicateSubscription(String email, String city) {
        SubscriptionEntity duplicate = (SubscriptionEntity) getSessionFactory().getCurrentSession().createQuery(IS_DUPLICATE_QUERY).setParameter("email", email).setParameter("city", city).uniqueResult();
        if(duplicate == null) return false;
        else return true;
    }

    public void updateSubscription(String email, String city, double temperature) {
        getSessionFactory().getCurrentSession().createSQLQuery(UPDATE_SUBSCRIPTION_QUERY).setParameter("email", email).setParameter("city", city).setParameter("temperature", temperature).executeUpdate();
    }

    public void setNotificationForSubscription(SubscriptionEntity s) {
        getSessionFactory().getCurrentSession().createSQLQuery(SET_NOTIFICATION_QUERY).setParameter("email", s.getEmail()).setParameter("city", s.getCity()).executeUpdate();
    }

    public void clearNotificationForSubscription(SubscriptionEntity s) {
        getSessionFactory().getCurrentSession().createSQLQuery(CLEAR_NOTIFICATIONS_QUERY).setParameter("email", s.getEmail()).setParameter("city", s.getCity()).executeUpdate();
    }
}
