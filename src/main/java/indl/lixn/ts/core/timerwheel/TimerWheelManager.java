package indl.lixn.ts.core.timerwheel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 16:38
 **/
public class TimerWheelManager {

    private static final Logger log = LoggerFactory.getLogger(TimerWheelManager.class);

    private static List<TimerWheel> wheels;

    public static void addWheel(TimerWheel wheel) {
        if (wheels.isEmpty()) {
            if (!(wheel instanceof ExecutionWheel)) {
                log.error("第一个时间轮必须可以执行任务");
                return;
            }
        } else {
            if (wheel instanceof ExecutionWheel) {
                log.error("不能存在多于一个的执行任务的时间轮");
                return;
            }
        }
        wheels.add(wheel);
    }

    public static void linkWheels() {

    }

}
