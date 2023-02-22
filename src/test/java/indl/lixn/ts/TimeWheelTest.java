package indl.lixn.ts;

import indl.lixn.ts.timerwheel.JobExecutionWheel;
import indl.lixn.ts.timerwheel.MJob;
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
//                    System.out.println("Child JobContent >>> now Running at time : " + TimeUtils.currentTimeAsSecond());
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
//                return TimeUtils.currentTimeAsSecond() + 3;
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
//                    System.out.println("Upper JobContent >>> now Running at time : " + TimeUtils.currentTimeAsSecond());
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
//                return TimeUtils.currentTimeAsSecond();
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
    public void test_isWheelRunNormally() throws Exception {
        // 0
        JobExecutionWheel wheel = new JobExecutionWheel();

        // 1
        JobExecutionWheel upperWheel = new JobExecutionWheel(60, 1, TimeUnit.MINUTES);
        wheel.setUpperLayer(upperWheel);

        wheel.addJob(new MJob(30)); // 0 -
        wheel.addJob(new MJob(90));
        wheel.addJob(new MJob(60));
        wheel.addJob(new MJob(10));

        wheel.startScanner();

        TimeUnit.SECONDS.sleep(120);
    }
}
