package indl.tswheel.enums;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/03/08 20:48
 **/
@Getter
public enum TimeUnitDimension {

    SECONDS(0, TimeUnit.SECONDS, 60, null),
    MINUTES(1, TimeUnit.MINUTES, 60, TimeUnitDimension.SECONDS),
    HOURS(2, TimeUnit.HOURS, 24, TimeUnitDimension.MINUTES),
    DAYS(3, TimeUnit.DAYS, Integer.MAX_VALUE, TimeUnitDimension.HOURS)
    ;

    private final int num;

    private final TimeUnit timeUnit;

    private final int scale;

    private final TimeUnitDimension lowerUnit;

    TimeUnitDimension(int num, TimeUnit timeUnit, int scale, TimeUnitDimension lowerUnit) {
        this.num = num;
        this.timeUnit = timeUnit;
        this.scale = scale;
        this.lowerUnit = lowerUnit;
    }

    public static int getScalesToSecond(TimeUnit unit) {
        TimeUnitDimension target = null;
        for (TimeUnitDimension tu : TimeUnitDimension.values()) {
            if (unit.equals(tu.timeUnit)) {
                target = tu;
                break;
            }
        }
        int result = 1;
        if (target == null) {
            return result;
        }
        while (target.lowerUnit != null) {
            target = target.lowerUnit;
            result *= target.scale;
        }
        return result;
    }

}
