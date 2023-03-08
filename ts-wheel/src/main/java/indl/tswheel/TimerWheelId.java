package indl.tswheel;

import indl.lixn.tscommon.support.Id;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:26
 **/
public class TimerWheelId implements Id {

    private static final String TIMER_WHEEL = "Ts-TimerWheel-";

    private static final AtomicInteger counter = new AtomicInteger();

    private final String id;

    public TimerWheelId() {
        this.id = TIMER_WHEEL + counter.getAndIncrement();
    }

    @Override
    public String getAsString() {
        return this.id;
    }
}
