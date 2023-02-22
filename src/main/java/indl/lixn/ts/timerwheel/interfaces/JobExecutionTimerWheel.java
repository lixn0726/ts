package indl.lixn.ts.timerwheel.interfaces;

import indl.lixn.ts.core.job.Job;

import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 22:01
 **/
public interface JobExecutionTimerWheel extends TimerWheel {

    List<Job> getExecutableJobs();

    void repositionJobsToUpperLayer();

}
