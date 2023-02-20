package indl.lixn.ts.core.job;

import indl.lixn.ts.core.Id;

/**
 * @author lixn
 * @description
 * @date 2023/02/16 14:54
 **/
public interface Job {

    Id getId();

    JobContent getContent();

    JobExecutor getExecutor();

    JobTimeConfig getExecutionTimeConfig();

    int getPriority();

    long getTimeAsMillis();

    int getTimeAsSeconds();

    boolean isPeriodic();
}
