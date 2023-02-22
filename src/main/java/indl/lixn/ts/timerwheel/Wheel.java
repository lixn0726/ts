package indl.lixn.ts.timerwheel;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.util.TimeUtils;
import lombok.Data;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author lixn
 * @description
 * @date 2023/02/20 10:57
 **/
@Data
public class Wheel {

    private Id id;

    private Wheel upper;

    private Wheel lower;

    private int pointer;

    private WheelNode[] nodes;

    // 相当于Netty的ticksPerWheel
    private int bucketSize;

    // 单个bucket的时间间隔
    private int tickDuration;

    private int tickDurationAsSecond;

    // 时间单位
    private TimeUnit unit;

    private int currentInterval;

    private final int maxInterval = tickDuration * bucketSize;

    private int maxIntervalInSecond;

    private boolean start = false;

    private boolean stop = true;

    private boolean pause = false;

    private Thread scanner;

    private ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public Wheel() {
        this(64);
    }

    public Wheel(int bucketSize) {
        this(bucketSize, null, null);
    }

    public Wheel(int bucketSize, Wheel upper, Wheel lower) {
        this.bucketSize = bucketSize;
        this.upper = upper;
        this.lower = lower;
        this.scanner = new Thread(new Scanner());
        this.tickDurationAsSecond = TimeUtils.transformToSecond(this.tickDuration, this.unit);
        initNodeArray();
    }

    private void initNodeArray() {
        this.nodes = new WheelNode[this.bucketSize];
        WheelNode[] nodeArray = this.nodes;
        for (int i = 0; i < nodeArray.length; i++) {
            // 如果不new，那么每个元素默认为null
            this.nodes[i] = new WheelNode();
        }
    }

    public void addJob(Job job) {
        int nodeIndex = getPlacementIndex(job);
        WheelNode node = nodes[nodeIndex];
        node.addJob(job);
    }

    public void addJob(Job job, int diff) {
        // TODO
    }

    private class Scanner implements Runnable {
        @Override
        public void run() {
            try {
                while (isRunning()) {
                    // 先执行，再移动
                    List<Job> executableJobs = nodes[pointer].getJobs();
                    if (isBottomWheel()) {
                        executeJobs(executableJobs);
                    } else {
                        repositionJobsToLower(executableJobs);
                    }
                    movePointer();
                }
            } catch (Exception ex) {
                throw new TsException("Wheel - " + getId().getAsString() + " inner scanner working error.",
                        ex.getCause());
            } finally {
                stop();
            }
        }
    }

    private void executeJobs(List<Job> jobs) {
        for (Job job : jobs) {
            job.getExecutor().work(job.getContent());
        }
    }

    private void movePointer() {
        this.pointer++;
        if (this.pointer == maxInterval) {
            pointer %= maxInterval;
            getUpper().movePointer();
        }
        try {
            Thread.sleep(this.tickDuration);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getPlacementIndex(Job job) {
        int gapAsSecond = job.getExecutionTimeAsSeconds() - TimeUtils.currentTimeAsSecond();
        if (gapAsSecond > this.maxInterval) {
            // 该层次的时间轮装不下，需要存到更高层级的去。
            repositionJobsToUpper(job);
        }
        return (gapAsSecond + this.pointer) % this.maxInterval;
    }

    private void repositionJobsToUpper(Job job) {
        if (hasUpper()) {
            getUpper().addJob(job);
        }
        throw new TsException("Current Highest Level TimerWheel is not enough for Job " +
                "[ " + job.getId().getAsString() + " ] to be placed. " +
                "Please check your setting and then submit again");
    }

    private void repositionJobsToUpper(List<Job> jobs) {
        if (hasUpper()) {
            for (Job job : jobs) {
                repositionJobsToUpper(job);
            }
        }
        throw new TsException("Current Highest Level TimerWheel is not enough for Job " +
                "[ " + jobs + " ] to be placed. " +
                "Please check your setting and then submit again");
    }

    private void repositionJobsToLower(Job job) {
        if (hasLower()) {
            getLower().addJob(job);
        }
        throw new TsException("TimerWheel - " + this.getId().getAsString() + "" +
                "should reposition jobs to lower. But lower TimerWheel is not exist.");
    }

    private void repositionJobsToLower(List<Job> jobs) {
        if (hasLower()) {
            for (Job job : jobs) {
                repositionJobsToLower(job);
            }
        }
        throw new TsException("TimerWheel - " + this.getId().getAsString() + "" +
                "should reposition jobs to lower. But lower TimerWheel is not exist.");
    }

    public boolean isBottomWheel() {
        return this.getLower() == null;
    }

    public boolean hasUpper() {
        return this.getUpper() != null;
    }

    public boolean hasLower() {
        return this.getLower() != null;
    }

    public boolean isRunning() {
        return this.start &&
                !this.stop &&
                !this.pause;
    }

    public boolean isStop() {
        return this.stop &&
                !this.start &&
                !this.pause;
    }

    public boolean isPause() {
        return this.start &&
                this.pause &&
                !this.stop;
    }


    public void start() {
        if (isStop()) {
            this.start = true;
            this.stop = false;
            this.pause = false;
            this.scanner.start();
        }
    }

    public void stop() {
        if (isRunning() || isPause()) {
            this.stop = true;
            this.pause = false;
            this.start = false;
        }
    }

    public void close() {
        this.stop();
    }

    public void pause() {
        if (isRunning()) {
            this.stop = false;
            this.pause = true;
            this.start = true;
        }
    }
}
