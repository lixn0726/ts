package indl.lixn.ts.timerwheel.interfaces;

import indl.lixn.ts.core.job.Job;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 21:33
 **/
public interface TimerWheel {

    void movePointer();

    void addJob(Job job);

    void start();

    void stop();

    void pause();

}
