package indl.tswheel;

import indl.lixn.tscommon.job.Job;

import java.util.Collection;
import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:21
 **/
public interface WheelNode {

    List<Job> getExecutableJobs();

    void addJob(Job job);

    void addJobs(Collection<Job> jobs);

}
