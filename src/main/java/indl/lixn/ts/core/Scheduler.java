package indl.lixn.ts.core;

import java.util.Collection;

/**
 * @author lixn
 * @description
 * @date 2023/02/15 14:13
 **/
public interface Scheduler {

    void start();

    void close();

    Collection<?> getJobs();

    Id getId();

}
