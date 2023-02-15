package indl.lixn.ts;

import indl.lixn.ts.job.component.Id;
import indl.lixn.ts.util.timewheel.TimeWheel;
import indl.lixn.ts.util.timewheel.TimeWheelNode;

import java.util.concurrent.ExecutorService;

/**
 * @author lixn
 * @description
 * @date 2023/02/15 16:20
 **/
public class DefaultTimeWheel implements TimeWheel {

    private static final int DEFAULT_CAPACITY = 64;

    private final Id timeWheelId;

    private final TimeWheelNode[] container;

    private final ExecutorService threadPool;

    private int bufferSize;

    private DefaultTimeWheel(ExecutorService threadPool) {
        this(threadPool, DEFAULT_CAPACITY);
    }

    private DefaultTimeWheel(ExecutorService threadPool, int bufferSize) {
        this.timeWheelId = () -> {
            // TODO IdDispatcher#getId() 获取分布式唯一Id
            return "";
        };
        this.threadPool = threadPool;
        this.bufferSize = bufferSize;
        this.container = new TimeWheelNode[bufferSize];
    }

}
