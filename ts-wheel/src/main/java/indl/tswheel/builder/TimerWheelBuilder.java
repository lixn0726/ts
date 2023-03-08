package indl.tswheel.builder;

import indl.tswheel.TimerWheel;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:36
 **/
@Data
public class TimerWheelBuilder {

    private int tickCount;

    private int durationPerTick;

    private TimeUnit timeUnit;

    private TimerWheel upperTimerWheel = null;

    private TimerWheel lowerTimerWheel = null;

    public static TimerWheelBuilder builder() {
        return new TimerWheelBuilder();
    }

    public static TimerWheel ofDefault() {
        //
        return null;
    }

    private TimerWheelBuilder() {}

    public TimerWheelBuilder withTickCount(int tickCount) {
        this.tickCount = tickCount;
        return this;
    }

    public TimerWheelBuilder withDurationPerTick(int durationPerTick) {
        this.durationPerTick = durationPerTick;
        return this;
    }

    public TimerWheelBuilder withTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public TimerWheelBuilder withUpper(TimerWheel upperTimerWheel) {
        this.upperTimerWheel = upperTimerWheel;
        return this;
    }

    public TimerWheelBuilder withLower(TimerWheel lowerTimerWheel) {
        this.lowerTimerWheel = lowerTimerWheel;
        return this;
    }

    public TimerWheel build() {
        return null;
    }

}
