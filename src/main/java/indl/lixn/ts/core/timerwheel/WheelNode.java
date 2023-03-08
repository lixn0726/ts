package indl.lixn.ts.core.timerwheel;

import indl.lixn.ts.core.job.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<Job> curJobs = this.jobList;
        // TODO 是分多一个时间轮来专门执行可重复任务还是直接遍历呢
        // TODO parallelStream的坑 --- ?
        this.jobList = this.jobList.parallelStream().filter(Job::isPeriodic).collect(Collectors.toList());
        return curJobs;
    }

}
