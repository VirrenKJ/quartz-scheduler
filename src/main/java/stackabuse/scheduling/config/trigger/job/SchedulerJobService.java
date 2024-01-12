package stackabuse.scheduling.config.trigger.job;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import stackabuse.scheduling.config.trigger.config.SchedulerFactoryService;

@Transactional
@Service
public class SchedulerJobService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerJobService.class);

    @Autowired
    private SchedulerFactoryService schedulerFactoryService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private SchedulerJobRepository schedulerJobRepository;

    public void saveOrUpdate(SchedulerJobInfo scheduleJob) throws Exception {
        if (scheduleJob.getCronExpression() != null && !scheduleJob.getCronExpression().isEmpty()) {
            scheduleJob.setJobClass(SimpleCronJob.class.getName());
            scheduleJob.setCronJob(true);
        } else {
            scheduleJob.setJobClass(SimpleJob.class.getName());
            scheduleJob.setCronJob(false);
            scheduleJob.setRepeatTime((long) 1000);
        }
        if (StringUtils.isEmpty(scheduleJob.getJobId())) {
            log.info("Job Info: {}", scheduleJob);
            schedulerFactoryService.scheduleNewJob(scheduleJob);
        } else {
            schedulerFactoryService.updateScheduleJob(scheduleJob);
        }
//        scheduleJob.setJobDescription("i am job number " + scheduleJob.getJobId());
//        scheduleJob.setInterfaceName("interface_" + scheduleJob.getJobId());
        log.info(">>>>> jobName = [" + scheduleJob.getJobName() + "]" + " created.");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() throws Exception {
        System.out.println("hello world, I have just started up");
        SchedulerJobInfo jobInfo = new SchedulerJobInfo();
        jobInfo.setJobName("Dynamic Task");
        jobInfo.setCronExpression("0/2 * * 1/1 * ? *");
        saveOrUpdate(jobInfo);
//        deleteJob(jobInfo);

        SchedulerJobInfo jobInfoSimple = new SchedulerJobInfo();
        jobInfoSimple.setJobName("Simple Task");
        saveOrUpdate(jobInfoSimple);
//        deleteJob(jobInfoSimple);

    }

    public boolean startJobNow(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerJobRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus("SCHEDULED & STARTED");
            schedulerJobRepository.save(getJobInfo);
            schedulerFactoryService.triggerJob(jobInfo.getJobName(), jobInfo.getJobGroup());
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled and started now.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean pauseJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerJobRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus("PAUSED");
            schedulerJobRepository.save(getJobInfo);
            schedulerFactoryService.pauseJob(jobInfo.getJobName(), jobInfo.getJobGroup());
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " paused.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerJobRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus("RESUMED");
            schedulerJobRepository.save(getJobInfo);
            schedulerFactoryService.resumeJob(jobInfo.getJobName(), jobInfo.getJobGroup());
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " resumed.");
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerJobRepository.findByJobName(jobInfo.getJobName());
            schedulerJobRepository.delete(getJobInfo);
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " deleted.");
            return schedulerFactoryService.deleteJob(jobInfo.getJobName(), jobInfo.getJobGroup());
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }
}
