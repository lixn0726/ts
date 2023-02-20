package indl.lixn.ts.timerwheel;

import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.core.job.JobContent;
import indl.lixn.ts.core.job.JobExecutor;
import indl.lixn.ts.core.job.JobTimeConfig;
import indl.lixn.ts.util.TimeUtils;

/**
 * @author lixn
 * @description Testing Job Details
 * @date 2023/02/20 11:07
 **/
public class MJob implements Job {


    @Override
    public Id getId() {
        return () -> "MJob Id - First";
    }

    @Override
    public JobContent getContent() {
        return () -> {
            int currentSecond = TimeUtils.currentTimestampInSeconds();
            System.out.println(this.getId().getAsString() + " --- execution at --- " + currentSecond);
        };
    }

    @Override
    public JobExecutor getExecutor() {
        return JobContent::execute;
    }

    @Override
    public JobTimeConfig getExecutionTimeConfig() {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public long getTimeAsMillis() {
        return TimeUtils.currentTimestampInMillis();
    }

    @Override
    public int getTimeAsSeconds() {
        return TimeUtils.currentTimestampInSeconds();
    }

    @Override
    public boolean isPeriodic() {
        return true;
    }
}
