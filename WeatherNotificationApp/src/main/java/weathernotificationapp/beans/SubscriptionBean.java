package weathernotificationapp.beans;

import weathernotificationapp.mail.MailService;
import weathernotificationapp.model.SubscriptionEntity;
import weathernotificationapp.service.ISubscriptionService;
import weathernotificationapp.weather.WeatherRESTController;

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
    ISubscriptionService subscriptionService;

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
        WeatherRESTController weatherRESTController = new WeatherRESTController();  // todo dependency injection
        return weatherRESTController.isRealCity(getCity());
    }

    public void updateSubscription() {
        setDialogVisible(false);
        subscriptionService.updateSubscription(getEmail(), getCity(), getTemperature());
        addMessage(SUBSCRIPTION_UPDATED);
    }

    private boolean isAlreadySubscribed() {
        return subscriptionService.isAlreadySubscribed(getEmail(), getCity());
    }

    public void startSchedulerForMessages() {
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable beeper = new Runnable() {
            public void run() {
                List<SubscriptionEntity> allSubscriptions = subscriptionService.findAll();
                for(SubscriptionEntity s : allSubscriptions) {
                    if(isTemperatureAboveSubscribed(s)) {
                        sendMailNotification(s);
                    }
                }
            }
        };
        // TODO: set the beeper to one tick per day
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 10, 60, TimeUnit.SECONDS);
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
        WeatherRESTController weatherRESTController = new WeatherRESTController();  // todo dependency injection
        return weatherRESTController.getCurrentTemperatureForCity(city);
    }

    private void sendMailNotification(SubscriptionEntity s) {
        MailService mailService = new MailService();    // todo dependency injection
        mailService.sendMailNotification(s);
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

    public ISubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }
}
