package stackabuse.scheduling.config.trigger.config;

import org.springframework.stereotype.Component;

@Component
public class FixedScheduledTask {

    int count = 0;

    //    @Scheduled(fixedDelay = 2000) // 2 seconds
    public void printMessage() {
        count++;
        System.out.println(count + ": Scheduled task executed from FixedScheduledTask!");
    }
}
