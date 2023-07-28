package stackabuse.scheduling.config.trigger.job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String jobId;
    private String jobName;
    private String jobGroup;
    private String jobStatus;
    private String jobClass;
    private String cronExpression;
    private String jobDescription;
    private String interfaceName;
    private Long repeatTime;
    private Boolean cronJob;
}
