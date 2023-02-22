package indl.lixn.ts.timerwheel;

import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.core.job.JobContent;
import indl.lixn.ts.core.job.JobExecutor;
import indl.lixn.ts.core.job.JobTimeConfig;
import indl.lixn.ts.util.TimeUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lixn
 * @description Testing Job Details
 * @date 2023/02/20 11:07
 **/
public class MJob implements Job {

    private static final AtomicInteger jobCounter = new AtomicInteger();

    private final int secondDiff;

    private final Id id;

    public MJob() {
        this(0);
    }

    public MJob(int secondDiff) {
        String idStr = "MJob_" + jobCounter.getAndIncrement();
        this.secondDiff = secondDiff;
        this.id = () -> idStr;
    }

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public JobContent getContent() {
        return () -> {
            int currentSecond = TimeUtils.currentTimeAsSecond();
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
    public int getExecutionTimeAsSeconds() {
        return TimeUtils.currentTimeAsSecond() + secondDiff;
    }

    @Override
    public int getSubmissionTimeAsSeconds() {
        return 0;
    }

    @Override
    public boolean isPeriodic() {
        return true;
    }

    public int getTimeGapAsSecond() {
        return (this.getExecutionTimeAsSeconds() - this.getSubmissionTimeAsSeconds());
    }
}
