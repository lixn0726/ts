package indl.lixn.ts.util.timewheel;

import indl.lixn.ts.job.aggregate.Job;

/**
 * @author lixn
 * @description
 * @date 2023/02/15 16:11
 **/
public final class TimeWheelNode {

    private static final int DEFAULT_ROUND = 0;

    private final String id;

    private final Job job;

    private int round = 0;

    static TimeWheelNode get(Job job) {
        return get(DEFAULT_ROUND, job);
    }

    static TimeWheelNode get(int round, Job job) {
        return new TimeWheelNode(round, job);
    }

    private TimeWheelNode(int round, Job job) {
        id = "";
        this.round = round;
        this.job = job;
    }

    public int getRound() {
        return this.round;
    }

    public void decreaseRound() {
        round--;
    }

    private Job getJob() {
        return this.job;
    }

    private void setRound(int round) {
        this.round = round;
    }

}
