package weathernotificationapp.beans;

import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import weathernotificationapp.model.SubscriptionEntity;
import weathernotificationapp.service.ISubscriptionService;
import weathernotificationapp.service.SubscriptionService;
import weathernotificationapp.weather.WeatherRESTController;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Csaba on 2015.07.07..
 */
@ManagedBean(name="subscriptionMB")
@RequestScoped
public class SubscriptionBean {

    @ManagedProperty(value="#{SubscriptionService}")
    ISubscriptionService subscriptionService;

    private String email;
    private String city;
    private int temperature;

    public void subscribe() {

        System.out.println("SubscriptionController subscribe");

        if(isAlreadySubscribed()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("Already subscribed!", new FacesMessage("Already subscribed!"));
            return;
        }

        SubscriptionEntity sub = new SubscriptionEntity();
        sub.setEmail(getEmail());
        sub.setCity(getCity());
        sub.setTemperature(getTemperature());

        if(!isRealCity()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("There is no weather information for this city!", new FacesMessage("There is no weather information for this city!"));
            return;
        }

        subscriptionService.createSubscription(sub);

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("Subscribed!", new FacesMessage("Subscribed!"));
    }

    private boolean isRealCity() {
        WeatherRESTController weatherRESTController = new WeatherRESTController();
        return weatherRESTController.isRealCity(getCity());
    }

    private boolean isAlreadySubscribed() {
        return subscriptionService.isAlreadySubscribed(getEmail());
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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public ISubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public void testScheduleMessages() {
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable beeper = new Runnable() {
            public void run() {
                List<SubscriptionEntity> allSubscriptions = subscriptionService.findAll();
                for(SubscriptionEntity s : allSubscriptions) {
                    //System.out.println(s.getEmail() + s.getCity() + s.getTemperature());
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
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result = restTemplate.getForObject(url, String.class, "SpringSource");
        JSONObject json = new JSONObject(result);
        JSONObject mainElement = (JSONObject) json.get("main");
        String temp = mainElement.get("temp").toString();
        double tempDouble = Double.parseDouble(temp);
        double tempInCelsius = tempDouble - 273.15;
        return tempInCelsius;
    }

    private void sendMailNotification(SubscriptionEntity s) {
        System.out.println("Email Sent!");

        final String username = "";
        final String password = "";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(""));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s.getEmail()));
            message.setSubject("Weather Notification");
            message.setText(
                    "Dear Subscriber,"
                    + "\n\n This is a notification about weather."
                    + "\n\n The temperature in " + s.getCity()
                    + " is above " + s.getTemperature()
                    + " degrees (Celsius)." );

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
