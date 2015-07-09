package weathernotificationapp.dao;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import weathernotificationapp.model.SubscriptionEntity;

import java.util.List;

/**
 * Created by Csaba on 2015.07.06..
 */
public class SubscriptionDAO implements ISubscriptionDAO{

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void createSubscription(String email, String city, double temperature) {
        getSessionFactory().getCurrentSession().
                createSQLQuery("INSERT INTO `SUBSCRIPTION` (`EMAIL`, `CITY`, `TEMPERATURE`) VALUES (:email, :city, :temperature);").
                setParameter("email", email).
                setParameter("city", city).
                setParameter("temperature", temperature).executeUpdate();
    }

    @Override
    public SubscriptionEntity findByEmail(String email) {
        return (SubscriptionEntity) getSessionFactory().getCurrentSession().createQuery("from subscription s where s.email = :email").setParameter("email", email).uniqueResult();
    }

    @Override
    public List<SubscriptionEntity> findAll() {
        return (List<SubscriptionEntity>) getSessionFactory().getCurrentSession().createQuery("from subscription").list();
    }

    @Override
    public boolean isDuplicateSubscription(String email, String city) {
        SubscriptionEntity duplicate = (SubscriptionEntity) getSessionFactory().getCurrentSession().createQuery("from subscription s where s.email = :email and s.city = :city").setParameter("email", email).setParameter("city", city).uniqueResult();
        if(duplicate == null) return false;
        else return true;
    }

    @Override
    public void updateSubscription(String email, String city, double temperature) {
        getSessionFactory().getCurrentSession().createSQLQuery("UPDATE `SUBSCRIPTION` SET TEMPERATURE = :temperature WHERE EMAIL = :email AND CITY = :city").setParameter("email", email).setParameter("city", city).setParameter("temperature", temperature).executeUpdate();
    }


}
