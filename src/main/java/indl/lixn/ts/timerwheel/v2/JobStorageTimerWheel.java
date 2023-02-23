package indl.lixn.ts.timerwheel.v2;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;

import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 22:01
 **/
public interface JobStorageTimerWheel extends TimerWheel {

    void repositionToLowerLayer(Job job);

    default void repositionJobToUpperLayer(List<Job> jobList) {
        final List<Job> jobs = jobList;
        for (Job job : jobs) {
            repositionJobToUpperLayer(job);
        }
    }

    void repositionJobToUpperLayer(Job job) throws TsException;

}
