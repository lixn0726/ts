package indl.lixn.ts.common;

import java.util.concurrent.TimeUnit;

/**
 * @author lixn
 * @description
 * @date 2023/02/20 16:09
 **/
public class TimeTuple<Unit, Power> extends Tuple<TimeUnit, Integer> {

    public TimeTuple(TimeUnit unit, Integer power) {
        super(unit, power);
    }

    public TimeUnit getUnit() {
        return this.getLeft();
    }

    public Integer getPower() {
        return this.getRight();
    }

}
