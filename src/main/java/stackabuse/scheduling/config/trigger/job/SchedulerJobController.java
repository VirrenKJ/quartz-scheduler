package stackabuse.scheduling.config.trigger.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduler")
public class SchedulerJobController {

    private final SchedulerJobService schedulerJobService;

    @Autowired
    public SchedulerJobController(SchedulerJobService schedulerJobService) {
        this.schedulerJobService = schedulerJobService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createJobNow(@RequestBody SchedulerJobInfo jobInfo) throws Exception {
        schedulerJobService.saveOrUpdate(jobInfo);
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public boolean startJobNow(@RequestBody SchedulerJobInfo jobInfo) {
        return schedulerJobService.startJobNow(jobInfo);
    }

    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    public boolean pauseJob(@RequestBody SchedulerJobInfo jobInfo) {
        return schedulerJobService.pauseJob(jobInfo);
    }

    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    public boolean resumeJob(@RequestBody SchedulerJobInfo jobInfo) {
        return schedulerJobService.resumeJob(jobInfo);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public boolean deleteJob(@RequestBody SchedulerJobInfo jobInfo) {
        return schedulerJobService.deleteJob(jobInfo);
    }
}
