package indl.lixn.ts;

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
public class TestingSimpleJob implements Job {

    private static final AtomicInteger jobCounter = new AtomicInteger();

    private final int secondDiff;

    private final int unmodifiableDiff;

    private final Id id;

    public TestingSimpleJob() {
        this(0);
    }

    public TestingSimpleJob(int secondDiff) {
        String idStr = "MJob_" + jobCounter.getAndIncrement();
        this.secondDiff = secondDiff;
        this.unmodifiableDiff = secondDiff + TimeUtils.currentTimeInSecond();
        this.id = () -> idStr;
    }

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public JobContent getContent() {
        return () -> {
            int currentSecond = TimeUtils.currentTimeInSecond();
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
    public long getTimeAsMillis() {
        return TimeUtils.currentTimestampInMillis();
    }

    @Override
    public int getExecutionTimeAsSeconds() {
        return this.unmodifiableDiff;
    }

    @Override
    public boolean isPeriodic() {
        return true;
    }

}
