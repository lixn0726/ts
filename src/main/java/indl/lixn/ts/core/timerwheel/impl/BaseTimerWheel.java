package indl.lixn.ts.core.timerwheel.impl;

import indl.lixn.ts.common.IdDispatcher;
import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.core.timerwheel.SerializableTimerWheel;
import indl.lixn.ts.core.timerwheel.TimerWheel;
import indl.lixn.ts.core.timerwheel.WheelNode;
import indl.lixn.ts.common.util.TimeUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static indl.lixn.ts.common.Constant.TimerConstant.*;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 10:04
 **/
@Data
public abstract class BaseTimerWheel implements SerializableTimerWheel {

    private static final Logger log = LoggerFactory.getLogger(BaseTimerWheel.class);

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *      Variables
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    protected Id id;

    protected int pointer;

    protected WheelNode[] nodeArray;

    protected int tickCount;

    protected int durationPerTick;

    protected TimeUnit unit;

    protected int arraySize;

    private boolean start = false;

    private boolean stop = true;

    private boolean pause = false;

    private int maxInterval;

    protected int maxIntervalInSecond;

    protected int durationPerTickInSecond;

    protected TimerWheel higherLayer;

    protected TimerWheel lowerLayer;

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *      Constructor
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    public BaseTimerWheel() {
        this(IdDispatcher.ofWheel(), TIMER_TICK_COUNT, TIMER_DURATION_PER_TICK, TIMER_UNIT);
    }

    public BaseTimerWheel(Id id) {
        this(id, TIMER_TICK_COUNT, TIMER_DURATION_PER_TICK, TIMER_UNIT);
    }

    public BaseTimerWheel(int tickCount, int durationPerTick, TimeUnit unit) {
        this(IdDispatcher.ofWheel(), tickCount, durationPerTick, unit);
    }

    public BaseTimerWheel(Id id, int tickCount, int durationPerTick, TimeUnit unit) {
        this(id, tickCount, durationPerTick, unit, TIMER_ARRAY_SIZE);
    }

    public BaseTimerWheel(Id id, int tickCount, int durationPerTick, TimeUnit unit, int arraySize) {
        this(id, tickCount, durationPerTick, unit, null, null, arraySize);
    }

    public BaseTimerWheel(int tickCount, int durationPerTick, TimeUnit unit, TimerWheel higher, TimerWheel lower) {
        this(IdDispatcher.ofWheel(), tickCount, durationPerTick, unit, higher, lower, TIMER_ARRAY_SIZE);
    }

    public BaseTimerWheel(Id id, int tickCount, int durationPerTick, TimeUnit unit, TimerWheel higher, TimerWheel lower, int arraySize) {
        this.id = id;
        this.tickCount = tickCount;
        this.durationPerTick = durationPerTick;
        this.maxInterval = this.tickCount * this.durationPerTick;
        this.unit = unit;
        this.higherLayer = higher;
        this.lowerLayer = lower;
        this.arraySize = arraySize;
        this.nodeArray = new WheelNode[arraySize];
        convertToSecond();
        initArray();
    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *      Interfaces
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    @Override
    public void addJob(Job job) throws TsException {
        log.info(getIdString() + "添加任务：" + job.getId().getAsString());
        int jobDiff = getJobDiff(job);
        log.info("该任务的预期运行时间与当前的时间间隔为 " + jobDiff);
        if (exceedTimeLimitation(jobDiff)) {
            log.info("当前任务时间间隔超过当前时间轮的最大间隔，需要放到上层时间轮");
            if (hasNoHigherLayer()) {
//                throw new TsException(getIdString() + "不存在上层时间轮，且接收到无法存放的Job" + job.getId().getAsString());
                log.info(getIdString() + "不存在上层时间轮，且接收到无法存放的Job" + job.getId().getAsString());
                return;
            }
            final TimerWheel higher = this.higherLayer;
            higher.addJob(job);
            return;
        }
        int nodeIndex = getNodeIndex(jobDiff);
        log.info(getIdString() + "存放任务：" + job.getId().getAsString() + "到：" + nodeIndex);
        this.nodeArray[nodeIndex].addJob(job);
    }

    private int getNodeIndex(int jobDiff) {
        int originIndex = ((jobDiff / this.durationPerTickInSecond) - 1 + this.pointer);
        log.info("计算出 originIndex为：" + originIndex);
        if ((jobDiff % this.durationPerTickInSecond) != 0) {
            originIndex++;
        }
        return originIndex % this.tickCount;
    }

    @Override
    public TimerWheel getHigherLayer() {
        return this.higherLayer;
    }

    @Override
    public TimerWheel getLowerLayer() {
        return this.lowerLayer;
    }

    @Override
    public void setLowerLayer(TimerWheel lower) {
        this.lowerLayer = lower;
    }

    @Override
    public void setHigherLayer(TimerWheel higher) {
        this.higherLayer = higher;
    }

    @Override
    public String getIdString() throws TsException {
        return this.id.getAsString();
    }

    @Override
    public List<Job> allJobs() throws TsException {
        List<Job> all = new ArrayList<>();
        for (WheelNode node : this.nodeArray) {
            all.addAll(node.getJobs());
        }
        return all;
    }

    @Override
    public List<Job> currentJobs() throws TsException {
        return this.nodeArray[this.pointer].getJobs();
    }

    @Override
    public void start() throws TsException {
        if (isRunning()) {
            throw new TsException(this.id.getAsString() + "不能被重复开启");
        }
        log.info(this.id.getAsString() + "开始转动");
        this.start = true;
        this.stop = false;
        this.pause = false;
        doStart();
    }


    @Override
    public void close() throws TsException {
        if (isClosed()) {
            throw new TsException(this.id.getAsString() + "不能被重复关闭");
        }
        this.stop = true;
        this.start = false;
        this.pause = false;
        doClose();
    }


    @Override
    public void pause() throws TsException {
        if (!isRunning() || isClosed()) {
            throw new TsException(this.id.getAsString() + "");
        }
        if (isPaused()) {
            throw new TsException(this.id.getAsString() + "");
        }
        this.pause = true;
        doPause();
    }

    @Override
    public boolean isRunning() throws TsException {
        return this.start && !this.stop && !this.pause;
    }

    @Override
    public boolean isClosed() throws TsException {
        return !this.start && this.stop;
    }

    @Override
    public boolean isPaused() throws TsException {
        return !this.stop && this.pause;
    }

    protected abstract void doStart();

    protected abstract void doClose();

    protected abstract void doPause();

    public boolean ranAround() {
        return (this.pointer == (this.tickCount));
    }

    public void ensurePointer() {
        this.pointer %= this.tickCount;
    }

    public boolean hasNoHigherLayer() {
        return this.higherLayer == null;
    }

    public boolean hasNoLowerLayer() {
        return this.lowerLayer == null;
    }

    public int getJobDiff(Job job) {
        int jobTime = job.getExecutionTimeAsSeconds();
        log.info(job.getId().getAsString() + "预计执行时间为：" + jobTime);
        int curTime = TimeUtils.currentTimeInSecond();
        log.info("当前时间为：" + curTime);
        return jobTime - curTime;
    }

    public boolean exceedTimeLimitation(int diff) {
        return diff >= this.maxIntervalInSecond;
    }

    private void initArray() {
        for (int pos = 0; pos < this.arraySize; pos++) {
            this.nodeArray[pos] = new WheelNode();
        }
    }

    private void convertToSecond() {
        this.maxIntervalInSecond = TimeUtils.toSecond(this.unit, this.maxInterval);
        this.durationPerTickInSecond = TimeUtils.toSecond(this.unit, this.durationPerTick);
    }

}
