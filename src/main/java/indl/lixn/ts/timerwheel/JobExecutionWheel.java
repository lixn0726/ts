package indl.lixn.ts.timerwheel;

import indl.lixn.ts.core.Id;
import indl.lixn.ts.core.job.Job;
import indl.lixn.ts.timerwheel.interfaces.TimerWheel;
import indl.lixn.ts.util.TimeUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lixn
 * @description
 * @date 2023/02/21 21:05
 **/
@Data
public class JobExecutionWheel {

    private static final AtomicInteger wheelCounter = new AtomicInteger();

    private Id id;

    private static final Logger log = LoggerFactory.getLogger(JobExecutionWheel.class);

    private int tickCount = 60;

    private int durationPerTick = 1;

    private int durationPerTickAsSecond;

    private TimeUnit unit;

    private int maxIntervalAsSecond;

    private final WheelNode[] nodeArray = new WheelNode[tickCount];

    private JobExecutionWheel upperLayer;

    private JobExecutionWheel lowerLayer;

    private int pointer = 0;

    public JobExecutionWheel() {
        this(null, null);
    }

    public JobExecutionWheel(int tickCount, int durationPerTick, TimeUnit unit) {
        this(tickCount, durationPerTick, unit, null, null);
    }

    public JobExecutionWheel(JobExecutionWheel upperLayer, JobExecutionWheel lowerLayer) {
        this(60, 1, TimeUnit.SECONDS, upperLayer, lowerLayer);
    }

    public JobExecutionWheel(int tickCount, int durationPerTick, TimeUnit unit, JobExecutionWheel upperLayer, JobExecutionWheel lowerLayer) {
        String idStr = "JobExecutionWheel-" + wheelCounter.getAndIncrement();
        this.id = () -> idStr;
        this.lowerLayer = lowerLayer;
        this.upperLayer = upperLayer;
        this.tickCount = tickCount;
        this.durationPerTick = durationPerTick;
        this.unit = unit;
        this.durationPerTickAsSecond = TimeUtils.toSecond(this.unit, durationPerTick, 0);
        this.maxIntervalAsSecond = TimeUtils.toSecond(this.unit, (tickCount * durationPerTick), 0);
        System.out.println(id.getAsString() + " maxIntervalAsSecond为 " + this.maxIntervalAsSecond);
        initNodeArray();
    }


    private void initNodeArray() {
        for (int pos = 0; pos < this.tickCount; pos++) {
            if (nodeArray[pos] == null) {
                nodeArray[pos] = new WheelNode();
            }
        }
    }

    public void addJob(Job job) {
        int timeAsSecond = job.getExecutionTimeAsSeconds();
        int currentSecond = TimeUtils.currentTimeAsSecond();
        int diff = this.pointer + timeAsSecond - currentSecond;
        if (needToReposition(diff)) {
            System.out.println(this.id.getAsString() + " >>> 需要往上层添加Job");
            repositionJobToUpper(job, diff);
            return;
        }
        int nodeIndex = getModIndex(diff);
        System.out.println(this.id.getAsString() + " >>> 添加Job : " + job.getId().getAsString() + "，node为 nodeArray[" + nodeIndex + "]");
        nodeArray[nodeIndex].addJob(job);
    }

    public List<Job> getExecutableJobs(int nodeIndex) {
        return this.nodeArray[nodeIndex].getJobs();
    }

    public void startScanner() {
        System.out.println(this.id.getAsString() + " >>> 开启Scanner");
        new Thread(() -> {
            while (true) {
                for (Job job : getExecutableJobs(this.pointer)) {
                    job.getExecutor().work(job.getContent());
                }
                movePointer();
            }
        }).start();
    }

    public void movePointer() {
        this.pointer++;
        if (ranARound()) {
            if (hasUpperLayer()) {
                getUpperLayer().movePointer(); // .movePointer();
            } else {
                log.error("当前时间轮接收到延时超过 maxInterval的任务，" +
                        "没有上一级时间轮，执行抛弃策略");
            }
        }
        modPointer();
        System.out.println(this.id.getAsString() + " Pointer 移动后：" + this.pointer);
        try {
            // Upper不用sleep
            unit.sleep(durationPerTick);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean ranARound() {
        return this.pointer == this.tickCount;
    }

    private void modPointer() {
        this.pointer %= this.tickCount;
    }

    private int getModIndex(int diff) {
        return diff / this.durationPerTickAsSecond;
    }

    private boolean needToReposition(int diff) {
        return diff >= this.maxIntervalAsSecond;
    }

    private void repositionJobToUpper(Job job, int diff) {
        if (hasUpperLayer()) {
            getUpperLayer().addJob(job);
        }
    }

    public boolean hasUpperLayer() {
        return this.getUpperLayer() != null;
    }
}
