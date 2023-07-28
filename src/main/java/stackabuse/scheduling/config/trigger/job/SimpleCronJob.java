package stackabuse.scheduling.config.trigger.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.stream.IntStream;

@DisallowConcurrentExecution
public class SimpleCronJob extends QuartzJobBean {

    int count = 0;

    private static final Logger log = LoggerFactory.getLogger(SimpleCronJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        count++;
        log.info("SimpleCronJob Start................" + count);
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