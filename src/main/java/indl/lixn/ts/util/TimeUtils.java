package indl.lixn.ts.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/16 15:37
 **/
public class TimeUtils {

    private static final Map<String, TimeZone> timeZoneById = new ConcurrentHashMap<>();

    private static final TimeZone timeZone = TimeZone.getDefault();

    private static final Map<TimeUnit, Integer> scaleByUnit = new HashMap<>();

    static {
        scaleByUnit.put(TimeUnit.MINUTES, 60);
        scaleByUnit.put(TimeUnit.HOURS, 60 * 60);
        scaleByUnit.put(TimeUnit.DAYS, 60 * 60 * 24);
    }

    public static long currentTimestampInMillis() {
        // 3 ways to get TIMESTAMP
        // ------------------------------------------------------------
        // 1. System.currentTimeMillis()
        // 2. Date current = new Date();
        // 3. Calendar.getInstance().getTimeInMillis();
        // ------------------------------------------------------------
        return System.currentTimeMillis();
    }

    public static int currentTimeAsSecond() {
        return (int) (currentTimestampInMillis() / 1000);
    }

    public static TimeZone defaultTimeZone() {
        return timeZone;
    }

    public static TimeZone getTimeZone(String id) {
        TimeZone res = timeZoneById.get(id);
        if (res == null) {
            timeZoneById.put(id, TimeZone.getTimeZone(id));
            res = timeZoneById.get(id);
        }
        return res;
    }

    public static int transformToSecond(int variable, TimeUnit unit) {
        // TODO 有必要用BigDecimal吗
        return (scaleByUnit.get(unit) * variable);
    }

    public static int toSecond(TimeUnit unit, int num, int secondLeft) {
        if (TimeUnit.SECONDS.equals(unit)) {
            return num + secondLeft;
        }
        int scale = scaleByUnit.get(unit);
        return (num * scale) + secondLeft;
    }
}
