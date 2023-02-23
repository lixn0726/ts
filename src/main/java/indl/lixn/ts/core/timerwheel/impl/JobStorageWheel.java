package indl.lixn.ts.core.timerwheel.impl;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.core.timerwheel.StorageWheel;
import indl.lixn.ts.core.timerwheel.TimerWheel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 10:38
 **/
public class JobStorageWheel extends BaseTimerWheel implements StorageWheel {

    private static final long serialVersionUID = 1_1L;

    private static final Logger log = LoggerFactory.getLogger(JobStorageWheel.class);

    public JobStorageWheel() {
        super();
    }

    public JobStorageWheel(int tickCount, int durationPerTick, TimeUnit unit) {
        super(tickCount, durationPerTick, unit);
    }

    public JobStorageWheel(int tickCount, int durationPerTick, TimeUnit unit, TimerWheel higher, TimerWheel lower) {
        super(tickCount, durationPerTick, unit, higher, lower);
    }

    @Override
    public void start() throws TsException {
        throw new TsException(getIdString() + "属于上层时间轮，不可手动启动");
    }

    @Override
    protected void doStart() {
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
    public void addJob(Job job) throws TsException {
        super.addJob(job);
        log.info("JobStoreWheel addJob");
    }

    @Override
    public void movePointer() throws TsException {
        log.info(getIdString() + "当前指针为：" + this.pointer);
        final List<Job> retainJobs = currentJobs();
        if (!retainJobs.isEmpty()) {
            if (hasNoLowerLayer()) {
                log.error(getIdString() + "不存在下层时间轮，但是还有待执行的任务等待分配下去");
                return;
            }
            log.info(getIdString() + "下发任务到下层时间轮");
            final TimerWheel lower = this.getLowerLayer();
            for (Job job : retainJobs) {
                lower.addJob(job);
            }
        }
        this.pointer++;
        ensurePointer();
    }
}
