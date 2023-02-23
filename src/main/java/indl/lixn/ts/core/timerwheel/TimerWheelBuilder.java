package indl.lixn.ts.core.timerwheel;

import indl.lixn.ts.core.timerwheel.impl.JobExecutionWheel;
import indl.lixn.ts.core.timerwheel.impl.JobStorageWheel;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 10:07
 **/
public class TimerWheelBuilder {

    private int tickCount;

    private int durationPerTick;

    private TimeUnit unit;

    private int arraySize;

    private TimerWheel higher;

    private TimerWheel lower;

    private boolean canExecuteJob = false;

    public static TimerWheel ofDefault(boolean canExecuteJob) {
        return canExecuteJob ? new JobExecutionWheel() : new JobStorageWheel();
    }

    public static TimerWheelBuilder builder() {
        return new TimerWheelBuilder();
    }

    public TimerWheelBuilder withTickCount(int tickCount) {
        this.tickCount = tickCount;
        return this;
    }

    public TimerWheelBuilder withDurationPerTick(int durationPerTick) {
        this.durationPerTick = durationPerTick;
        return this;
    }

    public TimerWheelBuilder withUnit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    public TimerWheelBuilder withHigherLayer(TimerWheel higher) {
        this.higher = higher;
        return this;
    }

    public TimerWheelBuilder withLowerLayer(TimerWheel lower) {
        this.lower = lower;
        return this;
    }

    public TimerWheelBuilder executable() {
        this.canExecuteJob = true;
        return this;
    }

    public TimerWheelBuilder onlyForStore() {
        this.canExecuteJob = false;
        return this;
    }

    public TimerWheel build() {
        return canExecuteJob ?
                new JobExecutionWheel(this.tickCount, this.durationPerTick, this.unit, this.higher) :
                new JobStorageWheel(this.tickCount, this.durationPerTick, this.unit, this.higher, this.lower);
    }

    private TimerWheelBuilder() {
    }

}
