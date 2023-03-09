package indl.tswheel.abstracts;

import indl.lixn.tscommon.exception.TsException;
import indl.tswheel.TimerWheel;
import indl.tswheel.TimerWheelId;
import indl.tswheel.WheelNode;
import indl.tswheel.enums.TimeUnitDimension;
import indl.tswheel.impl.GenericWheelNode;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:28
 **/
@Data
public abstract class AbstractTimerWheel implements TimerWheel {

    private TimerWheelId id;

    private int pointer;

    private WheelNode[] nodeArray;

    private int tickCount;

    private int durationPerTick;

    private TimeUnit timeUnit;

    private boolean start = false;

    private boolean pause = false;

    private boolean stop = false;

    private TimerWheel upperTimerWheel;

    private TimerWheel lowerTimerWheel;

    private int secondsLevelTimeInterval;

    private int secondsLeverDuration;

    public AbstractTimerWheel() {
        this(60);
    }

    public AbstractTimerWheel(int arraySize) {
        this(arraySize, 60, 1, TimeUnit.SECONDS);
    }

    public AbstractTimerWheel(int tickCount, int durationPerTick, TimeUnit timeUnit) {
        this(60, tickCount, durationPerTick, timeUnit);
    }

    public AbstractTimerWheel(int arraySize, int tickCount, int durationPerTick, TimeUnit timeUnit) {
        this.tickCount = tickCount;
        this.durationPerTick = durationPerTick;
        this.timeUnit = timeUnit;
        initAdditionalProps(arraySize);
    }

    protected abstract void startTimerWheel();

    protected abstract void closeTimerWheel();

    protected abstract void pauseTimerWheel();

    protected boolean wentAround() {
        return this.pointer == this.tickCount;
    }

    protected void ensurePointer() {
        this.pointer %= this.tickCount;
    }

    protected boolean hasUpper() {
        return this.upperTimerWheel != null;
    }

    protected boolean hasLower() {
        return this.lowerTimerWheel != null;
    }

    protected int getTimeIntervalInSeconds() {
        return this.secondsLevelTimeInterval;
    }

    private void initAdditionalProps(int arraySize) {
        if (attributesIllegal()) {
            throw new TsException("初始化参数不规范，请检查");
        }
        this.secondsLeverDuration =
                this.durationPerTick * TimeUnitDimension.getScalesToSecond(this.timeUnit);
        this.secondsLevelTimeInterval =
                this.tickCount * secondsLeverDuration;
        this.nodeArray = new WheelNode[arraySize];
        for (int i = 0; i < arraySize; i++) {
            nodeArray[i] = new GenericWheelNode();
        }
    }

    private boolean attributesIllegal() {
        return this.tickCount <= 0
                || this.durationPerTick <= 0
                || this.timeUnit == null;
    }

    @Override
    public String getIdString() throws TsException {
        return this.id.getAsString();
    }

    @Override
    public void start() throws TsException {
        if (isRunning()) {
            throw new TsException(this.id.getAsString() + "不能被重复开启");
        }
        this.start = true;
        this.stop = false;
        this.pause = false;
        startTimerWheel();
    }

    @Override
    public void close() throws TsException {
        if (isClosed()) {
            throw new TsException(this.id.getAsString() + "不能被重复关闭");
        }
        this.stop = true;
        this.start = false;
        this.pause = false;
        closeTimerWheel();
    }

    @Override
    public void pause() throws TsException {
        if (isClosed()) {
            throw new TsException(this.id.getAsString() + "不能被重复关闭");
        }
        this.stop = true;
        this.start = false;
        this.pause = false;
        pauseTimerWheel();
    }

    @Override
    public boolean isRunning() throws TsException {
        return this.start && !this.pause && !this.stop;
    }

    @Override
    public boolean isClosed() throws TsException {
        return !this.start && this.stop;
    }

    @Override
    public boolean isPaused() throws TsException {
        return !this.stop && this.pause;
    }

    @Override
    public TimerWheel getUpper() {
        return this.upperTimerWheel;
    }

    @Override
    public TimerWheel getLower() {
        return this.lowerTimerWheel;
    }

    @Override
    public void setLower(TimerWheel lower) {
        this.lowerTimerWheel = lower;
    }

    @Override
    public void setUpper(TimerWheel upper) {
        this.upperTimerWheel = upper;
    }
}
