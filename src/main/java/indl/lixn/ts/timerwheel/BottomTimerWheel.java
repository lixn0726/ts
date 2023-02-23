package indl.lixn.ts.timerwheel;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.util.TimeUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/22 14:17
 **/
public class BottomTimerWheel extends BaseTimerWheel implements JobExecutionTimerWheel, LowerLayer {

    public BottomTimerWheel(TimerWheel upper) {
        this.upper = upper;
    }

    public BottomTimerWheel(Id id, TimerWheel upper) {
        super(id);
        this.upper = upper;
    }

    public BottomTimerWheel(int tickCount, int durationPerTick, TimeUnit unit, TimerWheel upper) {
        super(tickCount, durationPerTick, unit);
        this.upper = upper;
    }

    public BottomTimerWheel(Id id, int tickCount, int durationPerTick, TimeUnit unit, TimerWheel upper) {
        super(id, tickCount, durationPerTick, unit);
        this.upper = upper;
    }

    public BottomTimerWheel(int tickCount, int durationPerTick, TimeUnit unit, int arraySize, TimerWheel upper) {
        super(tickCount, durationPerTick, unit, arraySize);
        this.upper = upper;
    }

    public BottomTimerWheel(Id id, int tickCount, int durationPerTick, TimeUnit unit, int arraySize, TimerWheel upper) {
        super(id, tickCount, durationPerTick, unit, arraySize);
        this.upper = upper;
    }

    private TimerWheel upper;

    private int durationPerTickInSecond;

    private int maxIntervalInSecond;

    private void initVariables() {
        this.durationPerTickInSecond = TimeUtils.toSecond(getUnit(), getDurationPerTick());
        this.maxIntervalInSecond = TimeUtils.toSecond(getUnit(), getMaxInterval());
    }

    private int getMaxInterval() {
        return getDurationPerTick() * getTickCount();
    }

    @Override
    public void movePointer() {
        if (ranARound()) {
            System.out.println(getId().getAsString() + "走完一圈");
            if (hasUpper()) {
                System.out.println(getId().getAsString() + "移动上层时间轮的指针");
                getUpper().movePointer();
            }
        }
        this.pointer++;
        modPointer();
        System.out.println(getIdString() + "准备度过一次 durationPerTick");
        try {
            getUnit().sleep(durationPerTick);
        } catch (Exception ex) {
            throw new TsException(getId().getAsString() + "休眠时出错", ex.getCause());
        }
    }

    private boolean ranARound() {
        return getPointer() == (getTickCount() - 1);
    }

    private void modPointer() {
        this.pointer %= this.tickCount;
    }

    @Override
    public void doStart() {
        initVariables();
        new Thread(() -> {
            while (isRunning()) {
                try {
                    // 先执行再移指针
                    executeJob();
                    movePointer();
                } catch (Exception ex) {
                    throw new TsException(getId().getAsString() + "", ex);
                }
            }
        }).start();
    }

    private void executeJob() {
        System.out.println("------------------------------------");
        System.out.println(getId().getAsString() + "执行至Index为：" + getPointer());
        List<Job> jobList = getExecutableJobs();
        if (jobList.isEmpty()) {
            return;
        }
        for (Job job : jobList) {
            job.getExecutor().work(job.getContent());
        }
    }

    @Override
    public List<Job> getExecutableJobs() {
        return this.getNodeArray()[getPointer()].getJobs();
    }

    @Override
    public void repositionJobToUpperLayer(Job job) {
        if (!hasUpper()) {
//            throw new TsException(getId().getAsString() + "不存在上层时间轮，且接收到无法存放的Job" + job.getId().getAsString());
            System.out.println(getId().getAsString() + "不存在上层时间轮，且接收到无法存放的Job" + job.getId().getAsString());
            return;
        }
        getUpper().addJob(job);
    }

    @Override
    public void addJob(Job job) throws TsException {
        System.out.println(getIdString() + "装配任务，任务时间为 " + job.getExecutionTimeAsSeconds());
        System.out.println(getIdString() + "装配任务，当前时间为 " + TimeUtils.currentTimeInSecond());
        int diff = (job.getExecutionTimeAsSeconds() - TimeUtils.currentTimeInSecond());
        System.out.println();
        if (exceedMaxInterval(diff)) {
            repositionJobToUpperLayer(job);
            return;
        }
        int nodeIndex = getNodeIndex(diff);
        System.out.println(getId().getAsString() + "添加任务至Index：" + nodeIndex);
        getNodeArray()[nodeIndex].addJob(job);
    }

    private boolean exceedMaxInterval(int diff) {
        return diff >= this.maxIntervalInSecond;
    }

    private int getNodeIndex(int diff) {
        int index = (diff / durationPerTickInSecond) - 1 + getPointer();
        return (index %= this.tickCount);
    }

    public boolean hasUpper() {
        return this.getUpper() != null;
    }

    public TimerWheel getUpper() {
        return this.upper;
    }

    public void setUpper(TimerWheel timerWheel) {
        this.upper = timerWheel;
    }
}
