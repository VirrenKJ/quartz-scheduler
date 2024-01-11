package stackabuse.scheduling.config.trigger.job;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.stream.IntStream;

public class SimpleJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(SimpleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("SimpleJob Start................");
        log.info("SimpleJob End................");
    }
}
