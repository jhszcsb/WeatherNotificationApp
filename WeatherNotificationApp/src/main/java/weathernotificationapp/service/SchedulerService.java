package weathernotificationapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weathernotificationapp.entity.SubscriptionEntity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class SchedulerService {

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    WeatherService weatherService;

    @Autowired
    MailService mailService;

    public void testSchedule() {
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable beeper = new Runnable() {
            public void run() { System.out.println("beep"); }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 3, 3, TimeUnit.SECONDS);
        /*scheduler.schedule(new Runnable() {
            public void run() {
                beeperHandle.cancel(true);
            }
        }, 3 , TimeUnit.SECONDS);*/
    }

    public void startWeatherCheck() {
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable beeper = new Runnable() {
            public void run() {
                List<SubscriptionEntity> allSubscriptions = subscriptionService.findAll();
                for(SubscriptionEntity s : allSubscriptions) {
                    if(isTemperatureAboveSubscribed(s)) {
                        setNotification(s);
                        System.out.println("setting notification for " + s.getCity() + s.getEmail());
                    }
                }
            }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 1, TimeUnit.HOURS);
    }

    private boolean isTemperatureAboveSubscribed(SubscriptionEntity s) {
        if(getCurrentTemperatureForCity(s.getCity()) >= s.getTemperature()) {
            System.out.println("Temperature in " + s.getCity() + " is above the subscribed value!");
            return true;
        }
        else {
            System.out.println("Temperature in " + s.getCity() + " is NOT above the subscribed value!");
            return false;
        }
    }

    private void setNotification(SubscriptionEntity s) {
        subscriptionService.setNotification(s);
    }

    private double getCurrentTemperatureForCity(String city) {
        return weatherService.getCurrentTemperatureForCity(city);
    }

    public void startMessageSending() {
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable beeper = new Runnable() {
            public void run() {
                List<SubscriptionEntity> allSubscriptions = subscriptionService.findAll();
                for(SubscriptionEntity s : allSubscriptions) {
                    if(isNotificationNecessary(s)) {
                        sendMailNotification(s);
                        clearNotification(s);
                        System.out.println("sending mail for " + s.getCity() + s.getEmail());
                    }
                }
            }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 1, TimeUnit.DAYS);
    }

    private boolean isNotificationNecessary(SubscriptionEntity s) {
        if(s.getNotification() == 1) return true;
        else return false;
    }

    private void sendMailNotification(SubscriptionEntity s) {
        mailService.sendMailNotification(s);
    }

    private void clearNotification(SubscriptionEntity s) {
        subscriptionService.clearNotification(s);
    }

    public String backToMainPage() {
        return "main";
    }
}
