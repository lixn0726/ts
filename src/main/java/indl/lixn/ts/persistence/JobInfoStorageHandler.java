package indl.lixn.ts.persistence;

import indl.lixn.ts.core.job.Job;

/**
 * @author lixn
 * @description
 * @date 2023/02/25 10:25
 **/
public interface JobInfoStorageHandler extends StorageHandler {

    void storeJobInfo(Job job);

}
