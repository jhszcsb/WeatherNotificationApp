package weathernotificationapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import weathernotificationapp.service.MailService;
import weathernotificationapp.service.SchedulerService;
import weathernotificationapp.service.WeatherService;

@Controller
public class ManagementController {

    public static final String MAIN_PAGE = "main";

    @Autowired
    SchedulerService schedulerService;

    @Autowired
    MailService mailService;

    @Autowired
    WeatherService weatherService;

    public void startSchedulerForPeriodicWeatherCheck() {
        schedulerService.startWeatherCheck();
    }

    public void startSchedulerForMessages() {
        schedulerService.startMessageSending();
    }

    public void testSchedule() {
        schedulerService.testSchedule();
    }

    public void testSendMail() {
        mailService.testMail();
    }

    public double readWeatherTest() {
        return weatherService.readWeatherTest();
    }

    public String backToMainPage() {
        return MAIN_PAGE;
    }
}
