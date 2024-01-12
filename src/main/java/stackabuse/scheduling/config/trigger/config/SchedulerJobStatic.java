package stackabuse.scheduling.config.trigger.config;

import org.springframework.stereotype.Component;

@Component
public class SchedulerJobStatic {

    int count = 0;

    // * 2 seconds
//    @Scheduled(fixedDelay = 2000)
    public void printMessage() {
        count++;
        System.out.println(count + ": Scheduled task executed from FixedScheduledTask!");
    }
}
