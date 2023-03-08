package indl.lixn.ts.core.job.impl;

import indl.lixn.ts.core.job.PeriodicJob;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 20:13
 **/
public class DefaultPeriodicJob extends DefaultJob implements PeriodicJob {

    private int repeatCount;

    private int period;

    private int periodInSecond;

    @Override
    public int getPeriod() {
        return this.periodInSecond;
    }

    @Override
    public int getRepeatCount() {
        return this.repeatCount;
    }
}
