package weathernotificationapp.dao;

import weathernotificationapp.model.SubscriptionEntity;

import java.util.List;

/**
 * Created by Csaba on 2015.07.06..
 */
public interface ISubscriptionDAO {

    public void createSubscription(String email, String city, double temperature);

    public SubscriptionEntity findByEmail(String email);

    public List<SubscriptionEntity> findAll();

    public boolean isDuplicateSubscription(String email, String city);

    public void updateSubscription(String email, String city, double temperature);
}
