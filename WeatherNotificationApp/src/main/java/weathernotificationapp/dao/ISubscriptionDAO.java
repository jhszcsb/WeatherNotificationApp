package weathernotificationapp.dao;

import weathernotificationapp.model.SubscriptionEntity;

import java.util.List;

/**
 * Created by Csaba on 2015.07.06..
 */
public interface ISubscriptionDAO {

    public void createSubscription(SubscriptionEntity subscription);

    public SubscriptionEntity findByEmail(String email);

    public List<SubscriptionEntity> findAll();
}
