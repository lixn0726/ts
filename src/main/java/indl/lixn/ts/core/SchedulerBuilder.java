package indl.lixn.ts.core;

/**
 * @author lixn
 * @description
 * @date 2023/02/16 15:31
 **/
public interface SchedulerBuilder {

    Scheduler ofDefault();

    Scheduler get(Id id);



}
