package stackabuse.scheduling.config.trigger.quartz.scheduler;

import org.quartz.SchedulerException;
import stackabuse.scheduling.config.trigger.job.SchedulerJobInfo;

public interface SchedulerFactoryService {

    void scheduleNewJob(SchedulerJobInfo jobInfo);

    void updateScheduleJob(SchedulerJobInfo jobInfo);

    void triggerJob(String name, String group) throws SchedulerException;

    void pauseJob(String name, String group) throws SchedulerException;

    void resumeJob(String name, String group) throws SchedulerException;

    boolean deleteJob(String name, String group) throws SchedulerException;
}
