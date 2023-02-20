package indl.lixn.ts.core.callback;

/**
 * @author lixn
 * @description
 * @date 2023/02/17 15:12
 **/
public interface JobListener {

    void onJobStart();

    void onJobError();

    void onJobFinish();

    void onJobPause();

}
