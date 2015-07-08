package weathernotificationapp.controller;

import weathernotificationapp.service.ISubscriptionService;
import weathernotificationapp.service.SubscriptionService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ManagedBean(name = "subscriptionController", eager = true)
@RequestScoped
public class SubscriptionController {

    public String backToMainPage() {
        return "main";
    }

    public String navigateToTestServices() {
        return "testServices";
    }

    public void testSchedule() {
        System.out.println("Scheduler invoked!");

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

