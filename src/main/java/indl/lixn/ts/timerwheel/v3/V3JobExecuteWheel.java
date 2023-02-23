package indl.lixn.ts.timerwheel.v3;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 10:33
 **/
public class V3JobExecuteWheel extends V3BaseTimerWheel {

    private static final long serialVersionUID = 1_0L;

    private static final Logger log = LoggerFactory.getLogger(V3JobExecuteWheel.class);

    public V3JobExecuteWheel() {
        super();
    }

    public V3JobExecuteWheel(int tickCount, int durationPerTick, TimeUnit unit) {
        super(tickCount, durationPerTick, unit);
    }

    @Override
    protected void doStart() {
        new Thread(() -> {
            while (isRunning()) {
                try {
                    executeJobs();
                    movePointer();
                } catch (Exception ex) {
                    throw new TsException(getIdString() + "运行时出错", ex);
                }
            }
        }).start();
    }

    private void executeJobs() {
        for (Job job : currentJobs()) {
            job.getExecutor().work(job.getContent());
        }
    }

    @Override
    protected void doClose() {

    }

    @Override
    protected void doPause() {

    }

    @Override
    public void onError() throws TsException {

    }

    @Override
    public void setLowerLayer(V3TimerWheel lower) {
        throw new TsException(getIdString() + "属于任务执行时间轮，不可拥有下层时间轮");
    }

    @Override
    public V3TimerWheel getLowerLayer() {
        throw new TsException(getIdString() + "属于任务执行时间轮，不可拥有下层时间轮");
    }

    @Override
    public void movePointer() throws TsException {
        System.out.println(getIdString() + "当前指针为" + this.pointer);
        this.pointer++;
        if (ranAround()) {
            System.out.println(getIdString() + "执行了一圈");
            if (hasNoHigherLayer()) {
                System.out.println(getIdString() + "不存在上层时间轮");
            } else {
                final V3TimerWheel higher = this.getHigherLayer();
                higher.movePointer();
            }
        }
        ensurePointer();
        try {
            this.unit.sleep(this.durationPerTick);
        } catch (Exception ex) {
            System.out.println(getIdString() + "休眠出错");
        }
    }
}
