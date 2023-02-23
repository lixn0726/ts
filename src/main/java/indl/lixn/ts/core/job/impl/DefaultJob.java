package indl.lixn.ts.core.job.impl;

import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.*;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 17:17
 **/
public class DefaultJob implements Job {

    private static final boolean NOT_PERIODIC = false;

    private Id id;

    private JobExecutor jobExecutor;

    private JobTimeConfig jobTimeConfig;

    private JobContent jobContent;

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public JobContent getContent() {
        return this.jobContent;
    }

    @Override
    public JobExecutor getExecutor() {
        return this.jobExecutor;
    }

    @Override
    public JobTimeConfig getExecutionTimeConfig() {
        return this.jobTimeConfig;
    }

    @Override
    public boolean isPeriodic() {
        return (this instanceof PeriodicJob);
    }

    @Override
    public int getExecutionTimeAsSeconds() {
        return (int) (this.jobTimeConfig.transformAsTimestamp() / 1000);
    }

    @Override
    public long getTimeAsMillis() {
        return this.jobTimeConfig.transformAsTimestamp();
    }
}
