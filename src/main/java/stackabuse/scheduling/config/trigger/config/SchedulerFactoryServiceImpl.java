package stackabuse.scheduling.config.trigger.config;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import stackabuse.scheduling.config.trigger.job.SchedulerJobInfo;
import stackabuse.scheduling.config.trigger.job.SchedulerJobRepository;
import stackabuse.scheduling.config.trigger.quartz.scheduler.SchedulerHelper;

import java.util.Date;

@Service
public class SchedulerFactoryServiceImpl implements SchedulerFactoryService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerFactoryServiceImpl.class);

    @Autowired
    private ApplicationContext context;

    private CronTrigger cronTrigger;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private SchedulerHelper schedulerHelper;

    @Autowired
    private SchedulerJobRepository schedulerJobRepository;

    public void scheduleNewJob(SchedulerJobInfo jobInfo) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass())).withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {
                jobDetail = schedulerHelper.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context, jobInfo.getJobName(), jobInfo.getJobGroup());
                Trigger trigger;
                if (jobInfo.getCronJob()) {
                    trigger = schedulerHelper.createCronTrigger(jobInfo.getJobName(), new Date(), jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                } else {
                    trigger = schedulerHelper.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                }
                scheduler.scheduleJob(jobDetail, trigger);
                jobInfo.setJobStatus("SCHEDULED");
                schedulerJobRepository.save(jobInfo);
                log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");
            } else {
                log.error("scheduleNewJobRequest.jobAlreadyExist");
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateScheduleJob(SchedulerJobInfo jobInfo) {
        Trigger newTrigger;
        if (jobInfo.getCronJob()) {
            newTrigger = cronTrigger;
        } else {
            newTrigger = schedulerHelper.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            jobInfo.setJobStatus("EDITED & SCHEDULED");
            schedulerJobRepository.save(jobInfo);
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " updated and scheduled.");
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void triggerJob(String name, String group) throws SchedulerException {
        schedulerFactoryBean.getScheduler().triggerJob(new JobKey(name, group));
    }

    public void pauseJob(String name, String group) throws SchedulerException {
        schedulerFactoryBean.getScheduler().pauseJob(new JobKey(name, group));
    }

    public void resumeJob(String name, String group) throws SchedulerException {
        schedulerFactoryBean.getScheduler().resumeJob(new JobKey(name, group));
    }

    public boolean deleteJob(String name, String group) throws SchedulerException {
        return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(name, group));
    }
}
