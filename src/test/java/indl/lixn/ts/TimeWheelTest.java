package indl.lixn.ts;

import indl.lixn.ts.timerwheel.MJob;
import indl.lixn.ts.timerwheel.BottomTimerWheel;
import indl.lixn.ts.timerwheel.HigherTimerWheel;
import indl.lixn.ts.timerwheel.v3.V3BaseTimerWheel;
import indl.lixn.ts.timerwheel.v3.V3JobExecuteWheel;
import indl.lixn.ts.timerwheel.v3.V3JobStoreWheel;
import indl.lixn.ts.timerwheel.v3.V3TimerWheel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/17 15:27
 **/
public class TimeWheelTest {

    @Test
    public void test_hierarchicalTimerWheelRunning() throws Exception {
        // execute per 1 minute
//        int tickCount = 10;
//        int tickDuration = 1;
//        TimeUnit unit = TimeUnit.SECONDS;
//
//        Job job = new Job() {
//            @Override
//            public Id getId() {
//                return () -> "Child";
//            }
//
//            @Override
//            public JobContent getContent() {
//                return () -> {
//                    System.out.println("Child JobContent >>> now Running at time : " + TimeUtils.currentTimeInSecond());
//                };
//            }
//
//            @Override
//            public JobExecutor getExecutor() {
//                return (JobContent::execute);
//            }
//
//            @Override
//            public JobTimeConfig getExecutionTimeConfig() {
//                return null;
//            }
//
//            @Override
//            public int getPriority() {
//                return 0;
//            }
//
//            @Override
//            public long getTimeAsMillis() {
//                return 0;
//            }
//
//            @Override
//            public int getExecutionTimeAsSeconds() {
//                return TimeUtils.currentTimeInSecond() + 3;
//            }
//
//            @Override
//            public boolean isPeriodic() {
//                return true;
//            }
//        };
//
//        Job upperJob = new Job() {
//            @Override
//            public Id getId() {
//                return () -> "Upper";
//            }
//
//            @Override
//            public JobContent getContent() {
//                return () -> {
//                    System.out.println("Upper JobContent >>> now Running at time : " + TimeUtils.currentTimeInSecond());
//                };
//            }
//
//            @Override
//            public JobExecutor getExecutor() {
//                return (JobContent::execute);
//            }
//
//            @Override
//            public JobTimeConfig getExecutionTimeConfig() {
//                return null;
//            }
//
//            @Override
//            public int getPriority() {
//                return 0;
//            }
//
//            @Override
//            public long getTimeAsMillis() {
//                return 0;
//            }
//
//            @Override
//            public int getExecutionTimeAsSeconds() {
//                return TimeUtils.currentTimeInSecond();
//            }
//
//            @Override
//            public boolean isPeriodic() {
//                return true;
//            }
//        };
//
//        // execute per 2 minutes
//        int upperTickCount = 2;
//        int upperTickDuration = 1;
//        CommonTimerWheel upper = new CommonTimerWheel(upperTickCount, upperTickDuration, unit);
//        upper.addJob(upperJob);
//
//        CommonTimerWheel wheel = new CommonTimerWheel(tickCount, tickDuration, unit, upper);
//        wheel.addJob(job);
//        wheel.start();
//
//
//        TimeUnit.MINUTES.sleep(2);
    }

    @Test
    public void test_v2TimerWheel() throws Exception {
        BottomTimerWheel wheel = new BottomTimerWheel(60, 1, TimeUnit.SECONDS, null);

        HigherTimerWheel higher = new HigherTimerWheel(60, 1, TimeUnit.MINUTES,
                null, wheel);

        wheel.setUpper(higher);
        higher.setLower(wheel);

        wheel.start();
        higher.start();
        wheel.addJob(new MJob(60));
        wheel.addJob(new MJob(70));

        TimeUnit.SECONDS.sleep(900);

    }

    @Test
    public void test_v3TimerWheel() throws Exception {
        V3TimerWheel bottom = new V3JobExecuteWheel(60, 1, TimeUnit.SECONDS);

        V3TimerWheel higher = new V3JobStoreWheel(60, 1, TimeUnit.MINUTES);

        bottom.setHigherLayer(higher);
        higher.setLowerLayer(bottom);


        bottom.start();
//        TimeUnit.SECONDS.sleep(5);
        bottom.addJob(new MJob(60));

        TimeUnit.SECONDS.sleep(600);

    }
}
