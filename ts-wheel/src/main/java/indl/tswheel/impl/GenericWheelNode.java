package indl.tswheel.impl;

import indl.lixn.tscommon.job.Job;
import indl.tswheel.WheelNode;

import java.util.Collection;
import java.util.List;

/**
 * @author lixn
 * @description 默认的时间轮节点。只用于存放任务
 * @date 2023/03/09 14:12
 **/
public class GenericWheelNode implements WheelNode {
    @Override
    public List<Job> getExecutableJobs() {
        return null;
    }

    @Override
    public void addJob(Job job) {

    }

    @Override
    public void addJobs(Collection<Job> jobs) {

    }
}
