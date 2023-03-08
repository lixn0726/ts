package indl.lixn.ts;

import indl.lixn.ts.core.job.impl.FormattedTimeConfig;
import org.junit.jupiter.api.Test;

/**
 * @author lixn
 * @description
 * @date 2023/02/23 20:15
 **/
public class TimeConfigTest {

    @Test
    public void test_timeConfigTransform() {
        FormattedTimeConfig config = new FormattedTimeConfig("2023-02-23 21:15:50");

        FormattedTimeConfig other = new FormattedTimeConfig("20230224", "yyyyMMdd");

        System.out.println("config :  " + config.transformAsTimestamp());
        System.out.println("other : " + other.transformAsTimestamp());

    }

}
