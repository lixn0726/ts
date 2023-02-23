package indl.lixn.ts.timerwheel;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/22 16:42
 **/
public class HigherTimerWheel extends BaseTimerWheel implements JobStorageTimerWheel, LowerLayer, UpperLayer {

    private TimerWheel upper;

    private TimerWheel lower;

    private final int durationPerTickInSecond;

    private final int maxIntervalInSecond;

    public HigherTimerWheel(int tickCount, int durationPerTick, TimeUnit unit) {
        this(tickCount, durationPerTick, unit, null, null);
    }

    public HigherTimerWheel(int tickCount, int durationPerTick, TimeUnit unit, TimerWheel upper, TimerWheel lower) {
        super(tickCount, durationPerTick, unit);
        this.upper = upper;
        this.lower = lower;
        this.durationPerTickInSecond = TimeUtils.toSecond(getUnit(), getDurationPerTick());
        this.maxIntervalInSecond = TimeUtils.toSecond(getUnit(), getMaxInterval());
    }


    @Override
    public void movePointer() {
        System.out.println(getIdString() + "挪动指针，当前指针为：" + this.pointer);
        final List<Job> retainJobs =
                new ArrayList<>(this.nodeArray[this.pointer].getJobs());
//                Collections.unmodifiableCollection(this.nodeArray[this.pointer].getJobs())
//                this.nodeArray[this.pointer].getJobs()
                ;
        if (!retainJobs.isEmpty()) {
            // TODO ConcurrentModification Exception
            for (Job job : retainJobs) {
                repositionToLowerLayer(job);
            }
        }
        this.pointer++;
        // 跑了一圈
        this.pointer %= this.maxIntervalInSecond;
    }

    @Override
    public void addJob(Job job) throws TsException {
        int diff = (job.getExecutionTimeAsSeconds() - TimeUtils.currentTimeInSecond());
        int nodeIndex = getNodeIndex(diff);
        System.out.println(getId().getAsString() + "添加任务至Index：" + nodeIndex);
        this.nodeArray[nodeIndex].addJob(job);
    }

    @Override
    public List<Job> getExecutableJobs() throws TsException {
        return this.nodeArray[this.pointer].getJobs();
    }

    @Override
    public void doStart() {
    }

    @Override
    public void repositionJobToUpperLayer(Job job) throws TsException {
        if (!hasUpper()) {
            System.out.println(getId().getAsString() + "不存在上层时间轮，且接收到无法存放的Job" + job.getId().getAsString());
            return;
        }
        final TimerWheel upper = this.upper;
        upper.addJob(job);
    }

    @Override
    public void repositionToLowerLayer(Job job) {
        if (hasNoLower()) {
            System.out.println(getId().getAsString() + "没有下层时间轮，无法将任务重新分配");
            return;
        }
        System.out.println(getIdString() + "下发任务给下层");
        final TimerWheel lower = getLower();
        lower.addJob(job);
    }

    private int getMaxInterval() {
        return getDurationPerTick() * getTickCount();
    }

    private void modPointer() {
        this.pointer %= this.tickCount;
    }

    private boolean hasJobRetained() {
        List<Job> jobList = this.nodeArray[this.pointer].getJobs();
        System.out.println(getIdString() + "在" + pointer + "是否有残留的任务：" + jobList.isEmpty());
        return jobList.isEmpty();
    }

    private boolean hasNoLower() {
        return this.lower == null;
    }

    private boolean hasUpper() {
        return this.upper != null;
    }

    private int getNodeIndex(int diff) {
        int index = (diff / durationPerTickInSecond) - 1;
        // 有余数就要往下一个格子放
        if ((diff % durationPerTickInSecond) != 0) {
            index++;
        }
        return index + getPointer();
    }


    public TimerWheel getUpper() {
        return upper;
    }

    public void setUpper(TimerWheel upper) {
        this.upper = upper;
    }

    public TimerWheel getLower() {
        return lower;
    }

    public void setLower(TimerWheel lower) {
        this.lower = lower;
    }
}
