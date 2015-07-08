package weathernotificationapp.model;

import javax.persistence.*;

/**
 * Created by Csaba on 2015.07.06..
 */
@Entity(name="subscription")
@Table(name="SUBSCRIPTION")
public class SubscriptionEntity {

    private int id;
    private String email;
    private String city;
    private int temperature;

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
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
