package weathernotificationapp.dao;

import org.hibernate.SessionFactory;
import weathernotificationapp.model.SubscriptionEntity;

import java.util.List;

/**
 * Created by Csaba on 2015.07.06..
 */
public class SubscriptionDAO implements ISubscriptionDAO{

    private SessionFactory sessionFactory;

    /**
     * Get Hibernate Session Factory
     *
     * @return SessionFactory - Hibernate Session Factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Set Hibernate Session Factory
     *
     * @param SessionFactory - Hibernate Session Factory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void createSubscription(SubscriptionEntity subscription) {
        getSessionFactory().getCurrentSession().
                createSQLQuery("INSERT INTO `SUBSCRIPTION` (`EMAIL`, `CITY`, `TEMPERATURE`) VALUES (:email, :city, :temperature);").
                setParameter("email", subscription.getEmail()).
                setParameter("city", subscription.getCity()).
                setParameter("temperature", subscription.getTemperature()).executeUpdate();
    }

    @Override
    public SubscriptionEntity findByEmail(String email) {
        return (SubscriptionEntity) getSessionFactory().getCurrentSession().createQuery("from subscription s where s.email = :email").setParameter("email", email).uniqueResult();
    }

    @Override
    public List<SubscriptionEntity> findAll() {
        return (List<SubscriptionEntity>) getSessionFactory().getCurrentSession().createQuery("from subscription").list();
    }
}
