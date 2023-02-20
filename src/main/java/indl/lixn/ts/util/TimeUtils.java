package indl.lixn.ts.util;

import java.util.TimeZone;

/**
 * @author lixn
 * @description
 * @date 2023/02/16 15:37
 **/
public class TimeUtils {

    private static final TimeZone timeZone = TimeZone.getDefault();

    public static long currentTimestampInMillis() {
        // 3 ways to get TIMESTAMP
        // ------------------------------------------------------------
        // 1. System.currentTimeMillis()
        // 2. Date current = new Date();
        // 3. Calendar.getInstance().getTimeInMillis();
        // ------------------------------------------------------------
        return System.currentTimeMillis();
    }

    public static int currentTimestampInSeconds() {
        return (int) (currentTimestampInMillis() / 1000);
    }

}
