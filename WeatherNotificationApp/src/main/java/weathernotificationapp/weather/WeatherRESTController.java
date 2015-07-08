package weathernotificationapp.weather;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class WeatherRESTController {

    ObjectMapper objectMapper = new ObjectMapper();

    public double readWeather() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Budapest";
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

    public boolean isRealCity(String city) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result = restTemplate.getForObject(url, String.class, "SpringSource");
        JSONObject json = new JSONObject(result);
        try {
            JSONObject mainElement = (JSONObject) json.get("main");
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }
}
