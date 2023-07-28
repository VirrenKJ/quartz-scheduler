package stackabuse.scheduling.config.trigger.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerJobRepository extends JpaRepository<SchedulerJobInfo, Long> {
    SchedulerJobInfo findByJobName(String jobName);
}
