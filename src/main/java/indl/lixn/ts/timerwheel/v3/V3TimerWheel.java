package indl.lixn.ts.timerwheel.v3;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;

import java.io.Serializable;
import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 09:51
 **/
public interface V3TimerWheel extends Serializable {

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

    V3TimerWheel getHigherLayer();

    V3TimerWheel getLowerLayer();

    void setLowerLayer(V3TimerWheel lower);

    void setHigherLayer(V3TimerWheel higher);
}
