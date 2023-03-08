package indl.lixn.ts.core.job;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 16:47
 **/
public interface PeriodicJob extends Job {

    int getRepeatCount();

    int getPeriod();

}
