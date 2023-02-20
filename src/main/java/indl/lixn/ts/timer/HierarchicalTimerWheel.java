package indl.lixn.ts.timer;

import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.util.TimeUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lixn
 * @description
 * @date 2023/02/17 20:00
 **/
@Data
public abstract class HierarchicalTimerWheel implements TimerWheel {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constants.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static final Map<TimeUnit, TimeUnit> upperByUnit = new HashMap<TimeUnit, TimeUnit>() {{
        put(TimeUnit.SECONDS, TimeUnit.MINUTES);
        put(TimeUnit.MINUTES, TimeUnit.HOURS);
        put(TimeUnit.HOURS, TimeUnit.DAYS);
    }};

    private Id id;

    private AtomicBoolean stop = new AtomicBoolean(true);

    private AtomicBoolean start = new AtomicBoolean(true);

    private AtomicBoolean pause = new AtomicBoolean(false);

    private int pointer;

    private int ticksPerWheel;

    private int tickDuration;

    private TimeUnit unit;

    private final int interval = ticksPerWheel * tickDuration;

    private TimerWheel upper;

    private TimerWheel lower;

    private JobTimerList[] bucket;

    private int bucketSize;

    private static final int DEFAULT_SIZE = 64;

    public HierarchicalTimerWheel(int ticksPerWheel, int tickDuration, TimeUnit unit) {
        this(ticksPerWheel, tickDuration, unit, DEFAULT_SIZE);
    }

    public HierarchicalTimerWheel(int ticksPerWheel, int tickDuration, TimeUnit unit, int bucketSize) {
        this.tickDuration = tickDuration;
        this.ticksPerWheel = ticksPerWheel;
        this.bucketSize = correctBucketSize(bucketSize);
        this.bucket = new JobTimerList[bucketSize];
    }

    // TODO 核心逻辑
    public void addJob(Job job) {
        int cur = TimeUtils.currentTimestampInSeconds();
        int jobTime = job.getTimeAsSeconds();
        int duration = jobTime - cur;
        if (duration > interval) {
            // 直接给到upper处理
            getUpper().addJob(job);
        } else {
            // 直接加到自己的执行列表中
            bucket[duration].addJob(job);
        }
    }

    public void start() {
        if (this.stop.get() && !this.start.get()) {
            start.set(true);
            stop.set(false);
            scan();
        }
    }

    private void scan() {
    }

    public void movePointer() {
    }

    private void placeToUpper(Job job) {

    }

    private void placeToLower(Job job) {

    }

    private int correctBucketSize(int origin) {
        return origin;
    }

    // TODO 层级时间轮降级逻辑
    private void relocate() {

    }

    public TimerWheel getUpper() {
        if (upper == null) {
            // construct upper timer wheel
            // assign to upper
            System.out.println("Upper is null. Construct legal Upper TimerWheel");
        }
        return this.upper;
    }

    private class JobScanHandler implements Runnable {

        @Override
        public void run() {
            while (!stop.get() && start.get()) {
                JobTimerList list = bucket[pointer];
                // 这里只会有取出来执行。因为任务的reLocate
                list.batchExecute();
                movePointer();
                try {
                    unit.sleep(tickDuration);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


}
