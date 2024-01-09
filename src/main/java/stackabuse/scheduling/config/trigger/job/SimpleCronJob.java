package stackabuse.scheduling.config.trigger.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@DisallowConcurrentExecution
public class SimpleCronJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(SimpleCronJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();

        // Define a format for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Format and print the current time
        String formattedTime = currentTime.format(formatter);

        log.info("SimpleCronJob Start................" + formattedTime);
//        IntStream.range(0, 10).forEach(i -> {
//            log.info("Counting - {}", i);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                log.error(e.getMessage(), e);
//            }
//        });
//        log.info("SimpleCronJob End................");
    }
}