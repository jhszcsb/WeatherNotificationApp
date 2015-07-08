package weathernotificationapp.service;

import weathernotificationapp.model.SubscriptionEntity;

import java.util.List;

/**
 * Created by Csaba on 2015.07.06..
 */
public interface ISubscriptionService {
    public void createSubscription(SubscriptionEntity subscription);

    public boolean isAlreadySubscribed(String email);

    public List<SubscriptionEntity> findAll();
}
