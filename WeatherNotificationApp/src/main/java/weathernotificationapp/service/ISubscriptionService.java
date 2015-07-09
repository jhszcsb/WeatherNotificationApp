package weathernotificationapp.service;

import weathernotificationapp.model.SubscriptionEntity;

import java.util.List;

/**
 * Created by Csaba on 2015.07.06..
 */
public interface ISubscriptionService {
    public void createSubscription(String email, String city, double temperature);

    public boolean isAlreadySubscribed(String email, String city);

    public List<SubscriptionEntity> findAll();

    public void updateSubscription(String email, String city, double temperature);
}
