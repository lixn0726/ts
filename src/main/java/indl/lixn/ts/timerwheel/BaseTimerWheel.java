package indl.lixn.ts.timerwheel;

import indl.lixn.ts.common.exception.TsException;
import indl.lixn.ts.core.Id;
import lombok.Data;

import java.util.concurrent.TimeUnit;

import static indl.lixn.ts.common.Constant.TimerConstant.*;

/**
 * @author lixn
 * @description
 * @date 2023/02/22 14:18
 **/
@Data
public abstract class BaseTimerWheel implements TimerWheel {

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *      Variables
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    protected final Id id;

    protected int pointer;

    protected WheelNode[] nodeArray;

    protected final int tickCount;

    protected final int durationPerTick;

    protected final TimeUnit unit;

    protected final int arraySize;

    private boolean start = false;

    private boolean stop = true;

    private boolean pause = false;

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *      Constructors
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    public BaseTimerWheel() {
        this(IdDispatcher.ofWheel());
    }

    public BaseTimerWheel(Id id) {
        this(id, TIMER_TICK_COUNT, TIMER_DURATION_PER_TICK, TIMER_UNIT);
    }

    public BaseTimerWheel(int tickCount, int durationPerTick, TimeUnit unit) {
        this(IdDispatcher.ofWheel(), tickCount, durationPerTick, unit);
    }

    public BaseTimerWheel(Id id, int tickCount, int durationPerTick, TimeUnit unit) {
        this(id, tickCount, durationPerTick, unit, TIMER_ARRAY_SIZE);
    }

    public BaseTimerWheel(int tickCount, int durationPerTick, TimeUnit unit, int arraySize) {
        this(IdDispatcher.ofWheel(), tickCount, durationPerTick, unit, TIMER_ARRAY_SIZE);
    }

    public BaseTimerWheel(Id id, int tickCount, int durationPerTick, TimeUnit unit, int arraySize) {
        this.id = id;
        this.tickCount = tickCount;
        this.durationPerTick = durationPerTick;
        this.unit = unit;
        this.arraySize = arraySize;
        this.nodeArray = new WheelNode[arraySize];
        initArray();
    }

    @Override
    public String getIdString() {
        return getId().getAsString();
    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *      Interfaces
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
//    protected abstract void movePointer();
//
//    @Override
//    public void movePointer() throws TsException {
//        movePointer();
//        this.pointer++;
//    }

    @Override
    public void start() throws TsException {
        if (isRunning()) {
            throw new TsException(this.id.getAsString() + "不能被重复开启");
        }
        System.out.println(getId().getAsString() + "开始转动");
        this.start = true;
        this.stop = false;
        this.pause = false;
        doStart();
    }

    protected abstract void doStart();

    @Override
    public void stop() throws TsException {
        if (isStopped()) {
            throw new TsException(this.id.getAsString() + "不能被重复关闭");
        }
        this.stop = true;
        this.start = false;
        this.pause = false;
    }

    @Override
    public void pause() throws TsException {
        if (!isRunning() || isStopped()) {
            throw new TsException(this.id.getAsString() + "");
        }
        if (isPaused()) {
            throw new TsException(this.id.getAsString() + "");
        }
        this.pause = true;
    }

    @Override
    public boolean isRunning() throws TsException {
        return this.start && !this.stop && !this.pause;
    }

    @Override
    public boolean isStopped() throws TsException {
        return !this.start && this.stop;
    }

    @Override
    public boolean isPaused() throws TsException {
        return !this.stop && this.pause;
    }

    private void initArray() {
        for (int pos = 0; pos < this.arraySize; pos++) {
            nodeArray[pos] = new WheelNode();
        }
    }
}
