package indl.lixn.ts.timerwheel.interfaces;

import indl.lixn.ts.core.job.Job;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 22:01
 **/
public interface JobStorageTimerWheel extends TimerWheel {

    void repositionToLowerLayer(Job job);

}
