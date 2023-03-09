package indl.tswheel;

import indl.lixn.tscommon.exception.TsException;
import indl.lixn.tscommon.job.Job;

import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:21
 **/
public interface TimerWheel {

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

    TimerWheel getUpper();

    TimerWheel getLower();

    void setLower(TimerWheel lower);

    void setUpper(TimerWheel upper);

}
