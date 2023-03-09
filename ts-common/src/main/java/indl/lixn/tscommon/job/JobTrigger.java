package indl.lixn.tscommon.job;

import java.util.Date;

/**
 * @author lixn
 * @description
 * @date 2023/03/09 20:08
 **/
public interface JobTrigger {

    void setTriggerTime();

    void resetFromNow();

    void setTriggerPeriod();

    int transformToSecond();

    int getTriggerPeriod();

    Date getTriggerTime();

    Date getLastTriggerTime();

    String getTriggerTimeDescription();

}
