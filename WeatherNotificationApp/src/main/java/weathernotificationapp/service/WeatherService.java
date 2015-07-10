package weathernotificationapp.service;

import org.json.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.faces.bean.ManagedBean;

@ManagedBean    // managedbean because test page uses it
public class WeatherService {

    public static final String WEATHER_API_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String TEST_CITY = "Budapest";
    public static final String MAIN_ELEMENT = "main";
    public static final String TEMP_ELEMENT = "temp";
    public static final double KELVIN = 273.15;

    public double readWeatherTest() {
        return readWeatherForCity(TEST_CITY);
    }

    public boolean isRealCity(String city) {
        JSONObject json = performAPIRequest(city);
        try {
            JSONObject mainElement = (JSONObject) json.get(MAIN_ELEMENT);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    public double getCurrentTemperatureForCity(String city) {
        return readWeatherForCity(city);
    }

    private double readWeatherForCity(String city) {
        JSONObject json = performAPIRequest(city);
        JSONObject mainElement = (JSONObject) json.get(MAIN_ELEMENT);
        String temp = mainElement.get(TEMP_ELEMENT).toString();
        double tempDouble = Double.parseDouble(temp);
        double tempInCelsius = tempDouble - KELVIN;
        return tempInCelsius;
    }

    private JSONObject performAPIRequest(String city) {
        String url = WEATHER_API_BASE_URL + city;
        RestTemplate restTemplate = new RestTemplate(); // todo dependency injection
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result = restTemplate.getForObject(url, String.class, "SpringSource");
        JSONObject json = new JSONObject(result);
        return json;
    }
}
