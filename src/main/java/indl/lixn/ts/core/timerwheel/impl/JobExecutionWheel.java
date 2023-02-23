package indl.lixn.ts.core.timerwheel.impl;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.core.timerwheel.ExecutionWheel;
import indl.lixn.ts.core.timerwheel.TimerWheel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 10:33
 **/
public class JobExecutionWheel extends BaseTimerWheel implements ExecutionWheel {

    private static final long serialVersionUID = 1_0L;

    private static final Logger log = LoggerFactory.getLogger(JobExecutionWheel.class);

    public JobExecutionWheel() {
        super();
    }

    public JobExecutionWheel(int tickCount, int durationPerTick, TimeUnit unit) {
        super(tickCount, durationPerTick, unit);
    }

    public JobExecutionWheel(int tickCount, int durationPerTick, TimeUnit unit, TimerWheel higher) {
        this(tickCount, durationPerTick, unit, higher, null);
    }

    public JobExecutionWheel(int tickCount, int durationPerTick, TimeUnit unit, TimerWheel higher, TimerWheel lower) {
        super(tickCount, durationPerTick, unit, higher, null);
    }
    @Override
    protected void doStart() {
        new Thread(() -> {
            while (isRunning()) {
                try {
                    executeJobs();
                    movePointer();
                } catch (Exception ex) {
                    throw new TsException(getIdString() + "运行时出错", ex);
                }
            }
        }).start();
    }

    private void executeJobs() {
        for (Job job : currentJobs()) {
            job.getExecutor().work(job.getContent());
        }
    }

    @Override
    protected void doClose() {

    }

    @Override
    protected void doPause() {

    }

    @Override
    public void onError() throws TsException {

    }

    @Override
    public void setLowerLayer(TimerWheel lower) {
        throw new TsException(getIdString() + "属于任务执行时间轮，不可拥有下层时间轮");
    }

    @Override
    public TimerWheel getLowerLayer() {
        throw new TsException(getIdString() + "属于任务执行时间轮，不可拥有下层时间轮");
    }

    @Override
    public void movePointer() {
        log.info(getIdString() + "当前指针为" + this.pointer);
        this.pointer++;
        if (ranAround()) {
            log.info(getIdString() + "执行了一圈");
            if (hasNoHigherLayer()) {
                log.info(getIdString() + "不存在上层时间轮");
            } else {
                final TimerWheel higher = this.getHigherLayer();
                higher.movePointer();
            }
        }
        ensurePointer();
        try {
            this.unit.sleep(this.durationPerTick);
        } catch (Exception ex) {
            log.info(getIdString() + "休眠出错");
        }
    }
}
