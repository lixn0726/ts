package indl.lixn.ts.util.ringbuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lixn
 * @description
 * @date 2023/02/15 15:05
 **/
public class RingWheelBuffer {

    private static final int INTERNAL = 64;

    private int bufferSize;

    /**
     * business thread pool
     **/
    private ExecutorService executorService;

    private AtomicInteger taskSize = new AtomicInteger();

    private volatile boolean stop = false;

    private volatile boolean start = true;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();


    private Object[] ringBuffer;

    private int round = 0;

    private int pointer = 0;

    private AtomicInteger tick = new AtomicInteger();


    public RingWheelBuffer(ExecutorService executorService) {
        this.executorService = executorService;
        this.bufferSize = INTERNAL;
        this.ringBuffer = new Object[bufferSize];
    }

    public RingWheelBuffer(ExecutorService executorService, int bufferSize) {
        this.executorService = executorService;
        this.bufferSize = bufferSize;
        this.ringBuffer = new Object[bufferSize];
    }

    public void addTask(Task task) {
        int key = task.getKey();
        Set<Task> tasks = get(key);

        if (tasks != null) {
            int cycleNum = cycleNum(key, bufferSize);
            task.setCycleNum(cycleNum);
            tasks.add(task);
        } else {
            int index = mod(key, bufferSize);
            int cycleNum = cycleNum(key, bufferSize);
            task.setCycleNum(index);
            task.setCycleNum(cycleNum);

            Set<Task> set = new HashSet<>();
            set.add(task);
            put(key, set);
        }
    }

    private Set<Task> get(int key) {
        int index = mod(key, bufferSize);
        return (Set<Task>) ringBuffer[index];
    }

    private void put(int key, Set<Task> tasks) {
        int index = mod(key, bufferSize);
        ringBuffer[index] = tasks;
    }

    private int mod(int target, int mod) {
        target += tick.get();
        // equals target % mod
        return target & (mod - 1);
    }

    private int cycleNum(int target, int mod) {
        return target >> Integer.bitCount(mod - 1);
    }

    public void start() {
        if (!start) {
            System.out.println("delay task is starting");
            start = true;
        }
    }

    private int ensurePowerOf2(int internal) {
        int n = internal - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= INTERNAL) ? INTERNAL : n + 1;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public abstract static class Task extends Thread {

        // 时间轮的圈数
        private int cycleNum;

        // 延时时间
        private int key;

        @Override
        public void run() {
        }


    }

    private class TriggerJob implements Runnable {
        @Override
        public void run() {
            int index = 0;
            while (!stop) {
                Set<Task> tasks = remove(index);
            }
        }
    }

    private Set<Task> remove(int key) {
        Set<Task> tempTask = new HashSet<>();
        Set<Task> result = new HashSet<>();

        Set<Task> tasks = (Set<Task>) ringBuffer[key];
        if (tasks == null) {
            return result;
        }

        for (Task task : tasks) {
            if (task.getCycleNum() == 0) {
                result.add(task);

                size2Notify();
                tick.incrementAndGet();

            } else {
                // decrement 1 cycle number and update origin data
                task.setCycleNum(task.getCycleNum() - 1);
                tempTask.add(task);
            }
        }

        ringBuffer[key] = tempTask;
        return result;
    }

    private void size2Notify() {
//        try {
//            lock.lock();
//        }
    }

    private boolean powerOf2(int target) {
        if (target < 0) {
            return false;
        }
        int value = target & (target - 1);
        return value == 0;
    }

}
