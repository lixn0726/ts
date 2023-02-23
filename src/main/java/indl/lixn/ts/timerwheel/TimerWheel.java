package indl.lixn.ts.timerwheel;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;

import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 21:33
 **/
public interface TimerWheel {

    String getIdString();

    void movePointer() throws TsException;

    void addJob(Job job) throws TsException;

    void start() throws TsException;

    void stop() throws TsException;

    void pause() throws TsException;

    boolean isRunning() throws TsException;

    boolean isStopped() throws TsException;

    boolean isPaused() throws TsException;

    List<Job> getExecutableJobs() throws TsException;

}
