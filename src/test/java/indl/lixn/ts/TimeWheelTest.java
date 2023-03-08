package indl.lixn.ts;

import indl.lixn.ts.core.timerwheel.TimerWheelBuilder;
import indl.lixn.ts.core.timerwheel.impl.JobExecutionWheel;
import indl.lixn.ts.core.timerwheel.impl.JobStorageWheel;
import indl.lixn.ts.core.timerwheel.TimerWheel;
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
    public void test_universalLatestVersionTimerWheel() throws Exception {
        TimerWheel bottom = new JobExecutionWheel(60, 1, TimeUnit.SECONDS);

        TimerWheel higher = new JobStorageWheel(60, 1, TimeUnit.MINUTES);

        bottom.setHigherLayer(higher);
        higher.setLowerLayer(bottom);

        bottom.start();
        bottom.addJob(new TestingSimpleJob(60));
        TimeUnit.SECONDS.sleep(5);
        bottom.addJob(new TestingSimpleJob(90));
        bottom.addJob(new TestingSimpleJob(143));
        bottom.addJob(new TestingSimpleJob(23));

        TimeUnit.SECONDS.sleep(600);
    }

    @Test
    public void test_specialTimerWheel() throws Exception {
        TimerWheel bottom = TimerWheelBuilder.builder()
                .withTickCount(10)
                .withDurationPerTick(1)
                .withUnit(TimeUnit.SECONDS)
                .executable()
                .build();

        TimerWheel higher = TimerWheelBuilder.builder()
                .withLowerLayer(bottom)
                .withTickCount(30)
                .withDurationPerTick(1)
                .withUnit(TimeUnit.MINUTES)
                .build();

        bottom.setHigherLayer(higher);

        bottom.start();
        bottom.addJob(new TestingSimpleJob(20));

        TimeUnit.SECONDS.sleep(900);

    }
}
