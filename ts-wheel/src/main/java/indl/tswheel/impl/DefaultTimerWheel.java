package indl.tswheel.impl;

import indl.lixn.tscommon.exception.TsException;
import indl.lixn.tscommon.job.Job;
import indl.lixn.tscommon.job.JobTrigger;
import indl.lixn.tscommon.utils.TimeUtils;
import indl.tswheel.TimerWheel;
import indl.tswheel.WheelNode;
import indl.tswheel.abstracts.AbstractTimerWheel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lixn
 * @description
 * @date 2023/03/09 13:41
 **/
public class DefaultTimerWheel extends AbstractTimerWheel {

    private static final Logger log = LoggerFactory.getLogger(DefaultTimerWheel.class);

    private final WheelNode[] nodeArray;

    public DefaultTimerWheel() {
        super();
        this.nodeArray = getNodeArray();
    }

    @Override
    public void onError() throws TsException {

    }

    @Override
    public void addJob(Job job) throws TsException {
        final JobTrigger trigger = job.getTrigger();
        Date triggerTime = trigger.getTriggerTime();
        int triggerSecond = (int)(triggerTime.getTime() / 1000);

        boolean exceed = (triggerSecond - TimeUtils.currentSecond()) >= getTimeIntervalInSeconds();
        if (exceed) {
            getUpper().addJob(job);
        } else {
            final WheelNode curNode = nodeArray[getPointer()];
            curNode.addJob(job);
        }
    }

    @Override
    public TimerWheel getUpper() {
        if (hasUpper()) {
            return super.getUpper();
        } else {
            // TODO
        }
        return null;
    }

    @Override
    public TimerWheel getLower() {
        return super.getLower();
    }

    @Override
    public void movePointer() throws TsException {
        int currentPointer = getPointer();
        currentPointer++;
        setPointer(currentPointer);
        if (wentAround()) {
            log.info(getIdString() + "运转了一圈");
            if (hasUpper()) {
                log.info(getIdString() + "拨动上层时间轮 {}", getUpper().getIdString());
                final TimerWheel upper = getUpper();
                upper.movePointer();
            }
        }
        ensurePointer();
        try {
            getTimeUnit().sleep(getDurationPerTick());
        } catch (InterruptedException ex) {
            throw new TsException(getIdString() + "时钟休眠时出错", ex);
        }
    }

    @Override
    public List<Job> allJobs() throws TsException {
        final List<Job> jobs = new ArrayList<>();
        for (WheelNode node : nodeArray) {
            jobs.addAll(node.getExecutableJobs());
        }
        return jobs;
    }

    @Override
    public List<Job> currentJobs() throws TsException {
        final int currentPointer = getPointer();
        return nodeArray[currentPointer].getExecutableJobs();
    }

    @Override
    protected void startTimerWheel() {

    }

    @Override
    protected void closeTimerWheel() {

    }

    @Override
    protected void pauseTimerWheel() {

    }
}
