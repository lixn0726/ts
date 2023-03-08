package indl.tswheel.abstracts;

import indl.lixn.tscommon.exception.TsException;
import indl.tswheel.TimerWheel;
import indl.tswheel.TimerWheelId;
import indl.tswheel.WheelNode;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:28
 **/
@Data
public abstract class AbstractTimerWheel implements TimerWheel {

    private TimerWheelId id;

    private int pointer;

    private WheelNode[] nodeArray;

    private int tickCount;

    private int durationPerTick;

    private TimeUnit timeUnit;

    private boolean start = false;

    private boolean pause = false;

    private boolean stop = false;

    private TimerWheel upperTimerWheel;

    private TimerWheel lowerTimerWheel;

    private int secondsLevelTimeInterval;

    public AbstractTimerWheel() {
    }

    protected abstract void startTimerWheel();

    protected abstract void closeTimerWheel();

    protected abstract void pauseTimerWheel();

    private void initAdditionalProps() {
        if (attributesIllegal()) {
            throw new TsException("初始化参数不规范，请检查");
        }
        this.secondsLevelTimeInterval =
                // TODO 转化为秒级的
                this.tickCount * this.durationPerTick;
    }

    private boolean attributesIllegal() {
        return this.tickCount <= 0
                || this.durationPerTick <= 0
                || this.timeUnit == null;
    }

    @Override
    public void start() throws TsException {
        if (isRunning()) {
            throw new TsException(this.id.getAsString() + "不能被重复开启");
        }
        this.start = true;
        this.stop = false;
        this.pause = false;
    }

    @Override
    public void close() throws TsException {
        if (isClosed()) {
            throw new TsException(this.id.getAsString() + "不能被重复关闭");
        }
        this.stop = true;
        this.start = false;
        this.pause = false;
    }

    @Override
    public void pause() throws TsException {
        if (isClosed()) {
            throw new TsException(this.id.getAsString() + "不能被重复关闭");
        }
        this.stop = true;
        this.start = false;
        this.pause = false;
    }

    @Override
    public boolean isRunning() throws TsException {
        return this.start && !this.pause && !this.stop;
    }

    @Override
    public boolean isClosed() throws TsException {
        return !this.start && this.stop;
    }

    @Override
    public boolean isPaused() throws TsException {
        return !this.stop && this.pause;
    }
}
