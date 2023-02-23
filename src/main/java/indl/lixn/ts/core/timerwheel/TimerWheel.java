package indl.lixn.ts.core.timerwheel;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;

import java.io.Serializable;
import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 09:51
 **/
public interface TimerWheel extends Serializable {

    void start() throws TsException;

    void close() throws TsException;

    void pause() throws TsException;

    boolean isRunning() throws TsException;

    boolean isClosed() throws TsException;

    boolean isPaused() throws TsException;

    void onError() throws TsException;

    void addJob(Job job) throws TsException;

    void movePointer() throws TsException;

    List<Job> allJobs() throws TsException;

    List<Job> currentJobs() throws TsException;
    
    String getIdString() throws TsException;

    TimerWheel getHigherLayer();

    TimerWheel getLowerLayer();

    void setLowerLayer(TimerWheel lower);

    void setHigherLayer(TimerWheel higher);
}
