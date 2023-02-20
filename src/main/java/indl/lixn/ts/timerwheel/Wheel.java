package indl.lixn.ts.timerwheel;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.util.TimeUtils;
import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private int bucketSize;

    private int tickDuration;

    private TimeUnit unit;

    private int currentInterval;

    private final int maxInterval = tickDuration * bucketSize;

    private boolean start = false;

    private boolean stop = true;

    private boolean pause = false;

    public Wheel() {
        this.bucketSize = 64;

    }

    public void addJob(Job job) {
        int nodeIndex = getPlacementIndex(job);
        checkNodeNotNull(nodeIndex);
        WheelNode node = nodes[nodeIndex];
        node.addJob(job);
    }

    // TODO 我个人感觉这里的synchronized是有必要的，但是有没有更好的办法去处理呢
    private synchronized void checkNodeNotNull(int nodeIndex) {
        WheelNode node = nodes[nodeIndex];
        if (node == null) {
            nodes[nodeIndex] = new WheelNode();
        }
    }
    
    private class Scanner implements Runnable {
        @Override
        public void run() {
            while (true) {
                int interval = currentInterval;
                List<Job> toDoJobs = nodes[pointer].getJobs();
                if (isBottom()) {
                    executeJobs(toDoJobs);
                } else {
                    repositionJobsToLower(toDoJobs);
                }
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
        // 转了一圈了
        if (this.pointer == maxInterval) {
            pointer %= maxInterval;
            upper.movePointer();
        }
    }

    private int getPlacementIndex(Job job) {
        int gap = job.getTimeAsSeconds() - TimeUtils.currentTimestampInSeconds();
        if (gap > this.maxInterval) {
            // TODO 应该在这里触发吗
            // 该层次的时间轮装不下，需要存到更高层级的去。
            repositionJobsToUpper(job);
        }
        return (gap + this.pointer) % this.maxInterval;
    }

    private void repositionJobsToUpper(Job job) {
        if (hasUpper()) {
            getUpper().addJob(job);
        }
        throw new TsException("Current Highest Level TimerWheel is not enough for Job " +
                "[ " + job.getId().getAsString() + " ] to be placed. " +
                "Please check your setting and then submit again");
    }

    // 放着
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

    public boolean isBottom() {
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
