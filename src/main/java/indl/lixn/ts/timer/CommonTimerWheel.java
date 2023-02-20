package indl.lixn.ts.timer;

import indl.lixn.ts.core.Id;
import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lixn
 * @description
 * @date 2023/02/17 11:00
 **/
@Data
@Deprecated
public class CommonTimerWheel {

    protected Id id;

    protected int pointer;

    // 高层级
    protected TimerWheel upper;

    // 低层级
    protected TimerWheel downer;

    // 相当于bucketSize
    protected int tickCount;

    // 时间间隔
    protected int tickDuration;

    protected TimeUnit unit;

    // 总时间跨度
    protected final int interval = tickCount * tickDuration;

    protected JobTimerList[] buckets;

    protected int jobCount;

    protected int bucketSize;

    protected final AtomicBoolean stop = new AtomicBoolean(true);

    protected final AtomicBoolean start = new AtomicBoolean(false);

    protected final AtomicBoolean pause = new AtomicBoolean(false);

    /*
    当运行至 tickCount * tickDuration时，唤醒upper
     */

    public CommonTimerWheel() {}

    public CommonTimerWheel(int tickCount,
                            int tickDuration,
                            TimeUnit unit) {
        this.tickCount = tickCount;
        this.tickDuration = tickDuration;
        this.unit = unit;
        this.buckets = new JobTimerList[64];
        for (int i = 0; i < 64; i++) {
            buckets[i] = new JobTimerList();
        }
    }

    public CommonTimerWheel(int tickCount,
                            int tickDuration,
                            TimeUnit unit,
                            TimerWheel upper) {
        this.tickCount = tickCount;
        this.tickDuration = tickDuration;
        this.unit = unit;
        this.upper = upper;
        this.buckets = new JobTimerList[64];
        for (int i = 0; i < 64; i++) {
            buckets[i] = new JobTimerList();
        }
    }

//    @Override
//    public Id getId() {
//        return this.id;
//    }
//
//    public void start() {
//        if (stop.get() && !start.get()) {
//            start.set(true);
//            stop.set(false);
//            startScan();
//            return;
//        }
//        throw new TsException("JobTimerWheel is already started");
//    }
//
//    public void pause() {
//        if (isRunning() && !pause.get()) {
//            pause.set(true);
//        }
//
//    }
//
//    public void relieve() {
//        if (isRunning() && pause.get()) {
//            pause.set(false);
//        }
//    }
//
//    public boolean isRunning() {
//        return start.get() && !stop.get();
//    }
//
//    public void addJob(Job job) {
//        int bucketIndex = getModIndex(job.getTimeAsSeconds());
//        JobTimerList list = buckets[bucketIndex];
//        list.addJob(job);
//    }
//
//    private void startScan() {
//        // TODO
//        new Thread(new JobScanner()).start();
//    }
//
//    private void notifyUpper() {
//        TimerWheel wheel = getUpper();
//        if (wheel == null) {
//            return;
//        }
//        if (wheel.isRunning()) {
//            wheel.movePointer();
//        } else {
//            wheel.start();
//        }
//    }
//
////    protected class JobScanner implements Runnable {
////        @Override
////        public void run() {
////            while (!stop.get() && start.get() && !pause.get()) {
////                if (ranAround()) {
////                    notifyUpper();
////                }
////                // TODO 将合适的任务降级
////                System.out.println("JobScanner >>> ready to handle bucket at index : " + pointer);
////                JobTimerList todoList = buckets[pointer];
////                todoList.getJobs().forEach(job -> {
////                    JobExecutor executor = job.getExecutor();
////                    JobContent content = job.getContent();
////                    executor.work(content);
////                });
////                movePointer();
////            }
////        }
////    }
//
//    @Override public void movePointer() {
//        try {
//            unit.sleep(tickDuration);
//            pointer++;
//            if (pointer > tickCount) {
//                pointer %= tickCount;
//            }
////            System.out.println("HierarchicalTimerWheel >>> pointer move on. Current pointer is : " + pointer);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private boolean ranAround() {
//        return (this.pointer > 0)
//        && ((this.pointer % tickCount) == 0);
//    }
//
//    private int getModIndex(int seconds) {
//        int diff = seconds - TimeUtils.currentTimestampInSeconds();
//        int index = (diff + pointer) % tickCount;
//        System.out.println("HierarchicalTimerWheel >>> search for index : " + index);
//        return index;
//    }

}
