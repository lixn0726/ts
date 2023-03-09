package indl.tswheel.support;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lixn
 * @description
 * @date 2023/03/09 22:50
 **/
public class ScannerThreadPool {

    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    public void submit(Runnable scanner) {
        pool.submit(scanner);
    }

    public void stop() {
        pool.shutdown();
    }


}
