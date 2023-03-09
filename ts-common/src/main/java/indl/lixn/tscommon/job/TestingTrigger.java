package indl.lixn.tscommon.job;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/09 20:28
 **/
public class TestingTrigger implements JobTrigger {

    private Date startTime = null;

    private Date endTime = null;

    private Date lastTriggerTime = null;

    private int triggerPeriod;

    private TimeUnit periodTriggerUnit = null;

    @Override
    public void setTriggerTime() {

    }

    @Override
    public void resetFromNow() {

    }

    @Override
    public void setTriggerPeriod() {

    }

    @Override
    public int transformToSecond() {
        return 0;
    }

    @Override
    public int getTriggerPeriod() {
        return 0;
    }

    @Override
    public Date getTriggerTime() {
        return null;
    }

    @Override
    public Date getLastTriggerTime() {
        return null;
    }

    @Override
    public String getTriggerTimeDescription() {
        return null;
    }
}
