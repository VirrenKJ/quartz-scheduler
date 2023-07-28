package stackabuse.scheduling.config.trigger.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import stackabuse.scheduling.config.trigger.JobScheduleCreator;
import stackabuse.scheduling.config.trigger.scheduler.SchedulerService;

import java.util.Date;

@Transactional
@Service
public class SchedulerJobService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerJobService.class);

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private SchedulerJobRepository schedulerJobRepository;

    public void saveOrUpdate(SchedulerJobInfo scheduleJob) throws Exception {
        if (scheduleJob.getCronExpression().length() > 0) {
            scheduleJob.setJobClass(SimpleCronJob.class.getName());
            scheduleJob.setCronJob(true);
        } else {
            scheduleJob.setJobClass(SimpleJob.class.getName());
            scheduleJob.setCronJob(false);
            scheduleJob.setRepeatTime((long) 1);
        }
        if (StringUtils.isEmpty(scheduleJob.getJobId())) {
            log.info("Job Info: {}", scheduleJob);
            schedulerService.scheduleNewJob(scheduleJob);
        } else {
            schedulerService.updateScheduleJob(scheduleJob);
        }
        scheduleJob.setJobDescription("i am job number " + scheduleJob.getJobId());
        scheduleJob.setInterfaceName("interface_" + scheduleJob.getJobId());
        log.info(">>>>> jobName = [" + scheduleJob.getJobName() + "]" + " created.");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() throws Exception {
        System.out.println("hello world, I have just started up");
        SchedulerJobInfo jobInfo = new SchedulerJobInfo();
        jobInfo.setJobName("Dynamic Task");
        jobInfo.setCronExpression("0/2 * * 1/1 * ? *");
        saveOrUpdate(jobInfo);
    }

    public boolean startJobNow(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerJobRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus("SCHEDULED & STARTED");
            schedulerJobRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
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
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
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
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
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
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }
}
