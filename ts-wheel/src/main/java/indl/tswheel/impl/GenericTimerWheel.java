package indl.tswheel.impl;

import indl.lixn.tscommon.exception.TsException;
import indl.lixn.tscommon.job.Job;
import indl.lixn.tscommon.job.JobTrigger;
import indl.lixn.tscommon.utils.TimeUtils;
import indl.tswheel.TimerWheel;
import indl.tswheel.WheelNode;
import indl.tswheel.abstracts.AbstractTimerWheel;
import indl.tswheel.enums.TimeUnitDimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/09 13:41
 **/
public class GenericTimerWheel extends AbstractTimerWheel {

    private static final Logger log = LoggerFactory.getLogger(GenericTimerWheel.class);

    private final WheelNode[] nodeArray;

    private boolean basic;

    private final Thread innerScanner;


    public GenericTimerWheel() {
        this(true);
    }

    public GenericTimerWheel(boolean basic) {
        this(basic, 60, 1, TimeUnit.SECONDS);
    }

    public GenericTimerWheel(boolean basic, int tickCount, int durationPerTick, TimeUnit timeUnit) {
        super(tickCount, durationPerTick, timeUnit);
        this.basic = basic;
        this.nodeArray = getNodeArray();
        this.innerScanner = new Thread(new JobScanner());
        System.out.println("Get Reference From AbstractTimerWheel --- NodeArray : " + Arrays.asList(this.nodeArray));
    }

    public static void main(String[] args) {
        GenericTimerWheel base = new GenericTimerWheel();
        base.start();
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
        if (!hasUpper()) {
            TimeUnitDimension upperDimension = TimeUnitDimension.getUpperUnit(getTimeUnit());
            if (upperDimension == null) {
                throw new TsException("已到达系统的最大刻度等级，无法添加任务");
            }
            TimerWheel upper = createUpperTimerWheel(upperDimension);
            setUpper(upper);
        }
        return super.getUpper();
    }

    private TimerWheel createUpperTimerWheel(TimeUnitDimension upperDimension) {
        int tickCount = upperDimension.getLowerScale().getScale();
        int durationPerTick = 1;
        TimeUnit timeUnit = upperDimension.getTimeUnit();
        GenericTimerWheel upper = new GenericTimerWheel(false);
        upper.setLower(this);
        setUpper(upper);
        return upper;
    }

    private void getJobsToExecute() {
        final List<Job> executableJobs = currentJobs();
        if (executableJobs.isEmpty()) {
            return;
        }
        for (Job job : executableJobs) {
            job.getContent().perform();
        }
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
    public void startTimerWheel() {
        // TODO 给Thread一个name，并且放到线程池里面去管理
        this.innerScanner.start();
    }

    @Override
    public void closeTimerWheel() {
    }

    @Override
    public void pauseTimerWheel() {

    }

    @Override
    public boolean isBasic() {
        return this.basic;
    }

    private class JobScanner implements Runnable {
        @Override
        public void run() {
            while (isRunning()) {
                getJobsToExecute();
                movePointer();
            }
        }
    }
}
