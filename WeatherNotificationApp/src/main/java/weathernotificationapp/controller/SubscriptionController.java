package weathernotificationapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import weathernotificationapp.service.SubscriptionService;
import weathernotificationapp.service.WeatherService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@RequestScoped
@Controller
@Scope("request")
public class SubscriptionController {

    public static final String ALREADY_SUBSCRIBED = "Already subscribed to this city!";
    public static final String NO_WEATHER_INFORMATION_AVAILABLE = "There is no weather information for this city!";
    public static final String SUBSCRIBED = "Subscribed!";
    public static final String SUBSCRIPTION_UPDATED = "Subscription updated!";
    public static final String TEST_PAGE = "testServices";

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    WeatherService weatherService;

    private String email;
    private String city;
    private double temperature;
    private boolean dialogVisible = false;

    public void subscribe() {
        if(!isAlreadySubscribed()) {    // cleanup: true/false is inverted
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

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }

    public String navigateToTestServices() {
        return TEST_PAGE;
    }
}
