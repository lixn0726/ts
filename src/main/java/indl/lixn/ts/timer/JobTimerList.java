package indl.lixn.ts.timer;

import indl.lixn.ts.core.job.Job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lixn
 * @description
 * @date 2023/02/17 11:02
 **/
public final class JobTimerList implements Serializable {

    private List<Job> jobs = new ArrayList<>();

    public void batchExecute() {

    }

    public void addJob(Job job) {
        jobs.add(job);
        System.out.println("JobTimerList >>> add job success. Current jobList size : " + jobs.size());
    }

    public List<Job> getJobs() {
        List<Job> retained = jobs.stream().filter(Job::isPeriodic).collect(Collectors.toList());
        List<Job> old = jobs;
        this.jobs = retained;
        System.out.println("After running once. The jobList size is " + jobs.size());
        return old;
    }

}
