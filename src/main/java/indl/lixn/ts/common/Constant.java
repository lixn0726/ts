package indl.lixn.ts.common;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/22 14:28
 **/
public interface Constant {

    int ZERO = 0;

    interface TimerConstant {
        int TIMER_TICK_COUNT = 60;

        int TIMER_DURATION_PER_TICK = 1;

        TimeUnit TIMER_UNIT = TimeUnit.SECONDS;

        int TIMER_ARRAY_SIZE = 60;
    }

}
