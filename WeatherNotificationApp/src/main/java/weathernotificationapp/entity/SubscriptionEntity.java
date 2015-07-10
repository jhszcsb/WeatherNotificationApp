package weathernotificationapp.entity;

import javax.persistence.*;

@Entity(name="subscription")
@Table(name="SUBSCRIPTION")
public class SubscriptionEntity {

    private int id;
    private String email;
    private String city;
    private double temperature;
    private int notification;

    @Id
    @Column(name="ID", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    @Column(name="CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name="TEMPERATURE")
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Column(name="NOTIFICATION")
    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }
}
