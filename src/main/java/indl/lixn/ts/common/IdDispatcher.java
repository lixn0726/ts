package indl.lixn.ts.common;

import indl.lixn.ts.core.Id;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lixn
 * @description
 * @date 2023/02/22 14:25
 **/
public class IdDispatcher {

    private static final AtomicInteger wheelCounter = new AtomicInteger();

    private static final AtomicInteger jobCounter = new AtomicInteger();

    private static final String wheelPrefix = "TimerWheel_";

    private static final String jobPrefix = "Job_";

    public static Id ofWheel() {
        final String unmodifiableId = wheelPrefix + wheelCounter.getAndIncrement();
        return () -> unmodifiableId;
    }

    public static Id ofJob() {
        final String unmodifiableId = jobPrefix + jobCounter.getAndIncrement();
        return () -> unmodifiableId;
    }
}
