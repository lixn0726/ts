package indl.lixn.ts;

import org.junit.jupiter.api.Test;

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
//                    System.out.println("Child JobContent >>> now Running at time : " + TimeUtils.currentTimestampInSeconds());
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
//            public int getTimeAsSeconds() {
//                return TimeUtils.currentTimestampInSeconds() + 3;
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
//                    System.out.println("Upper JobContent >>> now Running at time : " + TimeUtils.currentTimestampInSeconds());
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
//            public int getTimeAsSeconds() {
//                return TimeUtils.currentTimestampInSeconds();
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

}
