package indl.tswheel.enums;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:48
 **/
public enum TimeUnitDimension {

    SECONDS(0, TimeUnit.SECONDS, 60),
    MINUTES(1, TimeUnit.MINUTES, 60),
    HOURS(2, TimeUnit.HOURS, 24),
    DAYS(3, TimeUnit.DAYS, Integer.MAX_VALUE)
    ;

    private final int num;

    private final TimeUnit timeUnit;

    private final int scale;

    TimeUnitDimension(int num, TimeUnit timeUnit, int scale) {
        this.num = num;
        this.timeUnit = timeUnit;
        this.scale = scale;
    }

    public int getNum() {
        return num;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

}
