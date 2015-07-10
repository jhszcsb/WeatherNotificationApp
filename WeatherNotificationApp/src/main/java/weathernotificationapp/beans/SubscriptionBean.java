package weathernotificationapp.beans;

import weathernotificationapp.service.MailService;
import weathernotificationapp.entity.SubscriptionEntity;
import weathernotificationapp.service.SubscriptionService;
import weathernotificationapp.service.WeatherService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ManagedBean(name="subscriptionMB")
@RequestScoped
public class SubscriptionBean {

    public static final String ALREADY_SUBSCRIBED = "Already subscribed to this city!";
    public static final String NO_WEATHER_INFORMATION_AVAILABLE = "There is no weather information for this city!";
    public static final String SUBSCRIBED = "Subscribed!";
    public static final String SUBSCRIPTION_UPDATED = "Subscription updated!";

    @ManagedProperty(value="#{SubscriptionService}")
    SubscriptionService subscriptionService;

    MailService mailService = new MailService();

    WeatherService weatherService = new WeatherService();

    private String email;
    private String city;
    private double temperature;
    private boolean dialogVisible = false;

    public void subscribe() {
        if(!isAlreadySubscribed()) {    // todo cleanup: true/false is inverted
            addMessage(ALREADY_SUBSCRIBED);
            setDialogVisible(true);
            return;
        }
        if(!isRealCity()) {
            addMessage(NO_WEATHER_INFORMATION_AVAILABLE);
            return;
        }
        subscriptionService.createSubscription(getEmail(), getCity(), getTemperature());
        addMessage(SUBSCRIBED);
    }

    private boolean isRealCity() {
        return weatherService.isRealCity(getCity());
    }

    public void updateSubscription() {
        setDialogVisible(false);
        subscriptionService.updateSubscription(getEmail(), getCity(), getTemperature());
        addMessage(SUBSCRIPTION_UPDATED);
    }

    private boolean isAlreadySubscribed() {
        return subscriptionService.isAlreadySubscribed(getEmail(), getCity());
    }

    public void startSchedulerForPeriodicWeatherCheck() {
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

    private void setNotification(SubscriptionEntity s) {
        subscriptionService.setNotification(s);
    }

    public void startSchedulerForMessages() {
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
        // TODO: set the beeper to one tick per day
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 1, TimeUnit.DAYS);
    }

    private boolean isNotificationNecessary(SubscriptionEntity s) {
        if(s.getNotification() == 1) return true;
        else return false;
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

    private double getCurrentTemperatureForCity(String city) {
        return weatherService.getCurrentTemperatureForCity(city);
    }

    private void sendMailNotification(SubscriptionEntity s) {
        mailService.sendMailNotification(s);
    }

    private void clearNotification(SubscriptionEntity s) {
        subscriptionService.clearNotification(s);
    }

    private void addMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(msg, new FacesMessage(msg));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }

    public String backToMainPage() {
        return "main";
    }

    public String navigateToTestServices() {
        return "testServices";
    }

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
}
