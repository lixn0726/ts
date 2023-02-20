package indl.lixn.ts.timer;

import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;

import java.io.Serializable;

/**
 * @author lixn
 * @description
 * @date 2023/02/17 11:16
 **/
public interface TimerWheel extends Serializable {

    Id getId();

    void start();

    void pause();

    boolean isRunning();

    void movePointer();

    void addJob(Job job);
}
