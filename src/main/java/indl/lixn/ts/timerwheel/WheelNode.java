package indl.lixn.ts.timerwheel;

import indl.lixn.ts.core.job.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/02/20 10:54
 **/
public class WheelNode {

    private List<Job> jobList = new ArrayList<>();

    public WheelNode() {}

    public void addJob(Job job) {
        jobList.add(job);
    }

    public List<Job> getJobs() {
        return this.jobList;
    }

}
